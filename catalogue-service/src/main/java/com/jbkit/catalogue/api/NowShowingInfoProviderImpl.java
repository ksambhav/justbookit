package com.jbkit.catalogue.api;

import com.jbkit.catalogue.model.NowShowing;
import com.jbkit.catalogue.repo.NowShowingRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true) // disables JPA dirty checking
public class NowShowingInfoProviderImpl implements NowShowingInfoProvider {

    private final NowShowingRepo nowShowingRepo;

    @Override
    @Tool(
            description = "Find movie shows that are scheduled optionally filtered by city, movie title and date. If pageable is not provided, defaults to first 100 results sorted by show time.",
            name = "shows"
    )
    public Page<NowShowing> find(
            @ToolParam(required = false, description = "City where a movie is scheduled for screening") String city,
            @ToolParam(required = false, description = "Name of title of the movie") String movieTitle,
            @ToolParam(required = false, description = "Date on which a movie is scheduled for screening") LocalDate date,
            Pageable pageable) {
        log.debug("Finding movies by city: {} and movieTitle: {} , date = {}", city, movieTitle, date);
        if (pageable == null) {
            pageable = PageRequest.of(0, 100, Sort.by("showTime"));
        }
        Specification<NowShowing> spec = (root, query, cb) -> {
            var predicates = new java.util.ArrayList<jakarta.persistence.criteria.Predicate>();
            if (city != null) {
                predicates.add(cb.equal(root.get("city"), city));
            }
            if (movieTitle != null) {
                predicates.add(cb.equal(root.get("movieTitle"), movieTitle));
            }
            if (date != null) {
                predicates.add(cb.equal(root.get("showDate"), date));
            }
            return cb.and(predicates.toArray(new jakarta.persistence.criteria.Predicate[0]));
        };
        return nowShowingRepo.findAll(spec, pageable);
    }
}
