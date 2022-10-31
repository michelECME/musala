package com.musala.app.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.musala.app.IntegrationTest;
import com.musala.app.domain.Gateway;
import com.musala.app.domain.Peripheral;
import com.musala.app.domain.enumeration.Status;
import com.musala.app.repository.PeripheralRepository;
import com.musala.app.service.PeripheralService;
import com.musala.app.service.criteria.PeripheralCriteria;
import com.musala.app.service.dto.PeripheralDTO;
import com.musala.app.service.mapper.PeripheralMapper;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
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
 * Integration tests for the {@link PeripheralResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class PeripheralResourceIT {

    private static final Long DEFAULT_UID = 1L;
    private static final Long UPDATED_UID = 2L;
    private static final Long SMALLER_UID = 1L - 1L;

    private static final String DEFAULT_VENDOR = "AAAAAAAAAA";
    private static final String UPDATED_VENDOR = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_DATE_CREATED = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_CREATED = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_DATE_CREATED = LocalDate.ofEpochDay(-1L);

    private static final Status DEFAULT_STATUS = Status.OFFICE;
    private static final Status UPDATED_STATUS = Status.ONLINE;

    private static final String ENTITY_API_URL = "/api/peripherals";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private PeripheralRepository peripheralRepository;

    @Mock
    private PeripheralRepository peripheralRepositoryMock;

    @Autowired
    private PeripheralMapper peripheralMapper;

    @Mock
    private PeripheralService peripheralServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPeripheralMockMvc;

    private Peripheral peripheral;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Peripheral createEntity(EntityManager em) {
        Peripheral peripheral = new Peripheral()
            .uid(DEFAULT_UID)
            .vendor(DEFAULT_VENDOR)
            .date_created(DEFAULT_DATE_CREATED)
            .status(DEFAULT_STATUS);
        return peripheral;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Peripheral createUpdatedEntity(EntityManager em) {
        Peripheral peripheral = new Peripheral()
            .uid(UPDATED_UID)
            .vendor(UPDATED_VENDOR)
            .date_created(UPDATED_DATE_CREATED)
            .status(UPDATED_STATUS);
        return peripheral;
    }

    @BeforeEach
    public void initTest() {
        peripheral = createEntity(em);
    }

    @Test
    @Transactional
    void createPeripheral() throws Exception {
        int databaseSizeBeforeCreate = peripheralRepository.findAll().size();
        // Create the Peripheral
        PeripheralDTO peripheralDTO = peripheralMapper.toDto(peripheral);
        restPeripheralMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(peripheralDTO)))
            .andExpect(status().isCreated());

        // Validate the Peripheral in the database
        List<Peripheral> peripheralList = peripheralRepository.findAll();
        assertThat(peripheralList).hasSize(databaseSizeBeforeCreate + 1);
        Peripheral testPeripheral = peripheralList.get(peripheralList.size() - 1);
        assertThat(testPeripheral.getUid()).isEqualTo(DEFAULT_UID);
        assertThat(testPeripheral.getVendor()).isEqualTo(DEFAULT_VENDOR);
        assertThat(testPeripheral.getDate_created()).isEqualTo(DEFAULT_DATE_CREATED);
        assertThat(testPeripheral.getStatus()).isEqualTo(DEFAULT_STATUS);
    }

    @Test
    @Transactional
    void createPeripheralWithExistingId() throws Exception {
        // Create the Peripheral with an existing ID
        peripheral.setId(1L);
        PeripheralDTO peripheralDTO = peripheralMapper.toDto(peripheral);

        int databaseSizeBeforeCreate = peripheralRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPeripheralMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(peripheralDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Peripheral in the database
        List<Peripheral> peripheralList = peripheralRepository.findAll();
        assertThat(peripheralList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkUidIsRequired() throws Exception {
        int databaseSizeBeforeTest = peripheralRepository.findAll().size();
        // set the field null
        peripheral.setUid(null);

        // Create the Peripheral, which fails.
        PeripheralDTO peripheralDTO = peripheralMapper.toDto(peripheral);

        restPeripheralMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(peripheralDTO)))
            .andExpect(status().isBadRequest());

        List<Peripheral> peripheralList = peripheralRepository.findAll();
        assertThat(peripheralList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkVendorIsRequired() throws Exception {
        int databaseSizeBeforeTest = peripheralRepository.findAll().size();
        // set the field null
        peripheral.setVendor(null);

        // Create the Peripheral, which fails.
        PeripheralDTO peripheralDTO = peripheralMapper.toDto(peripheral);

        restPeripheralMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(peripheralDTO)))
            .andExpect(status().isBadRequest());

        List<Peripheral> peripheralList = peripheralRepository.findAll();
        assertThat(peripheralList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDate_createdIsRequired() throws Exception {
        int databaseSizeBeforeTest = peripheralRepository.findAll().size();
        // set the field null
        peripheral.setDate_created(null);

        // Create the Peripheral, which fails.
        PeripheralDTO peripheralDTO = peripheralMapper.toDto(peripheral);

        restPeripheralMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(peripheralDTO)))
            .andExpect(status().isBadRequest());

        List<Peripheral> peripheralList = peripheralRepository.findAll();
        assertThat(peripheralList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkStatusIsRequired() throws Exception {
        int databaseSizeBeforeTest = peripheralRepository.findAll().size();
        // set the field null
        peripheral.setStatus(null);

        // Create the Peripheral, which fails.
        PeripheralDTO peripheralDTO = peripheralMapper.toDto(peripheral);

        restPeripheralMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(peripheralDTO)))
            .andExpect(status().isBadRequest());

        List<Peripheral> peripheralList = peripheralRepository.findAll();
        assertThat(peripheralList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllPeripherals() throws Exception {
        // Initialize the database
        peripheralRepository.saveAndFlush(peripheral);

        // Get all the peripheralList
        restPeripheralMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(peripheral.getId().intValue())))
            .andExpect(jsonPath("$.[*].uid").value(hasItem(DEFAULT_UID.intValue())))
            .andExpect(jsonPath("$.[*].vendor").value(hasItem(DEFAULT_VENDOR)))
            .andExpect(jsonPath("$.[*].date_created").value(hasItem(DEFAULT_DATE_CREATED.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllPeripheralsWithEagerRelationshipsIsEnabled() throws Exception {
        when(peripheralServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restPeripheralMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(peripheralServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllPeripheralsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(peripheralServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restPeripheralMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(peripheralRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getPeripheral() throws Exception {
        // Initialize the database
        peripheralRepository.saveAndFlush(peripheral);

        // Get the peripheral
        restPeripheralMockMvc
            .perform(get(ENTITY_API_URL_ID, peripheral.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(peripheral.getId().intValue()))
            .andExpect(jsonPath("$.uid").value(DEFAULT_UID.intValue()))
            .andExpect(jsonPath("$.vendor").value(DEFAULT_VENDOR))
            .andExpect(jsonPath("$.date_created").value(DEFAULT_DATE_CREATED.toString()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()));
    }

    @Test
    @Transactional
    void getPeripheralsByIdFiltering() throws Exception {
        // Initialize the database
        peripheralRepository.saveAndFlush(peripheral);

        Long id = peripheral.getId();

        defaultPeripheralShouldBeFound("id.equals=" + id);
        defaultPeripheralShouldNotBeFound("id.notEquals=" + id);

        defaultPeripheralShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultPeripheralShouldNotBeFound("id.greaterThan=" + id);

        defaultPeripheralShouldBeFound("id.lessThanOrEqual=" + id);
        defaultPeripheralShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllPeripheralsByUidIsEqualToSomething() throws Exception {
        // Initialize the database
        peripheralRepository.saveAndFlush(peripheral);

        // Get all the peripheralList where uid equals to DEFAULT_UID
        defaultPeripheralShouldBeFound("uid.equals=" + DEFAULT_UID);

        // Get all the peripheralList where uid equals to UPDATED_UID
        defaultPeripheralShouldNotBeFound("uid.equals=" + UPDATED_UID);
    }

    @Test
    @Transactional
    void getAllPeripheralsByUidIsInShouldWork() throws Exception {
        // Initialize the database
        peripheralRepository.saveAndFlush(peripheral);

        // Get all the peripheralList where uid in DEFAULT_UID or UPDATED_UID
        defaultPeripheralShouldBeFound("uid.in=" + DEFAULT_UID + "," + UPDATED_UID);

        // Get all the peripheralList where uid equals to UPDATED_UID
        defaultPeripheralShouldNotBeFound("uid.in=" + UPDATED_UID);
    }

    @Test
    @Transactional
    void getAllPeripheralsByUidIsNullOrNotNull() throws Exception {
        // Initialize the database
        peripheralRepository.saveAndFlush(peripheral);

        // Get all the peripheralList where uid is not null
        defaultPeripheralShouldBeFound("uid.specified=true");

        // Get all the peripheralList where uid is null
        defaultPeripheralShouldNotBeFound("uid.specified=false");
    }

    @Test
    @Transactional
    void getAllPeripheralsByUidIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        peripheralRepository.saveAndFlush(peripheral);

        // Get all the peripheralList where uid is greater than or equal to DEFAULT_UID
        defaultPeripheralShouldBeFound("uid.greaterThanOrEqual=" + DEFAULT_UID);

        // Get all the peripheralList where uid is greater than or equal to UPDATED_UID
        defaultPeripheralShouldNotBeFound("uid.greaterThanOrEqual=" + UPDATED_UID);
    }

    @Test
    @Transactional
    void getAllPeripheralsByUidIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        peripheralRepository.saveAndFlush(peripheral);

        // Get all the peripheralList where uid is less than or equal to DEFAULT_UID
        defaultPeripheralShouldBeFound("uid.lessThanOrEqual=" + DEFAULT_UID);

        // Get all the peripheralList where uid is less than or equal to SMALLER_UID
        defaultPeripheralShouldNotBeFound("uid.lessThanOrEqual=" + SMALLER_UID);
    }

    @Test
    @Transactional
    void getAllPeripheralsByUidIsLessThanSomething() throws Exception {
        // Initialize the database
        peripheralRepository.saveAndFlush(peripheral);

        // Get all the peripheralList where uid is less than DEFAULT_UID
        defaultPeripheralShouldNotBeFound("uid.lessThan=" + DEFAULT_UID);

        // Get all the peripheralList where uid is less than UPDATED_UID
        defaultPeripheralShouldBeFound("uid.lessThan=" + UPDATED_UID);
    }

    @Test
    @Transactional
    void getAllPeripheralsByUidIsGreaterThanSomething() throws Exception {
        // Initialize the database
        peripheralRepository.saveAndFlush(peripheral);

        // Get all the peripheralList where uid is greater than DEFAULT_UID
        defaultPeripheralShouldNotBeFound("uid.greaterThan=" + DEFAULT_UID);

        // Get all the peripheralList where uid is greater than SMALLER_UID
        defaultPeripheralShouldBeFound("uid.greaterThan=" + SMALLER_UID);
    }

    @Test
    @Transactional
    void getAllPeripheralsByVendorIsEqualToSomething() throws Exception {
        // Initialize the database
        peripheralRepository.saveAndFlush(peripheral);

        // Get all the peripheralList where vendor equals to DEFAULT_VENDOR
        defaultPeripheralShouldBeFound("vendor.equals=" + DEFAULT_VENDOR);

        // Get all the peripheralList where vendor equals to UPDATED_VENDOR
        defaultPeripheralShouldNotBeFound("vendor.equals=" + UPDATED_VENDOR);
    }

    @Test
    @Transactional
    void getAllPeripheralsByVendorIsInShouldWork() throws Exception {
        // Initialize the database
        peripheralRepository.saveAndFlush(peripheral);

        // Get all the peripheralList where vendor in DEFAULT_VENDOR or UPDATED_VENDOR
        defaultPeripheralShouldBeFound("vendor.in=" + DEFAULT_VENDOR + "," + UPDATED_VENDOR);

        // Get all the peripheralList where vendor equals to UPDATED_VENDOR
        defaultPeripheralShouldNotBeFound("vendor.in=" + UPDATED_VENDOR);
    }

    @Test
    @Transactional
    void getAllPeripheralsByVendorIsNullOrNotNull() throws Exception {
        // Initialize the database
        peripheralRepository.saveAndFlush(peripheral);

        // Get all the peripheralList where vendor is not null
        defaultPeripheralShouldBeFound("vendor.specified=true");

        // Get all the peripheralList where vendor is null
        defaultPeripheralShouldNotBeFound("vendor.specified=false");
    }

    @Test
    @Transactional
    void getAllPeripheralsByVendorContainsSomething() throws Exception {
        // Initialize the database
        peripheralRepository.saveAndFlush(peripheral);

        // Get all the peripheralList where vendor contains DEFAULT_VENDOR
        defaultPeripheralShouldBeFound("vendor.contains=" + DEFAULT_VENDOR);

        // Get all the peripheralList where vendor contains UPDATED_VENDOR
        defaultPeripheralShouldNotBeFound("vendor.contains=" + UPDATED_VENDOR);
    }

    @Test
    @Transactional
    void getAllPeripheralsByVendorNotContainsSomething() throws Exception {
        // Initialize the database
        peripheralRepository.saveAndFlush(peripheral);

        // Get all the peripheralList where vendor does not contain DEFAULT_VENDOR
        defaultPeripheralShouldNotBeFound("vendor.doesNotContain=" + DEFAULT_VENDOR);

        // Get all the peripheralList where vendor does not contain UPDATED_VENDOR
        defaultPeripheralShouldBeFound("vendor.doesNotContain=" + UPDATED_VENDOR);
    }

    @Test
    @Transactional
    void getAllPeripheralsByDate_createdIsEqualToSomething() throws Exception {
        // Initialize the database
        peripheralRepository.saveAndFlush(peripheral);

        // Get all the peripheralList where date_created equals to DEFAULT_DATE_CREATED
        defaultPeripheralShouldBeFound("date_created.equals=" + DEFAULT_DATE_CREATED);

        // Get all the peripheralList where date_created equals to UPDATED_DATE_CREATED
        defaultPeripheralShouldNotBeFound("date_created.equals=" + UPDATED_DATE_CREATED);
    }

    @Test
    @Transactional
    void getAllPeripheralsByDate_createdIsInShouldWork() throws Exception {
        // Initialize the database
        peripheralRepository.saveAndFlush(peripheral);

        // Get all the peripheralList where date_created in DEFAULT_DATE_CREATED or UPDATED_DATE_CREATED
        defaultPeripheralShouldBeFound("date_created.in=" + DEFAULT_DATE_CREATED + "," + UPDATED_DATE_CREATED);

        // Get all the peripheralList where date_created equals to UPDATED_DATE_CREATED
        defaultPeripheralShouldNotBeFound("date_created.in=" + UPDATED_DATE_CREATED);
    }

    @Test
    @Transactional
    void getAllPeripheralsByDate_createdIsNullOrNotNull() throws Exception {
        // Initialize the database
        peripheralRepository.saveAndFlush(peripheral);

        // Get all the peripheralList where date_created is not null
        defaultPeripheralShouldBeFound("date_created.specified=true");

        // Get all the peripheralList where date_created is null
        defaultPeripheralShouldNotBeFound("date_created.specified=false");
    }

    @Test
    @Transactional
    void getAllPeripheralsByDate_createdIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        peripheralRepository.saveAndFlush(peripheral);

        // Get all the peripheralList where date_created is greater than or equal to DEFAULT_DATE_CREATED
        defaultPeripheralShouldBeFound("date_created.greaterThanOrEqual=" + DEFAULT_DATE_CREATED);

        // Get all the peripheralList where date_created is greater than or equal to UPDATED_DATE_CREATED
        defaultPeripheralShouldNotBeFound("date_created.greaterThanOrEqual=" + UPDATED_DATE_CREATED);
    }

    @Test
    @Transactional
    void getAllPeripheralsByDate_createdIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        peripheralRepository.saveAndFlush(peripheral);

        // Get all the peripheralList where date_created is less than or equal to DEFAULT_DATE_CREATED
        defaultPeripheralShouldBeFound("date_created.lessThanOrEqual=" + DEFAULT_DATE_CREATED);

        // Get all the peripheralList where date_created is less than or equal to SMALLER_DATE_CREATED
        defaultPeripheralShouldNotBeFound("date_created.lessThanOrEqual=" + SMALLER_DATE_CREATED);
    }

    @Test
    @Transactional
    void getAllPeripheralsByDate_createdIsLessThanSomething() throws Exception {
        // Initialize the database
        peripheralRepository.saveAndFlush(peripheral);

        // Get all the peripheralList where date_created is less than DEFAULT_DATE_CREATED
        defaultPeripheralShouldNotBeFound("date_created.lessThan=" + DEFAULT_DATE_CREATED);

        // Get all the peripheralList where date_created is less than UPDATED_DATE_CREATED
        defaultPeripheralShouldBeFound("date_created.lessThan=" + UPDATED_DATE_CREATED);
    }

    @Test
    @Transactional
    void getAllPeripheralsByDate_createdIsGreaterThanSomething() throws Exception {
        // Initialize the database
        peripheralRepository.saveAndFlush(peripheral);

        // Get all the peripheralList where date_created is greater than DEFAULT_DATE_CREATED
        defaultPeripheralShouldNotBeFound("date_created.greaterThan=" + DEFAULT_DATE_CREATED);

        // Get all the peripheralList where date_created is greater than SMALLER_DATE_CREATED
        defaultPeripheralShouldBeFound("date_created.greaterThan=" + SMALLER_DATE_CREATED);
    }

    @Test
    @Transactional
    void getAllPeripheralsByStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        peripheralRepository.saveAndFlush(peripheral);

        // Get all the peripheralList where status equals to DEFAULT_STATUS
        defaultPeripheralShouldBeFound("status.equals=" + DEFAULT_STATUS);

        // Get all the peripheralList where status equals to UPDATED_STATUS
        defaultPeripheralShouldNotBeFound("status.equals=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllPeripheralsByStatusIsInShouldWork() throws Exception {
        // Initialize the database
        peripheralRepository.saveAndFlush(peripheral);

        // Get all the peripheralList where status in DEFAULT_STATUS or UPDATED_STATUS
        defaultPeripheralShouldBeFound("status.in=" + DEFAULT_STATUS + "," + UPDATED_STATUS);

        // Get all the peripheralList where status equals to UPDATED_STATUS
        defaultPeripheralShouldNotBeFound("status.in=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllPeripheralsByStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        peripheralRepository.saveAndFlush(peripheral);

        // Get all the peripheralList where status is not null
        defaultPeripheralShouldBeFound("status.specified=true");

        // Get all the peripheralList where status is null
        defaultPeripheralShouldNotBeFound("status.specified=false");
    }

    @Test
    @Transactional
    void getAllPeripheralsByGatewayIsEqualToSomething() throws Exception {
        Gateway gateway;
        if (TestUtil.findAll(em, Gateway.class).isEmpty()) {
            peripheralRepository.saveAndFlush(peripheral);
            gateway = GatewayResourceIT.createEntity(em);
        } else {
            gateway = TestUtil.findAll(em, Gateway.class).get(0);
        }
        em.persist(gateway);
        em.flush();
        peripheral.setGateway(gateway);
        peripheralRepository.saveAndFlush(peripheral);
        Long gatewayId = gateway.getId();

        // Get all the peripheralList where gateway equals to gatewayId
        defaultPeripheralShouldBeFound("gatewayId.equals=" + gatewayId);

        // Get all the peripheralList where gateway equals to (gatewayId + 1)
        defaultPeripheralShouldNotBeFound("gatewayId.equals=" + (gatewayId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultPeripheralShouldBeFound(String filter) throws Exception {
        restPeripheralMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(peripheral.getId().intValue())))
            .andExpect(jsonPath("$.[*].uid").value(hasItem(DEFAULT_UID.intValue())))
            .andExpect(jsonPath("$.[*].vendor").value(hasItem(DEFAULT_VENDOR)))
            .andExpect(jsonPath("$.[*].date_created").value(hasItem(DEFAULT_DATE_CREATED.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())));

        // Check, that the count call also returns 1
        restPeripheralMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultPeripheralShouldNotBeFound(String filter) throws Exception {
        restPeripheralMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restPeripheralMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingPeripheral() throws Exception {
        // Get the peripheral
        restPeripheralMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingPeripheral() throws Exception {
        // Initialize the database
        peripheralRepository.saveAndFlush(peripheral);

        int databaseSizeBeforeUpdate = peripheralRepository.findAll().size();

        // Update the peripheral
        Peripheral updatedPeripheral = peripheralRepository.findById(peripheral.getId()).get();
        // Disconnect from session so that the updates on updatedPeripheral are not directly saved in db
        em.detach(updatedPeripheral);
        updatedPeripheral.uid(UPDATED_UID).vendor(UPDATED_VENDOR).date_created(UPDATED_DATE_CREATED).status(UPDATED_STATUS);
        PeripheralDTO peripheralDTO = peripheralMapper.toDto(updatedPeripheral);

        restPeripheralMockMvc
            .perform(
                put(ENTITY_API_URL_ID, peripheralDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(peripheralDTO))
            )
            .andExpect(status().isOk());

        // Validate the Peripheral in the database
        List<Peripheral> peripheralList = peripheralRepository.findAll();
        assertThat(peripheralList).hasSize(databaseSizeBeforeUpdate);
        Peripheral testPeripheral = peripheralList.get(peripheralList.size() - 1);
        assertThat(testPeripheral.getUid()).isEqualTo(UPDATED_UID);
        assertThat(testPeripheral.getVendor()).isEqualTo(UPDATED_VENDOR);
        assertThat(testPeripheral.getDate_created()).isEqualTo(UPDATED_DATE_CREATED);
        assertThat(testPeripheral.getStatus()).isEqualTo(UPDATED_STATUS);
    }

    @Test
    @Transactional
    void putNonExistingPeripheral() throws Exception {
        int databaseSizeBeforeUpdate = peripheralRepository.findAll().size();
        peripheral.setId(count.incrementAndGet());

        // Create the Peripheral
        PeripheralDTO peripheralDTO = peripheralMapper.toDto(peripheral);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPeripheralMockMvc
            .perform(
                put(ENTITY_API_URL_ID, peripheralDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(peripheralDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Peripheral in the database
        List<Peripheral> peripheralList = peripheralRepository.findAll();
        assertThat(peripheralList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPeripheral() throws Exception {
        int databaseSizeBeforeUpdate = peripheralRepository.findAll().size();
        peripheral.setId(count.incrementAndGet());

        // Create the Peripheral
        PeripheralDTO peripheralDTO = peripheralMapper.toDto(peripheral);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPeripheralMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(peripheralDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Peripheral in the database
        List<Peripheral> peripheralList = peripheralRepository.findAll();
        assertThat(peripheralList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPeripheral() throws Exception {
        int databaseSizeBeforeUpdate = peripheralRepository.findAll().size();
        peripheral.setId(count.incrementAndGet());

        // Create the Peripheral
        PeripheralDTO peripheralDTO = peripheralMapper.toDto(peripheral);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPeripheralMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(peripheralDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Peripheral in the database
        List<Peripheral> peripheralList = peripheralRepository.findAll();
        assertThat(peripheralList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePeripheralWithPatch() throws Exception {
        // Initialize the database
        peripheralRepository.saveAndFlush(peripheral);

        int databaseSizeBeforeUpdate = peripheralRepository.findAll().size();

        // Update the peripheral using partial update
        Peripheral partialUpdatedPeripheral = new Peripheral();
        partialUpdatedPeripheral.setId(peripheral.getId());

        partialUpdatedPeripheral.uid(UPDATED_UID).status(UPDATED_STATUS);

        restPeripheralMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPeripheral.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPeripheral))
            )
            .andExpect(status().isOk());

        // Validate the Peripheral in the database
        List<Peripheral> peripheralList = peripheralRepository.findAll();
        assertThat(peripheralList).hasSize(databaseSizeBeforeUpdate);
        Peripheral testPeripheral = peripheralList.get(peripheralList.size() - 1);
        assertThat(testPeripheral.getUid()).isEqualTo(UPDATED_UID);
        assertThat(testPeripheral.getVendor()).isEqualTo(DEFAULT_VENDOR);
        assertThat(testPeripheral.getDate_created()).isEqualTo(DEFAULT_DATE_CREATED);
        assertThat(testPeripheral.getStatus()).isEqualTo(UPDATED_STATUS);
    }

    @Test
    @Transactional
    void fullUpdatePeripheralWithPatch() throws Exception {
        // Initialize the database
        peripheralRepository.saveAndFlush(peripheral);

        int databaseSizeBeforeUpdate = peripheralRepository.findAll().size();

        // Update the peripheral using partial update
        Peripheral partialUpdatedPeripheral = new Peripheral();
        partialUpdatedPeripheral.setId(peripheral.getId());

        partialUpdatedPeripheral.uid(UPDATED_UID).vendor(UPDATED_VENDOR).date_created(UPDATED_DATE_CREATED).status(UPDATED_STATUS);

        restPeripheralMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPeripheral.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPeripheral))
            )
            .andExpect(status().isOk());

        // Validate the Peripheral in the database
        List<Peripheral> peripheralList = peripheralRepository.findAll();
        assertThat(peripheralList).hasSize(databaseSizeBeforeUpdate);
        Peripheral testPeripheral = peripheralList.get(peripheralList.size() - 1);
        assertThat(testPeripheral.getUid()).isEqualTo(UPDATED_UID);
        assertThat(testPeripheral.getVendor()).isEqualTo(UPDATED_VENDOR);
        assertThat(testPeripheral.getDate_created()).isEqualTo(UPDATED_DATE_CREATED);
        assertThat(testPeripheral.getStatus()).isEqualTo(UPDATED_STATUS);
    }

    @Test
    @Transactional
    void patchNonExistingPeripheral() throws Exception {
        int databaseSizeBeforeUpdate = peripheralRepository.findAll().size();
        peripheral.setId(count.incrementAndGet());

        // Create the Peripheral
        PeripheralDTO peripheralDTO = peripheralMapper.toDto(peripheral);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPeripheralMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, peripheralDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(peripheralDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Peripheral in the database
        List<Peripheral> peripheralList = peripheralRepository.findAll();
        assertThat(peripheralList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPeripheral() throws Exception {
        int databaseSizeBeforeUpdate = peripheralRepository.findAll().size();
        peripheral.setId(count.incrementAndGet());

        // Create the Peripheral
        PeripheralDTO peripheralDTO = peripheralMapper.toDto(peripheral);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPeripheralMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(peripheralDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Peripheral in the database
        List<Peripheral> peripheralList = peripheralRepository.findAll();
        assertThat(peripheralList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPeripheral() throws Exception {
        int databaseSizeBeforeUpdate = peripheralRepository.findAll().size();
        peripheral.setId(count.incrementAndGet());

        // Create the Peripheral
        PeripheralDTO peripheralDTO = peripheralMapper.toDto(peripheral);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPeripheralMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(peripheralDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Peripheral in the database
        List<Peripheral> peripheralList = peripheralRepository.findAll();
        assertThat(peripheralList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePeripheral() throws Exception {
        // Initialize the database
        peripheralRepository.saveAndFlush(peripheral);

        int databaseSizeBeforeDelete = peripheralRepository.findAll().size();

        // Delete the peripheral
        restPeripheralMockMvc
            .perform(delete(ENTITY_API_URL_ID, peripheral.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Peripheral> peripheralList = peripheralRepository.findAll();
        assertThat(peripheralList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
