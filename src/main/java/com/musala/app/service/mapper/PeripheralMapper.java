package com.musala.app.service.mapper;

import com.musala.app.domain.Gateway;
import com.musala.app.domain.Peripheral;
import com.musala.app.service.dto.GatewayDTO;
import com.musala.app.service.dto.PeripheralDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Peripheral} and its DTO {@link PeripheralDTO}.
 */
@Mapper(componentModel = "spring")
public interface PeripheralMapper extends EntityMapper<PeripheralDTO, Peripheral> {
    @Mapping(target = "gateway", source = "gateway", qualifiedByName = "gatewaySerial_number")
    PeripheralDTO toDto(Peripheral s);

    @Named("gatewaySerial_number")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "serial_number", source = "serial_number")
    GatewayDTO toDtoGatewaySerial_number(Gateway gateway);
}
