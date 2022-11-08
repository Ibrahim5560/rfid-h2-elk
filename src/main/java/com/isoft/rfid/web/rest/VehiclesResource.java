package com.isoft.rfid.web.rest;

import static org.elasticsearch.index.query.QueryBuilders.*;

import com.isoft.rfid.repository.VehiclesRepository;
import com.isoft.rfid.service.VehiclesQueryService;
import com.isoft.rfid.service.VehiclesService;
import com.isoft.rfid.service.criteria.VehiclesCriteria;
import com.isoft.rfid.service.dto.VehiclesDTO;
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
 * REST controller for managing {@link com.isoft.rfid.domain.Vehicles}.
 */
@RestController
@RequestMapping("/api")
public class VehiclesResource {

    private final Logger log = LoggerFactory.getLogger(VehiclesResource.class);

    private static final String ENTITY_NAME = "vehicles";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final VehiclesService vehiclesService;

    private final VehiclesRepository vehiclesRepository;

    private final VehiclesQueryService vehiclesQueryService;

    public VehiclesResource(
        VehiclesService vehiclesService,
        VehiclesRepository vehiclesRepository,
        VehiclesQueryService vehiclesQueryService
    ) {
        this.vehiclesService = vehiclesService;
        this.vehiclesRepository = vehiclesRepository;
        this.vehiclesQueryService = vehiclesQueryService;
    }

    /**
     * {@code POST  /vehicles} : Create a new vehicles.
     *
     * @param vehiclesDTO the vehiclesDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new vehiclesDTO, or with status {@code 400 (Bad Request)} if the vehicles has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/vehicles")
    public ResponseEntity<VehiclesDTO> createVehicles(@Valid @RequestBody VehiclesDTO vehiclesDTO) throws URISyntaxException {
        log.debug("REST request to save Vehicles : {}", vehiclesDTO);
        if (vehiclesDTO.getId() != null) {
            throw new BadRequestAlertException("A new vehicles cannot already have an ID", ENTITY_NAME, "idexists");
        }
        VehiclesDTO result = vehiclesService.save(vehiclesDTO);
        return ResponseEntity
            .created(new URI("/api/vehicles/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /vehicles/:id} : Updates an existing vehicles.
     *
     * @param id the id of the vehiclesDTO to save.
     * @param vehiclesDTO the vehiclesDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated vehiclesDTO,
     * or with status {@code 400 (Bad Request)} if the vehiclesDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the vehiclesDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/vehicles/{id}")
    public ResponseEntity<VehiclesDTO> updateVehicles(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody VehiclesDTO vehiclesDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Vehicles : {}, {}", id, vehiclesDTO);
        if (vehiclesDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, vehiclesDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!vehiclesRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        VehiclesDTO result = vehiclesService.update(vehiclesDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, vehiclesDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /vehicles/:id} : Partial updates given fields of an existing vehicles, field will ignore if it is null
     *
     * @param id the id of the vehiclesDTO to save.
     * @param vehiclesDTO the vehiclesDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated vehiclesDTO,
     * or with status {@code 400 (Bad Request)} if the vehiclesDTO is not valid,
     * or with status {@code 404 (Not Found)} if the vehiclesDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the vehiclesDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/vehicles/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<VehiclesDTO> partialUpdateVehicles(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody VehiclesDTO vehiclesDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Vehicles partially : {}, {}", id, vehiclesDTO);
        if (vehiclesDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, vehiclesDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!vehiclesRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<VehiclesDTO> result = vehiclesService.partialUpdate(vehiclesDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, vehiclesDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /vehicles} : get all the vehicles.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of vehicles in body.
     */
    @GetMapping("/vehicles")
    public ResponseEntity<List<VehiclesDTO>> getAllVehicles(
        VehiclesCriteria criteria,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get Vehicles by criteria: {}", criteria);
        Page<VehiclesDTO> page = vehiclesQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /vehicles/count} : count all the vehicles.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/vehicles/count")
    public ResponseEntity<Long> countVehicles(VehiclesCriteria criteria) {
        log.debug("REST request to count Vehicles by criteria: {}", criteria);
        return ResponseEntity.ok().body(vehiclesQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /vehicles/:id} : get the "id" vehicles.
     *
     * @param id the id of the vehiclesDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the vehiclesDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/vehicles/{id}")
    public ResponseEntity<VehiclesDTO> getVehicles(@PathVariable Long id) {
        log.debug("REST request to get Vehicles : {}", id);
        Optional<VehiclesDTO> vehiclesDTO = vehiclesService.findOne(id);
        return ResponseUtil.wrapOrNotFound(vehiclesDTO);
    }

    /**
     * {@code DELETE  /vehicles/:id} : delete the "id" vehicles.
     *
     * @param id the id of the vehiclesDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/vehicles/{id}")
    public ResponseEntity<Void> deleteVehicles(@PathVariable Long id) {
        log.debug("REST request to delete Vehicles : {}", id);
        vehiclesService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /_search/vehicles?query=:query} : search for the vehicles corresponding
     * to the query.
     *
     * @param query the query of the vehicles search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/vehicles")
    public ResponseEntity<List<VehiclesDTO>> searchVehicles(
        @RequestParam String query,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to search for a page of Vehicles for query {}", query);
        Page<VehiclesDTO> page = vehiclesService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
