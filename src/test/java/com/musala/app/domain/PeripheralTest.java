package com.musala.app.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.musala.app.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PeripheralTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Peripheral.class);
        Peripheral peripheral1 = new Peripheral();
        peripheral1.setId(1L);
        Peripheral peripheral2 = new Peripheral();
        peripheral2.setId(peripheral1.getId());
        assertThat(peripheral1).isEqualTo(peripheral2);
        peripheral2.setId(2L);
        assertThat(peripheral1).isNotEqualTo(peripheral2);
        peripheral1.setId(null);
        assertThat(peripheral1).isNotEqualTo(peripheral2);
    }
}
