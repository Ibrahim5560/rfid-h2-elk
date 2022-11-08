package com.isoft.rfid.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.isoft.rfid.IntegrationTest;
import com.isoft.rfid.domain.EventsImages;
import com.isoft.rfid.repository.EventsImagesRepository;
import com.isoft.rfid.repository.search.EventsImagesSearchRepository;
import com.isoft.rfid.service.criteria.EventsImagesCriteria;
import com.isoft.rfid.service.dto.EventsImagesDTO;
import com.isoft.rfid.service.mapper.EventsImagesMapper;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.apache.commons.collections4.IterableUtils;
import org.assertj.core.util.IterableUtil;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link EventsImagesResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class EventsImagesResourceIT {

    private static final String DEFAULT_GUID = "AAAAAAAAAA";
    private static final String UPDATED_GUID = "BBBBBBBBBB";

    private static final String DEFAULT_IMAGE_LP = "AAAAAAAAAA";
    private static final String UPDATED_IMAGE_LP = "BBBBBBBBBB";

    private static final String DEFAULT_IMAGE_THUMB = "AAAAAAAAAA";
    private static final String UPDATED_IMAGE_THUMB = "BBBBBBBBBB";

    private static final Long DEFAULT_PROCESSING_TIME = 1L;
    private static final Long UPDATED_PROCESSING_TIME = 2L;
    private static final Long SMALLER_PROCESSING_TIME = 1L - 1L;

    private static final Long DEFAULT_RULE_RCVD = 1L;
    private static final Long UPDATED_RULE_RCVD = 2L;
    private static final Long SMALLER_RULE_RCVD = 1L - 1L;

    private static final Long DEFAULT_RULE_SENT = 1L;
    private static final Long UPDATED_RULE_SENT = 2L;
    private static final Long SMALLER_RULE_SENT = 1L - 1L;

    private static final Long DEFAULT_WHEN = 1L;
    private static final Long UPDATED_WHEN = 2L;
    private static final Long SMALLER_WHEN = 1L - 1L;

    private static final Long DEFAULT_GANTRY_PROCESSED = 1L;
    private static final Long UPDATED_GANTRY_PROCESSED = 2L;
    private static final Long SMALLER_GANTRY_PROCESSED = 1L - 1L;

    private static final Long DEFAULT_GANTRY_SENT = 1L;
    private static final Long UPDATED_GANTRY_SENT = 2L;
    private static final Long SMALLER_GANTRY_SENT = 1L - 1L;

    private static final String DEFAULT_STATUS = "AAAAAAAAAA";
    private static final String UPDATED_STATUS = "BBBBBBBBBB";

    private static final String DEFAULT_DATA_STATUS = "AAAAAAAAAA";
    private static final String UPDATED_DATA_STATUS = "BBBBBBBBBB";

    private static final String DEFAULT_THREAD_RAND_NO = "AAAAAAAAAA";
    private static final String UPDATED_THREAD_RAND_NO = "BBBBBBBBBB";

    private static final Integer DEFAULT_GANTRY = 1;
    private static final Integer UPDATED_GANTRY = 2;
    private static final Integer SMALLER_GANTRY = 1 - 1;

    private static final String ENTITY_API_URL = "/api/events-images";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/_search/events-images";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private EventsImagesRepository eventsImagesRepository;

    @Autowired
    private EventsImagesMapper eventsImagesMapper;

    @Autowired
    private EventsImagesSearchRepository eventsImagesSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restEventsImagesMockMvc;

    private EventsImages eventsImages;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static EventsImages createEntity(EntityManager em) {
        EventsImages eventsImages = new EventsImages()
            .guid(DEFAULT_GUID)
            .imageLp(DEFAULT_IMAGE_LP)
            .imageThumb(DEFAULT_IMAGE_THUMB)
            .processingTime(DEFAULT_PROCESSING_TIME)
            .ruleRcvd(DEFAULT_RULE_RCVD)
            .ruleSent(DEFAULT_RULE_SENT)
            .when(DEFAULT_WHEN)
            .gantryProcessed(DEFAULT_GANTRY_PROCESSED)
            .gantrySent(DEFAULT_GANTRY_SENT)
            .status(DEFAULT_STATUS)
            .dataStatus(DEFAULT_DATA_STATUS)
            .threadRandNo(DEFAULT_THREAD_RAND_NO)
            .gantry(DEFAULT_GANTRY);
        return eventsImages;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static EventsImages createUpdatedEntity(EntityManager em) {
        EventsImages eventsImages = new EventsImages()
            .guid(UPDATED_GUID)
            .imageLp(UPDATED_IMAGE_LP)
            .imageThumb(UPDATED_IMAGE_THUMB)
            .processingTime(UPDATED_PROCESSING_TIME)
            .ruleRcvd(UPDATED_RULE_RCVD)
            .ruleSent(UPDATED_RULE_SENT)
            .when(UPDATED_WHEN)
            .gantryProcessed(UPDATED_GANTRY_PROCESSED)
            .gantrySent(UPDATED_GANTRY_SENT)
            .status(UPDATED_STATUS)
            .dataStatus(UPDATED_DATA_STATUS)
            .threadRandNo(UPDATED_THREAD_RAND_NO)
            .gantry(UPDATED_GANTRY);
        return eventsImages;
    }

    @AfterEach
    public void cleanupElasticSearchRepository() {
        eventsImagesSearchRepository.deleteAll();
        assertThat(eventsImagesSearchRepository.count()).isEqualTo(0);
    }

    @BeforeEach
    public void initTest() {
        eventsImages = createEntity(em);
    }

    @Test
    @Transactional
    void createEventsImages() throws Exception {
        int databaseSizeBeforeCreate = eventsImagesRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(eventsImagesSearchRepository.findAll());
        // Create the EventsImages
        EventsImagesDTO eventsImagesDTO = eventsImagesMapper.toDto(eventsImages);
        restEventsImagesMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(eventsImagesDTO))
            )
            .andExpect(status().isCreated());

        // Validate the EventsImages in the database
        List<EventsImages> eventsImagesList = eventsImagesRepository.findAll();
        assertThat(eventsImagesList).hasSize(databaseSizeBeforeCreate + 1);
        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(eventsImagesSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore + 1);
            });
        EventsImages testEventsImages = eventsImagesList.get(eventsImagesList.size() - 1);
        assertThat(testEventsImages.getGuid()).isEqualTo(DEFAULT_GUID);
        assertThat(testEventsImages.getImageLp()).isEqualTo(DEFAULT_IMAGE_LP);
        assertThat(testEventsImages.getImageThumb()).isEqualTo(DEFAULT_IMAGE_THUMB);
        assertThat(testEventsImages.getProcessingTime()).isEqualTo(DEFAULT_PROCESSING_TIME);
        assertThat(testEventsImages.getRuleRcvd()).isEqualTo(DEFAULT_RULE_RCVD);
        assertThat(testEventsImages.getRuleSent()).isEqualTo(DEFAULT_RULE_SENT);
        assertThat(testEventsImages.getWhen()).isEqualTo(DEFAULT_WHEN);
        assertThat(testEventsImages.getGantryProcessed()).isEqualTo(DEFAULT_GANTRY_PROCESSED);
        assertThat(testEventsImages.getGantrySent()).isEqualTo(DEFAULT_GANTRY_SENT);
        assertThat(testEventsImages.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testEventsImages.getDataStatus()).isEqualTo(DEFAULT_DATA_STATUS);
        assertThat(testEventsImages.getThreadRandNo()).isEqualTo(DEFAULT_THREAD_RAND_NO);
        assertThat(testEventsImages.getGantry()).isEqualTo(DEFAULT_GANTRY);
    }

    @Test
    @Transactional
    void createEventsImagesWithExistingId() throws Exception {
        // Create the EventsImages with an existing ID
        eventsImages.setId(1L);
        EventsImagesDTO eventsImagesDTO = eventsImagesMapper.toDto(eventsImages);

        int databaseSizeBeforeCreate = eventsImagesRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(eventsImagesSearchRepository.findAll());

        // An entity with an existing ID cannot be created, so this API call must fail
        restEventsImagesMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(eventsImagesDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the EventsImages in the database
        List<EventsImages> eventsImagesList = eventsImagesRepository.findAll();
        assertThat(eventsImagesList).hasSize(databaseSizeBeforeCreate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(eventsImagesSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkGuidIsRequired() throws Exception {
        int databaseSizeBeforeTest = eventsImagesRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(eventsImagesSearchRepository.findAll());
        // set the field null
        eventsImages.setGuid(null);

        // Create the EventsImages, which fails.
        EventsImagesDTO eventsImagesDTO = eventsImagesMapper.toDto(eventsImages);

        restEventsImagesMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(eventsImagesDTO))
            )
            .andExpect(status().isBadRequest());

        List<EventsImages> eventsImagesList = eventsImagesRepository.findAll();
        assertThat(eventsImagesList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(eventsImagesSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void getAllEventsImages() throws Exception {
        // Initialize the database
        eventsImagesRepository.saveAndFlush(eventsImages);

        // Get all the eventsImagesList
        restEventsImagesMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(eventsImages.getId().intValue())))
            .andExpect(jsonPath("$.[*].guid").value(hasItem(DEFAULT_GUID)))
            .andExpect(jsonPath("$.[*].imageLp").value(hasItem(DEFAULT_IMAGE_LP)))
            .andExpect(jsonPath("$.[*].imageThumb").value(hasItem(DEFAULT_IMAGE_THUMB)))
            .andExpect(jsonPath("$.[*].processingTime").value(hasItem(DEFAULT_PROCESSING_TIME.intValue())))
            .andExpect(jsonPath("$.[*].ruleRcvd").value(hasItem(DEFAULT_RULE_RCVD.intValue())))
            .andExpect(jsonPath("$.[*].ruleSent").value(hasItem(DEFAULT_RULE_SENT.intValue())))
            .andExpect(jsonPath("$.[*].when").value(hasItem(DEFAULT_WHEN.intValue())))
            .andExpect(jsonPath("$.[*].gantryProcessed").value(hasItem(DEFAULT_GANTRY_PROCESSED.intValue())))
            .andExpect(jsonPath("$.[*].gantrySent").value(hasItem(DEFAULT_GANTRY_SENT.intValue())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS)))
            .andExpect(jsonPath("$.[*].dataStatus").value(hasItem(DEFAULT_DATA_STATUS)))
            .andExpect(jsonPath("$.[*].threadRandNo").value(hasItem(DEFAULT_THREAD_RAND_NO)))
            .andExpect(jsonPath("$.[*].gantry").value(hasItem(DEFAULT_GANTRY)));
    }

    @Test
    @Transactional
    void getEventsImages() throws Exception {
        // Initialize the database
        eventsImagesRepository.saveAndFlush(eventsImages);

        // Get the eventsImages
        restEventsImagesMockMvc
            .perform(get(ENTITY_API_URL_ID, eventsImages.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(eventsImages.getId().intValue()))
            .andExpect(jsonPath("$.guid").value(DEFAULT_GUID))
            .andExpect(jsonPath("$.imageLp").value(DEFAULT_IMAGE_LP))
            .andExpect(jsonPath("$.imageThumb").value(DEFAULT_IMAGE_THUMB))
            .andExpect(jsonPath("$.processingTime").value(DEFAULT_PROCESSING_TIME.intValue()))
            .andExpect(jsonPath("$.ruleRcvd").value(DEFAULT_RULE_RCVD.intValue()))
            .andExpect(jsonPath("$.ruleSent").value(DEFAULT_RULE_SENT.intValue()))
            .andExpect(jsonPath("$.when").value(DEFAULT_WHEN.intValue()))
            .andExpect(jsonPath("$.gantryProcessed").value(DEFAULT_GANTRY_PROCESSED.intValue()))
            .andExpect(jsonPath("$.gantrySent").value(DEFAULT_GANTRY_SENT.intValue()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS))
            .andExpect(jsonPath("$.dataStatus").value(DEFAULT_DATA_STATUS))
            .andExpect(jsonPath("$.threadRandNo").value(DEFAULT_THREAD_RAND_NO))
            .andExpect(jsonPath("$.gantry").value(DEFAULT_GANTRY));
    }

    @Test
    @Transactional
    void getEventsImagesByIdFiltering() throws Exception {
        // Initialize the database
        eventsImagesRepository.saveAndFlush(eventsImages);

        Long id = eventsImages.getId();

        defaultEventsImagesShouldBeFound("id.equals=" + id);
        defaultEventsImagesShouldNotBeFound("id.notEquals=" + id);

        defaultEventsImagesShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultEventsImagesShouldNotBeFound("id.greaterThan=" + id);

        defaultEventsImagesShouldBeFound("id.lessThanOrEqual=" + id);
        defaultEventsImagesShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllEventsImagesByGuidIsEqualToSomething() throws Exception {
        // Initialize the database
        eventsImagesRepository.saveAndFlush(eventsImages);

        // Get all the eventsImagesList where guid equals to DEFAULT_GUID
        defaultEventsImagesShouldBeFound("guid.equals=" + DEFAULT_GUID);

        // Get all the eventsImagesList where guid equals to UPDATED_GUID
        defaultEventsImagesShouldNotBeFound("guid.equals=" + UPDATED_GUID);
    }

    @Test
    @Transactional
    void getAllEventsImagesByGuidIsInShouldWork() throws Exception {
        // Initialize the database
        eventsImagesRepository.saveAndFlush(eventsImages);

        // Get all the eventsImagesList where guid in DEFAULT_GUID or UPDATED_GUID
        defaultEventsImagesShouldBeFound("guid.in=" + DEFAULT_GUID + "," + UPDATED_GUID);

        // Get all the eventsImagesList where guid equals to UPDATED_GUID
        defaultEventsImagesShouldNotBeFound("guid.in=" + UPDATED_GUID);
    }

    @Test
    @Transactional
    void getAllEventsImagesByGuidIsNullOrNotNull() throws Exception {
        // Initialize the database
        eventsImagesRepository.saveAndFlush(eventsImages);

        // Get all the eventsImagesList where guid is not null
        defaultEventsImagesShouldBeFound("guid.specified=true");

        // Get all the eventsImagesList where guid is null
        defaultEventsImagesShouldNotBeFound("guid.specified=false");
    }

    @Test
    @Transactional
    void getAllEventsImagesByGuidContainsSomething() throws Exception {
        // Initialize the database
        eventsImagesRepository.saveAndFlush(eventsImages);

        // Get all the eventsImagesList where guid contains DEFAULT_GUID
        defaultEventsImagesShouldBeFound("guid.contains=" + DEFAULT_GUID);

        // Get all the eventsImagesList where guid contains UPDATED_GUID
        defaultEventsImagesShouldNotBeFound("guid.contains=" + UPDATED_GUID);
    }

    @Test
    @Transactional
    void getAllEventsImagesByGuidNotContainsSomething() throws Exception {
        // Initialize the database
        eventsImagesRepository.saveAndFlush(eventsImages);

        // Get all the eventsImagesList where guid does not contain DEFAULT_GUID
        defaultEventsImagesShouldNotBeFound("guid.doesNotContain=" + DEFAULT_GUID);

        // Get all the eventsImagesList where guid does not contain UPDATED_GUID
        defaultEventsImagesShouldBeFound("guid.doesNotContain=" + UPDATED_GUID);
    }

    @Test
    @Transactional
    void getAllEventsImagesByImageLpIsEqualToSomething() throws Exception {
        // Initialize the database
        eventsImagesRepository.saveAndFlush(eventsImages);

        // Get all the eventsImagesList where imageLp equals to DEFAULT_IMAGE_LP
        defaultEventsImagesShouldBeFound("imageLp.equals=" + DEFAULT_IMAGE_LP);

        // Get all the eventsImagesList where imageLp equals to UPDATED_IMAGE_LP
        defaultEventsImagesShouldNotBeFound("imageLp.equals=" + UPDATED_IMAGE_LP);
    }

    @Test
    @Transactional
    void getAllEventsImagesByImageLpIsInShouldWork() throws Exception {
        // Initialize the database
        eventsImagesRepository.saveAndFlush(eventsImages);

        // Get all the eventsImagesList where imageLp in DEFAULT_IMAGE_LP or UPDATED_IMAGE_LP
        defaultEventsImagesShouldBeFound("imageLp.in=" + DEFAULT_IMAGE_LP + "," + UPDATED_IMAGE_LP);

        // Get all the eventsImagesList where imageLp equals to UPDATED_IMAGE_LP
        defaultEventsImagesShouldNotBeFound("imageLp.in=" + UPDATED_IMAGE_LP);
    }

    @Test
    @Transactional
    void getAllEventsImagesByImageLpIsNullOrNotNull() throws Exception {
        // Initialize the database
        eventsImagesRepository.saveAndFlush(eventsImages);

        // Get all the eventsImagesList where imageLp is not null
        defaultEventsImagesShouldBeFound("imageLp.specified=true");

        // Get all the eventsImagesList where imageLp is null
        defaultEventsImagesShouldNotBeFound("imageLp.specified=false");
    }

    @Test
    @Transactional
    void getAllEventsImagesByImageLpContainsSomething() throws Exception {
        // Initialize the database
        eventsImagesRepository.saveAndFlush(eventsImages);

        // Get all the eventsImagesList where imageLp contains DEFAULT_IMAGE_LP
        defaultEventsImagesShouldBeFound("imageLp.contains=" + DEFAULT_IMAGE_LP);

        // Get all the eventsImagesList where imageLp contains UPDATED_IMAGE_LP
        defaultEventsImagesShouldNotBeFound("imageLp.contains=" + UPDATED_IMAGE_LP);
    }

    @Test
    @Transactional
    void getAllEventsImagesByImageLpNotContainsSomething() throws Exception {
        // Initialize the database
        eventsImagesRepository.saveAndFlush(eventsImages);

        // Get all the eventsImagesList where imageLp does not contain DEFAULT_IMAGE_LP
        defaultEventsImagesShouldNotBeFound("imageLp.doesNotContain=" + DEFAULT_IMAGE_LP);

        // Get all the eventsImagesList where imageLp does not contain UPDATED_IMAGE_LP
        defaultEventsImagesShouldBeFound("imageLp.doesNotContain=" + UPDATED_IMAGE_LP);
    }

    @Test
    @Transactional
    void getAllEventsImagesByImageThumbIsEqualToSomething() throws Exception {
        // Initialize the database
        eventsImagesRepository.saveAndFlush(eventsImages);

        // Get all the eventsImagesList where imageThumb equals to DEFAULT_IMAGE_THUMB
        defaultEventsImagesShouldBeFound("imageThumb.equals=" + DEFAULT_IMAGE_THUMB);

        // Get all the eventsImagesList where imageThumb equals to UPDATED_IMAGE_THUMB
        defaultEventsImagesShouldNotBeFound("imageThumb.equals=" + UPDATED_IMAGE_THUMB);
    }

    @Test
    @Transactional
    void getAllEventsImagesByImageThumbIsInShouldWork() throws Exception {
        // Initialize the database
        eventsImagesRepository.saveAndFlush(eventsImages);

        // Get all the eventsImagesList where imageThumb in DEFAULT_IMAGE_THUMB or UPDATED_IMAGE_THUMB
        defaultEventsImagesShouldBeFound("imageThumb.in=" + DEFAULT_IMAGE_THUMB + "," + UPDATED_IMAGE_THUMB);

        // Get all the eventsImagesList where imageThumb equals to UPDATED_IMAGE_THUMB
        defaultEventsImagesShouldNotBeFound("imageThumb.in=" + UPDATED_IMAGE_THUMB);
    }

    @Test
    @Transactional
    void getAllEventsImagesByImageThumbIsNullOrNotNull() throws Exception {
        // Initialize the database
        eventsImagesRepository.saveAndFlush(eventsImages);

        // Get all the eventsImagesList where imageThumb is not null
        defaultEventsImagesShouldBeFound("imageThumb.specified=true");

        // Get all the eventsImagesList where imageThumb is null
        defaultEventsImagesShouldNotBeFound("imageThumb.specified=false");
    }

    @Test
    @Transactional
    void getAllEventsImagesByImageThumbContainsSomething() throws Exception {
        // Initialize the database
        eventsImagesRepository.saveAndFlush(eventsImages);

        // Get all the eventsImagesList where imageThumb contains DEFAULT_IMAGE_THUMB
        defaultEventsImagesShouldBeFound("imageThumb.contains=" + DEFAULT_IMAGE_THUMB);

        // Get all the eventsImagesList where imageThumb contains UPDATED_IMAGE_THUMB
        defaultEventsImagesShouldNotBeFound("imageThumb.contains=" + UPDATED_IMAGE_THUMB);
    }

    @Test
    @Transactional
    void getAllEventsImagesByImageThumbNotContainsSomething() throws Exception {
        // Initialize the database
        eventsImagesRepository.saveAndFlush(eventsImages);

        // Get all the eventsImagesList where imageThumb does not contain DEFAULT_IMAGE_THUMB
        defaultEventsImagesShouldNotBeFound("imageThumb.doesNotContain=" + DEFAULT_IMAGE_THUMB);

        // Get all the eventsImagesList where imageThumb does not contain UPDATED_IMAGE_THUMB
        defaultEventsImagesShouldBeFound("imageThumb.doesNotContain=" + UPDATED_IMAGE_THUMB);
    }

    @Test
    @Transactional
    void getAllEventsImagesByProcessingTimeIsEqualToSomething() throws Exception {
        // Initialize the database
        eventsImagesRepository.saveAndFlush(eventsImages);

        // Get all the eventsImagesList where processingTime equals to DEFAULT_PROCESSING_TIME
        defaultEventsImagesShouldBeFound("processingTime.equals=" + DEFAULT_PROCESSING_TIME);

        // Get all the eventsImagesList where processingTime equals to UPDATED_PROCESSING_TIME
        defaultEventsImagesShouldNotBeFound("processingTime.equals=" + UPDATED_PROCESSING_TIME);
    }

    @Test
    @Transactional
    void getAllEventsImagesByProcessingTimeIsInShouldWork() throws Exception {
        // Initialize the database
        eventsImagesRepository.saveAndFlush(eventsImages);

        // Get all the eventsImagesList where processingTime in DEFAULT_PROCESSING_TIME or UPDATED_PROCESSING_TIME
        defaultEventsImagesShouldBeFound("processingTime.in=" + DEFAULT_PROCESSING_TIME + "," + UPDATED_PROCESSING_TIME);

        // Get all the eventsImagesList where processingTime equals to UPDATED_PROCESSING_TIME
        defaultEventsImagesShouldNotBeFound("processingTime.in=" + UPDATED_PROCESSING_TIME);
    }

    @Test
    @Transactional
    void getAllEventsImagesByProcessingTimeIsNullOrNotNull() throws Exception {
        // Initialize the database
        eventsImagesRepository.saveAndFlush(eventsImages);

        // Get all the eventsImagesList where processingTime is not null
        defaultEventsImagesShouldBeFound("processingTime.specified=true");

        // Get all the eventsImagesList where processingTime is null
        defaultEventsImagesShouldNotBeFound("processingTime.specified=false");
    }

    @Test
    @Transactional
    void getAllEventsImagesByProcessingTimeIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        eventsImagesRepository.saveAndFlush(eventsImages);

        // Get all the eventsImagesList where processingTime is greater than or equal to DEFAULT_PROCESSING_TIME
        defaultEventsImagesShouldBeFound("processingTime.greaterThanOrEqual=" + DEFAULT_PROCESSING_TIME);

        // Get all the eventsImagesList where processingTime is greater than or equal to UPDATED_PROCESSING_TIME
        defaultEventsImagesShouldNotBeFound("processingTime.greaterThanOrEqual=" + UPDATED_PROCESSING_TIME);
    }

    @Test
    @Transactional
    void getAllEventsImagesByProcessingTimeIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        eventsImagesRepository.saveAndFlush(eventsImages);

        // Get all the eventsImagesList where processingTime is less than or equal to DEFAULT_PROCESSING_TIME
        defaultEventsImagesShouldBeFound("processingTime.lessThanOrEqual=" + DEFAULT_PROCESSING_TIME);

        // Get all the eventsImagesList where processingTime is less than or equal to SMALLER_PROCESSING_TIME
        defaultEventsImagesShouldNotBeFound("processingTime.lessThanOrEqual=" + SMALLER_PROCESSING_TIME);
    }

    @Test
    @Transactional
    void getAllEventsImagesByProcessingTimeIsLessThanSomething() throws Exception {
        // Initialize the database
        eventsImagesRepository.saveAndFlush(eventsImages);

        // Get all the eventsImagesList where processingTime is less than DEFAULT_PROCESSING_TIME
        defaultEventsImagesShouldNotBeFound("processingTime.lessThan=" + DEFAULT_PROCESSING_TIME);

        // Get all the eventsImagesList where processingTime is less than UPDATED_PROCESSING_TIME
        defaultEventsImagesShouldBeFound("processingTime.lessThan=" + UPDATED_PROCESSING_TIME);
    }

    @Test
    @Transactional
    void getAllEventsImagesByProcessingTimeIsGreaterThanSomething() throws Exception {
        // Initialize the database
        eventsImagesRepository.saveAndFlush(eventsImages);

        // Get all the eventsImagesList where processingTime is greater than DEFAULT_PROCESSING_TIME
        defaultEventsImagesShouldNotBeFound("processingTime.greaterThan=" + DEFAULT_PROCESSING_TIME);

        // Get all the eventsImagesList where processingTime is greater than SMALLER_PROCESSING_TIME
        defaultEventsImagesShouldBeFound("processingTime.greaterThan=" + SMALLER_PROCESSING_TIME);
    }

    @Test
    @Transactional
    void getAllEventsImagesByRuleRcvdIsEqualToSomething() throws Exception {
        // Initialize the database
        eventsImagesRepository.saveAndFlush(eventsImages);

        // Get all the eventsImagesList where ruleRcvd equals to DEFAULT_RULE_RCVD
        defaultEventsImagesShouldBeFound("ruleRcvd.equals=" + DEFAULT_RULE_RCVD);

        // Get all the eventsImagesList where ruleRcvd equals to UPDATED_RULE_RCVD
        defaultEventsImagesShouldNotBeFound("ruleRcvd.equals=" + UPDATED_RULE_RCVD);
    }

    @Test
    @Transactional
    void getAllEventsImagesByRuleRcvdIsInShouldWork() throws Exception {
        // Initialize the database
        eventsImagesRepository.saveAndFlush(eventsImages);

        // Get all the eventsImagesList where ruleRcvd in DEFAULT_RULE_RCVD or UPDATED_RULE_RCVD
        defaultEventsImagesShouldBeFound("ruleRcvd.in=" + DEFAULT_RULE_RCVD + "," + UPDATED_RULE_RCVD);

        // Get all the eventsImagesList where ruleRcvd equals to UPDATED_RULE_RCVD
        defaultEventsImagesShouldNotBeFound("ruleRcvd.in=" + UPDATED_RULE_RCVD);
    }

    @Test
    @Transactional
    void getAllEventsImagesByRuleRcvdIsNullOrNotNull() throws Exception {
        // Initialize the database
        eventsImagesRepository.saveAndFlush(eventsImages);

        // Get all the eventsImagesList where ruleRcvd is not null
        defaultEventsImagesShouldBeFound("ruleRcvd.specified=true");

        // Get all the eventsImagesList where ruleRcvd is null
        defaultEventsImagesShouldNotBeFound("ruleRcvd.specified=false");
    }

    @Test
    @Transactional
    void getAllEventsImagesByRuleRcvdIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        eventsImagesRepository.saveAndFlush(eventsImages);

        // Get all the eventsImagesList where ruleRcvd is greater than or equal to DEFAULT_RULE_RCVD
        defaultEventsImagesShouldBeFound("ruleRcvd.greaterThanOrEqual=" + DEFAULT_RULE_RCVD);

        // Get all the eventsImagesList where ruleRcvd is greater than or equal to UPDATED_RULE_RCVD
        defaultEventsImagesShouldNotBeFound("ruleRcvd.greaterThanOrEqual=" + UPDATED_RULE_RCVD);
    }

    @Test
    @Transactional
    void getAllEventsImagesByRuleRcvdIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        eventsImagesRepository.saveAndFlush(eventsImages);

        // Get all the eventsImagesList where ruleRcvd is less than or equal to DEFAULT_RULE_RCVD
        defaultEventsImagesShouldBeFound("ruleRcvd.lessThanOrEqual=" + DEFAULT_RULE_RCVD);

        // Get all the eventsImagesList where ruleRcvd is less than or equal to SMALLER_RULE_RCVD
        defaultEventsImagesShouldNotBeFound("ruleRcvd.lessThanOrEqual=" + SMALLER_RULE_RCVD);
    }

    @Test
    @Transactional
    void getAllEventsImagesByRuleRcvdIsLessThanSomething() throws Exception {
        // Initialize the database
        eventsImagesRepository.saveAndFlush(eventsImages);

        // Get all the eventsImagesList where ruleRcvd is less than DEFAULT_RULE_RCVD
        defaultEventsImagesShouldNotBeFound("ruleRcvd.lessThan=" + DEFAULT_RULE_RCVD);

        // Get all the eventsImagesList where ruleRcvd is less than UPDATED_RULE_RCVD
        defaultEventsImagesShouldBeFound("ruleRcvd.lessThan=" + UPDATED_RULE_RCVD);
    }

    @Test
    @Transactional
    void getAllEventsImagesByRuleRcvdIsGreaterThanSomething() throws Exception {
        // Initialize the database
        eventsImagesRepository.saveAndFlush(eventsImages);

        // Get all the eventsImagesList where ruleRcvd is greater than DEFAULT_RULE_RCVD
        defaultEventsImagesShouldNotBeFound("ruleRcvd.greaterThan=" + DEFAULT_RULE_RCVD);

        // Get all the eventsImagesList where ruleRcvd is greater than SMALLER_RULE_RCVD
        defaultEventsImagesShouldBeFound("ruleRcvd.greaterThan=" + SMALLER_RULE_RCVD);
    }

    @Test
    @Transactional
    void getAllEventsImagesByRuleSentIsEqualToSomething() throws Exception {
        // Initialize the database
        eventsImagesRepository.saveAndFlush(eventsImages);

        // Get all the eventsImagesList where ruleSent equals to DEFAULT_RULE_SENT
        defaultEventsImagesShouldBeFound("ruleSent.equals=" + DEFAULT_RULE_SENT);

        // Get all the eventsImagesList where ruleSent equals to UPDATED_RULE_SENT
        defaultEventsImagesShouldNotBeFound("ruleSent.equals=" + UPDATED_RULE_SENT);
    }

    @Test
    @Transactional
    void getAllEventsImagesByRuleSentIsInShouldWork() throws Exception {
        // Initialize the database
        eventsImagesRepository.saveAndFlush(eventsImages);

        // Get all the eventsImagesList where ruleSent in DEFAULT_RULE_SENT or UPDATED_RULE_SENT
        defaultEventsImagesShouldBeFound("ruleSent.in=" + DEFAULT_RULE_SENT + "," + UPDATED_RULE_SENT);

        // Get all the eventsImagesList where ruleSent equals to UPDATED_RULE_SENT
        defaultEventsImagesShouldNotBeFound("ruleSent.in=" + UPDATED_RULE_SENT);
    }

    @Test
    @Transactional
    void getAllEventsImagesByRuleSentIsNullOrNotNull() throws Exception {
        // Initialize the database
        eventsImagesRepository.saveAndFlush(eventsImages);

        // Get all the eventsImagesList where ruleSent is not null
        defaultEventsImagesShouldBeFound("ruleSent.specified=true");

        // Get all the eventsImagesList where ruleSent is null
        defaultEventsImagesShouldNotBeFound("ruleSent.specified=false");
    }

    @Test
    @Transactional
    void getAllEventsImagesByRuleSentIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        eventsImagesRepository.saveAndFlush(eventsImages);

        // Get all the eventsImagesList where ruleSent is greater than or equal to DEFAULT_RULE_SENT
        defaultEventsImagesShouldBeFound("ruleSent.greaterThanOrEqual=" + DEFAULT_RULE_SENT);

        // Get all the eventsImagesList where ruleSent is greater than or equal to UPDATED_RULE_SENT
        defaultEventsImagesShouldNotBeFound("ruleSent.greaterThanOrEqual=" + UPDATED_RULE_SENT);
    }

    @Test
    @Transactional
    void getAllEventsImagesByRuleSentIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        eventsImagesRepository.saveAndFlush(eventsImages);

        // Get all the eventsImagesList where ruleSent is less than or equal to DEFAULT_RULE_SENT
        defaultEventsImagesShouldBeFound("ruleSent.lessThanOrEqual=" + DEFAULT_RULE_SENT);

        // Get all the eventsImagesList where ruleSent is less than or equal to SMALLER_RULE_SENT
        defaultEventsImagesShouldNotBeFound("ruleSent.lessThanOrEqual=" + SMALLER_RULE_SENT);
    }

    @Test
    @Transactional
    void getAllEventsImagesByRuleSentIsLessThanSomething() throws Exception {
        // Initialize the database
        eventsImagesRepository.saveAndFlush(eventsImages);

        // Get all the eventsImagesList where ruleSent is less than DEFAULT_RULE_SENT
        defaultEventsImagesShouldNotBeFound("ruleSent.lessThan=" + DEFAULT_RULE_SENT);

        // Get all the eventsImagesList where ruleSent is less than UPDATED_RULE_SENT
        defaultEventsImagesShouldBeFound("ruleSent.lessThan=" + UPDATED_RULE_SENT);
    }

    @Test
    @Transactional
    void getAllEventsImagesByRuleSentIsGreaterThanSomething() throws Exception {
        // Initialize the database
        eventsImagesRepository.saveAndFlush(eventsImages);

        // Get all the eventsImagesList where ruleSent is greater than DEFAULT_RULE_SENT
        defaultEventsImagesShouldNotBeFound("ruleSent.greaterThan=" + DEFAULT_RULE_SENT);

        // Get all the eventsImagesList where ruleSent is greater than SMALLER_RULE_SENT
        defaultEventsImagesShouldBeFound("ruleSent.greaterThan=" + SMALLER_RULE_SENT);
    }

    @Test
    @Transactional
    void getAllEventsImagesByWhenIsEqualToSomething() throws Exception {
        // Initialize the database
        eventsImagesRepository.saveAndFlush(eventsImages);

        // Get all the eventsImagesList where when equals to DEFAULT_WHEN
        defaultEventsImagesShouldBeFound("when.equals=" + DEFAULT_WHEN);

        // Get all the eventsImagesList where when equals to UPDATED_WHEN
        defaultEventsImagesShouldNotBeFound("when.equals=" + UPDATED_WHEN);
    }

    @Test
    @Transactional
    void getAllEventsImagesByWhenIsInShouldWork() throws Exception {
        // Initialize the database
        eventsImagesRepository.saveAndFlush(eventsImages);

        // Get all the eventsImagesList where when in DEFAULT_WHEN or UPDATED_WHEN
        defaultEventsImagesShouldBeFound("when.in=" + DEFAULT_WHEN + "," + UPDATED_WHEN);

        // Get all the eventsImagesList where when equals to UPDATED_WHEN
        defaultEventsImagesShouldNotBeFound("when.in=" + UPDATED_WHEN);
    }

    @Test
    @Transactional
    void getAllEventsImagesByWhenIsNullOrNotNull() throws Exception {
        // Initialize the database
        eventsImagesRepository.saveAndFlush(eventsImages);

        // Get all the eventsImagesList where when is not null
        defaultEventsImagesShouldBeFound("when.specified=true");

        // Get all the eventsImagesList where when is null
        defaultEventsImagesShouldNotBeFound("when.specified=false");
    }

    @Test
    @Transactional
    void getAllEventsImagesByWhenIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        eventsImagesRepository.saveAndFlush(eventsImages);

        // Get all the eventsImagesList where when is greater than or equal to DEFAULT_WHEN
        defaultEventsImagesShouldBeFound("when.greaterThanOrEqual=" + DEFAULT_WHEN);

        // Get all the eventsImagesList where when is greater than or equal to UPDATED_WHEN
        defaultEventsImagesShouldNotBeFound("when.greaterThanOrEqual=" + UPDATED_WHEN);
    }

    @Test
    @Transactional
    void getAllEventsImagesByWhenIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        eventsImagesRepository.saveAndFlush(eventsImages);

        // Get all the eventsImagesList where when is less than or equal to DEFAULT_WHEN
        defaultEventsImagesShouldBeFound("when.lessThanOrEqual=" + DEFAULT_WHEN);

        // Get all the eventsImagesList where when is less than or equal to SMALLER_WHEN
        defaultEventsImagesShouldNotBeFound("when.lessThanOrEqual=" + SMALLER_WHEN);
    }

    @Test
    @Transactional
    void getAllEventsImagesByWhenIsLessThanSomething() throws Exception {
        // Initialize the database
        eventsImagesRepository.saveAndFlush(eventsImages);

        // Get all the eventsImagesList where when is less than DEFAULT_WHEN
        defaultEventsImagesShouldNotBeFound("when.lessThan=" + DEFAULT_WHEN);

        // Get all the eventsImagesList where when is less than UPDATED_WHEN
        defaultEventsImagesShouldBeFound("when.lessThan=" + UPDATED_WHEN);
    }

    @Test
    @Transactional
    void getAllEventsImagesByWhenIsGreaterThanSomething() throws Exception {
        // Initialize the database
        eventsImagesRepository.saveAndFlush(eventsImages);

        // Get all the eventsImagesList where when is greater than DEFAULT_WHEN
        defaultEventsImagesShouldNotBeFound("when.greaterThan=" + DEFAULT_WHEN);

        // Get all the eventsImagesList where when is greater than SMALLER_WHEN
        defaultEventsImagesShouldBeFound("when.greaterThan=" + SMALLER_WHEN);
    }

    @Test
    @Transactional
    void getAllEventsImagesByGantryProcessedIsEqualToSomething() throws Exception {
        // Initialize the database
        eventsImagesRepository.saveAndFlush(eventsImages);

        // Get all the eventsImagesList where gantryProcessed equals to DEFAULT_GANTRY_PROCESSED
        defaultEventsImagesShouldBeFound("gantryProcessed.equals=" + DEFAULT_GANTRY_PROCESSED);

        // Get all the eventsImagesList where gantryProcessed equals to UPDATED_GANTRY_PROCESSED
        defaultEventsImagesShouldNotBeFound("gantryProcessed.equals=" + UPDATED_GANTRY_PROCESSED);
    }

    @Test
    @Transactional
    void getAllEventsImagesByGantryProcessedIsInShouldWork() throws Exception {
        // Initialize the database
        eventsImagesRepository.saveAndFlush(eventsImages);

        // Get all the eventsImagesList where gantryProcessed in DEFAULT_GANTRY_PROCESSED or UPDATED_GANTRY_PROCESSED
        defaultEventsImagesShouldBeFound("gantryProcessed.in=" + DEFAULT_GANTRY_PROCESSED + "," + UPDATED_GANTRY_PROCESSED);

        // Get all the eventsImagesList where gantryProcessed equals to UPDATED_GANTRY_PROCESSED
        defaultEventsImagesShouldNotBeFound("gantryProcessed.in=" + UPDATED_GANTRY_PROCESSED);
    }

    @Test
    @Transactional
    void getAllEventsImagesByGantryProcessedIsNullOrNotNull() throws Exception {
        // Initialize the database
        eventsImagesRepository.saveAndFlush(eventsImages);

        // Get all the eventsImagesList where gantryProcessed is not null
        defaultEventsImagesShouldBeFound("gantryProcessed.specified=true");

        // Get all the eventsImagesList where gantryProcessed is null
        defaultEventsImagesShouldNotBeFound("gantryProcessed.specified=false");
    }

    @Test
    @Transactional
    void getAllEventsImagesByGantryProcessedIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        eventsImagesRepository.saveAndFlush(eventsImages);

        // Get all the eventsImagesList where gantryProcessed is greater than or equal to DEFAULT_GANTRY_PROCESSED
        defaultEventsImagesShouldBeFound("gantryProcessed.greaterThanOrEqual=" + DEFAULT_GANTRY_PROCESSED);

        // Get all the eventsImagesList where gantryProcessed is greater than or equal to UPDATED_GANTRY_PROCESSED
        defaultEventsImagesShouldNotBeFound("gantryProcessed.greaterThanOrEqual=" + UPDATED_GANTRY_PROCESSED);
    }

    @Test
    @Transactional
    void getAllEventsImagesByGantryProcessedIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        eventsImagesRepository.saveAndFlush(eventsImages);

        // Get all the eventsImagesList where gantryProcessed is less than or equal to DEFAULT_GANTRY_PROCESSED
        defaultEventsImagesShouldBeFound("gantryProcessed.lessThanOrEqual=" + DEFAULT_GANTRY_PROCESSED);

        // Get all the eventsImagesList where gantryProcessed is less than or equal to SMALLER_GANTRY_PROCESSED
        defaultEventsImagesShouldNotBeFound("gantryProcessed.lessThanOrEqual=" + SMALLER_GANTRY_PROCESSED);
    }

    @Test
    @Transactional
    void getAllEventsImagesByGantryProcessedIsLessThanSomething() throws Exception {
        // Initialize the database
        eventsImagesRepository.saveAndFlush(eventsImages);

        // Get all the eventsImagesList where gantryProcessed is less than DEFAULT_GANTRY_PROCESSED
        defaultEventsImagesShouldNotBeFound("gantryProcessed.lessThan=" + DEFAULT_GANTRY_PROCESSED);

        // Get all the eventsImagesList where gantryProcessed is less than UPDATED_GANTRY_PROCESSED
        defaultEventsImagesShouldBeFound("gantryProcessed.lessThan=" + UPDATED_GANTRY_PROCESSED);
    }

    @Test
    @Transactional
    void getAllEventsImagesByGantryProcessedIsGreaterThanSomething() throws Exception {
        // Initialize the database
        eventsImagesRepository.saveAndFlush(eventsImages);

        // Get all the eventsImagesList where gantryProcessed is greater than DEFAULT_GANTRY_PROCESSED
        defaultEventsImagesShouldNotBeFound("gantryProcessed.greaterThan=" + DEFAULT_GANTRY_PROCESSED);

        // Get all the eventsImagesList where gantryProcessed is greater than SMALLER_GANTRY_PROCESSED
        defaultEventsImagesShouldBeFound("gantryProcessed.greaterThan=" + SMALLER_GANTRY_PROCESSED);
    }

    @Test
    @Transactional
    void getAllEventsImagesByGantrySentIsEqualToSomething() throws Exception {
        // Initialize the database
        eventsImagesRepository.saveAndFlush(eventsImages);

        // Get all the eventsImagesList where gantrySent equals to DEFAULT_GANTRY_SENT
        defaultEventsImagesShouldBeFound("gantrySent.equals=" + DEFAULT_GANTRY_SENT);

        // Get all the eventsImagesList where gantrySent equals to UPDATED_GANTRY_SENT
        defaultEventsImagesShouldNotBeFound("gantrySent.equals=" + UPDATED_GANTRY_SENT);
    }

    @Test
    @Transactional
    void getAllEventsImagesByGantrySentIsInShouldWork() throws Exception {
        // Initialize the database
        eventsImagesRepository.saveAndFlush(eventsImages);

        // Get all the eventsImagesList where gantrySent in DEFAULT_GANTRY_SENT or UPDATED_GANTRY_SENT
        defaultEventsImagesShouldBeFound("gantrySent.in=" + DEFAULT_GANTRY_SENT + "," + UPDATED_GANTRY_SENT);

        // Get all the eventsImagesList where gantrySent equals to UPDATED_GANTRY_SENT
        defaultEventsImagesShouldNotBeFound("gantrySent.in=" + UPDATED_GANTRY_SENT);
    }

    @Test
    @Transactional
    void getAllEventsImagesByGantrySentIsNullOrNotNull() throws Exception {
        // Initialize the database
        eventsImagesRepository.saveAndFlush(eventsImages);

        // Get all the eventsImagesList where gantrySent is not null
        defaultEventsImagesShouldBeFound("gantrySent.specified=true");

        // Get all the eventsImagesList where gantrySent is null
        defaultEventsImagesShouldNotBeFound("gantrySent.specified=false");
    }

    @Test
    @Transactional
    void getAllEventsImagesByGantrySentIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        eventsImagesRepository.saveAndFlush(eventsImages);

        // Get all the eventsImagesList where gantrySent is greater than or equal to DEFAULT_GANTRY_SENT
        defaultEventsImagesShouldBeFound("gantrySent.greaterThanOrEqual=" + DEFAULT_GANTRY_SENT);

        // Get all the eventsImagesList where gantrySent is greater than or equal to UPDATED_GANTRY_SENT
        defaultEventsImagesShouldNotBeFound("gantrySent.greaterThanOrEqual=" + UPDATED_GANTRY_SENT);
    }

    @Test
    @Transactional
    void getAllEventsImagesByGantrySentIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        eventsImagesRepository.saveAndFlush(eventsImages);

        // Get all the eventsImagesList where gantrySent is less than or equal to DEFAULT_GANTRY_SENT
        defaultEventsImagesShouldBeFound("gantrySent.lessThanOrEqual=" + DEFAULT_GANTRY_SENT);

        // Get all the eventsImagesList where gantrySent is less than or equal to SMALLER_GANTRY_SENT
        defaultEventsImagesShouldNotBeFound("gantrySent.lessThanOrEqual=" + SMALLER_GANTRY_SENT);
    }

    @Test
    @Transactional
    void getAllEventsImagesByGantrySentIsLessThanSomething() throws Exception {
        // Initialize the database
        eventsImagesRepository.saveAndFlush(eventsImages);

        // Get all the eventsImagesList where gantrySent is less than DEFAULT_GANTRY_SENT
        defaultEventsImagesShouldNotBeFound("gantrySent.lessThan=" + DEFAULT_GANTRY_SENT);

        // Get all the eventsImagesList where gantrySent is less than UPDATED_GANTRY_SENT
        defaultEventsImagesShouldBeFound("gantrySent.lessThan=" + UPDATED_GANTRY_SENT);
    }

    @Test
    @Transactional
    void getAllEventsImagesByGantrySentIsGreaterThanSomething() throws Exception {
        // Initialize the database
        eventsImagesRepository.saveAndFlush(eventsImages);

        // Get all the eventsImagesList where gantrySent is greater than DEFAULT_GANTRY_SENT
        defaultEventsImagesShouldNotBeFound("gantrySent.greaterThan=" + DEFAULT_GANTRY_SENT);

        // Get all the eventsImagesList where gantrySent is greater than SMALLER_GANTRY_SENT
        defaultEventsImagesShouldBeFound("gantrySent.greaterThan=" + SMALLER_GANTRY_SENT);
    }

    @Test
    @Transactional
    void getAllEventsImagesByStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        eventsImagesRepository.saveAndFlush(eventsImages);

        // Get all the eventsImagesList where status equals to DEFAULT_STATUS
        defaultEventsImagesShouldBeFound("status.equals=" + DEFAULT_STATUS);

        // Get all the eventsImagesList where status equals to UPDATED_STATUS
        defaultEventsImagesShouldNotBeFound("status.equals=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllEventsImagesByStatusIsInShouldWork() throws Exception {
        // Initialize the database
        eventsImagesRepository.saveAndFlush(eventsImages);

        // Get all the eventsImagesList where status in DEFAULT_STATUS or UPDATED_STATUS
        defaultEventsImagesShouldBeFound("status.in=" + DEFAULT_STATUS + "," + UPDATED_STATUS);

        // Get all the eventsImagesList where status equals to UPDATED_STATUS
        defaultEventsImagesShouldNotBeFound("status.in=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllEventsImagesByStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        eventsImagesRepository.saveAndFlush(eventsImages);

        // Get all the eventsImagesList where status is not null
        defaultEventsImagesShouldBeFound("status.specified=true");

        // Get all the eventsImagesList where status is null
        defaultEventsImagesShouldNotBeFound("status.specified=false");
    }

    @Test
    @Transactional
    void getAllEventsImagesByStatusContainsSomething() throws Exception {
        // Initialize the database
        eventsImagesRepository.saveAndFlush(eventsImages);

        // Get all the eventsImagesList where status contains DEFAULT_STATUS
        defaultEventsImagesShouldBeFound("status.contains=" + DEFAULT_STATUS);

        // Get all the eventsImagesList where status contains UPDATED_STATUS
        defaultEventsImagesShouldNotBeFound("status.contains=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllEventsImagesByStatusNotContainsSomething() throws Exception {
        // Initialize the database
        eventsImagesRepository.saveAndFlush(eventsImages);

        // Get all the eventsImagesList where status does not contain DEFAULT_STATUS
        defaultEventsImagesShouldNotBeFound("status.doesNotContain=" + DEFAULT_STATUS);

        // Get all the eventsImagesList where status does not contain UPDATED_STATUS
        defaultEventsImagesShouldBeFound("status.doesNotContain=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllEventsImagesByDataStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        eventsImagesRepository.saveAndFlush(eventsImages);

        // Get all the eventsImagesList where dataStatus equals to DEFAULT_DATA_STATUS
        defaultEventsImagesShouldBeFound("dataStatus.equals=" + DEFAULT_DATA_STATUS);

        // Get all the eventsImagesList where dataStatus equals to UPDATED_DATA_STATUS
        defaultEventsImagesShouldNotBeFound("dataStatus.equals=" + UPDATED_DATA_STATUS);
    }

    @Test
    @Transactional
    void getAllEventsImagesByDataStatusIsInShouldWork() throws Exception {
        // Initialize the database
        eventsImagesRepository.saveAndFlush(eventsImages);

        // Get all the eventsImagesList where dataStatus in DEFAULT_DATA_STATUS or UPDATED_DATA_STATUS
        defaultEventsImagesShouldBeFound("dataStatus.in=" + DEFAULT_DATA_STATUS + "," + UPDATED_DATA_STATUS);

        // Get all the eventsImagesList where dataStatus equals to UPDATED_DATA_STATUS
        defaultEventsImagesShouldNotBeFound("dataStatus.in=" + UPDATED_DATA_STATUS);
    }

    @Test
    @Transactional
    void getAllEventsImagesByDataStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        eventsImagesRepository.saveAndFlush(eventsImages);

        // Get all the eventsImagesList where dataStatus is not null
        defaultEventsImagesShouldBeFound("dataStatus.specified=true");

        // Get all the eventsImagesList where dataStatus is null
        defaultEventsImagesShouldNotBeFound("dataStatus.specified=false");
    }

    @Test
    @Transactional
    void getAllEventsImagesByDataStatusContainsSomething() throws Exception {
        // Initialize the database
        eventsImagesRepository.saveAndFlush(eventsImages);

        // Get all the eventsImagesList where dataStatus contains DEFAULT_DATA_STATUS
        defaultEventsImagesShouldBeFound("dataStatus.contains=" + DEFAULT_DATA_STATUS);

        // Get all the eventsImagesList where dataStatus contains UPDATED_DATA_STATUS
        defaultEventsImagesShouldNotBeFound("dataStatus.contains=" + UPDATED_DATA_STATUS);
    }

    @Test
    @Transactional
    void getAllEventsImagesByDataStatusNotContainsSomething() throws Exception {
        // Initialize the database
        eventsImagesRepository.saveAndFlush(eventsImages);

        // Get all the eventsImagesList where dataStatus does not contain DEFAULT_DATA_STATUS
        defaultEventsImagesShouldNotBeFound("dataStatus.doesNotContain=" + DEFAULT_DATA_STATUS);

        // Get all the eventsImagesList where dataStatus does not contain UPDATED_DATA_STATUS
        defaultEventsImagesShouldBeFound("dataStatus.doesNotContain=" + UPDATED_DATA_STATUS);
    }

    @Test
    @Transactional
    void getAllEventsImagesByThreadRandNoIsEqualToSomething() throws Exception {
        // Initialize the database
        eventsImagesRepository.saveAndFlush(eventsImages);

        // Get all the eventsImagesList where threadRandNo equals to DEFAULT_THREAD_RAND_NO
        defaultEventsImagesShouldBeFound("threadRandNo.equals=" + DEFAULT_THREAD_RAND_NO);

        // Get all the eventsImagesList where threadRandNo equals to UPDATED_THREAD_RAND_NO
        defaultEventsImagesShouldNotBeFound("threadRandNo.equals=" + UPDATED_THREAD_RAND_NO);
    }

    @Test
    @Transactional
    void getAllEventsImagesByThreadRandNoIsInShouldWork() throws Exception {
        // Initialize the database
        eventsImagesRepository.saveAndFlush(eventsImages);

        // Get all the eventsImagesList where threadRandNo in DEFAULT_THREAD_RAND_NO or UPDATED_THREAD_RAND_NO
        defaultEventsImagesShouldBeFound("threadRandNo.in=" + DEFAULT_THREAD_RAND_NO + "," + UPDATED_THREAD_RAND_NO);

        // Get all the eventsImagesList where threadRandNo equals to UPDATED_THREAD_RAND_NO
        defaultEventsImagesShouldNotBeFound("threadRandNo.in=" + UPDATED_THREAD_RAND_NO);
    }

    @Test
    @Transactional
    void getAllEventsImagesByThreadRandNoIsNullOrNotNull() throws Exception {
        // Initialize the database
        eventsImagesRepository.saveAndFlush(eventsImages);

        // Get all the eventsImagesList where threadRandNo is not null
        defaultEventsImagesShouldBeFound("threadRandNo.specified=true");

        // Get all the eventsImagesList where threadRandNo is null
        defaultEventsImagesShouldNotBeFound("threadRandNo.specified=false");
    }

    @Test
    @Transactional
    void getAllEventsImagesByThreadRandNoContainsSomething() throws Exception {
        // Initialize the database
        eventsImagesRepository.saveAndFlush(eventsImages);

        // Get all the eventsImagesList where threadRandNo contains DEFAULT_THREAD_RAND_NO
        defaultEventsImagesShouldBeFound("threadRandNo.contains=" + DEFAULT_THREAD_RAND_NO);

        // Get all the eventsImagesList where threadRandNo contains UPDATED_THREAD_RAND_NO
        defaultEventsImagesShouldNotBeFound("threadRandNo.contains=" + UPDATED_THREAD_RAND_NO);
    }

    @Test
    @Transactional
    void getAllEventsImagesByThreadRandNoNotContainsSomething() throws Exception {
        // Initialize the database
        eventsImagesRepository.saveAndFlush(eventsImages);

        // Get all the eventsImagesList where threadRandNo does not contain DEFAULT_THREAD_RAND_NO
        defaultEventsImagesShouldNotBeFound("threadRandNo.doesNotContain=" + DEFAULT_THREAD_RAND_NO);

        // Get all the eventsImagesList where threadRandNo does not contain UPDATED_THREAD_RAND_NO
        defaultEventsImagesShouldBeFound("threadRandNo.doesNotContain=" + UPDATED_THREAD_RAND_NO);
    }

    @Test
    @Transactional
    void getAllEventsImagesByGantryIsEqualToSomething() throws Exception {
        // Initialize the database
        eventsImagesRepository.saveAndFlush(eventsImages);

        // Get all the eventsImagesList where gantry equals to DEFAULT_GANTRY
        defaultEventsImagesShouldBeFound("gantry.equals=" + DEFAULT_GANTRY);

        // Get all the eventsImagesList where gantry equals to UPDATED_GANTRY
        defaultEventsImagesShouldNotBeFound("gantry.equals=" + UPDATED_GANTRY);
    }

    @Test
    @Transactional
    void getAllEventsImagesByGantryIsInShouldWork() throws Exception {
        // Initialize the database
        eventsImagesRepository.saveAndFlush(eventsImages);

        // Get all the eventsImagesList where gantry in DEFAULT_GANTRY or UPDATED_GANTRY
        defaultEventsImagesShouldBeFound("gantry.in=" + DEFAULT_GANTRY + "," + UPDATED_GANTRY);

        // Get all the eventsImagesList where gantry equals to UPDATED_GANTRY
        defaultEventsImagesShouldNotBeFound("gantry.in=" + UPDATED_GANTRY);
    }

    @Test
    @Transactional
    void getAllEventsImagesByGantryIsNullOrNotNull() throws Exception {
        // Initialize the database
        eventsImagesRepository.saveAndFlush(eventsImages);

        // Get all the eventsImagesList where gantry is not null
        defaultEventsImagesShouldBeFound("gantry.specified=true");

        // Get all the eventsImagesList where gantry is null
        defaultEventsImagesShouldNotBeFound("gantry.specified=false");
    }

    @Test
    @Transactional
    void getAllEventsImagesByGantryIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        eventsImagesRepository.saveAndFlush(eventsImages);

        // Get all the eventsImagesList where gantry is greater than or equal to DEFAULT_GANTRY
        defaultEventsImagesShouldBeFound("gantry.greaterThanOrEqual=" + DEFAULT_GANTRY);

        // Get all the eventsImagesList where gantry is greater than or equal to UPDATED_GANTRY
        defaultEventsImagesShouldNotBeFound("gantry.greaterThanOrEqual=" + UPDATED_GANTRY);
    }

    @Test
    @Transactional
    void getAllEventsImagesByGantryIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        eventsImagesRepository.saveAndFlush(eventsImages);

        // Get all the eventsImagesList where gantry is less than or equal to DEFAULT_GANTRY
        defaultEventsImagesShouldBeFound("gantry.lessThanOrEqual=" + DEFAULT_GANTRY);

        // Get all the eventsImagesList where gantry is less than or equal to SMALLER_GANTRY
        defaultEventsImagesShouldNotBeFound("gantry.lessThanOrEqual=" + SMALLER_GANTRY);
    }

    @Test
    @Transactional
    void getAllEventsImagesByGantryIsLessThanSomething() throws Exception {
        // Initialize the database
        eventsImagesRepository.saveAndFlush(eventsImages);

        // Get all the eventsImagesList where gantry is less than DEFAULT_GANTRY
        defaultEventsImagesShouldNotBeFound("gantry.lessThan=" + DEFAULT_GANTRY);

        // Get all the eventsImagesList where gantry is less than UPDATED_GANTRY
        defaultEventsImagesShouldBeFound("gantry.lessThan=" + UPDATED_GANTRY);
    }

    @Test
    @Transactional
    void getAllEventsImagesByGantryIsGreaterThanSomething() throws Exception {
        // Initialize the database
        eventsImagesRepository.saveAndFlush(eventsImages);

        // Get all the eventsImagesList where gantry is greater than DEFAULT_GANTRY
        defaultEventsImagesShouldNotBeFound("gantry.greaterThan=" + DEFAULT_GANTRY);

        // Get all the eventsImagesList where gantry is greater than SMALLER_GANTRY
        defaultEventsImagesShouldBeFound("gantry.greaterThan=" + SMALLER_GANTRY);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultEventsImagesShouldBeFound(String filter) throws Exception {
        restEventsImagesMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(eventsImages.getId().intValue())))
            .andExpect(jsonPath("$.[*].guid").value(hasItem(DEFAULT_GUID)))
            .andExpect(jsonPath("$.[*].imageLp").value(hasItem(DEFAULT_IMAGE_LP)))
            .andExpect(jsonPath("$.[*].imageThumb").value(hasItem(DEFAULT_IMAGE_THUMB)))
            .andExpect(jsonPath("$.[*].processingTime").value(hasItem(DEFAULT_PROCESSING_TIME.intValue())))
            .andExpect(jsonPath("$.[*].ruleRcvd").value(hasItem(DEFAULT_RULE_RCVD.intValue())))
            .andExpect(jsonPath("$.[*].ruleSent").value(hasItem(DEFAULT_RULE_SENT.intValue())))
            .andExpect(jsonPath("$.[*].when").value(hasItem(DEFAULT_WHEN.intValue())))
            .andExpect(jsonPath("$.[*].gantryProcessed").value(hasItem(DEFAULT_GANTRY_PROCESSED.intValue())))
            .andExpect(jsonPath("$.[*].gantrySent").value(hasItem(DEFAULT_GANTRY_SENT.intValue())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS)))
            .andExpect(jsonPath("$.[*].dataStatus").value(hasItem(DEFAULT_DATA_STATUS)))
            .andExpect(jsonPath("$.[*].threadRandNo").value(hasItem(DEFAULT_THREAD_RAND_NO)))
            .andExpect(jsonPath("$.[*].gantry").value(hasItem(DEFAULT_GANTRY)));

        // Check, that the count call also returns 1
        restEventsImagesMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultEventsImagesShouldNotBeFound(String filter) throws Exception {
        restEventsImagesMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restEventsImagesMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingEventsImages() throws Exception {
        // Get the eventsImages
        restEventsImagesMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingEventsImages() throws Exception {
        // Initialize the database
        eventsImagesRepository.saveAndFlush(eventsImages);

        int databaseSizeBeforeUpdate = eventsImagesRepository.findAll().size();
        eventsImagesSearchRepository.save(eventsImages);
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(eventsImagesSearchRepository.findAll());

        // Update the eventsImages
        EventsImages updatedEventsImages = eventsImagesRepository.findById(eventsImages.getId()).get();
        // Disconnect from session so that the updates on updatedEventsImages are not directly saved in db
        em.detach(updatedEventsImages);
        updatedEventsImages
            .guid(UPDATED_GUID)
            .imageLp(UPDATED_IMAGE_LP)
            .imageThumb(UPDATED_IMAGE_THUMB)
            .processingTime(UPDATED_PROCESSING_TIME)
            .ruleRcvd(UPDATED_RULE_RCVD)
            .ruleSent(UPDATED_RULE_SENT)
            .when(UPDATED_WHEN)
            .gantryProcessed(UPDATED_GANTRY_PROCESSED)
            .gantrySent(UPDATED_GANTRY_SENT)
            .status(UPDATED_STATUS)
            .dataStatus(UPDATED_DATA_STATUS)
            .threadRandNo(UPDATED_THREAD_RAND_NO)
            .gantry(UPDATED_GANTRY);
        EventsImagesDTO eventsImagesDTO = eventsImagesMapper.toDto(updatedEventsImages);

        restEventsImagesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, eventsImagesDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(eventsImagesDTO))
            )
            .andExpect(status().isOk());

        // Validate the EventsImages in the database
        List<EventsImages> eventsImagesList = eventsImagesRepository.findAll();
        assertThat(eventsImagesList).hasSize(databaseSizeBeforeUpdate);
        EventsImages testEventsImages = eventsImagesList.get(eventsImagesList.size() - 1);
        assertThat(testEventsImages.getGuid()).isEqualTo(UPDATED_GUID);
        assertThat(testEventsImages.getImageLp()).isEqualTo(UPDATED_IMAGE_LP);
        assertThat(testEventsImages.getImageThumb()).isEqualTo(UPDATED_IMAGE_THUMB);
        assertThat(testEventsImages.getProcessingTime()).isEqualTo(UPDATED_PROCESSING_TIME);
        assertThat(testEventsImages.getRuleRcvd()).isEqualTo(UPDATED_RULE_RCVD);
        assertThat(testEventsImages.getRuleSent()).isEqualTo(UPDATED_RULE_SENT);
        assertThat(testEventsImages.getWhen()).isEqualTo(UPDATED_WHEN);
        assertThat(testEventsImages.getGantryProcessed()).isEqualTo(UPDATED_GANTRY_PROCESSED);
        assertThat(testEventsImages.getGantrySent()).isEqualTo(UPDATED_GANTRY_SENT);
        assertThat(testEventsImages.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testEventsImages.getDataStatus()).isEqualTo(UPDATED_DATA_STATUS);
        assertThat(testEventsImages.getThreadRandNo()).isEqualTo(UPDATED_THREAD_RAND_NO);
        assertThat(testEventsImages.getGantry()).isEqualTo(UPDATED_GANTRY);
        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(eventsImagesSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
                List<EventsImages> eventsImagesSearchList = IterableUtils.toList(eventsImagesSearchRepository.findAll());
                EventsImages testEventsImagesSearch = eventsImagesSearchList.get(searchDatabaseSizeAfter - 1);
                assertThat(testEventsImagesSearch.getGuid()).isEqualTo(UPDATED_GUID);
                assertThat(testEventsImagesSearch.getImageLp()).isEqualTo(UPDATED_IMAGE_LP);
                assertThat(testEventsImagesSearch.getImageThumb()).isEqualTo(UPDATED_IMAGE_THUMB);
                assertThat(testEventsImagesSearch.getProcessingTime()).isEqualTo(UPDATED_PROCESSING_TIME);
                assertThat(testEventsImagesSearch.getRuleRcvd()).isEqualTo(UPDATED_RULE_RCVD);
                assertThat(testEventsImagesSearch.getRuleSent()).isEqualTo(UPDATED_RULE_SENT);
                assertThat(testEventsImagesSearch.getWhen()).isEqualTo(UPDATED_WHEN);
                assertThat(testEventsImagesSearch.getGantryProcessed()).isEqualTo(UPDATED_GANTRY_PROCESSED);
                assertThat(testEventsImagesSearch.getGantrySent()).isEqualTo(UPDATED_GANTRY_SENT);
                assertThat(testEventsImagesSearch.getStatus()).isEqualTo(UPDATED_STATUS);
                assertThat(testEventsImagesSearch.getDataStatus()).isEqualTo(UPDATED_DATA_STATUS);
                assertThat(testEventsImagesSearch.getThreadRandNo()).isEqualTo(UPDATED_THREAD_RAND_NO);
                assertThat(testEventsImagesSearch.getGantry()).isEqualTo(UPDATED_GANTRY);
            });
    }

    @Test
    @Transactional
    void putNonExistingEventsImages() throws Exception {
        int databaseSizeBeforeUpdate = eventsImagesRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(eventsImagesSearchRepository.findAll());
        eventsImages.setId(count.incrementAndGet());

        // Create the EventsImages
        EventsImagesDTO eventsImagesDTO = eventsImagesMapper.toDto(eventsImages);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEventsImagesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, eventsImagesDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(eventsImagesDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the EventsImages in the database
        List<EventsImages> eventsImagesList = eventsImagesRepository.findAll();
        assertThat(eventsImagesList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(eventsImagesSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithIdMismatchEventsImages() throws Exception {
        int databaseSizeBeforeUpdate = eventsImagesRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(eventsImagesSearchRepository.findAll());
        eventsImages.setId(count.incrementAndGet());

        // Create the EventsImages
        EventsImagesDTO eventsImagesDTO = eventsImagesMapper.toDto(eventsImages);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEventsImagesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(eventsImagesDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the EventsImages in the database
        List<EventsImages> eventsImagesList = eventsImagesRepository.findAll();
        assertThat(eventsImagesList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(eventsImagesSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamEventsImages() throws Exception {
        int databaseSizeBeforeUpdate = eventsImagesRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(eventsImagesSearchRepository.findAll());
        eventsImages.setId(count.incrementAndGet());

        // Create the EventsImages
        EventsImagesDTO eventsImagesDTO = eventsImagesMapper.toDto(eventsImages);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEventsImagesMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(eventsImagesDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the EventsImages in the database
        List<EventsImages> eventsImagesList = eventsImagesRepository.findAll();
        assertThat(eventsImagesList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(eventsImagesSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void partialUpdateEventsImagesWithPatch() throws Exception {
        // Initialize the database
        eventsImagesRepository.saveAndFlush(eventsImages);

        int databaseSizeBeforeUpdate = eventsImagesRepository.findAll().size();

        // Update the eventsImages using partial update
        EventsImages partialUpdatedEventsImages = new EventsImages();
        partialUpdatedEventsImages.setId(eventsImages.getId());

        partialUpdatedEventsImages
            .imageLp(UPDATED_IMAGE_LP)
            .imageThumb(UPDATED_IMAGE_THUMB)
            .processingTime(UPDATED_PROCESSING_TIME)
            .ruleSent(UPDATED_RULE_SENT)
            .gantryProcessed(UPDATED_GANTRY_PROCESSED)
            .gantrySent(UPDATED_GANTRY_SENT)
            .dataStatus(UPDATED_DATA_STATUS);

        restEventsImagesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEventsImages.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedEventsImages))
            )
            .andExpect(status().isOk());

        // Validate the EventsImages in the database
        List<EventsImages> eventsImagesList = eventsImagesRepository.findAll();
        assertThat(eventsImagesList).hasSize(databaseSizeBeforeUpdate);
        EventsImages testEventsImages = eventsImagesList.get(eventsImagesList.size() - 1);
        assertThat(testEventsImages.getGuid()).isEqualTo(DEFAULT_GUID);
        assertThat(testEventsImages.getImageLp()).isEqualTo(UPDATED_IMAGE_LP);
        assertThat(testEventsImages.getImageThumb()).isEqualTo(UPDATED_IMAGE_THUMB);
        assertThat(testEventsImages.getProcessingTime()).isEqualTo(UPDATED_PROCESSING_TIME);
        assertThat(testEventsImages.getRuleRcvd()).isEqualTo(DEFAULT_RULE_RCVD);
        assertThat(testEventsImages.getRuleSent()).isEqualTo(UPDATED_RULE_SENT);
        assertThat(testEventsImages.getWhen()).isEqualTo(DEFAULT_WHEN);
        assertThat(testEventsImages.getGantryProcessed()).isEqualTo(UPDATED_GANTRY_PROCESSED);
        assertThat(testEventsImages.getGantrySent()).isEqualTo(UPDATED_GANTRY_SENT);
        assertThat(testEventsImages.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testEventsImages.getDataStatus()).isEqualTo(UPDATED_DATA_STATUS);
        assertThat(testEventsImages.getThreadRandNo()).isEqualTo(DEFAULT_THREAD_RAND_NO);
        assertThat(testEventsImages.getGantry()).isEqualTo(DEFAULT_GANTRY);
    }

    @Test
    @Transactional
    void fullUpdateEventsImagesWithPatch() throws Exception {
        // Initialize the database
        eventsImagesRepository.saveAndFlush(eventsImages);

        int databaseSizeBeforeUpdate = eventsImagesRepository.findAll().size();

        // Update the eventsImages using partial update
        EventsImages partialUpdatedEventsImages = new EventsImages();
        partialUpdatedEventsImages.setId(eventsImages.getId());

        partialUpdatedEventsImages
            .guid(UPDATED_GUID)
            .imageLp(UPDATED_IMAGE_LP)
            .imageThumb(UPDATED_IMAGE_THUMB)
            .processingTime(UPDATED_PROCESSING_TIME)
            .ruleRcvd(UPDATED_RULE_RCVD)
            .ruleSent(UPDATED_RULE_SENT)
            .when(UPDATED_WHEN)
            .gantryProcessed(UPDATED_GANTRY_PROCESSED)
            .gantrySent(UPDATED_GANTRY_SENT)
            .status(UPDATED_STATUS)
            .dataStatus(UPDATED_DATA_STATUS)
            .threadRandNo(UPDATED_THREAD_RAND_NO)
            .gantry(UPDATED_GANTRY);

        restEventsImagesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEventsImages.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedEventsImages))
            )
            .andExpect(status().isOk());

        // Validate the EventsImages in the database
        List<EventsImages> eventsImagesList = eventsImagesRepository.findAll();
        assertThat(eventsImagesList).hasSize(databaseSizeBeforeUpdate);
        EventsImages testEventsImages = eventsImagesList.get(eventsImagesList.size() - 1);
        assertThat(testEventsImages.getGuid()).isEqualTo(UPDATED_GUID);
        assertThat(testEventsImages.getImageLp()).isEqualTo(UPDATED_IMAGE_LP);
        assertThat(testEventsImages.getImageThumb()).isEqualTo(UPDATED_IMAGE_THUMB);
        assertThat(testEventsImages.getProcessingTime()).isEqualTo(UPDATED_PROCESSING_TIME);
        assertThat(testEventsImages.getRuleRcvd()).isEqualTo(UPDATED_RULE_RCVD);
        assertThat(testEventsImages.getRuleSent()).isEqualTo(UPDATED_RULE_SENT);
        assertThat(testEventsImages.getWhen()).isEqualTo(UPDATED_WHEN);
        assertThat(testEventsImages.getGantryProcessed()).isEqualTo(UPDATED_GANTRY_PROCESSED);
        assertThat(testEventsImages.getGantrySent()).isEqualTo(UPDATED_GANTRY_SENT);
        assertThat(testEventsImages.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testEventsImages.getDataStatus()).isEqualTo(UPDATED_DATA_STATUS);
        assertThat(testEventsImages.getThreadRandNo()).isEqualTo(UPDATED_THREAD_RAND_NO);
        assertThat(testEventsImages.getGantry()).isEqualTo(UPDATED_GANTRY);
    }

    @Test
    @Transactional
    void patchNonExistingEventsImages() throws Exception {
        int databaseSizeBeforeUpdate = eventsImagesRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(eventsImagesSearchRepository.findAll());
        eventsImages.setId(count.incrementAndGet());

        // Create the EventsImages
        EventsImagesDTO eventsImagesDTO = eventsImagesMapper.toDto(eventsImages);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEventsImagesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, eventsImagesDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(eventsImagesDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the EventsImages in the database
        List<EventsImages> eventsImagesList = eventsImagesRepository.findAll();
        assertThat(eventsImagesList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(eventsImagesSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithIdMismatchEventsImages() throws Exception {
        int databaseSizeBeforeUpdate = eventsImagesRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(eventsImagesSearchRepository.findAll());
        eventsImages.setId(count.incrementAndGet());

        // Create the EventsImages
        EventsImagesDTO eventsImagesDTO = eventsImagesMapper.toDto(eventsImages);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEventsImagesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(eventsImagesDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the EventsImages in the database
        List<EventsImages> eventsImagesList = eventsImagesRepository.findAll();
        assertThat(eventsImagesList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(eventsImagesSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamEventsImages() throws Exception {
        int databaseSizeBeforeUpdate = eventsImagesRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(eventsImagesSearchRepository.findAll());
        eventsImages.setId(count.incrementAndGet());

        // Create the EventsImages
        EventsImagesDTO eventsImagesDTO = eventsImagesMapper.toDto(eventsImages);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEventsImagesMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(eventsImagesDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the EventsImages in the database
        List<EventsImages> eventsImagesList = eventsImagesRepository.findAll();
        assertThat(eventsImagesList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(eventsImagesSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void deleteEventsImages() throws Exception {
        // Initialize the database
        eventsImagesRepository.saveAndFlush(eventsImages);
        eventsImagesRepository.save(eventsImages);
        eventsImagesSearchRepository.save(eventsImages);

        int databaseSizeBeforeDelete = eventsImagesRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(eventsImagesSearchRepository.findAll());
        assertThat(searchDatabaseSizeBefore).isEqualTo(databaseSizeBeforeDelete);

        // Delete the eventsImages
        restEventsImagesMockMvc
            .perform(delete(ENTITY_API_URL_ID, eventsImages.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<EventsImages> eventsImagesList = eventsImagesRepository.findAll();
        assertThat(eventsImagesList).hasSize(databaseSizeBeforeDelete - 1);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(eventsImagesSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore - 1);
    }

    @Test
    @Transactional
    void searchEventsImages() throws Exception {
        // Initialize the database
        eventsImages = eventsImagesRepository.saveAndFlush(eventsImages);
        eventsImagesSearchRepository.save(eventsImages);

        // Search the eventsImages
        restEventsImagesMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + eventsImages.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(eventsImages.getId().intValue())))
            .andExpect(jsonPath("$.[*].guid").value(hasItem(DEFAULT_GUID)))
            .andExpect(jsonPath("$.[*].imageLp").value(hasItem(DEFAULT_IMAGE_LP)))
            .andExpect(jsonPath("$.[*].imageThumb").value(hasItem(DEFAULT_IMAGE_THUMB)))
            .andExpect(jsonPath("$.[*].processingTime").value(hasItem(DEFAULT_PROCESSING_TIME.intValue())))
            .andExpect(jsonPath("$.[*].ruleRcvd").value(hasItem(DEFAULT_RULE_RCVD.intValue())))
            .andExpect(jsonPath("$.[*].ruleSent").value(hasItem(DEFAULT_RULE_SENT.intValue())))
            .andExpect(jsonPath("$.[*].when").value(hasItem(DEFAULT_WHEN.intValue())))
            .andExpect(jsonPath("$.[*].gantryProcessed").value(hasItem(DEFAULT_GANTRY_PROCESSED.intValue())))
            .andExpect(jsonPath("$.[*].gantrySent").value(hasItem(DEFAULT_GANTRY_SENT.intValue())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS)))
            .andExpect(jsonPath("$.[*].dataStatus").value(hasItem(DEFAULT_DATA_STATUS)))
            .andExpect(jsonPath("$.[*].threadRandNo").value(hasItem(DEFAULT_THREAD_RAND_NO)))
            .andExpect(jsonPath("$.[*].gantry").value(hasItem(DEFAULT_GANTRY)));
    }
}
