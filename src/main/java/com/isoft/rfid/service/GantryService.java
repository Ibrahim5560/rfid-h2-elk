package com.isoft.rfid.service;

import com.isoft.rfid.service.dto.GantryDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.isoft.rfid.domain.Gantry}.
 */
public interface GantryService {
    /**
     * Save a gantry.
     *
     * @param gantryDTO the entity to save.
     * @return the persisted entity.
     */
    GantryDTO save(GantryDTO gantryDTO);

    /**
     * Updates a gantry.
     *
     * @param gantryDTO the entity to update.
     * @return the persisted entity.
     */
    GantryDTO update(GantryDTO gantryDTO);

    /**
     * Partially updates a gantry.
     *
     * @param gantryDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<GantryDTO> partialUpdate(GantryDTO gantryDTO);

    /**
     * Get all the gantries.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<GantryDTO> findAll(Pageable pageable);

    /**
     * Get the "id" gantry.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<GantryDTO> findOne(Long id);

    /**
     * Delete the "id" gantry.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the gantry corresponding to the query.
     *
     * @param query the query of the search.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<GantryDTO> search(String query, Pageable pageable);
}
