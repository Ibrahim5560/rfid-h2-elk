package com.isoft.rfid.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.isoft.rfid.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class GantryDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(GantryDTO.class);
        GantryDTO gantryDTO1 = new GantryDTO();
        gantryDTO1.setId(1L);
        GantryDTO gantryDTO2 = new GantryDTO();
        assertThat(gantryDTO1).isNotEqualTo(gantryDTO2);
        gantryDTO2.setId(gantryDTO1.getId());
        assertThat(gantryDTO1).isEqualTo(gantryDTO2);
        gantryDTO2.setId(2L);
        assertThat(gantryDTO1).isNotEqualTo(gantryDTO2);
        gantryDTO1.setId(null);
        assertThat(gantryDTO1).isNotEqualTo(gantryDTO2);
    }
}
