package com.isoft.rfid.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class VehiclesMapperTest {

    private VehiclesMapper vehiclesMapper;

    @BeforeEach
    public void setUp() {
        vehiclesMapper = new VehiclesMapperImpl();
    }
}
