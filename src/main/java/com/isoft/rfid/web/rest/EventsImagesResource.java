package com.isoft.rfid.web.rest;

import static org.elasticsearch.index.query.QueryBuilders.*;

import com.isoft.rfid.repository.EventsImagesRepository;
import com.isoft.rfid.service.EventsImagesQueryService;
import com.isoft.rfid.service.EventsImagesService;
import com.isoft.rfid.service.criteria.EventsImagesCriteria;
import com.isoft.rfid.service.dto.EventsImagesDTO;
import com.isoft.rfid.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.StreamSupport;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.isoft.rfid.domain.EventsImages}.
 */
@RestController
@RequestMapping("/api")
public class EventsImagesResource {

    private final Logger log = LoggerFactory.getLogger(EventsImagesResource.class);

    private static final String ENTITY_NAME = "eventsImages";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final EventsImagesService eventsImagesService;

    private final EventsImagesRepository eventsImagesRepository;

    private final EventsImagesQueryService eventsImagesQueryService;

    public EventsImagesResource(
        EventsImagesService eventsImagesService,
        EventsImagesRepository eventsImagesRepository,
        EventsImagesQueryService eventsImagesQueryService
    ) {
        this.eventsImagesService = eventsImagesService;
        this.eventsImagesRepository = eventsImagesRepository;
        this.eventsImagesQueryService = eventsImagesQueryService;
    }

    /**
     * {@code POST  /events-images} : Create a new eventsImages.
     *
     * @param eventsImagesDTO the eventsImagesDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new eventsImagesDTO, or with status {@code 400 (Bad Request)} if the eventsImages has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/events-images")
    public ResponseEntity<EventsImagesDTO> createEventsImages(@Valid @RequestBody EventsImagesDTO eventsImagesDTO)
        throws URISyntaxException {
        log.debug("REST request to save EventsImages : {}", eventsImagesDTO);
        if (eventsImagesDTO.getId() != null) {
            throw new BadRequestAlertException("A new eventsImages cannot already have an ID", ENTITY_NAME, "idexists");
        }
        EventsImagesDTO result = eventsImagesService.save(eventsImagesDTO);
        return ResponseEntity
            .created(new URI("/api/events-images/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /events-images/:id} : Updates an existing eventsImages.
     *
     * @param id the id of the eventsImagesDTO to save.
     * @param eventsImagesDTO the eventsImagesDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated eventsImagesDTO,
     * or with status {@code 400 (Bad Request)} if the eventsImagesDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the eventsImagesDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/events-images/{id}")
    public ResponseEntity<EventsImagesDTO> updateEventsImages(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody EventsImagesDTO eventsImagesDTO
    ) throws URISyntaxException {
        log.debug("REST request to update EventsImages : {}, {}", id, eventsImagesDTO);
        if (eventsImagesDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, eventsImagesDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!eventsImagesRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        EventsImagesDTO result = eventsImagesService.update(eventsImagesDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, eventsImagesDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /events-images/:id} : Partial updates given fields of an existing eventsImages, field will ignore if it is null
     *
     * @param id the id of the eventsImagesDTO to save.
     * @param eventsImagesDTO the eventsImagesDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated eventsImagesDTO,
     * or with status {@code 400 (Bad Request)} if the eventsImagesDTO is not valid,
     * or with status {@code 404 (Not Found)} if the eventsImagesDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the eventsImagesDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/events-images/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<EventsImagesDTO> partialUpdateEventsImages(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody EventsImagesDTO eventsImagesDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update EventsImages partially : {}, {}", id, eventsImagesDTO);
        if (eventsImagesDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, eventsImagesDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!eventsImagesRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<EventsImagesDTO> result = eventsImagesService.partialUpdate(eventsImagesDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, eventsImagesDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /events-images} : get all the eventsImages.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of eventsImages in body.
     */
    @GetMapping("/events-images")
    public ResponseEntity<List<EventsImagesDTO>> getAllEventsImages(
        EventsImagesCriteria criteria,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get EventsImages by criteria: {}", criteria);
        Page<EventsImagesDTO> page = eventsImagesQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /events-images/count} : count all the eventsImages.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/events-images/count")
    public ResponseEntity<Long> countEventsImages(EventsImagesCriteria criteria) {
        log.debug("REST request to count EventsImages by criteria: {}", criteria);
        return ResponseEntity.ok().body(eventsImagesQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /events-images/:id} : get the "id" eventsImages.
     *
     * @param id the id of the eventsImagesDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the eventsImagesDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/events-images/{id}")
    public ResponseEntity<EventsImagesDTO> getEventsImages(@PathVariable Long id) {
        log.debug("REST request to get EventsImages : {}", id);
        Optional<EventsImagesDTO> eventsImagesDTO = eventsImagesService.findOne(id);
        return ResponseUtil.wrapOrNotFound(eventsImagesDTO);
    }

    /**
     * {@code DELETE  /events-images/:id} : delete the "id" eventsImages.
     *
     * @param id the id of the eventsImagesDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/events-images/{id}")
    public ResponseEntity<Void> deleteEventsImages(@PathVariable Long id) {
        log.debug("REST request to delete EventsImages : {}", id);
        eventsImagesService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /_search/events-images?query=:query} : search for the eventsImages corresponding
     * to the query.
     *
     * @param query the query of the eventsImages search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/events-images")
    public ResponseEntity<List<EventsImagesDTO>> searchEventsImages(
        @RequestParam String query,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to search for a page of EventsImages for query {}", query);
        Page<EventsImagesDTO> page = eventsImagesService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
