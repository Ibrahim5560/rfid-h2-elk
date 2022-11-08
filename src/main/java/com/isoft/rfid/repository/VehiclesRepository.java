package com.isoft.rfid.repository;

import com.isoft.rfid.domain.Vehicles;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Vehicles entity.
 */
@SuppressWarnings("unused")
@Repository
public interface VehiclesRepository extends JpaRepository<Vehicles, Long>, JpaSpecificationExecutor<Vehicles> {}
