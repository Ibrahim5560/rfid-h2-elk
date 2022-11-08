package com.isoft.rfid.domain;

import java.io.Serializable;
import java.util.UUID;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * Events (events) entity.\n@author Ibrahim Mohamed.
 */
@Entity
@Table(name = "events")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "events")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Events implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "guid", nullable = false)
    private UUID guid;

    @Column(name = "plate")
    private String plate;

    @Column(name = "anpr")
    private String anpr;

    @Column(name = "rfid")
    private String rfid;

    @Column(name = "gantry")
    private Integer gantry;

    @Column(name = "wanted_for")
    private String wantedFor;

    @Column(name = "license_issue")
    private String licenseIssue;

    @Column(name = "issue")
    private String issue;

    @Column(name = "tag_issue")
    private String tagIssue;

    @Column(name = "status_name")
    private String statusName;

    @Column(name = "lane")
    private Integer lane;

    @Column(name = "direction")
    private Integer direction;

    @Column(name = "kph")
    private Integer kph;

    @Column(name = "ambush")
    private Integer ambush;

    @Column(name = "toll")
    private Double toll;

    @Column(name = "fine")
    private Double fine;

    @Column(name = "wanted_by")
    private Double wantedBy;

    @Column(name = "license_fine")
    private Double licenseFine;

    @Column(name = "speed_fine")
    private Double speedFine;

    @Column(name = "handled")
    private Long handled;

    @Column(name = "processing_time")
    private Long processingTime;

    @Column(name = "rule_rcvd")
    private Long ruleRcvd;

    @Column(name = "rule_issue")
    private Long ruleIssue;

    @Column(name = "rule_processed")
    private Long ruleProcessed;

    @Column(name = "rule_sent")
    private Long ruleSent;

    @Column(name = "jhi_when")
    private Long when;

    @Column(name = "vehicle")
    private Long vehicle;

    @Column(name = "stolen")
    private Long stolen;

    @Column(name = "wanted")
    private Boolean wanted;

    @Column(name = "gantry_processed")
    private Long gantryProcessed;

    @Column(name = "gantry_sent")
    private Long gantrySent;

    @Column(name = "status")
    private String status;

    @Column(name = "data_status")
    private String dataStatus;

    @Column(name = "thread_rand_no")
    private String threadRandNo;

    @Column(name = "handled_by")
    private String handledBy;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Events id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UUID getGuid() {
        return this.guid;
    }

    public Events guid(UUID guid) {
        this.setGuid(guid);
        return this;
    }

    public void setGuid(UUID guid) {
        this.guid = guid;
    }

    public String getPlate() {
        return this.plate;
    }

    public Events plate(String plate) {
        this.setPlate(plate);
        return this;
    }

    public void setPlate(String plate) {
        this.plate = plate;
    }

    public String getAnpr() {
        return this.anpr;
    }

    public Events anpr(String anpr) {
        this.setAnpr(anpr);
        return this;
    }

    public void setAnpr(String anpr) {
        this.anpr = anpr;
    }

    public String getRfid() {
        return this.rfid;
    }

    public Events rfid(String rfid) {
        this.setRfid(rfid);
        return this;
    }

    public void setRfid(String rfid) {
        this.rfid = rfid;
    }

    public Integer getGantry() {
        return this.gantry;
    }

    public Events gantry(Integer gantry) {
        this.setGantry(gantry);
        return this;
    }

    public void setGantry(Integer gantry) {
        this.gantry = gantry;
    }

    public String getWantedFor() {
        return this.wantedFor;
    }

    public Events wantedFor(String wantedFor) {
        this.setWantedFor(wantedFor);
        return this;
    }

    public void setWantedFor(String wantedFor) {
        this.wantedFor = wantedFor;
    }

    public String getLicenseIssue() {
        return this.licenseIssue;
    }

    public Events licenseIssue(String licenseIssue) {
        this.setLicenseIssue(licenseIssue);
        return this;
    }

    public void setLicenseIssue(String licenseIssue) {
        this.licenseIssue = licenseIssue;
    }

    public String getIssue() {
        return this.issue;
    }

    public Events issue(String issue) {
        this.setIssue(issue);
        return this;
    }

    public void setIssue(String issue) {
        this.issue = issue;
    }

    public String getTagIssue() {
        return this.tagIssue;
    }

    public Events tagIssue(String tagIssue) {
        this.setTagIssue(tagIssue);
        return this;
    }

    public void setTagIssue(String tagIssue) {
        this.tagIssue = tagIssue;
    }

    public String getStatusName() {
        return this.statusName;
    }

    public Events statusName(String statusName) {
        this.setStatusName(statusName);
        return this;
    }

    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }

    public Integer getLane() {
        return this.lane;
    }

    public Events lane(Integer lane) {
        this.setLane(lane);
        return this;
    }

    public void setLane(Integer lane) {
        this.lane = lane;
    }

    public Integer getDirection() {
        return this.direction;
    }

    public Events direction(Integer direction) {
        this.setDirection(direction);
        return this;
    }

    public void setDirection(Integer direction) {
        this.direction = direction;
    }

    public Integer getKph() {
        return this.kph;
    }

    public Events kph(Integer kph) {
        this.setKph(kph);
        return this;
    }

    public void setKph(Integer kph) {
        this.kph = kph;
    }

    public Integer getAmbush() {
        return this.ambush;
    }

    public Events ambush(Integer ambush) {
        this.setAmbush(ambush);
        return this;
    }

    public void setAmbush(Integer ambush) {
        this.ambush = ambush;
    }

    public Double getToll() {
        return this.toll;
    }

    public Events toll(Double toll) {
        this.setToll(toll);
        return this;
    }

    public void setToll(Double toll) {
        this.toll = toll;
    }

    public Double getFine() {
        return this.fine;
    }

    public Events fine(Double fine) {
        this.setFine(fine);
        return this;
    }

    public void setFine(Double fine) {
        this.fine = fine;
    }

    public Double getWantedBy() {
        return this.wantedBy;
    }

    public Events wantedBy(Double wantedBy) {
        this.setWantedBy(wantedBy);
        return this;
    }

    public void setWantedBy(Double wantedBy) {
        this.wantedBy = wantedBy;
    }

    public Double getLicenseFine() {
        return this.licenseFine;
    }

    public Events licenseFine(Double licenseFine) {
        this.setLicenseFine(licenseFine);
        return this;
    }

    public void setLicenseFine(Double licenseFine) {
        this.licenseFine = licenseFine;
    }

    public Double getSpeedFine() {
        return this.speedFine;
    }

    public Events speedFine(Double speedFine) {
        this.setSpeedFine(speedFine);
        return this;
    }

    public void setSpeedFine(Double speedFine) {
        this.speedFine = speedFine;
    }

    public Long getHandled() {
        return this.handled;
    }

    public Events handled(Long handled) {
        this.setHandled(handled);
        return this;
    }

    public void setHandled(Long handled) {
        this.handled = handled;
    }

    public Long getProcessingTime() {
        return this.processingTime;
    }

    public Events processingTime(Long processingTime) {
        this.setProcessingTime(processingTime);
        return this;
    }

    public void setProcessingTime(Long processingTime) {
        this.processingTime = processingTime;
    }

    public Long getRuleRcvd() {
        return this.ruleRcvd;
    }

    public Events ruleRcvd(Long ruleRcvd) {
        this.setRuleRcvd(ruleRcvd);
        return this;
    }

    public void setRuleRcvd(Long ruleRcvd) {
        this.ruleRcvd = ruleRcvd;
    }

    public Long getRuleIssue() {
        return this.ruleIssue;
    }

    public Events ruleIssue(Long ruleIssue) {
        this.setRuleIssue(ruleIssue);
        return this;
    }

    public void setRuleIssue(Long ruleIssue) {
        this.ruleIssue = ruleIssue;
    }

    public Long getRuleProcessed() {
        return this.ruleProcessed;
    }

    public Events ruleProcessed(Long ruleProcessed) {
        this.setRuleProcessed(ruleProcessed);
        return this;
    }

    public void setRuleProcessed(Long ruleProcessed) {
        this.ruleProcessed = ruleProcessed;
    }

    public Long getRuleSent() {
        return this.ruleSent;
    }

    public Events ruleSent(Long ruleSent) {
        this.setRuleSent(ruleSent);
        return this;
    }

    public void setRuleSent(Long ruleSent) {
        this.ruleSent = ruleSent;
    }

    public Long getWhen() {
        return this.when;
    }

    public Events when(Long when) {
        this.setWhen(when);
        return this;
    }

    public void setWhen(Long when) {
        this.when = when;
    }

    public Long getVehicle() {
        return this.vehicle;
    }

    public Events vehicle(Long vehicle) {
        this.setVehicle(vehicle);
        return this;
    }

    public void setVehicle(Long vehicle) {
        this.vehicle = vehicle;
    }

    public Long getStolen() {
        return this.stolen;
    }

    public Events stolen(Long stolen) {
        this.setStolen(stolen);
        return this;
    }

    public void setStolen(Long stolen) {
        this.stolen = stolen;
    }

    public Boolean getWanted() {
        return this.wanted;
    }

    public Events wanted(Boolean wanted) {
        this.setWanted(wanted);
        return this;
    }

    public void setWanted(Boolean wanted) {
        this.wanted = wanted;
    }

    public Long getGantryProcessed() {
        return this.gantryProcessed;
    }

    public Events gantryProcessed(Long gantryProcessed) {
        this.setGantryProcessed(gantryProcessed);
        return this;
    }

    public void setGantryProcessed(Long gantryProcessed) {
        this.gantryProcessed = gantryProcessed;
    }

    public Long getGantrySent() {
        return this.gantrySent;
    }

    public Events gantrySent(Long gantrySent) {
        this.setGantrySent(gantrySent);
        return this;
    }

    public void setGantrySent(Long gantrySent) {
        this.gantrySent = gantrySent;
    }

    public String getStatus() {
        return this.status;
    }

    public Events status(String status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDataStatus() {
        return this.dataStatus;
    }

    public Events dataStatus(String dataStatus) {
        this.setDataStatus(dataStatus);
        return this;
    }

    public void setDataStatus(String dataStatus) {
        this.dataStatus = dataStatus;
    }

    public String getThreadRandNo() {
        return this.threadRandNo;
    }

    public Events threadRandNo(String threadRandNo) {
        this.setThreadRandNo(threadRandNo);
        return this;
    }

    public void setThreadRandNo(String threadRandNo) {
        this.threadRandNo = threadRandNo;
    }

    public String getHandledBy() {
        return this.handledBy;
    }

    public Events handledBy(String handledBy) {
        this.setHandledBy(handledBy);
        return this;
    }

    public void setHandledBy(String handledBy) {
        this.handledBy = handledBy;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Events)) {
            return false;
        }
        return id != null && id.equals(((Events) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Events{" +
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
