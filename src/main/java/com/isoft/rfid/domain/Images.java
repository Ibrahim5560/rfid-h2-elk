package com.isoft.rfid.domain;

import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * Images (images) entity.\n@author Ibrahim Mohamed.
 */
@Entity
@Table(name = "images")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "images")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Images implements Serializable {

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

    @Lob
    @Column(name = "image_lp")
    private byte[] imageLp;

    @Column(name = "image_lp_content_type")
    private String imageLpContentType;

    @Lob
    @Column(name = "image_thumb")
    private byte[] imageThumb;

    @Column(name = "image_thumb_content_type")
    private String imageThumbContentType;

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

    @Column(name = "issue")
    private String issue;

    @Column(name = "status")
    private String status;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Images id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getGuid() {
        return this.guid;
    }

    public Images guid(String guid) {
        this.setGuid(guid);
        return this;
    }

    public void setGuid(String guid) {
        this.guid = guid;
    }

    public String getPlate() {
        return this.plate;
    }

    public Images plate(String plate) {
        this.setPlate(plate);
        return this;
    }

    public void setPlate(String plate) {
        this.plate = plate;
    }

    public byte[] getImageLp() {
        return this.imageLp;
    }

    public Images imageLp(byte[] imageLp) {
        this.setImageLp(imageLp);
        return this;
    }

    public void setImageLp(byte[] imageLp) {
        this.imageLp = imageLp;
    }

    public String getImageLpContentType() {
        return this.imageLpContentType;
    }

    public Images imageLpContentType(String imageLpContentType) {
        this.imageLpContentType = imageLpContentType;
        return this;
    }

    public void setImageLpContentType(String imageLpContentType) {
        this.imageLpContentType = imageLpContentType;
    }

    public byte[] getImageThumb() {
        return this.imageThumb;
    }

    public Images imageThumb(byte[] imageThumb) {
        this.setImageThumb(imageThumb);
        return this;
    }

    public void setImageThumb(byte[] imageThumb) {
        this.imageThumb = imageThumb;
    }

    public String getImageThumbContentType() {
        return this.imageThumbContentType;
    }

    public Images imageThumbContentType(String imageThumbContentType) {
        this.imageThumbContentType = imageThumbContentType;
        return this;
    }

    public void setImageThumbContentType(String imageThumbContentType) {
        this.imageThumbContentType = imageThumbContentType;
    }

    public String getAnpr() {
        return this.anpr;
    }

    public Images anpr(String anpr) {
        this.setAnpr(anpr);
        return this;
    }

    public void setAnpr(String anpr) {
        this.anpr = anpr;
    }

    public String getRfid() {
        return this.rfid;
    }

    public Images rfid(String rfid) {
        this.setRfid(rfid);
        return this;
    }

    public void setRfid(String rfid) {
        this.rfid = rfid;
    }

    public String getDataStatus() {
        return this.dataStatus;
    }

    public Images dataStatus(String dataStatus) {
        this.setDataStatus(dataStatus);
        return this;
    }

    public void setDataStatus(String dataStatus) {
        this.dataStatus = dataStatus;
    }

    public Long getGantry() {
        return this.gantry;
    }

    public Images gantry(Long gantry) {
        this.setGantry(gantry);
        return this;
    }

    public void setGantry(Long gantry) {
        this.gantry = gantry;
    }

    public Long getLane() {
        return this.lane;
    }

    public Images lane(Long lane) {
        this.setLane(lane);
        return this;
    }

    public void setLane(Long lane) {
        this.lane = lane;
    }

    public Long getKph() {
        return this.kph;
    }

    public Images kph(Long kph) {
        this.setKph(kph);
        return this;
    }

    public void setKph(Long kph) {
        this.kph = kph;
    }

    public Long getAmbush() {
        return this.ambush;
    }

    public Images ambush(Long ambush) {
        this.setAmbush(ambush);
        return this;
    }

    public void setAmbush(Long ambush) {
        this.ambush = ambush;
    }

    public Long getDirection() {
        return this.direction;
    }

    public Images direction(Long direction) {
        this.setDirection(direction);
        return this;
    }

    public void setDirection(Long direction) {
        this.direction = direction;
    }

    public Long getVehicle() {
        return this.vehicle;
    }

    public Images vehicle(Long vehicle) {
        this.setVehicle(vehicle);
        return this;
    }

    public void setVehicle(Long vehicle) {
        this.vehicle = vehicle;
    }

    public String getIssue() {
        return this.issue;
    }

    public Images issue(String issue) {
        this.setIssue(issue);
        return this;
    }

    public void setIssue(String issue) {
        this.issue = issue;
    }

    public String getStatus() {
        return this.status;
    }

    public Images status(String status) {
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
        if (!(o instanceof Images)) {
            return false;
        }
        return id != null && id.equals(((Images) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Images{" +
            "id=" + getId() +
            ", guid='" + getGuid() + "'" +
            ", plate='" + getPlate() + "'" +
            ", imageLp='" + getImageLp() + "'" +
            ", imageLpContentType='" + getImageLpContentType() + "'" +
            ", imageThumb='" + getImageThumb() + "'" +
            ", imageThumbContentType='" + getImageThumbContentType() + "'" +
            ", anpr='" + getAnpr() + "'" +
            ", rfid='" + getRfid() + "'" +
            ", dataStatus='" + getDataStatus() + "'" +
            ", gantry=" + getGantry() +
            ", lane=" + getLane() +
            ", kph=" + getKph() +
            ", ambush=" + getAmbush() +
            ", direction=" + getDirection() +
            ", vehicle=" + getVehicle() +
            ", issue='" + getIssue() + "'" +
            ", status='" + getStatus() + "'" +
            "}";
    }
}
