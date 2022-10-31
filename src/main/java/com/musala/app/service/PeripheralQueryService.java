package com.musala.app.service;

import com.musala.app.domain.*; // for static metamodels
import com.musala.app.domain.Peripheral;
import com.musala.app.repository.PeripheralRepository;
import com.musala.app.service.criteria.PeripheralCriteria;
import com.musala.app.service.dto.PeripheralDTO;
import com.musala.app.service.mapper.PeripheralMapper;
import java.util.List;
import javax.persistence.criteria.JoinType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link Peripheral} entities in the database.
 * The main input is a {@link PeripheralCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link PeripheralDTO} or a {@link Page} of {@link PeripheralDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class PeripheralQueryService extends QueryService<Peripheral> {

    private final Logger log = LoggerFactory.getLogger(PeripheralQueryService.class);

    private final PeripheralRepository peripheralRepository;

    private final PeripheralMapper peripheralMapper;

    public PeripheralQueryService(PeripheralRepository peripheralRepository, PeripheralMapper peripheralMapper) {
        this.peripheralRepository = peripheralRepository;
        this.peripheralMapper = peripheralMapper;
    }

    /**
     * Return a {@link List} of {@link PeripheralDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<PeripheralDTO> findByCriteria(PeripheralCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Peripheral> specification = createSpecification(criteria);
        return peripheralMapper.toDto(peripheralRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link PeripheralDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<PeripheralDTO> findByCriteria(PeripheralCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Peripheral> specification = createSpecification(criteria);
        return peripheralRepository.findAll(specification, page).map(peripheralMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(PeripheralCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Peripheral> specification = createSpecification(criteria);
        return peripheralRepository.count(specification);
    }

    /**
     * Function to convert {@link PeripheralCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Peripheral> createSpecification(PeripheralCriteria criteria) {
        Specification<Peripheral> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Peripheral_.id));
            }
            if (criteria.getUid() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getUid(), Peripheral_.uid));
            }
            if (criteria.getVendor() != null) {
                specification = specification.and(buildStringSpecification(criteria.getVendor(), Peripheral_.vendor));
            }
            if (criteria.getDate_created() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDate_created(), Peripheral_.date_created));
            }
            if (criteria.getStatus() != null) {
                specification = specification.and(buildSpecification(criteria.getStatus(), Peripheral_.status));
            }
            if (criteria.getGatewayId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getGatewayId(), root -> root.join(Peripheral_.gateway, JoinType.LEFT).get(Gateway_.id))
                    );
            }
        }
        return specification;
    }
}
