package com.isoft.rfid.domain;

import java.io.Serializable;
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

    @Column(name = "handled_by")
    private Long handledBy;

    @Column(name = "gantry_processed")
    private Long gantryProcessed;

    @Column(name = "gantry_sent")
    private Long gantrySent;

    @Column(name = "jhi_when")
    private Long when;

    @Column(name = "toll")
    private Long toll;

    @Column(name = "rule_rcvd")
    private Long ruleRcvd;

    @Column(name = "wanted_for")
    private String wantedFor;

    @Column(name = "fine")
    private Long fine;

    @Column(name = "license_issue")
    private String licenseIssue;

    @Column(name = "wanted_by")
    private Long wantedBy;

    @Column(name = "rule_processed")
    private Long ruleProcessed;

    @Column(name = "speed_fine")
    private Long speedFine;

    @NotNull
    @Column(name = "lane", nullable = false)
    private Long lane;

    @Column(name = "tag_issue")
    private String tagIssue;

    @Column(name = "status_name")
    private String statusName;

    @Column(name = "license_fine")
    private Long licenseFine;

    @Column(name = "stolen")
    private Long stolen;

    @Column(name = "wanted")
    private Boolean wanted;

    @Column(name = "rule_sent")
    private Long ruleSent;

    @Column(name = "handled")
    private Long handled;

    @Column(name = "rule_issue")
    private String ruleIssue;

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

    public String getGuid() {
        return this.guid;
    }

    public Events guid(String guid) {
        this.setGuid(guid);
        return this;
    }

    public void setGuid(String guid) {
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

    public Long getGantry() {
        return this.gantry;
    }

    public Events gantry(Long gantry) {
        this.setGantry(gantry);
        return this;
    }

    public void setGantry(Long gantry) {
        this.gantry = gantry;
    }

    public Long getKph() {
        return this.kph;
    }

    public Events kph(Long kph) {
        this.setKph(kph);
        return this;
    }

    public void setKph(Long kph) {
        this.kph = kph;
    }

    public Long getAmbush() {
        return this.ambush;
    }

    public Events ambush(Long ambush) {
        this.setAmbush(ambush);
        return this;
    }

    public void setAmbush(Long ambush) {
        this.ambush = ambush;
    }

    public Long getDirection() {
        return this.direction;
    }

    public Events direction(Long direction) {
        this.setDirection(direction);
        return this;
    }

    public void setDirection(Long direction) {
        this.direction = direction;
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

    public Long getHandledBy() {
        return this.handledBy;
    }

    public Events handledBy(Long handledBy) {
        this.setHandledBy(handledBy);
        return this;
    }

    public void setHandledBy(Long handledBy) {
        this.handledBy = handledBy;
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

    public Long getToll() {
        return this.toll;
    }

    public Events toll(Long toll) {
        this.setToll(toll);
        return this;
    }

    public void setToll(Long toll) {
        this.toll = toll;
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

    public Long getFine() {
        return this.fine;
    }

    public Events fine(Long fine) {
        this.setFine(fine);
        return this;
    }

    public void setFine(Long fine) {
        this.fine = fine;
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

    public Long getWantedBy() {
        return this.wantedBy;
    }

    public Events wantedBy(Long wantedBy) {
        this.setWantedBy(wantedBy);
        return this;
    }

    public void setWantedBy(Long wantedBy) {
        this.wantedBy = wantedBy;
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

    public Long getSpeedFine() {
        return this.speedFine;
    }

    public Events speedFine(Long speedFine) {
        this.setSpeedFine(speedFine);
        return this;
    }

    public void setSpeedFine(Long speedFine) {
        this.speedFine = speedFine;
    }

    public Long getLane() {
        return this.lane;
    }

    public Events lane(Long lane) {
        this.setLane(lane);
        return this;
    }

    public void setLane(Long lane) {
        this.lane = lane;
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

    public Long getLicenseFine() {
        return this.licenseFine;
    }

    public Events licenseFine(Long licenseFine) {
        this.setLicenseFine(licenseFine);
        return this;
    }

    public void setLicenseFine(Long licenseFine) {
        this.licenseFine = licenseFine;
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

    public String getRuleIssue() {
        return this.ruleIssue;
    }

    public Events ruleIssue(String ruleIssue) {
        this.setRuleIssue(ruleIssue);
        return this;
    }

    public void setRuleIssue(String ruleIssue) {
        this.ruleIssue = ruleIssue;
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
            "}";
    }
}
