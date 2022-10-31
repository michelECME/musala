package com.musala.app.repository;

import com.musala.app.domain.Peripheral;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Peripheral entity.
 */
@Repository
public interface PeripheralRepository extends JpaRepository<Peripheral, Long>, JpaSpecificationExecutor<Peripheral> {
    default Optional<Peripheral> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    List<Peripheral> findAllByGatewayId(Long id);

    default List<Peripheral> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<Peripheral> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select distinct peripheral from Peripheral peripheral left join fetch peripheral.gateway",
        countQuery = "select count(distinct peripheral) from Peripheral peripheral"
    )
    Page<Peripheral> findAllWithToOneRelationships(Pageable pageable);

    @Query("select distinct peripheral from Peripheral peripheral left join fetch peripheral.gateway")
    List<Peripheral> findAllWithToOneRelationships();

    @Query("select peripheral from Peripheral peripheral left join fetch peripheral.gateway where peripheral.id =:id")
    Optional<Peripheral> findOneWithToOneRelationships(@Param("id") Long id);
}
