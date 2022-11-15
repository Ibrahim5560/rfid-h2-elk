package com.isoft.rfid.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Lob;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.isoft.rfid.domain.Images} entity.
 */
@Schema(description = "Images (images) entity.\n@author Ibrahim Mohamed.")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ImagesDTO implements Serializable {

    private Long id;

    @NotNull
    private String guid;

    private String plate;

    @Lob
    private byte[] imageLp;

    private String imageLpContentType;

    @Lob
    private byte[] imageThumb;

    private String imageThumbContentType;
    private String anpr;

    private String rfid;

    @NotNull
    private String dataStatus;

    @NotNull
    private Long gantry;

    @NotNull
    private Long lane;

    private Long kph;

    private Long ambush;

    private Long direction;

    @NotNull
    private Long vehicle;

    private String issue;

    private String status;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getGuid() {
        return guid;
    }

    public void setGuid(String guid) {
        this.guid = guid;
    }

    public String getPlate() {
        return plate;
    }

    public void setPlate(String plate) {
        this.plate = plate;
    }

    public byte[] getImageLp() {
        return imageLp;
    }

    public void setImageLp(byte[] imageLp) {
        this.imageLp = imageLp;
    }

    public String getImageLpContentType() {
        return imageLpContentType;
    }

    public void setImageLpContentType(String imageLpContentType) {
        this.imageLpContentType = imageLpContentType;
    }

    public byte[] getImageThumb() {
        return imageThumb;
    }

    public void setImageThumb(byte[] imageThumb) {
        this.imageThumb = imageThumb;
    }

    public String getImageThumbContentType() {
        return imageThumbContentType;
    }

    public void setImageThumbContentType(String imageThumbContentType) {
        this.imageThumbContentType = imageThumbContentType;
    }

    public String getAnpr() {
        return anpr;
    }

    public void setAnpr(String anpr) {
        this.anpr = anpr;
    }

    public String getRfid() {
        return rfid;
    }

    public void setRfid(String rfid) {
        this.rfid = rfid;
    }

    public String getDataStatus() {
        return dataStatus;
    }

    public void setDataStatus(String dataStatus) {
        this.dataStatus = dataStatus;
    }

    public Long getGantry() {
        return gantry;
    }

    public void setGantry(Long gantry) {
        this.gantry = gantry;
    }

    public Long getLane() {
        return lane;
    }

    public void setLane(Long lane) {
        this.lane = lane;
    }

    public Long getKph() {
        return kph;
    }

    public void setKph(Long kph) {
        this.kph = kph;
    }

    public Long getAmbush() {
        return ambush;
    }

    public void setAmbush(Long ambush) {
        this.ambush = ambush;
    }

    public Long getDirection() {
        return direction;
    }

    public void setDirection(Long direction) {
        this.direction = direction;
    }

    public Long getVehicle() {
        return vehicle;
    }

    public void setVehicle(Long vehicle) {
        this.vehicle = vehicle;
    }

    public String getIssue() {
        return issue;
    }

    public void setIssue(String issue) {
        this.issue = issue;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ImagesDTO)) {
            return false;
        }

        ImagesDTO imagesDTO = (ImagesDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, imagesDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ImagesDTO{" +
            "id=" + getId() +
            ", guid='" + getGuid() + "'" +
            ", plate='" + getPlate() + "'" +
            ", imageLp='" + getImageLp() + "'" +
            ", imageThumb='" + getImageThumb() + "'" +
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
