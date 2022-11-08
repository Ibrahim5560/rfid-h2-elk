package com.isoft.rfid.service.mapper;

import com.isoft.rfid.domain.Events;
import com.isoft.rfid.service.dto.EventsDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Events} and its DTO {@link EventsDTO}.
 */
@Mapper(componentModel = "spring")
public interface EventsMapper extends EntityMapper<EventsDTO, Events> {}
