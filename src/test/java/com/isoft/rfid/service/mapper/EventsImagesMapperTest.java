package com.isoft.rfid.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class EventsImagesMapperTest {

    private EventsImagesMapper eventsImagesMapper;

    @BeforeEach
    public void setUp() {
        eventsImagesMapper = new EventsImagesMapperImpl();
    }
}
