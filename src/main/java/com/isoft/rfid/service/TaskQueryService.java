package com.isoft.rfid.service;

import com.isoft.rfid.domain.*; // for static metamodels
import com.isoft.rfid.domain.Task;
import com.isoft.rfid.repository.TaskRepository;
import com.isoft.rfid.repository.search.TaskSearchRepository;
import com.isoft.rfid.service.criteria.TaskCriteria;
import com.isoft.rfid.service.dto.TaskDTO;
import com.isoft.rfid.service.mapper.TaskMapper;
import java.util.List;
import javax.persistence.criteria.JoinType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link Task} entities in the database.
 * The main input is a {@link TaskCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link TaskDTO} or a {@link Page} of {@link TaskDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class TaskQueryService extends QueryService<Task> {

    private final Logger log = LoggerFactory.getLogger(TaskQueryService.class);

    private final TaskRepository taskRepository;

    private final TaskMapper taskMapper;

    private final TaskSearchRepository taskSearchRepository;

    public TaskQueryService(TaskRepository taskRepository, TaskMapper taskMapper, TaskSearchRepository taskSearchRepository) {
        this.taskRepository = taskRepository;
        this.taskMapper = taskMapper;
        this.taskSearchRepository = taskSearchRepository;
    }

    /**
     * Return a {@link List} of {@link TaskDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<TaskDTO> findByCriteria(TaskCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Task> specification = createSpecification(criteria);
        return taskMapper.toDto(taskRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link TaskDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<TaskDTO> findByCriteria(TaskCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Task> specification = createSpecification(criteria);
        return taskRepository.findAll(specification, page).map(taskMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(TaskCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Task> specification = createSpecification(criteria);
        return taskRepository.count(specification);
    }

    /**
     * Function to convert {@link TaskCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Task> createSpecification(TaskCriteria criteria) {
        Specification<Task> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Task_.id));
            }
            if (criteria.getGuid() != null) {
                specification = specification.and(buildStringSpecification(criteria.getGuid(), Task_.guid));
            }
            if (criteria.getPlate() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPlate(), Task_.plate));
            }
            if (criteria.getAnpr() != null) {
                specification = specification.and(buildStringSpecification(criteria.getAnpr(), Task_.anpr));
            }
            if (criteria.getRfid() != null) {
                specification = specification.and(buildStringSpecification(criteria.getRfid(), Task_.rfid));
            }
            if (criteria.getDataStatus() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDataStatus(), Task_.dataStatus));
            }
            if (criteria.getGantry() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getGantry(), Task_.gantry));
            }
            if (criteria.getKph() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getKph(), Task_.kph));
            }
            if (criteria.getAmbush() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getAmbush(), Task_.ambush));
            }
            if (criteria.getDirection() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDirection(), Task_.direction));
            }
            if (criteria.getVehicle() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getVehicle(), Task_.vehicle));
            }
            if (criteria.getIssue() != null) {
                specification = specification.and(buildStringSpecification(criteria.getIssue(), Task_.issue));
            }
            if (criteria.getStatus() != null) {
                specification = specification.and(buildStringSpecification(criteria.getStatus(), Task_.status));
            }
            if (criteria.getHandledBy() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getHandledBy(), Task_.handledBy));
            }
            if (criteria.getGantryProcessed() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getGantryProcessed(), Task_.gantryProcessed));
            }
            if (criteria.getGantrySent() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getGantrySent(), Task_.gantrySent));
            }
            if (criteria.getWhen() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getWhen(), Task_.when));
            }
            if (criteria.getToll() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getToll(), Task_.toll));
            }
            if (criteria.getRuleRcvd() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getRuleRcvd(), Task_.ruleRcvd));
            }
            if (criteria.getWantedFor() != null) {
                specification = specification.and(buildStringSpecification(criteria.getWantedFor(), Task_.wantedFor));
            }
            if (criteria.getFine() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getFine(), Task_.fine));
            }
            if (criteria.getLicenseIssue() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLicenseIssue(), Task_.licenseIssue));
            }
            if (criteria.getWantedBy() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getWantedBy(), Task_.wantedBy));
            }
            if (criteria.getRuleProcessed() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getRuleProcessed(), Task_.ruleProcessed));
            }
            if (criteria.getSpeedFine() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getSpeedFine(), Task_.speedFine));
            }
            if (criteria.getLane() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getLane(), Task_.lane));
            }
            if (criteria.getTagIssue() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTagIssue(), Task_.tagIssue));
            }
            if (criteria.getStatusName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getStatusName(), Task_.statusName));
            }
            if (criteria.getLicenseFine() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getLicenseFine(), Task_.licenseFine));
            }
            if (criteria.getStolen() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getStolen(), Task_.stolen));
            }
            if (criteria.getWanted() != null) {
                specification = specification.and(buildSpecification(criteria.getWanted(), Task_.wanted));
            }
            if (criteria.getRuleSent() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getRuleSent(), Task_.ruleSent));
            }
            if (criteria.getHandled() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getHandled(), Task_.handled));
            }
            if (criteria.getRuleIssue() != null) {
                specification = specification.and(buildStringSpecification(criteria.getRuleIssue(), Task_.ruleIssue));
            }
            if (criteria.getProcessingTime() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getProcessingTime(), Task_.processingTime));
            }
            if (criteria.getThreadRandNo() != null) {
                specification = specification.and(buildStringSpecification(criteria.getThreadRandNo(), Task_.threadRandNo));
            }
        }
        return specification;
    }
}
