package com.isoft.rfid.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.isoft.rfid.IntegrationTest;
import com.isoft.rfid.domain.Task;
import com.isoft.rfid.repository.TaskRepository;
import com.isoft.rfid.repository.search.TaskSearchRepository;
import com.isoft.rfid.service.criteria.TaskCriteria;
import com.isoft.rfid.service.dto.TaskDTO;
import com.isoft.rfid.service.mapper.TaskMapper;
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
 * Integration tests for the {@link TaskResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class TaskResourceIT {

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

    private static final Double DEFAULT_TOLL = 1D;
    private static final Double UPDATED_TOLL = 2D;
    private static final Double SMALLER_TOLL = 1D - 1D;

    private static final Long DEFAULT_RULE_RCVD = 1L;
    private static final Long UPDATED_RULE_RCVD = 2L;
    private static final Long SMALLER_RULE_RCVD = 1L - 1L;

    private static final String DEFAULT_WANTED_FOR = "AAAAAAAAAA";
    private static final String UPDATED_WANTED_FOR = "BBBBBBBBBB";

    private static final Double DEFAULT_FINE = 1D;
    private static final Double UPDATED_FINE = 2D;
    private static final Double SMALLER_FINE = 1D - 1D;

    private static final String DEFAULT_LICENSE_ISSUE = "AAAAAAAAAA";
    private static final String UPDATED_LICENSE_ISSUE = "BBBBBBBBBB";

    private static final Long DEFAULT_WANTED_BY = 1L;
    private static final Long UPDATED_WANTED_BY = 2L;
    private static final Long SMALLER_WANTED_BY = 1L - 1L;

    private static final Long DEFAULT_RULE_PROCESSED = 1L;
    private static final Long UPDATED_RULE_PROCESSED = 2L;
    private static final Long SMALLER_RULE_PROCESSED = 1L - 1L;

    private static final Double DEFAULT_SPEED_FINE = 1D;
    private static final Double UPDATED_SPEED_FINE = 2D;
    private static final Double SMALLER_SPEED_FINE = 1D - 1D;

    private static final Long DEFAULT_LANE = 1L;
    private static final Long UPDATED_LANE = 2L;
    private static final Long SMALLER_LANE = 1L - 1L;

    private static final String DEFAULT_TAG_ISSUE = "AAAAAAAAAA";
    private static final String UPDATED_TAG_ISSUE = "BBBBBBBBBB";

    private static final String DEFAULT_STATUS_NAME = "AAAAAAAAAA";
    private static final String UPDATED_STATUS_NAME = "BBBBBBBBBB";

    private static final Double DEFAULT_LICENSE_FINE = 1D;
    private static final Double UPDATED_LICENSE_FINE = 2D;
    private static final Double SMALLER_LICENSE_FINE = 1D - 1D;

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

    private static final Long DEFAULT_PROCESSING_TIME = 1L;
    private static final Long UPDATED_PROCESSING_TIME = 2L;
    private static final Long SMALLER_PROCESSING_TIME = 1L - 1L;

    private static final String DEFAULT_THREAD_RAND_NO = "AAAAAAAAAA";
    private static final String UPDATED_THREAD_RAND_NO = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/tasks";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/_search/tasks";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private TaskMapper taskMapper;

    @Autowired
    private TaskSearchRepository taskSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTaskMockMvc;

    private Task task;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Task createEntity(EntityManager em) {
        Task task = new Task()
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
            .ruleIssue(DEFAULT_RULE_ISSUE)
            .processingTime(DEFAULT_PROCESSING_TIME)
            .threadRandNo(DEFAULT_THREAD_RAND_NO);
        return task;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Task createUpdatedEntity(EntityManager em) {
        Task task = new Task()
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
            .ruleIssue(UPDATED_RULE_ISSUE)
            .processingTime(UPDATED_PROCESSING_TIME)
            .threadRandNo(UPDATED_THREAD_RAND_NO);
        return task;
    }

    @AfterEach
    public void cleanupElasticSearchRepository() {
        taskSearchRepository.deleteAll();
        assertThat(taskSearchRepository.count()).isEqualTo(0);
    }

    @BeforeEach
    public void initTest() {
        task = createEntity(em);
    }

    @Test
    @Transactional
    void createTask() throws Exception {
        int databaseSizeBeforeCreate = taskRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(taskSearchRepository.findAll());
        // Create the Task
        TaskDTO taskDTO = taskMapper.toDto(task);
        restTaskMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(taskDTO)))
            .andExpect(status().isCreated());

        // Validate the Task in the database
        List<Task> taskList = taskRepository.findAll();
        assertThat(taskList).hasSize(databaseSizeBeforeCreate + 1);
        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(taskSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore + 1);
            });
        Task testTask = taskList.get(taskList.size() - 1);
        assertThat(testTask.getGuid()).isEqualTo(DEFAULT_GUID);
        assertThat(testTask.getPlate()).isEqualTo(DEFAULT_PLATE);
        assertThat(testTask.getImageLp()).isEqualTo(DEFAULT_IMAGE_LP);
        assertThat(testTask.getImageLpContentType()).isEqualTo(DEFAULT_IMAGE_LP_CONTENT_TYPE);
        assertThat(testTask.getImageThumb()).isEqualTo(DEFAULT_IMAGE_THUMB);
        assertThat(testTask.getImageThumbContentType()).isEqualTo(DEFAULT_IMAGE_THUMB_CONTENT_TYPE);
        assertThat(testTask.getAnpr()).isEqualTo(DEFAULT_ANPR);
        assertThat(testTask.getRfid()).isEqualTo(DEFAULT_RFID);
        assertThat(testTask.getDataStatus()).isEqualTo(DEFAULT_DATA_STATUS);
        assertThat(testTask.getGantry()).isEqualTo(DEFAULT_GANTRY);
        assertThat(testTask.getKph()).isEqualTo(DEFAULT_KPH);
        assertThat(testTask.getAmbush()).isEqualTo(DEFAULT_AMBUSH);
        assertThat(testTask.getDirection()).isEqualTo(DEFAULT_DIRECTION);
        assertThat(testTask.getVehicle()).isEqualTo(DEFAULT_VEHICLE);
        assertThat(testTask.getIssue()).isEqualTo(DEFAULT_ISSUE);
        assertThat(testTask.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testTask.getHandledBy()).isEqualTo(DEFAULT_HANDLED_BY);
        assertThat(testTask.getGantryProcessed()).isEqualTo(DEFAULT_GANTRY_PROCESSED);
        assertThat(testTask.getGantrySent()).isEqualTo(DEFAULT_GANTRY_SENT);
        assertThat(testTask.getWhen()).isEqualTo(DEFAULT_WHEN);
        assertThat(testTask.getToll()).isEqualTo(DEFAULT_TOLL);
        assertThat(testTask.getRuleRcvd()).isEqualTo(DEFAULT_RULE_RCVD);
        assertThat(testTask.getWantedFor()).isEqualTo(DEFAULT_WANTED_FOR);
        assertThat(testTask.getFine()).isEqualTo(DEFAULT_FINE);
        assertThat(testTask.getLicenseIssue()).isEqualTo(DEFAULT_LICENSE_ISSUE);
        assertThat(testTask.getWantedBy()).isEqualTo(DEFAULT_WANTED_BY);
        assertThat(testTask.getRuleProcessed()).isEqualTo(DEFAULT_RULE_PROCESSED);
        assertThat(testTask.getSpeedFine()).isEqualTo(DEFAULT_SPEED_FINE);
        assertThat(testTask.getLane()).isEqualTo(DEFAULT_LANE);
        assertThat(testTask.getTagIssue()).isEqualTo(DEFAULT_TAG_ISSUE);
        assertThat(testTask.getStatusName()).isEqualTo(DEFAULT_STATUS_NAME);
        assertThat(testTask.getLicenseFine()).isEqualTo(DEFAULT_LICENSE_FINE);
        assertThat(testTask.getStolen()).isEqualTo(DEFAULT_STOLEN);
        assertThat(testTask.getWanted()).isEqualTo(DEFAULT_WANTED);
        assertThat(testTask.getRuleSent()).isEqualTo(DEFAULT_RULE_SENT);
        assertThat(testTask.getHandled()).isEqualTo(DEFAULT_HANDLED);
        assertThat(testTask.getRuleIssue()).isEqualTo(DEFAULT_RULE_ISSUE);
        assertThat(testTask.getProcessingTime()).isEqualTo(DEFAULT_PROCESSING_TIME);
        assertThat(testTask.getThreadRandNo()).isEqualTo(DEFAULT_THREAD_RAND_NO);
    }

    @Test
    @Transactional
    void createTaskWithExistingId() throws Exception {
        // Create the Task with an existing ID
        task.setId(1L);
        TaskDTO taskDTO = taskMapper.toDto(task);

        int databaseSizeBeforeCreate = taskRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(taskSearchRepository.findAll());

        // An entity with an existing ID cannot be created, so this API call must fail
        restTaskMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(taskDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Task in the database
        List<Task> taskList = taskRepository.findAll();
        assertThat(taskList).hasSize(databaseSizeBeforeCreate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(taskSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkGuidIsRequired() throws Exception {
        int databaseSizeBeforeTest = taskRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(taskSearchRepository.findAll());
        // set the field null
        task.setGuid(null);

        // Create the Task, which fails.
        TaskDTO taskDTO = taskMapper.toDto(task);

        restTaskMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(taskDTO)))
            .andExpect(status().isBadRequest());

        List<Task> taskList = taskRepository.findAll();
        assertThat(taskList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(taskSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkDataStatusIsRequired() throws Exception {
        int databaseSizeBeforeTest = taskRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(taskSearchRepository.findAll());
        // set the field null
        task.setDataStatus(null);

        // Create the Task, which fails.
        TaskDTO taskDTO = taskMapper.toDto(task);

        restTaskMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(taskDTO)))
            .andExpect(status().isBadRequest());

        List<Task> taskList = taskRepository.findAll();
        assertThat(taskList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(taskSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkGantryIsRequired() throws Exception {
        int databaseSizeBeforeTest = taskRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(taskSearchRepository.findAll());
        // set the field null
        task.setGantry(null);

        // Create the Task, which fails.
        TaskDTO taskDTO = taskMapper.toDto(task);

        restTaskMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(taskDTO)))
            .andExpect(status().isBadRequest());

        List<Task> taskList = taskRepository.findAll();
        assertThat(taskList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(taskSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkVehicleIsRequired() throws Exception {
        int databaseSizeBeforeTest = taskRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(taskSearchRepository.findAll());
        // set the field null
        task.setVehicle(null);

        // Create the Task, which fails.
        TaskDTO taskDTO = taskMapper.toDto(task);

        restTaskMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(taskDTO)))
            .andExpect(status().isBadRequest());

        List<Task> taskList = taskRepository.findAll();
        assertThat(taskList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(taskSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkLaneIsRequired() throws Exception {
        int databaseSizeBeforeTest = taskRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(taskSearchRepository.findAll());
        // set the field null
        task.setLane(null);

        // Create the Task, which fails.
        TaskDTO taskDTO = taskMapper.toDto(task);

        restTaskMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(taskDTO)))
            .andExpect(status().isBadRequest());

        List<Task> taskList = taskRepository.findAll();
        assertThat(taskList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(taskSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void getAllTasks() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);

        // Get all the taskList
        restTaskMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(task.getId().intValue())))
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
            .andExpect(jsonPath("$.[*].toll").value(hasItem(DEFAULT_TOLL.doubleValue())))
            .andExpect(jsonPath("$.[*].ruleRcvd").value(hasItem(DEFAULT_RULE_RCVD.intValue())))
            .andExpect(jsonPath("$.[*].wantedFor").value(hasItem(DEFAULT_WANTED_FOR)))
            .andExpect(jsonPath("$.[*].fine").value(hasItem(DEFAULT_FINE.doubleValue())))
            .andExpect(jsonPath("$.[*].licenseIssue").value(hasItem(DEFAULT_LICENSE_ISSUE)))
            .andExpect(jsonPath("$.[*].wantedBy").value(hasItem(DEFAULT_WANTED_BY.intValue())))
            .andExpect(jsonPath("$.[*].ruleProcessed").value(hasItem(DEFAULT_RULE_PROCESSED.intValue())))
            .andExpect(jsonPath("$.[*].speedFine").value(hasItem(DEFAULT_SPEED_FINE.doubleValue())))
            .andExpect(jsonPath("$.[*].lane").value(hasItem(DEFAULT_LANE.intValue())))
            .andExpect(jsonPath("$.[*].tagIssue").value(hasItem(DEFAULT_TAG_ISSUE)))
            .andExpect(jsonPath("$.[*].statusName").value(hasItem(DEFAULT_STATUS_NAME)))
            .andExpect(jsonPath("$.[*].licenseFine").value(hasItem(DEFAULT_LICENSE_FINE.doubleValue())))
            .andExpect(jsonPath("$.[*].stolen").value(hasItem(DEFAULT_STOLEN.intValue())))
            .andExpect(jsonPath("$.[*].wanted").value(hasItem(DEFAULT_WANTED.booleanValue())))
            .andExpect(jsonPath("$.[*].ruleSent").value(hasItem(DEFAULT_RULE_SENT.intValue())))
            .andExpect(jsonPath("$.[*].handled").value(hasItem(DEFAULT_HANDLED.intValue())))
            .andExpect(jsonPath("$.[*].ruleIssue").value(hasItem(DEFAULT_RULE_ISSUE)))
            .andExpect(jsonPath("$.[*].processingTime").value(hasItem(DEFAULT_PROCESSING_TIME.intValue())))
            .andExpect(jsonPath("$.[*].threadRandNo").value(hasItem(DEFAULT_THREAD_RAND_NO)));
    }

    @Test
    @Transactional
    void getTask() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);

        // Get the task
        restTaskMockMvc
            .perform(get(ENTITY_API_URL_ID, task.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(task.getId().intValue()))
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
            .andExpect(jsonPath("$.toll").value(DEFAULT_TOLL.doubleValue()))
            .andExpect(jsonPath("$.ruleRcvd").value(DEFAULT_RULE_RCVD.intValue()))
            .andExpect(jsonPath("$.wantedFor").value(DEFAULT_WANTED_FOR))
            .andExpect(jsonPath("$.fine").value(DEFAULT_FINE.doubleValue()))
            .andExpect(jsonPath("$.licenseIssue").value(DEFAULT_LICENSE_ISSUE))
            .andExpect(jsonPath("$.wantedBy").value(DEFAULT_WANTED_BY.intValue()))
            .andExpect(jsonPath("$.ruleProcessed").value(DEFAULT_RULE_PROCESSED.intValue()))
            .andExpect(jsonPath("$.speedFine").value(DEFAULT_SPEED_FINE.doubleValue()))
            .andExpect(jsonPath("$.lane").value(DEFAULT_LANE.intValue()))
            .andExpect(jsonPath("$.tagIssue").value(DEFAULT_TAG_ISSUE))
            .andExpect(jsonPath("$.statusName").value(DEFAULT_STATUS_NAME))
            .andExpect(jsonPath("$.licenseFine").value(DEFAULT_LICENSE_FINE.doubleValue()))
            .andExpect(jsonPath("$.stolen").value(DEFAULT_STOLEN.intValue()))
            .andExpect(jsonPath("$.wanted").value(DEFAULT_WANTED.booleanValue()))
            .andExpect(jsonPath("$.ruleSent").value(DEFAULT_RULE_SENT.intValue()))
            .andExpect(jsonPath("$.handled").value(DEFAULT_HANDLED.intValue()))
            .andExpect(jsonPath("$.ruleIssue").value(DEFAULT_RULE_ISSUE))
            .andExpect(jsonPath("$.processingTime").value(DEFAULT_PROCESSING_TIME.intValue()))
            .andExpect(jsonPath("$.threadRandNo").value(DEFAULT_THREAD_RAND_NO));
    }

    @Test
    @Transactional
    void getTasksByIdFiltering() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);

        Long id = task.getId();

        defaultTaskShouldBeFound("id.equals=" + id);
        defaultTaskShouldNotBeFound("id.notEquals=" + id);

        defaultTaskShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultTaskShouldNotBeFound("id.greaterThan=" + id);

        defaultTaskShouldBeFound("id.lessThanOrEqual=" + id);
        defaultTaskShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllTasksByGuidIsEqualToSomething() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);

        // Get all the taskList where guid equals to DEFAULT_GUID
        defaultTaskShouldBeFound("guid.equals=" + DEFAULT_GUID);

        // Get all the taskList where guid equals to UPDATED_GUID
        defaultTaskShouldNotBeFound("guid.equals=" + UPDATED_GUID);
    }

    @Test
    @Transactional
    void getAllTasksByGuidIsInShouldWork() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);

        // Get all the taskList where guid in DEFAULT_GUID or UPDATED_GUID
        defaultTaskShouldBeFound("guid.in=" + DEFAULT_GUID + "," + UPDATED_GUID);

        // Get all the taskList where guid equals to UPDATED_GUID
        defaultTaskShouldNotBeFound("guid.in=" + UPDATED_GUID);
    }

    @Test
    @Transactional
    void getAllTasksByGuidIsNullOrNotNull() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);

        // Get all the taskList where guid is not null
        defaultTaskShouldBeFound("guid.specified=true");

        // Get all the taskList where guid is null
        defaultTaskShouldNotBeFound("guid.specified=false");
    }

    @Test
    @Transactional
    void getAllTasksByGuidContainsSomething() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);

        // Get all the taskList where guid contains DEFAULT_GUID
        defaultTaskShouldBeFound("guid.contains=" + DEFAULT_GUID);

        // Get all the taskList where guid contains UPDATED_GUID
        defaultTaskShouldNotBeFound("guid.contains=" + UPDATED_GUID);
    }

    @Test
    @Transactional
    void getAllTasksByGuidNotContainsSomething() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);

        // Get all the taskList where guid does not contain DEFAULT_GUID
        defaultTaskShouldNotBeFound("guid.doesNotContain=" + DEFAULT_GUID);

        // Get all the taskList where guid does not contain UPDATED_GUID
        defaultTaskShouldBeFound("guid.doesNotContain=" + UPDATED_GUID);
    }

    @Test
    @Transactional
    void getAllTasksByPlateIsEqualToSomething() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);

        // Get all the taskList where plate equals to DEFAULT_PLATE
        defaultTaskShouldBeFound("plate.equals=" + DEFAULT_PLATE);

        // Get all the taskList where plate equals to UPDATED_PLATE
        defaultTaskShouldNotBeFound("plate.equals=" + UPDATED_PLATE);
    }

    @Test
    @Transactional
    void getAllTasksByPlateIsInShouldWork() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);

        // Get all the taskList where plate in DEFAULT_PLATE or UPDATED_PLATE
        defaultTaskShouldBeFound("plate.in=" + DEFAULT_PLATE + "," + UPDATED_PLATE);

        // Get all the taskList where plate equals to UPDATED_PLATE
        defaultTaskShouldNotBeFound("plate.in=" + UPDATED_PLATE);
    }

    @Test
    @Transactional
    void getAllTasksByPlateIsNullOrNotNull() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);

        // Get all the taskList where plate is not null
        defaultTaskShouldBeFound("plate.specified=true");

        // Get all the taskList where plate is null
        defaultTaskShouldNotBeFound("plate.specified=false");
    }

    @Test
    @Transactional
    void getAllTasksByPlateContainsSomething() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);

        // Get all the taskList where plate contains DEFAULT_PLATE
        defaultTaskShouldBeFound("plate.contains=" + DEFAULT_PLATE);

        // Get all the taskList where plate contains UPDATED_PLATE
        defaultTaskShouldNotBeFound("plate.contains=" + UPDATED_PLATE);
    }

    @Test
    @Transactional
    void getAllTasksByPlateNotContainsSomething() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);

        // Get all the taskList where plate does not contain DEFAULT_PLATE
        defaultTaskShouldNotBeFound("plate.doesNotContain=" + DEFAULT_PLATE);

        // Get all the taskList where plate does not contain UPDATED_PLATE
        defaultTaskShouldBeFound("plate.doesNotContain=" + UPDATED_PLATE);
    }

    @Test
    @Transactional
    void getAllTasksByAnprIsEqualToSomething() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);

        // Get all the taskList where anpr equals to DEFAULT_ANPR
        defaultTaskShouldBeFound("anpr.equals=" + DEFAULT_ANPR);

        // Get all the taskList where anpr equals to UPDATED_ANPR
        defaultTaskShouldNotBeFound("anpr.equals=" + UPDATED_ANPR);
    }

    @Test
    @Transactional
    void getAllTasksByAnprIsInShouldWork() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);

        // Get all the taskList where anpr in DEFAULT_ANPR or UPDATED_ANPR
        defaultTaskShouldBeFound("anpr.in=" + DEFAULT_ANPR + "," + UPDATED_ANPR);

        // Get all the taskList where anpr equals to UPDATED_ANPR
        defaultTaskShouldNotBeFound("anpr.in=" + UPDATED_ANPR);
    }

    @Test
    @Transactional
    void getAllTasksByAnprIsNullOrNotNull() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);

        // Get all the taskList where anpr is not null
        defaultTaskShouldBeFound("anpr.specified=true");

        // Get all the taskList where anpr is null
        defaultTaskShouldNotBeFound("anpr.specified=false");
    }

    @Test
    @Transactional
    void getAllTasksByAnprContainsSomething() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);

        // Get all the taskList where anpr contains DEFAULT_ANPR
        defaultTaskShouldBeFound("anpr.contains=" + DEFAULT_ANPR);

        // Get all the taskList where anpr contains UPDATED_ANPR
        defaultTaskShouldNotBeFound("anpr.contains=" + UPDATED_ANPR);
    }

    @Test
    @Transactional
    void getAllTasksByAnprNotContainsSomething() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);

        // Get all the taskList where anpr does not contain DEFAULT_ANPR
        defaultTaskShouldNotBeFound("anpr.doesNotContain=" + DEFAULT_ANPR);

        // Get all the taskList where anpr does not contain UPDATED_ANPR
        defaultTaskShouldBeFound("anpr.doesNotContain=" + UPDATED_ANPR);
    }

    @Test
    @Transactional
    void getAllTasksByRfidIsEqualToSomething() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);

        // Get all the taskList where rfid equals to DEFAULT_RFID
        defaultTaskShouldBeFound("rfid.equals=" + DEFAULT_RFID);

        // Get all the taskList where rfid equals to UPDATED_RFID
        defaultTaskShouldNotBeFound("rfid.equals=" + UPDATED_RFID);
    }

    @Test
    @Transactional
    void getAllTasksByRfidIsInShouldWork() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);

        // Get all the taskList where rfid in DEFAULT_RFID or UPDATED_RFID
        defaultTaskShouldBeFound("rfid.in=" + DEFAULT_RFID + "," + UPDATED_RFID);

        // Get all the taskList where rfid equals to UPDATED_RFID
        defaultTaskShouldNotBeFound("rfid.in=" + UPDATED_RFID);
    }

    @Test
    @Transactional
    void getAllTasksByRfidIsNullOrNotNull() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);

        // Get all the taskList where rfid is not null
        defaultTaskShouldBeFound("rfid.specified=true");

        // Get all the taskList where rfid is null
        defaultTaskShouldNotBeFound("rfid.specified=false");
    }

    @Test
    @Transactional
    void getAllTasksByRfidContainsSomething() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);

        // Get all the taskList where rfid contains DEFAULT_RFID
        defaultTaskShouldBeFound("rfid.contains=" + DEFAULT_RFID);

        // Get all the taskList where rfid contains UPDATED_RFID
        defaultTaskShouldNotBeFound("rfid.contains=" + UPDATED_RFID);
    }

    @Test
    @Transactional
    void getAllTasksByRfidNotContainsSomething() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);

        // Get all the taskList where rfid does not contain DEFAULT_RFID
        defaultTaskShouldNotBeFound("rfid.doesNotContain=" + DEFAULT_RFID);

        // Get all the taskList where rfid does not contain UPDATED_RFID
        defaultTaskShouldBeFound("rfid.doesNotContain=" + UPDATED_RFID);
    }

    @Test
    @Transactional
    void getAllTasksByDataStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);

        // Get all the taskList where dataStatus equals to DEFAULT_DATA_STATUS
        defaultTaskShouldBeFound("dataStatus.equals=" + DEFAULT_DATA_STATUS);

        // Get all the taskList where dataStatus equals to UPDATED_DATA_STATUS
        defaultTaskShouldNotBeFound("dataStatus.equals=" + UPDATED_DATA_STATUS);
    }

    @Test
    @Transactional
    void getAllTasksByDataStatusIsInShouldWork() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);

        // Get all the taskList where dataStatus in DEFAULT_DATA_STATUS or UPDATED_DATA_STATUS
        defaultTaskShouldBeFound("dataStatus.in=" + DEFAULT_DATA_STATUS + "," + UPDATED_DATA_STATUS);

        // Get all the taskList where dataStatus equals to UPDATED_DATA_STATUS
        defaultTaskShouldNotBeFound("dataStatus.in=" + UPDATED_DATA_STATUS);
    }

    @Test
    @Transactional
    void getAllTasksByDataStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);

        // Get all the taskList where dataStatus is not null
        defaultTaskShouldBeFound("dataStatus.specified=true");

        // Get all the taskList where dataStatus is null
        defaultTaskShouldNotBeFound("dataStatus.specified=false");
    }

    @Test
    @Transactional
    void getAllTasksByDataStatusContainsSomething() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);

        // Get all the taskList where dataStatus contains DEFAULT_DATA_STATUS
        defaultTaskShouldBeFound("dataStatus.contains=" + DEFAULT_DATA_STATUS);

        // Get all the taskList where dataStatus contains UPDATED_DATA_STATUS
        defaultTaskShouldNotBeFound("dataStatus.contains=" + UPDATED_DATA_STATUS);
    }

    @Test
    @Transactional
    void getAllTasksByDataStatusNotContainsSomething() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);

        // Get all the taskList where dataStatus does not contain DEFAULT_DATA_STATUS
        defaultTaskShouldNotBeFound("dataStatus.doesNotContain=" + DEFAULT_DATA_STATUS);

        // Get all the taskList where dataStatus does not contain UPDATED_DATA_STATUS
        defaultTaskShouldBeFound("dataStatus.doesNotContain=" + UPDATED_DATA_STATUS);
    }

    @Test
    @Transactional
    void getAllTasksByGantryIsEqualToSomething() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);

        // Get all the taskList where gantry equals to DEFAULT_GANTRY
        defaultTaskShouldBeFound("gantry.equals=" + DEFAULT_GANTRY);

        // Get all the taskList where gantry equals to UPDATED_GANTRY
        defaultTaskShouldNotBeFound("gantry.equals=" + UPDATED_GANTRY);
    }

    @Test
    @Transactional
    void getAllTasksByGantryIsInShouldWork() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);

        // Get all the taskList where gantry in DEFAULT_GANTRY or UPDATED_GANTRY
        defaultTaskShouldBeFound("gantry.in=" + DEFAULT_GANTRY + "," + UPDATED_GANTRY);

        // Get all the taskList where gantry equals to UPDATED_GANTRY
        defaultTaskShouldNotBeFound("gantry.in=" + UPDATED_GANTRY);
    }

    @Test
    @Transactional
    void getAllTasksByGantryIsNullOrNotNull() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);

        // Get all the taskList where gantry is not null
        defaultTaskShouldBeFound("gantry.specified=true");

        // Get all the taskList where gantry is null
        defaultTaskShouldNotBeFound("gantry.specified=false");
    }

    @Test
    @Transactional
    void getAllTasksByGantryIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);

        // Get all the taskList where gantry is greater than or equal to DEFAULT_GANTRY
        defaultTaskShouldBeFound("gantry.greaterThanOrEqual=" + DEFAULT_GANTRY);

        // Get all the taskList where gantry is greater than or equal to UPDATED_GANTRY
        defaultTaskShouldNotBeFound("gantry.greaterThanOrEqual=" + UPDATED_GANTRY);
    }

    @Test
    @Transactional
    void getAllTasksByGantryIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);

        // Get all the taskList where gantry is less than or equal to DEFAULT_GANTRY
        defaultTaskShouldBeFound("gantry.lessThanOrEqual=" + DEFAULT_GANTRY);

        // Get all the taskList where gantry is less than or equal to SMALLER_GANTRY
        defaultTaskShouldNotBeFound("gantry.lessThanOrEqual=" + SMALLER_GANTRY);
    }

    @Test
    @Transactional
    void getAllTasksByGantryIsLessThanSomething() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);

        // Get all the taskList where gantry is less than DEFAULT_GANTRY
        defaultTaskShouldNotBeFound("gantry.lessThan=" + DEFAULT_GANTRY);

        // Get all the taskList where gantry is less than UPDATED_GANTRY
        defaultTaskShouldBeFound("gantry.lessThan=" + UPDATED_GANTRY);
    }

    @Test
    @Transactional
    void getAllTasksByGantryIsGreaterThanSomething() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);

        // Get all the taskList where gantry is greater than DEFAULT_GANTRY
        defaultTaskShouldNotBeFound("gantry.greaterThan=" + DEFAULT_GANTRY);

        // Get all the taskList where gantry is greater than SMALLER_GANTRY
        defaultTaskShouldBeFound("gantry.greaterThan=" + SMALLER_GANTRY);
    }

    @Test
    @Transactional
    void getAllTasksByKphIsEqualToSomething() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);

        // Get all the taskList where kph equals to DEFAULT_KPH
        defaultTaskShouldBeFound("kph.equals=" + DEFAULT_KPH);

        // Get all the taskList where kph equals to UPDATED_KPH
        defaultTaskShouldNotBeFound("kph.equals=" + UPDATED_KPH);
    }

    @Test
    @Transactional
    void getAllTasksByKphIsInShouldWork() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);

        // Get all the taskList where kph in DEFAULT_KPH or UPDATED_KPH
        defaultTaskShouldBeFound("kph.in=" + DEFAULT_KPH + "," + UPDATED_KPH);

        // Get all the taskList where kph equals to UPDATED_KPH
        defaultTaskShouldNotBeFound("kph.in=" + UPDATED_KPH);
    }

    @Test
    @Transactional
    void getAllTasksByKphIsNullOrNotNull() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);

        // Get all the taskList where kph is not null
        defaultTaskShouldBeFound("kph.specified=true");

        // Get all the taskList where kph is null
        defaultTaskShouldNotBeFound("kph.specified=false");
    }

    @Test
    @Transactional
    void getAllTasksByKphIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);

        // Get all the taskList where kph is greater than or equal to DEFAULT_KPH
        defaultTaskShouldBeFound("kph.greaterThanOrEqual=" + DEFAULT_KPH);

        // Get all the taskList where kph is greater than or equal to UPDATED_KPH
        defaultTaskShouldNotBeFound("kph.greaterThanOrEqual=" + UPDATED_KPH);
    }

    @Test
    @Transactional
    void getAllTasksByKphIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);

        // Get all the taskList where kph is less than or equal to DEFAULT_KPH
        defaultTaskShouldBeFound("kph.lessThanOrEqual=" + DEFAULT_KPH);

        // Get all the taskList where kph is less than or equal to SMALLER_KPH
        defaultTaskShouldNotBeFound("kph.lessThanOrEqual=" + SMALLER_KPH);
    }

    @Test
    @Transactional
    void getAllTasksByKphIsLessThanSomething() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);

        // Get all the taskList where kph is less than DEFAULT_KPH
        defaultTaskShouldNotBeFound("kph.lessThan=" + DEFAULT_KPH);

        // Get all the taskList where kph is less than UPDATED_KPH
        defaultTaskShouldBeFound("kph.lessThan=" + UPDATED_KPH);
    }

    @Test
    @Transactional
    void getAllTasksByKphIsGreaterThanSomething() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);

        // Get all the taskList where kph is greater than DEFAULT_KPH
        defaultTaskShouldNotBeFound("kph.greaterThan=" + DEFAULT_KPH);

        // Get all the taskList where kph is greater than SMALLER_KPH
        defaultTaskShouldBeFound("kph.greaterThan=" + SMALLER_KPH);
    }

    @Test
    @Transactional
    void getAllTasksByAmbushIsEqualToSomething() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);

        // Get all the taskList where ambush equals to DEFAULT_AMBUSH
        defaultTaskShouldBeFound("ambush.equals=" + DEFAULT_AMBUSH);

        // Get all the taskList where ambush equals to UPDATED_AMBUSH
        defaultTaskShouldNotBeFound("ambush.equals=" + UPDATED_AMBUSH);
    }

    @Test
    @Transactional
    void getAllTasksByAmbushIsInShouldWork() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);

        // Get all the taskList where ambush in DEFAULT_AMBUSH or UPDATED_AMBUSH
        defaultTaskShouldBeFound("ambush.in=" + DEFAULT_AMBUSH + "," + UPDATED_AMBUSH);

        // Get all the taskList where ambush equals to UPDATED_AMBUSH
        defaultTaskShouldNotBeFound("ambush.in=" + UPDATED_AMBUSH);
    }

    @Test
    @Transactional
    void getAllTasksByAmbushIsNullOrNotNull() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);

        // Get all the taskList where ambush is not null
        defaultTaskShouldBeFound("ambush.specified=true");

        // Get all the taskList where ambush is null
        defaultTaskShouldNotBeFound("ambush.specified=false");
    }

    @Test
    @Transactional
    void getAllTasksByAmbushIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);

        // Get all the taskList where ambush is greater than or equal to DEFAULT_AMBUSH
        defaultTaskShouldBeFound("ambush.greaterThanOrEqual=" + DEFAULT_AMBUSH);

        // Get all the taskList where ambush is greater than or equal to UPDATED_AMBUSH
        defaultTaskShouldNotBeFound("ambush.greaterThanOrEqual=" + UPDATED_AMBUSH);
    }

    @Test
    @Transactional
    void getAllTasksByAmbushIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);

        // Get all the taskList where ambush is less than or equal to DEFAULT_AMBUSH
        defaultTaskShouldBeFound("ambush.lessThanOrEqual=" + DEFAULT_AMBUSH);

        // Get all the taskList where ambush is less than or equal to SMALLER_AMBUSH
        defaultTaskShouldNotBeFound("ambush.lessThanOrEqual=" + SMALLER_AMBUSH);
    }

    @Test
    @Transactional
    void getAllTasksByAmbushIsLessThanSomething() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);

        // Get all the taskList where ambush is less than DEFAULT_AMBUSH
        defaultTaskShouldNotBeFound("ambush.lessThan=" + DEFAULT_AMBUSH);

        // Get all the taskList where ambush is less than UPDATED_AMBUSH
        defaultTaskShouldBeFound("ambush.lessThan=" + UPDATED_AMBUSH);
    }

    @Test
    @Transactional
    void getAllTasksByAmbushIsGreaterThanSomething() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);

        // Get all the taskList where ambush is greater than DEFAULT_AMBUSH
        defaultTaskShouldNotBeFound("ambush.greaterThan=" + DEFAULT_AMBUSH);

        // Get all the taskList where ambush is greater than SMALLER_AMBUSH
        defaultTaskShouldBeFound("ambush.greaterThan=" + SMALLER_AMBUSH);
    }

    @Test
    @Transactional
    void getAllTasksByDirectionIsEqualToSomething() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);

        // Get all the taskList where direction equals to DEFAULT_DIRECTION
        defaultTaskShouldBeFound("direction.equals=" + DEFAULT_DIRECTION);

        // Get all the taskList where direction equals to UPDATED_DIRECTION
        defaultTaskShouldNotBeFound("direction.equals=" + UPDATED_DIRECTION);
    }

    @Test
    @Transactional
    void getAllTasksByDirectionIsInShouldWork() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);

        // Get all the taskList where direction in DEFAULT_DIRECTION or UPDATED_DIRECTION
        defaultTaskShouldBeFound("direction.in=" + DEFAULT_DIRECTION + "," + UPDATED_DIRECTION);

        // Get all the taskList where direction equals to UPDATED_DIRECTION
        defaultTaskShouldNotBeFound("direction.in=" + UPDATED_DIRECTION);
    }

    @Test
    @Transactional
    void getAllTasksByDirectionIsNullOrNotNull() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);

        // Get all the taskList where direction is not null
        defaultTaskShouldBeFound("direction.specified=true");

        // Get all the taskList where direction is null
        defaultTaskShouldNotBeFound("direction.specified=false");
    }

    @Test
    @Transactional
    void getAllTasksByDirectionIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);

        // Get all the taskList where direction is greater than or equal to DEFAULT_DIRECTION
        defaultTaskShouldBeFound("direction.greaterThanOrEqual=" + DEFAULT_DIRECTION);

        // Get all the taskList where direction is greater than or equal to UPDATED_DIRECTION
        defaultTaskShouldNotBeFound("direction.greaterThanOrEqual=" + UPDATED_DIRECTION);
    }

    @Test
    @Transactional
    void getAllTasksByDirectionIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);

        // Get all the taskList where direction is less than or equal to DEFAULT_DIRECTION
        defaultTaskShouldBeFound("direction.lessThanOrEqual=" + DEFAULT_DIRECTION);

        // Get all the taskList where direction is less than or equal to SMALLER_DIRECTION
        defaultTaskShouldNotBeFound("direction.lessThanOrEqual=" + SMALLER_DIRECTION);
    }

    @Test
    @Transactional
    void getAllTasksByDirectionIsLessThanSomething() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);

        // Get all the taskList where direction is less than DEFAULT_DIRECTION
        defaultTaskShouldNotBeFound("direction.lessThan=" + DEFAULT_DIRECTION);

        // Get all the taskList where direction is less than UPDATED_DIRECTION
        defaultTaskShouldBeFound("direction.lessThan=" + UPDATED_DIRECTION);
    }

    @Test
    @Transactional
    void getAllTasksByDirectionIsGreaterThanSomething() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);

        // Get all the taskList where direction is greater than DEFAULT_DIRECTION
        defaultTaskShouldNotBeFound("direction.greaterThan=" + DEFAULT_DIRECTION);

        // Get all the taskList where direction is greater than SMALLER_DIRECTION
        defaultTaskShouldBeFound("direction.greaterThan=" + SMALLER_DIRECTION);
    }

    @Test
    @Transactional
    void getAllTasksByVehicleIsEqualToSomething() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);

        // Get all the taskList where vehicle equals to DEFAULT_VEHICLE
        defaultTaskShouldBeFound("vehicle.equals=" + DEFAULT_VEHICLE);

        // Get all the taskList where vehicle equals to UPDATED_VEHICLE
        defaultTaskShouldNotBeFound("vehicle.equals=" + UPDATED_VEHICLE);
    }

    @Test
    @Transactional
    void getAllTasksByVehicleIsInShouldWork() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);

        // Get all the taskList where vehicle in DEFAULT_VEHICLE or UPDATED_VEHICLE
        defaultTaskShouldBeFound("vehicle.in=" + DEFAULT_VEHICLE + "," + UPDATED_VEHICLE);

        // Get all the taskList where vehicle equals to UPDATED_VEHICLE
        defaultTaskShouldNotBeFound("vehicle.in=" + UPDATED_VEHICLE);
    }

    @Test
    @Transactional
    void getAllTasksByVehicleIsNullOrNotNull() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);

        // Get all the taskList where vehicle is not null
        defaultTaskShouldBeFound("vehicle.specified=true");

        // Get all the taskList where vehicle is null
        defaultTaskShouldNotBeFound("vehicle.specified=false");
    }

    @Test
    @Transactional
    void getAllTasksByVehicleIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);

        // Get all the taskList where vehicle is greater than or equal to DEFAULT_VEHICLE
        defaultTaskShouldBeFound("vehicle.greaterThanOrEqual=" + DEFAULT_VEHICLE);

        // Get all the taskList where vehicle is greater than or equal to UPDATED_VEHICLE
        defaultTaskShouldNotBeFound("vehicle.greaterThanOrEqual=" + UPDATED_VEHICLE);
    }

    @Test
    @Transactional
    void getAllTasksByVehicleIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);

        // Get all the taskList where vehicle is less than or equal to DEFAULT_VEHICLE
        defaultTaskShouldBeFound("vehicle.lessThanOrEqual=" + DEFAULT_VEHICLE);

        // Get all the taskList where vehicle is less than or equal to SMALLER_VEHICLE
        defaultTaskShouldNotBeFound("vehicle.lessThanOrEqual=" + SMALLER_VEHICLE);
    }

    @Test
    @Transactional
    void getAllTasksByVehicleIsLessThanSomething() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);

        // Get all the taskList where vehicle is less than DEFAULT_VEHICLE
        defaultTaskShouldNotBeFound("vehicle.lessThan=" + DEFAULT_VEHICLE);

        // Get all the taskList where vehicle is less than UPDATED_VEHICLE
        defaultTaskShouldBeFound("vehicle.lessThan=" + UPDATED_VEHICLE);
    }

    @Test
    @Transactional
    void getAllTasksByVehicleIsGreaterThanSomething() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);

        // Get all the taskList where vehicle is greater than DEFAULT_VEHICLE
        defaultTaskShouldNotBeFound("vehicle.greaterThan=" + DEFAULT_VEHICLE);

        // Get all the taskList where vehicle is greater than SMALLER_VEHICLE
        defaultTaskShouldBeFound("vehicle.greaterThan=" + SMALLER_VEHICLE);
    }

    @Test
    @Transactional
    void getAllTasksByIssueIsEqualToSomething() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);

        // Get all the taskList where issue equals to DEFAULT_ISSUE
        defaultTaskShouldBeFound("issue.equals=" + DEFAULT_ISSUE);

        // Get all the taskList where issue equals to UPDATED_ISSUE
        defaultTaskShouldNotBeFound("issue.equals=" + UPDATED_ISSUE);
    }

    @Test
    @Transactional
    void getAllTasksByIssueIsInShouldWork() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);

        // Get all the taskList where issue in DEFAULT_ISSUE or UPDATED_ISSUE
        defaultTaskShouldBeFound("issue.in=" + DEFAULT_ISSUE + "," + UPDATED_ISSUE);

        // Get all the taskList where issue equals to UPDATED_ISSUE
        defaultTaskShouldNotBeFound("issue.in=" + UPDATED_ISSUE);
    }

    @Test
    @Transactional
    void getAllTasksByIssueIsNullOrNotNull() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);

        // Get all the taskList where issue is not null
        defaultTaskShouldBeFound("issue.specified=true");

        // Get all the taskList where issue is null
        defaultTaskShouldNotBeFound("issue.specified=false");
    }

    @Test
    @Transactional
    void getAllTasksByIssueContainsSomething() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);

        // Get all the taskList where issue contains DEFAULT_ISSUE
        defaultTaskShouldBeFound("issue.contains=" + DEFAULT_ISSUE);

        // Get all the taskList where issue contains UPDATED_ISSUE
        defaultTaskShouldNotBeFound("issue.contains=" + UPDATED_ISSUE);
    }

    @Test
    @Transactional
    void getAllTasksByIssueNotContainsSomething() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);

        // Get all the taskList where issue does not contain DEFAULT_ISSUE
        defaultTaskShouldNotBeFound("issue.doesNotContain=" + DEFAULT_ISSUE);

        // Get all the taskList where issue does not contain UPDATED_ISSUE
        defaultTaskShouldBeFound("issue.doesNotContain=" + UPDATED_ISSUE);
    }

    @Test
    @Transactional
    void getAllTasksByStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);

        // Get all the taskList where status equals to DEFAULT_STATUS
        defaultTaskShouldBeFound("status.equals=" + DEFAULT_STATUS);

        // Get all the taskList where status equals to UPDATED_STATUS
        defaultTaskShouldNotBeFound("status.equals=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllTasksByStatusIsInShouldWork() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);

        // Get all the taskList where status in DEFAULT_STATUS or UPDATED_STATUS
        defaultTaskShouldBeFound("status.in=" + DEFAULT_STATUS + "," + UPDATED_STATUS);

        // Get all the taskList where status equals to UPDATED_STATUS
        defaultTaskShouldNotBeFound("status.in=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllTasksByStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);

        // Get all the taskList where status is not null
        defaultTaskShouldBeFound("status.specified=true");

        // Get all the taskList where status is null
        defaultTaskShouldNotBeFound("status.specified=false");
    }

    @Test
    @Transactional
    void getAllTasksByStatusContainsSomething() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);

        // Get all the taskList where status contains DEFAULT_STATUS
        defaultTaskShouldBeFound("status.contains=" + DEFAULT_STATUS);

        // Get all the taskList where status contains UPDATED_STATUS
        defaultTaskShouldNotBeFound("status.contains=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllTasksByStatusNotContainsSomething() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);

        // Get all the taskList where status does not contain DEFAULT_STATUS
        defaultTaskShouldNotBeFound("status.doesNotContain=" + DEFAULT_STATUS);

        // Get all the taskList where status does not contain UPDATED_STATUS
        defaultTaskShouldBeFound("status.doesNotContain=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllTasksByHandledByIsEqualToSomething() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);

        // Get all the taskList where handledBy equals to DEFAULT_HANDLED_BY
        defaultTaskShouldBeFound("handledBy.equals=" + DEFAULT_HANDLED_BY);

        // Get all the taskList where handledBy equals to UPDATED_HANDLED_BY
        defaultTaskShouldNotBeFound("handledBy.equals=" + UPDATED_HANDLED_BY);
    }

    @Test
    @Transactional
    void getAllTasksByHandledByIsInShouldWork() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);

        // Get all the taskList where handledBy in DEFAULT_HANDLED_BY or UPDATED_HANDLED_BY
        defaultTaskShouldBeFound("handledBy.in=" + DEFAULT_HANDLED_BY + "," + UPDATED_HANDLED_BY);

        // Get all the taskList where handledBy equals to UPDATED_HANDLED_BY
        defaultTaskShouldNotBeFound("handledBy.in=" + UPDATED_HANDLED_BY);
    }

    @Test
    @Transactional
    void getAllTasksByHandledByIsNullOrNotNull() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);

        // Get all the taskList where handledBy is not null
        defaultTaskShouldBeFound("handledBy.specified=true");

        // Get all the taskList where handledBy is null
        defaultTaskShouldNotBeFound("handledBy.specified=false");
    }

    @Test
    @Transactional
    void getAllTasksByHandledByIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);

        // Get all the taskList where handledBy is greater than or equal to DEFAULT_HANDLED_BY
        defaultTaskShouldBeFound("handledBy.greaterThanOrEqual=" + DEFAULT_HANDLED_BY);

        // Get all the taskList where handledBy is greater than or equal to UPDATED_HANDLED_BY
        defaultTaskShouldNotBeFound("handledBy.greaterThanOrEqual=" + UPDATED_HANDLED_BY);
    }

    @Test
    @Transactional
    void getAllTasksByHandledByIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);

        // Get all the taskList where handledBy is less than or equal to DEFAULT_HANDLED_BY
        defaultTaskShouldBeFound("handledBy.lessThanOrEqual=" + DEFAULT_HANDLED_BY);

        // Get all the taskList where handledBy is less than or equal to SMALLER_HANDLED_BY
        defaultTaskShouldNotBeFound("handledBy.lessThanOrEqual=" + SMALLER_HANDLED_BY);
    }

    @Test
    @Transactional
    void getAllTasksByHandledByIsLessThanSomething() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);

        // Get all the taskList where handledBy is less than DEFAULT_HANDLED_BY
        defaultTaskShouldNotBeFound("handledBy.lessThan=" + DEFAULT_HANDLED_BY);

        // Get all the taskList where handledBy is less than UPDATED_HANDLED_BY
        defaultTaskShouldBeFound("handledBy.lessThan=" + UPDATED_HANDLED_BY);
    }

    @Test
    @Transactional
    void getAllTasksByHandledByIsGreaterThanSomething() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);

        // Get all the taskList where handledBy is greater than DEFAULT_HANDLED_BY
        defaultTaskShouldNotBeFound("handledBy.greaterThan=" + DEFAULT_HANDLED_BY);

        // Get all the taskList where handledBy is greater than SMALLER_HANDLED_BY
        defaultTaskShouldBeFound("handledBy.greaterThan=" + SMALLER_HANDLED_BY);
    }

    @Test
    @Transactional
    void getAllTasksByGantryProcessedIsEqualToSomething() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);

        // Get all the taskList where gantryProcessed equals to DEFAULT_GANTRY_PROCESSED
        defaultTaskShouldBeFound("gantryProcessed.equals=" + DEFAULT_GANTRY_PROCESSED);

        // Get all the taskList where gantryProcessed equals to UPDATED_GANTRY_PROCESSED
        defaultTaskShouldNotBeFound("gantryProcessed.equals=" + UPDATED_GANTRY_PROCESSED);
    }

    @Test
    @Transactional
    void getAllTasksByGantryProcessedIsInShouldWork() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);

        // Get all the taskList where gantryProcessed in DEFAULT_GANTRY_PROCESSED or UPDATED_GANTRY_PROCESSED
        defaultTaskShouldBeFound("gantryProcessed.in=" + DEFAULT_GANTRY_PROCESSED + "," + UPDATED_GANTRY_PROCESSED);

        // Get all the taskList where gantryProcessed equals to UPDATED_GANTRY_PROCESSED
        defaultTaskShouldNotBeFound("gantryProcessed.in=" + UPDATED_GANTRY_PROCESSED);
    }

    @Test
    @Transactional
    void getAllTasksByGantryProcessedIsNullOrNotNull() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);

        // Get all the taskList where gantryProcessed is not null
        defaultTaskShouldBeFound("gantryProcessed.specified=true");

        // Get all the taskList where gantryProcessed is null
        defaultTaskShouldNotBeFound("gantryProcessed.specified=false");
    }

    @Test
    @Transactional
    void getAllTasksByGantryProcessedIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);

        // Get all the taskList where gantryProcessed is greater than or equal to DEFAULT_GANTRY_PROCESSED
        defaultTaskShouldBeFound("gantryProcessed.greaterThanOrEqual=" + DEFAULT_GANTRY_PROCESSED);

        // Get all the taskList where gantryProcessed is greater than or equal to UPDATED_GANTRY_PROCESSED
        defaultTaskShouldNotBeFound("gantryProcessed.greaterThanOrEqual=" + UPDATED_GANTRY_PROCESSED);
    }

    @Test
    @Transactional
    void getAllTasksByGantryProcessedIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);

        // Get all the taskList where gantryProcessed is less than or equal to DEFAULT_GANTRY_PROCESSED
        defaultTaskShouldBeFound("gantryProcessed.lessThanOrEqual=" + DEFAULT_GANTRY_PROCESSED);

        // Get all the taskList where gantryProcessed is less than or equal to SMALLER_GANTRY_PROCESSED
        defaultTaskShouldNotBeFound("gantryProcessed.lessThanOrEqual=" + SMALLER_GANTRY_PROCESSED);
    }

    @Test
    @Transactional
    void getAllTasksByGantryProcessedIsLessThanSomething() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);

        // Get all the taskList where gantryProcessed is less than DEFAULT_GANTRY_PROCESSED
        defaultTaskShouldNotBeFound("gantryProcessed.lessThan=" + DEFAULT_GANTRY_PROCESSED);

        // Get all the taskList where gantryProcessed is less than UPDATED_GANTRY_PROCESSED
        defaultTaskShouldBeFound("gantryProcessed.lessThan=" + UPDATED_GANTRY_PROCESSED);
    }

    @Test
    @Transactional
    void getAllTasksByGantryProcessedIsGreaterThanSomething() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);

        // Get all the taskList where gantryProcessed is greater than DEFAULT_GANTRY_PROCESSED
        defaultTaskShouldNotBeFound("gantryProcessed.greaterThan=" + DEFAULT_GANTRY_PROCESSED);

        // Get all the taskList where gantryProcessed is greater than SMALLER_GANTRY_PROCESSED
        defaultTaskShouldBeFound("gantryProcessed.greaterThan=" + SMALLER_GANTRY_PROCESSED);
    }

    @Test
    @Transactional
    void getAllTasksByGantrySentIsEqualToSomething() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);

        // Get all the taskList where gantrySent equals to DEFAULT_GANTRY_SENT
        defaultTaskShouldBeFound("gantrySent.equals=" + DEFAULT_GANTRY_SENT);

        // Get all the taskList where gantrySent equals to UPDATED_GANTRY_SENT
        defaultTaskShouldNotBeFound("gantrySent.equals=" + UPDATED_GANTRY_SENT);
    }

    @Test
    @Transactional
    void getAllTasksByGantrySentIsInShouldWork() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);

        // Get all the taskList where gantrySent in DEFAULT_GANTRY_SENT or UPDATED_GANTRY_SENT
        defaultTaskShouldBeFound("gantrySent.in=" + DEFAULT_GANTRY_SENT + "," + UPDATED_GANTRY_SENT);

        // Get all the taskList where gantrySent equals to UPDATED_GANTRY_SENT
        defaultTaskShouldNotBeFound("gantrySent.in=" + UPDATED_GANTRY_SENT);
    }

    @Test
    @Transactional
    void getAllTasksByGantrySentIsNullOrNotNull() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);

        // Get all the taskList where gantrySent is not null
        defaultTaskShouldBeFound("gantrySent.specified=true");

        // Get all the taskList where gantrySent is null
        defaultTaskShouldNotBeFound("gantrySent.specified=false");
    }

    @Test
    @Transactional
    void getAllTasksByGantrySentIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);

        // Get all the taskList where gantrySent is greater than or equal to DEFAULT_GANTRY_SENT
        defaultTaskShouldBeFound("gantrySent.greaterThanOrEqual=" + DEFAULT_GANTRY_SENT);

        // Get all the taskList where gantrySent is greater than or equal to UPDATED_GANTRY_SENT
        defaultTaskShouldNotBeFound("gantrySent.greaterThanOrEqual=" + UPDATED_GANTRY_SENT);
    }

    @Test
    @Transactional
    void getAllTasksByGantrySentIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);

        // Get all the taskList where gantrySent is less than or equal to DEFAULT_GANTRY_SENT
        defaultTaskShouldBeFound("gantrySent.lessThanOrEqual=" + DEFAULT_GANTRY_SENT);

        // Get all the taskList where gantrySent is less than or equal to SMALLER_GANTRY_SENT
        defaultTaskShouldNotBeFound("gantrySent.lessThanOrEqual=" + SMALLER_GANTRY_SENT);
    }

    @Test
    @Transactional
    void getAllTasksByGantrySentIsLessThanSomething() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);

        // Get all the taskList where gantrySent is less than DEFAULT_GANTRY_SENT
        defaultTaskShouldNotBeFound("gantrySent.lessThan=" + DEFAULT_GANTRY_SENT);

        // Get all the taskList where gantrySent is less than UPDATED_GANTRY_SENT
        defaultTaskShouldBeFound("gantrySent.lessThan=" + UPDATED_GANTRY_SENT);
    }

    @Test
    @Transactional
    void getAllTasksByGantrySentIsGreaterThanSomething() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);

        // Get all the taskList where gantrySent is greater than DEFAULT_GANTRY_SENT
        defaultTaskShouldNotBeFound("gantrySent.greaterThan=" + DEFAULT_GANTRY_SENT);

        // Get all the taskList where gantrySent is greater than SMALLER_GANTRY_SENT
        defaultTaskShouldBeFound("gantrySent.greaterThan=" + SMALLER_GANTRY_SENT);
    }

    @Test
    @Transactional
    void getAllTasksByWhenIsEqualToSomething() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);

        // Get all the taskList where when equals to DEFAULT_WHEN
        defaultTaskShouldBeFound("when.equals=" + DEFAULT_WHEN);

        // Get all the taskList where when equals to UPDATED_WHEN
        defaultTaskShouldNotBeFound("when.equals=" + UPDATED_WHEN);
    }

    @Test
    @Transactional
    void getAllTasksByWhenIsInShouldWork() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);

        // Get all the taskList where when in DEFAULT_WHEN or UPDATED_WHEN
        defaultTaskShouldBeFound("when.in=" + DEFAULT_WHEN + "," + UPDATED_WHEN);

        // Get all the taskList where when equals to UPDATED_WHEN
        defaultTaskShouldNotBeFound("when.in=" + UPDATED_WHEN);
    }

    @Test
    @Transactional
    void getAllTasksByWhenIsNullOrNotNull() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);

        // Get all the taskList where when is not null
        defaultTaskShouldBeFound("when.specified=true");

        // Get all the taskList where when is null
        defaultTaskShouldNotBeFound("when.specified=false");
    }

    @Test
    @Transactional
    void getAllTasksByWhenIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);

        // Get all the taskList where when is greater than or equal to DEFAULT_WHEN
        defaultTaskShouldBeFound("when.greaterThanOrEqual=" + DEFAULT_WHEN);

        // Get all the taskList where when is greater than or equal to UPDATED_WHEN
        defaultTaskShouldNotBeFound("when.greaterThanOrEqual=" + UPDATED_WHEN);
    }

    @Test
    @Transactional
    void getAllTasksByWhenIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);

        // Get all the taskList where when is less than or equal to DEFAULT_WHEN
        defaultTaskShouldBeFound("when.lessThanOrEqual=" + DEFAULT_WHEN);

        // Get all the taskList where when is less than or equal to SMALLER_WHEN
        defaultTaskShouldNotBeFound("when.lessThanOrEqual=" + SMALLER_WHEN);
    }

    @Test
    @Transactional
    void getAllTasksByWhenIsLessThanSomething() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);

        // Get all the taskList where when is less than DEFAULT_WHEN
        defaultTaskShouldNotBeFound("when.lessThan=" + DEFAULT_WHEN);

        // Get all the taskList where when is less than UPDATED_WHEN
        defaultTaskShouldBeFound("when.lessThan=" + UPDATED_WHEN);
    }

    @Test
    @Transactional
    void getAllTasksByWhenIsGreaterThanSomething() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);

        // Get all the taskList where when is greater than DEFAULT_WHEN
        defaultTaskShouldNotBeFound("when.greaterThan=" + DEFAULT_WHEN);

        // Get all the taskList where when is greater than SMALLER_WHEN
        defaultTaskShouldBeFound("when.greaterThan=" + SMALLER_WHEN);
    }

    @Test
    @Transactional
    void getAllTasksByTollIsEqualToSomething() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);

        // Get all the taskList where toll equals to DEFAULT_TOLL
        defaultTaskShouldBeFound("toll.equals=" + DEFAULT_TOLL);

        // Get all the taskList where toll equals to UPDATED_TOLL
        defaultTaskShouldNotBeFound("toll.equals=" + UPDATED_TOLL);
    }

    @Test
    @Transactional
    void getAllTasksByTollIsInShouldWork() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);

        // Get all the taskList where toll in DEFAULT_TOLL or UPDATED_TOLL
        defaultTaskShouldBeFound("toll.in=" + DEFAULT_TOLL + "," + UPDATED_TOLL);

        // Get all the taskList where toll equals to UPDATED_TOLL
        defaultTaskShouldNotBeFound("toll.in=" + UPDATED_TOLL);
    }

    @Test
    @Transactional
    void getAllTasksByTollIsNullOrNotNull() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);

        // Get all the taskList where toll is not null
        defaultTaskShouldBeFound("toll.specified=true");

        // Get all the taskList where toll is null
        defaultTaskShouldNotBeFound("toll.specified=false");
    }

    @Test
    @Transactional
    void getAllTasksByTollIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);

        // Get all the taskList where toll is greater than or equal to DEFAULT_TOLL
        defaultTaskShouldBeFound("toll.greaterThanOrEqual=" + DEFAULT_TOLL);

        // Get all the taskList where toll is greater than or equal to UPDATED_TOLL
        defaultTaskShouldNotBeFound("toll.greaterThanOrEqual=" + UPDATED_TOLL);
    }

    @Test
    @Transactional
    void getAllTasksByTollIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);

        // Get all the taskList where toll is less than or equal to DEFAULT_TOLL
        defaultTaskShouldBeFound("toll.lessThanOrEqual=" + DEFAULT_TOLL);

        // Get all the taskList where toll is less than or equal to SMALLER_TOLL
        defaultTaskShouldNotBeFound("toll.lessThanOrEqual=" + SMALLER_TOLL);
    }

    @Test
    @Transactional
    void getAllTasksByTollIsLessThanSomething() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);

        // Get all the taskList where toll is less than DEFAULT_TOLL
        defaultTaskShouldNotBeFound("toll.lessThan=" + DEFAULT_TOLL);

        // Get all the taskList where toll is less than UPDATED_TOLL
        defaultTaskShouldBeFound("toll.lessThan=" + UPDATED_TOLL);
    }

    @Test
    @Transactional
    void getAllTasksByTollIsGreaterThanSomething() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);

        // Get all the taskList where toll is greater than DEFAULT_TOLL
        defaultTaskShouldNotBeFound("toll.greaterThan=" + DEFAULT_TOLL);

        // Get all the taskList where toll is greater than SMALLER_TOLL
        defaultTaskShouldBeFound("toll.greaterThan=" + SMALLER_TOLL);
    }

    @Test
    @Transactional
    void getAllTasksByRuleRcvdIsEqualToSomething() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);

        // Get all the taskList where ruleRcvd equals to DEFAULT_RULE_RCVD
        defaultTaskShouldBeFound("ruleRcvd.equals=" + DEFAULT_RULE_RCVD);

        // Get all the taskList where ruleRcvd equals to UPDATED_RULE_RCVD
        defaultTaskShouldNotBeFound("ruleRcvd.equals=" + UPDATED_RULE_RCVD);
    }

    @Test
    @Transactional
    void getAllTasksByRuleRcvdIsInShouldWork() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);

        // Get all the taskList where ruleRcvd in DEFAULT_RULE_RCVD or UPDATED_RULE_RCVD
        defaultTaskShouldBeFound("ruleRcvd.in=" + DEFAULT_RULE_RCVD + "," + UPDATED_RULE_RCVD);

        // Get all the taskList where ruleRcvd equals to UPDATED_RULE_RCVD
        defaultTaskShouldNotBeFound("ruleRcvd.in=" + UPDATED_RULE_RCVD);
    }

    @Test
    @Transactional
    void getAllTasksByRuleRcvdIsNullOrNotNull() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);

        // Get all the taskList where ruleRcvd is not null
        defaultTaskShouldBeFound("ruleRcvd.specified=true");

        // Get all the taskList where ruleRcvd is null
        defaultTaskShouldNotBeFound("ruleRcvd.specified=false");
    }

    @Test
    @Transactional
    void getAllTasksByRuleRcvdIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);

        // Get all the taskList where ruleRcvd is greater than or equal to DEFAULT_RULE_RCVD
        defaultTaskShouldBeFound("ruleRcvd.greaterThanOrEqual=" + DEFAULT_RULE_RCVD);

        // Get all the taskList where ruleRcvd is greater than or equal to UPDATED_RULE_RCVD
        defaultTaskShouldNotBeFound("ruleRcvd.greaterThanOrEqual=" + UPDATED_RULE_RCVD);
    }

    @Test
    @Transactional
    void getAllTasksByRuleRcvdIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);

        // Get all the taskList where ruleRcvd is less than or equal to DEFAULT_RULE_RCVD
        defaultTaskShouldBeFound("ruleRcvd.lessThanOrEqual=" + DEFAULT_RULE_RCVD);

        // Get all the taskList where ruleRcvd is less than or equal to SMALLER_RULE_RCVD
        defaultTaskShouldNotBeFound("ruleRcvd.lessThanOrEqual=" + SMALLER_RULE_RCVD);
    }

    @Test
    @Transactional
    void getAllTasksByRuleRcvdIsLessThanSomething() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);

        // Get all the taskList where ruleRcvd is less than DEFAULT_RULE_RCVD
        defaultTaskShouldNotBeFound("ruleRcvd.lessThan=" + DEFAULT_RULE_RCVD);

        // Get all the taskList where ruleRcvd is less than UPDATED_RULE_RCVD
        defaultTaskShouldBeFound("ruleRcvd.lessThan=" + UPDATED_RULE_RCVD);
    }

    @Test
    @Transactional
    void getAllTasksByRuleRcvdIsGreaterThanSomething() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);

        // Get all the taskList where ruleRcvd is greater than DEFAULT_RULE_RCVD
        defaultTaskShouldNotBeFound("ruleRcvd.greaterThan=" + DEFAULT_RULE_RCVD);

        // Get all the taskList where ruleRcvd is greater than SMALLER_RULE_RCVD
        defaultTaskShouldBeFound("ruleRcvd.greaterThan=" + SMALLER_RULE_RCVD);
    }

    @Test
    @Transactional
    void getAllTasksByWantedForIsEqualToSomething() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);

        // Get all the taskList where wantedFor equals to DEFAULT_WANTED_FOR
        defaultTaskShouldBeFound("wantedFor.equals=" + DEFAULT_WANTED_FOR);

        // Get all the taskList where wantedFor equals to UPDATED_WANTED_FOR
        defaultTaskShouldNotBeFound("wantedFor.equals=" + UPDATED_WANTED_FOR);
    }

    @Test
    @Transactional
    void getAllTasksByWantedForIsInShouldWork() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);

        // Get all the taskList where wantedFor in DEFAULT_WANTED_FOR or UPDATED_WANTED_FOR
        defaultTaskShouldBeFound("wantedFor.in=" + DEFAULT_WANTED_FOR + "," + UPDATED_WANTED_FOR);

        // Get all the taskList where wantedFor equals to UPDATED_WANTED_FOR
        defaultTaskShouldNotBeFound("wantedFor.in=" + UPDATED_WANTED_FOR);
    }

    @Test
    @Transactional
    void getAllTasksByWantedForIsNullOrNotNull() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);

        // Get all the taskList where wantedFor is not null
        defaultTaskShouldBeFound("wantedFor.specified=true");

        // Get all the taskList where wantedFor is null
        defaultTaskShouldNotBeFound("wantedFor.specified=false");
    }

    @Test
    @Transactional
    void getAllTasksByWantedForContainsSomething() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);

        // Get all the taskList where wantedFor contains DEFAULT_WANTED_FOR
        defaultTaskShouldBeFound("wantedFor.contains=" + DEFAULT_WANTED_FOR);

        // Get all the taskList where wantedFor contains UPDATED_WANTED_FOR
        defaultTaskShouldNotBeFound("wantedFor.contains=" + UPDATED_WANTED_FOR);
    }

    @Test
    @Transactional
    void getAllTasksByWantedForNotContainsSomething() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);

        // Get all the taskList where wantedFor does not contain DEFAULT_WANTED_FOR
        defaultTaskShouldNotBeFound("wantedFor.doesNotContain=" + DEFAULT_WANTED_FOR);

        // Get all the taskList where wantedFor does not contain UPDATED_WANTED_FOR
        defaultTaskShouldBeFound("wantedFor.doesNotContain=" + UPDATED_WANTED_FOR);
    }

    @Test
    @Transactional
    void getAllTasksByFineIsEqualToSomething() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);

        // Get all the taskList where fine equals to DEFAULT_FINE
        defaultTaskShouldBeFound("fine.equals=" + DEFAULT_FINE);

        // Get all the taskList where fine equals to UPDATED_FINE
        defaultTaskShouldNotBeFound("fine.equals=" + UPDATED_FINE);
    }

    @Test
    @Transactional
    void getAllTasksByFineIsInShouldWork() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);

        // Get all the taskList where fine in DEFAULT_FINE or UPDATED_FINE
        defaultTaskShouldBeFound("fine.in=" + DEFAULT_FINE + "," + UPDATED_FINE);

        // Get all the taskList where fine equals to UPDATED_FINE
        defaultTaskShouldNotBeFound("fine.in=" + UPDATED_FINE);
    }

    @Test
    @Transactional
    void getAllTasksByFineIsNullOrNotNull() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);

        // Get all the taskList where fine is not null
        defaultTaskShouldBeFound("fine.specified=true");

        // Get all the taskList where fine is null
        defaultTaskShouldNotBeFound("fine.specified=false");
    }

    @Test
    @Transactional
    void getAllTasksByFineIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);

        // Get all the taskList where fine is greater than or equal to DEFAULT_FINE
        defaultTaskShouldBeFound("fine.greaterThanOrEqual=" + DEFAULT_FINE);

        // Get all the taskList where fine is greater than or equal to UPDATED_FINE
        defaultTaskShouldNotBeFound("fine.greaterThanOrEqual=" + UPDATED_FINE);
    }

    @Test
    @Transactional
    void getAllTasksByFineIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);

        // Get all the taskList where fine is less than or equal to DEFAULT_FINE
        defaultTaskShouldBeFound("fine.lessThanOrEqual=" + DEFAULT_FINE);

        // Get all the taskList where fine is less than or equal to SMALLER_FINE
        defaultTaskShouldNotBeFound("fine.lessThanOrEqual=" + SMALLER_FINE);
    }

    @Test
    @Transactional
    void getAllTasksByFineIsLessThanSomething() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);

        // Get all the taskList where fine is less than DEFAULT_FINE
        defaultTaskShouldNotBeFound("fine.lessThan=" + DEFAULT_FINE);

        // Get all the taskList where fine is less than UPDATED_FINE
        defaultTaskShouldBeFound("fine.lessThan=" + UPDATED_FINE);
    }

    @Test
    @Transactional
    void getAllTasksByFineIsGreaterThanSomething() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);

        // Get all the taskList where fine is greater than DEFAULT_FINE
        defaultTaskShouldNotBeFound("fine.greaterThan=" + DEFAULT_FINE);

        // Get all the taskList where fine is greater than SMALLER_FINE
        defaultTaskShouldBeFound("fine.greaterThan=" + SMALLER_FINE);
    }

    @Test
    @Transactional
    void getAllTasksByLicenseIssueIsEqualToSomething() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);

        // Get all the taskList where licenseIssue equals to DEFAULT_LICENSE_ISSUE
        defaultTaskShouldBeFound("licenseIssue.equals=" + DEFAULT_LICENSE_ISSUE);

        // Get all the taskList where licenseIssue equals to UPDATED_LICENSE_ISSUE
        defaultTaskShouldNotBeFound("licenseIssue.equals=" + UPDATED_LICENSE_ISSUE);
    }

    @Test
    @Transactional
    void getAllTasksByLicenseIssueIsInShouldWork() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);

        // Get all the taskList where licenseIssue in DEFAULT_LICENSE_ISSUE or UPDATED_LICENSE_ISSUE
        defaultTaskShouldBeFound("licenseIssue.in=" + DEFAULT_LICENSE_ISSUE + "," + UPDATED_LICENSE_ISSUE);

        // Get all the taskList where licenseIssue equals to UPDATED_LICENSE_ISSUE
        defaultTaskShouldNotBeFound("licenseIssue.in=" + UPDATED_LICENSE_ISSUE);
    }

    @Test
    @Transactional
    void getAllTasksByLicenseIssueIsNullOrNotNull() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);

        // Get all the taskList where licenseIssue is not null
        defaultTaskShouldBeFound("licenseIssue.specified=true");

        // Get all the taskList where licenseIssue is null
        defaultTaskShouldNotBeFound("licenseIssue.specified=false");
    }

    @Test
    @Transactional
    void getAllTasksByLicenseIssueContainsSomething() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);

        // Get all the taskList where licenseIssue contains DEFAULT_LICENSE_ISSUE
        defaultTaskShouldBeFound("licenseIssue.contains=" + DEFAULT_LICENSE_ISSUE);

        // Get all the taskList where licenseIssue contains UPDATED_LICENSE_ISSUE
        defaultTaskShouldNotBeFound("licenseIssue.contains=" + UPDATED_LICENSE_ISSUE);
    }

    @Test
    @Transactional
    void getAllTasksByLicenseIssueNotContainsSomething() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);

        // Get all the taskList where licenseIssue does not contain DEFAULT_LICENSE_ISSUE
        defaultTaskShouldNotBeFound("licenseIssue.doesNotContain=" + DEFAULT_LICENSE_ISSUE);

        // Get all the taskList where licenseIssue does not contain UPDATED_LICENSE_ISSUE
        defaultTaskShouldBeFound("licenseIssue.doesNotContain=" + UPDATED_LICENSE_ISSUE);
    }

    @Test
    @Transactional
    void getAllTasksByWantedByIsEqualToSomething() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);

        // Get all the taskList where wantedBy equals to DEFAULT_WANTED_BY
        defaultTaskShouldBeFound("wantedBy.equals=" + DEFAULT_WANTED_BY);

        // Get all the taskList where wantedBy equals to UPDATED_WANTED_BY
        defaultTaskShouldNotBeFound("wantedBy.equals=" + UPDATED_WANTED_BY);
    }

    @Test
    @Transactional
    void getAllTasksByWantedByIsInShouldWork() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);

        // Get all the taskList where wantedBy in DEFAULT_WANTED_BY or UPDATED_WANTED_BY
        defaultTaskShouldBeFound("wantedBy.in=" + DEFAULT_WANTED_BY + "," + UPDATED_WANTED_BY);

        // Get all the taskList where wantedBy equals to UPDATED_WANTED_BY
        defaultTaskShouldNotBeFound("wantedBy.in=" + UPDATED_WANTED_BY);
    }

    @Test
    @Transactional
    void getAllTasksByWantedByIsNullOrNotNull() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);

        // Get all the taskList where wantedBy is not null
        defaultTaskShouldBeFound("wantedBy.specified=true");

        // Get all the taskList where wantedBy is null
        defaultTaskShouldNotBeFound("wantedBy.specified=false");
    }

    @Test
    @Transactional
    void getAllTasksByWantedByIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);

        // Get all the taskList where wantedBy is greater than or equal to DEFAULT_WANTED_BY
        defaultTaskShouldBeFound("wantedBy.greaterThanOrEqual=" + DEFAULT_WANTED_BY);

        // Get all the taskList where wantedBy is greater than or equal to UPDATED_WANTED_BY
        defaultTaskShouldNotBeFound("wantedBy.greaterThanOrEqual=" + UPDATED_WANTED_BY);
    }

    @Test
    @Transactional
    void getAllTasksByWantedByIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);

        // Get all the taskList where wantedBy is less than or equal to DEFAULT_WANTED_BY
        defaultTaskShouldBeFound("wantedBy.lessThanOrEqual=" + DEFAULT_WANTED_BY);

        // Get all the taskList where wantedBy is less than or equal to SMALLER_WANTED_BY
        defaultTaskShouldNotBeFound("wantedBy.lessThanOrEqual=" + SMALLER_WANTED_BY);
    }

    @Test
    @Transactional
    void getAllTasksByWantedByIsLessThanSomething() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);

        // Get all the taskList where wantedBy is less than DEFAULT_WANTED_BY
        defaultTaskShouldNotBeFound("wantedBy.lessThan=" + DEFAULT_WANTED_BY);

        // Get all the taskList where wantedBy is less than UPDATED_WANTED_BY
        defaultTaskShouldBeFound("wantedBy.lessThan=" + UPDATED_WANTED_BY);
    }

    @Test
    @Transactional
    void getAllTasksByWantedByIsGreaterThanSomething() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);

        // Get all the taskList where wantedBy is greater than DEFAULT_WANTED_BY
        defaultTaskShouldNotBeFound("wantedBy.greaterThan=" + DEFAULT_WANTED_BY);

        // Get all the taskList where wantedBy is greater than SMALLER_WANTED_BY
        defaultTaskShouldBeFound("wantedBy.greaterThan=" + SMALLER_WANTED_BY);
    }

    @Test
    @Transactional
    void getAllTasksByRuleProcessedIsEqualToSomething() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);

        // Get all the taskList where ruleProcessed equals to DEFAULT_RULE_PROCESSED
        defaultTaskShouldBeFound("ruleProcessed.equals=" + DEFAULT_RULE_PROCESSED);

        // Get all the taskList where ruleProcessed equals to UPDATED_RULE_PROCESSED
        defaultTaskShouldNotBeFound("ruleProcessed.equals=" + UPDATED_RULE_PROCESSED);
    }

    @Test
    @Transactional
    void getAllTasksByRuleProcessedIsInShouldWork() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);

        // Get all the taskList where ruleProcessed in DEFAULT_RULE_PROCESSED or UPDATED_RULE_PROCESSED
        defaultTaskShouldBeFound("ruleProcessed.in=" + DEFAULT_RULE_PROCESSED + "," + UPDATED_RULE_PROCESSED);

        // Get all the taskList where ruleProcessed equals to UPDATED_RULE_PROCESSED
        defaultTaskShouldNotBeFound("ruleProcessed.in=" + UPDATED_RULE_PROCESSED);
    }

    @Test
    @Transactional
    void getAllTasksByRuleProcessedIsNullOrNotNull() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);

        // Get all the taskList where ruleProcessed is not null
        defaultTaskShouldBeFound("ruleProcessed.specified=true");

        // Get all the taskList where ruleProcessed is null
        defaultTaskShouldNotBeFound("ruleProcessed.specified=false");
    }

    @Test
    @Transactional
    void getAllTasksByRuleProcessedIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);

        // Get all the taskList where ruleProcessed is greater than or equal to DEFAULT_RULE_PROCESSED
        defaultTaskShouldBeFound("ruleProcessed.greaterThanOrEqual=" + DEFAULT_RULE_PROCESSED);

        // Get all the taskList where ruleProcessed is greater than or equal to UPDATED_RULE_PROCESSED
        defaultTaskShouldNotBeFound("ruleProcessed.greaterThanOrEqual=" + UPDATED_RULE_PROCESSED);
    }

    @Test
    @Transactional
    void getAllTasksByRuleProcessedIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);

        // Get all the taskList where ruleProcessed is less than or equal to DEFAULT_RULE_PROCESSED
        defaultTaskShouldBeFound("ruleProcessed.lessThanOrEqual=" + DEFAULT_RULE_PROCESSED);

        // Get all the taskList where ruleProcessed is less than or equal to SMALLER_RULE_PROCESSED
        defaultTaskShouldNotBeFound("ruleProcessed.lessThanOrEqual=" + SMALLER_RULE_PROCESSED);
    }

    @Test
    @Transactional
    void getAllTasksByRuleProcessedIsLessThanSomething() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);

        // Get all the taskList where ruleProcessed is less than DEFAULT_RULE_PROCESSED
        defaultTaskShouldNotBeFound("ruleProcessed.lessThan=" + DEFAULT_RULE_PROCESSED);

        // Get all the taskList where ruleProcessed is less than UPDATED_RULE_PROCESSED
        defaultTaskShouldBeFound("ruleProcessed.lessThan=" + UPDATED_RULE_PROCESSED);
    }

    @Test
    @Transactional
    void getAllTasksByRuleProcessedIsGreaterThanSomething() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);

        // Get all the taskList where ruleProcessed is greater than DEFAULT_RULE_PROCESSED
        defaultTaskShouldNotBeFound("ruleProcessed.greaterThan=" + DEFAULT_RULE_PROCESSED);

        // Get all the taskList where ruleProcessed is greater than SMALLER_RULE_PROCESSED
        defaultTaskShouldBeFound("ruleProcessed.greaterThan=" + SMALLER_RULE_PROCESSED);
    }

    @Test
    @Transactional
    void getAllTasksBySpeedFineIsEqualToSomething() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);

        // Get all the taskList where speedFine equals to DEFAULT_SPEED_FINE
        defaultTaskShouldBeFound("speedFine.equals=" + DEFAULT_SPEED_FINE);

        // Get all the taskList where speedFine equals to UPDATED_SPEED_FINE
        defaultTaskShouldNotBeFound("speedFine.equals=" + UPDATED_SPEED_FINE);
    }

    @Test
    @Transactional
    void getAllTasksBySpeedFineIsInShouldWork() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);

        // Get all the taskList where speedFine in DEFAULT_SPEED_FINE or UPDATED_SPEED_FINE
        defaultTaskShouldBeFound("speedFine.in=" + DEFAULT_SPEED_FINE + "," + UPDATED_SPEED_FINE);

        // Get all the taskList where speedFine equals to UPDATED_SPEED_FINE
        defaultTaskShouldNotBeFound("speedFine.in=" + UPDATED_SPEED_FINE);
    }

    @Test
    @Transactional
    void getAllTasksBySpeedFineIsNullOrNotNull() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);

        // Get all the taskList where speedFine is not null
        defaultTaskShouldBeFound("speedFine.specified=true");

        // Get all the taskList where speedFine is null
        defaultTaskShouldNotBeFound("speedFine.specified=false");
    }

    @Test
    @Transactional
    void getAllTasksBySpeedFineIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);

        // Get all the taskList where speedFine is greater than or equal to DEFAULT_SPEED_FINE
        defaultTaskShouldBeFound("speedFine.greaterThanOrEqual=" + DEFAULT_SPEED_FINE);

        // Get all the taskList where speedFine is greater than or equal to UPDATED_SPEED_FINE
        defaultTaskShouldNotBeFound("speedFine.greaterThanOrEqual=" + UPDATED_SPEED_FINE);
    }

    @Test
    @Transactional
    void getAllTasksBySpeedFineIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);

        // Get all the taskList where speedFine is less than or equal to DEFAULT_SPEED_FINE
        defaultTaskShouldBeFound("speedFine.lessThanOrEqual=" + DEFAULT_SPEED_FINE);

        // Get all the taskList where speedFine is less than or equal to SMALLER_SPEED_FINE
        defaultTaskShouldNotBeFound("speedFine.lessThanOrEqual=" + SMALLER_SPEED_FINE);
    }

    @Test
    @Transactional
    void getAllTasksBySpeedFineIsLessThanSomething() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);

        // Get all the taskList where speedFine is less than DEFAULT_SPEED_FINE
        defaultTaskShouldNotBeFound("speedFine.lessThan=" + DEFAULT_SPEED_FINE);

        // Get all the taskList where speedFine is less than UPDATED_SPEED_FINE
        defaultTaskShouldBeFound("speedFine.lessThan=" + UPDATED_SPEED_FINE);
    }

    @Test
    @Transactional
    void getAllTasksBySpeedFineIsGreaterThanSomething() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);

        // Get all the taskList where speedFine is greater than DEFAULT_SPEED_FINE
        defaultTaskShouldNotBeFound("speedFine.greaterThan=" + DEFAULT_SPEED_FINE);

        // Get all the taskList where speedFine is greater than SMALLER_SPEED_FINE
        defaultTaskShouldBeFound("speedFine.greaterThan=" + SMALLER_SPEED_FINE);
    }

    @Test
    @Transactional
    void getAllTasksByLaneIsEqualToSomething() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);

        // Get all the taskList where lane equals to DEFAULT_LANE
        defaultTaskShouldBeFound("lane.equals=" + DEFAULT_LANE);

        // Get all the taskList where lane equals to UPDATED_LANE
        defaultTaskShouldNotBeFound("lane.equals=" + UPDATED_LANE);
    }

    @Test
    @Transactional
    void getAllTasksByLaneIsInShouldWork() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);

        // Get all the taskList where lane in DEFAULT_LANE or UPDATED_LANE
        defaultTaskShouldBeFound("lane.in=" + DEFAULT_LANE + "," + UPDATED_LANE);

        // Get all the taskList where lane equals to UPDATED_LANE
        defaultTaskShouldNotBeFound("lane.in=" + UPDATED_LANE);
    }

    @Test
    @Transactional
    void getAllTasksByLaneIsNullOrNotNull() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);

        // Get all the taskList where lane is not null
        defaultTaskShouldBeFound("lane.specified=true");

        // Get all the taskList where lane is null
        defaultTaskShouldNotBeFound("lane.specified=false");
    }

    @Test
    @Transactional
    void getAllTasksByLaneIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);

        // Get all the taskList where lane is greater than or equal to DEFAULT_LANE
        defaultTaskShouldBeFound("lane.greaterThanOrEqual=" + DEFAULT_LANE);

        // Get all the taskList where lane is greater than or equal to UPDATED_LANE
        defaultTaskShouldNotBeFound("lane.greaterThanOrEqual=" + UPDATED_LANE);
    }

    @Test
    @Transactional
    void getAllTasksByLaneIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);

        // Get all the taskList where lane is less than or equal to DEFAULT_LANE
        defaultTaskShouldBeFound("lane.lessThanOrEqual=" + DEFAULT_LANE);

        // Get all the taskList where lane is less than or equal to SMALLER_LANE
        defaultTaskShouldNotBeFound("lane.lessThanOrEqual=" + SMALLER_LANE);
    }

    @Test
    @Transactional
    void getAllTasksByLaneIsLessThanSomething() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);

        // Get all the taskList where lane is less than DEFAULT_LANE
        defaultTaskShouldNotBeFound("lane.lessThan=" + DEFAULT_LANE);

        // Get all the taskList where lane is less than UPDATED_LANE
        defaultTaskShouldBeFound("lane.lessThan=" + UPDATED_LANE);
    }

    @Test
    @Transactional
    void getAllTasksByLaneIsGreaterThanSomething() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);

        // Get all the taskList where lane is greater than DEFAULT_LANE
        defaultTaskShouldNotBeFound("lane.greaterThan=" + DEFAULT_LANE);

        // Get all the taskList where lane is greater than SMALLER_LANE
        defaultTaskShouldBeFound("lane.greaterThan=" + SMALLER_LANE);
    }

    @Test
    @Transactional
    void getAllTasksByTagIssueIsEqualToSomething() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);

        // Get all the taskList where tagIssue equals to DEFAULT_TAG_ISSUE
        defaultTaskShouldBeFound("tagIssue.equals=" + DEFAULT_TAG_ISSUE);

        // Get all the taskList where tagIssue equals to UPDATED_TAG_ISSUE
        defaultTaskShouldNotBeFound("tagIssue.equals=" + UPDATED_TAG_ISSUE);
    }

    @Test
    @Transactional
    void getAllTasksByTagIssueIsInShouldWork() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);

        // Get all the taskList where tagIssue in DEFAULT_TAG_ISSUE or UPDATED_TAG_ISSUE
        defaultTaskShouldBeFound("tagIssue.in=" + DEFAULT_TAG_ISSUE + "," + UPDATED_TAG_ISSUE);

        // Get all the taskList where tagIssue equals to UPDATED_TAG_ISSUE
        defaultTaskShouldNotBeFound("tagIssue.in=" + UPDATED_TAG_ISSUE);
    }

    @Test
    @Transactional
    void getAllTasksByTagIssueIsNullOrNotNull() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);

        // Get all the taskList where tagIssue is not null
        defaultTaskShouldBeFound("tagIssue.specified=true");

        // Get all the taskList where tagIssue is null
        defaultTaskShouldNotBeFound("tagIssue.specified=false");
    }

    @Test
    @Transactional
    void getAllTasksByTagIssueContainsSomething() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);

        // Get all the taskList where tagIssue contains DEFAULT_TAG_ISSUE
        defaultTaskShouldBeFound("tagIssue.contains=" + DEFAULT_TAG_ISSUE);

        // Get all the taskList where tagIssue contains UPDATED_TAG_ISSUE
        defaultTaskShouldNotBeFound("tagIssue.contains=" + UPDATED_TAG_ISSUE);
    }

    @Test
    @Transactional
    void getAllTasksByTagIssueNotContainsSomething() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);

        // Get all the taskList where tagIssue does not contain DEFAULT_TAG_ISSUE
        defaultTaskShouldNotBeFound("tagIssue.doesNotContain=" + DEFAULT_TAG_ISSUE);

        // Get all the taskList where tagIssue does not contain UPDATED_TAG_ISSUE
        defaultTaskShouldBeFound("tagIssue.doesNotContain=" + UPDATED_TAG_ISSUE);
    }

    @Test
    @Transactional
    void getAllTasksByStatusNameIsEqualToSomething() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);

        // Get all the taskList where statusName equals to DEFAULT_STATUS_NAME
        defaultTaskShouldBeFound("statusName.equals=" + DEFAULT_STATUS_NAME);

        // Get all the taskList where statusName equals to UPDATED_STATUS_NAME
        defaultTaskShouldNotBeFound("statusName.equals=" + UPDATED_STATUS_NAME);
    }

    @Test
    @Transactional
    void getAllTasksByStatusNameIsInShouldWork() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);

        // Get all the taskList where statusName in DEFAULT_STATUS_NAME or UPDATED_STATUS_NAME
        defaultTaskShouldBeFound("statusName.in=" + DEFAULT_STATUS_NAME + "," + UPDATED_STATUS_NAME);

        // Get all the taskList where statusName equals to UPDATED_STATUS_NAME
        defaultTaskShouldNotBeFound("statusName.in=" + UPDATED_STATUS_NAME);
    }

    @Test
    @Transactional
    void getAllTasksByStatusNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);

        // Get all the taskList where statusName is not null
        defaultTaskShouldBeFound("statusName.specified=true");

        // Get all the taskList where statusName is null
        defaultTaskShouldNotBeFound("statusName.specified=false");
    }

    @Test
    @Transactional
    void getAllTasksByStatusNameContainsSomething() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);

        // Get all the taskList where statusName contains DEFAULT_STATUS_NAME
        defaultTaskShouldBeFound("statusName.contains=" + DEFAULT_STATUS_NAME);

        // Get all the taskList where statusName contains UPDATED_STATUS_NAME
        defaultTaskShouldNotBeFound("statusName.contains=" + UPDATED_STATUS_NAME);
    }

    @Test
    @Transactional
    void getAllTasksByStatusNameNotContainsSomething() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);

        // Get all the taskList where statusName does not contain DEFAULT_STATUS_NAME
        defaultTaskShouldNotBeFound("statusName.doesNotContain=" + DEFAULT_STATUS_NAME);

        // Get all the taskList where statusName does not contain UPDATED_STATUS_NAME
        defaultTaskShouldBeFound("statusName.doesNotContain=" + UPDATED_STATUS_NAME);
    }

    @Test
    @Transactional
    void getAllTasksByLicenseFineIsEqualToSomething() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);

        // Get all the taskList where licenseFine equals to DEFAULT_LICENSE_FINE
        defaultTaskShouldBeFound("licenseFine.equals=" + DEFAULT_LICENSE_FINE);

        // Get all the taskList where licenseFine equals to UPDATED_LICENSE_FINE
        defaultTaskShouldNotBeFound("licenseFine.equals=" + UPDATED_LICENSE_FINE);
    }

    @Test
    @Transactional
    void getAllTasksByLicenseFineIsInShouldWork() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);

        // Get all the taskList where licenseFine in DEFAULT_LICENSE_FINE or UPDATED_LICENSE_FINE
        defaultTaskShouldBeFound("licenseFine.in=" + DEFAULT_LICENSE_FINE + "," + UPDATED_LICENSE_FINE);

        // Get all the taskList where licenseFine equals to UPDATED_LICENSE_FINE
        defaultTaskShouldNotBeFound("licenseFine.in=" + UPDATED_LICENSE_FINE);
    }

    @Test
    @Transactional
    void getAllTasksByLicenseFineIsNullOrNotNull() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);

        // Get all the taskList where licenseFine is not null
        defaultTaskShouldBeFound("licenseFine.specified=true");

        // Get all the taskList where licenseFine is null
        defaultTaskShouldNotBeFound("licenseFine.specified=false");
    }

    @Test
    @Transactional
    void getAllTasksByLicenseFineIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);

        // Get all the taskList where licenseFine is greater than or equal to DEFAULT_LICENSE_FINE
        defaultTaskShouldBeFound("licenseFine.greaterThanOrEqual=" + DEFAULT_LICENSE_FINE);

        // Get all the taskList where licenseFine is greater than or equal to UPDATED_LICENSE_FINE
        defaultTaskShouldNotBeFound("licenseFine.greaterThanOrEqual=" + UPDATED_LICENSE_FINE);
    }

    @Test
    @Transactional
    void getAllTasksByLicenseFineIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);

        // Get all the taskList where licenseFine is less than or equal to DEFAULT_LICENSE_FINE
        defaultTaskShouldBeFound("licenseFine.lessThanOrEqual=" + DEFAULT_LICENSE_FINE);

        // Get all the taskList where licenseFine is less than or equal to SMALLER_LICENSE_FINE
        defaultTaskShouldNotBeFound("licenseFine.lessThanOrEqual=" + SMALLER_LICENSE_FINE);
    }

    @Test
    @Transactional
    void getAllTasksByLicenseFineIsLessThanSomething() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);

        // Get all the taskList where licenseFine is less than DEFAULT_LICENSE_FINE
        defaultTaskShouldNotBeFound("licenseFine.lessThan=" + DEFAULT_LICENSE_FINE);

        // Get all the taskList where licenseFine is less than UPDATED_LICENSE_FINE
        defaultTaskShouldBeFound("licenseFine.lessThan=" + UPDATED_LICENSE_FINE);
    }

    @Test
    @Transactional
    void getAllTasksByLicenseFineIsGreaterThanSomething() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);

        // Get all the taskList where licenseFine is greater than DEFAULT_LICENSE_FINE
        defaultTaskShouldNotBeFound("licenseFine.greaterThan=" + DEFAULT_LICENSE_FINE);

        // Get all the taskList where licenseFine is greater than SMALLER_LICENSE_FINE
        defaultTaskShouldBeFound("licenseFine.greaterThan=" + SMALLER_LICENSE_FINE);
    }

    @Test
    @Transactional
    void getAllTasksByStolenIsEqualToSomething() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);

        // Get all the taskList where stolen equals to DEFAULT_STOLEN
        defaultTaskShouldBeFound("stolen.equals=" + DEFAULT_STOLEN);

        // Get all the taskList where stolen equals to UPDATED_STOLEN
        defaultTaskShouldNotBeFound("stolen.equals=" + UPDATED_STOLEN);
    }

    @Test
    @Transactional
    void getAllTasksByStolenIsInShouldWork() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);

        // Get all the taskList where stolen in DEFAULT_STOLEN or UPDATED_STOLEN
        defaultTaskShouldBeFound("stolen.in=" + DEFAULT_STOLEN + "," + UPDATED_STOLEN);

        // Get all the taskList where stolen equals to UPDATED_STOLEN
        defaultTaskShouldNotBeFound("stolen.in=" + UPDATED_STOLEN);
    }

    @Test
    @Transactional
    void getAllTasksByStolenIsNullOrNotNull() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);

        // Get all the taskList where stolen is not null
        defaultTaskShouldBeFound("stolen.specified=true");

        // Get all the taskList where stolen is null
        defaultTaskShouldNotBeFound("stolen.specified=false");
    }

    @Test
    @Transactional
    void getAllTasksByStolenIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);

        // Get all the taskList where stolen is greater than or equal to DEFAULT_STOLEN
        defaultTaskShouldBeFound("stolen.greaterThanOrEqual=" + DEFAULT_STOLEN);

        // Get all the taskList where stolen is greater than or equal to UPDATED_STOLEN
        defaultTaskShouldNotBeFound("stolen.greaterThanOrEqual=" + UPDATED_STOLEN);
    }

    @Test
    @Transactional
    void getAllTasksByStolenIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);

        // Get all the taskList where stolen is less than or equal to DEFAULT_STOLEN
        defaultTaskShouldBeFound("stolen.lessThanOrEqual=" + DEFAULT_STOLEN);

        // Get all the taskList where stolen is less than or equal to SMALLER_STOLEN
        defaultTaskShouldNotBeFound("stolen.lessThanOrEqual=" + SMALLER_STOLEN);
    }

    @Test
    @Transactional
    void getAllTasksByStolenIsLessThanSomething() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);

        // Get all the taskList where stolen is less than DEFAULT_STOLEN
        defaultTaskShouldNotBeFound("stolen.lessThan=" + DEFAULT_STOLEN);

        // Get all the taskList where stolen is less than UPDATED_STOLEN
        defaultTaskShouldBeFound("stolen.lessThan=" + UPDATED_STOLEN);
    }

    @Test
    @Transactional
    void getAllTasksByStolenIsGreaterThanSomething() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);

        // Get all the taskList where stolen is greater than DEFAULT_STOLEN
        defaultTaskShouldNotBeFound("stolen.greaterThan=" + DEFAULT_STOLEN);

        // Get all the taskList where stolen is greater than SMALLER_STOLEN
        defaultTaskShouldBeFound("stolen.greaterThan=" + SMALLER_STOLEN);
    }

    @Test
    @Transactional
    void getAllTasksByWantedIsEqualToSomething() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);

        // Get all the taskList where wanted equals to DEFAULT_WANTED
        defaultTaskShouldBeFound("wanted.equals=" + DEFAULT_WANTED);

        // Get all the taskList where wanted equals to UPDATED_WANTED
        defaultTaskShouldNotBeFound("wanted.equals=" + UPDATED_WANTED);
    }

    @Test
    @Transactional
    void getAllTasksByWantedIsInShouldWork() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);

        // Get all the taskList where wanted in DEFAULT_WANTED or UPDATED_WANTED
        defaultTaskShouldBeFound("wanted.in=" + DEFAULT_WANTED + "," + UPDATED_WANTED);

        // Get all the taskList where wanted equals to UPDATED_WANTED
        defaultTaskShouldNotBeFound("wanted.in=" + UPDATED_WANTED);
    }

    @Test
    @Transactional
    void getAllTasksByWantedIsNullOrNotNull() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);

        // Get all the taskList where wanted is not null
        defaultTaskShouldBeFound("wanted.specified=true");

        // Get all the taskList where wanted is null
        defaultTaskShouldNotBeFound("wanted.specified=false");
    }

    @Test
    @Transactional
    void getAllTasksByRuleSentIsEqualToSomething() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);

        // Get all the taskList where ruleSent equals to DEFAULT_RULE_SENT
        defaultTaskShouldBeFound("ruleSent.equals=" + DEFAULT_RULE_SENT);

        // Get all the taskList where ruleSent equals to UPDATED_RULE_SENT
        defaultTaskShouldNotBeFound("ruleSent.equals=" + UPDATED_RULE_SENT);
    }

    @Test
    @Transactional
    void getAllTasksByRuleSentIsInShouldWork() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);

        // Get all the taskList where ruleSent in DEFAULT_RULE_SENT or UPDATED_RULE_SENT
        defaultTaskShouldBeFound("ruleSent.in=" + DEFAULT_RULE_SENT + "," + UPDATED_RULE_SENT);

        // Get all the taskList where ruleSent equals to UPDATED_RULE_SENT
        defaultTaskShouldNotBeFound("ruleSent.in=" + UPDATED_RULE_SENT);
    }

    @Test
    @Transactional
    void getAllTasksByRuleSentIsNullOrNotNull() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);

        // Get all the taskList where ruleSent is not null
        defaultTaskShouldBeFound("ruleSent.specified=true");

        // Get all the taskList where ruleSent is null
        defaultTaskShouldNotBeFound("ruleSent.specified=false");
    }

    @Test
    @Transactional
    void getAllTasksByRuleSentIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);

        // Get all the taskList where ruleSent is greater than or equal to DEFAULT_RULE_SENT
        defaultTaskShouldBeFound("ruleSent.greaterThanOrEqual=" + DEFAULT_RULE_SENT);

        // Get all the taskList where ruleSent is greater than or equal to UPDATED_RULE_SENT
        defaultTaskShouldNotBeFound("ruleSent.greaterThanOrEqual=" + UPDATED_RULE_SENT);
    }

    @Test
    @Transactional
    void getAllTasksByRuleSentIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);

        // Get all the taskList where ruleSent is less than or equal to DEFAULT_RULE_SENT
        defaultTaskShouldBeFound("ruleSent.lessThanOrEqual=" + DEFAULT_RULE_SENT);

        // Get all the taskList where ruleSent is less than or equal to SMALLER_RULE_SENT
        defaultTaskShouldNotBeFound("ruleSent.lessThanOrEqual=" + SMALLER_RULE_SENT);
    }

    @Test
    @Transactional
    void getAllTasksByRuleSentIsLessThanSomething() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);

        // Get all the taskList where ruleSent is less than DEFAULT_RULE_SENT
        defaultTaskShouldNotBeFound("ruleSent.lessThan=" + DEFAULT_RULE_SENT);

        // Get all the taskList where ruleSent is less than UPDATED_RULE_SENT
        defaultTaskShouldBeFound("ruleSent.lessThan=" + UPDATED_RULE_SENT);
    }

    @Test
    @Transactional
    void getAllTasksByRuleSentIsGreaterThanSomething() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);

        // Get all the taskList where ruleSent is greater than DEFAULT_RULE_SENT
        defaultTaskShouldNotBeFound("ruleSent.greaterThan=" + DEFAULT_RULE_SENT);

        // Get all the taskList where ruleSent is greater than SMALLER_RULE_SENT
        defaultTaskShouldBeFound("ruleSent.greaterThan=" + SMALLER_RULE_SENT);
    }

    @Test
    @Transactional
    void getAllTasksByHandledIsEqualToSomething() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);

        // Get all the taskList where handled equals to DEFAULT_HANDLED
        defaultTaskShouldBeFound("handled.equals=" + DEFAULT_HANDLED);

        // Get all the taskList where handled equals to UPDATED_HANDLED
        defaultTaskShouldNotBeFound("handled.equals=" + UPDATED_HANDLED);
    }

    @Test
    @Transactional
    void getAllTasksByHandledIsInShouldWork() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);

        // Get all the taskList where handled in DEFAULT_HANDLED or UPDATED_HANDLED
        defaultTaskShouldBeFound("handled.in=" + DEFAULT_HANDLED + "," + UPDATED_HANDLED);

        // Get all the taskList where handled equals to UPDATED_HANDLED
        defaultTaskShouldNotBeFound("handled.in=" + UPDATED_HANDLED);
    }

    @Test
    @Transactional
    void getAllTasksByHandledIsNullOrNotNull() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);

        // Get all the taskList where handled is not null
        defaultTaskShouldBeFound("handled.specified=true");

        // Get all the taskList where handled is null
        defaultTaskShouldNotBeFound("handled.specified=false");
    }

    @Test
    @Transactional
    void getAllTasksByHandledIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);

        // Get all the taskList where handled is greater than or equal to DEFAULT_HANDLED
        defaultTaskShouldBeFound("handled.greaterThanOrEqual=" + DEFAULT_HANDLED);

        // Get all the taskList where handled is greater than or equal to UPDATED_HANDLED
        defaultTaskShouldNotBeFound("handled.greaterThanOrEqual=" + UPDATED_HANDLED);
    }

    @Test
    @Transactional
    void getAllTasksByHandledIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);

        // Get all the taskList where handled is less than or equal to DEFAULT_HANDLED
        defaultTaskShouldBeFound("handled.lessThanOrEqual=" + DEFAULT_HANDLED);

        // Get all the taskList where handled is less than or equal to SMALLER_HANDLED
        defaultTaskShouldNotBeFound("handled.lessThanOrEqual=" + SMALLER_HANDLED);
    }

    @Test
    @Transactional
    void getAllTasksByHandledIsLessThanSomething() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);

        // Get all the taskList where handled is less than DEFAULT_HANDLED
        defaultTaskShouldNotBeFound("handled.lessThan=" + DEFAULT_HANDLED);

        // Get all the taskList where handled is less than UPDATED_HANDLED
        defaultTaskShouldBeFound("handled.lessThan=" + UPDATED_HANDLED);
    }

    @Test
    @Transactional
    void getAllTasksByHandledIsGreaterThanSomething() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);

        // Get all the taskList where handled is greater than DEFAULT_HANDLED
        defaultTaskShouldNotBeFound("handled.greaterThan=" + DEFAULT_HANDLED);

        // Get all the taskList where handled is greater than SMALLER_HANDLED
        defaultTaskShouldBeFound("handled.greaterThan=" + SMALLER_HANDLED);
    }

    @Test
    @Transactional
    void getAllTasksByRuleIssueIsEqualToSomething() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);

        // Get all the taskList where ruleIssue equals to DEFAULT_RULE_ISSUE
        defaultTaskShouldBeFound("ruleIssue.equals=" + DEFAULT_RULE_ISSUE);

        // Get all the taskList where ruleIssue equals to UPDATED_RULE_ISSUE
        defaultTaskShouldNotBeFound("ruleIssue.equals=" + UPDATED_RULE_ISSUE);
    }

    @Test
    @Transactional
    void getAllTasksByRuleIssueIsInShouldWork() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);

        // Get all the taskList where ruleIssue in DEFAULT_RULE_ISSUE or UPDATED_RULE_ISSUE
        defaultTaskShouldBeFound("ruleIssue.in=" + DEFAULT_RULE_ISSUE + "," + UPDATED_RULE_ISSUE);

        // Get all the taskList where ruleIssue equals to UPDATED_RULE_ISSUE
        defaultTaskShouldNotBeFound("ruleIssue.in=" + UPDATED_RULE_ISSUE);
    }

    @Test
    @Transactional
    void getAllTasksByRuleIssueIsNullOrNotNull() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);

        // Get all the taskList where ruleIssue is not null
        defaultTaskShouldBeFound("ruleIssue.specified=true");

        // Get all the taskList where ruleIssue is null
        defaultTaskShouldNotBeFound("ruleIssue.specified=false");
    }

    @Test
    @Transactional
    void getAllTasksByRuleIssueContainsSomething() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);

        // Get all the taskList where ruleIssue contains DEFAULT_RULE_ISSUE
        defaultTaskShouldBeFound("ruleIssue.contains=" + DEFAULT_RULE_ISSUE);

        // Get all the taskList where ruleIssue contains UPDATED_RULE_ISSUE
        defaultTaskShouldNotBeFound("ruleIssue.contains=" + UPDATED_RULE_ISSUE);
    }

    @Test
    @Transactional
    void getAllTasksByRuleIssueNotContainsSomething() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);

        // Get all the taskList where ruleIssue does not contain DEFAULT_RULE_ISSUE
        defaultTaskShouldNotBeFound("ruleIssue.doesNotContain=" + DEFAULT_RULE_ISSUE);

        // Get all the taskList where ruleIssue does not contain UPDATED_RULE_ISSUE
        defaultTaskShouldBeFound("ruleIssue.doesNotContain=" + UPDATED_RULE_ISSUE);
    }

    @Test
    @Transactional
    void getAllTasksByProcessingTimeIsEqualToSomething() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);

        // Get all the taskList where processingTime equals to DEFAULT_PROCESSING_TIME
        defaultTaskShouldBeFound("processingTime.equals=" + DEFAULT_PROCESSING_TIME);

        // Get all the taskList where processingTime equals to UPDATED_PROCESSING_TIME
        defaultTaskShouldNotBeFound("processingTime.equals=" + UPDATED_PROCESSING_TIME);
    }

    @Test
    @Transactional
    void getAllTasksByProcessingTimeIsInShouldWork() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);

        // Get all the taskList where processingTime in DEFAULT_PROCESSING_TIME or UPDATED_PROCESSING_TIME
        defaultTaskShouldBeFound("processingTime.in=" + DEFAULT_PROCESSING_TIME + "," + UPDATED_PROCESSING_TIME);

        // Get all the taskList where processingTime equals to UPDATED_PROCESSING_TIME
        defaultTaskShouldNotBeFound("processingTime.in=" + UPDATED_PROCESSING_TIME);
    }

    @Test
    @Transactional
    void getAllTasksByProcessingTimeIsNullOrNotNull() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);

        // Get all the taskList where processingTime is not null
        defaultTaskShouldBeFound("processingTime.specified=true");

        // Get all the taskList where processingTime is null
        defaultTaskShouldNotBeFound("processingTime.specified=false");
    }

    @Test
    @Transactional
    void getAllTasksByProcessingTimeIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);

        // Get all the taskList where processingTime is greater than or equal to DEFAULT_PROCESSING_TIME
        defaultTaskShouldBeFound("processingTime.greaterThanOrEqual=" + DEFAULT_PROCESSING_TIME);

        // Get all the taskList where processingTime is greater than or equal to UPDATED_PROCESSING_TIME
        defaultTaskShouldNotBeFound("processingTime.greaterThanOrEqual=" + UPDATED_PROCESSING_TIME);
    }

    @Test
    @Transactional
    void getAllTasksByProcessingTimeIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);

        // Get all the taskList where processingTime is less than or equal to DEFAULT_PROCESSING_TIME
        defaultTaskShouldBeFound("processingTime.lessThanOrEqual=" + DEFAULT_PROCESSING_TIME);

        // Get all the taskList where processingTime is less than or equal to SMALLER_PROCESSING_TIME
        defaultTaskShouldNotBeFound("processingTime.lessThanOrEqual=" + SMALLER_PROCESSING_TIME);
    }

    @Test
    @Transactional
    void getAllTasksByProcessingTimeIsLessThanSomething() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);

        // Get all the taskList where processingTime is less than DEFAULT_PROCESSING_TIME
        defaultTaskShouldNotBeFound("processingTime.lessThan=" + DEFAULT_PROCESSING_TIME);

        // Get all the taskList where processingTime is less than UPDATED_PROCESSING_TIME
        defaultTaskShouldBeFound("processingTime.lessThan=" + UPDATED_PROCESSING_TIME);
    }

    @Test
    @Transactional
    void getAllTasksByProcessingTimeIsGreaterThanSomething() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);

        // Get all the taskList where processingTime is greater than DEFAULT_PROCESSING_TIME
        defaultTaskShouldNotBeFound("processingTime.greaterThan=" + DEFAULT_PROCESSING_TIME);

        // Get all the taskList where processingTime is greater than SMALLER_PROCESSING_TIME
        defaultTaskShouldBeFound("processingTime.greaterThan=" + SMALLER_PROCESSING_TIME);
    }

    @Test
    @Transactional
    void getAllTasksByThreadRandNoIsEqualToSomething() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);

        // Get all the taskList where threadRandNo equals to DEFAULT_THREAD_RAND_NO
        defaultTaskShouldBeFound("threadRandNo.equals=" + DEFAULT_THREAD_RAND_NO);

        // Get all the taskList where threadRandNo equals to UPDATED_THREAD_RAND_NO
        defaultTaskShouldNotBeFound("threadRandNo.equals=" + UPDATED_THREAD_RAND_NO);
    }

    @Test
    @Transactional
    void getAllTasksByThreadRandNoIsInShouldWork() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);

        // Get all the taskList where threadRandNo in DEFAULT_THREAD_RAND_NO or UPDATED_THREAD_RAND_NO
        defaultTaskShouldBeFound("threadRandNo.in=" + DEFAULT_THREAD_RAND_NO + "," + UPDATED_THREAD_RAND_NO);

        // Get all the taskList where threadRandNo equals to UPDATED_THREAD_RAND_NO
        defaultTaskShouldNotBeFound("threadRandNo.in=" + UPDATED_THREAD_RAND_NO);
    }

    @Test
    @Transactional
    void getAllTasksByThreadRandNoIsNullOrNotNull() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);

        // Get all the taskList where threadRandNo is not null
        defaultTaskShouldBeFound("threadRandNo.specified=true");

        // Get all the taskList where threadRandNo is null
        defaultTaskShouldNotBeFound("threadRandNo.specified=false");
    }

    @Test
    @Transactional
    void getAllTasksByThreadRandNoContainsSomething() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);

        // Get all the taskList where threadRandNo contains DEFAULT_THREAD_RAND_NO
        defaultTaskShouldBeFound("threadRandNo.contains=" + DEFAULT_THREAD_RAND_NO);

        // Get all the taskList where threadRandNo contains UPDATED_THREAD_RAND_NO
        defaultTaskShouldNotBeFound("threadRandNo.contains=" + UPDATED_THREAD_RAND_NO);
    }

    @Test
    @Transactional
    void getAllTasksByThreadRandNoNotContainsSomething() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);

        // Get all the taskList where threadRandNo does not contain DEFAULT_THREAD_RAND_NO
        defaultTaskShouldNotBeFound("threadRandNo.doesNotContain=" + DEFAULT_THREAD_RAND_NO);

        // Get all the taskList where threadRandNo does not contain UPDATED_THREAD_RAND_NO
        defaultTaskShouldBeFound("threadRandNo.doesNotContain=" + UPDATED_THREAD_RAND_NO);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultTaskShouldBeFound(String filter) throws Exception {
        restTaskMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(task.getId().intValue())))
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
            .andExpect(jsonPath("$.[*].toll").value(hasItem(DEFAULT_TOLL.doubleValue())))
            .andExpect(jsonPath("$.[*].ruleRcvd").value(hasItem(DEFAULT_RULE_RCVD.intValue())))
            .andExpect(jsonPath("$.[*].wantedFor").value(hasItem(DEFAULT_WANTED_FOR)))
            .andExpect(jsonPath("$.[*].fine").value(hasItem(DEFAULT_FINE.doubleValue())))
            .andExpect(jsonPath("$.[*].licenseIssue").value(hasItem(DEFAULT_LICENSE_ISSUE)))
            .andExpect(jsonPath("$.[*].wantedBy").value(hasItem(DEFAULT_WANTED_BY.intValue())))
            .andExpect(jsonPath("$.[*].ruleProcessed").value(hasItem(DEFAULT_RULE_PROCESSED.intValue())))
            .andExpect(jsonPath("$.[*].speedFine").value(hasItem(DEFAULT_SPEED_FINE.doubleValue())))
            .andExpect(jsonPath("$.[*].lane").value(hasItem(DEFAULT_LANE.intValue())))
            .andExpect(jsonPath("$.[*].tagIssue").value(hasItem(DEFAULT_TAG_ISSUE)))
            .andExpect(jsonPath("$.[*].statusName").value(hasItem(DEFAULT_STATUS_NAME)))
            .andExpect(jsonPath("$.[*].licenseFine").value(hasItem(DEFAULT_LICENSE_FINE.doubleValue())))
            .andExpect(jsonPath("$.[*].stolen").value(hasItem(DEFAULT_STOLEN.intValue())))
            .andExpect(jsonPath("$.[*].wanted").value(hasItem(DEFAULT_WANTED.booleanValue())))
            .andExpect(jsonPath("$.[*].ruleSent").value(hasItem(DEFAULT_RULE_SENT.intValue())))
            .andExpect(jsonPath("$.[*].handled").value(hasItem(DEFAULT_HANDLED.intValue())))
            .andExpect(jsonPath("$.[*].ruleIssue").value(hasItem(DEFAULT_RULE_ISSUE)))
            .andExpect(jsonPath("$.[*].processingTime").value(hasItem(DEFAULT_PROCESSING_TIME.intValue())))
            .andExpect(jsonPath("$.[*].threadRandNo").value(hasItem(DEFAULT_THREAD_RAND_NO)));

        // Check, that the count call also returns 1
        restTaskMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultTaskShouldNotBeFound(String filter) throws Exception {
        restTaskMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restTaskMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingTask() throws Exception {
        // Get the task
        restTaskMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingTask() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);

        int databaseSizeBeforeUpdate = taskRepository.findAll().size();
        taskSearchRepository.save(task);
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(taskSearchRepository.findAll());

        // Update the task
        Task updatedTask = taskRepository.findById(task.getId()).get();
        // Disconnect from session so that the updates on updatedTask are not directly saved in db
        em.detach(updatedTask);
        updatedTask
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
            .ruleIssue(UPDATED_RULE_ISSUE)
            .processingTime(UPDATED_PROCESSING_TIME)
            .threadRandNo(UPDATED_THREAD_RAND_NO);
        TaskDTO taskDTO = taskMapper.toDto(updatedTask);

        restTaskMockMvc
            .perform(
                put(ENTITY_API_URL_ID, taskDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(taskDTO))
            )
            .andExpect(status().isOk());

        // Validate the Task in the database
        List<Task> taskList = taskRepository.findAll();
        assertThat(taskList).hasSize(databaseSizeBeforeUpdate);
        Task testTask = taskList.get(taskList.size() - 1);
        assertThat(testTask.getGuid()).isEqualTo(UPDATED_GUID);
        assertThat(testTask.getPlate()).isEqualTo(UPDATED_PLATE);
        assertThat(testTask.getImageLp()).isEqualTo(UPDATED_IMAGE_LP);
        assertThat(testTask.getImageLpContentType()).isEqualTo(UPDATED_IMAGE_LP_CONTENT_TYPE);
        assertThat(testTask.getImageThumb()).isEqualTo(UPDATED_IMAGE_THUMB);
        assertThat(testTask.getImageThumbContentType()).isEqualTo(UPDATED_IMAGE_THUMB_CONTENT_TYPE);
        assertThat(testTask.getAnpr()).isEqualTo(UPDATED_ANPR);
        assertThat(testTask.getRfid()).isEqualTo(UPDATED_RFID);
        assertThat(testTask.getDataStatus()).isEqualTo(UPDATED_DATA_STATUS);
        assertThat(testTask.getGantry()).isEqualTo(UPDATED_GANTRY);
        assertThat(testTask.getKph()).isEqualTo(UPDATED_KPH);
        assertThat(testTask.getAmbush()).isEqualTo(UPDATED_AMBUSH);
        assertThat(testTask.getDirection()).isEqualTo(UPDATED_DIRECTION);
        assertThat(testTask.getVehicle()).isEqualTo(UPDATED_VEHICLE);
        assertThat(testTask.getIssue()).isEqualTo(UPDATED_ISSUE);
        assertThat(testTask.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testTask.getHandledBy()).isEqualTo(UPDATED_HANDLED_BY);
        assertThat(testTask.getGantryProcessed()).isEqualTo(UPDATED_GANTRY_PROCESSED);
        assertThat(testTask.getGantrySent()).isEqualTo(UPDATED_GANTRY_SENT);
        assertThat(testTask.getWhen()).isEqualTo(UPDATED_WHEN);
        assertThat(testTask.getToll()).isEqualTo(UPDATED_TOLL);
        assertThat(testTask.getRuleRcvd()).isEqualTo(UPDATED_RULE_RCVD);
        assertThat(testTask.getWantedFor()).isEqualTo(UPDATED_WANTED_FOR);
        assertThat(testTask.getFine()).isEqualTo(UPDATED_FINE);
        assertThat(testTask.getLicenseIssue()).isEqualTo(UPDATED_LICENSE_ISSUE);
        assertThat(testTask.getWantedBy()).isEqualTo(UPDATED_WANTED_BY);
        assertThat(testTask.getRuleProcessed()).isEqualTo(UPDATED_RULE_PROCESSED);
        assertThat(testTask.getSpeedFine()).isEqualTo(UPDATED_SPEED_FINE);
        assertThat(testTask.getLane()).isEqualTo(UPDATED_LANE);
        assertThat(testTask.getTagIssue()).isEqualTo(UPDATED_TAG_ISSUE);
        assertThat(testTask.getStatusName()).isEqualTo(UPDATED_STATUS_NAME);
        assertThat(testTask.getLicenseFine()).isEqualTo(UPDATED_LICENSE_FINE);
        assertThat(testTask.getStolen()).isEqualTo(UPDATED_STOLEN);
        assertThat(testTask.getWanted()).isEqualTo(UPDATED_WANTED);
        assertThat(testTask.getRuleSent()).isEqualTo(UPDATED_RULE_SENT);
        assertThat(testTask.getHandled()).isEqualTo(UPDATED_HANDLED);
        assertThat(testTask.getRuleIssue()).isEqualTo(UPDATED_RULE_ISSUE);
        assertThat(testTask.getProcessingTime()).isEqualTo(UPDATED_PROCESSING_TIME);
        assertThat(testTask.getThreadRandNo()).isEqualTo(UPDATED_THREAD_RAND_NO);
        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(taskSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
                List<Task> taskSearchList = IterableUtils.toList(taskSearchRepository.findAll());
                Task testTaskSearch = taskSearchList.get(searchDatabaseSizeAfter - 1);
                assertThat(testTaskSearch.getGuid()).isEqualTo(UPDATED_GUID);
                assertThat(testTaskSearch.getPlate()).isEqualTo(UPDATED_PLATE);
                assertThat(testTaskSearch.getImageLp()).isEqualTo(UPDATED_IMAGE_LP);
                assertThat(testTaskSearch.getImageLpContentType()).isEqualTo(UPDATED_IMAGE_LP_CONTENT_TYPE);
                assertThat(testTaskSearch.getImageThumb()).isEqualTo(UPDATED_IMAGE_THUMB);
                assertThat(testTaskSearch.getImageThumbContentType()).isEqualTo(UPDATED_IMAGE_THUMB_CONTENT_TYPE);
                assertThat(testTaskSearch.getAnpr()).isEqualTo(UPDATED_ANPR);
                assertThat(testTaskSearch.getRfid()).isEqualTo(UPDATED_RFID);
                assertThat(testTaskSearch.getDataStatus()).isEqualTo(UPDATED_DATA_STATUS);
                assertThat(testTaskSearch.getGantry()).isEqualTo(UPDATED_GANTRY);
                assertThat(testTaskSearch.getKph()).isEqualTo(UPDATED_KPH);
                assertThat(testTaskSearch.getAmbush()).isEqualTo(UPDATED_AMBUSH);
                assertThat(testTaskSearch.getDirection()).isEqualTo(UPDATED_DIRECTION);
                assertThat(testTaskSearch.getVehicle()).isEqualTo(UPDATED_VEHICLE);
                assertThat(testTaskSearch.getIssue()).isEqualTo(UPDATED_ISSUE);
                assertThat(testTaskSearch.getStatus()).isEqualTo(UPDATED_STATUS);
                assertThat(testTaskSearch.getHandledBy()).isEqualTo(UPDATED_HANDLED_BY);
                assertThat(testTaskSearch.getGantryProcessed()).isEqualTo(UPDATED_GANTRY_PROCESSED);
                assertThat(testTaskSearch.getGantrySent()).isEqualTo(UPDATED_GANTRY_SENT);
                assertThat(testTaskSearch.getWhen()).isEqualTo(UPDATED_WHEN);
                assertThat(testTaskSearch.getToll()).isEqualTo(UPDATED_TOLL);
                assertThat(testTaskSearch.getRuleRcvd()).isEqualTo(UPDATED_RULE_RCVD);
                assertThat(testTaskSearch.getWantedFor()).isEqualTo(UPDATED_WANTED_FOR);
                assertThat(testTaskSearch.getFine()).isEqualTo(UPDATED_FINE);
                assertThat(testTaskSearch.getLicenseIssue()).isEqualTo(UPDATED_LICENSE_ISSUE);
                assertThat(testTaskSearch.getWantedBy()).isEqualTo(UPDATED_WANTED_BY);
                assertThat(testTaskSearch.getRuleProcessed()).isEqualTo(UPDATED_RULE_PROCESSED);
                assertThat(testTaskSearch.getSpeedFine()).isEqualTo(UPDATED_SPEED_FINE);
                assertThat(testTaskSearch.getLane()).isEqualTo(UPDATED_LANE);
                assertThat(testTaskSearch.getTagIssue()).isEqualTo(UPDATED_TAG_ISSUE);
                assertThat(testTaskSearch.getStatusName()).isEqualTo(UPDATED_STATUS_NAME);
                assertThat(testTaskSearch.getLicenseFine()).isEqualTo(UPDATED_LICENSE_FINE);
                assertThat(testTaskSearch.getStolen()).isEqualTo(UPDATED_STOLEN);
                assertThat(testTaskSearch.getWanted()).isEqualTo(UPDATED_WANTED);
                assertThat(testTaskSearch.getRuleSent()).isEqualTo(UPDATED_RULE_SENT);
                assertThat(testTaskSearch.getHandled()).isEqualTo(UPDATED_HANDLED);
                assertThat(testTaskSearch.getRuleIssue()).isEqualTo(UPDATED_RULE_ISSUE);
                assertThat(testTaskSearch.getProcessingTime()).isEqualTo(UPDATED_PROCESSING_TIME);
                assertThat(testTaskSearch.getThreadRandNo()).isEqualTo(UPDATED_THREAD_RAND_NO);
            });
    }

    @Test
    @Transactional
    void putNonExistingTask() throws Exception {
        int databaseSizeBeforeUpdate = taskRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(taskSearchRepository.findAll());
        task.setId(count.incrementAndGet());

        // Create the Task
        TaskDTO taskDTO = taskMapper.toDto(task);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTaskMockMvc
            .perform(
                put(ENTITY_API_URL_ID, taskDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(taskDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Task in the database
        List<Task> taskList = taskRepository.findAll();
        assertThat(taskList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(taskSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithIdMismatchTask() throws Exception {
        int databaseSizeBeforeUpdate = taskRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(taskSearchRepository.findAll());
        task.setId(count.incrementAndGet());

        // Create the Task
        TaskDTO taskDTO = taskMapper.toDto(task);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTaskMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(taskDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Task in the database
        List<Task> taskList = taskRepository.findAll();
        assertThat(taskList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(taskSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTask() throws Exception {
        int databaseSizeBeforeUpdate = taskRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(taskSearchRepository.findAll());
        task.setId(count.incrementAndGet());

        // Create the Task
        TaskDTO taskDTO = taskMapper.toDto(task);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTaskMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(taskDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Task in the database
        List<Task> taskList = taskRepository.findAll();
        assertThat(taskList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(taskSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void partialUpdateTaskWithPatch() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);

        int databaseSizeBeforeUpdate = taskRepository.findAll().size();

        // Update the task using partial update
        Task partialUpdatedTask = new Task();
        partialUpdatedTask.setId(task.getId());

        partialUpdatedTask
            .plate(UPDATED_PLATE)
            .imageThumb(UPDATED_IMAGE_THUMB)
            .imageThumbContentType(UPDATED_IMAGE_THUMB_CONTENT_TYPE)
            .gantry(UPDATED_GANTRY)
            .kph(UPDATED_KPH)
            .ambush(UPDATED_AMBUSH)
            .vehicle(UPDATED_VEHICLE)
            .handledBy(UPDATED_HANDLED_BY)
            .gantryProcessed(UPDATED_GANTRY_PROCESSED)
            .gantrySent(UPDATED_GANTRY_SENT)
            .when(UPDATED_WHEN)
            .toll(UPDATED_TOLL)
            .wantedFor(UPDATED_WANTED_FOR)
            .fine(UPDATED_FINE)
            .wantedBy(UPDATED_WANTED_BY)
            .ruleProcessed(UPDATED_RULE_PROCESSED)
            .lane(UPDATED_LANE)
            .tagIssue(UPDATED_TAG_ISSUE)
            .licenseFine(UPDATED_LICENSE_FINE)
            .ruleIssue(UPDATED_RULE_ISSUE)
            .processingTime(UPDATED_PROCESSING_TIME);

        restTaskMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTask.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTask))
            )
            .andExpect(status().isOk());

        // Validate the Task in the database
        List<Task> taskList = taskRepository.findAll();
        assertThat(taskList).hasSize(databaseSizeBeforeUpdate);
        Task testTask = taskList.get(taskList.size() - 1);
        assertThat(testTask.getGuid()).isEqualTo(DEFAULT_GUID);
        assertThat(testTask.getPlate()).isEqualTo(UPDATED_PLATE);
        assertThat(testTask.getImageLp()).isEqualTo(DEFAULT_IMAGE_LP);
        assertThat(testTask.getImageLpContentType()).isEqualTo(DEFAULT_IMAGE_LP_CONTENT_TYPE);
        assertThat(testTask.getImageThumb()).isEqualTo(UPDATED_IMAGE_THUMB);
        assertThat(testTask.getImageThumbContentType()).isEqualTo(UPDATED_IMAGE_THUMB_CONTENT_TYPE);
        assertThat(testTask.getAnpr()).isEqualTo(DEFAULT_ANPR);
        assertThat(testTask.getRfid()).isEqualTo(DEFAULT_RFID);
        assertThat(testTask.getDataStatus()).isEqualTo(DEFAULT_DATA_STATUS);
        assertThat(testTask.getGantry()).isEqualTo(UPDATED_GANTRY);
        assertThat(testTask.getKph()).isEqualTo(UPDATED_KPH);
        assertThat(testTask.getAmbush()).isEqualTo(UPDATED_AMBUSH);
        assertThat(testTask.getDirection()).isEqualTo(DEFAULT_DIRECTION);
        assertThat(testTask.getVehicle()).isEqualTo(UPDATED_VEHICLE);
        assertThat(testTask.getIssue()).isEqualTo(DEFAULT_ISSUE);
        assertThat(testTask.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testTask.getHandledBy()).isEqualTo(UPDATED_HANDLED_BY);
        assertThat(testTask.getGantryProcessed()).isEqualTo(UPDATED_GANTRY_PROCESSED);
        assertThat(testTask.getGantrySent()).isEqualTo(UPDATED_GANTRY_SENT);
        assertThat(testTask.getWhen()).isEqualTo(UPDATED_WHEN);
        assertThat(testTask.getToll()).isEqualTo(UPDATED_TOLL);
        assertThat(testTask.getRuleRcvd()).isEqualTo(DEFAULT_RULE_RCVD);
        assertThat(testTask.getWantedFor()).isEqualTo(UPDATED_WANTED_FOR);
        assertThat(testTask.getFine()).isEqualTo(UPDATED_FINE);
        assertThat(testTask.getLicenseIssue()).isEqualTo(DEFAULT_LICENSE_ISSUE);
        assertThat(testTask.getWantedBy()).isEqualTo(UPDATED_WANTED_BY);
        assertThat(testTask.getRuleProcessed()).isEqualTo(UPDATED_RULE_PROCESSED);
        assertThat(testTask.getSpeedFine()).isEqualTo(DEFAULT_SPEED_FINE);
        assertThat(testTask.getLane()).isEqualTo(UPDATED_LANE);
        assertThat(testTask.getTagIssue()).isEqualTo(UPDATED_TAG_ISSUE);
        assertThat(testTask.getStatusName()).isEqualTo(DEFAULT_STATUS_NAME);
        assertThat(testTask.getLicenseFine()).isEqualTo(UPDATED_LICENSE_FINE);
        assertThat(testTask.getStolen()).isEqualTo(DEFAULT_STOLEN);
        assertThat(testTask.getWanted()).isEqualTo(DEFAULT_WANTED);
        assertThat(testTask.getRuleSent()).isEqualTo(DEFAULT_RULE_SENT);
        assertThat(testTask.getHandled()).isEqualTo(DEFAULT_HANDLED);
        assertThat(testTask.getRuleIssue()).isEqualTo(UPDATED_RULE_ISSUE);
        assertThat(testTask.getProcessingTime()).isEqualTo(UPDATED_PROCESSING_TIME);
        assertThat(testTask.getThreadRandNo()).isEqualTo(DEFAULT_THREAD_RAND_NO);
    }

    @Test
    @Transactional
    void fullUpdateTaskWithPatch() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);

        int databaseSizeBeforeUpdate = taskRepository.findAll().size();

        // Update the task using partial update
        Task partialUpdatedTask = new Task();
        partialUpdatedTask.setId(task.getId());

        partialUpdatedTask
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
            .ruleIssue(UPDATED_RULE_ISSUE)
            .processingTime(UPDATED_PROCESSING_TIME)
            .threadRandNo(UPDATED_THREAD_RAND_NO);

        restTaskMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTask.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTask))
            )
            .andExpect(status().isOk());

        // Validate the Task in the database
        List<Task> taskList = taskRepository.findAll();
        assertThat(taskList).hasSize(databaseSizeBeforeUpdate);
        Task testTask = taskList.get(taskList.size() - 1);
        assertThat(testTask.getGuid()).isEqualTo(UPDATED_GUID);
        assertThat(testTask.getPlate()).isEqualTo(UPDATED_PLATE);
        assertThat(testTask.getImageLp()).isEqualTo(UPDATED_IMAGE_LP);
        assertThat(testTask.getImageLpContentType()).isEqualTo(UPDATED_IMAGE_LP_CONTENT_TYPE);
        assertThat(testTask.getImageThumb()).isEqualTo(UPDATED_IMAGE_THUMB);
        assertThat(testTask.getImageThumbContentType()).isEqualTo(UPDATED_IMAGE_THUMB_CONTENT_TYPE);
        assertThat(testTask.getAnpr()).isEqualTo(UPDATED_ANPR);
        assertThat(testTask.getRfid()).isEqualTo(UPDATED_RFID);
        assertThat(testTask.getDataStatus()).isEqualTo(UPDATED_DATA_STATUS);
        assertThat(testTask.getGantry()).isEqualTo(UPDATED_GANTRY);
        assertThat(testTask.getKph()).isEqualTo(UPDATED_KPH);
        assertThat(testTask.getAmbush()).isEqualTo(UPDATED_AMBUSH);
        assertThat(testTask.getDirection()).isEqualTo(UPDATED_DIRECTION);
        assertThat(testTask.getVehicle()).isEqualTo(UPDATED_VEHICLE);
        assertThat(testTask.getIssue()).isEqualTo(UPDATED_ISSUE);
        assertThat(testTask.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testTask.getHandledBy()).isEqualTo(UPDATED_HANDLED_BY);
        assertThat(testTask.getGantryProcessed()).isEqualTo(UPDATED_GANTRY_PROCESSED);
        assertThat(testTask.getGantrySent()).isEqualTo(UPDATED_GANTRY_SENT);
        assertThat(testTask.getWhen()).isEqualTo(UPDATED_WHEN);
        assertThat(testTask.getToll()).isEqualTo(UPDATED_TOLL);
        assertThat(testTask.getRuleRcvd()).isEqualTo(UPDATED_RULE_RCVD);
        assertThat(testTask.getWantedFor()).isEqualTo(UPDATED_WANTED_FOR);
        assertThat(testTask.getFine()).isEqualTo(UPDATED_FINE);
        assertThat(testTask.getLicenseIssue()).isEqualTo(UPDATED_LICENSE_ISSUE);
        assertThat(testTask.getWantedBy()).isEqualTo(UPDATED_WANTED_BY);
        assertThat(testTask.getRuleProcessed()).isEqualTo(UPDATED_RULE_PROCESSED);
        assertThat(testTask.getSpeedFine()).isEqualTo(UPDATED_SPEED_FINE);
        assertThat(testTask.getLane()).isEqualTo(UPDATED_LANE);
        assertThat(testTask.getTagIssue()).isEqualTo(UPDATED_TAG_ISSUE);
        assertThat(testTask.getStatusName()).isEqualTo(UPDATED_STATUS_NAME);
        assertThat(testTask.getLicenseFine()).isEqualTo(UPDATED_LICENSE_FINE);
        assertThat(testTask.getStolen()).isEqualTo(UPDATED_STOLEN);
        assertThat(testTask.getWanted()).isEqualTo(UPDATED_WANTED);
        assertThat(testTask.getRuleSent()).isEqualTo(UPDATED_RULE_SENT);
        assertThat(testTask.getHandled()).isEqualTo(UPDATED_HANDLED);
        assertThat(testTask.getRuleIssue()).isEqualTo(UPDATED_RULE_ISSUE);
        assertThat(testTask.getProcessingTime()).isEqualTo(UPDATED_PROCESSING_TIME);
        assertThat(testTask.getThreadRandNo()).isEqualTo(UPDATED_THREAD_RAND_NO);
    }

    @Test
    @Transactional
    void patchNonExistingTask() throws Exception {
        int databaseSizeBeforeUpdate = taskRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(taskSearchRepository.findAll());
        task.setId(count.incrementAndGet());

        // Create the Task
        TaskDTO taskDTO = taskMapper.toDto(task);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTaskMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, taskDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(taskDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Task in the database
        List<Task> taskList = taskRepository.findAll();
        assertThat(taskList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(taskSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTask() throws Exception {
        int databaseSizeBeforeUpdate = taskRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(taskSearchRepository.findAll());
        task.setId(count.incrementAndGet());

        // Create the Task
        TaskDTO taskDTO = taskMapper.toDto(task);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTaskMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(taskDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Task in the database
        List<Task> taskList = taskRepository.findAll();
        assertThat(taskList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(taskSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTask() throws Exception {
        int databaseSizeBeforeUpdate = taskRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(taskSearchRepository.findAll());
        task.setId(count.incrementAndGet());

        // Create the Task
        TaskDTO taskDTO = taskMapper.toDto(task);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTaskMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(taskDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Task in the database
        List<Task> taskList = taskRepository.findAll();
        assertThat(taskList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(taskSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void deleteTask() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);
        taskRepository.save(task);
        taskSearchRepository.save(task);

        int databaseSizeBeforeDelete = taskRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(taskSearchRepository.findAll());
        assertThat(searchDatabaseSizeBefore).isEqualTo(databaseSizeBeforeDelete);

        // Delete the task
        restTaskMockMvc
            .perform(delete(ENTITY_API_URL_ID, task.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Task> taskList = taskRepository.findAll();
        assertThat(taskList).hasSize(databaseSizeBeforeDelete - 1);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(taskSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore - 1);
    }

    @Test
    @Transactional
    void searchTask() throws Exception {
        // Initialize the database
        task = taskRepository.saveAndFlush(task);
        taskSearchRepository.save(task);

        // Search the task
        restTaskMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + task.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(task.getId().intValue())))
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
            .andExpect(jsonPath("$.[*].toll").value(hasItem(DEFAULT_TOLL.doubleValue())))
            .andExpect(jsonPath("$.[*].ruleRcvd").value(hasItem(DEFAULT_RULE_RCVD.intValue())))
            .andExpect(jsonPath("$.[*].wantedFor").value(hasItem(DEFAULT_WANTED_FOR)))
            .andExpect(jsonPath("$.[*].fine").value(hasItem(DEFAULT_FINE.doubleValue())))
            .andExpect(jsonPath("$.[*].licenseIssue").value(hasItem(DEFAULT_LICENSE_ISSUE)))
            .andExpect(jsonPath("$.[*].wantedBy").value(hasItem(DEFAULT_WANTED_BY.intValue())))
            .andExpect(jsonPath("$.[*].ruleProcessed").value(hasItem(DEFAULT_RULE_PROCESSED.intValue())))
            .andExpect(jsonPath("$.[*].speedFine").value(hasItem(DEFAULT_SPEED_FINE.doubleValue())))
            .andExpect(jsonPath("$.[*].lane").value(hasItem(DEFAULT_LANE.intValue())))
            .andExpect(jsonPath("$.[*].tagIssue").value(hasItem(DEFAULT_TAG_ISSUE)))
            .andExpect(jsonPath("$.[*].statusName").value(hasItem(DEFAULT_STATUS_NAME)))
            .andExpect(jsonPath("$.[*].licenseFine").value(hasItem(DEFAULT_LICENSE_FINE.doubleValue())))
            .andExpect(jsonPath("$.[*].stolen").value(hasItem(DEFAULT_STOLEN.intValue())))
            .andExpect(jsonPath("$.[*].wanted").value(hasItem(DEFAULT_WANTED.booleanValue())))
            .andExpect(jsonPath("$.[*].ruleSent").value(hasItem(DEFAULT_RULE_SENT.intValue())))
            .andExpect(jsonPath("$.[*].handled").value(hasItem(DEFAULT_HANDLED.intValue())))
            .andExpect(jsonPath("$.[*].ruleIssue").value(hasItem(DEFAULT_RULE_ISSUE)))
            .andExpect(jsonPath("$.[*].processingTime").value(hasItem(DEFAULT_PROCESSING_TIME.intValue())))
            .andExpect(jsonPath("$.[*].threadRandNo").value(hasItem(DEFAULT_THREAD_RAND_NO)));
    }
}
