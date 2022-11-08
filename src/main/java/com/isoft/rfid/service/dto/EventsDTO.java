package com.isoft.rfid.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.isoft.rfid.domain.Events} entity.
 */
@Schema(description = "Events (events) entity.\n@author Ibrahim Mohamed.")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class EventsDTO implements Serializable {

    private Long id;

    @NotNull
    private UUID guid;

    private String plate;

    private String anpr;

    private String rfid;

    private Integer gantry;

    private String wantedFor;

    private String licenseIssue;

    private String issue;

    private String tagIssue;

    private String statusName;

    private Integer lane;

    private Integer direction;

    private Integer kph;

    private Integer ambush;

    private Double toll;

    private Double fine;

    private Double wantedBy;

    private Double licenseFine;

    private Double speedFine;

    private Long handled;

    private Long processingTime;

    private Long ruleRcvd;

    private Long ruleIssue;

    private Long ruleProcessed;

    private Long ruleSent;

    private Long when;

    private Long vehicle;

    private Long stolen;

    private Boolean wanted;

    private Long gantryProcessed;

    private Long gantrySent;

    private String status;

    private String dataStatus;

    private String threadRandNo;

    private String handledBy;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UUID getGuid() {
        return guid;
    }

    public void setGuid(UUID guid) {
        this.guid = guid;
    }

    public String getPlate() {
        return plate;
    }

    public void setPlate(String plate) {
        this.plate = plate;
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

    public Integer getGantry() {
        return gantry;
    }

    public void setGantry(Integer gantry) {
        this.gantry = gantry;
    }

    public String getWantedFor() {
        return wantedFor;
    }

    public void setWantedFor(String wantedFor) {
        this.wantedFor = wantedFor;
    }

    public String getLicenseIssue() {
        return licenseIssue;
    }

    public void setLicenseIssue(String licenseIssue) {
        this.licenseIssue = licenseIssue;
    }

    public String getIssue() {
        return issue;
    }

    public void setIssue(String issue) {
        this.issue = issue;
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

    public Integer getLane() {
        return lane;
    }

    public void setLane(Integer lane) {
        this.lane = lane;
    }

    public Integer getDirection() {
        return direction;
    }

    public void setDirection(Integer direction) {
        this.direction = direction;
    }

    public Integer getKph() {
        return kph;
    }

    public void setKph(Integer kph) {
        this.kph = kph;
    }

    public Integer getAmbush() {
        return ambush;
    }

    public void setAmbush(Integer ambush) {
        this.ambush = ambush;
    }

    public Double getToll() {
        return toll;
    }

    public void setToll(Double toll) {
        this.toll = toll;
    }

    public Double getFine() {
        return fine;
    }

    public void setFine(Double fine) {
        this.fine = fine;
    }

    public Double getWantedBy() {
        return wantedBy;
    }

    public void setWantedBy(Double wantedBy) {
        this.wantedBy = wantedBy;
    }

    public Double getLicenseFine() {
        return licenseFine;
    }

    public void setLicenseFine(Double licenseFine) {
        this.licenseFine = licenseFine;
    }

    public Double getSpeedFine() {
        return speedFine;
    }

    public void setSpeedFine(Double speedFine) {
        this.speedFine = speedFine;
    }

    public Long getHandled() {
        return handled;
    }

    public void setHandled(Long handled) {
        this.handled = handled;
    }

    public Long getProcessingTime() {
        return processingTime;
    }

    public void setProcessingTime(Long processingTime) {
        this.processingTime = processingTime;
    }

    public Long getRuleRcvd() {
        return ruleRcvd;
    }

    public void setRuleRcvd(Long ruleRcvd) {
        this.ruleRcvd = ruleRcvd;
    }

    public Long getRuleIssue() {
        return ruleIssue;
    }

    public void setRuleIssue(Long ruleIssue) {
        this.ruleIssue = ruleIssue;
    }

    public Long getRuleProcessed() {
        return ruleProcessed;
    }

    public void setRuleProcessed(Long ruleProcessed) {
        this.ruleProcessed = ruleProcessed;
    }

    public Long getRuleSent() {
        return ruleSent;
    }

    public void setRuleSent(Long ruleSent) {
        this.ruleSent = ruleSent;
    }

    public Long getWhen() {
        return when;
    }

    public void setWhen(Long when) {
        this.when = when;
    }

    public Long getVehicle() {
        return vehicle;
    }

    public void setVehicle(Long vehicle) {
        this.vehicle = vehicle;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDataStatus() {
        return dataStatus;
    }

    public void setDataStatus(String dataStatus) {
        this.dataStatus = dataStatus;
    }

    public String getThreadRandNo() {
        return threadRandNo;
    }

    public void setThreadRandNo(String threadRandNo) {
        this.threadRandNo = threadRandNo;
    }

    public String getHandledBy() {
        return handledBy;
    }

    public void setHandledBy(String handledBy) {
        this.handledBy = handledBy;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof EventsDTO)) {
            return false;
        }

        EventsDTO eventsDTO = (EventsDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, eventsDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "EventsDTO{" +
            "id=" + getId() +
            ", guid='" + getGuid() + "'" +
            ", plate='" + getPlate() + "'" +
            ", anpr='" + getAnpr() + "'" +
            ", rfid='" + getRfid() + "'" +
            ", gantry=" + getGantry() +
            ", wantedFor='" + getWantedFor() + "'" +
            ", licenseIssue='" + getLicenseIssue() + "'" +
            ", issue='" + getIssue() + "'" +
            ", tagIssue='" + getTagIssue() + "'" +
            ", statusName='" + getStatusName() + "'" +
            ", lane=" + getLane() +
            ", direction=" + getDirection() +
            ", kph=" + getKph() +
            ", ambush=" + getAmbush() +
            ", toll=" + getToll() +
            ", fine=" + getFine() +
            ", wantedBy=" + getWantedBy() +
            ", licenseFine=" + getLicenseFine() +
            ", speedFine=" + getSpeedFine() +
            ", handled=" + getHandled() +
            ", processingTime=" + getProcessingTime() +
            ", ruleRcvd=" + getRuleRcvd() +
            ", ruleIssue=" + getRuleIssue() +
            ", ruleProcessed=" + getRuleProcessed() +
            ", ruleSent=" + getRuleSent() +
            ", when=" + getWhen() +
            ", vehicle=" + getVehicle() +
            ", stolen=" + getStolen() +
            ", wanted='" + getWanted() + "'" +
            ", gantryProcessed=" + getGantryProcessed() +
            ", gantrySent=" + getGantrySent() +
            ", status='" + getStatus() + "'" +
            ", dataStatus='" + getDataStatus() + "'" +
            ", threadRandNo='" + getThreadRandNo() + "'" +
            ", handledBy='" + getHandledBy() + "'" +
            "}";
    }
}
