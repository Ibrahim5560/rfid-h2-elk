package com.isoft.rfid.repository;

import com.isoft.rfid.domain.Events;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Events entity.
 */
@SuppressWarnings("unused")
@Repository
public interface EventsRepository extends JpaRepository<Events, Long>, JpaSpecificationExecutor<Events> {}
