package com.isoft.rfid.service.mapper;

import com.isoft.rfid.domain.Gantry;
import com.isoft.rfid.service.dto.GantryDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Gantry} and its DTO {@link GantryDTO}.
 */
@Mapper(componentModel = "spring")
public interface GantryMapper extends EntityMapper<GantryDTO, Gantry> {}
