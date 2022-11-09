package com.isoft.rfid.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.isoft.rfid.IntegrationTest;
import com.isoft.rfid.domain.Gantry;
import com.isoft.rfid.repository.GantryRepository;
import com.isoft.rfid.repository.search.GantrySearchRepository;
import com.isoft.rfid.service.criteria.GantryCriteria;
import com.isoft.rfid.service.dto.GantryDTO;
import com.isoft.rfid.service.mapper.GantryMapper;
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
 * Integration tests for the {@link GantryResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class GantryResourceIT {

    private static final String DEFAULT_GUID = "AAAAAAAAAA";
    private static final String UPDATED_GUID = "BBBBBBBBBB";

    private static final String DEFAULT_NAME_EN = "AAAAAAAAAA";
    private static final String UPDATED_NAME_EN = "BBBBBBBBBB";

    private static final String DEFAULT_NAME_AR = "AAAAAAAAAA";
    private static final String UPDATED_NAME_AR = "BBBBBBBBBB";

    private static final Integer DEFAULT_STATUS = 1;
    private static final Integer UPDATED_STATUS = 2;
    private static final Integer SMALLER_STATUS = 1 - 1;

    private static final String DEFAULT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CODE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/gantries";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/_search/gantries";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private GantryRepository gantryRepository;

    @Autowired
    private GantryMapper gantryMapper;

    @Autowired
    private GantrySearchRepository gantrySearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restGantryMockMvc;

    private Gantry gantry;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Gantry createEntity(EntityManager em) {
        Gantry gantry = new Gantry()
            .guid(DEFAULT_GUID)
            .nameEn(DEFAULT_NAME_EN)
            .nameAr(DEFAULT_NAME_AR)
            .status(DEFAULT_STATUS)
            .code(DEFAULT_CODE);
        return gantry;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Gantry createUpdatedEntity(EntityManager em) {
        Gantry gantry = new Gantry()
            .guid(UPDATED_GUID)
            .nameEn(UPDATED_NAME_EN)
            .nameAr(UPDATED_NAME_AR)
            .status(UPDATED_STATUS)
            .code(UPDATED_CODE);
        return gantry;
    }

    @AfterEach
    public void cleanupElasticSearchRepository() {
        gantrySearchRepository.deleteAll();
        assertThat(gantrySearchRepository.count()).isEqualTo(0);
    }

    @BeforeEach
    public void initTest() {
        gantry = createEntity(em);
    }

    @Test
    @Transactional
    void createGantry() throws Exception {
        int databaseSizeBeforeCreate = gantryRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(gantrySearchRepository.findAll());
        // Create the Gantry
        GantryDTO gantryDTO = gantryMapper.toDto(gantry);
        restGantryMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(gantryDTO)))
            .andExpect(status().isCreated());

        // Validate the Gantry in the database
        List<Gantry> gantryList = gantryRepository.findAll();
        assertThat(gantryList).hasSize(databaseSizeBeforeCreate + 1);
        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(gantrySearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore + 1);
            });
        Gantry testGantry = gantryList.get(gantryList.size() - 1);
        assertThat(testGantry.getGuid()).isEqualTo(DEFAULT_GUID);
        assertThat(testGantry.getNameEn()).isEqualTo(DEFAULT_NAME_EN);
        assertThat(testGantry.getNameAr()).isEqualTo(DEFAULT_NAME_AR);
        assertThat(testGantry.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testGantry.getCode()).isEqualTo(DEFAULT_CODE);
    }

    @Test
    @Transactional
    void createGantryWithExistingId() throws Exception {
        // Create the Gantry with an existing ID
        gantry.setId(1L);
        GantryDTO gantryDTO = gantryMapper.toDto(gantry);

        int databaseSizeBeforeCreate = gantryRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(gantrySearchRepository.findAll());

        // An entity with an existing ID cannot be created, so this API call must fail
        restGantryMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(gantryDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Gantry in the database
        List<Gantry> gantryList = gantryRepository.findAll();
        assertThat(gantryList).hasSize(databaseSizeBeforeCreate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(gantrySearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkGuidIsRequired() throws Exception {
        int databaseSizeBeforeTest = gantryRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(gantrySearchRepository.findAll());
        // set the field null
        gantry.setGuid(null);

        // Create the Gantry, which fails.
        GantryDTO gantryDTO = gantryMapper.toDto(gantry);

        restGantryMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(gantryDTO)))
            .andExpect(status().isBadRequest());

        List<Gantry> gantryList = gantryRepository.findAll();
        assertThat(gantryList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(gantrySearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkNameEnIsRequired() throws Exception {
        int databaseSizeBeforeTest = gantryRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(gantrySearchRepository.findAll());
        // set the field null
        gantry.setNameEn(null);

        // Create the Gantry, which fails.
        GantryDTO gantryDTO = gantryMapper.toDto(gantry);

        restGantryMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(gantryDTO)))
            .andExpect(status().isBadRequest());

        List<Gantry> gantryList = gantryRepository.findAll();
        assertThat(gantryList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(gantrySearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkNameArIsRequired() throws Exception {
        int databaseSizeBeforeTest = gantryRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(gantrySearchRepository.findAll());
        // set the field null
        gantry.setNameAr(null);

        // Create the Gantry, which fails.
        GantryDTO gantryDTO = gantryMapper.toDto(gantry);

        restGantryMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(gantryDTO)))
            .andExpect(status().isBadRequest());

        List<Gantry> gantryList = gantryRepository.findAll();
        assertThat(gantryList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(gantrySearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void getAllGantries() throws Exception {
        // Initialize the database
        gantryRepository.saveAndFlush(gantry);

        // Get all the gantryList
        restGantryMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(gantry.getId().intValue())))
            .andExpect(jsonPath("$.[*].guid").value(hasItem(DEFAULT_GUID)))
            .andExpect(jsonPath("$.[*].nameEn").value(hasItem(DEFAULT_NAME_EN)))
            .andExpect(jsonPath("$.[*].nameAr").value(hasItem(DEFAULT_NAME_AR)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS)))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)));
    }

    @Test
    @Transactional
    void getGantry() throws Exception {
        // Initialize the database
        gantryRepository.saveAndFlush(gantry);

        // Get the gantry
        restGantryMockMvc
            .perform(get(ENTITY_API_URL_ID, gantry.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(gantry.getId().intValue()))
            .andExpect(jsonPath("$.guid").value(DEFAULT_GUID))
            .andExpect(jsonPath("$.nameEn").value(DEFAULT_NAME_EN))
            .andExpect(jsonPath("$.nameAr").value(DEFAULT_NAME_AR))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE));
    }

    @Test
    @Transactional
    void getGantriesByIdFiltering() throws Exception {
        // Initialize the database
        gantryRepository.saveAndFlush(gantry);

        Long id = gantry.getId();

        defaultGantryShouldBeFound("id.equals=" + id);
        defaultGantryShouldNotBeFound("id.notEquals=" + id);

        defaultGantryShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultGantryShouldNotBeFound("id.greaterThan=" + id);

        defaultGantryShouldBeFound("id.lessThanOrEqual=" + id);
        defaultGantryShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllGantriesByGuidIsEqualToSomething() throws Exception {
        // Initialize the database
        gantryRepository.saveAndFlush(gantry);

        // Get all the gantryList where guid equals to DEFAULT_GUID
        defaultGantryShouldBeFound("guid.equals=" + DEFAULT_GUID);

        // Get all the gantryList where guid equals to UPDATED_GUID
        defaultGantryShouldNotBeFound("guid.equals=" + UPDATED_GUID);
    }

    @Test
    @Transactional
    void getAllGantriesByGuidIsInShouldWork() throws Exception {
        // Initialize the database
        gantryRepository.saveAndFlush(gantry);

        // Get all the gantryList where guid in DEFAULT_GUID or UPDATED_GUID
        defaultGantryShouldBeFound("guid.in=" + DEFAULT_GUID + "," + UPDATED_GUID);

        // Get all the gantryList where guid equals to UPDATED_GUID
        defaultGantryShouldNotBeFound("guid.in=" + UPDATED_GUID);
    }

    @Test
    @Transactional
    void getAllGantriesByGuidIsNullOrNotNull() throws Exception {
        // Initialize the database
        gantryRepository.saveAndFlush(gantry);

        // Get all the gantryList where guid is not null
        defaultGantryShouldBeFound("guid.specified=true");

        // Get all the gantryList where guid is null
        defaultGantryShouldNotBeFound("guid.specified=false");
    }

    @Test
    @Transactional
    void getAllGantriesByGuidContainsSomething() throws Exception {
        // Initialize the database
        gantryRepository.saveAndFlush(gantry);

        // Get all the gantryList where guid contains DEFAULT_GUID
        defaultGantryShouldBeFound("guid.contains=" + DEFAULT_GUID);

        // Get all the gantryList where guid contains UPDATED_GUID
        defaultGantryShouldNotBeFound("guid.contains=" + UPDATED_GUID);
    }

    @Test
    @Transactional
    void getAllGantriesByGuidNotContainsSomething() throws Exception {
        // Initialize the database
        gantryRepository.saveAndFlush(gantry);

        // Get all the gantryList where guid does not contain DEFAULT_GUID
        defaultGantryShouldNotBeFound("guid.doesNotContain=" + DEFAULT_GUID);

        // Get all the gantryList where guid does not contain UPDATED_GUID
        defaultGantryShouldBeFound("guid.doesNotContain=" + UPDATED_GUID);
    }

    @Test
    @Transactional
    void getAllGantriesByNameEnIsEqualToSomething() throws Exception {
        // Initialize the database
        gantryRepository.saveAndFlush(gantry);

        // Get all the gantryList where nameEn equals to DEFAULT_NAME_EN
        defaultGantryShouldBeFound("nameEn.equals=" + DEFAULT_NAME_EN);

        // Get all the gantryList where nameEn equals to UPDATED_NAME_EN
        defaultGantryShouldNotBeFound("nameEn.equals=" + UPDATED_NAME_EN);
    }

    @Test
    @Transactional
    void getAllGantriesByNameEnIsInShouldWork() throws Exception {
        // Initialize the database
        gantryRepository.saveAndFlush(gantry);

        // Get all the gantryList where nameEn in DEFAULT_NAME_EN or UPDATED_NAME_EN
        defaultGantryShouldBeFound("nameEn.in=" + DEFAULT_NAME_EN + "," + UPDATED_NAME_EN);

        // Get all the gantryList where nameEn equals to UPDATED_NAME_EN
        defaultGantryShouldNotBeFound("nameEn.in=" + UPDATED_NAME_EN);
    }

    @Test
    @Transactional
    void getAllGantriesByNameEnIsNullOrNotNull() throws Exception {
        // Initialize the database
        gantryRepository.saveAndFlush(gantry);

        // Get all the gantryList where nameEn is not null
        defaultGantryShouldBeFound("nameEn.specified=true");

        // Get all the gantryList where nameEn is null
        defaultGantryShouldNotBeFound("nameEn.specified=false");
    }

    @Test
    @Transactional
    void getAllGantriesByNameEnContainsSomething() throws Exception {
        // Initialize the database
        gantryRepository.saveAndFlush(gantry);

        // Get all the gantryList where nameEn contains DEFAULT_NAME_EN
        defaultGantryShouldBeFound("nameEn.contains=" + DEFAULT_NAME_EN);

        // Get all the gantryList where nameEn contains UPDATED_NAME_EN
        defaultGantryShouldNotBeFound("nameEn.contains=" + UPDATED_NAME_EN);
    }

    @Test
    @Transactional
    void getAllGantriesByNameEnNotContainsSomething() throws Exception {
        // Initialize the database
        gantryRepository.saveAndFlush(gantry);

        // Get all the gantryList where nameEn does not contain DEFAULT_NAME_EN
        defaultGantryShouldNotBeFound("nameEn.doesNotContain=" + DEFAULT_NAME_EN);

        // Get all the gantryList where nameEn does not contain UPDATED_NAME_EN
        defaultGantryShouldBeFound("nameEn.doesNotContain=" + UPDATED_NAME_EN);
    }

    @Test
    @Transactional
    void getAllGantriesByNameArIsEqualToSomething() throws Exception {
        // Initialize the database
        gantryRepository.saveAndFlush(gantry);

        // Get all the gantryList where nameAr equals to DEFAULT_NAME_AR
        defaultGantryShouldBeFound("nameAr.equals=" + DEFAULT_NAME_AR);

        // Get all the gantryList where nameAr equals to UPDATED_NAME_AR
        defaultGantryShouldNotBeFound("nameAr.equals=" + UPDATED_NAME_AR);
    }

    @Test
    @Transactional
    void getAllGantriesByNameArIsInShouldWork() throws Exception {
        // Initialize the database
        gantryRepository.saveAndFlush(gantry);

        // Get all the gantryList where nameAr in DEFAULT_NAME_AR or UPDATED_NAME_AR
        defaultGantryShouldBeFound("nameAr.in=" + DEFAULT_NAME_AR + "," + UPDATED_NAME_AR);

        // Get all the gantryList where nameAr equals to UPDATED_NAME_AR
        defaultGantryShouldNotBeFound("nameAr.in=" + UPDATED_NAME_AR);
    }

    @Test
    @Transactional
    void getAllGantriesByNameArIsNullOrNotNull() throws Exception {
        // Initialize the database
        gantryRepository.saveAndFlush(gantry);

        // Get all the gantryList where nameAr is not null
        defaultGantryShouldBeFound("nameAr.specified=true");

        // Get all the gantryList where nameAr is null
        defaultGantryShouldNotBeFound("nameAr.specified=false");
    }

    @Test
    @Transactional
    void getAllGantriesByNameArContainsSomething() throws Exception {
        // Initialize the database
        gantryRepository.saveAndFlush(gantry);

        // Get all the gantryList where nameAr contains DEFAULT_NAME_AR
        defaultGantryShouldBeFound("nameAr.contains=" + DEFAULT_NAME_AR);

        // Get all the gantryList where nameAr contains UPDATED_NAME_AR
        defaultGantryShouldNotBeFound("nameAr.contains=" + UPDATED_NAME_AR);
    }

    @Test
    @Transactional
    void getAllGantriesByNameArNotContainsSomething() throws Exception {
        // Initialize the database
        gantryRepository.saveAndFlush(gantry);

        // Get all the gantryList where nameAr does not contain DEFAULT_NAME_AR
        defaultGantryShouldNotBeFound("nameAr.doesNotContain=" + DEFAULT_NAME_AR);

        // Get all the gantryList where nameAr does not contain UPDATED_NAME_AR
        defaultGantryShouldBeFound("nameAr.doesNotContain=" + UPDATED_NAME_AR);
    }

    @Test
    @Transactional
    void getAllGantriesByStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        gantryRepository.saveAndFlush(gantry);

        // Get all the gantryList where status equals to DEFAULT_STATUS
        defaultGantryShouldBeFound("status.equals=" + DEFAULT_STATUS);

        // Get all the gantryList where status equals to UPDATED_STATUS
        defaultGantryShouldNotBeFound("status.equals=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllGantriesByStatusIsInShouldWork() throws Exception {
        // Initialize the database
        gantryRepository.saveAndFlush(gantry);

        // Get all the gantryList where status in DEFAULT_STATUS or UPDATED_STATUS
        defaultGantryShouldBeFound("status.in=" + DEFAULT_STATUS + "," + UPDATED_STATUS);

        // Get all the gantryList where status equals to UPDATED_STATUS
        defaultGantryShouldNotBeFound("status.in=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllGantriesByStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        gantryRepository.saveAndFlush(gantry);

        // Get all the gantryList where status is not null
        defaultGantryShouldBeFound("status.specified=true");

        // Get all the gantryList where status is null
        defaultGantryShouldNotBeFound("status.specified=false");
    }

    @Test
    @Transactional
    void getAllGantriesByStatusIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        gantryRepository.saveAndFlush(gantry);

        // Get all the gantryList where status is greater than or equal to DEFAULT_STATUS
        defaultGantryShouldBeFound("status.greaterThanOrEqual=" + DEFAULT_STATUS);

        // Get all the gantryList where status is greater than or equal to UPDATED_STATUS
        defaultGantryShouldNotBeFound("status.greaterThanOrEqual=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllGantriesByStatusIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        gantryRepository.saveAndFlush(gantry);

        // Get all the gantryList where status is less than or equal to DEFAULT_STATUS
        defaultGantryShouldBeFound("status.lessThanOrEqual=" + DEFAULT_STATUS);

        // Get all the gantryList where status is less than or equal to SMALLER_STATUS
        defaultGantryShouldNotBeFound("status.lessThanOrEqual=" + SMALLER_STATUS);
    }

    @Test
    @Transactional
    void getAllGantriesByStatusIsLessThanSomething() throws Exception {
        // Initialize the database
        gantryRepository.saveAndFlush(gantry);

        // Get all the gantryList where status is less than DEFAULT_STATUS
        defaultGantryShouldNotBeFound("status.lessThan=" + DEFAULT_STATUS);

        // Get all the gantryList where status is less than UPDATED_STATUS
        defaultGantryShouldBeFound("status.lessThan=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllGantriesByStatusIsGreaterThanSomething() throws Exception {
        // Initialize the database
        gantryRepository.saveAndFlush(gantry);

        // Get all the gantryList where status is greater than DEFAULT_STATUS
        defaultGantryShouldNotBeFound("status.greaterThan=" + DEFAULT_STATUS);

        // Get all the gantryList where status is greater than SMALLER_STATUS
        defaultGantryShouldBeFound("status.greaterThan=" + SMALLER_STATUS);
    }

    @Test
    @Transactional
    void getAllGantriesByCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        gantryRepository.saveAndFlush(gantry);

        // Get all the gantryList where code equals to DEFAULT_CODE
        defaultGantryShouldBeFound("code.equals=" + DEFAULT_CODE);

        // Get all the gantryList where code equals to UPDATED_CODE
        defaultGantryShouldNotBeFound("code.equals=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllGantriesByCodeIsInShouldWork() throws Exception {
        // Initialize the database
        gantryRepository.saveAndFlush(gantry);

        // Get all the gantryList where code in DEFAULT_CODE or UPDATED_CODE
        defaultGantryShouldBeFound("code.in=" + DEFAULT_CODE + "," + UPDATED_CODE);

        // Get all the gantryList where code equals to UPDATED_CODE
        defaultGantryShouldNotBeFound("code.in=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllGantriesByCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        gantryRepository.saveAndFlush(gantry);

        // Get all the gantryList where code is not null
        defaultGantryShouldBeFound("code.specified=true");

        // Get all the gantryList where code is null
        defaultGantryShouldNotBeFound("code.specified=false");
    }

    @Test
    @Transactional
    void getAllGantriesByCodeContainsSomething() throws Exception {
        // Initialize the database
        gantryRepository.saveAndFlush(gantry);

        // Get all the gantryList where code contains DEFAULT_CODE
        defaultGantryShouldBeFound("code.contains=" + DEFAULT_CODE);

        // Get all the gantryList where code contains UPDATED_CODE
        defaultGantryShouldNotBeFound("code.contains=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllGantriesByCodeNotContainsSomething() throws Exception {
        // Initialize the database
        gantryRepository.saveAndFlush(gantry);

        // Get all the gantryList where code does not contain DEFAULT_CODE
        defaultGantryShouldNotBeFound("code.doesNotContain=" + DEFAULT_CODE);

        // Get all the gantryList where code does not contain UPDATED_CODE
        defaultGantryShouldBeFound("code.doesNotContain=" + UPDATED_CODE);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultGantryShouldBeFound(String filter) throws Exception {
        restGantryMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(gantry.getId().intValue())))
            .andExpect(jsonPath("$.[*].guid").value(hasItem(DEFAULT_GUID)))
            .andExpect(jsonPath("$.[*].nameEn").value(hasItem(DEFAULT_NAME_EN)))
            .andExpect(jsonPath("$.[*].nameAr").value(hasItem(DEFAULT_NAME_AR)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS)))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)));

        // Check, that the count call also returns 1
        restGantryMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultGantryShouldNotBeFound(String filter) throws Exception {
        restGantryMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restGantryMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingGantry() throws Exception {
        // Get the gantry
        restGantryMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingGantry() throws Exception {
        // Initialize the database
        gantryRepository.saveAndFlush(gantry);

        int databaseSizeBeforeUpdate = gantryRepository.findAll().size();
        gantrySearchRepository.save(gantry);
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(gantrySearchRepository.findAll());

        // Update the gantry
        Gantry updatedGantry = gantryRepository.findById(gantry.getId()).get();
        // Disconnect from session so that the updates on updatedGantry are not directly saved in db
        em.detach(updatedGantry);
        updatedGantry.guid(UPDATED_GUID).nameEn(UPDATED_NAME_EN).nameAr(UPDATED_NAME_AR).status(UPDATED_STATUS).code(UPDATED_CODE);
        GantryDTO gantryDTO = gantryMapper.toDto(updatedGantry);

        restGantryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, gantryDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(gantryDTO))
            )
            .andExpect(status().isOk());

        // Validate the Gantry in the database
        List<Gantry> gantryList = gantryRepository.findAll();
        assertThat(gantryList).hasSize(databaseSizeBeforeUpdate);
        Gantry testGantry = gantryList.get(gantryList.size() - 1);
        assertThat(testGantry.getGuid()).isEqualTo(UPDATED_GUID);
        assertThat(testGantry.getNameEn()).isEqualTo(UPDATED_NAME_EN);
        assertThat(testGantry.getNameAr()).isEqualTo(UPDATED_NAME_AR);
        assertThat(testGantry.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testGantry.getCode()).isEqualTo(UPDATED_CODE);
        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(gantrySearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
                List<Gantry> gantrySearchList = IterableUtils.toList(gantrySearchRepository.findAll());
                Gantry testGantrySearch = gantrySearchList.get(searchDatabaseSizeAfter - 1);
                assertThat(testGantrySearch.getGuid()).isEqualTo(UPDATED_GUID);
                assertThat(testGantrySearch.getNameEn()).isEqualTo(UPDATED_NAME_EN);
                assertThat(testGantrySearch.getNameAr()).isEqualTo(UPDATED_NAME_AR);
                assertThat(testGantrySearch.getStatus()).isEqualTo(UPDATED_STATUS);
                assertThat(testGantrySearch.getCode()).isEqualTo(UPDATED_CODE);
            });
    }

    @Test
    @Transactional
    void putNonExistingGantry() throws Exception {
        int databaseSizeBeforeUpdate = gantryRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(gantrySearchRepository.findAll());
        gantry.setId(count.incrementAndGet());

        // Create the Gantry
        GantryDTO gantryDTO = gantryMapper.toDto(gantry);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restGantryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, gantryDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(gantryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Gantry in the database
        List<Gantry> gantryList = gantryRepository.findAll();
        assertThat(gantryList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(gantrySearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithIdMismatchGantry() throws Exception {
        int databaseSizeBeforeUpdate = gantryRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(gantrySearchRepository.findAll());
        gantry.setId(count.incrementAndGet());

        // Create the Gantry
        GantryDTO gantryDTO = gantryMapper.toDto(gantry);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restGantryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(gantryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Gantry in the database
        List<Gantry> gantryList = gantryRepository.findAll();
        assertThat(gantryList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(gantrySearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamGantry() throws Exception {
        int databaseSizeBeforeUpdate = gantryRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(gantrySearchRepository.findAll());
        gantry.setId(count.incrementAndGet());

        // Create the Gantry
        GantryDTO gantryDTO = gantryMapper.toDto(gantry);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restGantryMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(gantryDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Gantry in the database
        List<Gantry> gantryList = gantryRepository.findAll();
        assertThat(gantryList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(gantrySearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void partialUpdateGantryWithPatch() throws Exception {
        // Initialize the database
        gantryRepository.saveAndFlush(gantry);

        int databaseSizeBeforeUpdate = gantryRepository.findAll().size();

        // Update the gantry using partial update
        Gantry partialUpdatedGantry = new Gantry();
        partialUpdatedGantry.setId(gantry.getId());

        partialUpdatedGantry.guid(UPDATED_GUID);

        restGantryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedGantry.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedGantry))
            )
            .andExpect(status().isOk());

        // Validate the Gantry in the database
        List<Gantry> gantryList = gantryRepository.findAll();
        assertThat(gantryList).hasSize(databaseSizeBeforeUpdate);
        Gantry testGantry = gantryList.get(gantryList.size() - 1);
        assertThat(testGantry.getGuid()).isEqualTo(UPDATED_GUID);
        assertThat(testGantry.getNameEn()).isEqualTo(DEFAULT_NAME_EN);
        assertThat(testGantry.getNameAr()).isEqualTo(DEFAULT_NAME_AR);
        assertThat(testGantry.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testGantry.getCode()).isEqualTo(DEFAULT_CODE);
    }

    @Test
    @Transactional
    void fullUpdateGantryWithPatch() throws Exception {
        // Initialize the database
        gantryRepository.saveAndFlush(gantry);

        int databaseSizeBeforeUpdate = gantryRepository.findAll().size();

        // Update the gantry using partial update
        Gantry partialUpdatedGantry = new Gantry();
        partialUpdatedGantry.setId(gantry.getId());

        partialUpdatedGantry.guid(UPDATED_GUID).nameEn(UPDATED_NAME_EN).nameAr(UPDATED_NAME_AR).status(UPDATED_STATUS).code(UPDATED_CODE);

        restGantryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedGantry.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedGantry))
            )
            .andExpect(status().isOk());

        // Validate the Gantry in the database
        List<Gantry> gantryList = gantryRepository.findAll();
        assertThat(gantryList).hasSize(databaseSizeBeforeUpdate);
        Gantry testGantry = gantryList.get(gantryList.size() - 1);
        assertThat(testGantry.getGuid()).isEqualTo(UPDATED_GUID);
        assertThat(testGantry.getNameEn()).isEqualTo(UPDATED_NAME_EN);
        assertThat(testGantry.getNameAr()).isEqualTo(UPDATED_NAME_AR);
        assertThat(testGantry.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testGantry.getCode()).isEqualTo(UPDATED_CODE);
    }

    @Test
    @Transactional
    void patchNonExistingGantry() throws Exception {
        int databaseSizeBeforeUpdate = gantryRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(gantrySearchRepository.findAll());
        gantry.setId(count.incrementAndGet());

        // Create the Gantry
        GantryDTO gantryDTO = gantryMapper.toDto(gantry);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restGantryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, gantryDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(gantryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Gantry in the database
        List<Gantry> gantryList = gantryRepository.findAll();
        assertThat(gantryList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(gantrySearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithIdMismatchGantry() throws Exception {
        int databaseSizeBeforeUpdate = gantryRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(gantrySearchRepository.findAll());
        gantry.setId(count.incrementAndGet());

        // Create the Gantry
        GantryDTO gantryDTO = gantryMapper.toDto(gantry);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restGantryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(gantryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Gantry in the database
        List<Gantry> gantryList = gantryRepository.findAll();
        assertThat(gantryList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(gantrySearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamGantry() throws Exception {
        int databaseSizeBeforeUpdate = gantryRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(gantrySearchRepository.findAll());
        gantry.setId(count.incrementAndGet());

        // Create the Gantry
        GantryDTO gantryDTO = gantryMapper.toDto(gantry);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restGantryMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(gantryDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Gantry in the database
        List<Gantry> gantryList = gantryRepository.findAll();
        assertThat(gantryList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(gantrySearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void deleteGantry() throws Exception {
        // Initialize the database
        gantryRepository.saveAndFlush(gantry);
        gantryRepository.save(gantry);
        gantrySearchRepository.save(gantry);

        int databaseSizeBeforeDelete = gantryRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(gantrySearchRepository.findAll());
        assertThat(searchDatabaseSizeBefore).isEqualTo(databaseSizeBeforeDelete);

        // Delete the gantry
        restGantryMockMvc
            .perform(delete(ENTITY_API_URL_ID, gantry.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Gantry> gantryList = gantryRepository.findAll();
        assertThat(gantryList).hasSize(databaseSizeBeforeDelete - 1);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(gantrySearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore - 1);
    }

    @Test
    @Transactional
    void searchGantry() throws Exception {
        // Initialize the database
        gantry = gantryRepository.saveAndFlush(gantry);
        gantrySearchRepository.save(gantry);

        // Search the gantry
        restGantryMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + gantry.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(gantry.getId().intValue())))
            .andExpect(jsonPath("$.[*].guid").value(hasItem(DEFAULT_GUID)))
            .andExpect(jsonPath("$.[*].nameEn").value(hasItem(DEFAULT_NAME_EN)))
            .andExpect(jsonPath("$.[*].nameAr").value(hasItem(DEFAULT_NAME_AR)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS)))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)));
    }
}
