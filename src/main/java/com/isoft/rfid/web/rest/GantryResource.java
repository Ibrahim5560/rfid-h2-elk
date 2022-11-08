package com.isoft.rfid.web.rest;

import static org.elasticsearch.index.query.QueryBuilders.*;

import com.isoft.rfid.repository.GantryRepository;
import com.isoft.rfid.service.GantryQueryService;
import com.isoft.rfid.service.GantryService;
import com.isoft.rfid.service.criteria.GantryCriteria;
import com.isoft.rfid.service.dto.GantryDTO;
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
 * REST controller for managing {@link com.isoft.rfid.domain.Gantry}.
 */
@RestController
@RequestMapping("/api")
public class GantryResource {

    private final Logger log = LoggerFactory.getLogger(GantryResource.class);

    private static final String ENTITY_NAME = "gantry";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final GantryService gantryService;

    private final GantryRepository gantryRepository;

    private final GantryQueryService gantryQueryService;

    public GantryResource(GantryService gantryService, GantryRepository gantryRepository, GantryQueryService gantryQueryService) {
        this.gantryService = gantryService;
        this.gantryRepository = gantryRepository;
        this.gantryQueryService = gantryQueryService;
    }

    /**
     * {@code POST  /gantries} : Create a new gantry.
     *
     * @param gantryDTO the gantryDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new gantryDTO, or with status {@code 400 (Bad Request)} if the gantry has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/gantries")
    public ResponseEntity<GantryDTO> createGantry(@Valid @RequestBody GantryDTO gantryDTO) throws URISyntaxException {
        log.debug("REST request to save Gantry : {}", gantryDTO);
        if (gantryDTO.getId() != null) {
            throw new BadRequestAlertException("A new gantry cannot already have an ID", ENTITY_NAME, "idexists");
        }
        GantryDTO result = gantryService.save(gantryDTO);
        return ResponseEntity
            .created(new URI("/api/gantries/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /gantries/:id} : Updates an existing gantry.
     *
     * @param id the id of the gantryDTO to save.
     * @param gantryDTO the gantryDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated gantryDTO,
     * or with status {@code 400 (Bad Request)} if the gantryDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the gantryDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/gantries/{id}")
    public ResponseEntity<GantryDTO> updateGantry(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody GantryDTO gantryDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Gantry : {}, {}", id, gantryDTO);
        if (gantryDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, gantryDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!gantryRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        GantryDTO result = gantryService.update(gantryDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, gantryDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /gantries/:id} : Partial updates given fields of an existing gantry, field will ignore if it is null
     *
     * @param id the id of the gantryDTO to save.
     * @param gantryDTO the gantryDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated gantryDTO,
     * or with status {@code 400 (Bad Request)} if the gantryDTO is not valid,
     * or with status {@code 404 (Not Found)} if the gantryDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the gantryDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/gantries/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<GantryDTO> partialUpdateGantry(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody GantryDTO gantryDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Gantry partially : {}, {}", id, gantryDTO);
        if (gantryDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, gantryDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!gantryRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<GantryDTO> result = gantryService.partialUpdate(gantryDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, gantryDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /gantries} : get all the gantries.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of gantries in body.
     */
    @GetMapping("/gantries")
    public ResponseEntity<List<GantryDTO>> getAllGantries(
        GantryCriteria criteria,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get Gantries by criteria: {}", criteria);
        Page<GantryDTO> page = gantryQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /gantries/count} : count all the gantries.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/gantries/count")
    public ResponseEntity<Long> countGantries(GantryCriteria criteria) {
        log.debug("REST request to count Gantries by criteria: {}", criteria);
        return ResponseEntity.ok().body(gantryQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /gantries/:id} : get the "id" gantry.
     *
     * @param id the id of the gantryDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the gantryDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/gantries/{id}")
    public ResponseEntity<GantryDTO> getGantry(@PathVariable Long id) {
        log.debug("REST request to get Gantry : {}", id);
        Optional<GantryDTO> gantryDTO = gantryService.findOne(id);
        return ResponseUtil.wrapOrNotFound(gantryDTO);
    }

    /**
     * {@code DELETE  /gantries/:id} : delete the "id" gantry.
     *
     * @param id the id of the gantryDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/gantries/{id}")
    public ResponseEntity<Void> deleteGantry(@PathVariable Long id) {
        log.debug("REST request to delete Gantry : {}", id);
        gantryService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /_search/gantries?query=:query} : search for the gantry corresponding
     * to the query.
     *
     * @param query the query of the gantry search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/gantries")
    public ResponseEntity<List<GantryDTO>> searchGantries(
        @RequestParam String query,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to search for a page of Gantries for query {}", query);
        Page<GantryDTO> page = gantryService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
