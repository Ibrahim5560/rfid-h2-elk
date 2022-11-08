package com.isoft.rfid.service;

import com.isoft.rfid.domain.*; // for static metamodels
import com.isoft.rfid.domain.EventsImages;
import com.isoft.rfid.repository.EventsImagesRepository;
import com.isoft.rfid.repository.search.EventsImagesSearchRepository;
import com.isoft.rfid.service.criteria.EventsImagesCriteria;
import com.isoft.rfid.service.dto.EventsImagesDTO;
import com.isoft.rfid.service.mapper.EventsImagesMapper;
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
 * Service for executing complex queries for {@link EventsImages} entities in the database.
 * The main input is a {@link EventsImagesCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link EventsImagesDTO} or a {@link Page} of {@link EventsImagesDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class EventsImagesQueryService extends QueryService<EventsImages> {

    private final Logger log = LoggerFactory.getLogger(EventsImagesQueryService.class);

    private final EventsImagesRepository eventsImagesRepository;

    private final EventsImagesMapper eventsImagesMapper;

    private final EventsImagesSearchRepository eventsImagesSearchRepository;

    public EventsImagesQueryService(
        EventsImagesRepository eventsImagesRepository,
        EventsImagesMapper eventsImagesMapper,
        EventsImagesSearchRepository eventsImagesSearchRepository
    ) {
        this.eventsImagesRepository = eventsImagesRepository;
        this.eventsImagesMapper = eventsImagesMapper;
        this.eventsImagesSearchRepository = eventsImagesSearchRepository;
    }

    /**
     * Return a {@link List} of {@link EventsImagesDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<EventsImagesDTO> findByCriteria(EventsImagesCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<EventsImages> specification = createSpecification(criteria);
        return eventsImagesMapper.toDto(eventsImagesRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link EventsImagesDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<EventsImagesDTO> findByCriteria(EventsImagesCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<EventsImages> specification = createSpecification(criteria);
        return eventsImagesRepository.findAll(specification, page).map(eventsImagesMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(EventsImagesCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<EventsImages> specification = createSpecification(criteria);
        return eventsImagesRepository.count(specification);
    }

    /**
     * Function to convert {@link EventsImagesCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<EventsImages> createSpecification(EventsImagesCriteria criteria) {
        Specification<EventsImages> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), EventsImages_.id));
            }
            if (criteria.getGuid() != null) {
                specification = specification.and(buildStringSpecification(criteria.getGuid(), EventsImages_.guid));
            }
            if (criteria.getImageLp() != null) {
                specification = specification.and(buildStringSpecification(criteria.getImageLp(), EventsImages_.imageLp));
            }
            if (criteria.getImageThumb() != null) {
                specification = specification.and(buildStringSpecification(criteria.getImageThumb(), EventsImages_.imageThumb));
            }
            if (criteria.getProcessingTime() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getProcessingTime(), EventsImages_.processingTime));
            }
            if (criteria.getRuleRcvd() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getRuleRcvd(), EventsImages_.ruleRcvd));
            }
            if (criteria.getRuleSent() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getRuleSent(), EventsImages_.ruleSent));
            }
            if (criteria.getWhen() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getWhen(), EventsImages_.when));
            }
            if (criteria.getGantryProcessed() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getGantryProcessed(), EventsImages_.gantryProcessed));
            }
            if (criteria.getGantrySent() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getGantrySent(), EventsImages_.gantrySent));
            }
            if (criteria.getStatus() != null) {
                specification = specification.and(buildStringSpecification(criteria.getStatus(), EventsImages_.status));
            }
            if (criteria.getDataStatus() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDataStatus(), EventsImages_.dataStatus));
            }
            if (criteria.getThreadRandNo() != null) {
                specification = specification.and(buildStringSpecification(criteria.getThreadRandNo(), EventsImages_.threadRandNo));
            }
            if (criteria.getGantry() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getGantry(), EventsImages_.gantry));
            }
        }
        return specification;
    }
}
