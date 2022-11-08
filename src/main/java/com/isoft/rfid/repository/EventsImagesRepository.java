package com.isoft.rfid.repository;

import com.isoft.rfid.domain.EventsImages;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the EventsImages entity.
 */
@SuppressWarnings("unused")
@Repository
public interface EventsImagesRepository extends JpaRepository<EventsImages, Long>, JpaSpecificationExecutor<EventsImages> {}
