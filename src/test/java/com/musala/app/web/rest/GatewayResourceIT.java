package com.musala.app.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.musala.app.IntegrationTest;
import com.musala.app.domain.Gateway;
import com.musala.app.repository.GatewayRepository;
import com.musala.app.service.criteria.GatewayCriteria;
import com.musala.app.service.dto.GatewayDTO;
import com.musala.app.service.mapper.GatewayMapper;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link GatewayResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class GatewayResourceIT {

    private static final String DEFAULT_SERIAL_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_SERIAL_NUMBER = "BBBBBBBBBB";

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_IP_ADDRESS = "afdadfadsf";
    private static final String UPDATED_IP_ADDRESS = "afdadfadsfB";

    private static final String ENTITY_API_URL = "/api/gateways";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private GatewayRepository gatewayRepository;

    @Autowired
    private GatewayMapper gatewayMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restGatewayMockMvc;

    private Gateway gateway;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Gateway createEntity(EntityManager em) {
        Gateway gateway = new Gateway().serial_number(DEFAULT_SERIAL_NUMBER).name(DEFAULT_NAME).ip_address(DEFAULT_IP_ADDRESS);
        return gateway;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Gateway createUpdatedEntity(EntityManager em) {
        Gateway gateway = new Gateway().serial_number(UPDATED_SERIAL_NUMBER).name(UPDATED_NAME).ip_address(UPDATED_IP_ADDRESS);
        return gateway;
    }

    @BeforeEach
    public void initTest() {
        gateway = createEntity(em);
    }

    @Test
    @Transactional
    void createGateway() throws Exception {
        int databaseSizeBeforeCreate = gatewayRepository.findAll().size();
        // Create the Gateway
        GatewayDTO gatewayDTO = gatewayMapper.toDto(gateway);
        restGatewayMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(gatewayDTO)))
            .andExpect(status().isCreated());

        // Validate the Gateway in the database
        List<Gateway> gatewayList = gatewayRepository.findAll();
        assertThat(gatewayList).hasSize(databaseSizeBeforeCreate + 1);
        Gateway testGateway = gatewayList.get(gatewayList.size() - 1);
        assertThat(testGateway.getSerial_number()).isEqualTo(DEFAULT_SERIAL_NUMBER);
        assertThat(testGateway.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testGateway.getIp_address()).isEqualTo(DEFAULT_IP_ADDRESS);
    }

    @Test
    @Transactional
    void createGatewayWithExistingId() throws Exception {
        // Create the Gateway with an existing ID
        gateway.setId(1L);
        GatewayDTO gatewayDTO = gatewayMapper.toDto(gateway);

        int databaseSizeBeforeCreate = gatewayRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restGatewayMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(gatewayDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Gateway in the database
        List<Gateway> gatewayList = gatewayRepository.findAll();
        assertThat(gatewayList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkSerial_numberIsRequired() throws Exception {
        int databaseSizeBeforeTest = gatewayRepository.findAll().size();
        // set the field null
        gateway.setSerial_number(null);

        // Create the Gateway, which fails.
        GatewayDTO gatewayDTO = gatewayMapper.toDto(gateway);

        restGatewayMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(gatewayDTO)))
            .andExpect(status().isBadRequest());

        List<Gateway> gatewayList = gatewayRepository.findAll();
        assertThat(gatewayList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = gatewayRepository.findAll().size();
        // set the field null
        gateway.setName(null);

        // Create the Gateway, which fails.
        GatewayDTO gatewayDTO = gatewayMapper.toDto(gateway);

        restGatewayMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(gatewayDTO)))
            .andExpect(status().isBadRequest());

        List<Gateway> gatewayList = gatewayRepository.findAll();
        assertThat(gatewayList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkIp_addressIsRequired() throws Exception {
        int databaseSizeBeforeTest = gatewayRepository.findAll().size();
        // set the field null
        gateway.setIp_address(null);

        // Create the Gateway, which fails.
        GatewayDTO gatewayDTO = gatewayMapper.toDto(gateway);

        restGatewayMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(gatewayDTO)))
            .andExpect(status().isBadRequest());

        List<Gateway> gatewayList = gatewayRepository.findAll();
        assertThat(gatewayList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllGateways() throws Exception {
        // Initialize the database
        gatewayRepository.saveAndFlush(gateway);

        // Get all the gatewayList
        restGatewayMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(gateway.getId().intValue())))
            .andExpect(jsonPath("$.[*].serial_number").value(hasItem(DEFAULT_SERIAL_NUMBER)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].ip_address").value(hasItem(DEFAULT_IP_ADDRESS)));
    }

    @Test
    @Transactional
    void getGateway() throws Exception {
        // Initialize the database
        gatewayRepository.saveAndFlush(gateway);

        // Get the gateway
        restGatewayMockMvc
            .perform(get(ENTITY_API_URL_ID, gateway.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(gateway.getId().intValue()))
            .andExpect(jsonPath("$.serial_number").value(DEFAULT_SERIAL_NUMBER))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.ip_address").value(DEFAULT_IP_ADDRESS));
    }

    @Test
    @Transactional
    void getGatewaysByIdFiltering() throws Exception {
        // Initialize the database
        gatewayRepository.saveAndFlush(gateway);

        Long id = gateway.getId();

        defaultGatewayShouldBeFound("id.equals=" + id);
        defaultGatewayShouldNotBeFound("id.notEquals=" + id);

        defaultGatewayShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultGatewayShouldNotBeFound("id.greaterThan=" + id);

        defaultGatewayShouldBeFound("id.lessThanOrEqual=" + id);
        defaultGatewayShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllGatewaysBySerial_numberIsEqualToSomething() throws Exception {
        // Initialize the database
        gatewayRepository.saveAndFlush(gateway);

        // Get all the gatewayList where serial_number equals to DEFAULT_SERIAL_NUMBER
        defaultGatewayShouldBeFound("serial_number.equals=" + DEFAULT_SERIAL_NUMBER);

        // Get all the gatewayList where serial_number equals to UPDATED_SERIAL_NUMBER
        defaultGatewayShouldNotBeFound("serial_number.equals=" + UPDATED_SERIAL_NUMBER);
    }

    @Test
    @Transactional
    void getAllGatewaysBySerial_numberIsInShouldWork() throws Exception {
        // Initialize the database
        gatewayRepository.saveAndFlush(gateway);

        // Get all the gatewayList where serial_number in DEFAULT_SERIAL_NUMBER or UPDATED_SERIAL_NUMBER
        defaultGatewayShouldBeFound("serial_number.in=" + DEFAULT_SERIAL_NUMBER + "," + UPDATED_SERIAL_NUMBER);

        // Get all the gatewayList where serial_number equals to UPDATED_SERIAL_NUMBER
        defaultGatewayShouldNotBeFound("serial_number.in=" + UPDATED_SERIAL_NUMBER);
    }

    @Test
    @Transactional
    void getAllGatewaysBySerial_numberIsNullOrNotNull() throws Exception {
        // Initialize the database
        gatewayRepository.saveAndFlush(gateway);

        // Get all the gatewayList where serial_number is not null
        defaultGatewayShouldBeFound("serial_number.specified=true");

        // Get all the gatewayList where serial_number is null
        defaultGatewayShouldNotBeFound("serial_number.specified=false");
    }

    @Test
    @Transactional
    void getAllGatewaysBySerial_numberContainsSomething() throws Exception {
        // Initialize the database
        gatewayRepository.saveAndFlush(gateway);

        // Get all the gatewayList where serial_number contains DEFAULT_SERIAL_NUMBER
        defaultGatewayShouldBeFound("serial_number.contains=" + DEFAULT_SERIAL_NUMBER);

        // Get all the gatewayList where serial_number contains UPDATED_SERIAL_NUMBER
        defaultGatewayShouldNotBeFound("serial_number.contains=" + UPDATED_SERIAL_NUMBER);
    }

    @Test
    @Transactional
    void getAllGatewaysBySerial_numberNotContainsSomething() throws Exception {
        // Initialize the database
        gatewayRepository.saveAndFlush(gateway);

        // Get all the gatewayList where serial_number does not contain DEFAULT_SERIAL_NUMBER
        defaultGatewayShouldNotBeFound("serial_number.doesNotContain=" + DEFAULT_SERIAL_NUMBER);

        // Get all the gatewayList where serial_number does not contain UPDATED_SERIAL_NUMBER
        defaultGatewayShouldBeFound("serial_number.doesNotContain=" + UPDATED_SERIAL_NUMBER);
    }

    @Test
    @Transactional
    void getAllGatewaysByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        gatewayRepository.saveAndFlush(gateway);

        // Get all the gatewayList where name equals to DEFAULT_NAME
        defaultGatewayShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the gatewayList where name equals to UPDATED_NAME
        defaultGatewayShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllGatewaysByNameIsInShouldWork() throws Exception {
        // Initialize the database
        gatewayRepository.saveAndFlush(gateway);

        // Get all the gatewayList where name in DEFAULT_NAME or UPDATED_NAME
        defaultGatewayShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the gatewayList where name equals to UPDATED_NAME
        defaultGatewayShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllGatewaysByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        gatewayRepository.saveAndFlush(gateway);

        // Get all the gatewayList where name is not null
        defaultGatewayShouldBeFound("name.specified=true");

        // Get all the gatewayList where name is null
        defaultGatewayShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    void getAllGatewaysByNameContainsSomething() throws Exception {
        // Initialize the database
        gatewayRepository.saveAndFlush(gateway);

        // Get all the gatewayList where name contains DEFAULT_NAME
        defaultGatewayShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the gatewayList where name contains UPDATED_NAME
        defaultGatewayShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllGatewaysByNameNotContainsSomething() throws Exception {
        // Initialize the database
        gatewayRepository.saveAndFlush(gateway);

        // Get all the gatewayList where name does not contain DEFAULT_NAME
        defaultGatewayShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the gatewayList where name does not contain UPDATED_NAME
        defaultGatewayShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllGatewaysByIp_addressIsEqualToSomething() throws Exception {
        // Initialize the database
        gatewayRepository.saveAndFlush(gateway);

        // Get all the gatewayList where ip_address equals to DEFAULT_IP_ADDRESS
        defaultGatewayShouldBeFound("ip_address.equals=" + DEFAULT_IP_ADDRESS);

        // Get all the gatewayList where ip_address equals to UPDATED_IP_ADDRESS
        defaultGatewayShouldNotBeFound("ip_address.equals=" + UPDATED_IP_ADDRESS);
    }

    @Test
    @Transactional
    void getAllGatewaysByIp_addressIsInShouldWork() throws Exception {
        // Initialize the database
        gatewayRepository.saveAndFlush(gateway);

        // Get all the gatewayList where ip_address in DEFAULT_IP_ADDRESS or UPDATED_IP_ADDRESS
        defaultGatewayShouldBeFound("ip_address.in=" + DEFAULT_IP_ADDRESS + "," + UPDATED_IP_ADDRESS);

        // Get all the gatewayList where ip_address equals to UPDATED_IP_ADDRESS
        defaultGatewayShouldNotBeFound("ip_address.in=" + UPDATED_IP_ADDRESS);
    }

    @Test
    @Transactional
    void getAllGatewaysByIp_addressIsNullOrNotNull() throws Exception {
        // Initialize the database
        gatewayRepository.saveAndFlush(gateway);

        // Get all the gatewayList where ip_address is not null
        defaultGatewayShouldBeFound("ip_address.specified=true");

        // Get all the gatewayList where ip_address is null
        defaultGatewayShouldNotBeFound("ip_address.specified=false");
    }

    @Test
    @Transactional
    void getAllGatewaysByIp_addressContainsSomething() throws Exception {
        // Initialize the database
        gatewayRepository.saveAndFlush(gateway);

        // Get all the gatewayList where ip_address contains DEFAULT_IP_ADDRESS
        defaultGatewayShouldBeFound("ip_address.contains=" + DEFAULT_IP_ADDRESS);

        // Get all the gatewayList where ip_address contains UPDATED_IP_ADDRESS
        defaultGatewayShouldNotBeFound("ip_address.contains=" + UPDATED_IP_ADDRESS);
    }

    @Test
    @Transactional
    void getAllGatewaysByIp_addressNotContainsSomething() throws Exception {
        // Initialize the database
        gatewayRepository.saveAndFlush(gateway);

        // Get all the gatewayList where ip_address does not contain DEFAULT_IP_ADDRESS
        defaultGatewayShouldNotBeFound("ip_address.doesNotContain=" + DEFAULT_IP_ADDRESS);

        // Get all the gatewayList where ip_address does not contain UPDATED_IP_ADDRESS
        defaultGatewayShouldBeFound("ip_address.doesNotContain=" + UPDATED_IP_ADDRESS);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultGatewayShouldBeFound(String filter) throws Exception {
        restGatewayMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(gateway.getId().intValue())))
            .andExpect(jsonPath("$.[*].serial_number").value(hasItem(DEFAULT_SERIAL_NUMBER)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].ip_address").value(hasItem(DEFAULT_IP_ADDRESS)));

        // Check, that the count call also returns 1
        restGatewayMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultGatewayShouldNotBeFound(String filter) throws Exception {
        restGatewayMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restGatewayMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingGateway() throws Exception {
        // Get the gateway
        restGatewayMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingGateway() throws Exception {
        // Initialize the database
        gatewayRepository.saveAndFlush(gateway);

        int databaseSizeBeforeUpdate = gatewayRepository.findAll().size();

        // Update the gateway
        Gateway updatedGateway = gatewayRepository.findById(gateway.getId()).get();
        // Disconnect from session so that the updates on updatedGateway are not directly saved in db
        em.detach(updatedGateway);
        updatedGateway.serial_number(UPDATED_SERIAL_NUMBER).name(UPDATED_NAME).ip_address(UPDATED_IP_ADDRESS);
        GatewayDTO gatewayDTO = gatewayMapper.toDto(updatedGateway);

        restGatewayMockMvc
            .perform(
                put(ENTITY_API_URL_ID, gatewayDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(gatewayDTO))
            )
            .andExpect(status().isOk());

        // Validate the Gateway in the database
        List<Gateway> gatewayList = gatewayRepository.findAll();
        assertThat(gatewayList).hasSize(databaseSizeBeforeUpdate);
        Gateway testGateway = gatewayList.get(gatewayList.size() - 1);
        assertThat(testGateway.getSerial_number()).isEqualTo(UPDATED_SERIAL_NUMBER);
        assertThat(testGateway.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testGateway.getIp_address()).isEqualTo(UPDATED_IP_ADDRESS);
    }

    @Test
    @Transactional
    void putNonExistingGateway() throws Exception {
        int databaseSizeBeforeUpdate = gatewayRepository.findAll().size();
        gateway.setId(count.incrementAndGet());

        // Create the Gateway
        GatewayDTO gatewayDTO = gatewayMapper.toDto(gateway);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restGatewayMockMvc
            .perform(
                put(ENTITY_API_URL_ID, gatewayDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(gatewayDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Gateway in the database
        List<Gateway> gatewayList = gatewayRepository.findAll();
        assertThat(gatewayList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchGateway() throws Exception {
        int databaseSizeBeforeUpdate = gatewayRepository.findAll().size();
        gateway.setId(count.incrementAndGet());

        // Create the Gateway
        GatewayDTO gatewayDTO = gatewayMapper.toDto(gateway);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restGatewayMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(gatewayDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Gateway in the database
        List<Gateway> gatewayList = gatewayRepository.findAll();
        assertThat(gatewayList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamGateway() throws Exception {
        int databaseSizeBeforeUpdate = gatewayRepository.findAll().size();
        gateway.setId(count.incrementAndGet());

        // Create the Gateway
        GatewayDTO gatewayDTO = gatewayMapper.toDto(gateway);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restGatewayMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(gatewayDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Gateway in the database
        List<Gateway> gatewayList = gatewayRepository.findAll();
        assertThat(gatewayList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateGatewayWithPatch() throws Exception {
        // Initialize the database
        gatewayRepository.saveAndFlush(gateway);

        int databaseSizeBeforeUpdate = gatewayRepository.findAll().size();

        // Update the gateway using partial update
        Gateway partialUpdatedGateway = new Gateway();
        partialUpdatedGateway.setId(gateway.getId());

        partialUpdatedGateway.ip_address(UPDATED_IP_ADDRESS);

        restGatewayMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedGateway.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedGateway))
            )
            .andExpect(status().isOk());

        // Validate the Gateway in the database
        List<Gateway> gatewayList = gatewayRepository.findAll();
        assertThat(gatewayList).hasSize(databaseSizeBeforeUpdate);
        Gateway testGateway = gatewayList.get(gatewayList.size() - 1);
        assertThat(testGateway.getSerial_number()).isEqualTo(DEFAULT_SERIAL_NUMBER);
        assertThat(testGateway.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testGateway.getIp_address()).isEqualTo(UPDATED_IP_ADDRESS);
    }

    @Test
    @Transactional
    void fullUpdateGatewayWithPatch() throws Exception {
        // Initialize the database
        gatewayRepository.saveAndFlush(gateway);

        int databaseSizeBeforeUpdate = gatewayRepository.findAll().size();

        // Update the gateway using partial update
        Gateway partialUpdatedGateway = new Gateway();
        partialUpdatedGateway.setId(gateway.getId());

        partialUpdatedGateway.serial_number(UPDATED_SERIAL_NUMBER).name(UPDATED_NAME).ip_address(UPDATED_IP_ADDRESS);

        restGatewayMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedGateway.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedGateway))
            )
            .andExpect(status().isOk());

        // Validate the Gateway in the database
        List<Gateway> gatewayList = gatewayRepository.findAll();
        assertThat(gatewayList).hasSize(databaseSizeBeforeUpdate);
        Gateway testGateway = gatewayList.get(gatewayList.size() - 1);
        assertThat(testGateway.getSerial_number()).isEqualTo(UPDATED_SERIAL_NUMBER);
        assertThat(testGateway.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testGateway.getIp_address()).isEqualTo(UPDATED_IP_ADDRESS);
    }

    @Test
    @Transactional
    void patchNonExistingGateway() throws Exception {
        int databaseSizeBeforeUpdate = gatewayRepository.findAll().size();
        gateway.setId(count.incrementAndGet());

        // Create the Gateway
        GatewayDTO gatewayDTO = gatewayMapper.toDto(gateway);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restGatewayMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, gatewayDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(gatewayDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Gateway in the database
        List<Gateway> gatewayList = gatewayRepository.findAll();
        assertThat(gatewayList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchGateway() throws Exception {
        int databaseSizeBeforeUpdate = gatewayRepository.findAll().size();
        gateway.setId(count.incrementAndGet());

        // Create the Gateway
        GatewayDTO gatewayDTO = gatewayMapper.toDto(gateway);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restGatewayMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(gatewayDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Gateway in the database
        List<Gateway> gatewayList = gatewayRepository.findAll();
        assertThat(gatewayList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamGateway() throws Exception {
        int databaseSizeBeforeUpdate = gatewayRepository.findAll().size();
        gateway.setId(count.incrementAndGet());

        // Create the Gateway
        GatewayDTO gatewayDTO = gatewayMapper.toDto(gateway);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restGatewayMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(gatewayDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Gateway in the database
        List<Gateway> gatewayList = gatewayRepository.findAll();
        assertThat(gatewayList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteGateway() throws Exception {
        // Initialize the database
        gatewayRepository.saveAndFlush(gateway);

        int databaseSizeBeforeDelete = gatewayRepository.findAll().size();

        // Delete the gateway
        restGatewayMockMvc
            .perform(delete(ENTITY_API_URL_ID, gateway.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Gateway> gatewayList = gatewayRepository.findAll();
        assertThat(gatewayList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
