package com.isoft.rfid.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class GantryMapperTest {

    private GantryMapper gantryMapper;

    @BeforeEach
    public void setUp() {
        gantryMapper = new GantryMapperImpl();
    }
}
