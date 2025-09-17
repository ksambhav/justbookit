package com.jbkit.catalogue.controller;

import com.jbkit.catalogue.api.NowShowingInfoProvider;
import com.jbkit.catalogue.model.NowShowing;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@Slf4j
@RestController
@RequestMapping("/api/v1/catalogue/shows")
@RequiredArgsConstructor
class ShowsController {

    private final NowShowingInfoProvider nowShowingInfoProvider;

    @GetMapping("/now-showing")
    public Page<NowShowing> find(@RequestParam(required = false) String city,
                                 @RequestParam(required = false) String movieTitle,
                                 @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date,
                                 Pageable pageable) {
        log.debug("Finding movies by city: {} and movieTitle: {}", city, movieTitle);
        return nowShowingInfoProvider.find(city, movieTitle, date, pageable);
    }

}
