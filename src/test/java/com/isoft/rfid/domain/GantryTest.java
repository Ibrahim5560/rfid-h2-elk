package com.isoft.rfid.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.isoft.rfid.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class GantryTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Gantry.class);
        Gantry gantry1 = new Gantry();
        gantry1.setId(1L);
        Gantry gantry2 = new Gantry();
        gantry2.setId(gantry1.getId());
        assertThat(gantry1).isEqualTo(gantry2);
        gantry2.setId(2L);
        assertThat(gantry1).isNotEqualTo(gantry2);
        gantry1.setId(null);
        assertThat(gantry1).isNotEqualTo(gantry2);
    }
}
