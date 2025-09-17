package com.jbkit.catalogue.repo;

import com.jbkit.catalogue.model.ScheduledShow;
import com.jbkit.catalogue.model.ScheduledShowId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ScheduledShowRepo extends JpaRepository<ScheduledShow, ScheduledShowId> {

    List<ScheduledShow> findByCityAndMovieTitle(String city, String movieTitle);
}
