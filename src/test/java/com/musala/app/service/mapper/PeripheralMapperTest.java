package com.musala.app.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PeripheralMapperTest {

    private PeripheralMapper peripheralMapper;

    @BeforeEach
    public void setUp() {
        peripheralMapper = new PeripheralMapperImpl();
    }
}
