package com.isoft.rfid.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.isoft.rfid.IntegrationTest;
import com.isoft.rfid.domain.Vehicles;
import com.isoft.rfid.repository.VehiclesRepository;
import com.isoft.rfid.repository.search.VehiclesSearchRepository;
import com.isoft.rfid.service.criteria.VehiclesCriteria;
import com.isoft.rfid.service.dto.VehiclesDTO;
import com.isoft.rfid.service.mapper.VehiclesMapper;
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
 * Integration tests for the {@link VehiclesResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class VehiclesResourceIT {

    private static final String DEFAULT_GUID = "AAAAAAAAAA";
    private static final String UPDATED_GUID = "BBBBBBBBBB";

    private static final String DEFAULT_PLATE = "AAAAAAAAAA";
    private static final String UPDATED_PLATE = "BBBBBBBBBB";

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

    private static final String DEFAULT_STATUS = "AAAAAAAAAA";
    private static final String UPDATED_STATUS = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/vehicles";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/_search/vehicles";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private VehiclesRepository vehiclesRepository;

    @Autowired
    private VehiclesMapper vehiclesMapper;

    @Autowired
    private VehiclesSearchRepository vehiclesSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restVehiclesMockMvc;

    private Vehicles vehicles;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Vehicles createEntity(EntityManager em) {
        Vehicles vehicles = new Vehicles()
            .guid(DEFAULT_GUID)
            .plate(DEFAULT_PLATE)
            .anpr(DEFAULT_ANPR)
            .rfid(DEFAULT_RFID)
            .dataStatus(DEFAULT_DATA_STATUS)
            .gantry(DEFAULT_GANTRY)
            .lane(DEFAULT_LANE)
            .kph(DEFAULT_KPH)
            .ambush(DEFAULT_AMBUSH)
            .direction(DEFAULT_DIRECTION)
            .vehicle(DEFAULT_VEHICLE)
            .status(DEFAULT_STATUS);
        return vehicles;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Vehicles createUpdatedEntity(EntityManager em) {
        Vehicles vehicles = new Vehicles()
            .guid(UPDATED_GUID)
            .plate(UPDATED_PLATE)
            .anpr(UPDATED_ANPR)
            .rfid(UPDATED_RFID)
            .dataStatus(UPDATED_DATA_STATUS)
            .gantry(UPDATED_GANTRY)
            .lane(UPDATED_LANE)
            .kph(UPDATED_KPH)
            .ambush(UPDATED_AMBUSH)
            .direction(UPDATED_DIRECTION)
            .vehicle(UPDATED_VEHICLE)
            .status(UPDATED_STATUS);
        return vehicles;
    }

    @AfterEach
    public void cleanupElasticSearchRepository() {
        vehiclesSearchRepository.deleteAll();
        assertThat(vehiclesSearchRepository.count()).isEqualTo(0);
    }

    @BeforeEach
    public void initTest() {
        vehicles = createEntity(em);
    }

    @Test
    @Transactional
    void createVehicles() throws Exception {
        int databaseSizeBeforeCreate = vehiclesRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(vehiclesSearchRepository.findAll());
        // Create the Vehicles
        VehiclesDTO vehiclesDTO = vehiclesMapper.toDto(vehicles);
        restVehiclesMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(vehiclesDTO)))
            .andExpect(status().isCreated());

        // Validate the Vehicles in the database
        List<Vehicles> vehiclesList = vehiclesRepository.findAll();
        assertThat(vehiclesList).hasSize(databaseSizeBeforeCreate + 1);
        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(vehiclesSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore + 1);
            });
        Vehicles testVehicles = vehiclesList.get(vehiclesList.size() - 1);
        assertThat(testVehicles.getGuid()).isEqualTo(DEFAULT_GUID);
        assertThat(testVehicles.getPlate()).isEqualTo(DEFAULT_PLATE);
        assertThat(testVehicles.getAnpr()).isEqualTo(DEFAULT_ANPR);
        assertThat(testVehicles.getRfid()).isEqualTo(DEFAULT_RFID);
        assertThat(testVehicles.getDataStatus()).isEqualTo(DEFAULT_DATA_STATUS);
        assertThat(testVehicles.getGantry()).isEqualTo(DEFAULT_GANTRY);
        assertThat(testVehicles.getLane()).isEqualTo(DEFAULT_LANE);
        assertThat(testVehicles.getKph()).isEqualTo(DEFAULT_KPH);
        assertThat(testVehicles.getAmbush()).isEqualTo(DEFAULT_AMBUSH);
        assertThat(testVehicles.getDirection()).isEqualTo(DEFAULT_DIRECTION);
        assertThat(testVehicles.getVehicle()).isEqualTo(DEFAULT_VEHICLE);
        assertThat(testVehicles.getStatus()).isEqualTo(DEFAULT_STATUS);
    }

    @Test
    @Transactional
    void createVehiclesWithExistingId() throws Exception {
        // Create the Vehicles with an existing ID
        vehicles.setId(1L);
        VehiclesDTO vehiclesDTO = vehiclesMapper.toDto(vehicles);

        int databaseSizeBeforeCreate = vehiclesRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(vehiclesSearchRepository.findAll());

        // An entity with an existing ID cannot be created, so this API call must fail
        restVehiclesMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(vehiclesDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Vehicles in the database
        List<Vehicles> vehiclesList = vehiclesRepository.findAll();
        assertThat(vehiclesList).hasSize(databaseSizeBeforeCreate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(vehiclesSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkGuidIsRequired() throws Exception {
        int databaseSizeBeforeTest = vehiclesRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(vehiclesSearchRepository.findAll());
        // set the field null
        vehicles.setGuid(null);

        // Create the Vehicles, which fails.
        VehiclesDTO vehiclesDTO = vehiclesMapper.toDto(vehicles);

        restVehiclesMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(vehiclesDTO)))
            .andExpect(status().isBadRequest());

        List<Vehicles> vehiclesList = vehiclesRepository.findAll();
        assertThat(vehiclesList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(vehiclesSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkDataStatusIsRequired() throws Exception {
        int databaseSizeBeforeTest = vehiclesRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(vehiclesSearchRepository.findAll());
        // set the field null
        vehicles.setDataStatus(null);

        // Create the Vehicles, which fails.
        VehiclesDTO vehiclesDTO = vehiclesMapper.toDto(vehicles);

        restVehiclesMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(vehiclesDTO)))
            .andExpect(status().isBadRequest());

        List<Vehicles> vehiclesList = vehiclesRepository.findAll();
        assertThat(vehiclesList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(vehiclesSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkGantryIsRequired() throws Exception {
        int databaseSizeBeforeTest = vehiclesRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(vehiclesSearchRepository.findAll());
        // set the field null
        vehicles.setGantry(null);

        // Create the Vehicles, which fails.
        VehiclesDTO vehiclesDTO = vehiclesMapper.toDto(vehicles);

        restVehiclesMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(vehiclesDTO)))
            .andExpect(status().isBadRequest());

        List<Vehicles> vehiclesList = vehiclesRepository.findAll();
        assertThat(vehiclesList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(vehiclesSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkLaneIsRequired() throws Exception {
        int databaseSizeBeforeTest = vehiclesRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(vehiclesSearchRepository.findAll());
        // set the field null
        vehicles.setLane(null);

        // Create the Vehicles, which fails.
        VehiclesDTO vehiclesDTO = vehiclesMapper.toDto(vehicles);

        restVehiclesMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(vehiclesDTO)))
            .andExpect(status().isBadRequest());

        List<Vehicles> vehiclesList = vehiclesRepository.findAll();
        assertThat(vehiclesList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(vehiclesSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkVehicleIsRequired() throws Exception {
        int databaseSizeBeforeTest = vehiclesRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(vehiclesSearchRepository.findAll());
        // set the field null
        vehicles.setVehicle(null);

        // Create the Vehicles, which fails.
        VehiclesDTO vehiclesDTO = vehiclesMapper.toDto(vehicles);

        restVehiclesMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(vehiclesDTO)))
            .andExpect(status().isBadRequest());

        List<Vehicles> vehiclesList = vehiclesRepository.findAll();
        assertThat(vehiclesList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(vehiclesSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void getAllVehicles() throws Exception {
        // Initialize the database
        vehiclesRepository.saveAndFlush(vehicles);

        // Get all the vehiclesList
        restVehiclesMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(vehicles.getId().intValue())))
            .andExpect(jsonPath("$.[*].guid").value(hasItem(DEFAULT_GUID)))
            .andExpect(jsonPath("$.[*].plate").value(hasItem(DEFAULT_PLATE)))
            .andExpect(jsonPath("$.[*].anpr").value(hasItem(DEFAULT_ANPR)))
            .andExpect(jsonPath("$.[*].rfid").value(hasItem(DEFAULT_RFID)))
            .andExpect(jsonPath("$.[*].dataStatus").value(hasItem(DEFAULT_DATA_STATUS)))
            .andExpect(jsonPath("$.[*].gantry").value(hasItem(DEFAULT_GANTRY.intValue())))
            .andExpect(jsonPath("$.[*].lane").value(hasItem(DEFAULT_LANE.intValue())))
            .andExpect(jsonPath("$.[*].kph").value(hasItem(DEFAULT_KPH.intValue())))
            .andExpect(jsonPath("$.[*].ambush").value(hasItem(DEFAULT_AMBUSH.intValue())))
            .andExpect(jsonPath("$.[*].direction").value(hasItem(DEFAULT_DIRECTION.intValue())))
            .andExpect(jsonPath("$.[*].vehicle").value(hasItem(DEFAULT_VEHICLE.intValue())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS)));
    }

    @Test
    @Transactional
    void getVehicles() throws Exception {
        // Initialize the database
        vehiclesRepository.saveAndFlush(vehicles);

        // Get the vehicles
        restVehiclesMockMvc
            .perform(get(ENTITY_API_URL_ID, vehicles.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(vehicles.getId().intValue()))
            .andExpect(jsonPath("$.guid").value(DEFAULT_GUID))
            .andExpect(jsonPath("$.plate").value(DEFAULT_PLATE))
            .andExpect(jsonPath("$.anpr").value(DEFAULT_ANPR))
            .andExpect(jsonPath("$.rfid").value(DEFAULT_RFID))
            .andExpect(jsonPath("$.dataStatus").value(DEFAULT_DATA_STATUS))
            .andExpect(jsonPath("$.gantry").value(DEFAULT_GANTRY.intValue()))
            .andExpect(jsonPath("$.lane").value(DEFAULT_LANE.intValue()))
            .andExpect(jsonPath("$.kph").value(DEFAULT_KPH.intValue()))
            .andExpect(jsonPath("$.ambush").value(DEFAULT_AMBUSH.intValue()))
            .andExpect(jsonPath("$.direction").value(DEFAULT_DIRECTION.intValue()))
            .andExpect(jsonPath("$.vehicle").value(DEFAULT_VEHICLE.intValue()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS));
    }

    @Test
    @Transactional
    void getVehiclesByIdFiltering() throws Exception {
        // Initialize the database
        vehiclesRepository.saveAndFlush(vehicles);

        Long id = vehicles.getId();

        defaultVehiclesShouldBeFound("id.equals=" + id);
        defaultVehiclesShouldNotBeFound("id.notEquals=" + id);

        defaultVehiclesShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultVehiclesShouldNotBeFound("id.greaterThan=" + id);

        defaultVehiclesShouldBeFound("id.lessThanOrEqual=" + id);
        defaultVehiclesShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllVehiclesByGuidIsEqualToSomething() throws Exception {
        // Initialize the database
        vehiclesRepository.saveAndFlush(vehicles);

        // Get all the vehiclesList where guid equals to DEFAULT_GUID
        defaultVehiclesShouldBeFound("guid.equals=" + DEFAULT_GUID);

        // Get all the vehiclesList where guid equals to UPDATED_GUID
        defaultVehiclesShouldNotBeFound("guid.equals=" + UPDATED_GUID);
    }

    @Test
    @Transactional
    void getAllVehiclesByGuidIsInShouldWork() throws Exception {
        // Initialize the database
        vehiclesRepository.saveAndFlush(vehicles);

        // Get all the vehiclesList where guid in DEFAULT_GUID or UPDATED_GUID
        defaultVehiclesShouldBeFound("guid.in=" + DEFAULT_GUID + "," + UPDATED_GUID);

        // Get all the vehiclesList where guid equals to UPDATED_GUID
        defaultVehiclesShouldNotBeFound("guid.in=" + UPDATED_GUID);
    }

    @Test
    @Transactional
    void getAllVehiclesByGuidIsNullOrNotNull() throws Exception {
        // Initialize the database
        vehiclesRepository.saveAndFlush(vehicles);

        // Get all the vehiclesList where guid is not null
        defaultVehiclesShouldBeFound("guid.specified=true");

        // Get all the vehiclesList where guid is null
        defaultVehiclesShouldNotBeFound("guid.specified=false");
    }

    @Test
    @Transactional
    void getAllVehiclesByGuidContainsSomething() throws Exception {
        // Initialize the database
        vehiclesRepository.saveAndFlush(vehicles);

        // Get all the vehiclesList where guid contains DEFAULT_GUID
        defaultVehiclesShouldBeFound("guid.contains=" + DEFAULT_GUID);

        // Get all the vehiclesList where guid contains UPDATED_GUID
        defaultVehiclesShouldNotBeFound("guid.contains=" + UPDATED_GUID);
    }

    @Test
    @Transactional
    void getAllVehiclesByGuidNotContainsSomething() throws Exception {
        // Initialize the database
        vehiclesRepository.saveAndFlush(vehicles);

        // Get all the vehiclesList where guid does not contain DEFAULT_GUID
        defaultVehiclesShouldNotBeFound("guid.doesNotContain=" + DEFAULT_GUID);

        // Get all the vehiclesList where guid does not contain UPDATED_GUID
        defaultVehiclesShouldBeFound("guid.doesNotContain=" + UPDATED_GUID);
    }

    @Test
    @Transactional
    void getAllVehiclesByPlateIsEqualToSomething() throws Exception {
        // Initialize the database
        vehiclesRepository.saveAndFlush(vehicles);

        // Get all the vehiclesList where plate equals to DEFAULT_PLATE
        defaultVehiclesShouldBeFound("plate.equals=" + DEFAULT_PLATE);

        // Get all the vehiclesList where plate equals to UPDATED_PLATE
        defaultVehiclesShouldNotBeFound("plate.equals=" + UPDATED_PLATE);
    }

    @Test
    @Transactional
    void getAllVehiclesByPlateIsInShouldWork() throws Exception {
        // Initialize the database
        vehiclesRepository.saveAndFlush(vehicles);

        // Get all the vehiclesList where plate in DEFAULT_PLATE or UPDATED_PLATE
        defaultVehiclesShouldBeFound("plate.in=" + DEFAULT_PLATE + "," + UPDATED_PLATE);

        // Get all the vehiclesList where plate equals to UPDATED_PLATE
        defaultVehiclesShouldNotBeFound("plate.in=" + UPDATED_PLATE);
    }

    @Test
    @Transactional
    void getAllVehiclesByPlateIsNullOrNotNull() throws Exception {
        // Initialize the database
        vehiclesRepository.saveAndFlush(vehicles);

        // Get all the vehiclesList where plate is not null
        defaultVehiclesShouldBeFound("plate.specified=true");

        // Get all the vehiclesList where plate is null
        defaultVehiclesShouldNotBeFound("plate.specified=false");
    }

    @Test
    @Transactional
    void getAllVehiclesByPlateContainsSomething() throws Exception {
        // Initialize the database
        vehiclesRepository.saveAndFlush(vehicles);

        // Get all the vehiclesList where plate contains DEFAULT_PLATE
        defaultVehiclesShouldBeFound("plate.contains=" + DEFAULT_PLATE);

        // Get all the vehiclesList where plate contains UPDATED_PLATE
        defaultVehiclesShouldNotBeFound("plate.contains=" + UPDATED_PLATE);
    }

    @Test
    @Transactional
    void getAllVehiclesByPlateNotContainsSomething() throws Exception {
        // Initialize the database
        vehiclesRepository.saveAndFlush(vehicles);

        // Get all the vehiclesList where plate does not contain DEFAULT_PLATE
        defaultVehiclesShouldNotBeFound("plate.doesNotContain=" + DEFAULT_PLATE);

        // Get all the vehiclesList where plate does not contain UPDATED_PLATE
        defaultVehiclesShouldBeFound("plate.doesNotContain=" + UPDATED_PLATE);
    }

    @Test
    @Transactional
    void getAllVehiclesByAnprIsEqualToSomething() throws Exception {
        // Initialize the database
        vehiclesRepository.saveAndFlush(vehicles);

        // Get all the vehiclesList where anpr equals to DEFAULT_ANPR
        defaultVehiclesShouldBeFound("anpr.equals=" + DEFAULT_ANPR);

        // Get all the vehiclesList where anpr equals to UPDATED_ANPR
        defaultVehiclesShouldNotBeFound("anpr.equals=" + UPDATED_ANPR);
    }

    @Test
    @Transactional
    void getAllVehiclesByAnprIsInShouldWork() throws Exception {
        // Initialize the database
        vehiclesRepository.saveAndFlush(vehicles);

        // Get all the vehiclesList where anpr in DEFAULT_ANPR or UPDATED_ANPR
        defaultVehiclesShouldBeFound("anpr.in=" + DEFAULT_ANPR + "," + UPDATED_ANPR);

        // Get all the vehiclesList where anpr equals to UPDATED_ANPR
        defaultVehiclesShouldNotBeFound("anpr.in=" + UPDATED_ANPR);
    }

    @Test
    @Transactional
    void getAllVehiclesByAnprIsNullOrNotNull() throws Exception {
        // Initialize the database
        vehiclesRepository.saveAndFlush(vehicles);

        // Get all the vehiclesList where anpr is not null
        defaultVehiclesShouldBeFound("anpr.specified=true");

        // Get all the vehiclesList where anpr is null
        defaultVehiclesShouldNotBeFound("anpr.specified=false");
    }

    @Test
    @Transactional
    void getAllVehiclesByAnprContainsSomething() throws Exception {
        // Initialize the database
        vehiclesRepository.saveAndFlush(vehicles);

        // Get all the vehiclesList where anpr contains DEFAULT_ANPR
        defaultVehiclesShouldBeFound("anpr.contains=" + DEFAULT_ANPR);

        // Get all the vehiclesList where anpr contains UPDATED_ANPR
        defaultVehiclesShouldNotBeFound("anpr.contains=" + UPDATED_ANPR);
    }

    @Test
    @Transactional
    void getAllVehiclesByAnprNotContainsSomething() throws Exception {
        // Initialize the database
        vehiclesRepository.saveAndFlush(vehicles);

        // Get all the vehiclesList where anpr does not contain DEFAULT_ANPR
        defaultVehiclesShouldNotBeFound("anpr.doesNotContain=" + DEFAULT_ANPR);

        // Get all the vehiclesList where anpr does not contain UPDATED_ANPR
        defaultVehiclesShouldBeFound("anpr.doesNotContain=" + UPDATED_ANPR);
    }

    @Test
    @Transactional
    void getAllVehiclesByRfidIsEqualToSomething() throws Exception {
        // Initialize the database
        vehiclesRepository.saveAndFlush(vehicles);

        // Get all the vehiclesList where rfid equals to DEFAULT_RFID
        defaultVehiclesShouldBeFound("rfid.equals=" + DEFAULT_RFID);

        // Get all the vehiclesList where rfid equals to UPDATED_RFID
        defaultVehiclesShouldNotBeFound("rfid.equals=" + UPDATED_RFID);
    }

    @Test
    @Transactional
    void getAllVehiclesByRfidIsInShouldWork() throws Exception {
        // Initialize the database
        vehiclesRepository.saveAndFlush(vehicles);

        // Get all the vehiclesList where rfid in DEFAULT_RFID or UPDATED_RFID
        defaultVehiclesShouldBeFound("rfid.in=" + DEFAULT_RFID + "," + UPDATED_RFID);

        // Get all the vehiclesList where rfid equals to UPDATED_RFID
        defaultVehiclesShouldNotBeFound("rfid.in=" + UPDATED_RFID);
    }

    @Test
    @Transactional
    void getAllVehiclesByRfidIsNullOrNotNull() throws Exception {
        // Initialize the database
        vehiclesRepository.saveAndFlush(vehicles);

        // Get all the vehiclesList where rfid is not null
        defaultVehiclesShouldBeFound("rfid.specified=true");

        // Get all the vehiclesList where rfid is null
        defaultVehiclesShouldNotBeFound("rfid.specified=false");
    }

    @Test
    @Transactional
    void getAllVehiclesByRfidContainsSomething() throws Exception {
        // Initialize the database
        vehiclesRepository.saveAndFlush(vehicles);

        // Get all the vehiclesList where rfid contains DEFAULT_RFID
        defaultVehiclesShouldBeFound("rfid.contains=" + DEFAULT_RFID);

        // Get all the vehiclesList where rfid contains UPDATED_RFID
        defaultVehiclesShouldNotBeFound("rfid.contains=" + UPDATED_RFID);
    }

    @Test
    @Transactional
    void getAllVehiclesByRfidNotContainsSomething() throws Exception {
        // Initialize the database
        vehiclesRepository.saveAndFlush(vehicles);

        // Get all the vehiclesList where rfid does not contain DEFAULT_RFID
        defaultVehiclesShouldNotBeFound("rfid.doesNotContain=" + DEFAULT_RFID);

        // Get all the vehiclesList where rfid does not contain UPDATED_RFID
        defaultVehiclesShouldBeFound("rfid.doesNotContain=" + UPDATED_RFID);
    }

    @Test
    @Transactional
    void getAllVehiclesByDataStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        vehiclesRepository.saveAndFlush(vehicles);

        // Get all the vehiclesList where dataStatus equals to DEFAULT_DATA_STATUS
        defaultVehiclesShouldBeFound("dataStatus.equals=" + DEFAULT_DATA_STATUS);

        // Get all the vehiclesList where dataStatus equals to UPDATED_DATA_STATUS
        defaultVehiclesShouldNotBeFound("dataStatus.equals=" + UPDATED_DATA_STATUS);
    }

    @Test
    @Transactional
    void getAllVehiclesByDataStatusIsInShouldWork() throws Exception {
        // Initialize the database
        vehiclesRepository.saveAndFlush(vehicles);

        // Get all the vehiclesList where dataStatus in DEFAULT_DATA_STATUS or UPDATED_DATA_STATUS
        defaultVehiclesShouldBeFound("dataStatus.in=" + DEFAULT_DATA_STATUS + "," + UPDATED_DATA_STATUS);

        // Get all the vehiclesList where dataStatus equals to UPDATED_DATA_STATUS
        defaultVehiclesShouldNotBeFound("dataStatus.in=" + UPDATED_DATA_STATUS);
    }

    @Test
    @Transactional
    void getAllVehiclesByDataStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        vehiclesRepository.saveAndFlush(vehicles);

        // Get all the vehiclesList where dataStatus is not null
        defaultVehiclesShouldBeFound("dataStatus.specified=true");

        // Get all the vehiclesList where dataStatus is null
        defaultVehiclesShouldNotBeFound("dataStatus.specified=false");
    }

    @Test
    @Transactional
    void getAllVehiclesByDataStatusContainsSomething() throws Exception {
        // Initialize the database
        vehiclesRepository.saveAndFlush(vehicles);

        // Get all the vehiclesList where dataStatus contains DEFAULT_DATA_STATUS
        defaultVehiclesShouldBeFound("dataStatus.contains=" + DEFAULT_DATA_STATUS);

        // Get all the vehiclesList where dataStatus contains UPDATED_DATA_STATUS
        defaultVehiclesShouldNotBeFound("dataStatus.contains=" + UPDATED_DATA_STATUS);
    }

    @Test
    @Transactional
    void getAllVehiclesByDataStatusNotContainsSomething() throws Exception {
        // Initialize the database
        vehiclesRepository.saveAndFlush(vehicles);

        // Get all the vehiclesList where dataStatus does not contain DEFAULT_DATA_STATUS
        defaultVehiclesShouldNotBeFound("dataStatus.doesNotContain=" + DEFAULT_DATA_STATUS);

        // Get all the vehiclesList where dataStatus does not contain UPDATED_DATA_STATUS
        defaultVehiclesShouldBeFound("dataStatus.doesNotContain=" + UPDATED_DATA_STATUS);
    }

    @Test
    @Transactional
    void getAllVehiclesByGantryIsEqualToSomething() throws Exception {
        // Initialize the database
        vehiclesRepository.saveAndFlush(vehicles);

        // Get all the vehiclesList where gantry equals to DEFAULT_GANTRY
        defaultVehiclesShouldBeFound("gantry.equals=" + DEFAULT_GANTRY);

        // Get all the vehiclesList where gantry equals to UPDATED_GANTRY
        defaultVehiclesShouldNotBeFound("gantry.equals=" + UPDATED_GANTRY);
    }

    @Test
    @Transactional
    void getAllVehiclesByGantryIsInShouldWork() throws Exception {
        // Initialize the database
        vehiclesRepository.saveAndFlush(vehicles);

        // Get all the vehiclesList where gantry in DEFAULT_GANTRY or UPDATED_GANTRY
        defaultVehiclesShouldBeFound("gantry.in=" + DEFAULT_GANTRY + "," + UPDATED_GANTRY);

        // Get all the vehiclesList where gantry equals to UPDATED_GANTRY
        defaultVehiclesShouldNotBeFound("gantry.in=" + UPDATED_GANTRY);
    }

    @Test
    @Transactional
    void getAllVehiclesByGantryIsNullOrNotNull() throws Exception {
        // Initialize the database
        vehiclesRepository.saveAndFlush(vehicles);

        // Get all the vehiclesList where gantry is not null
        defaultVehiclesShouldBeFound("gantry.specified=true");

        // Get all the vehiclesList where gantry is null
        defaultVehiclesShouldNotBeFound("gantry.specified=false");
    }

    @Test
    @Transactional
    void getAllVehiclesByGantryIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        vehiclesRepository.saveAndFlush(vehicles);

        // Get all the vehiclesList where gantry is greater than or equal to DEFAULT_GANTRY
        defaultVehiclesShouldBeFound("gantry.greaterThanOrEqual=" + DEFAULT_GANTRY);

        // Get all the vehiclesList where gantry is greater than or equal to UPDATED_GANTRY
        defaultVehiclesShouldNotBeFound("gantry.greaterThanOrEqual=" + UPDATED_GANTRY);
    }

    @Test
    @Transactional
    void getAllVehiclesByGantryIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        vehiclesRepository.saveAndFlush(vehicles);

        // Get all the vehiclesList where gantry is less than or equal to DEFAULT_GANTRY
        defaultVehiclesShouldBeFound("gantry.lessThanOrEqual=" + DEFAULT_GANTRY);

        // Get all the vehiclesList where gantry is less than or equal to SMALLER_GANTRY
        defaultVehiclesShouldNotBeFound("gantry.lessThanOrEqual=" + SMALLER_GANTRY);
    }

    @Test
    @Transactional
    void getAllVehiclesByGantryIsLessThanSomething() throws Exception {
        // Initialize the database
        vehiclesRepository.saveAndFlush(vehicles);

        // Get all the vehiclesList where gantry is less than DEFAULT_GANTRY
        defaultVehiclesShouldNotBeFound("gantry.lessThan=" + DEFAULT_GANTRY);

        // Get all the vehiclesList where gantry is less than UPDATED_GANTRY
        defaultVehiclesShouldBeFound("gantry.lessThan=" + UPDATED_GANTRY);
    }

    @Test
    @Transactional
    void getAllVehiclesByGantryIsGreaterThanSomething() throws Exception {
        // Initialize the database
        vehiclesRepository.saveAndFlush(vehicles);

        // Get all the vehiclesList where gantry is greater than DEFAULT_GANTRY
        defaultVehiclesShouldNotBeFound("gantry.greaterThan=" + DEFAULT_GANTRY);

        // Get all the vehiclesList where gantry is greater than SMALLER_GANTRY
        defaultVehiclesShouldBeFound("gantry.greaterThan=" + SMALLER_GANTRY);
    }

    @Test
    @Transactional
    void getAllVehiclesByLaneIsEqualToSomething() throws Exception {
        // Initialize the database
        vehiclesRepository.saveAndFlush(vehicles);

        // Get all the vehiclesList where lane equals to DEFAULT_LANE
        defaultVehiclesShouldBeFound("lane.equals=" + DEFAULT_LANE);

        // Get all the vehiclesList where lane equals to UPDATED_LANE
        defaultVehiclesShouldNotBeFound("lane.equals=" + UPDATED_LANE);
    }

    @Test
    @Transactional
    void getAllVehiclesByLaneIsInShouldWork() throws Exception {
        // Initialize the database
        vehiclesRepository.saveAndFlush(vehicles);

        // Get all the vehiclesList where lane in DEFAULT_LANE or UPDATED_LANE
        defaultVehiclesShouldBeFound("lane.in=" + DEFAULT_LANE + "," + UPDATED_LANE);

        // Get all the vehiclesList where lane equals to UPDATED_LANE
        defaultVehiclesShouldNotBeFound("lane.in=" + UPDATED_LANE);
    }

    @Test
    @Transactional
    void getAllVehiclesByLaneIsNullOrNotNull() throws Exception {
        // Initialize the database
        vehiclesRepository.saveAndFlush(vehicles);

        // Get all the vehiclesList where lane is not null
        defaultVehiclesShouldBeFound("lane.specified=true");

        // Get all the vehiclesList where lane is null
        defaultVehiclesShouldNotBeFound("lane.specified=false");
    }

    @Test
    @Transactional
    void getAllVehiclesByLaneIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        vehiclesRepository.saveAndFlush(vehicles);

        // Get all the vehiclesList where lane is greater than or equal to DEFAULT_LANE
        defaultVehiclesShouldBeFound("lane.greaterThanOrEqual=" + DEFAULT_LANE);

        // Get all the vehiclesList where lane is greater than or equal to UPDATED_LANE
        defaultVehiclesShouldNotBeFound("lane.greaterThanOrEqual=" + UPDATED_LANE);
    }

    @Test
    @Transactional
    void getAllVehiclesByLaneIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        vehiclesRepository.saveAndFlush(vehicles);

        // Get all the vehiclesList where lane is less than or equal to DEFAULT_LANE
        defaultVehiclesShouldBeFound("lane.lessThanOrEqual=" + DEFAULT_LANE);

        // Get all the vehiclesList where lane is less than or equal to SMALLER_LANE
        defaultVehiclesShouldNotBeFound("lane.lessThanOrEqual=" + SMALLER_LANE);
    }

    @Test
    @Transactional
    void getAllVehiclesByLaneIsLessThanSomething() throws Exception {
        // Initialize the database
        vehiclesRepository.saveAndFlush(vehicles);

        // Get all the vehiclesList where lane is less than DEFAULT_LANE
        defaultVehiclesShouldNotBeFound("lane.lessThan=" + DEFAULT_LANE);

        // Get all the vehiclesList where lane is less than UPDATED_LANE
        defaultVehiclesShouldBeFound("lane.lessThan=" + UPDATED_LANE);
    }

    @Test
    @Transactional
    void getAllVehiclesByLaneIsGreaterThanSomething() throws Exception {
        // Initialize the database
        vehiclesRepository.saveAndFlush(vehicles);

        // Get all the vehiclesList where lane is greater than DEFAULT_LANE
        defaultVehiclesShouldNotBeFound("lane.greaterThan=" + DEFAULT_LANE);

        // Get all the vehiclesList where lane is greater than SMALLER_LANE
        defaultVehiclesShouldBeFound("lane.greaterThan=" + SMALLER_LANE);
    }

    @Test
    @Transactional
    void getAllVehiclesByKphIsEqualToSomething() throws Exception {
        // Initialize the database
        vehiclesRepository.saveAndFlush(vehicles);

        // Get all the vehiclesList where kph equals to DEFAULT_KPH
        defaultVehiclesShouldBeFound("kph.equals=" + DEFAULT_KPH);

        // Get all the vehiclesList where kph equals to UPDATED_KPH
        defaultVehiclesShouldNotBeFound("kph.equals=" + UPDATED_KPH);
    }

    @Test
    @Transactional
    void getAllVehiclesByKphIsInShouldWork() throws Exception {
        // Initialize the database
        vehiclesRepository.saveAndFlush(vehicles);

        // Get all the vehiclesList where kph in DEFAULT_KPH or UPDATED_KPH
        defaultVehiclesShouldBeFound("kph.in=" + DEFAULT_KPH + "," + UPDATED_KPH);

        // Get all the vehiclesList where kph equals to UPDATED_KPH
        defaultVehiclesShouldNotBeFound("kph.in=" + UPDATED_KPH);
    }

    @Test
    @Transactional
    void getAllVehiclesByKphIsNullOrNotNull() throws Exception {
        // Initialize the database
        vehiclesRepository.saveAndFlush(vehicles);

        // Get all the vehiclesList where kph is not null
        defaultVehiclesShouldBeFound("kph.specified=true");

        // Get all the vehiclesList where kph is null
        defaultVehiclesShouldNotBeFound("kph.specified=false");
    }

    @Test
    @Transactional
    void getAllVehiclesByKphIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        vehiclesRepository.saveAndFlush(vehicles);

        // Get all the vehiclesList where kph is greater than or equal to DEFAULT_KPH
        defaultVehiclesShouldBeFound("kph.greaterThanOrEqual=" + DEFAULT_KPH);

        // Get all the vehiclesList where kph is greater than or equal to UPDATED_KPH
        defaultVehiclesShouldNotBeFound("kph.greaterThanOrEqual=" + UPDATED_KPH);
    }

    @Test
    @Transactional
    void getAllVehiclesByKphIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        vehiclesRepository.saveAndFlush(vehicles);

        // Get all the vehiclesList where kph is less than or equal to DEFAULT_KPH
        defaultVehiclesShouldBeFound("kph.lessThanOrEqual=" + DEFAULT_KPH);

        // Get all the vehiclesList where kph is less than or equal to SMALLER_KPH
        defaultVehiclesShouldNotBeFound("kph.lessThanOrEqual=" + SMALLER_KPH);
    }

    @Test
    @Transactional
    void getAllVehiclesByKphIsLessThanSomething() throws Exception {
        // Initialize the database
        vehiclesRepository.saveAndFlush(vehicles);

        // Get all the vehiclesList where kph is less than DEFAULT_KPH
        defaultVehiclesShouldNotBeFound("kph.lessThan=" + DEFAULT_KPH);

        // Get all the vehiclesList where kph is less than UPDATED_KPH
        defaultVehiclesShouldBeFound("kph.lessThan=" + UPDATED_KPH);
    }

    @Test
    @Transactional
    void getAllVehiclesByKphIsGreaterThanSomething() throws Exception {
        // Initialize the database
        vehiclesRepository.saveAndFlush(vehicles);

        // Get all the vehiclesList where kph is greater than DEFAULT_KPH
        defaultVehiclesShouldNotBeFound("kph.greaterThan=" + DEFAULT_KPH);

        // Get all the vehiclesList where kph is greater than SMALLER_KPH
        defaultVehiclesShouldBeFound("kph.greaterThan=" + SMALLER_KPH);
    }

    @Test
    @Transactional
    void getAllVehiclesByAmbushIsEqualToSomething() throws Exception {
        // Initialize the database
        vehiclesRepository.saveAndFlush(vehicles);

        // Get all the vehiclesList where ambush equals to DEFAULT_AMBUSH
        defaultVehiclesShouldBeFound("ambush.equals=" + DEFAULT_AMBUSH);

        // Get all the vehiclesList where ambush equals to UPDATED_AMBUSH
        defaultVehiclesShouldNotBeFound("ambush.equals=" + UPDATED_AMBUSH);
    }

    @Test
    @Transactional
    void getAllVehiclesByAmbushIsInShouldWork() throws Exception {
        // Initialize the database
        vehiclesRepository.saveAndFlush(vehicles);

        // Get all the vehiclesList where ambush in DEFAULT_AMBUSH or UPDATED_AMBUSH
        defaultVehiclesShouldBeFound("ambush.in=" + DEFAULT_AMBUSH + "," + UPDATED_AMBUSH);

        // Get all the vehiclesList where ambush equals to UPDATED_AMBUSH
        defaultVehiclesShouldNotBeFound("ambush.in=" + UPDATED_AMBUSH);
    }

    @Test
    @Transactional
    void getAllVehiclesByAmbushIsNullOrNotNull() throws Exception {
        // Initialize the database
        vehiclesRepository.saveAndFlush(vehicles);

        // Get all the vehiclesList where ambush is not null
        defaultVehiclesShouldBeFound("ambush.specified=true");

        // Get all the vehiclesList where ambush is null
        defaultVehiclesShouldNotBeFound("ambush.specified=false");
    }

    @Test
    @Transactional
    void getAllVehiclesByAmbushIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        vehiclesRepository.saveAndFlush(vehicles);

        // Get all the vehiclesList where ambush is greater than or equal to DEFAULT_AMBUSH
        defaultVehiclesShouldBeFound("ambush.greaterThanOrEqual=" + DEFAULT_AMBUSH);

        // Get all the vehiclesList where ambush is greater than or equal to UPDATED_AMBUSH
        defaultVehiclesShouldNotBeFound("ambush.greaterThanOrEqual=" + UPDATED_AMBUSH);
    }

    @Test
    @Transactional
    void getAllVehiclesByAmbushIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        vehiclesRepository.saveAndFlush(vehicles);

        // Get all the vehiclesList where ambush is less than or equal to DEFAULT_AMBUSH
        defaultVehiclesShouldBeFound("ambush.lessThanOrEqual=" + DEFAULT_AMBUSH);

        // Get all the vehiclesList where ambush is less than or equal to SMALLER_AMBUSH
        defaultVehiclesShouldNotBeFound("ambush.lessThanOrEqual=" + SMALLER_AMBUSH);
    }

    @Test
    @Transactional
    void getAllVehiclesByAmbushIsLessThanSomething() throws Exception {
        // Initialize the database
        vehiclesRepository.saveAndFlush(vehicles);

        // Get all the vehiclesList where ambush is less than DEFAULT_AMBUSH
        defaultVehiclesShouldNotBeFound("ambush.lessThan=" + DEFAULT_AMBUSH);

        // Get all the vehiclesList where ambush is less than UPDATED_AMBUSH
        defaultVehiclesShouldBeFound("ambush.lessThan=" + UPDATED_AMBUSH);
    }

    @Test
    @Transactional
    void getAllVehiclesByAmbushIsGreaterThanSomething() throws Exception {
        // Initialize the database
        vehiclesRepository.saveAndFlush(vehicles);

        // Get all the vehiclesList where ambush is greater than DEFAULT_AMBUSH
        defaultVehiclesShouldNotBeFound("ambush.greaterThan=" + DEFAULT_AMBUSH);

        // Get all the vehiclesList where ambush is greater than SMALLER_AMBUSH
        defaultVehiclesShouldBeFound("ambush.greaterThan=" + SMALLER_AMBUSH);
    }

    @Test
    @Transactional
    void getAllVehiclesByDirectionIsEqualToSomething() throws Exception {
        // Initialize the database
        vehiclesRepository.saveAndFlush(vehicles);

        // Get all the vehiclesList where direction equals to DEFAULT_DIRECTION
        defaultVehiclesShouldBeFound("direction.equals=" + DEFAULT_DIRECTION);

        // Get all the vehiclesList where direction equals to UPDATED_DIRECTION
        defaultVehiclesShouldNotBeFound("direction.equals=" + UPDATED_DIRECTION);
    }

    @Test
    @Transactional
    void getAllVehiclesByDirectionIsInShouldWork() throws Exception {
        // Initialize the database
        vehiclesRepository.saveAndFlush(vehicles);

        // Get all the vehiclesList where direction in DEFAULT_DIRECTION or UPDATED_DIRECTION
        defaultVehiclesShouldBeFound("direction.in=" + DEFAULT_DIRECTION + "," + UPDATED_DIRECTION);

        // Get all the vehiclesList where direction equals to UPDATED_DIRECTION
        defaultVehiclesShouldNotBeFound("direction.in=" + UPDATED_DIRECTION);
    }

    @Test
    @Transactional
    void getAllVehiclesByDirectionIsNullOrNotNull() throws Exception {
        // Initialize the database
        vehiclesRepository.saveAndFlush(vehicles);

        // Get all the vehiclesList where direction is not null
        defaultVehiclesShouldBeFound("direction.specified=true");

        // Get all the vehiclesList where direction is null
        defaultVehiclesShouldNotBeFound("direction.specified=false");
    }

    @Test
    @Transactional
    void getAllVehiclesByDirectionIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        vehiclesRepository.saveAndFlush(vehicles);

        // Get all the vehiclesList where direction is greater than or equal to DEFAULT_DIRECTION
        defaultVehiclesShouldBeFound("direction.greaterThanOrEqual=" + DEFAULT_DIRECTION);

        // Get all the vehiclesList where direction is greater than or equal to UPDATED_DIRECTION
        defaultVehiclesShouldNotBeFound("direction.greaterThanOrEqual=" + UPDATED_DIRECTION);
    }

    @Test
    @Transactional
    void getAllVehiclesByDirectionIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        vehiclesRepository.saveAndFlush(vehicles);

        // Get all the vehiclesList where direction is less than or equal to DEFAULT_DIRECTION
        defaultVehiclesShouldBeFound("direction.lessThanOrEqual=" + DEFAULT_DIRECTION);

        // Get all the vehiclesList where direction is less than or equal to SMALLER_DIRECTION
        defaultVehiclesShouldNotBeFound("direction.lessThanOrEqual=" + SMALLER_DIRECTION);
    }

    @Test
    @Transactional
    void getAllVehiclesByDirectionIsLessThanSomething() throws Exception {
        // Initialize the database
        vehiclesRepository.saveAndFlush(vehicles);

        // Get all the vehiclesList where direction is less than DEFAULT_DIRECTION
        defaultVehiclesShouldNotBeFound("direction.lessThan=" + DEFAULT_DIRECTION);

        // Get all the vehiclesList where direction is less than UPDATED_DIRECTION
        defaultVehiclesShouldBeFound("direction.lessThan=" + UPDATED_DIRECTION);
    }

    @Test
    @Transactional
    void getAllVehiclesByDirectionIsGreaterThanSomething() throws Exception {
        // Initialize the database
        vehiclesRepository.saveAndFlush(vehicles);

        // Get all the vehiclesList where direction is greater than DEFAULT_DIRECTION
        defaultVehiclesShouldNotBeFound("direction.greaterThan=" + DEFAULT_DIRECTION);

        // Get all the vehiclesList where direction is greater than SMALLER_DIRECTION
        defaultVehiclesShouldBeFound("direction.greaterThan=" + SMALLER_DIRECTION);
    }

    @Test
    @Transactional
    void getAllVehiclesByVehicleIsEqualToSomething() throws Exception {
        // Initialize the database
        vehiclesRepository.saveAndFlush(vehicles);

        // Get all the vehiclesList where vehicle equals to DEFAULT_VEHICLE
        defaultVehiclesShouldBeFound("vehicle.equals=" + DEFAULT_VEHICLE);

        // Get all the vehiclesList where vehicle equals to UPDATED_VEHICLE
        defaultVehiclesShouldNotBeFound("vehicle.equals=" + UPDATED_VEHICLE);
    }

    @Test
    @Transactional
    void getAllVehiclesByVehicleIsInShouldWork() throws Exception {
        // Initialize the database
        vehiclesRepository.saveAndFlush(vehicles);

        // Get all the vehiclesList where vehicle in DEFAULT_VEHICLE or UPDATED_VEHICLE
        defaultVehiclesShouldBeFound("vehicle.in=" + DEFAULT_VEHICLE + "," + UPDATED_VEHICLE);

        // Get all the vehiclesList where vehicle equals to UPDATED_VEHICLE
        defaultVehiclesShouldNotBeFound("vehicle.in=" + UPDATED_VEHICLE);
    }

    @Test
    @Transactional
    void getAllVehiclesByVehicleIsNullOrNotNull() throws Exception {
        // Initialize the database
        vehiclesRepository.saveAndFlush(vehicles);

        // Get all the vehiclesList where vehicle is not null
        defaultVehiclesShouldBeFound("vehicle.specified=true");

        // Get all the vehiclesList where vehicle is null
        defaultVehiclesShouldNotBeFound("vehicle.specified=false");
    }

    @Test
    @Transactional
    void getAllVehiclesByVehicleIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        vehiclesRepository.saveAndFlush(vehicles);

        // Get all the vehiclesList where vehicle is greater than or equal to DEFAULT_VEHICLE
        defaultVehiclesShouldBeFound("vehicle.greaterThanOrEqual=" + DEFAULT_VEHICLE);

        // Get all the vehiclesList where vehicle is greater than or equal to UPDATED_VEHICLE
        defaultVehiclesShouldNotBeFound("vehicle.greaterThanOrEqual=" + UPDATED_VEHICLE);
    }

    @Test
    @Transactional
    void getAllVehiclesByVehicleIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        vehiclesRepository.saveAndFlush(vehicles);

        // Get all the vehiclesList where vehicle is less than or equal to DEFAULT_VEHICLE
        defaultVehiclesShouldBeFound("vehicle.lessThanOrEqual=" + DEFAULT_VEHICLE);

        // Get all the vehiclesList where vehicle is less than or equal to SMALLER_VEHICLE
        defaultVehiclesShouldNotBeFound("vehicle.lessThanOrEqual=" + SMALLER_VEHICLE);
    }

    @Test
    @Transactional
    void getAllVehiclesByVehicleIsLessThanSomething() throws Exception {
        // Initialize the database
        vehiclesRepository.saveAndFlush(vehicles);

        // Get all the vehiclesList where vehicle is less than DEFAULT_VEHICLE
        defaultVehiclesShouldNotBeFound("vehicle.lessThan=" + DEFAULT_VEHICLE);

        // Get all the vehiclesList where vehicle is less than UPDATED_VEHICLE
        defaultVehiclesShouldBeFound("vehicle.lessThan=" + UPDATED_VEHICLE);
    }

    @Test
    @Transactional
    void getAllVehiclesByVehicleIsGreaterThanSomething() throws Exception {
        // Initialize the database
        vehiclesRepository.saveAndFlush(vehicles);

        // Get all the vehiclesList where vehicle is greater than DEFAULT_VEHICLE
        defaultVehiclesShouldNotBeFound("vehicle.greaterThan=" + DEFAULT_VEHICLE);

        // Get all the vehiclesList where vehicle is greater than SMALLER_VEHICLE
        defaultVehiclesShouldBeFound("vehicle.greaterThan=" + SMALLER_VEHICLE);
    }

    @Test
    @Transactional
    void getAllVehiclesByStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        vehiclesRepository.saveAndFlush(vehicles);

        // Get all the vehiclesList where status equals to DEFAULT_STATUS
        defaultVehiclesShouldBeFound("status.equals=" + DEFAULT_STATUS);

        // Get all the vehiclesList where status equals to UPDATED_STATUS
        defaultVehiclesShouldNotBeFound("status.equals=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllVehiclesByStatusIsInShouldWork() throws Exception {
        // Initialize the database
        vehiclesRepository.saveAndFlush(vehicles);

        // Get all the vehiclesList where status in DEFAULT_STATUS or UPDATED_STATUS
        defaultVehiclesShouldBeFound("status.in=" + DEFAULT_STATUS + "," + UPDATED_STATUS);

        // Get all the vehiclesList where status equals to UPDATED_STATUS
        defaultVehiclesShouldNotBeFound("status.in=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllVehiclesByStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        vehiclesRepository.saveAndFlush(vehicles);

        // Get all the vehiclesList where status is not null
        defaultVehiclesShouldBeFound("status.specified=true");

        // Get all the vehiclesList where status is null
        defaultVehiclesShouldNotBeFound("status.specified=false");
    }

    @Test
    @Transactional
    void getAllVehiclesByStatusContainsSomething() throws Exception {
        // Initialize the database
        vehiclesRepository.saveAndFlush(vehicles);

        // Get all the vehiclesList where status contains DEFAULT_STATUS
        defaultVehiclesShouldBeFound("status.contains=" + DEFAULT_STATUS);

        // Get all the vehiclesList where status contains UPDATED_STATUS
        defaultVehiclesShouldNotBeFound("status.contains=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllVehiclesByStatusNotContainsSomething() throws Exception {
        // Initialize the database
        vehiclesRepository.saveAndFlush(vehicles);

        // Get all the vehiclesList where status does not contain DEFAULT_STATUS
        defaultVehiclesShouldNotBeFound("status.doesNotContain=" + DEFAULT_STATUS);

        // Get all the vehiclesList where status does not contain UPDATED_STATUS
        defaultVehiclesShouldBeFound("status.doesNotContain=" + UPDATED_STATUS);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultVehiclesShouldBeFound(String filter) throws Exception {
        restVehiclesMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(vehicles.getId().intValue())))
            .andExpect(jsonPath("$.[*].guid").value(hasItem(DEFAULT_GUID)))
            .andExpect(jsonPath("$.[*].plate").value(hasItem(DEFAULT_PLATE)))
            .andExpect(jsonPath("$.[*].anpr").value(hasItem(DEFAULT_ANPR)))
            .andExpect(jsonPath("$.[*].rfid").value(hasItem(DEFAULT_RFID)))
            .andExpect(jsonPath("$.[*].dataStatus").value(hasItem(DEFAULT_DATA_STATUS)))
            .andExpect(jsonPath("$.[*].gantry").value(hasItem(DEFAULT_GANTRY.intValue())))
            .andExpect(jsonPath("$.[*].lane").value(hasItem(DEFAULT_LANE.intValue())))
            .andExpect(jsonPath("$.[*].kph").value(hasItem(DEFAULT_KPH.intValue())))
            .andExpect(jsonPath("$.[*].ambush").value(hasItem(DEFAULT_AMBUSH.intValue())))
            .andExpect(jsonPath("$.[*].direction").value(hasItem(DEFAULT_DIRECTION.intValue())))
            .andExpect(jsonPath("$.[*].vehicle").value(hasItem(DEFAULT_VEHICLE.intValue())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS)));

        // Check, that the count call also returns 1
        restVehiclesMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultVehiclesShouldNotBeFound(String filter) throws Exception {
        restVehiclesMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restVehiclesMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingVehicles() throws Exception {
        // Get the vehicles
        restVehiclesMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingVehicles() throws Exception {
        // Initialize the database
        vehiclesRepository.saveAndFlush(vehicles);

        int databaseSizeBeforeUpdate = vehiclesRepository.findAll().size();
        vehiclesSearchRepository.save(vehicles);
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(vehiclesSearchRepository.findAll());

        // Update the vehicles
        Vehicles updatedVehicles = vehiclesRepository.findById(vehicles.getId()).get();
        // Disconnect from session so that the updates on updatedVehicles are not directly saved in db
        em.detach(updatedVehicles);
        updatedVehicles
            .guid(UPDATED_GUID)
            .plate(UPDATED_PLATE)
            .anpr(UPDATED_ANPR)
            .rfid(UPDATED_RFID)
            .dataStatus(UPDATED_DATA_STATUS)
            .gantry(UPDATED_GANTRY)
            .lane(UPDATED_LANE)
            .kph(UPDATED_KPH)
            .ambush(UPDATED_AMBUSH)
            .direction(UPDATED_DIRECTION)
            .vehicle(UPDATED_VEHICLE)
            .status(UPDATED_STATUS);
        VehiclesDTO vehiclesDTO = vehiclesMapper.toDto(updatedVehicles);

        restVehiclesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, vehiclesDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(vehiclesDTO))
            )
            .andExpect(status().isOk());

        // Validate the Vehicles in the database
        List<Vehicles> vehiclesList = vehiclesRepository.findAll();
        assertThat(vehiclesList).hasSize(databaseSizeBeforeUpdate);
        Vehicles testVehicles = vehiclesList.get(vehiclesList.size() - 1);
        assertThat(testVehicles.getGuid()).isEqualTo(UPDATED_GUID);
        assertThat(testVehicles.getPlate()).isEqualTo(UPDATED_PLATE);
        assertThat(testVehicles.getAnpr()).isEqualTo(UPDATED_ANPR);
        assertThat(testVehicles.getRfid()).isEqualTo(UPDATED_RFID);
        assertThat(testVehicles.getDataStatus()).isEqualTo(UPDATED_DATA_STATUS);
        assertThat(testVehicles.getGantry()).isEqualTo(UPDATED_GANTRY);
        assertThat(testVehicles.getLane()).isEqualTo(UPDATED_LANE);
        assertThat(testVehicles.getKph()).isEqualTo(UPDATED_KPH);
        assertThat(testVehicles.getAmbush()).isEqualTo(UPDATED_AMBUSH);
        assertThat(testVehicles.getDirection()).isEqualTo(UPDATED_DIRECTION);
        assertThat(testVehicles.getVehicle()).isEqualTo(UPDATED_VEHICLE);
        assertThat(testVehicles.getStatus()).isEqualTo(UPDATED_STATUS);
        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(vehiclesSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
                List<Vehicles> vehiclesSearchList = IterableUtils.toList(vehiclesSearchRepository.findAll());
                Vehicles testVehiclesSearch = vehiclesSearchList.get(searchDatabaseSizeAfter - 1);
                assertThat(testVehiclesSearch.getGuid()).isEqualTo(UPDATED_GUID);
                assertThat(testVehiclesSearch.getPlate()).isEqualTo(UPDATED_PLATE);
                assertThat(testVehiclesSearch.getAnpr()).isEqualTo(UPDATED_ANPR);
                assertThat(testVehiclesSearch.getRfid()).isEqualTo(UPDATED_RFID);
                assertThat(testVehiclesSearch.getDataStatus()).isEqualTo(UPDATED_DATA_STATUS);
                assertThat(testVehiclesSearch.getGantry()).isEqualTo(UPDATED_GANTRY);
                assertThat(testVehiclesSearch.getLane()).isEqualTo(UPDATED_LANE);
                assertThat(testVehiclesSearch.getKph()).isEqualTo(UPDATED_KPH);
                assertThat(testVehiclesSearch.getAmbush()).isEqualTo(UPDATED_AMBUSH);
                assertThat(testVehiclesSearch.getDirection()).isEqualTo(UPDATED_DIRECTION);
                assertThat(testVehiclesSearch.getVehicle()).isEqualTo(UPDATED_VEHICLE);
                assertThat(testVehiclesSearch.getStatus()).isEqualTo(UPDATED_STATUS);
            });
    }

    @Test
    @Transactional
    void putNonExistingVehicles() throws Exception {
        int databaseSizeBeforeUpdate = vehiclesRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(vehiclesSearchRepository.findAll());
        vehicles.setId(count.incrementAndGet());

        // Create the Vehicles
        VehiclesDTO vehiclesDTO = vehiclesMapper.toDto(vehicles);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restVehiclesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, vehiclesDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(vehiclesDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Vehicles in the database
        List<Vehicles> vehiclesList = vehiclesRepository.findAll();
        assertThat(vehiclesList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(vehiclesSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithIdMismatchVehicles() throws Exception {
        int databaseSizeBeforeUpdate = vehiclesRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(vehiclesSearchRepository.findAll());
        vehicles.setId(count.incrementAndGet());

        // Create the Vehicles
        VehiclesDTO vehiclesDTO = vehiclesMapper.toDto(vehicles);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVehiclesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(vehiclesDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Vehicles in the database
        List<Vehicles> vehiclesList = vehiclesRepository.findAll();
        assertThat(vehiclesList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(vehiclesSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamVehicles() throws Exception {
        int databaseSizeBeforeUpdate = vehiclesRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(vehiclesSearchRepository.findAll());
        vehicles.setId(count.incrementAndGet());

        // Create the Vehicles
        VehiclesDTO vehiclesDTO = vehiclesMapper.toDto(vehicles);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVehiclesMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(vehiclesDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Vehicles in the database
        List<Vehicles> vehiclesList = vehiclesRepository.findAll();
        assertThat(vehiclesList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(vehiclesSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void partialUpdateVehiclesWithPatch() throws Exception {
        // Initialize the database
        vehiclesRepository.saveAndFlush(vehicles);

        int databaseSizeBeforeUpdate = vehiclesRepository.findAll().size();

        // Update the vehicles using partial update
        Vehicles partialUpdatedVehicles = new Vehicles();
        partialUpdatedVehicles.setId(vehicles.getId());

        partialUpdatedVehicles.plate(UPDATED_PLATE).anpr(UPDATED_ANPR).kph(UPDATED_KPH).direction(UPDATED_DIRECTION);

        restVehiclesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedVehicles.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedVehicles))
            )
            .andExpect(status().isOk());

        // Validate the Vehicles in the database
        List<Vehicles> vehiclesList = vehiclesRepository.findAll();
        assertThat(vehiclesList).hasSize(databaseSizeBeforeUpdate);
        Vehicles testVehicles = vehiclesList.get(vehiclesList.size() - 1);
        assertThat(testVehicles.getGuid()).isEqualTo(DEFAULT_GUID);
        assertThat(testVehicles.getPlate()).isEqualTo(UPDATED_PLATE);
        assertThat(testVehicles.getAnpr()).isEqualTo(UPDATED_ANPR);
        assertThat(testVehicles.getRfid()).isEqualTo(DEFAULT_RFID);
        assertThat(testVehicles.getDataStatus()).isEqualTo(DEFAULT_DATA_STATUS);
        assertThat(testVehicles.getGantry()).isEqualTo(DEFAULT_GANTRY);
        assertThat(testVehicles.getLane()).isEqualTo(DEFAULT_LANE);
        assertThat(testVehicles.getKph()).isEqualTo(UPDATED_KPH);
        assertThat(testVehicles.getAmbush()).isEqualTo(DEFAULT_AMBUSH);
        assertThat(testVehicles.getDirection()).isEqualTo(UPDATED_DIRECTION);
        assertThat(testVehicles.getVehicle()).isEqualTo(DEFAULT_VEHICLE);
        assertThat(testVehicles.getStatus()).isEqualTo(DEFAULT_STATUS);
    }

    @Test
    @Transactional
    void fullUpdateVehiclesWithPatch() throws Exception {
        // Initialize the database
        vehiclesRepository.saveAndFlush(vehicles);

        int databaseSizeBeforeUpdate = vehiclesRepository.findAll().size();

        // Update the vehicles using partial update
        Vehicles partialUpdatedVehicles = new Vehicles();
        partialUpdatedVehicles.setId(vehicles.getId());

        partialUpdatedVehicles
            .guid(UPDATED_GUID)
            .plate(UPDATED_PLATE)
            .anpr(UPDATED_ANPR)
            .rfid(UPDATED_RFID)
            .dataStatus(UPDATED_DATA_STATUS)
            .gantry(UPDATED_GANTRY)
            .lane(UPDATED_LANE)
            .kph(UPDATED_KPH)
            .ambush(UPDATED_AMBUSH)
            .direction(UPDATED_DIRECTION)
            .vehicle(UPDATED_VEHICLE)
            .status(UPDATED_STATUS);

        restVehiclesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedVehicles.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedVehicles))
            )
            .andExpect(status().isOk());

        // Validate the Vehicles in the database
        List<Vehicles> vehiclesList = vehiclesRepository.findAll();
        assertThat(vehiclesList).hasSize(databaseSizeBeforeUpdate);
        Vehicles testVehicles = vehiclesList.get(vehiclesList.size() - 1);
        assertThat(testVehicles.getGuid()).isEqualTo(UPDATED_GUID);
        assertThat(testVehicles.getPlate()).isEqualTo(UPDATED_PLATE);
        assertThat(testVehicles.getAnpr()).isEqualTo(UPDATED_ANPR);
        assertThat(testVehicles.getRfid()).isEqualTo(UPDATED_RFID);
        assertThat(testVehicles.getDataStatus()).isEqualTo(UPDATED_DATA_STATUS);
        assertThat(testVehicles.getGantry()).isEqualTo(UPDATED_GANTRY);
        assertThat(testVehicles.getLane()).isEqualTo(UPDATED_LANE);
        assertThat(testVehicles.getKph()).isEqualTo(UPDATED_KPH);
        assertThat(testVehicles.getAmbush()).isEqualTo(UPDATED_AMBUSH);
        assertThat(testVehicles.getDirection()).isEqualTo(UPDATED_DIRECTION);
        assertThat(testVehicles.getVehicle()).isEqualTo(UPDATED_VEHICLE);
        assertThat(testVehicles.getStatus()).isEqualTo(UPDATED_STATUS);
    }

    @Test
    @Transactional
    void patchNonExistingVehicles() throws Exception {
        int databaseSizeBeforeUpdate = vehiclesRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(vehiclesSearchRepository.findAll());
        vehicles.setId(count.incrementAndGet());

        // Create the Vehicles
        VehiclesDTO vehiclesDTO = vehiclesMapper.toDto(vehicles);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restVehiclesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, vehiclesDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(vehiclesDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Vehicles in the database
        List<Vehicles> vehiclesList = vehiclesRepository.findAll();
        assertThat(vehiclesList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(vehiclesSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithIdMismatchVehicles() throws Exception {
        int databaseSizeBeforeUpdate = vehiclesRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(vehiclesSearchRepository.findAll());
        vehicles.setId(count.incrementAndGet());

        // Create the Vehicles
        VehiclesDTO vehiclesDTO = vehiclesMapper.toDto(vehicles);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVehiclesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(vehiclesDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Vehicles in the database
        List<Vehicles> vehiclesList = vehiclesRepository.findAll();
        assertThat(vehiclesList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(vehiclesSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamVehicles() throws Exception {
        int databaseSizeBeforeUpdate = vehiclesRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(vehiclesSearchRepository.findAll());
        vehicles.setId(count.incrementAndGet());

        // Create the Vehicles
        VehiclesDTO vehiclesDTO = vehiclesMapper.toDto(vehicles);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVehiclesMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(vehiclesDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Vehicles in the database
        List<Vehicles> vehiclesList = vehiclesRepository.findAll();
        assertThat(vehiclesList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(vehiclesSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void deleteVehicles() throws Exception {
        // Initialize the database
        vehiclesRepository.saveAndFlush(vehicles);
        vehiclesRepository.save(vehicles);
        vehiclesSearchRepository.save(vehicles);

        int databaseSizeBeforeDelete = vehiclesRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(vehiclesSearchRepository.findAll());
        assertThat(searchDatabaseSizeBefore).isEqualTo(databaseSizeBeforeDelete);

        // Delete the vehicles
        restVehiclesMockMvc
            .perform(delete(ENTITY_API_URL_ID, vehicles.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Vehicles> vehiclesList = vehiclesRepository.findAll();
        assertThat(vehiclesList).hasSize(databaseSizeBeforeDelete - 1);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(vehiclesSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore - 1);
    }

    @Test
    @Transactional
    void searchVehicles() throws Exception {
        // Initialize the database
        vehicles = vehiclesRepository.saveAndFlush(vehicles);
        vehiclesSearchRepository.save(vehicles);

        // Search the vehicles
        restVehiclesMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + vehicles.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(vehicles.getId().intValue())))
            .andExpect(jsonPath("$.[*].guid").value(hasItem(DEFAULT_GUID)))
            .andExpect(jsonPath("$.[*].plate").value(hasItem(DEFAULT_PLATE)))
            .andExpect(jsonPath("$.[*].anpr").value(hasItem(DEFAULT_ANPR)))
            .andExpect(jsonPath("$.[*].rfid").value(hasItem(DEFAULT_RFID)))
            .andExpect(jsonPath("$.[*].dataStatus").value(hasItem(DEFAULT_DATA_STATUS)))
            .andExpect(jsonPath("$.[*].gantry").value(hasItem(DEFAULT_GANTRY.intValue())))
            .andExpect(jsonPath("$.[*].lane").value(hasItem(DEFAULT_LANE.intValue())))
            .andExpect(jsonPath("$.[*].kph").value(hasItem(DEFAULT_KPH.intValue())))
            .andExpect(jsonPath("$.[*].ambush").value(hasItem(DEFAULT_AMBUSH.intValue())))
            .andExpect(jsonPath("$.[*].direction").value(hasItem(DEFAULT_DIRECTION.intValue())))
            .andExpect(jsonPath("$.[*].vehicle").value(hasItem(DEFAULT_VEHICLE.intValue())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS)));
    }
}
