package com.jbkit.booking.show.api.internal;

import com.jbkit.booking.config.BookingConfigProp;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
class SeatCordonService {
    private final StringRedisTemplate redisTemplate;
    private final BookingConfigProp bookingConfigProp;

    boolean acquireLocks(Set<String> keysToLock, String lockOwnerId) {
        if (keysToLock == null || keysToLock.isEmpty()) {
            return true;
        }
        Map<String, String> keyOwnerMap = keysToLock.stream()
                .collect(Collectors.toMap(key -> key, key -> lockOwnerId));
        Boolean allLocksAcquired = redisTemplate.opsForValue().multiSetIfAbsent(keyOwnerMap);
        if (Boolean.TRUE.equals(allLocksAcquired)) {
            keysToLock.forEach(key -> redisTemplate.expire(key, bookingConfigProp.getMaxHoldMinutes()));
            log.info("Successfully locked keys: {} for owner: {}", keysToLock, lockOwnerId);
            return true;
        } else {
            log.warn("Failed to acquire locks for keys: {}. Some or all keys are already locked.", keysToLock);
            return false;
        }
    }

    void releaseLocks(Set<String> keysToUnlock) {
        if (keysToUnlock == null || keysToUnlock.isEmpty()) {
            return;
        }
        redisTemplate.delete(keysToUnlock);
        System.out.println("Released locks for keys: " + keysToUnlock);
    }
}
