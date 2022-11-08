package com.isoft.rfid.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.isoft.rfid.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class VehiclesDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(VehiclesDTO.class);
        VehiclesDTO vehiclesDTO1 = new VehiclesDTO();
        vehiclesDTO1.setId(1L);
        VehiclesDTO vehiclesDTO2 = new VehiclesDTO();
        assertThat(vehiclesDTO1).isNotEqualTo(vehiclesDTO2);
        vehiclesDTO2.setId(vehiclesDTO1.getId());
        assertThat(vehiclesDTO1).isEqualTo(vehiclesDTO2);
        vehiclesDTO2.setId(2L);
        assertThat(vehiclesDTO1).isNotEqualTo(vehiclesDTO2);
        vehiclesDTO1.setId(null);
        assertThat(vehiclesDTO1).isNotEqualTo(vehiclesDTO2);
    }
}
