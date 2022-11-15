package com.isoft.rfid.service.mapper;

import com.isoft.rfid.domain.Images;
import com.isoft.rfid.service.dto.ImagesDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Images} and its DTO {@link ImagesDTO}.
 */
@Mapper(componentModel = "spring")
public interface ImagesMapper extends EntityMapper<ImagesDTO, Images> {}
