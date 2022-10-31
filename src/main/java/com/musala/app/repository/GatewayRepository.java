package com.musala.app.repository;

import com.musala.app.domain.Gateway;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Gateway entity.
 */
@SuppressWarnings("unused")
@Repository
public interface GatewayRepository extends JpaRepository<Gateway, Long>, JpaSpecificationExecutor<Gateway> {}
