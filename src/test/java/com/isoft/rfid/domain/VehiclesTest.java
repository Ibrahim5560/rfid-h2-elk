package com.isoft.rfid.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.isoft.rfid.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class VehiclesTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Vehicles.class);
        Vehicles vehicles1 = new Vehicles();
        vehicles1.setId(1L);
        Vehicles vehicles2 = new Vehicles();
        vehicles2.setId(vehicles1.getId());
        assertThat(vehicles1).isEqualTo(vehicles2);
        vehicles2.setId(2L);
        assertThat(vehicles1).isNotEqualTo(vehicles2);
        vehicles1.setId(null);
        assertThat(vehicles1).isNotEqualTo(vehicles2);
    }
}
