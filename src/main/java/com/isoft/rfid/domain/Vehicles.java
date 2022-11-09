package com.isoft.rfid.domain;

import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * Vehicles (vehicles) entity.\n@author Ibrahim Mohamed.
 */
@Entity
@Table(name = "vehicles")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "vehicles")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Vehicles implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "guid", nullable = false)
    private String guid;

    @Column(name = "plate")
    private String plate;

    @Column(name = "anpr")
    private String anpr;

    @Column(name = "rfid")
    private String rfid;

    @NotNull
    @Column(name = "data_status", nullable = false)
    private String dataStatus;

    @NotNull
    @Column(name = "gantry", nullable = false)
    private Long gantry;

    @NotNull
    @Column(name = "lane", nullable = false)
    private Long lane;

    @Column(name = "kph")
    private Long kph;

    @Column(name = "ambush")
    private Long ambush;

    @Column(name = "direction")
    private Long direction;

    @NotNull
    @Column(name = "vehicle", nullable = false)
    private Long vehicle;

    @Column(name = "status")
    private String status;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Vehicles id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getGuid() {
        return this.guid;
    }

    public Vehicles guid(String guid) {
        this.setGuid(guid);
        return this;
    }

    public void setGuid(String guid) {
        this.guid = guid;
    }

    public String getPlate() {
        return this.plate;
    }

    public Vehicles plate(String plate) {
        this.setPlate(plate);
        return this;
    }

    public void setPlate(String plate) {
        this.plate = plate;
    }

    public String getAnpr() {
        return this.anpr;
    }

    public Vehicles anpr(String anpr) {
        this.setAnpr(anpr);
        return this;
    }

    public void setAnpr(String anpr) {
        this.anpr = anpr;
    }

    public String getRfid() {
        return this.rfid;
    }

    public Vehicles rfid(String rfid) {
        this.setRfid(rfid);
        return this;
    }

    public void setRfid(String rfid) {
        this.rfid = rfid;
    }

    public String getDataStatus() {
        return this.dataStatus;
    }

    public Vehicles dataStatus(String dataStatus) {
        this.setDataStatus(dataStatus);
        return this;
    }

    public void setDataStatus(String dataStatus) {
        this.dataStatus = dataStatus;
    }

    public Long getGantry() {
        return this.gantry;
    }

    public Vehicles gantry(Long gantry) {
        this.setGantry(gantry);
        return this;
    }

    public void setGantry(Long gantry) {
        this.gantry = gantry;
    }

    public Long getLane() {
        return this.lane;
    }

    public Vehicles lane(Long lane) {
        this.setLane(lane);
        return this;
    }

    public void setLane(Long lane) {
        this.lane = lane;
    }

    public Long getKph() {
        return this.kph;
    }

    public Vehicles kph(Long kph) {
        this.setKph(kph);
        return this;
    }

    public void setKph(Long kph) {
        this.kph = kph;
    }

    public Long getAmbush() {
        return this.ambush;
    }

    public Vehicles ambush(Long ambush) {
        this.setAmbush(ambush);
        return this;
    }

    public void setAmbush(Long ambush) {
        this.ambush = ambush;
    }

    public Long getDirection() {
        return this.direction;
    }

    public Vehicles direction(Long direction) {
        this.setDirection(direction);
        return this;
    }

    public void setDirection(Long direction) {
        this.direction = direction;
    }

    public Long getVehicle() {
        return this.vehicle;
    }

    public Vehicles vehicle(Long vehicle) {
        this.setVehicle(vehicle);
        return this;
    }

    public void setVehicle(Long vehicle) {
        this.vehicle = vehicle;
    }

    public String getStatus() {
        return this.status;
    }

    public Vehicles status(String status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Vehicles)) {
            return false;
        }
        return id != null && id.equals(((Vehicles) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Vehicles{" +
            "id=" + getId() +
            ", guid='" + getGuid() + "'" +
            ", plate='" + getPlate() + "'" +
            ", anpr='" + getAnpr() + "'" +
            ", rfid='" + getRfid() + "'" +
            ", dataStatus='" + getDataStatus() + "'" +
            ", gantry=" + getGantry() +
            ", lane=" + getLane() +
            ", kph=" + getKph() +
            ", ambush=" + getAmbush() +
            ", direction=" + getDirection() +
            ", vehicle=" + getVehicle() +
            ", status='" + getStatus() + "'" +
            "}";
    }
}
