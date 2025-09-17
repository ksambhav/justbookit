package com.jbkit.booking.show.event;

import com.jbkit.common.events.ShowSeatCreatedEvent;
import org.springframework.kafka.support.serializer.JsonDeserializer;

public class SeatEventDeSerializer extends JsonDeserializer<ShowSeatCreatedEvent> {
}
