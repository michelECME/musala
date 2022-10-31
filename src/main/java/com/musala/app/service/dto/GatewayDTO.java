package com.musala.app.service.dto;

import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.musala.app.domain.Gateway} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class GatewayDTO implements Serializable {

    private Long id;

    @NotNull
    private String serial_number;

    @NotNull
    private String name;

    private static final String IPV4_PATTERN_SHORTEST = "^((25[0-5]|(2[0-4]|1[0-9]|[1-9]|)[0-9])(\\.(?!$)|$)){4}$";

    @NotNull
    @Pattern(regexp = IPV4_PATTERN_SHORTEST)
    private String ip_address;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSerial_number() {
        return serial_number;
    }

    public void setSerial_number(String serial_number) {
        this.serial_number = serial_number;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIp_address() {
        return ip_address;
    }

    public void setIp_address(String ip_address) {
        this.ip_address = ip_address;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof GatewayDTO)) {
            return false;
        }

        GatewayDTO gatewayDTO = (GatewayDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, gatewayDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "GatewayDTO{" +
            "id=" + getId() +
            ", serial_number='" + getSerial_number() + "'" +
            ", name='" + getName() + "'" +
            ", ip_address='" + getIp_address() + "'" +
            "}";
    }
}
