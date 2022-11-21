package com.isoft.rfid.service.impl;

import static org.elasticsearch.index.query.QueryBuilders.*;

import com.isoft.rfid.domain.Images;
import com.isoft.rfid.repository.ImagesRepository;
import com.isoft.rfid.repository.search.ImagesSearchRepository;
import com.isoft.rfid.service.ImagesService;
import com.isoft.rfid.service.dto.ImagesDTO;
import com.isoft.rfid.service.mapper.ImagesMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Images}.
 */
@Service
@Transactional
public class ImagesServiceImpl implements ImagesService {

    private final Logger log = LoggerFactory.getLogger(ImagesServiceImpl.class);

    private final ImagesRepository imagesRepository;

    private final ImagesMapper imagesMapper;

    private final ImagesSearchRepository imagesSearchRepository;

    public ImagesServiceImpl(ImagesRepository imagesRepository, ImagesMapper imagesMapper, ImagesSearchRepository imagesSearchRepository) {
        this.imagesRepository = imagesRepository;
        this.imagesMapper = imagesMapper;
        this.imagesSearchRepository = imagesSearchRepository;
    }

    @Override
    public ImagesDTO save(ImagesDTO imagesDTO) {
        log.debug("Request to save Images : {}", imagesDTO);
        Images images = imagesMapper.toEntity(imagesDTO);
        images = imagesRepository.save(images);
        ImagesDTO result = imagesMapper.toDto(images);
        imagesSearchRepository.index(images);
        return result;
    }

    @Override
    public ImagesDTO update(ImagesDTO imagesDTO) {
        log.debug("Request to update Images : {}", imagesDTO);
        Images images = imagesMapper.toEntity(imagesDTO);
        images = imagesRepository.save(images);
        ImagesDTO result = imagesMapper.toDto(images);
        imagesSearchRepository.index(images);
        return result;
    }

    @Override
    public Optional<ImagesDTO> partialUpdate(ImagesDTO imagesDTO) {
        log.debug("Request to partially update Images : {}", imagesDTO);

        return imagesRepository
            .findById(imagesDTO.getId())
            .map(existingImages -> {
                imagesMapper.partialUpdate(existingImages, imagesDTO);

                return existingImages;
            })
            .map(imagesRepository::save)
            .map(savedImages -> {
                imagesSearchRepository.save(savedImages);

                return savedImages;
            })
            .map(imagesMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ImagesDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Images");
        return imagesRepository.findAll(pageable).map(imagesMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ImagesDTO> findOne(Long id) {
        log.debug("Request to get Images : {}", id);
        return imagesRepository.findById(id).map(imagesMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Images : {}", id);
        imagesRepository.deleteById(id);
        imagesSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ImagesDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Images for query {}", query);
        return imagesSearchRepository.search(query, pageable).map(imagesMapper::toDto);
    }
}
