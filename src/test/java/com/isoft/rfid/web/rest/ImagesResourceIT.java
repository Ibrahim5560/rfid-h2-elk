package com.isoft.rfid.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.isoft.rfid.IntegrationTest;
import com.isoft.rfid.domain.Images;
import com.isoft.rfid.repository.ImagesRepository;
import com.isoft.rfid.repository.search.ImagesSearchRepository;
import com.isoft.rfid.service.criteria.ImagesCriteria;
import com.isoft.rfid.service.dto.ImagesDTO;
import com.isoft.rfid.service.mapper.ImagesMapper;
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
import org.springframework.util.Base64Utils;

/**
 * Integration tests for the {@link ImagesResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ImagesResourceIT {

    private static final String DEFAULT_GUID = "AAAAAAAAAA";
    private static final String UPDATED_GUID = "BBBBBBBBBB";

    private static final String DEFAULT_PLATE = "AAAAAAAAAA";
    private static final String UPDATED_PLATE = "BBBBBBBBBB";

    private static final byte[] DEFAULT_IMAGE_LP = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_IMAGE_LP = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_IMAGE_LP_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_IMAGE_LP_CONTENT_TYPE = "image/png";

    private static final byte[] DEFAULT_IMAGE_THUMB = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_IMAGE_THUMB = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_IMAGE_THUMB_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_IMAGE_THUMB_CONTENT_TYPE = "image/png";

    private static final String DEFAULT_ANPR = "AAAAAAAAAA";
    private static final String UPDATED_ANPR = "BBBBBBBBBB";

    private static final String DEFAULT_RFID = "AAAAAAAAAA";
    private static final String UPDATED_RFID = "BBBBBBBBBB";

    private static final String DEFAULT_DATA_STATUS = "AAAAAAAAAA";
    private static final String UPDATED_DATA_STATUS = "BBBBBBBBBB";

    private static final Long DEFAULT_GANTRY = 1L;
    private static final Long UPDATED_GANTRY = 2L;
    private static final Long SMALLER_GANTRY = 1L - 1L;

    private static final Long DEFAULT_LANE = 1L;
    private static final Long UPDATED_LANE = 2L;
    private static final Long SMALLER_LANE = 1L - 1L;

    private static final Long DEFAULT_KPH = 1L;
    private static final Long UPDATED_KPH = 2L;
    private static final Long SMALLER_KPH = 1L - 1L;

    private static final Long DEFAULT_AMBUSH = 1L;
    private static final Long UPDATED_AMBUSH = 2L;
    private static final Long SMALLER_AMBUSH = 1L - 1L;

    private static final Long DEFAULT_DIRECTION = 1L;
    private static final Long UPDATED_DIRECTION = 2L;
    private static final Long SMALLER_DIRECTION = 1L - 1L;

    private static final Long DEFAULT_VEHICLE = 1L;
    private static final Long UPDATED_VEHICLE = 2L;
    private static final Long SMALLER_VEHICLE = 1L - 1L;

    private static final String DEFAULT_ISSUE = "AAAAAAAAAA";
    private static final String UPDATED_ISSUE = "BBBBBBBBBB";

    private static final String DEFAULT_STATUS = "AAAAAAAAAA";
    private static final String UPDATED_STATUS = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/images";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/_search/images";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ImagesRepository imagesRepository;

    @Autowired
    private ImagesMapper imagesMapper;

    @Autowired
    private ImagesSearchRepository imagesSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restImagesMockMvc;

    private Images images;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Images createEntity(EntityManager em) {
        Images images = new Images()
            .guid(DEFAULT_GUID)
            .plate(DEFAULT_PLATE)
            .imageLp(DEFAULT_IMAGE_LP)
            .imageLpContentType(DEFAULT_IMAGE_LP_CONTENT_TYPE)
            .imageThumb(DEFAULT_IMAGE_THUMB)
            .imageThumbContentType(DEFAULT_IMAGE_THUMB_CONTENT_TYPE)
            .anpr(DEFAULT_ANPR)
            .rfid(DEFAULT_RFID)
            .dataStatus(DEFAULT_DATA_STATUS)
            .gantry(DEFAULT_GANTRY)
            .lane(DEFAULT_LANE)
            .kph(DEFAULT_KPH)
            .ambush(DEFAULT_AMBUSH)
            .direction(DEFAULT_DIRECTION)
            .vehicle(DEFAULT_VEHICLE)
            .issue(DEFAULT_ISSUE)
            .status(DEFAULT_STATUS);
        return images;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Images createUpdatedEntity(EntityManager em) {
        Images images = new Images()
            .guid(UPDATED_GUID)
            .plate(UPDATED_PLATE)
            .imageLp(UPDATED_IMAGE_LP)
            .imageLpContentType(UPDATED_IMAGE_LP_CONTENT_TYPE)
            .imageThumb(UPDATED_IMAGE_THUMB)
            .imageThumbContentType(UPDATED_IMAGE_THUMB_CONTENT_TYPE)
            .anpr(UPDATED_ANPR)
            .rfid(UPDATED_RFID)
            .dataStatus(UPDATED_DATA_STATUS)
            .gantry(UPDATED_GANTRY)
            .lane(UPDATED_LANE)
            .kph(UPDATED_KPH)
            .ambush(UPDATED_AMBUSH)
            .direction(UPDATED_DIRECTION)
            .vehicle(UPDATED_VEHICLE)
            .issue(UPDATED_ISSUE)
            .status(UPDATED_STATUS);
        return images;
    }

    @AfterEach
    public void cleanupElasticSearchRepository() {
        imagesSearchRepository.deleteAll();
        assertThat(imagesSearchRepository.count()).isEqualTo(0);
    }

    @BeforeEach
    public void initTest() {
        images = createEntity(em);
    }

    @Test
    @Transactional
    void createImages() throws Exception {
        int databaseSizeBeforeCreate = imagesRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(imagesSearchRepository.findAll());
        // Create the Images
        ImagesDTO imagesDTO = imagesMapper.toDto(images);
        restImagesMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(imagesDTO)))
            .andExpect(status().isCreated());

        // Validate the Images in the database
        List<Images> imagesList = imagesRepository.findAll();
        assertThat(imagesList).hasSize(databaseSizeBeforeCreate + 1);
        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(imagesSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore + 1);
            });
        Images testImages = imagesList.get(imagesList.size() - 1);
        assertThat(testImages.getGuid()).isEqualTo(DEFAULT_GUID);
        assertThat(testImages.getPlate()).isEqualTo(DEFAULT_PLATE);
        assertThat(testImages.getImageLp()).isEqualTo(DEFAULT_IMAGE_LP);
        assertThat(testImages.getImageLpContentType()).isEqualTo(DEFAULT_IMAGE_LP_CONTENT_TYPE);
        assertThat(testImages.getImageThumb()).isEqualTo(DEFAULT_IMAGE_THUMB);
        assertThat(testImages.getImageThumbContentType()).isEqualTo(DEFAULT_IMAGE_THUMB_CONTENT_TYPE);
        assertThat(testImages.getAnpr()).isEqualTo(DEFAULT_ANPR);
        assertThat(testImages.getRfid()).isEqualTo(DEFAULT_RFID);
        assertThat(testImages.getDataStatus()).isEqualTo(DEFAULT_DATA_STATUS);
        assertThat(testImages.getGantry()).isEqualTo(DEFAULT_GANTRY);
        assertThat(testImages.getLane()).isEqualTo(DEFAULT_LANE);
        assertThat(testImages.getKph()).isEqualTo(DEFAULT_KPH);
        assertThat(testImages.getAmbush()).isEqualTo(DEFAULT_AMBUSH);
        assertThat(testImages.getDirection()).isEqualTo(DEFAULT_DIRECTION);
        assertThat(testImages.getVehicle()).isEqualTo(DEFAULT_VEHICLE);
        assertThat(testImages.getIssue()).isEqualTo(DEFAULT_ISSUE);
        assertThat(testImages.getStatus()).isEqualTo(DEFAULT_STATUS);
    }

    @Test
    @Transactional
    void createImagesWithExistingId() throws Exception {
        // Create the Images with an existing ID
        images.setId(1L);
        ImagesDTO imagesDTO = imagesMapper.toDto(images);

        int databaseSizeBeforeCreate = imagesRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(imagesSearchRepository.findAll());

        // An entity with an existing ID cannot be created, so this API call must fail
        restImagesMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(imagesDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Images in the database
        List<Images> imagesList = imagesRepository.findAll();
        assertThat(imagesList).hasSize(databaseSizeBeforeCreate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(imagesSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkGuidIsRequired() throws Exception {
        int databaseSizeBeforeTest = imagesRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(imagesSearchRepository.findAll());
        // set the field null
        images.setGuid(null);

        // Create the Images, which fails.
        ImagesDTO imagesDTO = imagesMapper.toDto(images);

        restImagesMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(imagesDTO)))
            .andExpect(status().isBadRequest());

        List<Images> imagesList = imagesRepository.findAll();
        assertThat(imagesList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(imagesSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkDataStatusIsRequired() throws Exception {
        int databaseSizeBeforeTest = imagesRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(imagesSearchRepository.findAll());
        // set the field null
        images.setDataStatus(null);

        // Create the Images, which fails.
        ImagesDTO imagesDTO = imagesMapper.toDto(images);

        restImagesMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(imagesDTO)))
            .andExpect(status().isBadRequest());

        List<Images> imagesList = imagesRepository.findAll();
        assertThat(imagesList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(imagesSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkGantryIsRequired() throws Exception {
        int databaseSizeBeforeTest = imagesRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(imagesSearchRepository.findAll());
        // set the field null
        images.setGantry(null);

        // Create the Images, which fails.
        ImagesDTO imagesDTO = imagesMapper.toDto(images);

        restImagesMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(imagesDTO)))
            .andExpect(status().isBadRequest());

        List<Images> imagesList = imagesRepository.findAll();
        assertThat(imagesList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(imagesSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkLaneIsRequired() throws Exception {
        int databaseSizeBeforeTest = imagesRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(imagesSearchRepository.findAll());
        // set the field null
        images.setLane(null);

        // Create the Images, which fails.
        ImagesDTO imagesDTO = imagesMapper.toDto(images);

        restImagesMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(imagesDTO)))
            .andExpect(status().isBadRequest());

        List<Images> imagesList = imagesRepository.findAll();
        assertThat(imagesList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(imagesSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkVehicleIsRequired() throws Exception {
        int databaseSizeBeforeTest = imagesRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(imagesSearchRepository.findAll());
        // set the field null
        images.setVehicle(null);

        // Create the Images, which fails.
        ImagesDTO imagesDTO = imagesMapper.toDto(images);

        restImagesMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(imagesDTO)))
            .andExpect(status().isBadRequest());

        List<Images> imagesList = imagesRepository.findAll();
        assertThat(imagesList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(imagesSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void getAllImages() throws Exception {
        // Initialize the database
        imagesRepository.saveAndFlush(images);

        // Get all the imagesList
        restImagesMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(images.getId().intValue())))
            .andExpect(jsonPath("$.[*].guid").value(hasItem(DEFAULT_GUID)))
            .andExpect(jsonPath("$.[*].plate").value(hasItem(DEFAULT_PLATE)))
            .andExpect(jsonPath("$.[*].imageLpContentType").value(hasItem(DEFAULT_IMAGE_LP_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].imageLp").value(hasItem(Base64Utils.encodeToString(DEFAULT_IMAGE_LP))))
            .andExpect(jsonPath("$.[*].imageThumbContentType").value(hasItem(DEFAULT_IMAGE_THUMB_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].imageThumb").value(hasItem(Base64Utils.encodeToString(DEFAULT_IMAGE_THUMB))))
            .andExpect(jsonPath("$.[*].anpr").value(hasItem(DEFAULT_ANPR)))
            .andExpect(jsonPath("$.[*].rfid").value(hasItem(DEFAULT_RFID)))
            .andExpect(jsonPath("$.[*].dataStatus").value(hasItem(DEFAULT_DATA_STATUS)))
            .andExpect(jsonPath("$.[*].gantry").value(hasItem(DEFAULT_GANTRY.intValue())))
            .andExpect(jsonPath("$.[*].lane").value(hasItem(DEFAULT_LANE.intValue())))
            .andExpect(jsonPath("$.[*].kph").value(hasItem(DEFAULT_KPH.intValue())))
            .andExpect(jsonPath("$.[*].ambush").value(hasItem(DEFAULT_AMBUSH.intValue())))
            .andExpect(jsonPath("$.[*].direction").value(hasItem(DEFAULT_DIRECTION.intValue())))
            .andExpect(jsonPath("$.[*].vehicle").value(hasItem(DEFAULT_VEHICLE.intValue())))
            .andExpect(jsonPath("$.[*].issue").value(hasItem(DEFAULT_ISSUE)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS)));
    }

    @Test
    @Transactional
    void getImages() throws Exception {
        // Initialize the database
        imagesRepository.saveAndFlush(images);

        // Get the images
        restImagesMockMvc
            .perform(get(ENTITY_API_URL_ID, images.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(images.getId().intValue()))
            .andExpect(jsonPath("$.guid").value(DEFAULT_GUID))
            .andExpect(jsonPath("$.plate").value(DEFAULT_PLATE))
            .andExpect(jsonPath("$.imageLpContentType").value(DEFAULT_IMAGE_LP_CONTENT_TYPE))
            .andExpect(jsonPath("$.imageLp").value(Base64Utils.encodeToString(DEFAULT_IMAGE_LP)))
            .andExpect(jsonPath("$.imageThumbContentType").value(DEFAULT_IMAGE_THUMB_CONTENT_TYPE))
            .andExpect(jsonPath("$.imageThumb").value(Base64Utils.encodeToString(DEFAULT_IMAGE_THUMB)))
            .andExpect(jsonPath("$.anpr").value(DEFAULT_ANPR))
            .andExpect(jsonPath("$.rfid").value(DEFAULT_RFID))
            .andExpect(jsonPath("$.dataStatus").value(DEFAULT_DATA_STATUS))
            .andExpect(jsonPath("$.gantry").value(DEFAULT_GANTRY.intValue()))
            .andExpect(jsonPath("$.lane").value(DEFAULT_LANE.intValue()))
            .andExpect(jsonPath("$.kph").value(DEFAULT_KPH.intValue()))
            .andExpect(jsonPath("$.ambush").value(DEFAULT_AMBUSH.intValue()))
            .andExpect(jsonPath("$.direction").value(DEFAULT_DIRECTION.intValue()))
            .andExpect(jsonPath("$.vehicle").value(DEFAULT_VEHICLE.intValue()))
            .andExpect(jsonPath("$.issue").value(DEFAULT_ISSUE))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS));
    }

    @Test
    @Transactional
    void getImagesByIdFiltering() throws Exception {
        // Initialize the database
        imagesRepository.saveAndFlush(images);

        Long id = images.getId();

        defaultImagesShouldBeFound("id.equals=" + id);
        defaultImagesShouldNotBeFound("id.notEquals=" + id);

        defaultImagesShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultImagesShouldNotBeFound("id.greaterThan=" + id);

        defaultImagesShouldBeFound("id.lessThanOrEqual=" + id);
        defaultImagesShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllImagesByGuidIsEqualToSomething() throws Exception {
        // Initialize the database
        imagesRepository.saveAndFlush(images);

        // Get all the imagesList where guid equals to DEFAULT_GUID
        defaultImagesShouldBeFound("guid.equals=" + DEFAULT_GUID);

        // Get all the imagesList where guid equals to UPDATED_GUID
        defaultImagesShouldNotBeFound("guid.equals=" + UPDATED_GUID);
    }

    @Test
    @Transactional
    void getAllImagesByGuidIsInShouldWork() throws Exception {
        // Initialize the database
        imagesRepository.saveAndFlush(images);

        // Get all the imagesList where guid in DEFAULT_GUID or UPDATED_GUID
        defaultImagesShouldBeFound("guid.in=" + DEFAULT_GUID + "," + UPDATED_GUID);

        // Get all the imagesList where guid equals to UPDATED_GUID
        defaultImagesShouldNotBeFound("guid.in=" + UPDATED_GUID);
    }

    @Test
    @Transactional
    void getAllImagesByGuidIsNullOrNotNull() throws Exception {
        // Initialize the database
        imagesRepository.saveAndFlush(images);

        // Get all the imagesList where guid is not null
        defaultImagesShouldBeFound("guid.specified=true");

        // Get all the imagesList where guid is null
        defaultImagesShouldNotBeFound("guid.specified=false");
    }

    @Test
    @Transactional
    void getAllImagesByGuidContainsSomething() throws Exception {
        // Initialize the database
        imagesRepository.saveAndFlush(images);

        // Get all the imagesList where guid contains DEFAULT_GUID
        defaultImagesShouldBeFound("guid.contains=" + DEFAULT_GUID);

        // Get all the imagesList where guid contains UPDATED_GUID
        defaultImagesShouldNotBeFound("guid.contains=" + UPDATED_GUID);
    }

    @Test
    @Transactional
    void getAllImagesByGuidNotContainsSomething() throws Exception {
        // Initialize the database
        imagesRepository.saveAndFlush(images);

        // Get all the imagesList where guid does not contain DEFAULT_GUID
        defaultImagesShouldNotBeFound("guid.doesNotContain=" + DEFAULT_GUID);

        // Get all the imagesList where guid does not contain UPDATED_GUID
        defaultImagesShouldBeFound("guid.doesNotContain=" + UPDATED_GUID);
    }

    @Test
    @Transactional
    void getAllImagesByPlateIsEqualToSomething() throws Exception {
        // Initialize the database
        imagesRepository.saveAndFlush(images);

        // Get all the imagesList where plate equals to DEFAULT_PLATE
        defaultImagesShouldBeFound("plate.equals=" + DEFAULT_PLATE);

        // Get all the imagesList where plate equals to UPDATED_PLATE
        defaultImagesShouldNotBeFound("plate.equals=" + UPDATED_PLATE);
    }

    @Test
    @Transactional
    void getAllImagesByPlateIsInShouldWork() throws Exception {
        // Initialize the database
        imagesRepository.saveAndFlush(images);

        // Get all the imagesList where plate in DEFAULT_PLATE or UPDATED_PLATE
        defaultImagesShouldBeFound("plate.in=" + DEFAULT_PLATE + "," + UPDATED_PLATE);

        // Get all the imagesList where plate equals to UPDATED_PLATE
        defaultImagesShouldNotBeFound("plate.in=" + UPDATED_PLATE);
    }

    @Test
    @Transactional
    void getAllImagesByPlateIsNullOrNotNull() throws Exception {
        // Initialize the database
        imagesRepository.saveAndFlush(images);

        // Get all the imagesList where plate is not null
        defaultImagesShouldBeFound("plate.specified=true");

        // Get all the imagesList where plate is null
        defaultImagesShouldNotBeFound("plate.specified=false");
    }

    @Test
    @Transactional
    void getAllImagesByPlateContainsSomething() throws Exception {
        // Initialize the database
        imagesRepository.saveAndFlush(images);

        // Get all the imagesList where plate contains DEFAULT_PLATE
        defaultImagesShouldBeFound("plate.contains=" + DEFAULT_PLATE);

        // Get all the imagesList where plate contains UPDATED_PLATE
        defaultImagesShouldNotBeFound("plate.contains=" + UPDATED_PLATE);
    }

    @Test
    @Transactional
    void getAllImagesByPlateNotContainsSomething() throws Exception {
        // Initialize the database
        imagesRepository.saveAndFlush(images);

        // Get all the imagesList where plate does not contain DEFAULT_PLATE
        defaultImagesShouldNotBeFound("plate.doesNotContain=" + DEFAULT_PLATE);

        // Get all the imagesList where plate does not contain UPDATED_PLATE
        defaultImagesShouldBeFound("plate.doesNotContain=" + UPDATED_PLATE);
    }

    @Test
    @Transactional
    void getAllImagesByAnprIsEqualToSomething() throws Exception {
        // Initialize the database
        imagesRepository.saveAndFlush(images);

        // Get all the imagesList where anpr equals to DEFAULT_ANPR
        defaultImagesShouldBeFound("anpr.equals=" + DEFAULT_ANPR);

        // Get all the imagesList where anpr equals to UPDATED_ANPR
        defaultImagesShouldNotBeFound("anpr.equals=" + UPDATED_ANPR);
    }

    @Test
    @Transactional
    void getAllImagesByAnprIsInShouldWork() throws Exception {
        // Initialize the database
        imagesRepository.saveAndFlush(images);

        // Get all the imagesList where anpr in DEFAULT_ANPR or UPDATED_ANPR
        defaultImagesShouldBeFound("anpr.in=" + DEFAULT_ANPR + "," + UPDATED_ANPR);

        // Get all the imagesList where anpr equals to UPDATED_ANPR
        defaultImagesShouldNotBeFound("anpr.in=" + UPDATED_ANPR);
    }

    @Test
    @Transactional
    void getAllImagesByAnprIsNullOrNotNull() throws Exception {
        // Initialize the database
        imagesRepository.saveAndFlush(images);

        // Get all the imagesList where anpr is not null
        defaultImagesShouldBeFound("anpr.specified=true");

        // Get all the imagesList where anpr is null
        defaultImagesShouldNotBeFound("anpr.specified=false");
    }

    @Test
    @Transactional
    void getAllImagesByAnprContainsSomething() throws Exception {
        // Initialize the database
        imagesRepository.saveAndFlush(images);

        // Get all the imagesList where anpr contains DEFAULT_ANPR
        defaultImagesShouldBeFound("anpr.contains=" + DEFAULT_ANPR);

        // Get all the imagesList where anpr contains UPDATED_ANPR
        defaultImagesShouldNotBeFound("anpr.contains=" + UPDATED_ANPR);
    }

    @Test
    @Transactional
    void getAllImagesByAnprNotContainsSomething() throws Exception {
        // Initialize the database
        imagesRepository.saveAndFlush(images);

        // Get all the imagesList where anpr does not contain DEFAULT_ANPR
        defaultImagesShouldNotBeFound("anpr.doesNotContain=" + DEFAULT_ANPR);

        // Get all the imagesList where anpr does not contain UPDATED_ANPR
        defaultImagesShouldBeFound("anpr.doesNotContain=" + UPDATED_ANPR);
    }

    @Test
    @Transactional
    void getAllImagesByRfidIsEqualToSomething() throws Exception {
        // Initialize the database
        imagesRepository.saveAndFlush(images);

        // Get all the imagesList where rfid equals to DEFAULT_RFID
        defaultImagesShouldBeFound("rfid.equals=" + DEFAULT_RFID);

        // Get all the imagesList where rfid equals to UPDATED_RFID
        defaultImagesShouldNotBeFound("rfid.equals=" + UPDATED_RFID);
    }

    @Test
    @Transactional
    void getAllImagesByRfidIsInShouldWork() throws Exception {
        // Initialize the database
        imagesRepository.saveAndFlush(images);

        // Get all the imagesList where rfid in DEFAULT_RFID or UPDATED_RFID
        defaultImagesShouldBeFound("rfid.in=" + DEFAULT_RFID + "," + UPDATED_RFID);

        // Get all the imagesList where rfid equals to UPDATED_RFID
        defaultImagesShouldNotBeFound("rfid.in=" + UPDATED_RFID);
    }

    @Test
    @Transactional
    void getAllImagesByRfidIsNullOrNotNull() throws Exception {
        // Initialize the database
        imagesRepository.saveAndFlush(images);

        // Get all the imagesList where rfid is not null
        defaultImagesShouldBeFound("rfid.specified=true");

        // Get all the imagesList where rfid is null
        defaultImagesShouldNotBeFound("rfid.specified=false");
    }

    @Test
    @Transactional
    void getAllImagesByRfidContainsSomething() throws Exception {
        // Initialize the database
        imagesRepository.saveAndFlush(images);

        // Get all the imagesList where rfid contains DEFAULT_RFID
        defaultImagesShouldBeFound("rfid.contains=" + DEFAULT_RFID);

        // Get all the imagesList where rfid contains UPDATED_RFID
        defaultImagesShouldNotBeFound("rfid.contains=" + UPDATED_RFID);
    }

    @Test
    @Transactional
    void getAllImagesByRfidNotContainsSomething() throws Exception {
        // Initialize the database
        imagesRepository.saveAndFlush(images);

        // Get all the imagesList where rfid does not contain DEFAULT_RFID
        defaultImagesShouldNotBeFound("rfid.doesNotContain=" + DEFAULT_RFID);

        // Get all the imagesList where rfid does not contain UPDATED_RFID
        defaultImagesShouldBeFound("rfid.doesNotContain=" + UPDATED_RFID);
    }

    @Test
    @Transactional
    void getAllImagesByDataStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        imagesRepository.saveAndFlush(images);

        // Get all the imagesList where dataStatus equals to DEFAULT_DATA_STATUS
        defaultImagesShouldBeFound("dataStatus.equals=" + DEFAULT_DATA_STATUS);

        // Get all the imagesList where dataStatus equals to UPDATED_DATA_STATUS
        defaultImagesShouldNotBeFound("dataStatus.equals=" + UPDATED_DATA_STATUS);
    }

    @Test
    @Transactional
    void getAllImagesByDataStatusIsInShouldWork() throws Exception {
        // Initialize the database
        imagesRepository.saveAndFlush(images);

        // Get all the imagesList where dataStatus in DEFAULT_DATA_STATUS or UPDATED_DATA_STATUS
        defaultImagesShouldBeFound("dataStatus.in=" + DEFAULT_DATA_STATUS + "," + UPDATED_DATA_STATUS);

        // Get all the imagesList where dataStatus equals to UPDATED_DATA_STATUS
        defaultImagesShouldNotBeFound("dataStatus.in=" + UPDATED_DATA_STATUS);
    }

    @Test
    @Transactional
    void getAllImagesByDataStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        imagesRepository.saveAndFlush(images);

        // Get all the imagesList where dataStatus is not null
        defaultImagesShouldBeFound("dataStatus.specified=true");

        // Get all the imagesList where dataStatus is null
        defaultImagesShouldNotBeFound("dataStatus.specified=false");
    }

    @Test
    @Transactional
    void getAllImagesByDataStatusContainsSomething() throws Exception {
        // Initialize the database
        imagesRepository.saveAndFlush(images);

        // Get all the imagesList where dataStatus contains DEFAULT_DATA_STATUS
        defaultImagesShouldBeFound("dataStatus.contains=" + DEFAULT_DATA_STATUS);

        // Get all the imagesList where dataStatus contains UPDATED_DATA_STATUS
        defaultImagesShouldNotBeFound("dataStatus.contains=" + UPDATED_DATA_STATUS);
    }

    @Test
    @Transactional
    void getAllImagesByDataStatusNotContainsSomething() throws Exception {
        // Initialize the database
        imagesRepository.saveAndFlush(images);

        // Get all the imagesList where dataStatus does not contain DEFAULT_DATA_STATUS
        defaultImagesShouldNotBeFound("dataStatus.doesNotContain=" + DEFAULT_DATA_STATUS);

        // Get all the imagesList where dataStatus does not contain UPDATED_DATA_STATUS
        defaultImagesShouldBeFound("dataStatus.doesNotContain=" + UPDATED_DATA_STATUS);
    }

    @Test
    @Transactional
    void getAllImagesByGantryIsEqualToSomething() throws Exception {
        // Initialize the database
        imagesRepository.saveAndFlush(images);

        // Get all the imagesList where gantry equals to DEFAULT_GANTRY
        defaultImagesShouldBeFound("gantry.equals=" + DEFAULT_GANTRY);

        // Get all the imagesList where gantry equals to UPDATED_GANTRY
        defaultImagesShouldNotBeFound("gantry.equals=" + UPDATED_GANTRY);
    }

    @Test
    @Transactional
    void getAllImagesByGantryIsInShouldWork() throws Exception {
        // Initialize the database
        imagesRepository.saveAndFlush(images);

        // Get all the imagesList where gantry in DEFAULT_GANTRY or UPDATED_GANTRY
        defaultImagesShouldBeFound("gantry.in=" + DEFAULT_GANTRY + "," + UPDATED_GANTRY);

        // Get all the imagesList where gantry equals to UPDATED_GANTRY
        defaultImagesShouldNotBeFound("gantry.in=" + UPDATED_GANTRY);
    }

    @Test
    @Transactional
    void getAllImagesByGantryIsNullOrNotNull() throws Exception {
        // Initialize the database
        imagesRepository.saveAndFlush(images);

        // Get all the imagesList where gantry is not null
        defaultImagesShouldBeFound("gantry.specified=true");

        // Get all the imagesList where gantry is null
        defaultImagesShouldNotBeFound("gantry.specified=false");
    }

    @Test
    @Transactional
    void getAllImagesByGantryIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        imagesRepository.saveAndFlush(images);

        // Get all the imagesList where gantry is greater than or equal to DEFAULT_GANTRY
        defaultImagesShouldBeFound("gantry.greaterThanOrEqual=" + DEFAULT_GANTRY);

        // Get all the imagesList where gantry is greater than or equal to UPDATED_GANTRY
        defaultImagesShouldNotBeFound("gantry.greaterThanOrEqual=" + UPDATED_GANTRY);
    }

    @Test
    @Transactional
    void getAllImagesByGantryIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        imagesRepository.saveAndFlush(images);

        // Get all the imagesList where gantry is less than or equal to DEFAULT_GANTRY
        defaultImagesShouldBeFound("gantry.lessThanOrEqual=" + DEFAULT_GANTRY);

        // Get all the imagesList where gantry is less than or equal to SMALLER_GANTRY
        defaultImagesShouldNotBeFound("gantry.lessThanOrEqual=" + SMALLER_GANTRY);
    }

    @Test
    @Transactional
    void getAllImagesByGantryIsLessThanSomething() throws Exception {
        // Initialize the database
        imagesRepository.saveAndFlush(images);

        // Get all the imagesList where gantry is less than DEFAULT_GANTRY
        defaultImagesShouldNotBeFound("gantry.lessThan=" + DEFAULT_GANTRY);

        // Get all the imagesList where gantry is less than UPDATED_GANTRY
        defaultImagesShouldBeFound("gantry.lessThan=" + UPDATED_GANTRY);
    }

    @Test
    @Transactional
    void getAllImagesByGantryIsGreaterThanSomething() throws Exception {
        // Initialize the database
        imagesRepository.saveAndFlush(images);

        // Get all the imagesList where gantry is greater than DEFAULT_GANTRY
        defaultImagesShouldNotBeFound("gantry.greaterThan=" + DEFAULT_GANTRY);

        // Get all the imagesList where gantry is greater than SMALLER_GANTRY
        defaultImagesShouldBeFound("gantry.greaterThan=" + SMALLER_GANTRY);
    }

    @Test
    @Transactional
    void getAllImagesByLaneIsEqualToSomething() throws Exception {
        // Initialize the database
        imagesRepository.saveAndFlush(images);

        // Get all the imagesList where lane equals to DEFAULT_LANE
        defaultImagesShouldBeFound("lane.equals=" + DEFAULT_LANE);

        // Get all the imagesList where lane equals to UPDATED_LANE
        defaultImagesShouldNotBeFound("lane.equals=" + UPDATED_LANE);
    }

    @Test
    @Transactional
    void getAllImagesByLaneIsInShouldWork() throws Exception {
        // Initialize the database
        imagesRepository.saveAndFlush(images);

        // Get all the imagesList where lane in DEFAULT_LANE or UPDATED_LANE
        defaultImagesShouldBeFound("lane.in=" + DEFAULT_LANE + "," + UPDATED_LANE);

        // Get all the imagesList where lane equals to UPDATED_LANE
        defaultImagesShouldNotBeFound("lane.in=" + UPDATED_LANE);
    }

    @Test
    @Transactional
    void getAllImagesByLaneIsNullOrNotNull() throws Exception {
        // Initialize the database
        imagesRepository.saveAndFlush(images);

        // Get all the imagesList where lane is not null
        defaultImagesShouldBeFound("lane.specified=true");

        // Get all the imagesList where lane is null
        defaultImagesShouldNotBeFound("lane.specified=false");
    }

    @Test
    @Transactional
    void getAllImagesByLaneIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        imagesRepository.saveAndFlush(images);

        // Get all the imagesList where lane is greater than or equal to DEFAULT_LANE
        defaultImagesShouldBeFound("lane.greaterThanOrEqual=" + DEFAULT_LANE);

        // Get all the imagesList where lane is greater than or equal to UPDATED_LANE
        defaultImagesShouldNotBeFound("lane.greaterThanOrEqual=" + UPDATED_LANE);
    }

    @Test
    @Transactional
    void getAllImagesByLaneIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        imagesRepository.saveAndFlush(images);

        // Get all the imagesList where lane is less than or equal to DEFAULT_LANE
        defaultImagesShouldBeFound("lane.lessThanOrEqual=" + DEFAULT_LANE);

        // Get all the imagesList where lane is less than or equal to SMALLER_LANE
        defaultImagesShouldNotBeFound("lane.lessThanOrEqual=" + SMALLER_LANE);
    }

    @Test
    @Transactional
    void getAllImagesByLaneIsLessThanSomething() throws Exception {
        // Initialize the database
        imagesRepository.saveAndFlush(images);

        // Get all the imagesList where lane is less than DEFAULT_LANE
        defaultImagesShouldNotBeFound("lane.lessThan=" + DEFAULT_LANE);

        // Get all the imagesList where lane is less than UPDATED_LANE
        defaultImagesShouldBeFound("lane.lessThan=" + UPDATED_LANE);
    }

    @Test
    @Transactional
    void getAllImagesByLaneIsGreaterThanSomething() throws Exception {
        // Initialize the database
        imagesRepository.saveAndFlush(images);

        // Get all the imagesList where lane is greater than DEFAULT_LANE
        defaultImagesShouldNotBeFound("lane.greaterThan=" + DEFAULT_LANE);

        // Get all the imagesList where lane is greater than SMALLER_LANE
        defaultImagesShouldBeFound("lane.greaterThan=" + SMALLER_LANE);
    }

    @Test
    @Transactional
    void getAllImagesByKphIsEqualToSomething() throws Exception {
        // Initialize the database
        imagesRepository.saveAndFlush(images);

        // Get all the imagesList where kph equals to DEFAULT_KPH
        defaultImagesShouldBeFound("kph.equals=" + DEFAULT_KPH);

        // Get all the imagesList where kph equals to UPDATED_KPH
        defaultImagesShouldNotBeFound("kph.equals=" + UPDATED_KPH);
    }

    @Test
    @Transactional
    void getAllImagesByKphIsInShouldWork() throws Exception {
        // Initialize the database
        imagesRepository.saveAndFlush(images);

        // Get all the imagesList where kph in DEFAULT_KPH or UPDATED_KPH
        defaultImagesShouldBeFound("kph.in=" + DEFAULT_KPH + "," + UPDATED_KPH);

        // Get all the imagesList where kph equals to UPDATED_KPH
        defaultImagesShouldNotBeFound("kph.in=" + UPDATED_KPH);
    }

    @Test
    @Transactional
    void getAllImagesByKphIsNullOrNotNull() throws Exception {
        // Initialize the database
        imagesRepository.saveAndFlush(images);

        // Get all the imagesList where kph is not null
        defaultImagesShouldBeFound("kph.specified=true");

        // Get all the imagesList where kph is null
        defaultImagesShouldNotBeFound("kph.specified=false");
    }

    @Test
    @Transactional
    void getAllImagesByKphIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        imagesRepository.saveAndFlush(images);

        // Get all the imagesList where kph is greater than or equal to DEFAULT_KPH
        defaultImagesShouldBeFound("kph.greaterThanOrEqual=" + DEFAULT_KPH);

        // Get all the imagesList where kph is greater than or equal to UPDATED_KPH
        defaultImagesShouldNotBeFound("kph.greaterThanOrEqual=" + UPDATED_KPH);
    }

    @Test
    @Transactional
    void getAllImagesByKphIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        imagesRepository.saveAndFlush(images);

        // Get all the imagesList where kph is less than or equal to DEFAULT_KPH
        defaultImagesShouldBeFound("kph.lessThanOrEqual=" + DEFAULT_KPH);

        // Get all the imagesList where kph is less than or equal to SMALLER_KPH
        defaultImagesShouldNotBeFound("kph.lessThanOrEqual=" + SMALLER_KPH);
    }

    @Test
    @Transactional
    void getAllImagesByKphIsLessThanSomething() throws Exception {
        // Initialize the database
        imagesRepository.saveAndFlush(images);

        // Get all the imagesList where kph is less than DEFAULT_KPH
        defaultImagesShouldNotBeFound("kph.lessThan=" + DEFAULT_KPH);

        // Get all the imagesList where kph is less than UPDATED_KPH
        defaultImagesShouldBeFound("kph.lessThan=" + UPDATED_KPH);
    }

    @Test
    @Transactional
    void getAllImagesByKphIsGreaterThanSomething() throws Exception {
        // Initialize the database
        imagesRepository.saveAndFlush(images);

        // Get all the imagesList where kph is greater than DEFAULT_KPH
        defaultImagesShouldNotBeFound("kph.greaterThan=" + DEFAULT_KPH);

        // Get all the imagesList where kph is greater than SMALLER_KPH
        defaultImagesShouldBeFound("kph.greaterThan=" + SMALLER_KPH);
    }

    @Test
    @Transactional
    void getAllImagesByAmbushIsEqualToSomething() throws Exception {
        // Initialize the database
        imagesRepository.saveAndFlush(images);

        // Get all the imagesList where ambush equals to DEFAULT_AMBUSH
        defaultImagesShouldBeFound("ambush.equals=" + DEFAULT_AMBUSH);

        // Get all the imagesList where ambush equals to UPDATED_AMBUSH
        defaultImagesShouldNotBeFound("ambush.equals=" + UPDATED_AMBUSH);
    }

    @Test
    @Transactional
    void getAllImagesByAmbushIsInShouldWork() throws Exception {
        // Initialize the database
        imagesRepository.saveAndFlush(images);

        // Get all the imagesList where ambush in DEFAULT_AMBUSH or UPDATED_AMBUSH
        defaultImagesShouldBeFound("ambush.in=" + DEFAULT_AMBUSH + "," + UPDATED_AMBUSH);

        // Get all the imagesList where ambush equals to UPDATED_AMBUSH
        defaultImagesShouldNotBeFound("ambush.in=" + UPDATED_AMBUSH);
    }

    @Test
    @Transactional
    void getAllImagesByAmbushIsNullOrNotNull() throws Exception {
        // Initialize the database
        imagesRepository.saveAndFlush(images);

        // Get all the imagesList where ambush is not null
        defaultImagesShouldBeFound("ambush.specified=true");

        // Get all the imagesList where ambush is null
        defaultImagesShouldNotBeFound("ambush.specified=false");
    }

    @Test
    @Transactional
    void getAllImagesByAmbushIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        imagesRepository.saveAndFlush(images);

        // Get all the imagesList where ambush is greater than or equal to DEFAULT_AMBUSH
        defaultImagesShouldBeFound("ambush.greaterThanOrEqual=" + DEFAULT_AMBUSH);

        // Get all the imagesList where ambush is greater than or equal to UPDATED_AMBUSH
        defaultImagesShouldNotBeFound("ambush.greaterThanOrEqual=" + UPDATED_AMBUSH);
    }

    @Test
    @Transactional
    void getAllImagesByAmbushIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        imagesRepository.saveAndFlush(images);

        // Get all the imagesList where ambush is less than or equal to DEFAULT_AMBUSH
        defaultImagesShouldBeFound("ambush.lessThanOrEqual=" + DEFAULT_AMBUSH);

        // Get all the imagesList where ambush is less than or equal to SMALLER_AMBUSH
        defaultImagesShouldNotBeFound("ambush.lessThanOrEqual=" + SMALLER_AMBUSH);
    }

    @Test
    @Transactional
    void getAllImagesByAmbushIsLessThanSomething() throws Exception {
        // Initialize the database
        imagesRepository.saveAndFlush(images);

        // Get all the imagesList where ambush is less than DEFAULT_AMBUSH
        defaultImagesShouldNotBeFound("ambush.lessThan=" + DEFAULT_AMBUSH);

        // Get all the imagesList where ambush is less than UPDATED_AMBUSH
        defaultImagesShouldBeFound("ambush.lessThan=" + UPDATED_AMBUSH);
    }

    @Test
    @Transactional
    void getAllImagesByAmbushIsGreaterThanSomething() throws Exception {
        // Initialize the database
        imagesRepository.saveAndFlush(images);

        // Get all the imagesList where ambush is greater than DEFAULT_AMBUSH
        defaultImagesShouldNotBeFound("ambush.greaterThan=" + DEFAULT_AMBUSH);

        // Get all the imagesList where ambush is greater than SMALLER_AMBUSH
        defaultImagesShouldBeFound("ambush.greaterThan=" + SMALLER_AMBUSH);
    }

    @Test
    @Transactional
    void getAllImagesByDirectionIsEqualToSomething() throws Exception {
        // Initialize the database
        imagesRepository.saveAndFlush(images);

        // Get all the imagesList where direction equals to DEFAULT_DIRECTION
        defaultImagesShouldBeFound("direction.equals=" + DEFAULT_DIRECTION);

        // Get all the imagesList where direction equals to UPDATED_DIRECTION
        defaultImagesShouldNotBeFound("direction.equals=" + UPDATED_DIRECTION);
    }

    @Test
    @Transactional
    void getAllImagesByDirectionIsInShouldWork() throws Exception {
        // Initialize the database
        imagesRepository.saveAndFlush(images);

        // Get all the imagesList where direction in DEFAULT_DIRECTION or UPDATED_DIRECTION
        defaultImagesShouldBeFound("direction.in=" + DEFAULT_DIRECTION + "," + UPDATED_DIRECTION);

        // Get all the imagesList where direction equals to UPDATED_DIRECTION
        defaultImagesShouldNotBeFound("direction.in=" + UPDATED_DIRECTION);
    }

    @Test
    @Transactional
    void getAllImagesByDirectionIsNullOrNotNull() throws Exception {
        // Initialize the database
        imagesRepository.saveAndFlush(images);

        // Get all the imagesList where direction is not null
        defaultImagesShouldBeFound("direction.specified=true");

        // Get all the imagesList where direction is null
        defaultImagesShouldNotBeFound("direction.specified=false");
    }

    @Test
    @Transactional
    void getAllImagesByDirectionIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        imagesRepository.saveAndFlush(images);

        // Get all the imagesList where direction is greater than or equal to DEFAULT_DIRECTION
        defaultImagesShouldBeFound("direction.greaterThanOrEqual=" + DEFAULT_DIRECTION);

        // Get all the imagesList where direction is greater than or equal to UPDATED_DIRECTION
        defaultImagesShouldNotBeFound("direction.greaterThanOrEqual=" + UPDATED_DIRECTION);
    }

    @Test
    @Transactional
    void getAllImagesByDirectionIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        imagesRepository.saveAndFlush(images);

        // Get all the imagesList where direction is less than or equal to DEFAULT_DIRECTION
        defaultImagesShouldBeFound("direction.lessThanOrEqual=" + DEFAULT_DIRECTION);

        // Get all the imagesList where direction is less than or equal to SMALLER_DIRECTION
        defaultImagesShouldNotBeFound("direction.lessThanOrEqual=" + SMALLER_DIRECTION);
    }

    @Test
    @Transactional
    void getAllImagesByDirectionIsLessThanSomething() throws Exception {
        // Initialize the database
        imagesRepository.saveAndFlush(images);

        // Get all the imagesList where direction is less than DEFAULT_DIRECTION
        defaultImagesShouldNotBeFound("direction.lessThan=" + DEFAULT_DIRECTION);

        // Get all the imagesList where direction is less than UPDATED_DIRECTION
        defaultImagesShouldBeFound("direction.lessThan=" + UPDATED_DIRECTION);
    }

    @Test
    @Transactional
    void getAllImagesByDirectionIsGreaterThanSomething() throws Exception {
        // Initialize the database
        imagesRepository.saveAndFlush(images);

        // Get all the imagesList where direction is greater than DEFAULT_DIRECTION
        defaultImagesShouldNotBeFound("direction.greaterThan=" + DEFAULT_DIRECTION);

        // Get all the imagesList where direction is greater than SMALLER_DIRECTION
        defaultImagesShouldBeFound("direction.greaterThan=" + SMALLER_DIRECTION);
    }

    @Test
    @Transactional
    void getAllImagesByVehicleIsEqualToSomething() throws Exception {
        // Initialize the database
        imagesRepository.saveAndFlush(images);

        // Get all the imagesList where vehicle equals to DEFAULT_VEHICLE
        defaultImagesShouldBeFound("vehicle.equals=" + DEFAULT_VEHICLE);

        // Get all the imagesList where vehicle equals to UPDATED_VEHICLE
        defaultImagesShouldNotBeFound("vehicle.equals=" + UPDATED_VEHICLE);
    }

    @Test
    @Transactional
    void getAllImagesByVehicleIsInShouldWork() throws Exception {
        // Initialize the database
        imagesRepository.saveAndFlush(images);

        // Get all the imagesList where vehicle in DEFAULT_VEHICLE or UPDATED_VEHICLE
        defaultImagesShouldBeFound("vehicle.in=" + DEFAULT_VEHICLE + "," + UPDATED_VEHICLE);

        // Get all the imagesList where vehicle equals to UPDATED_VEHICLE
        defaultImagesShouldNotBeFound("vehicle.in=" + UPDATED_VEHICLE);
    }

    @Test
    @Transactional
    void getAllImagesByVehicleIsNullOrNotNull() throws Exception {
        // Initialize the database
        imagesRepository.saveAndFlush(images);

        // Get all the imagesList where vehicle is not null
        defaultImagesShouldBeFound("vehicle.specified=true");

        // Get all the imagesList where vehicle is null
        defaultImagesShouldNotBeFound("vehicle.specified=false");
    }

    @Test
    @Transactional
    void getAllImagesByVehicleIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        imagesRepository.saveAndFlush(images);

        // Get all the imagesList where vehicle is greater than or equal to DEFAULT_VEHICLE
        defaultImagesShouldBeFound("vehicle.greaterThanOrEqual=" + DEFAULT_VEHICLE);

        // Get all the imagesList where vehicle is greater than or equal to UPDATED_VEHICLE
        defaultImagesShouldNotBeFound("vehicle.greaterThanOrEqual=" + UPDATED_VEHICLE);
    }

    @Test
    @Transactional
    void getAllImagesByVehicleIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        imagesRepository.saveAndFlush(images);

        // Get all the imagesList where vehicle is less than or equal to DEFAULT_VEHICLE
        defaultImagesShouldBeFound("vehicle.lessThanOrEqual=" + DEFAULT_VEHICLE);

        // Get all the imagesList where vehicle is less than or equal to SMALLER_VEHICLE
        defaultImagesShouldNotBeFound("vehicle.lessThanOrEqual=" + SMALLER_VEHICLE);
    }

    @Test
    @Transactional
    void getAllImagesByVehicleIsLessThanSomething() throws Exception {
        // Initialize the database
        imagesRepository.saveAndFlush(images);

        // Get all the imagesList where vehicle is less than DEFAULT_VEHICLE
        defaultImagesShouldNotBeFound("vehicle.lessThan=" + DEFAULT_VEHICLE);

        // Get all the imagesList where vehicle is less than UPDATED_VEHICLE
        defaultImagesShouldBeFound("vehicle.lessThan=" + UPDATED_VEHICLE);
    }

    @Test
    @Transactional
    void getAllImagesByVehicleIsGreaterThanSomething() throws Exception {
        // Initialize the database
        imagesRepository.saveAndFlush(images);

        // Get all the imagesList where vehicle is greater than DEFAULT_VEHICLE
        defaultImagesShouldNotBeFound("vehicle.greaterThan=" + DEFAULT_VEHICLE);

        // Get all the imagesList where vehicle is greater than SMALLER_VEHICLE
        defaultImagesShouldBeFound("vehicle.greaterThan=" + SMALLER_VEHICLE);
    }

    @Test
    @Transactional
    void getAllImagesByIssueIsEqualToSomething() throws Exception {
        // Initialize the database
        imagesRepository.saveAndFlush(images);

        // Get all the imagesList where issue equals to DEFAULT_ISSUE
        defaultImagesShouldBeFound("issue.equals=" + DEFAULT_ISSUE);

        // Get all the imagesList where issue equals to UPDATED_ISSUE
        defaultImagesShouldNotBeFound("issue.equals=" + UPDATED_ISSUE);
    }

    @Test
    @Transactional
    void getAllImagesByIssueIsInShouldWork() throws Exception {
        // Initialize the database
        imagesRepository.saveAndFlush(images);

        // Get all the imagesList where issue in DEFAULT_ISSUE or UPDATED_ISSUE
        defaultImagesShouldBeFound("issue.in=" + DEFAULT_ISSUE + "," + UPDATED_ISSUE);

        // Get all the imagesList where issue equals to UPDATED_ISSUE
        defaultImagesShouldNotBeFound("issue.in=" + UPDATED_ISSUE);
    }

    @Test
    @Transactional
    void getAllImagesByIssueIsNullOrNotNull() throws Exception {
        // Initialize the database
        imagesRepository.saveAndFlush(images);

        // Get all the imagesList where issue is not null
        defaultImagesShouldBeFound("issue.specified=true");

        // Get all the imagesList where issue is null
        defaultImagesShouldNotBeFound("issue.specified=false");
    }

    @Test
    @Transactional
    void getAllImagesByIssueContainsSomething() throws Exception {
        // Initialize the database
        imagesRepository.saveAndFlush(images);

        // Get all the imagesList where issue contains DEFAULT_ISSUE
        defaultImagesShouldBeFound("issue.contains=" + DEFAULT_ISSUE);

        // Get all the imagesList where issue contains UPDATED_ISSUE
        defaultImagesShouldNotBeFound("issue.contains=" + UPDATED_ISSUE);
    }

    @Test
    @Transactional
    void getAllImagesByIssueNotContainsSomething() throws Exception {
        // Initialize the database
        imagesRepository.saveAndFlush(images);

        // Get all the imagesList where issue does not contain DEFAULT_ISSUE
        defaultImagesShouldNotBeFound("issue.doesNotContain=" + DEFAULT_ISSUE);

        // Get all the imagesList where issue does not contain UPDATED_ISSUE
        defaultImagesShouldBeFound("issue.doesNotContain=" + UPDATED_ISSUE);
    }

    @Test
    @Transactional
    void getAllImagesByStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        imagesRepository.saveAndFlush(images);

        // Get all the imagesList where status equals to DEFAULT_STATUS
        defaultImagesShouldBeFound("status.equals=" + DEFAULT_STATUS);

        // Get all the imagesList where status equals to UPDATED_STATUS
        defaultImagesShouldNotBeFound("status.equals=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllImagesByStatusIsInShouldWork() throws Exception {
        // Initialize the database
        imagesRepository.saveAndFlush(images);

        // Get all the imagesList where status in DEFAULT_STATUS or UPDATED_STATUS
        defaultImagesShouldBeFound("status.in=" + DEFAULT_STATUS + "," + UPDATED_STATUS);

        // Get all the imagesList where status equals to UPDATED_STATUS
        defaultImagesShouldNotBeFound("status.in=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllImagesByStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        imagesRepository.saveAndFlush(images);

        // Get all the imagesList where status is not null
        defaultImagesShouldBeFound("status.specified=true");

        // Get all the imagesList where status is null
        defaultImagesShouldNotBeFound("status.specified=false");
    }

    @Test
    @Transactional
    void getAllImagesByStatusContainsSomething() throws Exception {
        // Initialize the database
        imagesRepository.saveAndFlush(images);

        // Get all the imagesList where status contains DEFAULT_STATUS
        defaultImagesShouldBeFound("status.contains=" + DEFAULT_STATUS);

        // Get all the imagesList where status contains UPDATED_STATUS
        defaultImagesShouldNotBeFound("status.contains=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllImagesByStatusNotContainsSomething() throws Exception {
        // Initialize the database
        imagesRepository.saveAndFlush(images);

        // Get all the imagesList where status does not contain DEFAULT_STATUS
        defaultImagesShouldNotBeFound("status.doesNotContain=" + DEFAULT_STATUS);

        // Get all the imagesList where status does not contain UPDATED_STATUS
        defaultImagesShouldBeFound("status.doesNotContain=" + UPDATED_STATUS);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultImagesShouldBeFound(String filter) throws Exception {
        restImagesMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(images.getId().intValue())))
            .andExpect(jsonPath("$.[*].guid").value(hasItem(DEFAULT_GUID)))
            .andExpect(jsonPath("$.[*].plate").value(hasItem(DEFAULT_PLATE)))
            .andExpect(jsonPath("$.[*].imageLpContentType").value(hasItem(DEFAULT_IMAGE_LP_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].imageLp").value(hasItem(Base64Utils.encodeToString(DEFAULT_IMAGE_LP))))
            .andExpect(jsonPath("$.[*].imageThumbContentType").value(hasItem(DEFAULT_IMAGE_THUMB_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].imageThumb").value(hasItem(Base64Utils.encodeToString(DEFAULT_IMAGE_THUMB))))
            .andExpect(jsonPath("$.[*].anpr").value(hasItem(DEFAULT_ANPR)))
            .andExpect(jsonPath("$.[*].rfid").value(hasItem(DEFAULT_RFID)))
            .andExpect(jsonPath("$.[*].dataStatus").value(hasItem(DEFAULT_DATA_STATUS)))
            .andExpect(jsonPath("$.[*].gantry").value(hasItem(DEFAULT_GANTRY.intValue())))
            .andExpect(jsonPath("$.[*].lane").value(hasItem(DEFAULT_LANE.intValue())))
            .andExpect(jsonPath("$.[*].kph").value(hasItem(DEFAULT_KPH.intValue())))
            .andExpect(jsonPath("$.[*].ambush").value(hasItem(DEFAULT_AMBUSH.intValue())))
            .andExpect(jsonPath("$.[*].direction").value(hasItem(DEFAULT_DIRECTION.intValue())))
            .andExpect(jsonPath("$.[*].vehicle").value(hasItem(DEFAULT_VEHICLE.intValue())))
            .andExpect(jsonPath("$.[*].issue").value(hasItem(DEFAULT_ISSUE)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS)));

        // Check, that the count call also returns 1
        restImagesMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultImagesShouldNotBeFound(String filter) throws Exception {
        restImagesMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restImagesMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingImages() throws Exception {
        // Get the images
        restImagesMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingImages() throws Exception {
        // Initialize the database
        imagesRepository.saveAndFlush(images);

        int databaseSizeBeforeUpdate = imagesRepository.findAll().size();
        imagesSearchRepository.save(images);
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(imagesSearchRepository.findAll());

        // Update the images
        Images updatedImages = imagesRepository.findById(images.getId()).get();
        // Disconnect from session so that the updates on updatedImages are not directly saved in db
        em.detach(updatedImages);
        updatedImages
            .guid(UPDATED_GUID)
            .plate(UPDATED_PLATE)
            .imageLp(UPDATED_IMAGE_LP)
            .imageLpContentType(UPDATED_IMAGE_LP_CONTENT_TYPE)
            .imageThumb(UPDATED_IMAGE_THUMB)
            .imageThumbContentType(UPDATED_IMAGE_THUMB_CONTENT_TYPE)
            .anpr(UPDATED_ANPR)
            .rfid(UPDATED_RFID)
            .dataStatus(UPDATED_DATA_STATUS)
            .gantry(UPDATED_GANTRY)
            .lane(UPDATED_LANE)
            .kph(UPDATED_KPH)
            .ambush(UPDATED_AMBUSH)
            .direction(UPDATED_DIRECTION)
            .vehicle(UPDATED_VEHICLE)
            .issue(UPDATED_ISSUE)
            .status(UPDATED_STATUS);
        ImagesDTO imagesDTO = imagesMapper.toDto(updatedImages);

        restImagesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, imagesDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(imagesDTO))
            )
            .andExpect(status().isOk());

        // Validate the Images in the database
        List<Images> imagesList = imagesRepository.findAll();
        assertThat(imagesList).hasSize(databaseSizeBeforeUpdate);
        Images testImages = imagesList.get(imagesList.size() - 1);
        assertThat(testImages.getGuid()).isEqualTo(UPDATED_GUID);
        assertThat(testImages.getPlate()).isEqualTo(UPDATED_PLATE);
        assertThat(testImages.getImageLp()).isEqualTo(UPDATED_IMAGE_LP);
        assertThat(testImages.getImageLpContentType()).isEqualTo(UPDATED_IMAGE_LP_CONTENT_TYPE);
        assertThat(testImages.getImageThumb()).isEqualTo(UPDATED_IMAGE_THUMB);
        assertThat(testImages.getImageThumbContentType()).isEqualTo(UPDATED_IMAGE_THUMB_CONTENT_TYPE);
        assertThat(testImages.getAnpr()).isEqualTo(UPDATED_ANPR);
        assertThat(testImages.getRfid()).isEqualTo(UPDATED_RFID);
        assertThat(testImages.getDataStatus()).isEqualTo(UPDATED_DATA_STATUS);
        assertThat(testImages.getGantry()).isEqualTo(UPDATED_GANTRY);
        assertThat(testImages.getLane()).isEqualTo(UPDATED_LANE);
        assertThat(testImages.getKph()).isEqualTo(UPDATED_KPH);
        assertThat(testImages.getAmbush()).isEqualTo(UPDATED_AMBUSH);
        assertThat(testImages.getDirection()).isEqualTo(UPDATED_DIRECTION);
        assertThat(testImages.getVehicle()).isEqualTo(UPDATED_VEHICLE);
        assertThat(testImages.getIssue()).isEqualTo(UPDATED_ISSUE);
        assertThat(testImages.getStatus()).isEqualTo(UPDATED_STATUS);
        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(imagesSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
                List<Images> imagesSearchList = IterableUtils.toList(imagesSearchRepository.findAll());
                Images testImagesSearch = imagesSearchList.get(searchDatabaseSizeAfter - 1);
                assertThat(testImagesSearch.getGuid()).isEqualTo(UPDATED_GUID);
                assertThat(testImagesSearch.getPlate()).isEqualTo(UPDATED_PLATE);
                assertThat(testImagesSearch.getImageLp()).isEqualTo(UPDATED_IMAGE_LP);
                assertThat(testImagesSearch.getImageLpContentType()).isEqualTo(UPDATED_IMAGE_LP_CONTENT_TYPE);
                assertThat(testImagesSearch.getImageThumb()).isEqualTo(UPDATED_IMAGE_THUMB);
                assertThat(testImagesSearch.getImageThumbContentType()).isEqualTo(UPDATED_IMAGE_THUMB_CONTENT_TYPE);
                assertThat(testImagesSearch.getAnpr()).isEqualTo(UPDATED_ANPR);
                assertThat(testImagesSearch.getRfid()).isEqualTo(UPDATED_RFID);
                assertThat(testImagesSearch.getDataStatus()).isEqualTo(UPDATED_DATA_STATUS);
                assertThat(testImagesSearch.getGantry()).isEqualTo(UPDATED_GANTRY);
                assertThat(testImagesSearch.getLane()).isEqualTo(UPDATED_LANE);
                assertThat(testImagesSearch.getKph()).isEqualTo(UPDATED_KPH);
                assertThat(testImagesSearch.getAmbush()).isEqualTo(UPDATED_AMBUSH);
                assertThat(testImagesSearch.getDirection()).isEqualTo(UPDATED_DIRECTION);
                assertThat(testImagesSearch.getVehicle()).isEqualTo(UPDATED_VEHICLE);
                assertThat(testImagesSearch.getIssue()).isEqualTo(UPDATED_ISSUE);
                assertThat(testImagesSearch.getStatus()).isEqualTo(UPDATED_STATUS);
            });
    }

    @Test
    @Transactional
    void putNonExistingImages() throws Exception {
        int databaseSizeBeforeUpdate = imagesRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(imagesSearchRepository.findAll());
        images.setId(count.incrementAndGet());

        // Create the Images
        ImagesDTO imagesDTO = imagesMapper.toDto(images);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restImagesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, imagesDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(imagesDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Images in the database
        List<Images> imagesList = imagesRepository.findAll();
        assertThat(imagesList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(imagesSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithIdMismatchImages() throws Exception {
        int databaseSizeBeforeUpdate = imagesRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(imagesSearchRepository.findAll());
        images.setId(count.incrementAndGet());

        // Create the Images
        ImagesDTO imagesDTO = imagesMapper.toDto(images);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restImagesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(imagesDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Images in the database
        List<Images> imagesList = imagesRepository.findAll();
        assertThat(imagesList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(imagesSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamImages() throws Exception {
        int databaseSizeBeforeUpdate = imagesRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(imagesSearchRepository.findAll());
        images.setId(count.incrementAndGet());

        // Create the Images
        ImagesDTO imagesDTO = imagesMapper.toDto(images);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restImagesMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(imagesDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Images in the database
        List<Images> imagesList = imagesRepository.findAll();
        assertThat(imagesList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(imagesSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void partialUpdateImagesWithPatch() throws Exception {
        // Initialize the database
        imagesRepository.saveAndFlush(images);

        int databaseSizeBeforeUpdate = imagesRepository.findAll().size();

        // Update the images using partial update
        Images partialUpdatedImages = new Images();
        partialUpdatedImages.setId(images.getId());

        partialUpdatedImages
            .plate(UPDATED_PLATE)
            .imageLp(UPDATED_IMAGE_LP)
            .imageLpContentType(UPDATED_IMAGE_LP_CONTENT_TYPE)
            .imageThumb(UPDATED_IMAGE_THUMB)
            .imageThumbContentType(UPDATED_IMAGE_THUMB_CONTENT_TYPE)
            .gantry(UPDATED_GANTRY)
            .kph(UPDATED_KPH)
            .ambush(UPDATED_AMBUSH)
            .direction(UPDATED_DIRECTION);

        restImagesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedImages.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedImages))
            )
            .andExpect(status().isOk());

        // Validate the Images in the database
        List<Images> imagesList = imagesRepository.findAll();
        assertThat(imagesList).hasSize(databaseSizeBeforeUpdate);
        Images testImages = imagesList.get(imagesList.size() - 1);
        assertThat(testImages.getGuid()).isEqualTo(DEFAULT_GUID);
        assertThat(testImages.getPlate()).isEqualTo(UPDATED_PLATE);
        assertThat(testImages.getImageLp()).isEqualTo(UPDATED_IMAGE_LP);
        assertThat(testImages.getImageLpContentType()).isEqualTo(UPDATED_IMAGE_LP_CONTENT_TYPE);
        assertThat(testImages.getImageThumb()).isEqualTo(UPDATED_IMAGE_THUMB);
        assertThat(testImages.getImageThumbContentType()).isEqualTo(UPDATED_IMAGE_THUMB_CONTENT_TYPE);
        assertThat(testImages.getAnpr()).isEqualTo(DEFAULT_ANPR);
        assertThat(testImages.getRfid()).isEqualTo(DEFAULT_RFID);
        assertThat(testImages.getDataStatus()).isEqualTo(DEFAULT_DATA_STATUS);
        assertThat(testImages.getGantry()).isEqualTo(UPDATED_GANTRY);
        assertThat(testImages.getLane()).isEqualTo(DEFAULT_LANE);
        assertThat(testImages.getKph()).isEqualTo(UPDATED_KPH);
        assertThat(testImages.getAmbush()).isEqualTo(UPDATED_AMBUSH);
        assertThat(testImages.getDirection()).isEqualTo(UPDATED_DIRECTION);
        assertThat(testImages.getVehicle()).isEqualTo(DEFAULT_VEHICLE);
        assertThat(testImages.getIssue()).isEqualTo(DEFAULT_ISSUE);
        assertThat(testImages.getStatus()).isEqualTo(DEFAULT_STATUS);
    }

    @Test
    @Transactional
    void fullUpdateImagesWithPatch() throws Exception {
        // Initialize the database
        imagesRepository.saveAndFlush(images);

        int databaseSizeBeforeUpdate = imagesRepository.findAll().size();

        // Update the images using partial update
        Images partialUpdatedImages = new Images();
        partialUpdatedImages.setId(images.getId());

        partialUpdatedImages
            .guid(UPDATED_GUID)
            .plate(UPDATED_PLATE)
            .imageLp(UPDATED_IMAGE_LP)
            .imageLpContentType(UPDATED_IMAGE_LP_CONTENT_TYPE)
            .imageThumb(UPDATED_IMAGE_THUMB)
            .imageThumbContentType(UPDATED_IMAGE_THUMB_CONTENT_TYPE)
            .anpr(UPDATED_ANPR)
            .rfid(UPDATED_RFID)
            .dataStatus(UPDATED_DATA_STATUS)
            .gantry(UPDATED_GANTRY)
            .lane(UPDATED_LANE)
            .kph(UPDATED_KPH)
            .ambush(UPDATED_AMBUSH)
            .direction(UPDATED_DIRECTION)
            .vehicle(UPDATED_VEHICLE)
            .issue(UPDATED_ISSUE)
            .status(UPDATED_STATUS);

        restImagesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedImages.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedImages))
            )
            .andExpect(status().isOk());

        // Validate the Images in the database
        List<Images> imagesList = imagesRepository.findAll();
        assertThat(imagesList).hasSize(databaseSizeBeforeUpdate);
        Images testImages = imagesList.get(imagesList.size() - 1);
        assertThat(testImages.getGuid()).isEqualTo(UPDATED_GUID);
        assertThat(testImages.getPlate()).isEqualTo(UPDATED_PLATE);
        assertThat(testImages.getImageLp()).isEqualTo(UPDATED_IMAGE_LP);
        assertThat(testImages.getImageLpContentType()).isEqualTo(UPDATED_IMAGE_LP_CONTENT_TYPE);
        assertThat(testImages.getImageThumb()).isEqualTo(UPDATED_IMAGE_THUMB);
        assertThat(testImages.getImageThumbContentType()).isEqualTo(UPDATED_IMAGE_THUMB_CONTENT_TYPE);
        assertThat(testImages.getAnpr()).isEqualTo(UPDATED_ANPR);
        assertThat(testImages.getRfid()).isEqualTo(UPDATED_RFID);
        assertThat(testImages.getDataStatus()).isEqualTo(UPDATED_DATA_STATUS);
        assertThat(testImages.getGantry()).isEqualTo(UPDATED_GANTRY);
        assertThat(testImages.getLane()).isEqualTo(UPDATED_LANE);
        assertThat(testImages.getKph()).isEqualTo(UPDATED_KPH);
        assertThat(testImages.getAmbush()).isEqualTo(UPDATED_AMBUSH);
        assertThat(testImages.getDirection()).isEqualTo(UPDATED_DIRECTION);
        assertThat(testImages.getVehicle()).isEqualTo(UPDATED_VEHICLE);
        assertThat(testImages.getIssue()).isEqualTo(UPDATED_ISSUE);
        assertThat(testImages.getStatus()).isEqualTo(UPDATED_STATUS);
    }

    @Test
    @Transactional
    void patchNonExistingImages() throws Exception {
        int databaseSizeBeforeUpdate = imagesRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(imagesSearchRepository.findAll());
        images.setId(count.incrementAndGet());

        // Create the Images
        ImagesDTO imagesDTO = imagesMapper.toDto(images);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restImagesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, imagesDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(imagesDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Images in the database
        List<Images> imagesList = imagesRepository.findAll();
        assertThat(imagesList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(imagesSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithIdMismatchImages() throws Exception {
        int databaseSizeBeforeUpdate = imagesRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(imagesSearchRepository.findAll());
        images.setId(count.incrementAndGet());

        // Create the Images
        ImagesDTO imagesDTO = imagesMapper.toDto(images);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restImagesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(imagesDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Images in the database
        List<Images> imagesList = imagesRepository.findAll();
        assertThat(imagesList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(imagesSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamImages() throws Exception {
        int databaseSizeBeforeUpdate = imagesRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(imagesSearchRepository.findAll());
        images.setId(count.incrementAndGet());

        // Create the Images
        ImagesDTO imagesDTO = imagesMapper.toDto(images);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restImagesMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(imagesDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Images in the database
        List<Images> imagesList = imagesRepository.findAll();
        assertThat(imagesList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(imagesSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void deleteImages() throws Exception {
        // Initialize the database
        imagesRepository.saveAndFlush(images);
        imagesRepository.save(images);
        imagesSearchRepository.save(images);

        int databaseSizeBeforeDelete = imagesRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(imagesSearchRepository.findAll());
        assertThat(searchDatabaseSizeBefore).isEqualTo(databaseSizeBeforeDelete);

        // Delete the images
        restImagesMockMvc
            .perform(delete(ENTITY_API_URL_ID, images.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Images> imagesList = imagesRepository.findAll();
        assertThat(imagesList).hasSize(databaseSizeBeforeDelete - 1);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(imagesSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore - 1);
    }

    @Test
    @Transactional
    void searchImages() throws Exception {
        // Initialize the database
        images = imagesRepository.saveAndFlush(images);
        imagesSearchRepository.save(images);

        // Search the images
        restImagesMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + images.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(images.getId().intValue())))
            .andExpect(jsonPath("$.[*].guid").value(hasItem(DEFAULT_GUID)))
            .andExpect(jsonPath("$.[*].plate").value(hasItem(DEFAULT_PLATE)))
            .andExpect(jsonPath("$.[*].imageLpContentType").value(hasItem(DEFAULT_IMAGE_LP_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].imageLp").value(hasItem(Base64Utils.encodeToString(DEFAULT_IMAGE_LP))))
            .andExpect(jsonPath("$.[*].imageThumbContentType").value(hasItem(DEFAULT_IMAGE_THUMB_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].imageThumb").value(hasItem(Base64Utils.encodeToString(DEFAULT_IMAGE_THUMB))))
            .andExpect(jsonPath("$.[*].anpr").value(hasItem(DEFAULT_ANPR)))
            .andExpect(jsonPath("$.[*].rfid").value(hasItem(DEFAULT_RFID)))
            .andExpect(jsonPath("$.[*].dataStatus").value(hasItem(DEFAULT_DATA_STATUS)))
            .andExpect(jsonPath("$.[*].gantry").value(hasItem(DEFAULT_GANTRY.intValue())))
            .andExpect(jsonPath("$.[*].lane").value(hasItem(DEFAULT_LANE.intValue())))
            .andExpect(jsonPath("$.[*].kph").value(hasItem(DEFAULT_KPH.intValue())))
            .andExpect(jsonPath("$.[*].ambush").value(hasItem(DEFAULT_AMBUSH.intValue())))
            .andExpect(jsonPath("$.[*].direction").value(hasItem(DEFAULT_DIRECTION.intValue())))
            .andExpect(jsonPath("$.[*].vehicle").value(hasItem(DEFAULT_VEHICLE.intValue())))
            .andExpect(jsonPath("$.[*].issue").value(hasItem(DEFAULT_ISSUE)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS)));
    }
}
