package com.musala.app.service.criteria;

import com.musala.app.domain.enumeration.Status;
import java.io.Serializable;
import java.util.Objects;
import org.springdoc.api.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.musala.app.domain.Peripheral} entity. This class is used
 * in {@link com.musala.app.web.rest.PeripheralResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /peripherals?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PeripheralCriteria implements Serializable, Criteria {

    /**
     * Class for filtering Status
     */
    public static class StatusFilter extends Filter<Status> {

        public StatusFilter() {}

        public StatusFilter(StatusFilter filter) {
            super(filter);
        }

        @Override
        public StatusFilter copy() {
            return new StatusFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private LongFilter uid;

    private StringFilter vendor;

    private LocalDateFilter date_created;

    private StatusFilter status;

    private LongFilter gatewayId;

    private Boolean distinct;

    public PeripheralCriteria() {}

    public PeripheralCriteria(PeripheralCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.uid = other.uid == null ? null : other.uid.copy();
        this.vendor = other.vendor == null ? null : other.vendor.copy();
        this.date_created = other.date_created == null ? null : other.date_created.copy();
        this.status = other.status == null ? null : other.status.copy();
        this.gatewayId = other.gatewayId == null ? null : other.gatewayId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public PeripheralCriteria copy() {
        return new PeripheralCriteria(this);
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

    public LongFilter getUid() {
        return uid;
    }

    public LongFilter uid() {
        if (uid == null) {
            uid = new LongFilter();
        }
        return uid;
    }

    public void setUid(LongFilter uid) {
        this.uid = uid;
    }

    public StringFilter getVendor() {
        return vendor;
    }

    public StringFilter vendor() {
        if (vendor == null) {
            vendor = new StringFilter();
        }
        return vendor;
    }

    public void setVendor(StringFilter vendor) {
        this.vendor = vendor;
    }

    public LocalDateFilter getDate_created() {
        return date_created;
    }

    public LocalDateFilter date_created() {
        if (date_created == null) {
            date_created = new LocalDateFilter();
        }
        return date_created;
    }

    public void setDate_created(LocalDateFilter date_created) {
        this.date_created = date_created;
    }

    public StatusFilter getStatus() {
        return status;
    }

    public StatusFilter status() {
        if (status == null) {
            status = new StatusFilter();
        }
        return status;
    }

    public void setStatus(StatusFilter status) {
        this.status = status;
    }

    public LongFilter getGatewayId() {
        return gatewayId;
    }

    public LongFilter gatewayId() {
        if (gatewayId == null) {
            gatewayId = new LongFilter();
        }
        return gatewayId;
    }

    public void setGatewayId(LongFilter gatewayId) {
        this.gatewayId = gatewayId;
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
        final PeripheralCriteria that = (PeripheralCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(uid, that.uid) &&
            Objects.equals(vendor, that.vendor) &&
            Objects.equals(date_created, that.date_created) &&
            Objects.equals(status, that.status) &&
            Objects.equals(gatewayId, that.gatewayId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, uid, vendor, date_created, status, gatewayId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PeripheralCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (uid != null ? "uid=" + uid + ", " : "") +
            (vendor != null ? "vendor=" + vendor + ", " : "") +
            (date_created != null ? "date_created=" + date_created + ", " : "") +
            (status != null ? "status=" + status + ", " : "") +
            (gatewayId != null ? "gatewayId=" + gatewayId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
