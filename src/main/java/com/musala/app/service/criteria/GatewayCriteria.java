package com.musala.app.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.api.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.musala.app.domain.Gateway} entity. This class is used
 * in {@link com.musala.app.web.rest.GatewayResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /gateways?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class GatewayCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter serial_number;

    private StringFilter name;

    private StringFilter ip_address;

    private Boolean distinct;

    public GatewayCriteria() {}

    public GatewayCriteria(GatewayCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.serial_number = other.serial_number == null ? null : other.serial_number.copy();
        this.name = other.name == null ? null : other.name.copy();
        this.ip_address = other.ip_address == null ? null : other.ip_address.copy();
        this.distinct = other.distinct;
    }

    @Override
    public GatewayCriteria copy() {
        return new GatewayCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public LongFilter id() {
        if (id == null) {
            id = new LongFilter();
        }
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getSerial_number() {
        return serial_number;
    }

    public StringFilter serial_number() {
        if (serial_number == null) {
            serial_number = new StringFilter();
        }
        return serial_number;
    }

    public void setSerial_number(StringFilter serial_number) {
        this.serial_number = serial_number;
    }

    public StringFilter getName() {
        return name;
    }

    public StringFilter name() {
        if (name == null) {
            name = new StringFilter();
        }
        return name;
    }

    public void setName(StringFilter name) {
        this.name = name;
    }

    public StringFilter getIp_address() {
        return ip_address;
    }

    public StringFilter ip_address() {
        if (ip_address == null) {
            ip_address = new StringFilter();
        }
        return ip_address;
    }

    public void setIp_address(StringFilter ip_address) {
        this.ip_address = ip_address;
    }

    public Boolean getDistinct() {
        return distinct;
    }

    public void setDistinct(Boolean distinct) {
        this.distinct = distinct;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final GatewayCriteria that = (GatewayCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(serial_number, that.serial_number) &&
            Objects.equals(name, that.name) &&
            Objects.equals(ip_address, that.ip_address) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, serial_number, name, ip_address, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "GatewayCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (serial_number != null ? "serial_number=" + serial_number + ", " : "") +
            (name != null ? "name=" + name + ", " : "") +
            (ip_address != null ? "ip_address=" + ip_address + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
