package com.jbkit.catalogue.repo;

import com.jbkit.catalogue.model.ScheduleShowSeatRef;
import com.jbkit.catalogue.model.ScheduledShowSeat;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ScheduledShowSeatRepo extends JpaRepository<ScheduledShowSeat, ScheduleShowSeatRef> {
}
