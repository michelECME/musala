package com.musala.app.web.rest;

import com.musala.app.repository.PeripheralRepository;
import com.musala.app.service.PeripheralQueryService;
import com.musala.app.service.PeripheralService;
import com.musala.app.service.criteria.PeripheralCriteria;
import com.musala.app.service.dto.PeripheralDTO;
import com.musala.app.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.musala.app.domain.Peripheral}.
 */
@RestController
@RequestMapping("/api")
public class PeripheralResource {

    private final Logger log = LoggerFactory.getLogger(PeripheralResource.class);

    private static final String ENTITY_NAME = "peripheral";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PeripheralService peripheralService;

    private final PeripheralRepository peripheralRepository;

    private final PeripheralQueryService peripheralQueryService;

    public PeripheralResource(
        PeripheralService peripheralService,
        PeripheralRepository peripheralRepository,
        PeripheralQueryService peripheralQueryService
    ) {
        this.peripheralService = peripheralService;
        this.peripheralRepository = peripheralRepository;
        this.peripheralQueryService = peripheralQueryService;
    }

    /**
     * {@code POST  /peripherals} : Create a new peripheral.
     *
     * @param peripheralDTO the peripheralDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new peripheralDTO, or with status {@code 400 (Bad Request)} if the peripheral has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/peripherals")
    public ResponseEntity<PeripheralDTO> createPeripheral(@Valid @RequestBody PeripheralDTO peripheralDTO) throws URISyntaxException {
        log.debug("REST request to save Peripheral : {}", peripheralDTO);
        if (peripheralDTO.getId() != null) {
            throw new BadRequestAlertException("A new peripheral cannot already have an ID", ENTITY_NAME, "idexists");
        }
        if (findAllByGatewayId(peripheralDTO.getGateway().getId()).getBody().size() == 10) {
            throw new BadRequestAlertException("There is to many peripherals in this gateway ", ENTITY_NAME, "tomanyperipherals");
        }
        PeripheralDTO result = peripheralService.save(peripheralDTO);
        return ResponseEntity
            .created(new URI("/api/peripherals/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /peripherals/:id} : Updates an existing peripheral.
     *
     * @param id the id of the peripheralDTO to save.
     * @param peripheralDTO the peripheralDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated peripheralDTO,
     * or with status {@code 400 (Bad Request)} if the peripheralDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the peripheralDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/peripherals/{id}")
    public ResponseEntity<PeripheralDTO> updatePeripheral(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody PeripheralDTO peripheralDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Peripheral : {}, {}", id, peripheralDTO);
        if (peripheralDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, peripheralDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!peripheralRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }
        if (findAllByGatewayId(peripheralDTO.getGateway().getId()).getBody().size() == 10) {
            throw new BadRequestAlertException("There is to many peripherals in this gateway ", ENTITY_NAME, "tomanyperipherals");
        }

        PeripheralDTO result = peripheralService.update(peripheralDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, peripheralDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /peripherals/:id} : Partial updates given fields of an existing peripheral, field will ignore if it is null
     *
     * @param id the id of the peripheralDTO to save.
     * @param peripheralDTO the peripheralDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated peripheralDTO,
     * or with status {@code 400 (Bad Request)} if the peripheralDTO is not valid,
     * or with status {@code 404 (Not Found)} if the peripheralDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the peripheralDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/peripherals/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<PeripheralDTO> partialUpdatePeripheral(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody PeripheralDTO peripheralDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Peripheral partially : {}, {}", id, peripheralDTO);
        if (peripheralDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, peripheralDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!peripheralRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }
        if (findAllByGatewayId(peripheralDTO.getGateway().getId()).getBody().size() == 10) {
            throw new BadRequestAlertException("There is to many peripherals in this gateway ", ENTITY_NAME, "tomanyperipherals");
        }

        Optional<PeripheralDTO> result = peripheralService.partialUpdate(peripheralDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, peripheralDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /peripherals} : get all the peripherals.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of peripherals in body.
     */
    @GetMapping("/peripherals")
    public ResponseEntity<List<PeripheralDTO>> getAllPeripherals(
        PeripheralCriteria criteria,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get Peripherals by criteria: {}", criteria);
        Page<PeripheralDTO> page = peripheralQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /peripherals/count} : count all the peripherals.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/peripherals/count")
    public ResponseEntity<Long> countPeripherals(PeripheralCriteria criteria) {
        log.debug("REST request to count Peripherals by criteria: {}", criteria);
        return ResponseEntity.ok().body(peripheralQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /peripherals/:id} : get the "id" peripheral.
     *
     * @param id the id of the peripheralDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the peripheralDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/peripherals/{id}")
    public ResponseEntity<PeripheralDTO> getPeripheral(@PathVariable Long id) {
        log.debug("REST request to get Peripheral : {}", id);
        Optional<PeripheralDTO> peripheralDTO = peripheralService.findOne(id);
        return ResponseUtil.wrapOrNotFound(peripheralDTO);
    }

    /**
     * {@code GET  /gateways/:id/peripherals} : get the "id" Gateways.
     *
     * @param id the id of the peripheralDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the peripheralDTO}.
     */
    @GetMapping("/gateways/{id}/peripherals")
    public ResponseEntity<List<PeripheralDTO>> findAllByGatewayId(@PathVariable Long id) {
        log.debug("REST request to get Peripheral : {}", id);

        return ResponseEntity.ok().body(peripheralService.findAllByGatewayId(id));
    }

    /**
     * {@code DELETE  /peripherals/:id} : delete the "id" peripheral.
     *
     * @param id the id of the peripheralDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/peripherals/{id}")
    public ResponseEntity<Void> deletePeripheral(@PathVariable Long id) {
        log.debug("REST request to delete Peripheral : {}", id);
        peripheralService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
