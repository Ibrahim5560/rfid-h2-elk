package com.isoft.rfid.service.impl;

import static org.elasticsearch.index.query.QueryBuilders.*;

import com.isoft.rfid.domain.EventsImages;
import com.isoft.rfid.repository.EventsImagesRepository;
import com.isoft.rfid.repository.search.EventsImagesSearchRepository;
import com.isoft.rfid.service.EventsImagesService;
import com.isoft.rfid.service.dto.EventsImagesDTO;
import com.isoft.rfid.service.mapper.EventsImagesMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link EventsImages}.
 */
@Service
@Transactional
public class EventsImagesServiceImpl implements EventsImagesService {

    private final Logger log = LoggerFactory.getLogger(EventsImagesServiceImpl.class);

    private final EventsImagesRepository eventsImagesRepository;

    private final EventsImagesMapper eventsImagesMapper;

    private final EventsImagesSearchRepository eventsImagesSearchRepository;

    public EventsImagesServiceImpl(
        EventsImagesRepository eventsImagesRepository,
        EventsImagesMapper eventsImagesMapper,
        EventsImagesSearchRepository eventsImagesSearchRepository
    ) {
        this.eventsImagesRepository = eventsImagesRepository;
        this.eventsImagesMapper = eventsImagesMapper;
        this.eventsImagesSearchRepository = eventsImagesSearchRepository;
    }

    @Override
    public EventsImagesDTO save(EventsImagesDTO eventsImagesDTO) {
        log.debug("Request to save EventsImages : {}", eventsImagesDTO);
        EventsImages eventsImages = eventsImagesMapper.toEntity(eventsImagesDTO);
        eventsImages = eventsImagesRepository.save(eventsImages);
        EventsImagesDTO result = eventsImagesMapper.toDto(eventsImages);
        eventsImagesSearchRepository.index(eventsImages);
        return result;
    }

    @Override
    public EventsImagesDTO update(EventsImagesDTO eventsImagesDTO) {
        log.debug("Request to update EventsImages : {}", eventsImagesDTO);
        EventsImages eventsImages = eventsImagesMapper.toEntity(eventsImagesDTO);
        eventsImages = eventsImagesRepository.save(eventsImages);
        EventsImagesDTO result = eventsImagesMapper.toDto(eventsImages);
        eventsImagesSearchRepository.index(eventsImages);
        return result;
    }

    @Override
    public Optional<EventsImagesDTO> partialUpdate(EventsImagesDTO eventsImagesDTO) {
        log.debug("Request to partially update EventsImages : {}", eventsImagesDTO);

        return eventsImagesRepository
            .findById(eventsImagesDTO.getId())
            .map(existingEventsImages -> {
                eventsImagesMapper.partialUpdate(existingEventsImages, eventsImagesDTO);

                return existingEventsImages;
            })
            .map(eventsImagesRepository::save)
            .map(savedEventsImages -> {
                eventsImagesSearchRepository.save(savedEventsImages);

                return savedEventsImages;
            })
            .map(eventsImagesMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<EventsImagesDTO> findAll(Pageable pageable) {
        log.debug("Request to get all EventsImages");
        return eventsImagesRepository.findAll(pageable).map(eventsImagesMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<EventsImagesDTO> findOne(Long id) {
        log.debug("Request to get EventsImages : {}", id);
        return eventsImagesRepository.findById(id).map(eventsImagesMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete EventsImages : {}", id);
        eventsImagesRepository.deleteById(id);
        eventsImagesSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<EventsImagesDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of EventsImages for query {}", query);
        return eventsImagesSearchRepository.search(query, pageable).map(eventsImagesMapper::toDto);
    }
}
