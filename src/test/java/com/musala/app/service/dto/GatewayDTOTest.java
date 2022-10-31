package com.musala.app.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.musala.app.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class GatewayDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(GatewayDTO.class);
        GatewayDTO gatewayDTO1 = new GatewayDTO();
        gatewayDTO1.setId(1L);
        GatewayDTO gatewayDTO2 = new GatewayDTO();
        assertThat(gatewayDTO1).isNotEqualTo(gatewayDTO2);
        gatewayDTO2.setId(gatewayDTO1.getId());
        assertThat(gatewayDTO1).isEqualTo(gatewayDTO2);
        gatewayDTO2.setId(2L);
        assertThat(gatewayDTO1).isNotEqualTo(gatewayDTO2);
        gatewayDTO1.setId(null);
        assertThat(gatewayDTO1).isNotEqualTo(gatewayDTO2);
    }
}
