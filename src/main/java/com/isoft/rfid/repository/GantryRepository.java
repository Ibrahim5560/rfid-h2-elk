package com.isoft.rfid.repository;

import com.isoft.rfid.domain.Gantry;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Gantry entity.
 */
@SuppressWarnings("unused")
@Repository
public interface GantryRepository extends JpaRepository<Gantry, Long>, JpaSpecificationExecutor<Gantry> {}
