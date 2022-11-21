package com.isoft.rfid.domain;

import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * Task (task) entity.\n@author Ibrahim Mohamed.
 */
@Entity
@Table(name = "task")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "task")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Task implements Serializable {

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
    private Double toll;

    @Column(name = "rule_rcvd")
    private Long ruleRcvd;

    @Column(name = "wanted_for")
    private String wantedFor;

    @Column(name = "fine")
    private Double fine;

    @Column(name = "license_issue")
    private String licenseIssue;

    @Column(name = "wanted_by")
    private Long wantedBy;

    @Column(name = "rule_processed")
    private Long ruleProcessed;

    @Column(name = "speed_fine")
    private Double speedFine;

    @NotNull
    @Column(name = "lane", nullable = false)
    private Long lane;

    @Column(name = "tag_issue")
    private String tagIssue;

    @Column(name = "status_name")
    private String statusName;

    @Column(name = "license_fine")
    private Double licenseFine;

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

    @Column(name = "processing_time")
    private Long processingTime;

    @Column(name = "thread_rand_no")
    private String threadRandNo;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Task id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getGuid() {
        return this.guid;
    }

    public Task guid(String guid) {
        this.setGuid(guid);
        return this;
    }

    public void setGuid(String guid) {
        this.guid = guid;
    }

    public String getPlate() {
        return this.plate;
    }

    public Task plate(String plate) {
        this.setPlate(plate);
        return this;
    }

    public void setPlate(String plate) {
        this.plate = plate;
    }

    public byte[] getImageLp() {
        return this.imageLp;
    }

    public Task imageLp(byte[] imageLp) {
        this.setImageLp(imageLp);
        return this;
    }

    public void setImageLp(byte[] imageLp) {
        this.imageLp = imageLp;
    }

    public String getImageLpContentType() {
        return this.imageLpContentType;
    }

    public Task imageLpContentType(String imageLpContentType) {
        this.imageLpContentType = imageLpContentType;
        return this;
    }

    public void setImageLpContentType(String imageLpContentType) {
        this.imageLpContentType = imageLpContentType;
    }

    public byte[] getImageThumb() {
        return this.imageThumb;
    }

    public Task imageThumb(byte[] imageThumb) {
        this.setImageThumb(imageThumb);
        return this;
    }

    public void setImageThumb(byte[] imageThumb) {
        this.imageThumb = imageThumb;
    }

    public String getImageThumbContentType() {
        return this.imageThumbContentType;
    }

    public Task imageThumbContentType(String imageThumbContentType) {
        this.imageThumbContentType = imageThumbContentType;
        return this;
    }

    public void setImageThumbContentType(String imageThumbContentType) {
        this.imageThumbContentType = imageThumbContentType;
    }

    public String getAnpr() {
        return this.anpr;
    }

    public Task anpr(String anpr) {
        this.setAnpr(anpr);
        return this;
    }

    public void setAnpr(String anpr) {
        this.anpr = anpr;
    }

    public String getRfid() {
        return this.rfid;
    }

    public Task rfid(String rfid) {
        this.setRfid(rfid);
        return this;
    }

    public void setRfid(String rfid) {
        this.rfid = rfid;
    }

    public String getDataStatus() {
        return this.dataStatus;
    }

    public Task dataStatus(String dataStatus) {
        this.setDataStatus(dataStatus);
        return this;
    }

    public void setDataStatus(String dataStatus) {
        this.dataStatus = dataStatus;
    }

    public Long getGantry() {
        return this.gantry;
    }

    public Task gantry(Long gantry) {
        this.setGantry(gantry);
        return this;
    }

    public void setGantry(Long gantry) {
        this.gantry = gantry;
    }

    public Long getKph() {
        return this.kph;
    }

    public Task kph(Long kph) {
        this.setKph(kph);
        return this;
    }

    public void setKph(Long kph) {
        this.kph = kph;
    }

    public Long getAmbush() {
        return this.ambush;
    }

    public Task ambush(Long ambush) {
        this.setAmbush(ambush);
        return this;
    }

    public void setAmbush(Long ambush) {
        this.ambush = ambush;
    }

    public Long getDirection() {
        return this.direction;
    }

    public Task direction(Long direction) {
        this.setDirection(direction);
        return this;
    }

    public void setDirection(Long direction) {
        this.direction = direction;
    }

    public Long getVehicle() {
        return this.vehicle;
    }

    public Task vehicle(Long vehicle) {
        this.setVehicle(vehicle);
        return this;
    }

    public void setVehicle(Long vehicle) {
        this.vehicle = vehicle;
    }

    public String getIssue() {
        return this.issue;
    }

    public Task issue(String issue) {
        this.setIssue(issue);
        return this;
    }

    public void setIssue(String issue) {
        this.issue = issue;
    }

    public String getStatus() {
        return this.status;
    }

    public Task status(String status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Long getHandledBy() {
        return this.handledBy;
    }

    public Task handledBy(Long handledBy) {
        this.setHandledBy(handledBy);
        return this;
    }

    public void setHandledBy(Long handledBy) {
        this.handledBy = handledBy;
    }

    public Long getGantryProcessed() {
        return this.gantryProcessed;
    }

    public Task gantryProcessed(Long gantryProcessed) {
        this.setGantryProcessed(gantryProcessed);
        return this;
    }

    public void setGantryProcessed(Long gantryProcessed) {
        this.gantryProcessed = gantryProcessed;
    }

    public Long getGantrySent() {
        return this.gantrySent;
    }

    public Task gantrySent(Long gantrySent) {
        this.setGantrySent(gantrySent);
        return this;
    }

    public void setGantrySent(Long gantrySent) {
        this.gantrySent = gantrySent;
    }

    public Long getWhen() {
        return this.when;
    }

    public Task when(Long when) {
        this.setWhen(when);
        return this;
    }

    public void setWhen(Long when) {
        this.when = when;
    }

    public Double getToll() {
        return this.toll;
    }

    public Task toll(Double toll) {
        this.setToll(toll);
        return this;
    }

    public void setToll(Double toll) {
        this.toll = toll;
    }

    public Long getRuleRcvd() {
        return this.ruleRcvd;
    }

    public Task ruleRcvd(Long ruleRcvd) {
        this.setRuleRcvd(ruleRcvd);
        return this;
    }

    public void setRuleRcvd(Long ruleRcvd) {
        this.ruleRcvd = ruleRcvd;
    }

    public String getWantedFor() {
        return this.wantedFor;
    }

    public Task wantedFor(String wantedFor) {
        this.setWantedFor(wantedFor);
        return this;
    }

    public void setWantedFor(String wantedFor) {
        this.wantedFor = wantedFor;
    }

    public Double getFine() {
        return this.fine;
    }

    public Task fine(Double fine) {
        this.setFine(fine);
        return this;
    }

    public void setFine(Double fine) {
        this.fine = fine;
    }

    public String getLicenseIssue() {
        return this.licenseIssue;
    }

    public Task licenseIssue(String licenseIssue) {
        this.setLicenseIssue(licenseIssue);
        return this;
    }

    public void setLicenseIssue(String licenseIssue) {
        this.licenseIssue = licenseIssue;
    }

    public Long getWantedBy() {
        return this.wantedBy;
    }

    public Task wantedBy(Long wantedBy) {
        this.setWantedBy(wantedBy);
        return this;
    }

    public void setWantedBy(Long wantedBy) {
        this.wantedBy = wantedBy;
    }

    public Long getRuleProcessed() {
        return this.ruleProcessed;
    }

    public Task ruleProcessed(Long ruleProcessed) {
        this.setRuleProcessed(ruleProcessed);
        return this;
    }

    public void setRuleProcessed(Long ruleProcessed) {
        this.ruleProcessed = ruleProcessed;
    }

    public Double getSpeedFine() {
        return this.speedFine;
    }

    public Task speedFine(Double speedFine) {
        this.setSpeedFine(speedFine);
        return this;
    }

    public void setSpeedFine(Double speedFine) {
        this.speedFine = speedFine;
    }

    public Long getLane() {
        return this.lane;
    }

    public Task lane(Long lane) {
        this.setLane(lane);
        return this;
    }

    public void setLane(Long lane) {
        this.lane = lane;
    }

    public String getTagIssue() {
        return this.tagIssue;
    }

    public Task tagIssue(String tagIssue) {
        this.setTagIssue(tagIssue);
        return this;
    }

    public void setTagIssue(String tagIssue) {
        this.tagIssue = tagIssue;
    }

    public String getStatusName() {
        return this.statusName;
    }

    public Task statusName(String statusName) {
        this.setStatusName(statusName);
        return this;
    }

    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }

    public Double getLicenseFine() {
        return this.licenseFine;
    }

    public Task licenseFine(Double licenseFine) {
        this.setLicenseFine(licenseFine);
        return this;
    }

    public void setLicenseFine(Double licenseFine) {
        this.licenseFine = licenseFine;
    }

    public Long getStolen() {
        return this.stolen;
    }

    public Task stolen(Long stolen) {
        this.setStolen(stolen);
        return this;
    }

    public void setStolen(Long stolen) {
        this.stolen = stolen;
    }

    public Boolean getWanted() {
        return this.wanted;
    }

    public Task wanted(Boolean wanted) {
        this.setWanted(wanted);
        return this;
    }

    public void setWanted(Boolean wanted) {
        this.wanted = wanted;
    }

    public Long getRuleSent() {
        return this.ruleSent;
    }

    public Task ruleSent(Long ruleSent) {
        this.setRuleSent(ruleSent);
        return this;
    }

    public void setRuleSent(Long ruleSent) {
        this.ruleSent = ruleSent;
    }

    public Long getHandled() {
        return this.handled;
    }

    public Task handled(Long handled) {
        this.setHandled(handled);
        return this;
    }

    public void setHandled(Long handled) {
        this.handled = handled;
    }

    public String getRuleIssue() {
        return this.ruleIssue;
    }

    public Task ruleIssue(String ruleIssue) {
        this.setRuleIssue(ruleIssue);
        return this;
    }

    public void setRuleIssue(String ruleIssue) {
        this.ruleIssue = ruleIssue;
    }

    public Long getProcessingTime() {
        return this.processingTime;
    }

    public Task processingTime(Long processingTime) {
        this.setProcessingTime(processingTime);
        return this;
    }

    public void setProcessingTime(Long processingTime) {
        this.processingTime = processingTime;
    }

    public String getThreadRandNo() {
        return this.threadRandNo;
    }

    public Task threadRandNo(String threadRandNo) {
        this.setThreadRandNo(threadRandNo);
        return this;
    }

    public void setThreadRandNo(String threadRandNo) {
        this.threadRandNo = threadRandNo;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Task)) {
            return false;
        }
        return id != null && id.equals(((Task) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Task{" +
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
