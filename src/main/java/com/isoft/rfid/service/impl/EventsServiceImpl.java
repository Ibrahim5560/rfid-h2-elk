package com.isoft.rfid.service.impl;

import static org.elasticsearch.index.query.QueryBuilders.*;

import com.isoft.rfid.domain.Events;
import com.isoft.rfid.repository.EventsRepository;
import com.isoft.rfid.repository.search.EventsSearchRepository;
import com.isoft.rfid.service.EventsService;
import com.isoft.rfid.service.dto.EventsDTO;
import com.isoft.rfid.service.mapper.EventsMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Events}.
 */
@Service
@Transactional
public class EventsServiceImpl implements EventsService {

    private final Logger log = LoggerFactory.getLogger(EventsServiceImpl.class);

    private final EventsRepository eventsRepository;

    private final EventsMapper eventsMapper;

    private final EventsSearchRepository eventsSearchRepository;

    public EventsServiceImpl(EventsRepository eventsRepository, EventsMapper eventsMapper, EventsSearchRepository eventsSearchRepository) {
        this.eventsRepository = eventsRepository;
        this.eventsMapper = eventsMapper;
        this.eventsSearchRepository = eventsSearchRepository;
    }

    @Override
    public EventsDTO save(EventsDTO eventsDTO) {
        log.debug("Request to save Events : {}", eventsDTO);
        Events events = eventsMapper.toEntity(eventsDTO);
        events = eventsRepository.save(events);
        EventsDTO result = eventsMapper.toDto(events);
        eventsSearchRepository.index(events);
        return result;
    }

    @Override
    public EventsDTO update(EventsDTO eventsDTO) {
        log.debug("Request to update Events : {}", eventsDTO);
        Events events = eventsMapper.toEntity(eventsDTO);
        events = eventsRepository.save(events);
        EventsDTO result = eventsMapper.toDto(events);
        eventsSearchRepository.index(events);
        return result;
    }

    @Override
    public Optional<EventsDTO> partialUpdate(EventsDTO eventsDTO) {
        log.debug("Request to partially update Events : {}", eventsDTO);

        return eventsRepository
            .findById(eventsDTO.getId())
            .map(existingEvents -> {
                eventsMapper.partialUpdate(existingEvents, eventsDTO);

                return existingEvents;
            })
            .map(eventsRepository::save)
            .map(savedEvents -> {
                eventsSearchRepository.save(savedEvents);

                return savedEvents;
            })
            .map(eventsMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<EventsDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Events");
        return eventsRepository.findAll(pageable).map(eventsMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<EventsDTO> findOne(Long id) {
        log.debug("Request to get Events : {}", id);
        return eventsRepository.findById(id).map(eventsMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Events : {}", id);
        eventsRepository.deleteById(id);
        eventsSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<EventsDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Events for query {}", query);
        return eventsSearchRepository.search(query, pageable).map(eventsMapper::toDto);
    }
}
