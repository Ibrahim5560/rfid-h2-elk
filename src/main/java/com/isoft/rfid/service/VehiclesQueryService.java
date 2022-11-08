package com.isoft.rfid.service;

import com.isoft.rfid.domain.*; // for static metamodels
import com.isoft.rfid.domain.Vehicles;
import com.isoft.rfid.repository.VehiclesRepository;
import com.isoft.rfid.repository.search.VehiclesSearchRepository;
import com.isoft.rfid.service.criteria.VehiclesCriteria;
import com.isoft.rfid.service.dto.VehiclesDTO;
import com.isoft.rfid.service.mapper.VehiclesMapper;
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
 * Service for executing complex queries for {@link Vehicles} entities in the database.
 * The main input is a {@link VehiclesCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link VehiclesDTO} or a {@link Page} of {@link VehiclesDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class VehiclesQueryService extends QueryService<Vehicles> {

    private final Logger log = LoggerFactory.getLogger(VehiclesQueryService.class);

    private final VehiclesRepository vehiclesRepository;

    private final VehiclesMapper vehiclesMapper;

    private final VehiclesSearchRepository vehiclesSearchRepository;

    public VehiclesQueryService(
        VehiclesRepository vehiclesRepository,
        VehiclesMapper vehiclesMapper,
        VehiclesSearchRepository vehiclesSearchRepository
    ) {
        this.vehiclesRepository = vehiclesRepository;
        this.vehiclesMapper = vehiclesMapper;
        this.vehiclesSearchRepository = vehiclesSearchRepository;
    }

    /**
     * Return a {@link List} of {@link VehiclesDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<VehiclesDTO> findByCriteria(VehiclesCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Vehicles> specification = createSpecification(criteria);
        return vehiclesMapper.toDto(vehiclesRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link VehiclesDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<VehiclesDTO> findByCriteria(VehiclesCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Vehicles> specification = createSpecification(criteria);
        return vehiclesRepository.findAll(specification, page).map(vehiclesMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(VehiclesCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Vehicles> specification = createSpecification(criteria);
        return vehiclesRepository.count(specification);
    }

    /**
     * Function to convert {@link VehiclesCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Vehicles> createSpecification(VehiclesCriteria criteria) {
        Specification<Vehicles> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Vehicles_.id));
            }
            if (criteria.getGuid() != null) {
                specification = specification.and(buildStringSpecification(criteria.getGuid(), Vehicles_.guid));
            }
            if (criteria.getPlate() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPlate(), Vehicles_.plate));
            }
            if (criteria.getAnpr() != null) {
                specification = specification.and(buildStringSpecification(criteria.getAnpr(), Vehicles_.anpr));
            }
            if (criteria.getRfid() != null) {
                specification = specification.and(buildStringSpecification(criteria.getRfid(), Vehicles_.rfid));
            }
            if (criteria.getDataStatus() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDataStatus(), Vehicles_.dataStatus));
            }
            if (criteria.getGantry() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getGantry(), Vehicles_.gantry));
            }
            if (criteria.getLane() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getLane(), Vehicles_.lane));
            }
            if (criteria.getKph() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getKph(), Vehicles_.kph));
            }
            if (criteria.getAmbush() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getAmbush(), Vehicles_.ambush));
            }
            if (criteria.getDirection() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDirection(), Vehicles_.direction));
            }
            if (criteria.getVehicle() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getVehicle(), Vehicles_.vehicle));
            }
            if (criteria.getStatus() != null) {
                specification = specification.and(buildStringSpecification(criteria.getStatus(), Vehicles_.status));
            }
        }
        return specification;
    }
}
