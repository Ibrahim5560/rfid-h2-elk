package com.isoft.rfid.service;

import com.isoft.rfid.domain.*; // for static metamodels
import com.isoft.rfid.domain.Images;
import com.isoft.rfid.repository.ImagesRepository;
import com.isoft.rfid.repository.search.ImagesSearchRepository;
import com.isoft.rfid.service.criteria.ImagesCriteria;
import com.isoft.rfid.service.dto.ImagesDTO;
import com.isoft.rfid.service.mapper.ImagesMapper;
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
 * Service for executing complex queries for {@link Images} entities in the database.
 * The main input is a {@link ImagesCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link ImagesDTO} or a {@link Page} of {@link ImagesDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ImagesQueryService extends QueryService<Images> {

    private final Logger log = LoggerFactory.getLogger(ImagesQueryService.class);

    private final ImagesRepository imagesRepository;

    private final ImagesMapper imagesMapper;

    private final ImagesSearchRepository imagesSearchRepository;

    public ImagesQueryService(ImagesRepository imagesRepository, ImagesMapper imagesMapper, ImagesSearchRepository imagesSearchRepository) {
        this.imagesRepository = imagesRepository;
        this.imagesMapper = imagesMapper;
        this.imagesSearchRepository = imagesSearchRepository;
    }

    /**
     * Return a {@link List} of {@link ImagesDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<ImagesDTO> findByCriteria(ImagesCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Images> specification = createSpecification(criteria);
        return imagesMapper.toDto(imagesRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link ImagesDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ImagesDTO> findByCriteria(ImagesCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Images> specification = createSpecification(criteria);
        return imagesRepository.findAll(specification, page).map(imagesMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ImagesCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Images> specification = createSpecification(criteria);
        return imagesRepository.count(specification);
    }

    /**
     * Function to convert {@link ImagesCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Images> createSpecification(ImagesCriteria criteria) {
        Specification<Images> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Images_.id));
            }
            if (criteria.getGuid() != null) {
                specification = specification.and(buildStringSpecification(criteria.getGuid(), Images_.guid));
            }
            if (criteria.getPlate() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPlate(), Images_.plate));
            }
            if (criteria.getAnpr() != null) {
                specification = specification.and(buildStringSpecification(criteria.getAnpr(), Images_.anpr));
            }
            if (criteria.getRfid() != null) {
                specification = specification.and(buildStringSpecification(criteria.getRfid(), Images_.rfid));
            }
            if (criteria.getDataStatus() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDataStatus(), Images_.dataStatus));
            }
            if (criteria.getGantry() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getGantry(), Images_.gantry));
            }
            if (criteria.getLane() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getLane(), Images_.lane));
            }
            if (criteria.getKph() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getKph(), Images_.kph));
            }
            if (criteria.getAmbush() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getAmbush(), Images_.ambush));
            }
            if (criteria.getDirection() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDirection(), Images_.direction));
            }
            if (criteria.getVehicle() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getVehicle(), Images_.vehicle));
            }
            if (criteria.getImageLp() != null) {
                specification = specification.and(buildStringSpecification(criteria.getImageLp(), Images_.imageLp));
            }
            if (criteria.getIssue() != null) {
                specification = specification.and(buildStringSpecification(criteria.getIssue(), Images_.issue));
            }
            if (criteria.getStatus() != null) {
                specification = specification.and(buildStringSpecification(criteria.getStatus(), Images_.status));
            }
        }
        return specification;
    }
}
