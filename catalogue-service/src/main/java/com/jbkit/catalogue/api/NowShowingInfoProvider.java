package com.jbkit.catalogue.api;

import com.jbkit.catalogue.model.NowShowing;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;

public interface NowShowingInfoProvider {
    @Tool(
            description = "Find movie shows that are scheduled optionally filtered by city, movie title and date. If pageable is not provided, defaults to first 100 results sorted by show time.",
            name = "shows"
    )
    Page<NowShowing> find(
            @ToolParam(required = false, description = "City where a movie is scheduled for screening") String city,
            @ToolParam(required = false, description = "Name of title of the movie") String movieTitle,
            @ToolParam(required = false, description = "Date on which a movie is scheduled for screening") LocalDate date,
            Pageable pageable);

}
