package com.musala.app.domain;

import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Gateway.
 */
@Entity
@Table(name = "gateway")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Gateway implements Serializable {

    private static final long serialVersionUID = 1L;
    private static final String IPV4_PATTERN_SHORTEST = "^((25[0-5]|(2[0-4]|1[0-9]|[1-9]|)[0-9])(\\.(?!$)|$)){4}$";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "serial_number", nullable = false, unique = true)
    private String serial_number;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @NotNull
    @Pattern(regexp = IPV4_PATTERN_SHORTEST)
    @Column(name = "ip_address", nullable = false)
    private String ip_address;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Gateway id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSerial_number() {
        return this.serial_number;
    }

    public Gateway serial_number(String serial_number) {
        this.setSerial_number(serial_number);
        return this;
    }

    public void setSerial_number(String serial_number) {
        this.serial_number = serial_number;
    }

    public String getName() {
        return this.name;
    }

    public Gateway name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIp_address() {
        return this.ip_address;
    }

    public Gateway ip_address(String ip_address) {
        this.setIp_address(ip_address);
        return this;
    }

    public void setIp_address(String ip_address) {
        this.ip_address = ip_address;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Gateway)) {
            return false;
        }
        return id != null && id.equals(((Gateway) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Gateway{" +
            "id=" + getId() +
            ", serial_number='" + getSerial_number() + "'" +
            ", name='" + getName() + "'" +
            ", ip_address='" + getIp_address() + "'" +
            "}";
    }
}
