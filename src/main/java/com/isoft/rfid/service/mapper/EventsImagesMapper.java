package com.isoft.rfid.service.mapper;

import com.isoft.rfid.domain.EventsImages;
import com.isoft.rfid.service.dto.EventsImagesDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link EventsImages} and its DTO {@link EventsImagesDTO}.
 */
@Mapper(componentModel = "spring")
public interface EventsImagesMapper extends EntityMapper<EventsImagesDTO, EventsImages> {}
