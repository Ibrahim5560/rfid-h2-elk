package com.isoft.rfid.service;

import com.isoft.rfid.domain.*; // for static metamodels
import com.isoft.rfid.domain.Gantry;
import com.isoft.rfid.repository.GantryRepository;
import com.isoft.rfid.repository.search.GantrySearchRepository;
import com.isoft.rfid.service.criteria.GantryCriteria;
import com.isoft.rfid.service.dto.GantryDTO;
import com.isoft.rfid.service.mapper.GantryMapper;
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
 * Service for executing complex queries for {@link Gantry} entities in the database.
 * The main input is a {@link GantryCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link GantryDTO} or a {@link Page} of {@link GantryDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class GantryQueryService extends QueryService<Gantry> {

    private final Logger log = LoggerFactory.getLogger(GantryQueryService.class);

    private final GantryRepository gantryRepository;

    private final GantryMapper gantryMapper;

    private final GantrySearchRepository gantrySearchRepository;

    public GantryQueryService(GantryRepository gantryRepository, GantryMapper gantryMapper, GantrySearchRepository gantrySearchRepository) {
        this.gantryRepository = gantryRepository;
        this.gantryMapper = gantryMapper;
        this.gantrySearchRepository = gantrySearchRepository;
    }

    /**
     * Return a {@link List} of {@link GantryDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<GantryDTO> findByCriteria(GantryCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Gantry> specification = createSpecification(criteria);
        return gantryMapper.toDto(gantryRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link GantryDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<GantryDTO> findByCriteria(GantryCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Gantry> specification = createSpecification(criteria);
        return gantryRepository.findAll(specification, page).map(gantryMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(GantryCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Gantry> specification = createSpecification(criteria);
        return gantryRepository.count(specification);
    }

    /**
     * Function to convert {@link GantryCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Gantry> createSpecification(GantryCriteria criteria) {
        Specification<Gantry> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Gantry_.id));
            }
            if (criteria.getGuid() != null) {
                specification = specification.and(buildStringSpecification(criteria.getGuid(), Gantry_.guid));
            }
            if (criteria.getNameEn() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNameEn(), Gantry_.nameEn));
            }
            if (criteria.getNameAr() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNameAr(), Gantry_.nameAr));
            }
            if (criteria.getStatus() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getStatus(), Gantry_.status));
            }
            if (criteria.getCode() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCode(), Gantry_.code));
            }
        }
        return specification;
    }
}
