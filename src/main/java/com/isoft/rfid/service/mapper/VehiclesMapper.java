package com.isoft.rfid.service.mapper;

import com.isoft.rfid.domain.Vehicles;
import com.isoft.rfid.service.dto.VehiclesDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Vehicles} and its DTO {@link VehiclesDTO}.
 */
@Mapper(componentModel = "spring")
public interface VehiclesMapper extends EntityMapper<VehiclesDTO, Vehicles> {}
