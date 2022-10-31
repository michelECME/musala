package com.musala.app.service;

import com.musala.app.domain.Peripheral;
import com.musala.app.repository.PeripheralRepository;
import com.musala.app.service.dto.PeripheralDTO;
import com.musala.app.service.mapper.PeripheralMapper;
import java.util.*;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Peripheral}.
 */
@Service
@Transactional
public class PeripheralService {

    private final Logger log = LoggerFactory.getLogger(PeripheralService.class);

    private final PeripheralRepository peripheralRepository;

    private final PeripheralMapper peripheralMapper;

    public PeripheralService(PeripheralRepository peripheralRepository, PeripheralMapper peripheralMapper) {
        this.peripheralRepository = peripheralRepository;
        this.peripheralMapper = peripheralMapper;
    }

    /**
     * Save a peripheral.
     *
     * @param Gateway's id the entity to find its peripheral.
     * @return the persisted entity.
     */
    public List<PeripheralDTO> findAllByGatewayId(Long id) {
        log.debug("Request to get all Peripherals by the Gateway id:" + id);
        return peripheralMapper.toDto(peripheralRepository.findAllByGatewayId(id));
    }

    /**
     * Save a peripheral.
     *
     * @param peripheralDTO the entity to save.
     * @return the persisted entity.
     */
    public PeripheralDTO save(PeripheralDTO peripheralDTO) {
        log.debug("Request to save Peripheral : {}", peripheralDTO);
        Peripheral peripheral = peripheralMapper.toEntity(peripheralDTO);
        peripheral = peripheralRepository.save(peripheral);
        return peripheralMapper.toDto(peripheral);
    }

    /**
     * Update a peripheral.
     *
     * @param peripheralDTO the entity to save.
     * @return the persisted entity.
     */
    public PeripheralDTO update(PeripheralDTO peripheralDTO) {
        log.debug("Request to update Peripheral : {}", peripheralDTO);
        Peripheral peripheral = peripheralMapper.toEntity(peripheralDTO);
        peripheral = peripheralRepository.save(peripheral);
        return peripheralMapper.toDto(peripheral);
    }

    /**
     * Partially update a peripheral.
     *
     * @param peripheralDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<PeripheralDTO> partialUpdate(PeripheralDTO peripheralDTO) {
        log.debug("Request to partially update Peripheral : {}", peripheralDTO);

        return peripheralRepository
            .findById(peripheralDTO.getId())
            .map(existingPeripheral -> {
                peripheralMapper.partialUpdate(existingPeripheral, peripheralDTO);

                return existingPeripheral;
            })
            .map(peripheralRepository::save)
            .map(peripheralMapper::toDto);
    }

    /**
     * Get all the peripherals.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<PeripheralDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Peripherals");
        return peripheralRepository.findAll(pageable).map(peripheralMapper::toDto);
    }

    /**
     * Get all the peripherals with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<PeripheralDTO> findAllWithEagerRelationships(Pageable pageable) {
        return peripheralRepository.findAllWithEagerRelationships(pageable).map(peripheralMapper::toDto);
    }

    /**
     * Get one peripheral by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<PeripheralDTO> findOne(Long id) {
        log.debug("Request to get Peripheral : {}", id);
        return peripheralRepository.findOneWithEagerRelationships(id).map(peripheralMapper::toDto);
    }

    /**
     * Delete the peripheral by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Peripheral : {}", id);
        peripheralRepository.deleteById(id);
    }
}
