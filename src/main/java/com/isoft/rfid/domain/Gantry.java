package com.isoft.rfid.domain;

import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * Gantry (gantry) entity.\n@author Ibrahim Mohamed.
 */
@Entity
@Table(name = "gantry")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "gantry")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Gantry implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "guid", nullable = false)
    private String guid;

    @NotNull
    @Column(name = "name_en", nullable = false)
    private String nameEn;

    @NotNull
    @Column(name = "name_ar", nullable = false)
    private String nameAr;

    @Column(name = "status")
    private Integer status;

    @Column(name = "code")
    private String code;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Gantry id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getGuid() {
        return this.guid;
    }

    public Gantry guid(String guid) {
        this.setGuid(guid);
        return this;
    }

    public void setGuid(String guid) {
        this.guid = guid;
    }

    public String getNameEn() {
        return this.nameEn;
    }

    public Gantry nameEn(String nameEn) {
        this.setNameEn(nameEn);
        return this;
    }

    public void setNameEn(String nameEn) {
        this.nameEn = nameEn;
    }

    public String getNameAr() {
        return this.nameAr;
    }

    public Gantry nameAr(String nameAr) {
        this.setNameAr(nameAr);
        return this;
    }

    public void setNameAr(String nameAr) {
        this.nameAr = nameAr;
    }

    public Integer getStatus() {
        return this.status;
    }

    public Gantry status(Integer status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getCode() {
        return this.code;
    }

    public Gantry code(String code) {
        this.setCode(code);
        return this;
    }

    public void setCode(String code) {
        this.code = code;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Gantry)) {
            return false;
        }
        return id != null && id.equals(((Gantry) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Gantry{" +
            "id=" + getId() +
            ", guid='" + getGuid() + "'" +
            ", nameEn='" + getNameEn() + "'" +
            ", nameAr='" + getNameAr() + "'" +
            ", status=" + getStatus() +
            ", code='" + getCode() + "'" +
            "}";
    }
}
