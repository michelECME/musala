package com.musala.app.service.dto;

import com.musala.app.domain.enumeration.Status;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.musala.app.domain.Peripheral} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PeripheralDTO implements Serializable {

    private Long id;

    @NotNull
    private Long uid;

    @NotNull
    private String vendor;

    @NotNull
    private LocalDate date_created;

    @NotNull
    private Status status;

    private GatewayDTO gateway;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public String getVendor() {
        return vendor;
    }

    public void setVendor(String vendor) {
        this.vendor = vendor;
    }

    public LocalDate getDate_created() {
        return date_created;
    }

    public void setDate_created(LocalDate date_created) {
        this.date_created = date_created;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public GatewayDTO getGateway() {
        return gateway;
    }

    public void setGateway(GatewayDTO gateway) {
        this.gateway = gateway;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PeripheralDTO)) {
            return false;
        }

        PeripheralDTO peripheralDTO = (PeripheralDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, peripheralDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PeripheralDTO{" +
            "id=" + getId() +
            ", uid=" + getUid() +
            ", vendor='" + getVendor() + "'" +
            ", date_created='" + getDate_created() + "'" +
            ", status='" + getStatus() + "'" +
            ", gateway=" + getGateway() +
            "}";
    }
}
