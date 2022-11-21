package com.isoft.rfid.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Lob;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.isoft.rfid.domain.Task} entity.
 */
@Schema(description = "Task (task) entity.\n@author Ibrahim Mohamed.")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class TaskDTO implements Serializable {

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

    private Long kph;

    private Long ambush;

    private Long direction;

    @NotNull
    private Long vehicle;

    private String issue;

    private String status;

    private Long handledBy;

    private Long gantryProcessed;

    private Long gantrySent;

    private Long when;

    private Double toll;

    private Long ruleRcvd;

    private String wantedFor;

    private Double fine;

    private String licenseIssue;

    private Long wantedBy;

    private Long ruleProcessed;

    private Double speedFine;

    @NotNull
    private Long lane;

    private String tagIssue;

    private String statusName;

    private Double licenseFine;

    private Long stolen;

    private Boolean wanted;

    private Long ruleSent;

    private Long handled;

    private String ruleIssue;

    private Long processingTime;

    private String threadRandNo;

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

    public Long getHandledBy() {
        return handledBy;
    }

    public void setHandledBy(Long handledBy) {
        this.handledBy = handledBy;
    }

    public Long getGantryProcessed() {
        return gantryProcessed;
    }

    public void setGantryProcessed(Long gantryProcessed) {
        this.gantryProcessed = gantryProcessed;
    }

    public Long getGantrySent() {
        return gantrySent;
    }

    public void setGantrySent(Long gantrySent) {
        this.gantrySent = gantrySent;
    }

    public Long getWhen() {
        return when;
    }

    public void setWhen(Long when) {
        this.when = when;
    }

    public Double getToll() {
        return toll;
    }

    public void setToll(Double toll) {
        this.toll = toll;
    }

    public Long getRuleRcvd() {
        return ruleRcvd;
    }

    public void setRuleRcvd(Long ruleRcvd) {
        this.ruleRcvd = ruleRcvd;
    }

    public String getWantedFor() {
        return wantedFor;
    }

    public void setWantedFor(String wantedFor) {
        this.wantedFor = wantedFor;
    }

    public Double getFine() {
        return fine;
    }

    public void setFine(Double fine) {
        this.fine = fine;
    }

    public String getLicenseIssue() {
        return licenseIssue;
    }

    public void setLicenseIssue(String licenseIssue) {
        this.licenseIssue = licenseIssue;
    }

    public Long getWantedBy() {
        return wantedBy;
    }

    public void setWantedBy(Long wantedBy) {
        this.wantedBy = wantedBy;
    }

    public Long getRuleProcessed() {
        return ruleProcessed;
    }

    public void setRuleProcessed(Long ruleProcessed) {
        this.ruleProcessed = ruleProcessed;
    }

    public Double getSpeedFine() {
        return speedFine;
    }

    public void setSpeedFine(Double speedFine) {
        this.speedFine = speedFine;
    }

    public Long getLane() {
        return lane;
    }

    public void setLane(Long lane) {
        this.lane = lane;
    }

    public String getTagIssue() {
        return tagIssue;
    }

    public void setTagIssue(String tagIssue) {
        this.tagIssue = tagIssue;
    }

    public String getStatusName() {
        return statusName;
    }

    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }

    public Double getLicenseFine() {
        return licenseFine;
    }

    public void setLicenseFine(Double licenseFine) {
        this.licenseFine = licenseFine;
    }

    public Long getStolen() {
        return stolen;
    }

    public void setStolen(Long stolen) {
        this.stolen = stolen;
    }

    public Boolean getWanted() {
        return wanted;
    }

    public void setWanted(Boolean wanted) {
        this.wanted = wanted;
    }

    public Long getRuleSent() {
        return ruleSent;
    }

    public void setRuleSent(Long ruleSent) {
        this.ruleSent = ruleSent;
    }

    public Long getHandled() {
        return handled;
    }

    public void setHandled(Long handled) {
        this.handled = handled;
    }

    public String getRuleIssue() {
        return ruleIssue;
    }

    public void setRuleIssue(String ruleIssue) {
        this.ruleIssue = ruleIssue;
    }

    public Long getProcessingTime() {
        return processingTime;
    }

    public void setProcessingTime(Long processingTime) {
        this.processingTime = processingTime;
    }

    public String getThreadRandNo() {
        return threadRandNo;
    }

    public void setThreadRandNo(String threadRandNo) {
        this.threadRandNo = threadRandNo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TaskDTO)) {
            return false;
        }

        TaskDTO taskDTO = (TaskDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, taskDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TaskDTO{" +
            "id=" + getId() +
            ", guid='" + getGuid() + "'" +
            ", plate='" + getPlate() + "'" +
            ", imageLp='" + getImageLp() + "'" +
            ", imageThumb='" + getImageThumb() + "'" +
            ", anpr='" + getAnpr() + "'" +
            ", rfid='" + getRfid() + "'" +
            ", dataStatus='" + getDataStatus() + "'" +
            ", gantry=" + getGantry() +
            ", kph=" + getKph() +
            ", ambush=" + getAmbush() +
            ", direction=" + getDirection() +
            ", vehicle=" + getVehicle() +
            ", issue='" + getIssue() + "'" +
            ", status='" + getStatus() + "'" +
            ", handledBy=" + getHandledBy() +
            ", gantryProcessed=" + getGantryProcessed() +
            ", gantrySent=" + getGantrySent() +
            ", when=" + getWhen() +
            ", toll=" + getToll() +
            ", ruleRcvd=" + getRuleRcvd() +
            ", wantedFor='" + getWantedFor() + "'" +
            ", fine=" + getFine() +
            ", licenseIssue='" + getLicenseIssue() + "'" +
            ", wantedBy=" + getWantedBy() +
            ", ruleProcessed=" + getRuleProcessed() +
            ", speedFine=" + getSpeedFine() +
            ", lane=" + getLane() +
            ", tagIssue='" + getTagIssue() + "'" +
            ", statusName='" + getStatusName() + "'" +
            ", licenseFine=" + getLicenseFine() +
            ", stolen=" + getStolen() +
            ", wanted='" + getWanted() + "'" +
            ", ruleSent=" + getRuleSent() +
            ", handled=" + getHandled() +
            ", ruleIssue='" + getRuleIssue() + "'" +
            ", processingTime=" + getProcessingTime() +
            ", threadRandNo='" + getThreadRandNo() + "'" +
            "}";
    }
}
