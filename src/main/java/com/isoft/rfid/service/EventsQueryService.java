package com.isoft.rfid.service;

import com.isoft.rfid.domain.*; // for static metamodels
import com.isoft.rfid.domain.Events;
import com.isoft.rfid.repository.EventsRepository;
import com.isoft.rfid.repository.search.EventsSearchRepository;
import com.isoft.rfid.service.criteria.EventsCriteria;
import com.isoft.rfid.service.dto.EventsDTO;
import com.isoft.rfid.service.mapper.EventsMapper;
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
 * Service for executing complex queries for {@link Events} entities in the database.
 * The main input is a {@link EventsCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link EventsDTO} or a {@link Page} of {@link EventsDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class EventsQueryService extends QueryService<Events> {

    private final Logger log = LoggerFactory.getLogger(EventsQueryService.class);

    private final EventsRepository eventsRepository;

    private final EventsMapper eventsMapper;

    private final EventsSearchRepository eventsSearchRepository;

    public EventsQueryService(EventsRepository eventsRepository, EventsMapper eventsMapper, EventsSearchRepository eventsSearchRepository) {
        this.eventsRepository = eventsRepository;
        this.eventsMapper = eventsMapper;
        this.eventsSearchRepository = eventsSearchRepository;
    }

    /**
     * Return a {@link List} of {@link EventsDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<EventsDTO> findByCriteria(EventsCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Events> specification = createSpecification(criteria);
        return eventsMapper.toDto(eventsRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link EventsDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<EventsDTO> findByCriteria(EventsCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Events> specification = createSpecification(criteria);
        return eventsRepository.findAll(specification, page).map(eventsMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(EventsCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Events> specification = createSpecification(criteria);
        return eventsRepository.count(specification);
    }

    /**
     * Function to convert {@link EventsCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Events> createSpecification(EventsCriteria criteria) {
        Specification<Events> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Events_.id));
            }
            if (criteria.getGuid() != null) {
                specification = specification.and(buildStringSpecification(criteria.getGuid(), Events_.guid));
            }
            if (criteria.getPlate() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPlate(), Events_.plate));
            }
            if (criteria.getAnpr() != null) {
                specification = specification.and(buildStringSpecification(criteria.getAnpr(), Events_.anpr));
            }
            if (criteria.getRfid() != null) {
                specification = specification.and(buildStringSpecification(criteria.getRfid(), Events_.rfid));
            }
            if (criteria.getGantry() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getGantry(), Events_.gantry));
            }
            if (criteria.getWantedFor() != null) {
                specification = specification.and(buildStringSpecification(criteria.getWantedFor(), Events_.wantedFor));
            }
            if (criteria.getLicenseIssue() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLicenseIssue(), Events_.licenseIssue));
            }
            if (criteria.getIssue() != null) {
                specification = specification.and(buildStringSpecification(criteria.getIssue(), Events_.issue));
            }
            if (criteria.getTagIssue() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTagIssue(), Events_.tagIssue));
            }
            if (criteria.getStatusName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getStatusName(), Events_.statusName));
            }
            if (criteria.getLane() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getLane(), Events_.lane));
            }
            if (criteria.getDirection() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDirection(), Events_.direction));
            }
            if (criteria.getKph() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getKph(), Events_.kph));
            }
            if (criteria.getAmbush() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getAmbush(), Events_.ambush));
            }
            if (criteria.getToll() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getToll(), Events_.toll));
            }
            if (criteria.getFine() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getFine(), Events_.fine));
            }
            if (criteria.getWantedBy() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getWantedBy(), Events_.wantedBy));
            }
            if (criteria.getLicenseFine() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getLicenseFine(), Events_.licenseFine));
            }
            if (criteria.getSpeedFine() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getSpeedFine(), Events_.speedFine));
            }
            if (criteria.getHandled() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getHandled(), Events_.handled));
            }
            if (criteria.getProcessingTime() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getProcessingTime(), Events_.processingTime));
            }
            if (criteria.getRuleRcvd() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getRuleRcvd(), Events_.ruleRcvd));
            }
            if (criteria.getRuleIssue() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getRuleIssue(), Events_.ruleIssue));
            }
            if (criteria.getRuleProcessed() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getRuleProcessed(), Events_.ruleProcessed));
            }
            if (criteria.getRuleSent() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getRuleSent(), Events_.ruleSent));
            }
            if (criteria.getWhen() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getWhen(), Events_.when));
            }
            if (criteria.getVehicle() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getVehicle(), Events_.vehicle));
            }
            if (criteria.getStolen() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getStolen(), Events_.stolen));
            }
            if (criteria.getWanted() != null) {
                specification = specification.and(buildSpecification(criteria.getWanted(), Events_.wanted));
            }
            if (criteria.getGantryProcessed() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getGantryProcessed(), Events_.gantryProcessed));
            }
            if (criteria.getGantrySent() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getGantrySent(), Events_.gantrySent));
            }
            if (criteria.getStatus() != null) {
                specification = specification.and(buildStringSpecification(criteria.getStatus(), Events_.status));
            }
            if (criteria.getDataStatus() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDataStatus(), Events_.dataStatus));
            }
            if (criteria.getThreadRandNo() != null) {
                specification = specification.and(buildStringSpecification(criteria.getThreadRandNo(), Events_.threadRandNo));
            }
            if (criteria.getHandledBy() != null) {
                specification = specification.and(buildStringSpecification(criteria.getHandledBy(), Events_.handledBy));
            }
        }
        return specification;
    }
}
