package com.isoft.rfid.service;

import com.isoft.rfid.service.dto.EventsImagesDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.isoft.rfid.domain.EventsImages}.
 */
public interface EventsImagesService {
    /**
     * Save a eventsImages.
     *
     * @param eventsImagesDTO the entity to save.
     * @return the persisted entity.
     */
    EventsImagesDTO save(EventsImagesDTO eventsImagesDTO);

    /**
     * Updates a eventsImages.
     *
     * @param eventsImagesDTO the entity to update.
     * @return the persisted entity.
     */
    EventsImagesDTO update(EventsImagesDTO eventsImagesDTO);

    /**
     * Partially updates a eventsImages.
     *
     * @param eventsImagesDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<EventsImagesDTO> partialUpdate(EventsImagesDTO eventsImagesDTO);

    /**
     * Get all the eventsImages.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<EventsImagesDTO> findAll(Pageable pageable);

    /**
     * Get the "id" eventsImages.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<EventsImagesDTO> findOne(Long id);

    /**
     * Delete the "id" eventsImages.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the eventsImages corresponding to the query.
     *
     * @param query the query of the search.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<EventsImagesDTO> search(String query, Pageable pageable);
}
