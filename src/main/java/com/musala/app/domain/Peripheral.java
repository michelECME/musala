package com.musala.app.domain;

import com.musala.app.domain.enumeration.Status;
import java.io.Serializable;
import java.time.LocalDate;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Peripheral.
 */
@Entity
@Table(name = "peripheral")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Peripheral implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "uid", nullable = false, unique = true)
    private Long uid;

    @NotNull
    @Column(name = "vendor", nullable = false)
    private String vendor;

    @NotNull
    @Column(name = "date_created", nullable = false)
    private LocalDate date_created;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private Status status;

    @ManyToOne
    private Gateway gateway;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Peripheral id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUid() {
        return this.uid;
    }

    public Peripheral uid(Long uid) {
        this.setUid(uid);
        return this;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public String getVendor() {
        return this.vendor;
    }

    public Peripheral vendor(String vendor) {
        this.setVendor(vendor);
        return this;
    }

    public void setVendor(String vendor) {
        this.vendor = vendor;
    }

    public LocalDate getDate_created() {
        return this.date_created;
    }

    public Peripheral date_created(LocalDate date_created) {
        this.setDate_created(date_created);
        return this;
    }

    public void setDate_created(LocalDate date_created) {
        this.date_created = date_created;
    }

    public Status getStatus() {
        return this.status;
    }

    public Peripheral status(Status status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Gateway getGateway() {
        return this.gateway;
    }

    public void setGateway(Gateway gateway) {
        this.gateway = gateway;
    }

    public Peripheral gateway(Gateway gateway) {
        this.setGateway(gateway);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Peripheral)) {
            return false;
        }
        return id != null && id.equals(((Peripheral) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Peripheral{" +
            "id=" + getId() +
            ", uid=" + getUid() +
            ", vendor='" + getVendor() + "'" +
            ", date_created='" + getDate_created() + "'" +
            ", status='" + getStatus() + "'" +
            "}";
    }
}
