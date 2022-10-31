package com.musala.app.service;

import com.musala.app.domain.*; // for static metamodels
import com.musala.app.domain.Gateway;
import com.musala.app.repository.GatewayRepository;
import com.musala.app.service.criteria.GatewayCriteria;
import com.musala.app.service.dto.GatewayDTO;
import com.musala.app.service.mapper.GatewayMapper;
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
 * Service for executing complex queries for {@link Gateway} entities in the database.
 * The main input is a {@link GatewayCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link GatewayDTO} or a {@link Page} of {@link GatewayDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class GatewayQueryService extends QueryService<Gateway> {

    private final Logger log = LoggerFactory.getLogger(GatewayQueryService.class);

    private final GatewayRepository gatewayRepository;

    private final GatewayMapper gatewayMapper;

    public GatewayQueryService(GatewayRepository gatewayRepository, GatewayMapper gatewayMapper) {
        this.gatewayRepository = gatewayRepository;
        this.gatewayMapper = gatewayMapper;
    }

    /**
     * Return a {@link List} of {@link GatewayDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<GatewayDTO> findByCriteria(GatewayCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Gateway> specification = createSpecification(criteria);
        return gatewayMapper.toDto(gatewayRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link GatewayDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<GatewayDTO> findByCriteria(GatewayCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Gateway> specification = createSpecification(criteria);
        return gatewayRepository.findAll(specification, page).map(gatewayMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(GatewayCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Gateway> specification = createSpecification(criteria);
        return gatewayRepository.count(specification);
    }

    /**
     * Function to convert {@link GatewayCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Gateway> createSpecification(GatewayCriteria criteria) {
        Specification<Gateway> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Gateway_.id));
            }
            if (criteria.getSerial_number() != null) {
                specification = specification.and(buildStringSpecification(criteria.getSerial_number(), Gateway_.serial_number));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), Gateway_.name));
            }
            if (criteria.getIp_address() != null) {
                specification = specification.and(buildStringSpecification(criteria.getIp_address(), Gateway_.ip_address));
            }
        }
        return specification;
    }
}
