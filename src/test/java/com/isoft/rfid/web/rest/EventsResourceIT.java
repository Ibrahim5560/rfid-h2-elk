package com.isoft.rfid.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.isoft.rfid.IntegrationTest;
import com.isoft.rfid.domain.Events;
import com.isoft.rfid.repository.EventsRepository;
import com.isoft.rfid.repository.search.EventsSearchRepository;
import com.isoft.rfid.service.criteria.EventsCriteria;
import com.isoft.rfid.service.dto.EventsDTO;
import com.isoft.rfid.service.mapper.EventsMapper;
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
 * Integration tests for the {@link EventsResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class EventsResourceIT {

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

    private static final Long DEFAULT_HANDLED_BY = 1L;
    private static final Long UPDATED_HANDLED_BY = 2L;
    private static final Long SMALLER_HANDLED_BY = 1L - 1L;

    private static final Long DEFAULT_GANTRY_PROCESSED = 1L;
    private static final Long UPDATED_GANTRY_PROCESSED = 2L;
    private static final Long SMALLER_GANTRY_PROCESSED = 1L - 1L;

    private static final Long DEFAULT_GANTRY_SENT = 1L;
    private static final Long UPDATED_GANTRY_SENT = 2L;
    private static final Long SMALLER_GANTRY_SENT = 1L - 1L;

    private static final Long DEFAULT_WHEN = 1L;
    private static final Long UPDATED_WHEN = 2L;
    private static final Long SMALLER_WHEN = 1L - 1L;

    private static final Long DEFAULT_TOLL = 1L;
    private static final Long UPDATED_TOLL = 2L;
    private static final Long SMALLER_TOLL = 1L - 1L;

    private static final Long DEFAULT_RULE_RCVD = 1L;
    private static final Long UPDATED_RULE_RCVD = 2L;
    private static final Long SMALLER_RULE_RCVD = 1L - 1L;

    private static final String DEFAULT_WANTED_FOR = "AAAAAAAAAA";
    private static final String UPDATED_WANTED_FOR = "BBBBBBBBBB";

    private static final Long DEFAULT_FINE = 1L;
    private static final Long UPDATED_FINE = 2L;
    private static final Long SMALLER_FINE = 1L - 1L;

    private static final String DEFAULT_LICENSE_ISSUE = "AAAAAAAAAA";
    private static final String UPDATED_LICENSE_ISSUE = "BBBBBBBBBB";

    private static final Long DEFAULT_WANTED_BY = 1L;
    private static final Long UPDATED_WANTED_BY = 2L;
    private static final Long SMALLER_WANTED_BY = 1L - 1L;

    private static final Long DEFAULT_RULE_PROCESSED = 1L;
    private static final Long UPDATED_RULE_PROCESSED = 2L;
    private static final Long SMALLER_RULE_PROCESSED = 1L - 1L;

    private static final Long DEFAULT_SPEED_FINE = 1L;
    private static final Long UPDATED_SPEED_FINE = 2L;
    private static final Long SMALLER_SPEED_FINE = 1L - 1L;

    private static final Long DEFAULT_LANE = 1L;
    private static final Long UPDATED_LANE = 2L;
    private static final Long SMALLER_LANE = 1L - 1L;

    private static final String DEFAULT_TAG_ISSUE = "AAAAAAAAAA";
    private static final String UPDATED_TAG_ISSUE = "BBBBBBBBBB";

    private static final String DEFAULT_STATUS_NAME = "AAAAAAAAAA";
    private static final String UPDATED_STATUS_NAME = "BBBBBBBBBB";

    private static final Long DEFAULT_LICENSE_FINE = 1L;
    private static final Long UPDATED_LICENSE_FINE = 2L;
    private static final Long SMALLER_LICENSE_FINE = 1L - 1L;

    private static final Long DEFAULT_STOLEN = 1L;
    private static final Long UPDATED_STOLEN = 2L;
    private static final Long SMALLER_STOLEN = 1L - 1L;

    private static final Boolean DEFAULT_WANTED = false;
    private static final Boolean UPDATED_WANTED = true;

    private static final Long DEFAULT_RULE_SENT = 1L;
    private static final Long UPDATED_RULE_SENT = 2L;
    private static final Long SMALLER_RULE_SENT = 1L - 1L;

    private static final Long DEFAULT_HANDLED = 1L;
    private static final Long UPDATED_HANDLED = 2L;
    private static final Long SMALLER_HANDLED = 1L - 1L;

    private static final String DEFAULT_RULE_ISSUE = "AAAAAAAAAA";
    private static final String UPDATED_RULE_ISSUE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/events";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/_search/events";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private EventsRepository eventsRepository;

    @Autowired
    private EventsMapper eventsMapper;

    @Autowired
    private EventsSearchRepository eventsSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restEventsMockMvc;

    private Events events;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Events createEntity(EntityManager em) {
        Events events = new Events()
            .guid(DEFAULT_GUID)
            .plate(DEFAULT_PLATE)
            .anpr(DEFAULT_ANPR)
            .rfid(DEFAULT_RFID)
            .dataStatus(DEFAULT_DATA_STATUS)
            .gantry(DEFAULT_GANTRY)
            .kph(DEFAULT_KPH)
            .ambush(DEFAULT_AMBUSH)
            .direction(DEFAULT_DIRECTION)
            .vehicle(DEFAULT_VEHICLE)
            .issue(DEFAULT_ISSUE)
            .status(DEFAULT_STATUS)
            .handledBy(DEFAULT_HANDLED_BY)
            .gantryProcessed(DEFAULT_GANTRY_PROCESSED)
            .gantrySent(DEFAULT_GANTRY_SENT)
            .when(DEFAULT_WHEN)
            .toll(DEFAULT_TOLL)
            .ruleRcvd(DEFAULT_RULE_RCVD)
            .wantedFor(DEFAULT_WANTED_FOR)
            .fine(DEFAULT_FINE)
            .licenseIssue(DEFAULT_LICENSE_ISSUE)
            .wantedBy(DEFAULT_WANTED_BY)
            .ruleProcessed(DEFAULT_RULE_PROCESSED)
            .speedFine(DEFAULT_SPEED_FINE)
            .lane(DEFAULT_LANE)
            .tagIssue(DEFAULT_TAG_ISSUE)
            .statusName(DEFAULT_STATUS_NAME)
            .licenseFine(DEFAULT_LICENSE_FINE)
            .stolen(DEFAULT_STOLEN)
            .wanted(DEFAULT_WANTED)
            .ruleSent(DEFAULT_RULE_SENT)
            .handled(DEFAULT_HANDLED)
            .ruleIssue(DEFAULT_RULE_ISSUE);
        return events;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Events createUpdatedEntity(EntityManager em) {
        Events events = new Events()
            .guid(UPDATED_GUID)
            .plate(UPDATED_PLATE)
            .anpr(UPDATED_ANPR)
            .rfid(UPDATED_RFID)
            .dataStatus(UPDATED_DATA_STATUS)
            .gantry(UPDATED_GANTRY)
            .kph(UPDATED_KPH)
            .ambush(UPDATED_AMBUSH)
            .direction(UPDATED_DIRECTION)
            .vehicle(UPDATED_VEHICLE)
            .issue(UPDATED_ISSUE)
            .status(UPDATED_STATUS)
            .handledBy(UPDATED_HANDLED_BY)
            .gantryProcessed(UPDATED_GANTRY_PROCESSED)
            .gantrySent(UPDATED_GANTRY_SENT)
            .when(UPDATED_WHEN)
            .toll(UPDATED_TOLL)
            .ruleRcvd(UPDATED_RULE_RCVD)
            .wantedFor(UPDATED_WANTED_FOR)
            .fine(UPDATED_FINE)
            .licenseIssue(UPDATED_LICENSE_ISSUE)
            .wantedBy(UPDATED_WANTED_BY)
            .ruleProcessed(UPDATED_RULE_PROCESSED)
            .speedFine(UPDATED_SPEED_FINE)
            .lane(UPDATED_LANE)
            .tagIssue(UPDATED_TAG_ISSUE)
            .statusName(UPDATED_STATUS_NAME)
            .licenseFine(UPDATED_LICENSE_FINE)
            .stolen(UPDATED_STOLEN)
            .wanted(UPDATED_WANTED)
            .ruleSent(UPDATED_RULE_SENT)
            .handled(UPDATED_HANDLED)
            .ruleIssue(UPDATED_RULE_ISSUE);
        return events;
    }

    @AfterEach
    public void cleanupElasticSearchRepository() {
        eventsSearchRepository.deleteAll();
        assertThat(eventsSearchRepository.count()).isEqualTo(0);
    }

    @BeforeEach
    public void initTest() {
        events = createEntity(em);
    }

    @Test
    @Transactional
    void createEvents() throws Exception {
        int databaseSizeBeforeCreate = eventsRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(eventsSearchRepository.findAll());
        // Create the Events
        EventsDTO eventsDTO = eventsMapper.toDto(events);
        restEventsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(eventsDTO)))
            .andExpect(status().isCreated());

        // Validate the Events in the database
        List<Events> eventsList = eventsRepository.findAll();
        assertThat(eventsList).hasSize(databaseSizeBeforeCreate + 1);
        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(eventsSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore + 1);
            });
        Events testEvents = eventsList.get(eventsList.size() - 1);
        assertThat(testEvents.getGuid()).isEqualTo(DEFAULT_GUID);
        assertThat(testEvents.getPlate()).isEqualTo(DEFAULT_PLATE);
        assertThat(testEvents.getAnpr()).isEqualTo(DEFAULT_ANPR);
        assertThat(testEvents.getRfid()).isEqualTo(DEFAULT_RFID);
        assertThat(testEvents.getDataStatus()).isEqualTo(DEFAULT_DATA_STATUS);
        assertThat(testEvents.getGantry()).isEqualTo(DEFAULT_GANTRY);
        assertThat(testEvents.getKph()).isEqualTo(DEFAULT_KPH);
        assertThat(testEvents.getAmbush()).isEqualTo(DEFAULT_AMBUSH);
        assertThat(testEvents.getDirection()).isEqualTo(DEFAULT_DIRECTION);
        assertThat(testEvents.getVehicle()).isEqualTo(DEFAULT_VEHICLE);
        assertThat(testEvents.getIssue()).isEqualTo(DEFAULT_ISSUE);
        assertThat(testEvents.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testEvents.getHandledBy()).isEqualTo(DEFAULT_HANDLED_BY);
        assertThat(testEvents.getGantryProcessed()).isEqualTo(DEFAULT_GANTRY_PROCESSED);
        assertThat(testEvents.getGantrySent()).isEqualTo(DEFAULT_GANTRY_SENT);
        assertThat(testEvents.getWhen()).isEqualTo(DEFAULT_WHEN);
        assertThat(testEvents.getToll()).isEqualTo(DEFAULT_TOLL);
        assertThat(testEvents.getRuleRcvd()).isEqualTo(DEFAULT_RULE_RCVD);
        assertThat(testEvents.getWantedFor()).isEqualTo(DEFAULT_WANTED_FOR);
        assertThat(testEvents.getFine()).isEqualTo(DEFAULT_FINE);
        assertThat(testEvents.getLicenseIssue()).isEqualTo(DEFAULT_LICENSE_ISSUE);
        assertThat(testEvents.getWantedBy()).isEqualTo(DEFAULT_WANTED_BY);
        assertThat(testEvents.getRuleProcessed()).isEqualTo(DEFAULT_RULE_PROCESSED);
        assertThat(testEvents.getSpeedFine()).isEqualTo(DEFAULT_SPEED_FINE);
        assertThat(testEvents.getLane()).isEqualTo(DEFAULT_LANE);
        assertThat(testEvents.getTagIssue()).isEqualTo(DEFAULT_TAG_ISSUE);
        assertThat(testEvents.getStatusName()).isEqualTo(DEFAULT_STATUS_NAME);
        assertThat(testEvents.getLicenseFine()).isEqualTo(DEFAULT_LICENSE_FINE);
        assertThat(testEvents.getStolen()).isEqualTo(DEFAULT_STOLEN);
        assertThat(testEvents.getWanted()).isEqualTo(DEFAULT_WANTED);
        assertThat(testEvents.getRuleSent()).isEqualTo(DEFAULT_RULE_SENT);
        assertThat(testEvents.getHandled()).isEqualTo(DEFAULT_HANDLED);
        assertThat(testEvents.getRuleIssue()).isEqualTo(DEFAULT_RULE_ISSUE);
    }

    @Test
    @Transactional
    void createEventsWithExistingId() throws Exception {
        // Create the Events with an existing ID
        events.setId(1L);
        EventsDTO eventsDTO = eventsMapper.toDto(events);

        int databaseSizeBeforeCreate = eventsRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(eventsSearchRepository.findAll());

        // An entity with an existing ID cannot be created, so this API call must fail
        restEventsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(eventsDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Events in the database
        List<Events> eventsList = eventsRepository.findAll();
        assertThat(eventsList).hasSize(databaseSizeBeforeCreate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(eventsSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkGuidIsRequired() throws Exception {
        int databaseSizeBeforeTest = eventsRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(eventsSearchRepository.findAll());
        // set the field null
        events.setGuid(null);

        // Create the Events, which fails.
        EventsDTO eventsDTO = eventsMapper.toDto(events);

        restEventsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(eventsDTO)))
            .andExpect(status().isBadRequest());

        List<Events> eventsList = eventsRepository.findAll();
        assertThat(eventsList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(eventsSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkDataStatusIsRequired() throws Exception {
        int databaseSizeBeforeTest = eventsRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(eventsSearchRepository.findAll());
        // set the field null
        events.setDataStatus(null);

        // Create the Events, which fails.
        EventsDTO eventsDTO = eventsMapper.toDto(events);

        restEventsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(eventsDTO)))
            .andExpect(status().isBadRequest());

        List<Events> eventsList = eventsRepository.findAll();
        assertThat(eventsList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(eventsSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkGantryIsRequired() throws Exception {
        int databaseSizeBeforeTest = eventsRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(eventsSearchRepository.findAll());
        // set the field null
        events.setGantry(null);

        // Create the Events, which fails.
        EventsDTO eventsDTO = eventsMapper.toDto(events);

        restEventsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(eventsDTO)))
            .andExpect(status().isBadRequest());

        List<Events> eventsList = eventsRepository.findAll();
        assertThat(eventsList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(eventsSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkVehicleIsRequired() throws Exception {
        int databaseSizeBeforeTest = eventsRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(eventsSearchRepository.findAll());
        // set the field null
        events.setVehicle(null);

        // Create the Events, which fails.
        EventsDTO eventsDTO = eventsMapper.toDto(events);

        restEventsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(eventsDTO)))
            .andExpect(status().isBadRequest());

        List<Events> eventsList = eventsRepository.findAll();
        assertThat(eventsList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(eventsSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkLaneIsRequired() throws Exception {
        int databaseSizeBeforeTest = eventsRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(eventsSearchRepository.findAll());
        // set the field null
        events.setLane(null);

        // Create the Events, which fails.
        EventsDTO eventsDTO = eventsMapper.toDto(events);

        restEventsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(eventsDTO)))
            .andExpect(status().isBadRequest());

        List<Events> eventsList = eventsRepository.findAll();
        assertThat(eventsList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(eventsSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void getAllEvents() throws Exception {
        // Initialize the database
        eventsRepository.saveAndFlush(events);

        // Get all the eventsList
        restEventsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(events.getId().intValue())))
            .andExpect(jsonPath("$.[*].guid").value(hasItem(DEFAULT_GUID)))
            .andExpect(jsonPath("$.[*].plate").value(hasItem(DEFAULT_PLATE)))
            .andExpect(jsonPath("$.[*].anpr").value(hasItem(DEFAULT_ANPR)))
            .andExpect(jsonPath("$.[*].rfid").value(hasItem(DEFAULT_RFID)))
            .andExpect(jsonPath("$.[*].dataStatus").value(hasItem(DEFAULT_DATA_STATUS)))
            .andExpect(jsonPath("$.[*].gantry").value(hasItem(DEFAULT_GANTRY.intValue())))
            .andExpect(jsonPath("$.[*].kph").value(hasItem(DEFAULT_KPH.intValue())))
            .andExpect(jsonPath("$.[*].ambush").value(hasItem(DEFAULT_AMBUSH.intValue())))
            .andExpect(jsonPath("$.[*].direction").value(hasItem(DEFAULT_DIRECTION.intValue())))
            .andExpect(jsonPath("$.[*].vehicle").value(hasItem(DEFAULT_VEHICLE.intValue())))
            .andExpect(jsonPath("$.[*].issue").value(hasItem(DEFAULT_ISSUE)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS)))
            .andExpect(jsonPath("$.[*].handledBy").value(hasItem(DEFAULT_HANDLED_BY.intValue())))
            .andExpect(jsonPath("$.[*].gantryProcessed").value(hasItem(DEFAULT_GANTRY_PROCESSED.intValue())))
            .andExpect(jsonPath("$.[*].gantrySent").value(hasItem(DEFAULT_GANTRY_SENT.intValue())))
            .andExpect(jsonPath("$.[*].when").value(hasItem(DEFAULT_WHEN.intValue())))
            .andExpect(jsonPath("$.[*].toll").value(hasItem(DEFAULT_TOLL.intValue())))
            .andExpect(jsonPath("$.[*].ruleRcvd").value(hasItem(DEFAULT_RULE_RCVD.intValue())))
            .andExpect(jsonPath("$.[*].wantedFor").value(hasItem(DEFAULT_WANTED_FOR)))
            .andExpect(jsonPath("$.[*].fine").value(hasItem(DEFAULT_FINE.intValue())))
            .andExpect(jsonPath("$.[*].licenseIssue").value(hasItem(DEFAULT_LICENSE_ISSUE)))
            .andExpect(jsonPath("$.[*].wantedBy").value(hasItem(DEFAULT_WANTED_BY.intValue())))
            .andExpect(jsonPath("$.[*].ruleProcessed").value(hasItem(DEFAULT_RULE_PROCESSED.intValue())))
            .andExpect(jsonPath("$.[*].speedFine").value(hasItem(DEFAULT_SPEED_FINE.intValue())))
            .andExpect(jsonPath("$.[*].lane").value(hasItem(DEFAULT_LANE.intValue())))
            .andExpect(jsonPath("$.[*].tagIssue").value(hasItem(DEFAULT_TAG_ISSUE)))
            .andExpect(jsonPath("$.[*].statusName").value(hasItem(DEFAULT_STATUS_NAME)))
            .andExpect(jsonPath("$.[*].licenseFine").value(hasItem(DEFAULT_LICENSE_FINE.intValue())))
            .andExpect(jsonPath("$.[*].stolen").value(hasItem(DEFAULT_STOLEN.intValue())))
            .andExpect(jsonPath("$.[*].wanted").value(hasItem(DEFAULT_WANTED.booleanValue())))
            .andExpect(jsonPath("$.[*].ruleSent").value(hasItem(DEFAULT_RULE_SENT.intValue())))
            .andExpect(jsonPath("$.[*].handled").value(hasItem(DEFAULT_HANDLED.intValue())))
            .andExpect(jsonPath("$.[*].ruleIssue").value(hasItem(DEFAULT_RULE_ISSUE)));
    }

    @Test
    @Transactional
    void getEvents() throws Exception {
        // Initialize the database
        eventsRepository.saveAndFlush(events);

        // Get the events
        restEventsMockMvc
            .perform(get(ENTITY_API_URL_ID, events.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(events.getId().intValue()))
            .andExpect(jsonPath("$.guid").value(DEFAULT_GUID))
            .andExpect(jsonPath("$.plate").value(DEFAULT_PLATE))
            .andExpect(jsonPath("$.anpr").value(DEFAULT_ANPR))
            .andExpect(jsonPath("$.rfid").value(DEFAULT_RFID))
            .andExpect(jsonPath("$.dataStatus").value(DEFAULT_DATA_STATUS))
            .andExpect(jsonPath("$.gantry").value(DEFAULT_GANTRY.intValue()))
            .andExpect(jsonPath("$.kph").value(DEFAULT_KPH.intValue()))
            .andExpect(jsonPath("$.ambush").value(DEFAULT_AMBUSH.intValue()))
            .andExpect(jsonPath("$.direction").value(DEFAULT_DIRECTION.intValue()))
            .andExpect(jsonPath("$.vehicle").value(DEFAULT_VEHICLE.intValue()))
            .andExpect(jsonPath("$.issue").value(DEFAULT_ISSUE))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS))
            .andExpect(jsonPath("$.handledBy").value(DEFAULT_HANDLED_BY.intValue()))
            .andExpect(jsonPath("$.gantryProcessed").value(DEFAULT_GANTRY_PROCESSED.intValue()))
            .andExpect(jsonPath("$.gantrySent").value(DEFAULT_GANTRY_SENT.intValue()))
            .andExpect(jsonPath("$.when").value(DEFAULT_WHEN.intValue()))
            .andExpect(jsonPath("$.toll").value(DEFAULT_TOLL.intValue()))
            .andExpect(jsonPath("$.ruleRcvd").value(DEFAULT_RULE_RCVD.intValue()))
            .andExpect(jsonPath("$.wantedFor").value(DEFAULT_WANTED_FOR))
            .andExpect(jsonPath("$.fine").value(DEFAULT_FINE.intValue()))
            .andExpect(jsonPath("$.licenseIssue").value(DEFAULT_LICENSE_ISSUE))
            .andExpect(jsonPath("$.wantedBy").value(DEFAULT_WANTED_BY.intValue()))
            .andExpect(jsonPath("$.ruleProcessed").value(DEFAULT_RULE_PROCESSED.intValue()))
            .andExpect(jsonPath("$.speedFine").value(DEFAULT_SPEED_FINE.intValue()))
            .andExpect(jsonPath("$.lane").value(DEFAULT_LANE.intValue()))
            .andExpect(jsonPath("$.tagIssue").value(DEFAULT_TAG_ISSUE))
            .andExpect(jsonPath("$.statusName").value(DEFAULT_STATUS_NAME))
            .andExpect(jsonPath("$.licenseFine").value(DEFAULT_LICENSE_FINE.intValue()))
            .andExpect(jsonPath("$.stolen").value(DEFAULT_STOLEN.intValue()))
            .andExpect(jsonPath("$.wanted").value(DEFAULT_WANTED.booleanValue()))
            .andExpect(jsonPath("$.ruleSent").value(DEFAULT_RULE_SENT.intValue()))
            .andExpect(jsonPath("$.handled").value(DEFAULT_HANDLED.intValue()))
            .andExpect(jsonPath("$.ruleIssue").value(DEFAULT_RULE_ISSUE));
    }

    @Test
    @Transactional
    void getEventsByIdFiltering() throws Exception {
        // Initialize the database
        eventsRepository.saveAndFlush(events);

        Long id = events.getId();

        defaultEventsShouldBeFound("id.equals=" + id);
        defaultEventsShouldNotBeFound("id.notEquals=" + id);

        defaultEventsShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultEventsShouldNotBeFound("id.greaterThan=" + id);

        defaultEventsShouldBeFound("id.lessThanOrEqual=" + id);
        defaultEventsShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllEventsByGuidIsEqualToSomething() throws Exception {
        // Initialize the database
        eventsRepository.saveAndFlush(events);

        // Get all the eventsList where guid equals to DEFAULT_GUID
        defaultEventsShouldBeFound("guid.equals=" + DEFAULT_GUID);

        // Get all the eventsList where guid equals to UPDATED_GUID
        defaultEventsShouldNotBeFound("guid.equals=" + UPDATED_GUID);
    }

    @Test
    @Transactional
    void getAllEventsByGuidIsInShouldWork() throws Exception {
        // Initialize the database
        eventsRepository.saveAndFlush(events);

        // Get all the eventsList where guid in DEFAULT_GUID or UPDATED_GUID
        defaultEventsShouldBeFound("guid.in=" + DEFAULT_GUID + "," + UPDATED_GUID);

        // Get all the eventsList where guid equals to UPDATED_GUID
        defaultEventsShouldNotBeFound("guid.in=" + UPDATED_GUID);
    }

    @Test
    @Transactional
    void getAllEventsByGuidIsNullOrNotNull() throws Exception {
        // Initialize the database
        eventsRepository.saveAndFlush(events);

        // Get all the eventsList where guid is not null
        defaultEventsShouldBeFound("guid.specified=true");

        // Get all the eventsList where guid is null
        defaultEventsShouldNotBeFound("guid.specified=false");
    }

    @Test
    @Transactional
    void getAllEventsByGuidContainsSomething() throws Exception {
        // Initialize the database
        eventsRepository.saveAndFlush(events);

        // Get all the eventsList where guid contains DEFAULT_GUID
        defaultEventsShouldBeFound("guid.contains=" + DEFAULT_GUID);

        // Get all the eventsList where guid contains UPDATED_GUID
        defaultEventsShouldNotBeFound("guid.contains=" + UPDATED_GUID);
    }

    @Test
    @Transactional
    void getAllEventsByGuidNotContainsSomething() throws Exception {
        // Initialize the database
        eventsRepository.saveAndFlush(events);

        // Get all the eventsList where guid does not contain DEFAULT_GUID
        defaultEventsShouldNotBeFound("guid.doesNotContain=" + DEFAULT_GUID);

        // Get all the eventsList where guid does not contain UPDATED_GUID
        defaultEventsShouldBeFound("guid.doesNotContain=" + UPDATED_GUID);
    }

    @Test
    @Transactional
    void getAllEventsByPlateIsEqualToSomething() throws Exception {
        // Initialize the database
        eventsRepository.saveAndFlush(events);

        // Get all the eventsList where plate equals to DEFAULT_PLATE
        defaultEventsShouldBeFound("plate.equals=" + DEFAULT_PLATE);

        // Get all the eventsList where plate equals to UPDATED_PLATE
        defaultEventsShouldNotBeFound("plate.equals=" + UPDATED_PLATE);
    }

    @Test
    @Transactional
    void getAllEventsByPlateIsInShouldWork() throws Exception {
        // Initialize the database
        eventsRepository.saveAndFlush(events);

        // Get all the eventsList where plate in DEFAULT_PLATE or UPDATED_PLATE
        defaultEventsShouldBeFound("plate.in=" + DEFAULT_PLATE + "," + UPDATED_PLATE);

        // Get all the eventsList where plate equals to UPDATED_PLATE
        defaultEventsShouldNotBeFound("plate.in=" + UPDATED_PLATE);
    }

    @Test
    @Transactional
    void getAllEventsByPlateIsNullOrNotNull() throws Exception {
        // Initialize the database
        eventsRepository.saveAndFlush(events);

        // Get all the eventsList where plate is not null
        defaultEventsShouldBeFound("plate.specified=true");

        // Get all the eventsList where plate is null
        defaultEventsShouldNotBeFound("plate.specified=false");
    }

    @Test
    @Transactional
    void getAllEventsByPlateContainsSomething() throws Exception {
        // Initialize the database
        eventsRepository.saveAndFlush(events);

        // Get all the eventsList where plate contains DEFAULT_PLATE
        defaultEventsShouldBeFound("plate.contains=" + DEFAULT_PLATE);

        // Get all the eventsList where plate contains UPDATED_PLATE
        defaultEventsShouldNotBeFound("plate.contains=" + UPDATED_PLATE);
    }

    @Test
    @Transactional
    void getAllEventsByPlateNotContainsSomething() throws Exception {
        // Initialize the database
        eventsRepository.saveAndFlush(events);

        // Get all the eventsList where plate does not contain DEFAULT_PLATE
        defaultEventsShouldNotBeFound("plate.doesNotContain=" + DEFAULT_PLATE);

        // Get all the eventsList where plate does not contain UPDATED_PLATE
        defaultEventsShouldBeFound("plate.doesNotContain=" + UPDATED_PLATE);
    }

    @Test
    @Transactional
    void getAllEventsByAnprIsEqualToSomething() throws Exception {
        // Initialize the database
        eventsRepository.saveAndFlush(events);

        // Get all the eventsList where anpr equals to DEFAULT_ANPR
        defaultEventsShouldBeFound("anpr.equals=" + DEFAULT_ANPR);

        // Get all the eventsList where anpr equals to UPDATED_ANPR
        defaultEventsShouldNotBeFound("anpr.equals=" + UPDATED_ANPR);
    }

    @Test
    @Transactional
    void getAllEventsByAnprIsInShouldWork() throws Exception {
        // Initialize the database
        eventsRepository.saveAndFlush(events);

        // Get all the eventsList where anpr in DEFAULT_ANPR or UPDATED_ANPR
        defaultEventsShouldBeFound("anpr.in=" + DEFAULT_ANPR + "," + UPDATED_ANPR);

        // Get all the eventsList where anpr equals to UPDATED_ANPR
        defaultEventsShouldNotBeFound("anpr.in=" + UPDATED_ANPR);
    }

    @Test
    @Transactional
    void getAllEventsByAnprIsNullOrNotNull() throws Exception {
        // Initialize the database
        eventsRepository.saveAndFlush(events);

        // Get all the eventsList where anpr is not null
        defaultEventsShouldBeFound("anpr.specified=true");

        // Get all the eventsList where anpr is null
        defaultEventsShouldNotBeFound("anpr.specified=false");
    }

    @Test
    @Transactional
    void getAllEventsByAnprContainsSomething() throws Exception {
        // Initialize the database
        eventsRepository.saveAndFlush(events);

        // Get all the eventsList where anpr contains DEFAULT_ANPR
        defaultEventsShouldBeFound("anpr.contains=" + DEFAULT_ANPR);

        // Get all the eventsList where anpr contains UPDATED_ANPR
        defaultEventsShouldNotBeFound("anpr.contains=" + UPDATED_ANPR);
    }

    @Test
    @Transactional
    void getAllEventsByAnprNotContainsSomething() throws Exception {
        // Initialize the database
        eventsRepository.saveAndFlush(events);

        // Get all the eventsList where anpr does not contain DEFAULT_ANPR
        defaultEventsShouldNotBeFound("anpr.doesNotContain=" + DEFAULT_ANPR);

        // Get all the eventsList where anpr does not contain UPDATED_ANPR
        defaultEventsShouldBeFound("anpr.doesNotContain=" + UPDATED_ANPR);
    }

    @Test
    @Transactional
    void getAllEventsByRfidIsEqualToSomething() throws Exception {
        // Initialize the database
        eventsRepository.saveAndFlush(events);

        // Get all the eventsList where rfid equals to DEFAULT_RFID
        defaultEventsShouldBeFound("rfid.equals=" + DEFAULT_RFID);

        // Get all the eventsList where rfid equals to UPDATED_RFID
        defaultEventsShouldNotBeFound("rfid.equals=" + UPDATED_RFID);
    }

    @Test
    @Transactional
    void getAllEventsByRfidIsInShouldWork() throws Exception {
        // Initialize the database
        eventsRepository.saveAndFlush(events);

        // Get all the eventsList where rfid in DEFAULT_RFID or UPDATED_RFID
        defaultEventsShouldBeFound("rfid.in=" + DEFAULT_RFID + "," + UPDATED_RFID);

        // Get all the eventsList where rfid equals to UPDATED_RFID
        defaultEventsShouldNotBeFound("rfid.in=" + UPDATED_RFID);
    }

    @Test
    @Transactional
    void getAllEventsByRfidIsNullOrNotNull() throws Exception {
        // Initialize the database
        eventsRepository.saveAndFlush(events);

        // Get all the eventsList where rfid is not null
        defaultEventsShouldBeFound("rfid.specified=true");

        // Get all the eventsList where rfid is null
        defaultEventsShouldNotBeFound("rfid.specified=false");
    }

    @Test
    @Transactional
    void getAllEventsByRfidContainsSomething() throws Exception {
        // Initialize the database
        eventsRepository.saveAndFlush(events);

        // Get all the eventsList where rfid contains DEFAULT_RFID
        defaultEventsShouldBeFound("rfid.contains=" + DEFAULT_RFID);

        // Get all the eventsList where rfid contains UPDATED_RFID
        defaultEventsShouldNotBeFound("rfid.contains=" + UPDATED_RFID);
    }

    @Test
    @Transactional
    void getAllEventsByRfidNotContainsSomething() throws Exception {
        // Initialize the database
        eventsRepository.saveAndFlush(events);

        // Get all the eventsList where rfid does not contain DEFAULT_RFID
        defaultEventsShouldNotBeFound("rfid.doesNotContain=" + DEFAULT_RFID);

        // Get all the eventsList where rfid does not contain UPDATED_RFID
        defaultEventsShouldBeFound("rfid.doesNotContain=" + UPDATED_RFID);
    }

    @Test
    @Transactional
    void getAllEventsByDataStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        eventsRepository.saveAndFlush(events);

        // Get all the eventsList where dataStatus equals to DEFAULT_DATA_STATUS
        defaultEventsShouldBeFound("dataStatus.equals=" + DEFAULT_DATA_STATUS);

        // Get all the eventsList where dataStatus equals to UPDATED_DATA_STATUS
        defaultEventsShouldNotBeFound("dataStatus.equals=" + UPDATED_DATA_STATUS);
    }

    @Test
    @Transactional
    void getAllEventsByDataStatusIsInShouldWork() throws Exception {
        // Initialize the database
        eventsRepository.saveAndFlush(events);

        // Get all the eventsList where dataStatus in DEFAULT_DATA_STATUS or UPDATED_DATA_STATUS
        defaultEventsShouldBeFound("dataStatus.in=" + DEFAULT_DATA_STATUS + "," + UPDATED_DATA_STATUS);

        // Get all the eventsList where dataStatus equals to UPDATED_DATA_STATUS
        defaultEventsShouldNotBeFound("dataStatus.in=" + UPDATED_DATA_STATUS);
    }

    @Test
    @Transactional
    void getAllEventsByDataStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        eventsRepository.saveAndFlush(events);

        // Get all the eventsList where dataStatus is not null
        defaultEventsShouldBeFound("dataStatus.specified=true");

        // Get all the eventsList where dataStatus is null
        defaultEventsShouldNotBeFound("dataStatus.specified=false");
    }

    @Test
    @Transactional
    void getAllEventsByDataStatusContainsSomething() throws Exception {
        // Initialize the database
        eventsRepository.saveAndFlush(events);

        // Get all the eventsList where dataStatus contains DEFAULT_DATA_STATUS
        defaultEventsShouldBeFound("dataStatus.contains=" + DEFAULT_DATA_STATUS);

        // Get all the eventsList where dataStatus contains UPDATED_DATA_STATUS
        defaultEventsShouldNotBeFound("dataStatus.contains=" + UPDATED_DATA_STATUS);
    }

    @Test
    @Transactional
    void getAllEventsByDataStatusNotContainsSomething() throws Exception {
        // Initialize the database
        eventsRepository.saveAndFlush(events);

        // Get all the eventsList where dataStatus does not contain DEFAULT_DATA_STATUS
        defaultEventsShouldNotBeFound("dataStatus.doesNotContain=" + DEFAULT_DATA_STATUS);

        // Get all the eventsList where dataStatus does not contain UPDATED_DATA_STATUS
        defaultEventsShouldBeFound("dataStatus.doesNotContain=" + UPDATED_DATA_STATUS);
    }

    @Test
    @Transactional
    void getAllEventsByGantryIsEqualToSomething() throws Exception {
        // Initialize the database
        eventsRepository.saveAndFlush(events);

        // Get all the eventsList where gantry equals to DEFAULT_GANTRY
        defaultEventsShouldBeFound("gantry.equals=" + DEFAULT_GANTRY);

        // Get all the eventsList where gantry equals to UPDATED_GANTRY
        defaultEventsShouldNotBeFound("gantry.equals=" + UPDATED_GANTRY);
    }

    @Test
    @Transactional
    void getAllEventsByGantryIsInShouldWork() throws Exception {
        // Initialize the database
        eventsRepository.saveAndFlush(events);

        // Get all the eventsList where gantry in DEFAULT_GANTRY or UPDATED_GANTRY
        defaultEventsShouldBeFound("gantry.in=" + DEFAULT_GANTRY + "," + UPDATED_GANTRY);

        // Get all the eventsList where gantry equals to UPDATED_GANTRY
        defaultEventsShouldNotBeFound("gantry.in=" + UPDATED_GANTRY);
    }

    @Test
    @Transactional
    void getAllEventsByGantryIsNullOrNotNull() throws Exception {
        // Initialize the database
        eventsRepository.saveAndFlush(events);

        // Get all the eventsList where gantry is not null
        defaultEventsShouldBeFound("gantry.specified=true");

        // Get all the eventsList where gantry is null
        defaultEventsShouldNotBeFound("gantry.specified=false");
    }

    @Test
    @Transactional
    void getAllEventsByGantryIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        eventsRepository.saveAndFlush(events);

        // Get all the eventsList where gantry is greater than or equal to DEFAULT_GANTRY
        defaultEventsShouldBeFound("gantry.greaterThanOrEqual=" + DEFAULT_GANTRY);

        // Get all the eventsList where gantry is greater than or equal to UPDATED_GANTRY
        defaultEventsShouldNotBeFound("gantry.greaterThanOrEqual=" + UPDATED_GANTRY);
    }

    @Test
    @Transactional
    void getAllEventsByGantryIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        eventsRepository.saveAndFlush(events);

        // Get all the eventsList where gantry is less than or equal to DEFAULT_GANTRY
        defaultEventsShouldBeFound("gantry.lessThanOrEqual=" + DEFAULT_GANTRY);

        // Get all the eventsList where gantry is less than or equal to SMALLER_GANTRY
        defaultEventsShouldNotBeFound("gantry.lessThanOrEqual=" + SMALLER_GANTRY);
    }

    @Test
    @Transactional
    void getAllEventsByGantryIsLessThanSomething() throws Exception {
        // Initialize the database
        eventsRepository.saveAndFlush(events);

        // Get all the eventsList where gantry is less than DEFAULT_GANTRY
        defaultEventsShouldNotBeFound("gantry.lessThan=" + DEFAULT_GANTRY);

        // Get all the eventsList where gantry is less than UPDATED_GANTRY
        defaultEventsShouldBeFound("gantry.lessThan=" + UPDATED_GANTRY);
    }

    @Test
    @Transactional
    void getAllEventsByGantryIsGreaterThanSomething() throws Exception {
        // Initialize the database
        eventsRepository.saveAndFlush(events);

        // Get all the eventsList where gantry is greater than DEFAULT_GANTRY
        defaultEventsShouldNotBeFound("gantry.greaterThan=" + DEFAULT_GANTRY);

        // Get all the eventsList where gantry is greater than SMALLER_GANTRY
        defaultEventsShouldBeFound("gantry.greaterThan=" + SMALLER_GANTRY);
    }

    @Test
    @Transactional
    void getAllEventsByKphIsEqualToSomething() throws Exception {
        // Initialize the database
        eventsRepository.saveAndFlush(events);

        // Get all the eventsList where kph equals to DEFAULT_KPH
        defaultEventsShouldBeFound("kph.equals=" + DEFAULT_KPH);

        // Get all the eventsList where kph equals to UPDATED_KPH
        defaultEventsShouldNotBeFound("kph.equals=" + UPDATED_KPH);
    }

    @Test
    @Transactional
    void getAllEventsByKphIsInShouldWork() throws Exception {
        // Initialize the database
        eventsRepository.saveAndFlush(events);

        // Get all the eventsList where kph in DEFAULT_KPH or UPDATED_KPH
        defaultEventsShouldBeFound("kph.in=" + DEFAULT_KPH + "," + UPDATED_KPH);

        // Get all the eventsList where kph equals to UPDATED_KPH
        defaultEventsShouldNotBeFound("kph.in=" + UPDATED_KPH);
    }

    @Test
    @Transactional
    void getAllEventsByKphIsNullOrNotNull() throws Exception {
        // Initialize the database
        eventsRepository.saveAndFlush(events);

        // Get all the eventsList where kph is not null
        defaultEventsShouldBeFound("kph.specified=true");

        // Get all the eventsList where kph is null
        defaultEventsShouldNotBeFound("kph.specified=false");
    }

    @Test
    @Transactional
    void getAllEventsByKphIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        eventsRepository.saveAndFlush(events);

        // Get all the eventsList where kph is greater than or equal to DEFAULT_KPH
        defaultEventsShouldBeFound("kph.greaterThanOrEqual=" + DEFAULT_KPH);

        // Get all the eventsList where kph is greater than or equal to UPDATED_KPH
        defaultEventsShouldNotBeFound("kph.greaterThanOrEqual=" + UPDATED_KPH);
    }

    @Test
    @Transactional
    void getAllEventsByKphIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        eventsRepository.saveAndFlush(events);

        // Get all the eventsList where kph is less than or equal to DEFAULT_KPH
        defaultEventsShouldBeFound("kph.lessThanOrEqual=" + DEFAULT_KPH);

        // Get all the eventsList where kph is less than or equal to SMALLER_KPH
        defaultEventsShouldNotBeFound("kph.lessThanOrEqual=" + SMALLER_KPH);
    }

    @Test
    @Transactional
    void getAllEventsByKphIsLessThanSomething() throws Exception {
        // Initialize the database
        eventsRepository.saveAndFlush(events);

        // Get all the eventsList where kph is less than DEFAULT_KPH
        defaultEventsShouldNotBeFound("kph.lessThan=" + DEFAULT_KPH);

        // Get all the eventsList where kph is less than UPDATED_KPH
        defaultEventsShouldBeFound("kph.lessThan=" + UPDATED_KPH);
    }

    @Test
    @Transactional
    void getAllEventsByKphIsGreaterThanSomething() throws Exception {
        // Initialize the database
        eventsRepository.saveAndFlush(events);

        // Get all the eventsList where kph is greater than DEFAULT_KPH
        defaultEventsShouldNotBeFound("kph.greaterThan=" + DEFAULT_KPH);

        // Get all the eventsList where kph is greater than SMALLER_KPH
        defaultEventsShouldBeFound("kph.greaterThan=" + SMALLER_KPH);
    }

    @Test
    @Transactional
    void getAllEventsByAmbushIsEqualToSomething() throws Exception {
        // Initialize the database
        eventsRepository.saveAndFlush(events);

        // Get all the eventsList where ambush equals to DEFAULT_AMBUSH
        defaultEventsShouldBeFound("ambush.equals=" + DEFAULT_AMBUSH);

        // Get all the eventsList where ambush equals to UPDATED_AMBUSH
        defaultEventsShouldNotBeFound("ambush.equals=" + UPDATED_AMBUSH);
    }

    @Test
    @Transactional
    void getAllEventsByAmbushIsInShouldWork() throws Exception {
        // Initialize the database
        eventsRepository.saveAndFlush(events);

        // Get all the eventsList where ambush in DEFAULT_AMBUSH or UPDATED_AMBUSH
        defaultEventsShouldBeFound("ambush.in=" + DEFAULT_AMBUSH + "," + UPDATED_AMBUSH);

        // Get all the eventsList where ambush equals to UPDATED_AMBUSH
        defaultEventsShouldNotBeFound("ambush.in=" + UPDATED_AMBUSH);
    }

    @Test
    @Transactional
    void getAllEventsByAmbushIsNullOrNotNull() throws Exception {
        // Initialize the database
        eventsRepository.saveAndFlush(events);

        // Get all the eventsList where ambush is not null
        defaultEventsShouldBeFound("ambush.specified=true");

        // Get all the eventsList where ambush is null
        defaultEventsShouldNotBeFound("ambush.specified=false");
    }

    @Test
    @Transactional
    void getAllEventsByAmbushIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        eventsRepository.saveAndFlush(events);

        // Get all the eventsList where ambush is greater than or equal to DEFAULT_AMBUSH
        defaultEventsShouldBeFound("ambush.greaterThanOrEqual=" + DEFAULT_AMBUSH);

        // Get all the eventsList where ambush is greater than or equal to UPDATED_AMBUSH
        defaultEventsShouldNotBeFound("ambush.greaterThanOrEqual=" + UPDATED_AMBUSH);
    }

    @Test
    @Transactional
    void getAllEventsByAmbushIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        eventsRepository.saveAndFlush(events);

        // Get all the eventsList where ambush is less than or equal to DEFAULT_AMBUSH
        defaultEventsShouldBeFound("ambush.lessThanOrEqual=" + DEFAULT_AMBUSH);

        // Get all the eventsList where ambush is less than or equal to SMALLER_AMBUSH
        defaultEventsShouldNotBeFound("ambush.lessThanOrEqual=" + SMALLER_AMBUSH);
    }

    @Test
    @Transactional
    void getAllEventsByAmbushIsLessThanSomething() throws Exception {
        // Initialize the database
        eventsRepository.saveAndFlush(events);

        // Get all the eventsList where ambush is less than DEFAULT_AMBUSH
        defaultEventsShouldNotBeFound("ambush.lessThan=" + DEFAULT_AMBUSH);

        // Get all the eventsList where ambush is less than UPDATED_AMBUSH
        defaultEventsShouldBeFound("ambush.lessThan=" + UPDATED_AMBUSH);
    }

    @Test
    @Transactional
    void getAllEventsByAmbushIsGreaterThanSomething() throws Exception {
        // Initialize the database
        eventsRepository.saveAndFlush(events);

        // Get all the eventsList where ambush is greater than DEFAULT_AMBUSH
        defaultEventsShouldNotBeFound("ambush.greaterThan=" + DEFAULT_AMBUSH);

        // Get all the eventsList where ambush is greater than SMALLER_AMBUSH
        defaultEventsShouldBeFound("ambush.greaterThan=" + SMALLER_AMBUSH);
    }

    @Test
    @Transactional
    void getAllEventsByDirectionIsEqualToSomething() throws Exception {
        // Initialize the database
        eventsRepository.saveAndFlush(events);

        // Get all the eventsList where direction equals to DEFAULT_DIRECTION
        defaultEventsShouldBeFound("direction.equals=" + DEFAULT_DIRECTION);

        // Get all the eventsList where direction equals to UPDATED_DIRECTION
        defaultEventsShouldNotBeFound("direction.equals=" + UPDATED_DIRECTION);
    }

    @Test
    @Transactional
    void getAllEventsByDirectionIsInShouldWork() throws Exception {
        // Initialize the database
        eventsRepository.saveAndFlush(events);

        // Get all the eventsList where direction in DEFAULT_DIRECTION or UPDATED_DIRECTION
        defaultEventsShouldBeFound("direction.in=" + DEFAULT_DIRECTION + "," + UPDATED_DIRECTION);

        // Get all the eventsList where direction equals to UPDATED_DIRECTION
        defaultEventsShouldNotBeFound("direction.in=" + UPDATED_DIRECTION);
    }

    @Test
    @Transactional
    void getAllEventsByDirectionIsNullOrNotNull() throws Exception {
        // Initialize the database
        eventsRepository.saveAndFlush(events);

        // Get all the eventsList where direction is not null
        defaultEventsShouldBeFound("direction.specified=true");

        // Get all the eventsList where direction is null
        defaultEventsShouldNotBeFound("direction.specified=false");
    }

    @Test
    @Transactional
    void getAllEventsByDirectionIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        eventsRepository.saveAndFlush(events);

        // Get all the eventsList where direction is greater than or equal to DEFAULT_DIRECTION
        defaultEventsShouldBeFound("direction.greaterThanOrEqual=" + DEFAULT_DIRECTION);

        // Get all the eventsList where direction is greater than or equal to UPDATED_DIRECTION
        defaultEventsShouldNotBeFound("direction.greaterThanOrEqual=" + UPDATED_DIRECTION);
    }

    @Test
    @Transactional
    void getAllEventsByDirectionIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        eventsRepository.saveAndFlush(events);

        // Get all the eventsList where direction is less than or equal to DEFAULT_DIRECTION
        defaultEventsShouldBeFound("direction.lessThanOrEqual=" + DEFAULT_DIRECTION);

        // Get all the eventsList where direction is less than or equal to SMALLER_DIRECTION
        defaultEventsShouldNotBeFound("direction.lessThanOrEqual=" + SMALLER_DIRECTION);
    }

    @Test
    @Transactional
    void getAllEventsByDirectionIsLessThanSomething() throws Exception {
        // Initialize the database
        eventsRepository.saveAndFlush(events);

        // Get all the eventsList where direction is less than DEFAULT_DIRECTION
        defaultEventsShouldNotBeFound("direction.lessThan=" + DEFAULT_DIRECTION);

        // Get all the eventsList where direction is less than UPDATED_DIRECTION
        defaultEventsShouldBeFound("direction.lessThan=" + UPDATED_DIRECTION);
    }

    @Test
    @Transactional
    void getAllEventsByDirectionIsGreaterThanSomething() throws Exception {
        // Initialize the database
        eventsRepository.saveAndFlush(events);

        // Get all the eventsList where direction is greater than DEFAULT_DIRECTION
        defaultEventsShouldNotBeFound("direction.greaterThan=" + DEFAULT_DIRECTION);

        // Get all the eventsList where direction is greater than SMALLER_DIRECTION
        defaultEventsShouldBeFound("direction.greaterThan=" + SMALLER_DIRECTION);
    }

    @Test
    @Transactional
    void getAllEventsByVehicleIsEqualToSomething() throws Exception {
        // Initialize the database
        eventsRepository.saveAndFlush(events);

        // Get all the eventsList where vehicle equals to DEFAULT_VEHICLE
        defaultEventsShouldBeFound("vehicle.equals=" + DEFAULT_VEHICLE);

        // Get all the eventsList where vehicle equals to UPDATED_VEHICLE
        defaultEventsShouldNotBeFound("vehicle.equals=" + UPDATED_VEHICLE);
    }

    @Test
    @Transactional
    void getAllEventsByVehicleIsInShouldWork() throws Exception {
        // Initialize the database
        eventsRepository.saveAndFlush(events);

        // Get all the eventsList where vehicle in DEFAULT_VEHICLE or UPDATED_VEHICLE
        defaultEventsShouldBeFound("vehicle.in=" + DEFAULT_VEHICLE + "," + UPDATED_VEHICLE);

        // Get all the eventsList where vehicle equals to UPDATED_VEHICLE
        defaultEventsShouldNotBeFound("vehicle.in=" + UPDATED_VEHICLE);
    }

    @Test
    @Transactional
    void getAllEventsByVehicleIsNullOrNotNull() throws Exception {
        // Initialize the database
        eventsRepository.saveAndFlush(events);

        // Get all the eventsList where vehicle is not null
        defaultEventsShouldBeFound("vehicle.specified=true");

        // Get all the eventsList where vehicle is null
        defaultEventsShouldNotBeFound("vehicle.specified=false");
    }

    @Test
    @Transactional
    void getAllEventsByVehicleIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        eventsRepository.saveAndFlush(events);

        // Get all the eventsList where vehicle is greater than or equal to DEFAULT_VEHICLE
        defaultEventsShouldBeFound("vehicle.greaterThanOrEqual=" + DEFAULT_VEHICLE);

        // Get all the eventsList where vehicle is greater than or equal to UPDATED_VEHICLE
        defaultEventsShouldNotBeFound("vehicle.greaterThanOrEqual=" + UPDATED_VEHICLE);
    }

    @Test
    @Transactional
    void getAllEventsByVehicleIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        eventsRepository.saveAndFlush(events);

        // Get all the eventsList where vehicle is less than or equal to DEFAULT_VEHICLE
        defaultEventsShouldBeFound("vehicle.lessThanOrEqual=" + DEFAULT_VEHICLE);

        // Get all the eventsList where vehicle is less than or equal to SMALLER_VEHICLE
        defaultEventsShouldNotBeFound("vehicle.lessThanOrEqual=" + SMALLER_VEHICLE);
    }

    @Test
    @Transactional
    void getAllEventsByVehicleIsLessThanSomething() throws Exception {
        // Initialize the database
        eventsRepository.saveAndFlush(events);

        // Get all the eventsList where vehicle is less than DEFAULT_VEHICLE
        defaultEventsShouldNotBeFound("vehicle.lessThan=" + DEFAULT_VEHICLE);

        // Get all the eventsList where vehicle is less than UPDATED_VEHICLE
        defaultEventsShouldBeFound("vehicle.lessThan=" + UPDATED_VEHICLE);
    }

    @Test
    @Transactional
    void getAllEventsByVehicleIsGreaterThanSomething() throws Exception {
        // Initialize the database
        eventsRepository.saveAndFlush(events);

        // Get all the eventsList where vehicle is greater than DEFAULT_VEHICLE
        defaultEventsShouldNotBeFound("vehicle.greaterThan=" + DEFAULT_VEHICLE);

        // Get all the eventsList where vehicle is greater than SMALLER_VEHICLE
        defaultEventsShouldBeFound("vehicle.greaterThan=" + SMALLER_VEHICLE);
    }

    @Test
    @Transactional
    void getAllEventsByIssueIsEqualToSomething() throws Exception {
        // Initialize the database
        eventsRepository.saveAndFlush(events);

        // Get all the eventsList where issue equals to DEFAULT_ISSUE
        defaultEventsShouldBeFound("issue.equals=" + DEFAULT_ISSUE);

        // Get all the eventsList where issue equals to UPDATED_ISSUE
        defaultEventsShouldNotBeFound("issue.equals=" + UPDATED_ISSUE);
    }

    @Test
    @Transactional
    void getAllEventsByIssueIsInShouldWork() throws Exception {
        // Initialize the database
        eventsRepository.saveAndFlush(events);

        // Get all the eventsList where issue in DEFAULT_ISSUE or UPDATED_ISSUE
        defaultEventsShouldBeFound("issue.in=" + DEFAULT_ISSUE + "," + UPDATED_ISSUE);

        // Get all the eventsList where issue equals to UPDATED_ISSUE
        defaultEventsShouldNotBeFound("issue.in=" + UPDATED_ISSUE);
    }

    @Test
    @Transactional
    void getAllEventsByIssueIsNullOrNotNull() throws Exception {
        // Initialize the database
        eventsRepository.saveAndFlush(events);

        // Get all the eventsList where issue is not null
        defaultEventsShouldBeFound("issue.specified=true");

        // Get all the eventsList where issue is null
        defaultEventsShouldNotBeFound("issue.specified=false");
    }

    @Test
    @Transactional
    void getAllEventsByIssueContainsSomething() throws Exception {
        // Initialize the database
        eventsRepository.saveAndFlush(events);

        // Get all the eventsList where issue contains DEFAULT_ISSUE
        defaultEventsShouldBeFound("issue.contains=" + DEFAULT_ISSUE);

        // Get all the eventsList where issue contains UPDATED_ISSUE
        defaultEventsShouldNotBeFound("issue.contains=" + UPDATED_ISSUE);
    }

    @Test
    @Transactional
    void getAllEventsByIssueNotContainsSomething() throws Exception {
        // Initialize the database
        eventsRepository.saveAndFlush(events);

        // Get all the eventsList where issue does not contain DEFAULT_ISSUE
        defaultEventsShouldNotBeFound("issue.doesNotContain=" + DEFAULT_ISSUE);

        // Get all the eventsList where issue does not contain UPDATED_ISSUE
        defaultEventsShouldBeFound("issue.doesNotContain=" + UPDATED_ISSUE);
    }

    @Test
    @Transactional
    void getAllEventsByStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        eventsRepository.saveAndFlush(events);

        // Get all the eventsList where status equals to DEFAULT_STATUS
        defaultEventsShouldBeFound("status.equals=" + DEFAULT_STATUS);

        // Get all the eventsList where status equals to UPDATED_STATUS
        defaultEventsShouldNotBeFound("status.equals=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllEventsByStatusIsInShouldWork() throws Exception {
        // Initialize the database
        eventsRepository.saveAndFlush(events);

        // Get all the eventsList where status in DEFAULT_STATUS or UPDATED_STATUS
        defaultEventsShouldBeFound("status.in=" + DEFAULT_STATUS + "," + UPDATED_STATUS);

        // Get all the eventsList where status equals to UPDATED_STATUS
        defaultEventsShouldNotBeFound("status.in=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllEventsByStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        eventsRepository.saveAndFlush(events);

        // Get all the eventsList where status is not null
        defaultEventsShouldBeFound("status.specified=true");

        // Get all the eventsList where status is null
        defaultEventsShouldNotBeFound("status.specified=false");
    }

    @Test
    @Transactional
    void getAllEventsByStatusContainsSomething() throws Exception {
        // Initialize the database
        eventsRepository.saveAndFlush(events);

        // Get all the eventsList where status contains DEFAULT_STATUS
        defaultEventsShouldBeFound("status.contains=" + DEFAULT_STATUS);

        // Get all the eventsList where status contains UPDATED_STATUS
        defaultEventsShouldNotBeFound("status.contains=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllEventsByStatusNotContainsSomething() throws Exception {
        // Initialize the database
        eventsRepository.saveAndFlush(events);

        // Get all the eventsList where status does not contain DEFAULT_STATUS
        defaultEventsShouldNotBeFound("status.doesNotContain=" + DEFAULT_STATUS);

        // Get all the eventsList where status does not contain UPDATED_STATUS
        defaultEventsShouldBeFound("status.doesNotContain=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllEventsByHandledByIsEqualToSomething() throws Exception {
        // Initialize the database
        eventsRepository.saveAndFlush(events);

        // Get all the eventsList where handledBy equals to DEFAULT_HANDLED_BY
        defaultEventsShouldBeFound("handledBy.equals=" + DEFAULT_HANDLED_BY);

        // Get all the eventsList where handledBy equals to UPDATED_HANDLED_BY
        defaultEventsShouldNotBeFound("handledBy.equals=" + UPDATED_HANDLED_BY);
    }

    @Test
    @Transactional
    void getAllEventsByHandledByIsInShouldWork() throws Exception {
        // Initialize the database
        eventsRepository.saveAndFlush(events);

        // Get all the eventsList where handledBy in DEFAULT_HANDLED_BY or UPDATED_HANDLED_BY
        defaultEventsShouldBeFound("handledBy.in=" + DEFAULT_HANDLED_BY + "," + UPDATED_HANDLED_BY);

        // Get all the eventsList where handledBy equals to UPDATED_HANDLED_BY
        defaultEventsShouldNotBeFound("handledBy.in=" + UPDATED_HANDLED_BY);
    }

    @Test
    @Transactional
    void getAllEventsByHandledByIsNullOrNotNull() throws Exception {
        // Initialize the database
        eventsRepository.saveAndFlush(events);

        // Get all the eventsList where handledBy is not null
        defaultEventsShouldBeFound("handledBy.specified=true");

        // Get all the eventsList where handledBy is null
        defaultEventsShouldNotBeFound("handledBy.specified=false");
    }

    @Test
    @Transactional
    void getAllEventsByHandledByIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        eventsRepository.saveAndFlush(events);

        // Get all the eventsList where handledBy is greater than or equal to DEFAULT_HANDLED_BY
        defaultEventsShouldBeFound("handledBy.greaterThanOrEqual=" + DEFAULT_HANDLED_BY);

        // Get all the eventsList where handledBy is greater than or equal to UPDATED_HANDLED_BY
        defaultEventsShouldNotBeFound("handledBy.greaterThanOrEqual=" + UPDATED_HANDLED_BY);
    }

    @Test
    @Transactional
    void getAllEventsByHandledByIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        eventsRepository.saveAndFlush(events);

        // Get all the eventsList where handledBy is less than or equal to DEFAULT_HANDLED_BY
        defaultEventsShouldBeFound("handledBy.lessThanOrEqual=" + DEFAULT_HANDLED_BY);

        // Get all the eventsList where handledBy is less than or equal to SMALLER_HANDLED_BY
        defaultEventsShouldNotBeFound("handledBy.lessThanOrEqual=" + SMALLER_HANDLED_BY);
    }

    @Test
    @Transactional
    void getAllEventsByHandledByIsLessThanSomething() throws Exception {
        // Initialize the database
        eventsRepository.saveAndFlush(events);

        // Get all the eventsList where handledBy is less than DEFAULT_HANDLED_BY
        defaultEventsShouldNotBeFound("handledBy.lessThan=" + DEFAULT_HANDLED_BY);

        // Get all the eventsList where handledBy is less than UPDATED_HANDLED_BY
        defaultEventsShouldBeFound("handledBy.lessThan=" + UPDATED_HANDLED_BY);
    }

    @Test
    @Transactional
    void getAllEventsByHandledByIsGreaterThanSomething() throws Exception {
        // Initialize the database
        eventsRepository.saveAndFlush(events);

        // Get all the eventsList where handledBy is greater than DEFAULT_HANDLED_BY
        defaultEventsShouldNotBeFound("handledBy.greaterThan=" + DEFAULT_HANDLED_BY);

        // Get all the eventsList where handledBy is greater than SMALLER_HANDLED_BY
        defaultEventsShouldBeFound("handledBy.greaterThan=" + SMALLER_HANDLED_BY);
    }

    @Test
    @Transactional
    void getAllEventsByGantryProcessedIsEqualToSomething() throws Exception {
        // Initialize the database
        eventsRepository.saveAndFlush(events);

        // Get all the eventsList where gantryProcessed equals to DEFAULT_GANTRY_PROCESSED
        defaultEventsShouldBeFound("gantryProcessed.equals=" + DEFAULT_GANTRY_PROCESSED);

        // Get all the eventsList where gantryProcessed equals to UPDATED_GANTRY_PROCESSED
        defaultEventsShouldNotBeFound("gantryProcessed.equals=" + UPDATED_GANTRY_PROCESSED);
    }

    @Test
    @Transactional
    void getAllEventsByGantryProcessedIsInShouldWork() throws Exception {
        // Initialize the database
        eventsRepository.saveAndFlush(events);

        // Get all the eventsList where gantryProcessed in DEFAULT_GANTRY_PROCESSED or UPDATED_GANTRY_PROCESSED
        defaultEventsShouldBeFound("gantryProcessed.in=" + DEFAULT_GANTRY_PROCESSED + "," + UPDATED_GANTRY_PROCESSED);

        // Get all the eventsList where gantryProcessed equals to UPDATED_GANTRY_PROCESSED
        defaultEventsShouldNotBeFound("gantryProcessed.in=" + UPDATED_GANTRY_PROCESSED);
    }

    @Test
    @Transactional
    void getAllEventsByGantryProcessedIsNullOrNotNull() throws Exception {
        // Initialize the database
        eventsRepository.saveAndFlush(events);

        // Get all the eventsList where gantryProcessed is not null
        defaultEventsShouldBeFound("gantryProcessed.specified=true");

        // Get all the eventsList where gantryProcessed is null
        defaultEventsShouldNotBeFound("gantryProcessed.specified=false");
    }

    @Test
    @Transactional
    void getAllEventsByGantryProcessedIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        eventsRepository.saveAndFlush(events);

        // Get all the eventsList where gantryProcessed is greater than or equal to DEFAULT_GANTRY_PROCESSED
        defaultEventsShouldBeFound("gantryProcessed.greaterThanOrEqual=" + DEFAULT_GANTRY_PROCESSED);

        // Get all the eventsList where gantryProcessed is greater than or equal to UPDATED_GANTRY_PROCESSED
        defaultEventsShouldNotBeFound("gantryProcessed.greaterThanOrEqual=" + UPDATED_GANTRY_PROCESSED);
    }

    @Test
    @Transactional
    void getAllEventsByGantryProcessedIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        eventsRepository.saveAndFlush(events);

        // Get all the eventsList where gantryProcessed is less than or equal to DEFAULT_GANTRY_PROCESSED
        defaultEventsShouldBeFound("gantryProcessed.lessThanOrEqual=" + DEFAULT_GANTRY_PROCESSED);

        // Get all the eventsList where gantryProcessed is less than or equal to SMALLER_GANTRY_PROCESSED
        defaultEventsShouldNotBeFound("gantryProcessed.lessThanOrEqual=" + SMALLER_GANTRY_PROCESSED);
    }

    @Test
    @Transactional
    void getAllEventsByGantryProcessedIsLessThanSomething() throws Exception {
        // Initialize the database
        eventsRepository.saveAndFlush(events);

        // Get all the eventsList where gantryProcessed is less than DEFAULT_GANTRY_PROCESSED
        defaultEventsShouldNotBeFound("gantryProcessed.lessThan=" + DEFAULT_GANTRY_PROCESSED);

        // Get all the eventsList where gantryProcessed is less than UPDATED_GANTRY_PROCESSED
        defaultEventsShouldBeFound("gantryProcessed.lessThan=" + UPDATED_GANTRY_PROCESSED);
    }

    @Test
    @Transactional
    void getAllEventsByGantryProcessedIsGreaterThanSomething() throws Exception {
        // Initialize the database
        eventsRepository.saveAndFlush(events);

        // Get all the eventsList where gantryProcessed is greater than DEFAULT_GANTRY_PROCESSED
        defaultEventsShouldNotBeFound("gantryProcessed.greaterThan=" + DEFAULT_GANTRY_PROCESSED);

        // Get all the eventsList where gantryProcessed is greater than SMALLER_GANTRY_PROCESSED
        defaultEventsShouldBeFound("gantryProcessed.greaterThan=" + SMALLER_GANTRY_PROCESSED);
    }

    @Test
    @Transactional
    void getAllEventsByGantrySentIsEqualToSomething() throws Exception {
        // Initialize the database
        eventsRepository.saveAndFlush(events);

        // Get all the eventsList where gantrySent equals to DEFAULT_GANTRY_SENT
        defaultEventsShouldBeFound("gantrySent.equals=" + DEFAULT_GANTRY_SENT);

        // Get all the eventsList where gantrySent equals to UPDATED_GANTRY_SENT
        defaultEventsShouldNotBeFound("gantrySent.equals=" + UPDATED_GANTRY_SENT);
    }

    @Test
    @Transactional
    void getAllEventsByGantrySentIsInShouldWork() throws Exception {
        // Initialize the database
        eventsRepository.saveAndFlush(events);

        // Get all the eventsList where gantrySent in DEFAULT_GANTRY_SENT or UPDATED_GANTRY_SENT
        defaultEventsShouldBeFound("gantrySent.in=" + DEFAULT_GANTRY_SENT + "," + UPDATED_GANTRY_SENT);

        // Get all the eventsList where gantrySent equals to UPDATED_GANTRY_SENT
        defaultEventsShouldNotBeFound("gantrySent.in=" + UPDATED_GANTRY_SENT);
    }

    @Test
    @Transactional
    void getAllEventsByGantrySentIsNullOrNotNull() throws Exception {
        // Initialize the database
        eventsRepository.saveAndFlush(events);

        // Get all the eventsList where gantrySent is not null
        defaultEventsShouldBeFound("gantrySent.specified=true");

        // Get all the eventsList where gantrySent is null
        defaultEventsShouldNotBeFound("gantrySent.specified=false");
    }

    @Test
    @Transactional
    void getAllEventsByGantrySentIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        eventsRepository.saveAndFlush(events);

        // Get all the eventsList where gantrySent is greater than or equal to DEFAULT_GANTRY_SENT
        defaultEventsShouldBeFound("gantrySent.greaterThanOrEqual=" + DEFAULT_GANTRY_SENT);

        // Get all the eventsList where gantrySent is greater than or equal to UPDATED_GANTRY_SENT
        defaultEventsShouldNotBeFound("gantrySent.greaterThanOrEqual=" + UPDATED_GANTRY_SENT);
    }

    @Test
    @Transactional
    void getAllEventsByGantrySentIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        eventsRepository.saveAndFlush(events);

        // Get all the eventsList where gantrySent is less than or equal to DEFAULT_GANTRY_SENT
        defaultEventsShouldBeFound("gantrySent.lessThanOrEqual=" + DEFAULT_GANTRY_SENT);

        // Get all the eventsList where gantrySent is less than or equal to SMALLER_GANTRY_SENT
        defaultEventsShouldNotBeFound("gantrySent.lessThanOrEqual=" + SMALLER_GANTRY_SENT);
    }

    @Test
    @Transactional
    void getAllEventsByGantrySentIsLessThanSomething() throws Exception {
        // Initialize the database
        eventsRepository.saveAndFlush(events);

        // Get all the eventsList where gantrySent is less than DEFAULT_GANTRY_SENT
        defaultEventsShouldNotBeFound("gantrySent.lessThan=" + DEFAULT_GANTRY_SENT);

        // Get all the eventsList where gantrySent is less than UPDATED_GANTRY_SENT
        defaultEventsShouldBeFound("gantrySent.lessThan=" + UPDATED_GANTRY_SENT);
    }

    @Test
    @Transactional
    void getAllEventsByGantrySentIsGreaterThanSomething() throws Exception {
        // Initialize the database
        eventsRepository.saveAndFlush(events);

        // Get all the eventsList where gantrySent is greater than DEFAULT_GANTRY_SENT
        defaultEventsShouldNotBeFound("gantrySent.greaterThan=" + DEFAULT_GANTRY_SENT);

        // Get all the eventsList where gantrySent is greater than SMALLER_GANTRY_SENT
        defaultEventsShouldBeFound("gantrySent.greaterThan=" + SMALLER_GANTRY_SENT);
    }

    @Test
    @Transactional
    void getAllEventsByWhenIsEqualToSomething() throws Exception {
        // Initialize the database
        eventsRepository.saveAndFlush(events);

        // Get all the eventsList where when equals to DEFAULT_WHEN
        defaultEventsShouldBeFound("when.equals=" + DEFAULT_WHEN);

        // Get all the eventsList where when equals to UPDATED_WHEN
        defaultEventsShouldNotBeFound("when.equals=" + UPDATED_WHEN);
    }

    @Test
    @Transactional
    void getAllEventsByWhenIsInShouldWork() throws Exception {
        // Initialize the database
        eventsRepository.saveAndFlush(events);

        // Get all the eventsList where when in DEFAULT_WHEN or UPDATED_WHEN
        defaultEventsShouldBeFound("when.in=" + DEFAULT_WHEN + "," + UPDATED_WHEN);

        // Get all the eventsList where when equals to UPDATED_WHEN
        defaultEventsShouldNotBeFound("when.in=" + UPDATED_WHEN);
    }

    @Test
    @Transactional
    void getAllEventsByWhenIsNullOrNotNull() throws Exception {
        // Initialize the database
        eventsRepository.saveAndFlush(events);

        // Get all the eventsList where when is not null
        defaultEventsShouldBeFound("when.specified=true");

        // Get all the eventsList where when is null
        defaultEventsShouldNotBeFound("when.specified=false");
    }

    @Test
    @Transactional
    void getAllEventsByWhenIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        eventsRepository.saveAndFlush(events);

        // Get all the eventsList where when is greater than or equal to DEFAULT_WHEN
        defaultEventsShouldBeFound("when.greaterThanOrEqual=" + DEFAULT_WHEN);

        // Get all the eventsList where when is greater than or equal to UPDATED_WHEN
        defaultEventsShouldNotBeFound("when.greaterThanOrEqual=" + UPDATED_WHEN);
    }

    @Test
    @Transactional
    void getAllEventsByWhenIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        eventsRepository.saveAndFlush(events);

        // Get all the eventsList where when is less than or equal to DEFAULT_WHEN
        defaultEventsShouldBeFound("when.lessThanOrEqual=" + DEFAULT_WHEN);

        // Get all the eventsList where when is less than or equal to SMALLER_WHEN
        defaultEventsShouldNotBeFound("when.lessThanOrEqual=" + SMALLER_WHEN);
    }

    @Test
    @Transactional
    void getAllEventsByWhenIsLessThanSomething() throws Exception {
        // Initialize the database
        eventsRepository.saveAndFlush(events);

        // Get all the eventsList where when is less than DEFAULT_WHEN
        defaultEventsShouldNotBeFound("when.lessThan=" + DEFAULT_WHEN);

        // Get all the eventsList where when is less than UPDATED_WHEN
        defaultEventsShouldBeFound("when.lessThan=" + UPDATED_WHEN);
    }

    @Test
    @Transactional
    void getAllEventsByWhenIsGreaterThanSomething() throws Exception {
        // Initialize the database
        eventsRepository.saveAndFlush(events);

        // Get all the eventsList where when is greater than DEFAULT_WHEN
        defaultEventsShouldNotBeFound("when.greaterThan=" + DEFAULT_WHEN);

        // Get all the eventsList where when is greater than SMALLER_WHEN
        defaultEventsShouldBeFound("when.greaterThan=" + SMALLER_WHEN);
    }

    @Test
    @Transactional
    void getAllEventsByTollIsEqualToSomething() throws Exception {
        // Initialize the database
        eventsRepository.saveAndFlush(events);

        // Get all the eventsList where toll equals to DEFAULT_TOLL
        defaultEventsShouldBeFound("toll.equals=" + DEFAULT_TOLL);

        // Get all the eventsList where toll equals to UPDATED_TOLL
        defaultEventsShouldNotBeFound("toll.equals=" + UPDATED_TOLL);
    }

    @Test
    @Transactional
    void getAllEventsByTollIsInShouldWork() throws Exception {
        // Initialize the database
        eventsRepository.saveAndFlush(events);

        // Get all the eventsList where toll in DEFAULT_TOLL or UPDATED_TOLL
        defaultEventsShouldBeFound("toll.in=" + DEFAULT_TOLL + "," + UPDATED_TOLL);

        // Get all the eventsList where toll equals to UPDATED_TOLL
        defaultEventsShouldNotBeFound("toll.in=" + UPDATED_TOLL);
    }

    @Test
    @Transactional
    void getAllEventsByTollIsNullOrNotNull() throws Exception {
        // Initialize the database
        eventsRepository.saveAndFlush(events);

        // Get all the eventsList where toll is not null
        defaultEventsShouldBeFound("toll.specified=true");

        // Get all the eventsList where toll is null
        defaultEventsShouldNotBeFound("toll.specified=false");
    }

    @Test
    @Transactional
    void getAllEventsByTollIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        eventsRepository.saveAndFlush(events);

        // Get all the eventsList where toll is greater than or equal to DEFAULT_TOLL
        defaultEventsShouldBeFound("toll.greaterThanOrEqual=" + DEFAULT_TOLL);

        // Get all the eventsList where toll is greater than or equal to UPDATED_TOLL
        defaultEventsShouldNotBeFound("toll.greaterThanOrEqual=" + UPDATED_TOLL);
    }

    @Test
    @Transactional
    void getAllEventsByTollIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        eventsRepository.saveAndFlush(events);

        // Get all the eventsList where toll is less than or equal to DEFAULT_TOLL
        defaultEventsShouldBeFound("toll.lessThanOrEqual=" + DEFAULT_TOLL);

        // Get all the eventsList where toll is less than or equal to SMALLER_TOLL
        defaultEventsShouldNotBeFound("toll.lessThanOrEqual=" + SMALLER_TOLL);
    }

    @Test
    @Transactional
    void getAllEventsByTollIsLessThanSomething() throws Exception {
        // Initialize the database
        eventsRepository.saveAndFlush(events);

        // Get all the eventsList where toll is less than DEFAULT_TOLL
        defaultEventsShouldNotBeFound("toll.lessThan=" + DEFAULT_TOLL);

        // Get all the eventsList where toll is less than UPDATED_TOLL
        defaultEventsShouldBeFound("toll.lessThan=" + UPDATED_TOLL);
    }

    @Test
    @Transactional
    void getAllEventsByTollIsGreaterThanSomething() throws Exception {
        // Initialize the database
        eventsRepository.saveAndFlush(events);

        // Get all the eventsList where toll is greater than DEFAULT_TOLL
        defaultEventsShouldNotBeFound("toll.greaterThan=" + DEFAULT_TOLL);

        // Get all the eventsList where toll is greater than SMALLER_TOLL
        defaultEventsShouldBeFound("toll.greaterThan=" + SMALLER_TOLL);
    }

    @Test
    @Transactional
    void getAllEventsByRuleRcvdIsEqualToSomething() throws Exception {
        // Initialize the database
        eventsRepository.saveAndFlush(events);

        // Get all the eventsList where ruleRcvd equals to DEFAULT_RULE_RCVD
        defaultEventsShouldBeFound("ruleRcvd.equals=" + DEFAULT_RULE_RCVD);

        // Get all the eventsList where ruleRcvd equals to UPDATED_RULE_RCVD
        defaultEventsShouldNotBeFound("ruleRcvd.equals=" + UPDATED_RULE_RCVD);
    }

    @Test
    @Transactional
    void getAllEventsByRuleRcvdIsInShouldWork() throws Exception {
        // Initialize the database
        eventsRepository.saveAndFlush(events);

        // Get all the eventsList where ruleRcvd in DEFAULT_RULE_RCVD or UPDATED_RULE_RCVD
        defaultEventsShouldBeFound("ruleRcvd.in=" + DEFAULT_RULE_RCVD + "," + UPDATED_RULE_RCVD);

        // Get all the eventsList where ruleRcvd equals to UPDATED_RULE_RCVD
        defaultEventsShouldNotBeFound("ruleRcvd.in=" + UPDATED_RULE_RCVD);
    }

    @Test
    @Transactional
    void getAllEventsByRuleRcvdIsNullOrNotNull() throws Exception {
        // Initialize the database
        eventsRepository.saveAndFlush(events);

        // Get all the eventsList where ruleRcvd is not null
        defaultEventsShouldBeFound("ruleRcvd.specified=true");

        // Get all the eventsList where ruleRcvd is null
        defaultEventsShouldNotBeFound("ruleRcvd.specified=false");
    }

    @Test
    @Transactional
    void getAllEventsByRuleRcvdIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        eventsRepository.saveAndFlush(events);

        // Get all the eventsList where ruleRcvd is greater than or equal to DEFAULT_RULE_RCVD
        defaultEventsShouldBeFound("ruleRcvd.greaterThanOrEqual=" + DEFAULT_RULE_RCVD);

        // Get all the eventsList where ruleRcvd is greater than or equal to UPDATED_RULE_RCVD
        defaultEventsShouldNotBeFound("ruleRcvd.greaterThanOrEqual=" + UPDATED_RULE_RCVD);
    }

    @Test
    @Transactional
    void getAllEventsByRuleRcvdIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        eventsRepository.saveAndFlush(events);

        // Get all the eventsList where ruleRcvd is less than or equal to DEFAULT_RULE_RCVD
        defaultEventsShouldBeFound("ruleRcvd.lessThanOrEqual=" + DEFAULT_RULE_RCVD);

        // Get all the eventsList where ruleRcvd is less than or equal to SMALLER_RULE_RCVD
        defaultEventsShouldNotBeFound("ruleRcvd.lessThanOrEqual=" + SMALLER_RULE_RCVD);
    }

    @Test
    @Transactional
    void getAllEventsByRuleRcvdIsLessThanSomething() throws Exception {
        // Initialize the database
        eventsRepository.saveAndFlush(events);

        // Get all the eventsList where ruleRcvd is less than DEFAULT_RULE_RCVD
        defaultEventsShouldNotBeFound("ruleRcvd.lessThan=" + DEFAULT_RULE_RCVD);

        // Get all the eventsList where ruleRcvd is less than UPDATED_RULE_RCVD
        defaultEventsShouldBeFound("ruleRcvd.lessThan=" + UPDATED_RULE_RCVD);
    }

    @Test
    @Transactional
    void getAllEventsByRuleRcvdIsGreaterThanSomething() throws Exception {
        // Initialize the database
        eventsRepository.saveAndFlush(events);

        // Get all the eventsList where ruleRcvd is greater than DEFAULT_RULE_RCVD
        defaultEventsShouldNotBeFound("ruleRcvd.greaterThan=" + DEFAULT_RULE_RCVD);

        // Get all the eventsList where ruleRcvd is greater than SMALLER_RULE_RCVD
        defaultEventsShouldBeFound("ruleRcvd.greaterThan=" + SMALLER_RULE_RCVD);
    }

    @Test
    @Transactional
    void getAllEventsByWantedForIsEqualToSomething() throws Exception {
        // Initialize the database
        eventsRepository.saveAndFlush(events);

        // Get all the eventsList where wantedFor equals to DEFAULT_WANTED_FOR
        defaultEventsShouldBeFound("wantedFor.equals=" + DEFAULT_WANTED_FOR);

        // Get all the eventsList where wantedFor equals to UPDATED_WANTED_FOR
        defaultEventsShouldNotBeFound("wantedFor.equals=" + UPDATED_WANTED_FOR);
    }

    @Test
    @Transactional
    void getAllEventsByWantedForIsInShouldWork() throws Exception {
        // Initialize the database
        eventsRepository.saveAndFlush(events);

        // Get all the eventsList where wantedFor in DEFAULT_WANTED_FOR or UPDATED_WANTED_FOR
        defaultEventsShouldBeFound("wantedFor.in=" + DEFAULT_WANTED_FOR + "," + UPDATED_WANTED_FOR);

        // Get all the eventsList where wantedFor equals to UPDATED_WANTED_FOR
        defaultEventsShouldNotBeFound("wantedFor.in=" + UPDATED_WANTED_FOR);
    }

    @Test
    @Transactional
    void getAllEventsByWantedForIsNullOrNotNull() throws Exception {
        // Initialize the database
        eventsRepository.saveAndFlush(events);

        // Get all the eventsList where wantedFor is not null
        defaultEventsShouldBeFound("wantedFor.specified=true");

        // Get all the eventsList where wantedFor is null
        defaultEventsShouldNotBeFound("wantedFor.specified=false");
    }

    @Test
    @Transactional
    void getAllEventsByWantedForContainsSomething() throws Exception {
        // Initialize the database
        eventsRepository.saveAndFlush(events);

        // Get all the eventsList where wantedFor contains DEFAULT_WANTED_FOR
        defaultEventsShouldBeFound("wantedFor.contains=" + DEFAULT_WANTED_FOR);

        // Get all the eventsList where wantedFor contains UPDATED_WANTED_FOR
        defaultEventsShouldNotBeFound("wantedFor.contains=" + UPDATED_WANTED_FOR);
    }

    @Test
    @Transactional
    void getAllEventsByWantedForNotContainsSomething() throws Exception {
        // Initialize the database
        eventsRepository.saveAndFlush(events);

        // Get all the eventsList where wantedFor does not contain DEFAULT_WANTED_FOR
        defaultEventsShouldNotBeFound("wantedFor.doesNotContain=" + DEFAULT_WANTED_FOR);

        // Get all the eventsList where wantedFor does not contain UPDATED_WANTED_FOR
        defaultEventsShouldBeFound("wantedFor.doesNotContain=" + UPDATED_WANTED_FOR);
    }

    @Test
    @Transactional
    void getAllEventsByFineIsEqualToSomething() throws Exception {
        // Initialize the database
        eventsRepository.saveAndFlush(events);

        // Get all the eventsList where fine equals to DEFAULT_FINE
        defaultEventsShouldBeFound("fine.equals=" + DEFAULT_FINE);

        // Get all the eventsList where fine equals to UPDATED_FINE
        defaultEventsShouldNotBeFound("fine.equals=" + UPDATED_FINE);
    }

    @Test
    @Transactional
    void getAllEventsByFineIsInShouldWork() throws Exception {
        // Initialize the database
        eventsRepository.saveAndFlush(events);

        // Get all the eventsList where fine in DEFAULT_FINE or UPDATED_FINE
        defaultEventsShouldBeFound("fine.in=" + DEFAULT_FINE + "," + UPDATED_FINE);

        // Get all the eventsList where fine equals to UPDATED_FINE
        defaultEventsShouldNotBeFound("fine.in=" + UPDATED_FINE);
    }

    @Test
    @Transactional
    void getAllEventsByFineIsNullOrNotNull() throws Exception {
        // Initialize the database
        eventsRepository.saveAndFlush(events);

        // Get all the eventsList where fine is not null
        defaultEventsShouldBeFound("fine.specified=true");

        // Get all the eventsList where fine is null
        defaultEventsShouldNotBeFound("fine.specified=false");
    }

    @Test
    @Transactional
    void getAllEventsByFineIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        eventsRepository.saveAndFlush(events);

        // Get all the eventsList where fine is greater than or equal to DEFAULT_FINE
        defaultEventsShouldBeFound("fine.greaterThanOrEqual=" + DEFAULT_FINE);

        // Get all the eventsList where fine is greater than or equal to UPDATED_FINE
        defaultEventsShouldNotBeFound("fine.greaterThanOrEqual=" + UPDATED_FINE);
    }

    @Test
    @Transactional
    void getAllEventsByFineIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        eventsRepository.saveAndFlush(events);

        // Get all the eventsList where fine is less than or equal to DEFAULT_FINE
        defaultEventsShouldBeFound("fine.lessThanOrEqual=" + DEFAULT_FINE);

        // Get all the eventsList where fine is less than or equal to SMALLER_FINE
        defaultEventsShouldNotBeFound("fine.lessThanOrEqual=" + SMALLER_FINE);
    }

    @Test
    @Transactional
    void getAllEventsByFineIsLessThanSomething() throws Exception {
        // Initialize the database
        eventsRepository.saveAndFlush(events);

        // Get all the eventsList where fine is less than DEFAULT_FINE
        defaultEventsShouldNotBeFound("fine.lessThan=" + DEFAULT_FINE);

        // Get all the eventsList where fine is less than UPDATED_FINE
        defaultEventsShouldBeFound("fine.lessThan=" + UPDATED_FINE);
    }

    @Test
    @Transactional
    void getAllEventsByFineIsGreaterThanSomething() throws Exception {
        // Initialize the database
        eventsRepository.saveAndFlush(events);

        // Get all the eventsList where fine is greater than DEFAULT_FINE
        defaultEventsShouldNotBeFound("fine.greaterThan=" + DEFAULT_FINE);

        // Get all the eventsList where fine is greater than SMALLER_FINE
        defaultEventsShouldBeFound("fine.greaterThan=" + SMALLER_FINE);
    }

    @Test
    @Transactional
    void getAllEventsByLicenseIssueIsEqualToSomething() throws Exception {
        // Initialize the database
        eventsRepository.saveAndFlush(events);

        // Get all the eventsList where licenseIssue equals to DEFAULT_LICENSE_ISSUE
        defaultEventsShouldBeFound("licenseIssue.equals=" + DEFAULT_LICENSE_ISSUE);

        // Get all the eventsList where licenseIssue equals to UPDATED_LICENSE_ISSUE
        defaultEventsShouldNotBeFound("licenseIssue.equals=" + UPDATED_LICENSE_ISSUE);
    }

    @Test
    @Transactional
    void getAllEventsByLicenseIssueIsInShouldWork() throws Exception {
        // Initialize the database
        eventsRepository.saveAndFlush(events);

        // Get all the eventsList where licenseIssue in DEFAULT_LICENSE_ISSUE or UPDATED_LICENSE_ISSUE
        defaultEventsShouldBeFound("licenseIssue.in=" + DEFAULT_LICENSE_ISSUE + "," + UPDATED_LICENSE_ISSUE);

        // Get all the eventsList where licenseIssue equals to UPDATED_LICENSE_ISSUE
        defaultEventsShouldNotBeFound("licenseIssue.in=" + UPDATED_LICENSE_ISSUE);
    }

    @Test
    @Transactional
    void getAllEventsByLicenseIssueIsNullOrNotNull() throws Exception {
        // Initialize the database
        eventsRepository.saveAndFlush(events);

        // Get all the eventsList where licenseIssue is not null
        defaultEventsShouldBeFound("licenseIssue.specified=true");

        // Get all the eventsList where licenseIssue is null
        defaultEventsShouldNotBeFound("licenseIssue.specified=false");
    }

    @Test
    @Transactional
    void getAllEventsByLicenseIssueContainsSomething() throws Exception {
        // Initialize the database
        eventsRepository.saveAndFlush(events);

        // Get all the eventsList where licenseIssue contains DEFAULT_LICENSE_ISSUE
        defaultEventsShouldBeFound("licenseIssue.contains=" + DEFAULT_LICENSE_ISSUE);

        // Get all the eventsList where licenseIssue contains UPDATED_LICENSE_ISSUE
        defaultEventsShouldNotBeFound("licenseIssue.contains=" + UPDATED_LICENSE_ISSUE);
    }

    @Test
    @Transactional
    void getAllEventsByLicenseIssueNotContainsSomething() throws Exception {
        // Initialize the database
        eventsRepository.saveAndFlush(events);

        // Get all the eventsList where licenseIssue does not contain DEFAULT_LICENSE_ISSUE
        defaultEventsShouldNotBeFound("licenseIssue.doesNotContain=" + DEFAULT_LICENSE_ISSUE);

        // Get all the eventsList where licenseIssue does not contain UPDATED_LICENSE_ISSUE
        defaultEventsShouldBeFound("licenseIssue.doesNotContain=" + UPDATED_LICENSE_ISSUE);
    }

    @Test
    @Transactional
    void getAllEventsByWantedByIsEqualToSomething() throws Exception {
        // Initialize the database
        eventsRepository.saveAndFlush(events);

        // Get all the eventsList where wantedBy equals to DEFAULT_WANTED_BY
        defaultEventsShouldBeFound("wantedBy.equals=" + DEFAULT_WANTED_BY);

        // Get all the eventsList where wantedBy equals to UPDATED_WANTED_BY
        defaultEventsShouldNotBeFound("wantedBy.equals=" + UPDATED_WANTED_BY);
    }

    @Test
    @Transactional
    void getAllEventsByWantedByIsInShouldWork() throws Exception {
        // Initialize the database
        eventsRepository.saveAndFlush(events);

        // Get all the eventsList where wantedBy in DEFAULT_WANTED_BY or UPDATED_WANTED_BY
        defaultEventsShouldBeFound("wantedBy.in=" + DEFAULT_WANTED_BY + "," + UPDATED_WANTED_BY);

        // Get all the eventsList where wantedBy equals to UPDATED_WANTED_BY
        defaultEventsShouldNotBeFound("wantedBy.in=" + UPDATED_WANTED_BY);
    }

    @Test
    @Transactional
    void getAllEventsByWantedByIsNullOrNotNull() throws Exception {
        // Initialize the database
        eventsRepository.saveAndFlush(events);

        // Get all the eventsList where wantedBy is not null
        defaultEventsShouldBeFound("wantedBy.specified=true");

        // Get all the eventsList where wantedBy is null
        defaultEventsShouldNotBeFound("wantedBy.specified=false");
    }

    @Test
    @Transactional
    void getAllEventsByWantedByIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        eventsRepository.saveAndFlush(events);

        // Get all the eventsList where wantedBy is greater than or equal to DEFAULT_WANTED_BY
        defaultEventsShouldBeFound("wantedBy.greaterThanOrEqual=" + DEFAULT_WANTED_BY);

        // Get all the eventsList where wantedBy is greater than or equal to UPDATED_WANTED_BY
        defaultEventsShouldNotBeFound("wantedBy.greaterThanOrEqual=" + UPDATED_WANTED_BY);
    }

    @Test
    @Transactional
    void getAllEventsByWantedByIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        eventsRepository.saveAndFlush(events);

        // Get all the eventsList where wantedBy is less than or equal to DEFAULT_WANTED_BY
        defaultEventsShouldBeFound("wantedBy.lessThanOrEqual=" + DEFAULT_WANTED_BY);

        // Get all the eventsList where wantedBy is less than or equal to SMALLER_WANTED_BY
        defaultEventsShouldNotBeFound("wantedBy.lessThanOrEqual=" + SMALLER_WANTED_BY);
    }

    @Test
    @Transactional
    void getAllEventsByWantedByIsLessThanSomething() throws Exception {
        // Initialize the database
        eventsRepository.saveAndFlush(events);

        // Get all the eventsList where wantedBy is less than DEFAULT_WANTED_BY
        defaultEventsShouldNotBeFound("wantedBy.lessThan=" + DEFAULT_WANTED_BY);

        // Get all the eventsList where wantedBy is less than UPDATED_WANTED_BY
        defaultEventsShouldBeFound("wantedBy.lessThan=" + UPDATED_WANTED_BY);
    }

    @Test
    @Transactional
    void getAllEventsByWantedByIsGreaterThanSomething() throws Exception {
        // Initialize the database
        eventsRepository.saveAndFlush(events);

        // Get all the eventsList where wantedBy is greater than DEFAULT_WANTED_BY
        defaultEventsShouldNotBeFound("wantedBy.greaterThan=" + DEFAULT_WANTED_BY);

        // Get all the eventsList where wantedBy is greater than SMALLER_WANTED_BY
        defaultEventsShouldBeFound("wantedBy.greaterThan=" + SMALLER_WANTED_BY);
    }

    @Test
    @Transactional
    void getAllEventsByRuleProcessedIsEqualToSomething() throws Exception {
        // Initialize the database
        eventsRepository.saveAndFlush(events);

        // Get all the eventsList where ruleProcessed equals to DEFAULT_RULE_PROCESSED
        defaultEventsShouldBeFound("ruleProcessed.equals=" + DEFAULT_RULE_PROCESSED);

        // Get all the eventsList where ruleProcessed equals to UPDATED_RULE_PROCESSED
        defaultEventsShouldNotBeFound("ruleProcessed.equals=" + UPDATED_RULE_PROCESSED);
    }

    @Test
    @Transactional
    void getAllEventsByRuleProcessedIsInShouldWork() throws Exception {
        // Initialize the database
        eventsRepository.saveAndFlush(events);

        // Get all the eventsList where ruleProcessed in DEFAULT_RULE_PROCESSED or UPDATED_RULE_PROCESSED
        defaultEventsShouldBeFound("ruleProcessed.in=" + DEFAULT_RULE_PROCESSED + "," + UPDATED_RULE_PROCESSED);

        // Get all the eventsList where ruleProcessed equals to UPDATED_RULE_PROCESSED
        defaultEventsShouldNotBeFound("ruleProcessed.in=" + UPDATED_RULE_PROCESSED);
    }

    @Test
    @Transactional
    void getAllEventsByRuleProcessedIsNullOrNotNull() throws Exception {
        // Initialize the database
        eventsRepository.saveAndFlush(events);

        // Get all the eventsList where ruleProcessed is not null
        defaultEventsShouldBeFound("ruleProcessed.specified=true");

        // Get all the eventsList where ruleProcessed is null
        defaultEventsShouldNotBeFound("ruleProcessed.specified=false");
    }

    @Test
    @Transactional
    void getAllEventsByRuleProcessedIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        eventsRepository.saveAndFlush(events);

        // Get all the eventsList where ruleProcessed is greater than or equal to DEFAULT_RULE_PROCESSED
        defaultEventsShouldBeFound("ruleProcessed.greaterThanOrEqual=" + DEFAULT_RULE_PROCESSED);

        // Get all the eventsList where ruleProcessed is greater than or equal to UPDATED_RULE_PROCESSED
        defaultEventsShouldNotBeFound("ruleProcessed.greaterThanOrEqual=" + UPDATED_RULE_PROCESSED);
    }

    @Test
    @Transactional
    void getAllEventsByRuleProcessedIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        eventsRepository.saveAndFlush(events);

        // Get all the eventsList where ruleProcessed is less than or equal to DEFAULT_RULE_PROCESSED
        defaultEventsShouldBeFound("ruleProcessed.lessThanOrEqual=" + DEFAULT_RULE_PROCESSED);

        // Get all the eventsList where ruleProcessed is less than or equal to SMALLER_RULE_PROCESSED
        defaultEventsShouldNotBeFound("ruleProcessed.lessThanOrEqual=" + SMALLER_RULE_PROCESSED);
    }

    @Test
    @Transactional
    void getAllEventsByRuleProcessedIsLessThanSomething() throws Exception {
        // Initialize the database
        eventsRepository.saveAndFlush(events);

        // Get all the eventsList where ruleProcessed is less than DEFAULT_RULE_PROCESSED
        defaultEventsShouldNotBeFound("ruleProcessed.lessThan=" + DEFAULT_RULE_PROCESSED);

        // Get all the eventsList where ruleProcessed is less than UPDATED_RULE_PROCESSED
        defaultEventsShouldBeFound("ruleProcessed.lessThan=" + UPDATED_RULE_PROCESSED);
    }

    @Test
    @Transactional
    void getAllEventsByRuleProcessedIsGreaterThanSomething() throws Exception {
        // Initialize the database
        eventsRepository.saveAndFlush(events);

        // Get all the eventsList where ruleProcessed is greater than DEFAULT_RULE_PROCESSED
        defaultEventsShouldNotBeFound("ruleProcessed.greaterThan=" + DEFAULT_RULE_PROCESSED);

        // Get all the eventsList where ruleProcessed is greater than SMALLER_RULE_PROCESSED
        defaultEventsShouldBeFound("ruleProcessed.greaterThan=" + SMALLER_RULE_PROCESSED);
    }

    @Test
    @Transactional
    void getAllEventsBySpeedFineIsEqualToSomething() throws Exception {
        // Initialize the database
        eventsRepository.saveAndFlush(events);

        // Get all the eventsList where speedFine equals to DEFAULT_SPEED_FINE
        defaultEventsShouldBeFound("speedFine.equals=" + DEFAULT_SPEED_FINE);

        // Get all the eventsList where speedFine equals to UPDATED_SPEED_FINE
        defaultEventsShouldNotBeFound("speedFine.equals=" + UPDATED_SPEED_FINE);
    }

    @Test
    @Transactional
    void getAllEventsBySpeedFineIsInShouldWork() throws Exception {
        // Initialize the database
        eventsRepository.saveAndFlush(events);

        // Get all the eventsList where speedFine in DEFAULT_SPEED_FINE or UPDATED_SPEED_FINE
        defaultEventsShouldBeFound("speedFine.in=" + DEFAULT_SPEED_FINE + "," + UPDATED_SPEED_FINE);

        // Get all the eventsList where speedFine equals to UPDATED_SPEED_FINE
        defaultEventsShouldNotBeFound("speedFine.in=" + UPDATED_SPEED_FINE);
    }

    @Test
    @Transactional
    void getAllEventsBySpeedFineIsNullOrNotNull() throws Exception {
        // Initialize the database
        eventsRepository.saveAndFlush(events);

        // Get all the eventsList where speedFine is not null
        defaultEventsShouldBeFound("speedFine.specified=true");

        // Get all the eventsList where speedFine is null
        defaultEventsShouldNotBeFound("speedFine.specified=false");
    }

    @Test
    @Transactional
    void getAllEventsBySpeedFineIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        eventsRepository.saveAndFlush(events);

        // Get all the eventsList where speedFine is greater than or equal to DEFAULT_SPEED_FINE
        defaultEventsShouldBeFound("speedFine.greaterThanOrEqual=" + DEFAULT_SPEED_FINE);

        // Get all the eventsList where speedFine is greater than or equal to UPDATED_SPEED_FINE
        defaultEventsShouldNotBeFound("speedFine.greaterThanOrEqual=" + UPDATED_SPEED_FINE);
    }

    @Test
    @Transactional
    void getAllEventsBySpeedFineIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        eventsRepository.saveAndFlush(events);

        // Get all the eventsList where speedFine is less than or equal to DEFAULT_SPEED_FINE
        defaultEventsShouldBeFound("speedFine.lessThanOrEqual=" + DEFAULT_SPEED_FINE);

        // Get all the eventsList where speedFine is less than or equal to SMALLER_SPEED_FINE
        defaultEventsShouldNotBeFound("speedFine.lessThanOrEqual=" + SMALLER_SPEED_FINE);
    }

    @Test
    @Transactional
    void getAllEventsBySpeedFineIsLessThanSomething() throws Exception {
        // Initialize the database
        eventsRepository.saveAndFlush(events);

        // Get all the eventsList where speedFine is less than DEFAULT_SPEED_FINE
        defaultEventsShouldNotBeFound("speedFine.lessThan=" + DEFAULT_SPEED_FINE);

        // Get all the eventsList where speedFine is less than UPDATED_SPEED_FINE
        defaultEventsShouldBeFound("speedFine.lessThan=" + UPDATED_SPEED_FINE);
    }

    @Test
    @Transactional
    void getAllEventsBySpeedFineIsGreaterThanSomething() throws Exception {
        // Initialize the database
        eventsRepository.saveAndFlush(events);

        // Get all the eventsList where speedFine is greater than DEFAULT_SPEED_FINE
        defaultEventsShouldNotBeFound("speedFine.greaterThan=" + DEFAULT_SPEED_FINE);

        // Get all the eventsList where speedFine is greater than SMALLER_SPEED_FINE
        defaultEventsShouldBeFound("speedFine.greaterThan=" + SMALLER_SPEED_FINE);
    }

    @Test
    @Transactional
    void getAllEventsByLaneIsEqualToSomething() throws Exception {
        // Initialize the database
        eventsRepository.saveAndFlush(events);

        // Get all the eventsList where lane equals to DEFAULT_LANE
        defaultEventsShouldBeFound("lane.equals=" + DEFAULT_LANE);

        // Get all the eventsList where lane equals to UPDATED_LANE
        defaultEventsShouldNotBeFound("lane.equals=" + UPDATED_LANE);
    }

    @Test
    @Transactional
    void getAllEventsByLaneIsInShouldWork() throws Exception {
        // Initialize the database
        eventsRepository.saveAndFlush(events);

        // Get all the eventsList where lane in DEFAULT_LANE or UPDATED_LANE
        defaultEventsShouldBeFound("lane.in=" + DEFAULT_LANE + "," + UPDATED_LANE);

        // Get all the eventsList where lane equals to UPDATED_LANE
        defaultEventsShouldNotBeFound("lane.in=" + UPDATED_LANE);
    }

    @Test
    @Transactional
    void getAllEventsByLaneIsNullOrNotNull() throws Exception {
        // Initialize the database
        eventsRepository.saveAndFlush(events);

        // Get all the eventsList where lane is not null
        defaultEventsShouldBeFound("lane.specified=true");

        // Get all the eventsList where lane is null
        defaultEventsShouldNotBeFound("lane.specified=false");
    }

    @Test
    @Transactional
    void getAllEventsByLaneIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        eventsRepository.saveAndFlush(events);

        // Get all the eventsList where lane is greater than or equal to DEFAULT_LANE
        defaultEventsShouldBeFound("lane.greaterThanOrEqual=" + DEFAULT_LANE);

        // Get all the eventsList where lane is greater than or equal to UPDATED_LANE
        defaultEventsShouldNotBeFound("lane.greaterThanOrEqual=" + UPDATED_LANE);
    }

    @Test
    @Transactional
    void getAllEventsByLaneIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        eventsRepository.saveAndFlush(events);

        // Get all the eventsList where lane is less than or equal to DEFAULT_LANE
        defaultEventsShouldBeFound("lane.lessThanOrEqual=" + DEFAULT_LANE);

        // Get all the eventsList where lane is less than or equal to SMALLER_LANE
        defaultEventsShouldNotBeFound("lane.lessThanOrEqual=" + SMALLER_LANE);
    }

    @Test
    @Transactional
    void getAllEventsByLaneIsLessThanSomething() throws Exception {
        // Initialize the database
        eventsRepository.saveAndFlush(events);

        // Get all the eventsList where lane is less than DEFAULT_LANE
        defaultEventsShouldNotBeFound("lane.lessThan=" + DEFAULT_LANE);

        // Get all the eventsList where lane is less than UPDATED_LANE
        defaultEventsShouldBeFound("lane.lessThan=" + UPDATED_LANE);
    }

    @Test
    @Transactional
    void getAllEventsByLaneIsGreaterThanSomething() throws Exception {
        // Initialize the database
        eventsRepository.saveAndFlush(events);

        // Get all the eventsList where lane is greater than DEFAULT_LANE
        defaultEventsShouldNotBeFound("lane.greaterThan=" + DEFAULT_LANE);

        // Get all the eventsList where lane is greater than SMALLER_LANE
        defaultEventsShouldBeFound("lane.greaterThan=" + SMALLER_LANE);
    }

    @Test
    @Transactional
    void getAllEventsByTagIssueIsEqualToSomething() throws Exception {
        // Initialize the database
        eventsRepository.saveAndFlush(events);

        // Get all the eventsList where tagIssue equals to DEFAULT_TAG_ISSUE
        defaultEventsShouldBeFound("tagIssue.equals=" + DEFAULT_TAG_ISSUE);

        // Get all the eventsList where tagIssue equals to UPDATED_TAG_ISSUE
        defaultEventsShouldNotBeFound("tagIssue.equals=" + UPDATED_TAG_ISSUE);
    }

    @Test
    @Transactional
    void getAllEventsByTagIssueIsInShouldWork() throws Exception {
        // Initialize the database
        eventsRepository.saveAndFlush(events);

        // Get all the eventsList where tagIssue in DEFAULT_TAG_ISSUE or UPDATED_TAG_ISSUE
        defaultEventsShouldBeFound("tagIssue.in=" + DEFAULT_TAG_ISSUE + "," + UPDATED_TAG_ISSUE);

        // Get all the eventsList where tagIssue equals to UPDATED_TAG_ISSUE
        defaultEventsShouldNotBeFound("tagIssue.in=" + UPDATED_TAG_ISSUE);
    }

    @Test
    @Transactional
    void getAllEventsByTagIssueIsNullOrNotNull() throws Exception {
        // Initialize the database
        eventsRepository.saveAndFlush(events);

        // Get all the eventsList where tagIssue is not null
        defaultEventsShouldBeFound("tagIssue.specified=true");

        // Get all the eventsList where tagIssue is null
        defaultEventsShouldNotBeFound("tagIssue.specified=false");
    }

    @Test
    @Transactional
    void getAllEventsByTagIssueContainsSomething() throws Exception {
        // Initialize the database
        eventsRepository.saveAndFlush(events);

        // Get all the eventsList where tagIssue contains DEFAULT_TAG_ISSUE
        defaultEventsShouldBeFound("tagIssue.contains=" + DEFAULT_TAG_ISSUE);

        // Get all the eventsList where tagIssue contains UPDATED_TAG_ISSUE
        defaultEventsShouldNotBeFound("tagIssue.contains=" + UPDATED_TAG_ISSUE);
    }

    @Test
    @Transactional
    void getAllEventsByTagIssueNotContainsSomething() throws Exception {
        // Initialize the database
        eventsRepository.saveAndFlush(events);

        // Get all the eventsList where tagIssue does not contain DEFAULT_TAG_ISSUE
        defaultEventsShouldNotBeFound("tagIssue.doesNotContain=" + DEFAULT_TAG_ISSUE);

        // Get all the eventsList where tagIssue does not contain UPDATED_TAG_ISSUE
        defaultEventsShouldBeFound("tagIssue.doesNotContain=" + UPDATED_TAG_ISSUE);
    }

    @Test
    @Transactional
    void getAllEventsByStatusNameIsEqualToSomething() throws Exception {
        // Initialize the database
        eventsRepository.saveAndFlush(events);

        // Get all the eventsList where statusName equals to DEFAULT_STATUS_NAME
        defaultEventsShouldBeFound("statusName.equals=" + DEFAULT_STATUS_NAME);

        // Get all the eventsList where statusName equals to UPDATED_STATUS_NAME
        defaultEventsShouldNotBeFound("statusName.equals=" + UPDATED_STATUS_NAME);
    }

    @Test
    @Transactional
    void getAllEventsByStatusNameIsInShouldWork() throws Exception {
        // Initialize the database
        eventsRepository.saveAndFlush(events);

        // Get all the eventsList where statusName in DEFAULT_STATUS_NAME or UPDATED_STATUS_NAME
        defaultEventsShouldBeFound("statusName.in=" + DEFAULT_STATUS_NAME + "," + UPDATED_STATUS_NAME);

        // Get all the eventsList where statusName equals to UPDATED_STATUS_NAME
        defaultEventsShouldNotBeFound("statusName.in=" + UPDATED_STATUS_NAME);
    }

    @Test
    @Transactional
    void getAllEventsByStatusNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        eventsRepository.saveAndFlush(events);

        // Get all the eventsList where statusName is not null
        defaultEventsShouldBeFound("statusName.specified=true");

        // Get all the eventsList where statusName is null
        defaultEventsShouldNotBeFound("statusName.specified=false");
    }

    @Test
    @Transactional
    void getAllEventsByStatusNameContainsSomething() throws Exception {
        // Initialize the database
        eventsRepository.saveAndFlush(events);

        // Get all the eventsList where statusName contains DEFAULT_STATUS_NAME
        defaultEventsShouldBeFound("statusName.contains=" + DEFAULT_STATUS_NAME);

        // Get all the eventsList where statusName contains UPDATED_STATUS_NAME
        defaultEventsShouldNotBeFound("statusName.contains=" + UPDATED_STATUS_NAME);
    }

    @Test
    @Transactional
    void getAllEventsByStatusNameNotContainsSomething() throws Exception {
        // Initialize the database
        eventsRepository.saveAndFlush(events);

        // Get all the eventsList where statusName does not contain DEFAULT_STATUS_NAME
        defaultEventsShouldNotBeFound("statusName.doesNotContain=" + DEFAULT_STATUS_NAME);

        // Get all the eventsList where statusName does not contain UPDATED_STATUS_NAME
        defaultEventsShouldBeFound("statusName.doesNotContain=" + UPDATED_STATUS_NAME);
    }

    @Test
    @Transactional
    void getAllEventsByLicenseFineIsEqualToSomething() throws Exception {
        // Initialize the database
        eventsRepository.saveAndFlush(events);

        // Get all the eventsList where licenseFine equals to DEFAULT_LICENSE_FINE
        defaultEventsShouldBeFound("licenseFine.equals=" + DEFAULT_LICENSE_FINE);

        // Get all the eventsList where licenseFine equals to UPDATED_LICENSE_FINE
        defaultEventsShouldNotBeFound("licenseFine.equals=" + UPDATED_LICENSE_FINE);
    }

    @Test
    @Transactional
    void getAllEventsByLicenseFineIsInShouldWork() throws Exception {
        // Initialize the database
        eventsRepository.saveAndFlush(events);

        // Get all the eventsList where licenseFine in DEFAULT_LICENSE_FINE or UPDATED_LICENSE_FINE
        defaultEventsShouldBeFound("licenseFine.in=" + DEFAULT_LICENSE_FINE + "," + UPDATED_LICENSE_FINE);

        // Get all the eventsList where licenseFine equals to UPDATED_LICENSE_FINE
        defaultEventsShouldNotBeFound("licenseFine.in=" + UPDATED_LICENSE_FINE);
    }

    @Test
    @Transactional
    void getAllEventsByLicenseFineIsNullOrNotNull() throws Exception {
        // Initialize the database
        eventsRepository.saveAndFlush(events);

        // Get all the eventsList where licenseFine is not null
        defaultEventsShouldBeFound("licenseFine.specified=true");

        // Get all the eventsList where licenseFine is null
        defaultEventsShouldNotBeFound("licenseFine.specified=false");
    }

    @Test
    @Transactional
    void getAllEventsByLicenseFineIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        eventsRepository.saveAndFlush(events);

        // Get all the eventsList where licenseFine is greater than or equal to DEFAULT_LICENSE_FINE
        defaultEventsShouldBeFound("licenseFine.greaterThanOrEqual=" + DEFAULT_LICENSE_FINE);

        // Get all the eventsList where licenseFine is greater than or equal to UPDATED_LICENSE_FINE
        defaultEventsShouldNotBeFound("licenseFine.greaterThanOrEqual=" + UPDATED_LICENSE_FINE);
    }

    @Test
    @Transactional
    void getAllEventsByLicenseFineIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        eventsRepository.saveAndFlush(events);

        // Get all the eventsList where licenseFine is less than or equal to DEFAULT_LICENSE_FINE
        defaultEventsShouldBeFound("licenseFine.lessThanOrEqual=" + DEFAULT_LICENSE_FINE);

        // Get all the eventsList where licenseFine is less than or equal to SMALLER_LICENSE_FINE
        defaultEventsShouldNotBeFound("licenseFine.lessThanOrEqual=" + SMALLER_LICENSE_FINE);
    }

    @Test
    @Transactional
    void getAllEventsByLicenseFineIsLessThanSomething() throws Exception {
        // Initialize the database
        eventsRepository.saveAndFlush(events);

        // Get all the eventsList where licenseFine is less than DEFAULT_LICENSE_FINE
        defaultEventsShouldNotBeFound("licenseFine.lessThan=" + DEFAULT_LICENSE_FINE);

        // Get all the eventsList where licenseFine is less than UPDATED_LICENSE_FINE
        defaultEventsShouldBeFound("licenseFine.lessThan=" + UPDATED_LICENSE_FINE);
    }

    @Test
    @Transactional
    void getAllEventsByLicenseFineIsGreaterThanSomething() throws Exception {
        // Initialize the database
        eventsRepository.saveAndFlush(events);

        // Get all the eventsList where licenseFine is greater than DEFAULT_LICENSE_FINE
        defaultEventsShouldNotBeFound("licenseFine.greaterThan=" + DEFAULT_LICENSE_FINE);

        // Get all the eventsList where licenseFine is greater than SMALLER_LICENSE_FINE
        defaultEventsShouldBeFound("licenseFine.greaterThan=" + SMALLER_LICENSE_FINE);
    }

    @Test
    @Transactional
    void getAllEventsByStolenIsEqualToSomething() throws Exception {
        // Initialize the database
        eventsRepository.saveAndFlush(events);

        // Get all the eventsList where stolen equals to DEFAULT_STOLEN
        defaultEventsShouldBeFound("stolen.equals=" + DEFAULT_STOLEN);

        // Get all the eventsList where stolen equals to UPDATED_STOLEN
        defaultEventsShouldNotBeFound("stolen.equals=" + UPDATED_STOLEN);
    }

    @Test
    @Transactional
    void getAllEventsByStolenIsInShouldWork() throws Exception {
        // Initialize the database
        eventsRepository.saveAndFlush(events);

        // Get all the eventsList where stolen in DEFAULT_STOLEN or UPDATED_STOLEN
        defaultEventsShouldBeFound("stolen.in=" + DEFAULT_STOLEN + "," + UPDATED_STOLEN);

        // Get all the eventsList where stolen equals to UPDATED_STOLEN
        defaultEventsShouldNotBeFound("stolen.in=" + UPDATED_STOLEN);
    }

    @Test
    @Transactional
    void getAllEventsByStolenIsNullOrNotNull() throws Exception {
        // Initialize the database
        eventsRepository.saveAndFlush(events);

        // Get all the eventsList where stolen is not null
        defaultEventsShouldBeFound("stolen.specified=true");

        // Get all the eventsList where stolen is null
        defaultEventsShouldNotBeFound("stolen.specified=false");
    }

    @Test
    @Transactional
    void getAllEventsByStolenIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        eventsRepository.saveAndFlush(events);

        // Get all the eventsList where stolen is greater than or equal to DEFAULT_STOLEN
        defaultEventsShouldBeFound("stolen.greaterThanOrEqual=" + DEFAULT_STOLEN);

        // Get all the eventsList where stolen is greater than or equal to UPDATED_STOLEN
        defaultEventsShouldNotBeFound("stolen.greaterThanOrEqual=" + UPDATED_STOLEN);
    }

    @Test
    @Transactional
    void getAllEventsByStolenIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        eventsRepository.saveAndFlush(events);

        // Get all the eventsList where stolen is less than or equal to DEFAULT_STOLEN
        defaultEventsShouldBeFound("stolen.lessThanOrEqual=" + DEFAULT_STOLEN);

        // Get all the eventsList where stolen is less than or equal to SMALLER_STOLEN
        defaultEventsShouldNotBeFound("stolen.lessThanOrEqual=" + SMALLER_STOLEN);
    }

    @Test
    @Transactional
    void getAllEventsByStolenIsLessThanSomething() throws Exception {
        // Initialize the database
        eventsRepository.saveAndFlush(events);

        // Get all the eventsList where stolen is less than DEFAULT_STOLEN
        defaultEventsShouldNotBeFound("stolen.lessThan=" + DEFAULT_STOLEN);

        // Get all the eventsList where stolen is less than UPDATED_STOLEN
        defaultEventsShouldBeFound("stolen.lessThan=" + UPDATED_STOLEN);
    }

    @Test
    @Transactional
    void getAllEventsByStolenIsGreaterThanSomething() throws Exception {
        // Initialize the database
        eventsRepository.saveAndFlush(events);

        // Get all the eventsList where stolen is greater than DEFAULT_STOLEN
        defaultEventsShouldNotBeFound("stolen.greaterThan=" + DEFAULT_STOLEN);

        // Get all the eventsList where stolen is greater than SMALLER_STOLEN
        defaultEventsShouldBeFound("stolen.greaterThan=" + SMALLER_STOLEN);
    }

    @Test
    @Transactional
    void getAllEventsByWantedIsEqualToSomething() throws Exception {
        // Initialize the database
        eventsRepository.saveAndFlush(events);

        // Get all the eventsList where wanted equals to DEFAULT_WANTED
        defaultEventsShouldBeFound("wanted.equals=" + DEFAULT_WANTED);

        // Get all the eventsList where wanted equals to UPDATED_WANTED
        defaultEventsShouldNotBeFound("wanted.equals=" + UPDATED_WANTED);
    }

    @Test
    @Transactional
    void getAllEventsByWantedIsInShouldWork() throws Exception {
        // Initialize the database
        eventsRepository.saveAndFlush(events);

        // Get all the eventsList where wanted in DEFAULT_WANTED or UPDATED_WANTED
        defaultEventsShouldBeFound("wanted.in=" + DEFAULT_WANTED + "," + UPDATED_WANTED);

        // Get all the eventsList where wanted equals to UPDATED_WANTED
        defaultEventsShouldNotBeFound("wanted.in=" + UPDATED_WANTED);
    }

    @Test
    @Transactional
    void getAllEventsByWantedIsNullOrNotNull() throws Exception {
        // Initialize the database
        eventsRepository.saveAndFlush(events);

        // Get all the eventsList where wanted is not null
        defaultEventsShouldBeFound("wanted.specified=true");

        // Get all the eventsList where wanted is null
        defaultEventsShouldNotBeFound("wanted.specified=false");
    }

    @Test
    @Transactional
    void getAllEventsByRuleSentIsEqualToSomething() throws Exception {
        // Initialize the database
        eventsRepository.saveAndFlush(events);

        // Get all the eventsList where ruleSent equals to DEFAULT_RULE_SENT
        defaultEventsShouldBeFound("ruleSent.equals=" + DEFAULT_RULE_SENT);

        // Get all the eventsList where ruleSent equals to UPDATED_RULE_SENT
        defaultEventsShouldNotBeFound("ruleSent.equals=" + UPDATED_RULE_SENT);
    }

    @Test
    @Transactional
    void getAllEventsByRuleSentIsInShouldWork() throws Exception {
        // Initialize the database
        eventsRepository.saveAndFlush(events);

        // Get all the eventsList where ruleSent in DEFAULT_RULE_SENT or UPDATED_RULE_SENT
        defaultEventsShouldBeFound("ruleSent.in=" + DEFAULT_RULE_SENT + "," + UPDATED_RULE_SENT);

        // Get all the eventsList where ruleSent equals to UPDATED_RULE_SENT
        defaultEventsShouldNotBeFound("ruleSent.in=" + UPDATED_RULE_SENT);
    }

    @Test
    @Transactional
    void getAllEventsByRuleSentIsNullOrNotNull() throws Exception {
        // Initialize the database
        eventsRepository.saveAndFlush(events);

        // Get all the eventsList where ruleSent is not null
        defaultEventsShouldBeFound("ruleSent.specified=true");

        // Get all the eventsList where ruleSent is null
        defaultEventsShouldNotBeFound("ruleSent.specified=false");
    }

    @Test
    @Transactional
    void getAllEventsByRuleSentIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        eventsRepository.saveAndFlush(events);

        // Get all the eventsList where ruleSent is greater than or equal to DEFAULT_RULE_SENT
        defaultEventsShouldBeFound("ruleSent.greaterThanOrEqual=" + DEFAULT_RULE_SENT);

        // Get all the eventsList where ruleSent is greater than or equal to UPDATED_RULE_SENT
        defaultEventsShouldNotBeFound("ruleSent.greaterThanOrEqual=" + UPDATED_RULE_SENT);
    }

    @Test
    @Transactional
    void getAllEventsByRuleSentIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        eventsRepository.saveAndFlush(events);

        // Get all the eventsList where ruleSent is less than or equal to DEFAULT_RULE_SENT
        defaultEventsShouldBeFound("ruleSent.lessThanOrEqual=" + DEFAULT_RULE_SENT);

        // Get all the eventsList where ruleSent is less than or equal to SMALLER_RULE_SENT
        defaultEventsShouldNotBeFound("ruleSent.lessThanOrEqual=" + SMALLER_RULE_SENT);
    }

    @Test
    @Transactional
    void getAllEventsByRuleSentIsLessThanSomething() throws Exception {
        // Initialize the database
        eventsRepository.saveAndFlush(events);

        // Get all the eventsList where ruleSent is less than DEFAULT_RULE_SENT
        defaultEventsShouldNotBeFound("ruleSent.lessThan=" + DEFAULT_RULE_SENT);

        // Get all the eventsList where ruleSent is less than UPDATED_RULE_SENT
        defaultEventsShouldBeFound("ruleSent.lessThan=" + UPDATED_RULE_SENT);
    }

    @Test
    @Transactional
    void getAllEventsByRuleSentIsGreaterThanSomething() throws Exception {
        // Initialize the database
        eventsRepository.saveAndFlush(events);

        // Get all the eventsList where ruleSent is greater than DEFAULT_RULE_SENT
        defaultEventsShouldNotBeFound("ruleSent.greaterThan=" + DEFAULT_RULE_SENT);

        // Get all the eventsList where ruleSent is greater than SMALLER_RULE_SENT
        defaultEventsShouldBeFound("ruleSent.greaterThan=" + SMALLER_RULE_SENT);
    }

    @Test
    @Transactional
    void getAllEventsByHandledIsEqualToSomething() throws Exception {
        // Initialize the database
        eventsRepository.saveAndFlush(events);

        // Get all the eventsList where handled equals to DEFAULT_HANDLED
        defaultEventsShouldBeFound("handled.equals=" + DEFAULT_HANDLED);

        // Get all the eventsList where handled equals to UPDATED_HANDLED
        defaultEventsShouldNotBeFound("handled.equals=" + UPDATED_HANDLED);
    }

    @Test
    @Transactional
    void getAllEventsByHandledIsInShouldWork() throws Exception {
        // Initialize the database
        eventsRepository.saveAndFlush(events);

        // Get all the eventsList where handled in DEFAULT_HANDLED or UPDATED_HANDLED
        defaultEventsShouldBeFound("handled.in=" + DEFAULT_HANDLED + "," + UPDATED_HANDLED);

        // Get all the eventsList where handled equals to UPDATED_HANDLED
        defaultEventsShouldNotBeFound("handled.in=" + UPDATED_HANDLED);
    }

    @Test
    @Transactional
    void getAllEventsByHandledIsNullOrNotNull() throws Exception {
        // Initialize the database
        eventsRepository.saveAndFlush(events);

        // Get all the eventsList where handled is not null
        defaultEventsShouldBeFound("handled.specified=true");

        // Get all the eventsList where handled is null
        defaultEventsShouldNotBeFound("handled.specified=false");
    }

    @Test
    @Transactional
    void getAllEventsByHandledIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        eventsRepository.saveAndFlush(events);

        // Get all the eventsList where handled is greater than or equal to DEFAULT_HANDLED
        defaultEventsShouldBeFound("handled.greaterThanOrEqual=" + DEFAULT_HANDLED);

        // Get all the eventsList where handled is greater than or equal to UPDATED_HANDLED
        defaultEventsShouldNotBeFound("handled.greaterThanOrEqual=" + UPDATED_HANDLED);
    }

    @Test
    @Transactional
    void getAllEventsByHandledIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        eventsRepository.saveAndFlush(events);

        // Get all the eventsList where handled is less than or equal to DEFAULT_HANDLED
        defaultEventsShouldBeFound("handled.lessThanOrEqual=" + DEFAULT_HANDLED);

        // Get all the eventsList where handled is less than or equal to SMALLER_HANDLED
        defaultEventsShouldNotBeFound("handled.lessThanOrEqual=" + SMALLER_HANDLED);
    }

    @Test
    @Transactional
    void getAllEventsByHandledIsLessThanSomething() throws Exception {
        // Initialize the database
        eventsRepository.saveAndFlush(events);

        // Get all the eventsList where handled is less than DEFAULT_HANDLED
        defaultEventsShouldNotBeFound("handled.lessThan=" + DEFAULT_HANDLED);

        // Get all the eventsList where handled is less than UPDATED_HANDLED
        defaultEventsShouldBeFound("handled.lessThan=" + UPDATED_HANDLED);
    }

    @Test
    @Transactional
    void getAllEventsByHandledIsGreaterThanSomething() throws Exception {
        // Initialize the database
        eventsRepository.saveAndFlush(events);

        // Get all the eventsList where handled is greater than DEFAULT_HANDLED
        defaultEventsShouldNotBeFound("handled.greaterThan=" + DEFAULT_HANDLED);

        // Get all the eventsList where handled is greater than SMALLER_HANDLED
        defaultEventsShouldBeFound("handled.greaterThan=" + SMALLER_HANDLED);
    }

    @Test
    @Transactional
    void getAllEventsByRuleIssueIsEqualToSomething() throws Exception {
        // Initialize the database
        eventsRepository.saveAndFlush(events);

        // Get all the eventsList where ruleIssue equals to DEFAULT_RULE_ISSUE
        defaultEventsShouldBeFound("ruleIssue.equals=" + DEFAULT_RULE_ISSUE);

        // Get all the eventsList where ruleIssue equals to UPDATED_RULE_ISSUE
        defaultEventsShouldNotBeFound("ruleIssue.equals=" + UPDATED_RULE_ISSUE);
    }

    @Test
    @Transactional
    void getAllEventsByRuleIssueIsInShouldWork() throws Exception {
        // Initialize the database
        eventsRepository.saveAndFlush(events);

        // Get all the eventsList where ruleIssue in DEFAULT_RULE_ISSUE or UPDATED_RULE_ISSUE
        defaultEventsShouldBeFound("ruleIssue.in=" + DEFAULT_RULE_ISSUE + "," + UPDATED_RULE_ISSUE);

        // Get all the eventsList where ruleIssue equals to UPDATED_RULE_ISSUE
        defaultEventsShouldNotBeFound("ruleIssue.in=" + UPDATED_RULE_ISSUE);
    }

    @Test
    @Transactional
    void getAllEventsByRuleIssueIsNullOrNotNull() throws Exception {
        // Initialize the database
        eventsRepository.saveAndFlush(events);

        // Get all the eventsList where ruleIssue is not null
        defaultEventsShouldBeFound("ruleIssue.specified=true");

        // Get all the eventsList where ruleIssue is null
        defaultEventsShouldNotBeFound("ruleIssue.specified=false");
    }

    @Test
    @Transactional
    void getAllEventsByRuleIssueContainsSomething() throws Exception {
        // Initialize the database
        eventsRepository.saveAndFlush(events);

        // Get all the eventsList where ruleIssue contains DEFAULT_RULE_ISSUE
        defaultEventsShouldBeFound("ruleIssue.contains=" + DEFAULT_RULE_ISSUE);

        // Get all the eventsList where ruleIssue contains UPDATED_RULE_ISSUE
        defaultEventsShouldNotBeFound("ruleIssue.contains=" + UPDATED_RULE_ISSUE);
    }

    @Test
    @Transactional
    void getAllEventsByRuleIssueNotContainsSomething() throws Exception {
        // Initialize the database
        eventsRepository.saveAndFlush(events);

        // Get all the eventsList where ruleIssue does not contain DEFAULT_RULE_ISSUE
        defaultEventsShouldNotBeFound("ruleIssue.doesNotContain=" + DEFAULT_RULE_ISSUE);

        // Get all the eventsList where ruleIssue does not contain UPDATED_RULE_ISSUE
        defaultEventsShouldBeFound("ruleIssue.doesNotContain=" + UPDATED_RULE_ISSUE);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultEventsShouldBeFound(String filter) throws Exception {
        restEventsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(events.getId().intValue())))
            .andExpect(jsonPath("$.[*].guid").value(hasItem(DEFAULT_GUID)))
            .andExpect(jsonPath("$.[*].plate").value(hasItem(DEFAULT_PLATE)))
            .andExpect(jsonPath("$.[*].anpr").value(hasItem(DEFAULT_ANPR)))
            .andExpect(jsonPath("$.[*].rfid").value(hasItem(DEFAULT_RFID)))
            .andExpect(jsonPath("$.[*].dataStatus").value(hasItem(DEFAULT_DATA_STATUS)))
            .andExpect(jsonPath("$.[*].gantry").value(hasItem(DEFAULT_GANTRY.intValue())))
            .andExpect(jsonPath("$.[*].kph").value(hasItem(DEFAULT_KPH.intValue())))
            .andExpect(jsonPath("$.[*].ambush").value(hasItem(DEFAULT_AMBUSH.intValue())))
            .andExpect(jsonPath("$.[*].direction").value(hasItem(DEFAULT_DIRECTION.intValue())))
            .andExpect(jsonPath("$.[*].vehicle").value(hasItem(DEFAULT_VEHICLE.intValue())))
            .andExpect(jsonPath("$.[*].issue").value(hasItem(DEFAULT_ISSUE)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS)))
            .andExpect(jsonPath("$.[*].handledBy").value(hasItem(DEFAULT_HANDLED_BY.intValue())))
            .andExpect(jsonPath("$.[*].gantryProcessed").value(hasItem(DEFAULT_GANTRY_PROCESSED.intValue())))
            .andExpect(jsonPath("$.[*].gantrySent").value(hasItem(DEFAULT_GANTRY_SENT.intValue())))
            .andExpect(jsonPath("$.[*].when").value(hasItem(DEFAULT_WHEN.intValue())))
            .andExpect(jsonPath("$.[*].toll").value(hasItem(DEFAULT_TOLL.intValue())))
            .andExpect(jsonPath("$.[*].ruleRcvd").value(hasItem(DEFAULT_RULE_RCVD.intValue())))
            .andExpect(jsonPath("$.[*].wantedFor").value(hasItem(DEFAULT_WANTED_FOR)))
            .andExpect(jsonPath("$.[*].fine").value(hasItem(DEFAULT_FINE.intValue())))
            .andExpect(jsonPath("$.[*].licenseIssue").value(hasItem(DEFAULT_LICENSE_ISSUE)))
            .andExpect(jsonPath("$.[*].wantedBy").value(hasItem(DEFAULT_WANTED_BY.intValue())))
            .andExpect(jsonPath("$.[*].ruleProcessed").value(hasItem(DEFAULT_RULE_PROCESSED.intValue())))
            .andExpect(jsonPath("$.[*].speedFine").value(hasItem(DEFAULT_SPEED_FINE.intValue())))
            .andExpect(jsonPath("$.[*].lane").value(hasItem(DEFAULT_LANE.intValue())))
            .andExpect(jsonPath("$.[*].tagIssue").value(hasItem(DEFAULT_TAG_ISSUE)))
            .andExpect(jsonPath("$.[*].statusName").value(hasItem(DEFAULT_STATUS_NAME)))
            .andExpect(jsonPath("$.[*].licenseFine").value(hasItem(DEFAULT_LICENSE_FINE.intValue())))
            .andExpect(jsonPath("$.[*].stolen").value(hasItem(DEFAULT_STOLEN.intValue())))
            .andExpect(jsonPath("$.[*].wanted").value(hasItem(DEFAULT_WANTED.booleanValue())))
            .andExpect(jsonPath("$.[*].ruleSent").value(hasItem(DEFAULT_RULE_SENT.intValue())))
            .andExpect(jsonPath("$.[*].handled").value(hasItem(DEFAULT_HANDLED.intValue())))
            .andExpect(jsonPath("$.[*].ruleIssue").value(hasItem(DEFAULT_RULE_ISSUE)));

        // Check, that the count call also returns 1
        restEventsMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultEventsShouldNotBeFound(String filter) throws Exception {
        restEventsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restEventsMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingEvents() throws Exception {
        // Get the events
        restEventsMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingEvents() throws Exception {
        // Initialize the database
        eventsRepository.saveAndFlush(events);

        int databaseSizeBeforeUpdate = eventsRepository.findAll().size();
        eventsSearchRepository.save(events);
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(eventsSearchRepository.findAll());

        // Update the events
        Events updatedEvents = eventsRepository.findById(events.getId()).get();
        // Disconnect from session so that the updates on updatedEvents are not directly saved in db
        em.detach(updatedEvents);
        updatedEvents
            .guid(UPDATED_GUID)
            .plate(UPDATED_PLATE)
            .anpr(UPDATED_ANPR)
            .rfid(UPDATED_RFID)
            .dataStatus(UPDATED_DATA_STATUS)
            .gantry(UPDATED_GANTRY)
            .kph(UPDATED_KPH)
            .ambush(UPDATED_AMBUSH)
            .direction(UPDATED_DIRECTION)
            .vehicle(UPDATED_VEHICLE)
            .issue(UPDATED_ISSUE)
            .status(UPDATED_STATUS)
            .handledBy(UPDATED_HANDLED_BY)
            .gantryProcessed(UPDATED_GANTRY_PROCESSED)
            .gantrySent(UPDATED_GANTRY_SENT)
            .when(UPDATED_WHEN)
            .toll(UPDATED_TOLL)
            .ruleRcvd(UPDATED_RULE_RCVD)
            .wantedFor(UPDATED_WANTED_FOR)
            .fine(UPDATED_FINE)
            .licenseIssue(UPDATED_LICENSE_ISSUE)
            .wantedBy(UPDATED_WANTED_BY)
            .ruleProcessed(UPDATED_RULE_PROCESSED)
            .speedFine(UPDATED_SPEED_FINE)
            .lane(UPDATED_LANE)
            .tagIssue(UPDATED_TAG_ISSUE)
            .statusName(UPDATED_STATUS_NAME)
            .licenseFine(UPDATED_LICENSE_FINE)
            .stolen(UPDATED_STOLEN)
            .wanted(UPDATED_WANTED)
            .ruleSent(UPDATED_RULE_SENT)
            .handled(UPDATED_HANDLED)
            .ruleIssue(UPDATED_RULE_ISSUE);
        EventsDTO eventsDTO = eventsMapper.toDto(updatedEvents);

        restEventsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, eventsDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(eventsDTO))
            )
            .andExpect(status().isOk());

        // Validate the Events in the database
        List<Events> eventsList = eventsRepository.findAll();
        assertThat(eventsList).hasSize(databaseSizeBeforeUpdate);
        Events testEvents = eventsList.get(eventsList.size() - 1);
        assertThat(testEvents.getGuid()).isEqualTo(UPDATED_GUID);
        assertThat(testEvents.getPlate()).isEqualTo(UPDATED_PLATE);
        assertThat(testEvents.getAnpr()).isEqualTo(UPDATED_ANPR);
        assertThat(testEvents.getRfid()).isEqualTo(UPDATED_RFID);
        assertThat(testEvents.getDataStatus()).isEqualTo(UPDATED_DATA_STATUS);
        assertThat(testEvents.getGantry()).isEqualTo(UPDATED_GANTRY);
        assertThat(testEvents.getKph()).isEqualTo(UPDATED_KPH);
        assertThat(testEvents.getAmbush()).isEqualTo(UPDATED_AMBUSH);
        assertThat(testEvents.getDirection()).isEqualTo(UPDATED_DIRECTION);
        assertThat(testEvents.getVehicle()).isEqualTo(UPDATED_VEHICLE);
        assertThat(testEvents.getIssue()).isEqualTo(UPDATED_ISSUE);
        assertThat(testEvents.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testEvents.getHandledBy()).isEqualTo(UPDATED_HANDLED_BY);
        assertThat(testEvents.getGantryProcessed()).isEqualTo(UPDATED_GANTRY_PROCESSED);
        assertThat(testEvents.getGantrySent()).isEqualTo(UPDATED_GANTRY_SENT);
        assertThat(testEvents.getWhen()).isEqualTo(UPDATED_WHEN);
        assertThat(testEvents.getToll()).isEqualTo(UPDATED_TOLL);
        assertThat(testEvents.getRuleRcvd()).isEqualTo(UPDATED_RULE_RCVD);
        assertThat(testEvents.getWantedFor()).isEqualTo(UPDATED_WANTED_FOR);
        assertThat(testEvents.getFine()).isEqualTo(UPDATED_FINE);
        assertThat(testEvents.getLicenseIssue()).isEqualTo(UPDATED_LICENSE_ISSUE);
        assertThat(testEvents.getWantedBy()).isEqualTo(UPDATED_WANTED_BY);
        assertThat(testEvents.getRuleProcessed()).isEqualTo(UPDATED_RULE_PROCESSED);
        assertThat(testEvents.getSpeedFine()).isEqualTo(UPDATED_SPEED_FINE);
        assertThat(testEvents.getLane()).isEqualTo(UPDATED_LANE);
        assertThat(testEvents.getTagIssue()).isEqualTo(UPDATED_TAG_ISSUE);
        assertThat(testEvents.getStatusName()).isEqualTo(UPDATED_STATUS_NAME);
        assertThat(testEvents.getLicenseFine()).isEqualTo(UPDATED_LICENSE_FINE);
        assertThat(testEvents.getStolen()).isEqualTo(UPDATED_STOLEN);
        assertThat(testEvents.getWanted()).isEqualTo(UPDATED_WANTED);
        assertThat(testEvents.getRuleSent()).isEqualTo(UPDATED_RULE_SENT);
        assertThat(testEvents.getHandled()).isEqualTo(UPDATED_HANDLED);
        assertThat(testEvents.getRuleIssue()).isEqualTo(UPDATED_RULE_ISSUE);
        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(eventsSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
                List<Events> eventsSearchList = IterableUtils.toList(eventsSearchRepository.findAll());
                Events testEventsSearch = eventsSearchList.get(searchDatabaseSizeAfter - 1);
                assertThat(testEventsSearch.getGuid()).isEqualTo(UPDATED_GUID);
                assertThat(testEventsSearch.getPlate()).isEqualTo(UPDATED_PLATE);
                assertThat(testEventsSearch.getAnpr()).isEqualTo(UPDATED_ANPR);
                assertThat(testEventsSearch.getRfid()).isEqualTo(UPDATED_RFID);
                assertThat(testEventsSearch.getDataStatus()).isEqualTo(UPDATED_DATA_STATUS);
                assertThat(testEventsSearch.getGantry()).isEqualTo(UPDATED_GANTRY);
                assertThat(testEventsSearch.getKph()).isEqualTo(UPDATED_KPH);
                assertThat(testEventsSearch.getAmbush()).isEqualTo(UPDATED_AMBUSH);
                assertThat(testEventsSearch.getDirection()).isEqualTo(UPDATED_DIRECTION);
                assertThat(testEventsSearch.getVehicle()).isEqualTo(UPDATED_VEHICLE);
                assertThat(testEventsSearch.getIssue()).isEqualTo(UPDATED_ISSUE);
                assertThat(testEventsSearch.getStatus()).isEqualTo(UPDATED_STATUS);
                assertThat(testEventsSearch.getHandledBy()).isEqualTo(UPDATED_HANDLED_BY);
                assertThat(testEventsSearch.getGantryProcessed()).isEqualTo(UPDATED_GANTRY_PROCESSED);
                assertThat(testEventsSearch.getGantrySent()).isEqualTo(UPDATED_GANTRY_SENT);
                assertThat(testEventsSearch.getWhen()).isEqualTo(UPDATED_WHEN);
                assertThat(testEventsSearch.getToll()).isEqualTo(UPDATED_TOLL);
                assertThat(testEventsSearch.getRuleRcvd()).isEqualTo(UPDATED_RULE_RCVD);
                assertThat(testEventsSearch.getWantedFor()).isEqualTo(UPDATED_WANTED_FOR);
                assertThat(testEventsSearch.getFine()).isEqualTo(UPDATED_FINE);
                assertThat(testEventsSearch.getLicenseIssue()).isEqualTo(UPDATED_LICENSE_ISSUE);
                assertThat(testEventsSearch.getWantedBy()).isEqualTo(UPDATED_WANTED_BY);
                assertThat(testEventsSearch.getRuleProcessed()).isEqualTo(UPDATED_RULE_PROCESSED);
                assertThat(testEventsSearch.getSpeedFine()).isEqualTo(UPDATED_SPEED_FINE);
                assertThat(testEventsSearch.getLane()).isEqualTo(UPDATED_LANE);
                assertThat(testEventsSearch.getTagIssue()).isEqualTo(UPDATED_TAG_ISSUE);
                assertThat(testEventsSearch.getStatusName()).isEqualTo(UPDATED_STATUS_NAME);
                assertThat(testEventsSearch.getLicenseFine()).isEqualTo(UPDATED_LICENSE_FINE);
                assertThat(testEventsSearch.getStolen()).isEqualTo(UPDATED_STOLEN);
                assertThat(testEventsSearch.getWanted()).isEqualTo(UPDATED_WANTED);
                assertThat(testEventsSearch.getRuleSent()).isEqualTo(UPDATED_RULE_SENT);
                assertThat(testEventsSearch.getHandled()).isEqualTo(UPDATED_HANDLED);
                assertThat(testEventsSearch.getRuleIssue()).isEqualTo(UPDATED_RULE_ISSUE);
            });
    }

    @Test
    @Transactional
    void putNonExistingEvents() throws Exception {
        int databaseSizeBeforeUpdate = eventsRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(eventsSearchRepository.findAll());
        events.setId(count.incrementAndGet());

        // Create the Events
        EventsDTO eventsDTO = eventsMapper.toDto(events);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEventsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, eventsDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(eventsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Events in the database
        List<Events> eventsList = eventsRepository.findAll();
        assertThat(eventsList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(eventsSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithIdMismatchEvents() throws Exception {
        int databaseSizeBeforeUpdate = eventsRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(eventsSearchRepository.findAll());
        events.setId(count.incrementAndGet());

        // Create the Events
        EventsDTO eventsDTO = eventsMapper.toDto(events);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEventsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(eventsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Events in the database
        List<Events> eventsList = eventsRepository.findAll();
        assertThat(eventsList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(eventsSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamEvents() throws Exception {
        int databaseSizeBeforeUpdate = eventsRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(eventsSearchRepository.findAll());
        events.setId(count.incrementAndGet());

        // Create the Events
        EventsDTO eventsDTO = eventsMapper.toDto(events);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEventsMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(eventsDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Events in the database
        List<Events> eventsList = eventsRepository.findAll();
        assertThat(eventsList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(eventsSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void partialUpdateEventsWithPatch() throws Exception {
        // Initialize the database
        eventsRepository.saveAndFlush(events);

        int databaseSizeBeforeUpdate = eventsRepository.findAll().size();

        // Update the events using partial update
        Events partialUpdatedEvents = new Events();
        partialUpdatedEvents.setId(events.getId());

        partialUpdatedEvents
            .guid(UPDATED_GUID)
            .plate(UPDATED_PLATE)
            .rfid(UPDATED_RFID)
            .dataStatus(UPDATED_DATA_STATUS)
            .gantry(UPDATED_GANTRY)
            .kph(UPDATED_KPH)
            .vehicle(UPDATED_VEHICLE)
            .issue(UPDATED_ISSUE)
            .handledBy(UPDATED_HANDLED_BY)
            .when(UPDATED_WHEN)
            .toll(UPDATED_TOLL)
            .wantedFor(UPDATED_WANTED_FOR)
            .licenseIssue(UPDATED_LICENSE_ISSUE)
            .wantedBy(UPDATED_WANTED_BY)
            .lane(UPDATED_LANE)
            .tagIssue(UPDATED_TAG_ISSUE)
            .licenseFine(UPDATED_LICENSE_FINE);

        restEventsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEvents.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedEvents))
            )
            .andExpect(status().isOk());

        // Validate the Events in the database
        List<Events> eventsList = eventsRepository.findAll();
        assertThat(eventsList).hasSize(databaseSizeBeforeUpdate);
        Events testEvents = eventsList.get(eventsList.size() - 1);
        assertThat(testEvents.getGuid()).isEqualTo(UPDATED_GUID);
        assertThat(testEvents.getPlate()).isEqualTo(UPDATED_PLATE);
        assertThat(testEvents.getAnpr()).isEqualTo(DEFAULT_ANPR);
        assertThat(testEvents.getRfid()).isEqualTo(UPDATED_RFID);
        assertThat(testEvents.getDataStatus()).isEqualTo(UPDATED_DATA_STATUS);
        assertThat(testEvents.getGantry()).isEqualTo(UPDATED_GANTRY);
        assertThat(testEvents.getKph()).isEqualTo(UPDATED_KPH);
        assertThat(testEvents.getAmbush()).isEqualTo(DEFAULT_AMBUSH);
        assertThat(testEvents.getDirection()).isEqualTo(DEFAULT_DIRECTION);
        assertThat(testEvents.getVehicle()).isEqualTo(UPDATED_VEHICLE);
        assertThat(testEvents.getIssue()).isEqualTo(UPDATED_ISSUE);
        assertThat(testEvents.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testEvents.getHandledBy()).isEqualTo(UPDATED_HANDLED_BY);
        assertThat(testEvents.getGantryProcessed()).isEqualTo(DEFAULT_GANTRY_PROCESSED);
        assertThat(testEvents.getGantrySent()).isEqualTo(DEFAULT_GANTRY_SENT);
        assertThat(testEvents.getWhen()).isEqualTo(UPDATED_WHEN);
        assertThat(testEvents.getToll()).isEqualTo(UPDATED_TOLL);
        assertThat(testEvents.getRuleRcvd()).isEqualTo(DEFAULT_RULE_RCVD);
        assertThat(testEvents.getWantedFor()).isEqualTo(UPDATED_WANTED_FOR);
        assertThat(testEvents.getFine()).isEqualTo(DEFAULT_FINE);
        assertThat(testEvents.getLicenseIssue()).isEqualTo(UPDATED_LICENSE_ISSUE);
        assertThat(testEvents.getWantedBy()).isEqualTo(UPDATED_WANTED_BY);
        assertThat(testEvents.getRuleProcessed()).isEqualTo(DEFAULT_RULE_PROCESSED);
        assertThat(testEvents.getSpeedFine()).isEqualTo(DEFAULT_SPEED_FINE);
        assertThat(testEvents.getLane()).isEqualTo(UPDATED_LANE);
        assertThat(testEvents.getTagIssue()).isEqualTo(UPDATED_TAG_ISSUE);
        assertThat(testEvents.getStatusName()).isEqualTo(DEFAULT_STATUS_NAME);
        assertThat(testEvents.getLicenseFine()).isEqualTo(UPDATED_LICENSE_FINE);
        assertThat(testEvents.getStolen()).isEqualTo(DEFAULT_STOLEN);
        assertThat(testEvents.getWanted()).isEqualTo(DEFAULT_WANTED);
        assertThat(testEvents.getRuleSent()).isEqualTo(DEFAULT_RULE_SENT);
        assertThat(testEvents.getHandled()).isEqualTo(DEFAULT_HANDLED);
        assertThat(testEvents.getRuleIssue()).isEqualTo(DEFAULT_RULE_ISSUE);
    }

    @Test
    @Transactional
    void fullUpdateEventsWithPatch() throws Exception {
        // Initialize the database
        eventsRepository.saveAndFlush(events);

        int databaseSizeBeforeUpdate = eventsRepository.findAll().size();

        // Update the events using partial update
        Events partialUpdatedEvents = new Events();
        partialUpdatedEvents.setId(events.getId());

        partialUpdatedEvents
            .guid(UPDATED_GUID)
            .plate(UPDATED_PLATE)
            .anpr(UPDATED_ANPR)
            .rfid(UPDATED_RFID)
            .dataStatus(UPDATED_DATA_STATUS)
            .gantry(UPDATED_GANTRY)
            .kph(UPDATED_KPH)
            .ambush(UPDATED_AMBUSH)
            .direction(UPDATED_DIRECTION)
            .vehicle(UPDATED_VEHICLE)
            .issue(UPDATED_ISSUE)
            .status(UPDATED_STATUS)
            .handledBy(UPDATED_HANDLED_BY)
            .gantryProcessed(UPDATED_GANTRY_PROCESSED)
            .gantrySent(UPDATED_GANTRY_SENT)
            .when(UPDATED_WHEN)
            .toll(UPDATED_TOLL)
            .ruleRcvd(UPDATED_RULE_RCVD)
            .wantedFor(UPDATED_WANTED_FOR)
            .fine(UPDATED_FINE)
            .licenseIssue(UPDATED_LICENSE_ISSUE)
            .wantedBy(UPDATED_WANTED_BY)
            .ruleProcessed(UPDATED_RULE_PROCESSED)
            .speedFine(UPDATED_SPEED_FINE)
            .lane(UPDATED_LANE)
            .tagIssue(UPDATED_TAG_ISSUE)
            .statusName(UPDATED_STATUS_NAME)
            .licenseFine(UPDATED_LICENSE_FINE)
            .stolen(UPDATED_STOLEN)
            .wanted(UPDATED_WANTED)
            .ruleSent(UPDATED_RULE_SENT)
            .handled(UPDATED_HANDLED)
            .ruleIssue(UPDATED_RULE_ISSUE);

        restEventsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEvents.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedEvents))
            )
            .andExpect(status().isOk());

        // Validate the Events in the database
        List<Events> eventsList = eventsRepository.findAll();
        assertThat(eventsList).hasSize(databaseSizeBeforeUpdate);
        Events testEvents = eventsList.get(eventsList.size() - 1);
        assertThat(testEvents.getGuid()).isEqualTo(UPDATED_GUID);
        assertThat(testEvents.getPlate()).isEqualTo(UPDATED_PLATE);
        assertThat(testEvents.getAnpr()).isEqualTo(UPDATED_ANPR);
        assertThat(testEvents.getRfid()).isEqualTo(UPDATED_RFID);
        assertThat(testEvents.getDataStatus()).isEqualTo(UPDATED_DATA_STATUS);
        assertThat(testEvents.getGantry()).isEqualTo(UPDATED_GANTRY);
        assertThat(testEvents.getKph()).isEqualTo(UPDATED_KPH);
        assertThat(testEvents.getAmbush()).isEqualTo(UPDATED_AMBUSH);
        assertThat(testEvents.getDirection()).isEqualTo(UPDATED_DIRECTION);
        assertThat(testEvents.getVehicle()).isEqualTo(UPDATED_VEHICLE);
        assertThat(testEvents.getIssue()).isEqualTo(UPDATED_ISSUE);
        assertThat(testEvents.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testEvents.getHandledBy()).isEqualTo(UPDATED_HANDLED_BY);
        assertThat(testEvents.getGantryProcessed()).isEqualTo(UPDATED_GANTRY_PROCESSED);
        assertThat(testEvents.getGantrySent()).isEqualTo(UPDATED_GANTRY_SENT);
        assertThat(testEvents.getWhen()).isEqualTo(UPDATED_WHEN);
        assertThat(testEvents.getToll()).isEqualTo(UPDATED_TOLL);
        assertThat(testEvents.getRuleRcvd()).isEqualTo(UPDATED_RULE_RCVD);
        assertThat(testEvents.getWantedFor()).isEqualTo(UPDATED_WANTED_FOR);
        assertThat(testEvents.getFine()).isEqualTo(UPDATED_FINE);
        assertThat(testEvents.getLicenseIssue()).isEqualTo(UPDATED_LICENSE_ISSUE);
        assertThat(testEvents.getWantedBy()).isEqualTo(UPDATED_WANTED_BY);
        assertThat(testEvents.getRuleProcessed()).isEqualTo(UPDATED_RULE_PROCESSED);
        assertThat(testEvents.getSpeedFine()).isEqualTo(UPDATED_SPEED_FINE);
        assertThat(testEvents.getLane()).isEqualTo(UPDATED_LANE);
        assertThat(testEvents.getTagIssue()).isEqualTo(UPDATED_TAG_ISSUE);
        assertThat(testEvents.getStatusName()).isEqualTo(UPDATED_STATUS_NAME);
        assertThat(testEvents.getLicenseFine()).isEqualTo(UPDATED_LICENSE_FINE);
        assertThat(testEvents.getStolen()).isEqualTo(UPDATED_STOLEN);
        assertThat(testEvents.getWanted()).isEqualTo(UPDATED_WANTED);
        assertThat(testEvents.getRuleSent()).isEqualTo(UPDATED_RULE_SENT);
        assertThat(testEvents.getHandled()).isEqualTo(UPDATED_HANDLED);
        assertThat(testEvents.getRuleIssue()).isEqualTo(UPDATED_RULE_ISSUE);
    }

    @Test
    @Transactional
    void patchNonExistingEvents() throws Exception {
        int databaseSizeBeforeUpdate = eventsRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(eventsSearchRepository.findAll());
        events.setId(count.incrementAndGet());

        // Create the Events
        EventsDTO eventsDTO = eventsMapper.toDto(events);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEventsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, eventsDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(eventsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Events in the database
        List<Events> eventsList = eventsRepository.findAll();
        assertThat(eventsList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(eventsSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithIdMismatchEvents() throws Exception {
        int databaseSizeBeforeUpdate = eventsRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(eventsSearchRepository.findAll());
        events.setId(count.incrementAndGet());

        // Create the Events
        EventsDTO eventsDTO = eventsMapper.toDto(events);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEventsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(eventsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Events in the database
        List<Events> eventsList = eventsRepository.findAll();
        assertThat(eventsList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(eventsSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamEvents() throws Exception {
        int databaseSizeBeforeUpdate = eventsRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(eventsSearchRepository.findAll());
        events.setId(count.incrementAndGet());

        // Create the Events
        EventsDTO eventsDTO = eventsMapper.toDto(events);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEventsMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(eventsDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Events in the database
        List<Events> eventsList = eventsRepository.findAll();
        assertThat(eventsList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(eventsSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void deleteEvents() throws Exception {
        // Initialize the database
        eventsRepository.saveAndFlush(events);
        eventsRepository.save(events);
        eventsSearchRepository.save(events);

        int databaseSizeBeforeDelete = eventsRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(eventsSearchRepository.findAll());
        assertThat(searchDatabaseSizeBefore).isEqualTo(databaseSizeBeforeDelete);

        // Delete the events
        restEventsMockMvc
            .perform(delete(ENTITY_API_URL_ID, events.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Events> eventsList = eventsRepository.findAll();
        assertThat(eventsList).hasSize(databaseSizeBeforeDelete - 1);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(eventsSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore - 1);
    }

    @Test
    @Transactional
    void searchEvents() throws Exception {
        // Initialize the database
        events = eventsRepository.saveAndFlush(events);
        eventsSearchRepository.save(events);

        // Search the events
        restEventsMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + events.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(events.getId().intValue())))
            .andExpect(jsonPath("$.[*].guid").value(hasItem(DEFAULT_GUID)))
            .andExpect(jsonPath("$.[*].plate").value(hasItem(DEFAULT_PLATE)))
            .andExpect(jsonPath("$.[*].anpr").value(hasItem(DEFAULT_ANPR)))
            .andExpect(jsonPath("$.[*].rfid").value(hasItem(DEFAULT_RFID)))
            .andExpect(jsonPath("$.[*].dataStatus").value(hasItem(DEFAULT_DATA_STATUS)))
            .andExpect(jsonPath("$.[*].gantry").value(hasItem(DEFAULT_GANTRY.intValue())))
            .andExpect(jsonPath("$.[*].kph").value(hasItem(DEFAULT_KPH.intValue())))
            .andExpect(jsonPath("$.[*].ambush").value(hasItem(DEFAULT_AMBUSH.intValue())))
            .andExpect(jsonPath("$.[*].direction").value(hasItem(DEFAULT_DIRECTION.intValue())))
            .andExpect(jsonPath("$.[*].vehicle").value(hasItem(DEFAULT_VEHICLE.intValue())))
            .andExpect(jsonPath("$.[*].issue").value(hasItem(DEFAULT_ISSUE)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS)))
            .andExpect(jsonPath("$.[*].handledBy").value(hasItem(DEFAULT_HANDLED_BY.intValue())))
            .andExpect(jsonPath("$.[*].gantryProcessed").value(hasItem(DEFAULT_GANTRY_PROCESSED.intValue())))
            .andExpect(jsonPath("$.[*].gantrySent").value(hasItem(DEFAULT_GANTRY_SENT.intValue())))
            .andExpect(jsonPath("$.[*].when").value(hasItem(DEFAULT_WHEN.intValue())))
            .andExpect(jsonPath("$.[*].toll").value(hasItem(DEFAULT_TOLL.intValue())))
            .andExpect(jsonPath("$.[*].ruleRcvd").value(hasItem(DEFAULT_RULE_RCVD.intValue())))
            .andExpect(jsonPath("$.[*].wantedFor").value(hasItem(DEFAULT_WANTED_FOR)))
            .andExpect(jsonPath("$.[*].fine").value(hasItem(DEFAULT_FINE.intValue())))
            .andExpect(jsonPath("$.[*].licenseIssue").value(hasItem(DEFAULT_LICENSE_ISSUE)))
            .andExpect(jsonPath("$.[*].wantedBy").value(hasItem(DEFAULT_WANTED_BY.intValue())))
            .andExpect(jsonPath("$.[*].ruleProcessed").value(hasItem(DEFAULT_RULE_PROCESSED.intValue())))
            .andExpect(jsonPath("$.[*].speedFine").value(hasItem(DEFAULT_SPEED_FINE.intValue())))
            .andExpect(jsonPath("$.[*].lane").value(hasItem(DEFAULT_LANE.intValue())))
            .andExpect(jsonPath("$.[*].tagIssue").value(hasItem(DEFAULT_TAG_ISSUE)))
            .andExpect(jsonPath("$.[*].statusName").value(hasItem(DEFAULT_STATUS_NAME)))
            .andExpect(jsonPath("$.[*].licenseFine").value(hasItem(DEFAULT_LICENSE_FINE.intValue())))
            .andExpect(jsonPath("$.[*].stolen").value(hasItem(DEFAULT_STOLEN.intValue())))
            .andExpect(jsonPath("$.[*].wanted").value(hasItem(DEFAULT_WANTED.booleanValue())))
            .andExpect(jsonPath("$.[*].ruleSent").value(hasItem(DEFAULT_RULE_SENT.intValue())))
            .andExpect(jsonPath("$.[*].handled").value(hasItem(DEFAULT_HANDLED.intValue())))
            .andExpect(jsonPath("$.[*].ruleIssue").value(hasItem(DEFAULT_RULE_ISSUE)));
    }
}
