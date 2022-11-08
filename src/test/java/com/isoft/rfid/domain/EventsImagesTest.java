package com.isoft.rfid.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.isoft.rfid.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class EventsImagesTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(EventsImages.class);
        EventsImages eventsImages1 = new EventsImages();
        eventsImages1.setId(1L);
        EventsImages eventsImages2 = new EventsImages();
        eventsImages2.setId(eventsImages1.getId());
        assertThat(eventsImages1).isEqualTo(eventsImages2);
        eventsImages2.setId(2L);
        assertThat(eventsImages1).isNotEqualTo(eventsImages2);
        eventsImages1.setId(null);
        assertThat(eventsImages1).isNotEqualTo(eventsImages2);
    }
}
