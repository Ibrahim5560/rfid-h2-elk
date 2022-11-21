package com.isoft.rfid.service.mapper;

import com.isoft.rfid.domain.Task;
import com.isoft.rfid.service.dto.TaskDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Task} and its DTO {@link TaskDTO}.
 */
@Mapper(componentModel = "spring")
public interface TaskMapper extends EntityMapper<TaskDTO, Task> {}
