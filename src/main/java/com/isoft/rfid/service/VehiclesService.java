package com.isoft.rfid.service;

import com.isoft.rfid.service.dto.VehiclesDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.isoft.rfid.domain.Vehicles}.
 */
public interface VehiclesService {
    /**
     * Save a vehicles.
     *
     * @param vehiclesDTO the entity to save.
     * @return the persisted entity.
     */
    VehiclesDTO save(VehiclesDTO vehiclesDTO);

    /**
     * Updates a vehicles.
     *
     * @param vehiclesDTO the entity to update.
     * @return the persisted entity.
     */
    VehiclesDTO update(VehiclesDTO vehiclesDTO);

    /**
     * Partially updates a vehicles.
     *
     * @param vehiclesDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<VehiclesDTO> partialUpdate(VehiclesDTO vehiclesDTO);

    /**
     * Get all the vehicles.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<VehiclesDTO> findAll(Pageable pageable);

    /**
     * Get the "id" vehicles.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<VehiclesDTO> findOne(Long id);

    /**
     * Delete the "id" vehicles.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the vehicles corresponding to the query.
     *
     * @param query the query of the search.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<VehiclesDTO> search(String query, Pageable pageable);
}
