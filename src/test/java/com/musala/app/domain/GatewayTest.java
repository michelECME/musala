package com.musala.app.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.musala.app.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class GatewayTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Gateway.class);
        Gateway gateway1 = new Gateway();
        gateway1.setId(1L);
        Gateway gateway2 = new Gateway();
        gateway2.setId(gateway1.getId());
        assertThat(gateway1).isEqualTo(gateway2);
        gateway2.setId(2L);
        assertThat(gateway1).isNotEqualTo(gateway2);
        gateway1.setId(null);
        assertThat(gateway1).isNotEqualTo(gateway2);
    }
}
