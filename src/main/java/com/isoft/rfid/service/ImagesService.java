package com.isoft.rfid.service;

import com.isoft.rfid.service.dto.ImagesDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.isoft.rfid.domain.Images}.
 */
public interface ImagesService {
    /**
     * Save a images.
     *
     * @param imagesDTO the entity to save.
     * @return the persisted entity.
     */
    ImagesDTO save(ImagesDTO imagesDTO);

    /**
     * Updates a images.
     *
     * @param imagesDTO the entity to update.
     * @return the persisted entity.
     */
    ImagesDTO update(ImagesDTO imagesDTO);

    /**
     * Partially updates a images.
     *
     * @param imagesDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<ImagesDTO> partialUpdate(ImagesDTO imagesDTO);

    /**
     * Get all the images.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<ImagesDTO> findAll(Pageable pageable);

    /**
     * Get the "id" images.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ImagesDTO> findOne(Long id);

    /**
     * Delete the "id" images.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the images corresponding to the query.
     *
     * @param query the query of the search.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<ImagesDTO> search(String query, Pageable pageable);
}
