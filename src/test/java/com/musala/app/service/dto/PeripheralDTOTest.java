package com.musala.app.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.musala.app.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PeripheralDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(PeripheralDTO.class);
        PeripheralDTO peripheralDTO1 = new PeripheralDTO();
        peripheralDTO1.setId(1L);
        PeripheralDTO peripheralDTO2 = new PeripheralDTO();
        assertThat(peripheralDTO1).isNotEqualTo(peripheralDTO2);
        peripheralDTO2.setId(peripheralDTO1.getId());
        assertThat(peripheralDTO1).isEqualTo(peripheralDTO2);
        peripheralDTO2.setId(2L);
        assertThat(peripheralDTO1).isNotEqualTo(peripheralDTO2);
        peripheralDTO1.setId(null);
        assertThat(peripheralDTO1).isNotEqualTo(peripheralDTO2);
    }
}
