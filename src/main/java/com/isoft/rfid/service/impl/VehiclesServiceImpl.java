package com.isoft.rfid.service.impl;

import static org.elasticsearch.index.query.QueryBuilders.*;

import com.isoft.rfid.domain.Vehicles;
import com.isoft.rfid.repository.VehiclesRepository;
import com.isoft.rfid.repository.search.VehiclesSearchRepository;
import com.isoft.rfid.service.VehiclesService;
import com.isoft.rfid.service.dto.VehiclesDTO;
import com.isoft.rfid.service.mapper.VehiclesMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Vehicles}.
 */
@Service
@Transactional
public class VehiclesServiceImpl implements VehiclesService {

    private final Logger log = LoggerFactory.getLogger(VehiclesServiceImpl.class);

    private final VehiclesRepository vehiclesRepository;

    private final VehiclesMapper vehiclesMapper;

    private final VehiclesSearchRepository vehiclesSearchRepository;

    public VehiclesServiceImpl(
        VehiclesRepository vehiclesRepository,
        VehiclesMapper vehiclesMapper,
        VehiclesSearchRepository vehiclesSearchRepository
    ) {
        this.vehiclesRepository = vehiclesRepository;
        this.vehiclesMapper = vehiclesMapper;
        this.vehiclesSearchRepository = vehiclesSearchRepository;
    }

    @Override
    public VehiclesDTO save(VehiclesDTO vehiclesDTO) {
        log.debug("Request to save Vehicles : {}", vehiclesDTO);
        Vehicles vehicles = vehiclesMapper.toEntity(vehiclesDTO);
        vehicles = vehiclesRepository.save(vehicles);
        VehiclesDTO result = vehiclesMapper.toDto(vehicles);
        vehiclesSearchRepository.index(vehicles);
        return result;
    }

    @Override
    public VehiclesDTO update(VehiclesDTO vehiclesDTO) {
        log.debug("Request to update Vehicles : {}", vehiclesDTO);
        Vehicles vehicles = vehiclesMapper.toEntity(vehiclesDTO);
        vehicles = vehiclesRepository.save(vehicles);
        VehiclesDTO result = vehiclesMapper.toDto(vehicles);
        vehiclesSearchRepository.index(vehicles);
        return result;
    }

    @Override
    public Optional<VehiclesDTO> partialUpdate(VehiclesDTO vehiclesDTO) {
        log.debug("Request to partially update Vehicles : {}", vehiclesDTO);

        return vehiclesRepository
            .findById(vehiclesDTO.getId())
            .map(existingVehicles -> {
                vehiclesMapper.partialUpdate(existingVehicles, vehiclesDTO);

                return existingVehicles;
            })
            .map(vehiclesRepository::save)
            .map(savedVehicles -> {
                vehiclesSearchRepository.save(savedVehicles);

                return savedVehicles;
            })
            .map(vehiclesMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<VehiclesDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Vehicles");
        return vehiclesRepository.findAll(pageable).map(vehiclesMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<VehiclesDTO> findOne(Long id) {
        log.debug("Request to get Vehicles : {}", id);
        return vehiclesRepository.findById(id).map(vehiclesMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Vehicles : {}", id);
        vehiclesRepository.deleteById(id);
        vehiclesSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<VehiclesDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Vehicles for query {}", query);
        return vehiclesSearchRepository.search(query, pageable).map(vehiclesMapper::toDto);
    }
}
