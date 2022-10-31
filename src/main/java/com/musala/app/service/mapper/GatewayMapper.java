package com.musala.app.service.mapper;

import com.musala.app.domain.Gateway;
import com.musala.app.service.dto.GatewayDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Gateway} and its DTO {@link GatewayDTO}.
 */
@Mapper(componentModel = "spring")
public interface GatewayMapper extends EntityMapper<GatewayDTO, Gateway> {}
