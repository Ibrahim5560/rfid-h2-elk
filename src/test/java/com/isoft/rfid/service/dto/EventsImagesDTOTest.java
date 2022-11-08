package com.isoft.rfid.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.isoft.rfid.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class EventsImagesDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(EventsImagesDTO.class);
        EventsImagesDTO eventsImagesDTO1 = new EventsImagesDTO();
        eventsImagesDTO1.setId(1L);
        EventsImagesDTO eventsImagesDTO2 = new EventsImagesDTO();
        assertThat(eventsImagesDTO1).isNotEqualTo(eventsImagesDTO2);
        eventsImagesDTO2.setId(eventsImagesDTO1.getId());
        assertThat(eventsImagesDTO1).isEqualTo(eventsImagesDTO2);
        eventsImagesDTO2.setId(2L);
        assertThat(eventsImagesDTO1).isNotEqualTo(eventsImagesDTO2);
        eventsImagesDTO1.setId(null);
        assertThat(eventsImagesDTO1).isNotEqualTo(eventsImagesDTO2);
    }
}
