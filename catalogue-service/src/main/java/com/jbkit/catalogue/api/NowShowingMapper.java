package com.jbkit.catalogue.api;

import com.jbkit.catalogue.model.NowShowing;
import com.jbkit.catalogue.model.ScheduledShow;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface NowShowingMapper {

    NowShowing map(ScheduledShow scheduledScreening);

}
