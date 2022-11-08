package com.isoft.rfid.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.api.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.isoft.rfid.domain.Events} entity. This class is used
 * in {@link com.isoft.rfid.web.rest.EventsResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /events?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class EventsCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private UUIDFilter guid;

    private StringFilter plate;

    private StringFilter anpr;

    private StringFilter rfid;

    private IntegerFilter gantry;

    private StringFilter wantedFor;

    private StringFilter licenseIssue;

    private StringFilter issue;

    private StringFilter tagIssue;

    private StringFilter statusName;

    private IntegerFilter lane;

    private IntegerFilter direction;

    private IntegerFilter kph;

    private IntegerFilter ambush;

    private DoubleFilter toll;

    private DoubleFilter fine;

    private DoubleFilter wantedBy;

    private DoubleFilter licenseFine;

    private DoubleFilter speedFine;

    private LongFilter handled;

    private LongFilter processingTime;

    private LongFilter ruleRcvd;

    private LongFilter ruleIssue;

    private LongFilter ruleProcessed;

    private LongFilter ruleSent;

    private LongFilter when;

    private LongFilter vehicle;

    private LongFilter stolen;

    private BooleanFilter wanted;

    private LongFilter gantryProcessed;

    private LongFilter gantrySent;

    private StringFilter status;

    private StringFilter dataStatus;

    private StringFilter threadRandNo;

    private StringFilter handledBy;

    private Boolean distinct;

    public EventsCriteria() {}

    public EventsCriteria(EventsCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.guid = other.guid == null ? null : other.guid.copy();
        this.plate = other.plate == null ? null : other.plate.copy();
        this.anpr = other.anpr == null ? null : other.anpr.copy();
        this.rfid = other.rfid == null ? null : other.rfid.copy();
        this.gantry = other.gantry == null ? null : other.gantry.copy();
        this.wantedFor = other.wantedFor == null ? null : other.wantedFor.copy();
        this.licenseIssue = other.licenseIssue == null ? null : other.licenseIssue.copy();
        this.issue = other.issue == null ? null : other.issue.copy();
        this.tagIssue = other.tagIssue == null ? null : other.tagIssue.copy();
        this.statusName = other.statusName == null ? null : other.statusName.copy();
        this.lane = other.lane == null ? null : other.lane.copy();
        this.direction = other.direction == null ? null : other.direction.copy();
        this.kph = other.kph == null ? null : other.kph.copy();
        this.ambush = other.ambush == null ? null : other.ambush.copy();
        this.toll = other.toll == null ? null : other.toll.copy();
        this.fine = other.fine == null ? null : other.fine.copy();
        this.wantedBy = other.wantedBy == null ? null : other.wantedBy.copy();
        this.licenseFine = other.licenseFine == null ? null : other.licenseFine.copy();
        this.speedFine = other.speedFine == null ? null : other.speedFine.copy();
        this.handled = other.handled == null ? null : other.handled.copy();
        this.processingTime = other.processingTime == null ? null : other.processingTime.copy();
        this.ruleRcvd = other.ruleRcvd == null ? null : other.ruleRcvd.copy();
        this.ruleIssue = other.ruleIssue == null ? null : other.ruleIssue.copy();
        this.ruleProcessed = other.ruleProcessed == null ? null : other.ruleProcessed.copy();
        this.ruleSent = other.ruleSent == null ? null : other.ruleSent.copy();
        this.when = other.when == null ? null : other.when.copy();
        this.vehicle = other.vehicle == null ? null : other.vehicle.copy();
        this.stolen = other.stolen == null ? null : other.stolen.copy();
        this.wanted = other.wanted == null ? null : other.wanted.copy();
        this.gantryProcessed = other.gantryProcessed == null ? null : other.gantryProcessed.copy();
        this.gantrySent = other.gantrySent == null ? null : other.gantrySent.copy();
        this.status = other.status == null ? null : other.status.copy();
        this.dataStatus = other.dataStatus == null ? null : other.dataStatus.copy();
        this.threadRandNo = other.threadRandNo == null ? null : other.threadRandNo.copy();
        this.handledBy = other.handledBy == null ? null : other.handledBy.copy();
        this.distinct = other.distinct;
    }

    @Override
    public EventsCriteria copy() {
        return new EventsCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public LongFilter id() {
        if (id == null) {
            id = new LongFilter();
        }
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public UUIDFilter getGuid() {
        return guid;
    }

    public UUIDFilter guid() {
        if (guid == null) {
            guid = new UUIDFilter();
        }
        return guid;
    }

    public void setGuid(UUIDFilter guid) {
        this.guid = guid;
    }

    public StringFilter getPlate() {
        return plate;
    }

    public StringFilter plate() {
        if (plate == null) {
            plate = new StringFilter();
        }
        return plate;
    }

    public void setPlate(StringFilter plate) {
        this.plate = plate;
    }

    public StringFilter getAnpr() {
        return anpr;
    }

    public StringFilter anpr() {
        if (anpr == null) {
            anpr = new StringFilter();
        }
        return anpr;
    }

    public void setAnpr(StringFilter anpr) {
        this.anpr = anpr;
    }

    public StringFilter getRfid() {
        return rfid;
    }

    public StringFilter rfid() {
        if (rfid == null) {
            rfid = new StringFilter();
        }
        return rfid;
    }

    public void setRfid(StringFilter rfid) {
        this.rfid = rfid;
    }

    public IntegerFilter getGantry() {
        return gantry;
    }

    public IntegerFilter gantry() {
        if (gantry == null) {
            gantry = new IntegerFilter();
        }
        return gantry;
    }

    public void setGantry(IntegerFilter gantry) {
        this.gantry = gantry;
    }

    public StringFilter getWantedFor() {
        return wantedFor;
    }

    public StringFilter wantedFor() {
        if (wantedFor == null) {
            wantedFor = new StringFilter();
        }
        return wantedFor;
    }

    public void setWantedFor(StringFilter wantedFor) {
        this.wantedFor = wantedFor;
    }

    public StringFilter getLicenseIssue() {
        return licenseIssue;
    }

    public StringFilter licenseIssue() {
        if (licenseIssue == null) {
            licenseIssue = new StringFilter();
        }
        return licenseIssue;
    }

    public void setLicenseIssue(StringFilter licenseIssue) {
        this.licenseIssue = licenseIssue;
    }

    public StringFilter getIssue() {
        return issue;
    }

    public StringFilter issue() {
        if (issue == null) {
            issue = new StringFilter();
        }
        return issue;
    }

    public void setIssue(StringFilter issue) {
        this.issue = issue;
    }

    public StringFilter getTagIssue() {
        return tagIssue;
    }

    public StringFilter tagIssue() {
        if (tagIssue == null) {
            tagIssue = new StringFilter();
        }
        return tagIssue;
    }

    public void setTagIssue(StringFilter tagIssue) {
        this.tagIssue = tagIssue;
    }

    public StringFilter getStatusName() {
        return statusName;
    }

    public StringFilter statusName() {
        if (statusName == null) {
            statusName = new StringFilter();
        }
        return statusName;
    }

    public void setStatusName(StringFilter statusName) {
        this.statusName = statusName;
    }

    public IntegerFilter getLane() {
        return lane;
    }

    public IntegerFilter lane() {
        if (lane == null) {
            lane = new IntegerFilter();
        }
        return lane;
    }

    public void setLane(IntegerFilter lane) {
        this.lane = lane;
    }

    public IntegerFilter getDirection() {
        return direction;
    }

    public IntegerFilter direction() {
        if (direction == null) {
            direction = new IntegerFilter();
        }
        return direction;
    }

    public void setDirection(IntegerFilter direction) {
        this.direction = direction;
    }

    public IntegerFilter getKph() {
        return kph;
    }

    public IntegerFilter kph() {
        if (kph == null) {
            kph = new IntegerFilter();
        }
        return kph;
    }

    public void setKph(IntegerFilter kph) {
        this.kph = kph;
    }

    public IntegerFilter getAmbush() {
        return ambush;
    }

    public IntegerFilter ambush() {
        if (ambush == null) {
            ambush = new IntegerFilter();
        }
        return ambush;
    }

    public void setAmbush(IntegerFilter ambush) {
        this.ambush = ambush;
    }

    public DoubleFilter getToll() {
        return toll;
    }

    public DoubleFilter toll() {
        if (toll == null) {
            toll = new DoubleFilter();
        }
        return toll;
    }

    public void setToll(DoubleFilter toll) {
        this.toll = toll;
    }

    public DoubleFilter getFine() {
        return fine;
    }

    public DoubleFilter fine() {
        if (fine == null) {
            fine = new DoubleFilter();
        }
        return fine;
    }

    public void setFine(DoubleFilter fine) {
        this.fine = fine;
    }

    public DoubleFilter getWantedBy() {
        return wantedBy;
    }

    public DoubleFilter wantedBy() {
        if (wantedBy == null) {
            wantedBy = new DoubleFilter();
        }
        return wantedBy;
    }

    public void setWantedBy(DoubleFilter wantedBy) {
        this.wantedBy = wantedBy;
    }

    public DoubleFilter getLicenseFine() {
        return licenseFine;
    }

    public DoubleFilter licenseFine() {
        if (licenseFine == null) {
            licenseFine = new DoubleFilter();
        }
        return licenseFine;
    }

    public void setLicenseFine(DoubleFilter licenseFine) {
        this.licenseFine = licenseFine;
    }

    public DoubleFilter getSpeedFine() {
        return speedFine;
    }

    public DoubleFilter speedFine() {
        if (speedFine == null) {
            speedFine = new DoubleFilter();
        }
        return speedFine;
    }

    public void setSpeedFine(DoubleFilter speedFine) {
        this.speedFine = speedFine;
    }

    public LongFilter getHandled() {
        return handled;
    }

    public LongFilter handled() {
        if (handled == null) {
            handled = new LongFilter();
        }
        return handled;
    }

    public void setHandled(LongFilter handled) {
        this.handled = handled;
    }

    public LongFilter getProcessingTime() {
        return processingTime;
    }

    public LongFilter processingTime() {
        if (processingTime == null) {
            processingTime = new LongFilter();
        }
        return processingTime;
    }

    public void setProcessingTime(LongFilter processingTime) {
        this.processingTime = processingTime;
    }

    public LongFilter getRuleRcvd() {
        return ruleRcvd;
    }

    public LongFilter ruleRcvd() {
        if (ruleRcvd == null) {
            ruleRcvd = new LongFilter();
        }
        return ruleRcvd;
    }

    public void setRuleRcvd(LongFilter ruleRcvd) {
        this.ruleRcvd = ruleRcvd;
    }

    public LongFilter getRuleIssue() {
        return ruleIssue;
    }

    public LongFilter ruleIssue() {
        if (ruleIssue == null) {
            ruleIssue = new LongFilter();
        }
        return ruleIssue;
    }

    public void setRuleIssue(LongFilter ruleIssue) {
        this.ruleIssue = ruleIssue;
    }

    public LongFilter getRuleProcessed() {
        return ruleProcessed;
    }

    public LongFilter ruleProcessed() {
        if (ruleProcessed == null) {
            ruleProcessed = new LongFilter();
        }
        return ruleProcessed;
    }

    public void setRuleProcessed(LongFilter ruleProcessed) {
        this.ruleProcessed = ruleProcessed;
    }

    public LongFilter getRuleSent() {
        return ruleSent;
    }

    public LongFilter ruleSent() {
        if (ruleSent == null) {
            ruleSent = new LongFilter();
        }
        return ruleSent;
    }

    public void setRuleSent(LongFilter ruleSent) {
        this.ruleSent = ruleSent;
    }

    public LongFilter getWhen() {
        return when;
    }

    public LongFilter when() {
        if (when == null) {
            when = new LongFilter();
        }
        return when;
    }

    public void setWhen(LongFilter when) {
        this.when = when;
    }

    public LongFilter getVehicle() {
        return vehicle;
    }

    public LongFilter vehicle() {
        if (vehicle == null) {
            vehicle = new LongFilter();
        }
        return vehicle;
    }

    public void setVehicle(LongFilter vehicle) {
        this.vehicle = vehicle;
    }

    public LongFilter getStolen() {
        return stolen;
    }

    public LongFilter stolen() {
        if (stolen == null) {
            stolen = new LongFilter();
        }
        return stolen;
    }

    public void setStolen(LongFilter stolen) {
        this.stolen = stolen;
    }

    public BooleanFilter getWanted() {
        return wanted;
    }

    public BooleanFilter wanted() {
        if (wanted == null) {
            wanted = new BooleanFilter();
        }
        return wanted;
    }

    public void setWanted(BooleanFilter wanted) {
        this.wanted = wanted;
    }

    public LongFilter getGantryProcessed() {
        return gantryProcessed;
    }

    public LongFilter gantryProcessed() {
        if (gantryProcessed == null) {
            gantryProcessed = new LongFilter();
        }
        return gantryProcessed;
    }

    public void setGantryProcessed(LongFilter gantryProcessed) {
        this.gantryProcessed = gantryProcessed;
    }

    public LongFilter getGantrySent() {
        return gantrySent;
    }

    public LongFilter gantrySent() {
        if (gantrySent == null) {
            gantrySent = new LongFilter();
        }
        return gantrySent;
    }

    public void setGantrySent(LongFilter gantrySent) {
        this.gantrySent = gantrySent;
    }

    public StringFilter getStatus() {
        return status;
    }

    public StringFilter status() {
        if (status == null) {
            status = new StringFilter();
        }
        return status;
    }

    public void setStatus(StringFilter status) {
        this.status = status;
    }

    public StringFilter getDataStatus() {
        return dataStatus;
    }

    public StringFilter dataStatus() {
        if (dataStatus == null) {
            dataStatus = new StringFilter();
        }
        return dataStatus;
    }

    public void setDataStatus(StringFilter dataStatus) {
        this.dataStatus = dataStatus;
    }

    public StringFilter getThreadRandNo() {
        return threadRandNo;
    }

    public StringFilter threadRandNo() {
        if (threadRandNo == null) {
            threadRandNo = new StringFilter();
        }
        return threadRandNo;
    }

    public void setThreadRandNo(StringFilter threadRandNo) {
        this.threadRandNo = threadRandNo;
    }

    public StringFilter getHandledBy() {
        return handledBy;
    }

    public StringFilter handledBy() {
        if (handledBy == null) {
            handledBy = new StringFilter();
        }
        return handledBy;
    }

    public void setHandledBy(StringFilter handledBy) {
        this.handledBy = handledBy;
    }

    public Boolean getDistinct() {
        return distinct;
    }

    public void setDistinct(Boolean distinct) {
        this.distinct = distinct;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final EventsCriteria that = (EventsCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(guid, that.guid) &&
            Objects.equals(plate, that.plate) &&
            Objects.equals(anpr, that.anpr) &&
            Objects.equals(rfid, that.rfid) &&
            Objects.equals(gantry, that.gantry) &&
            Objects.equals(wantedFor, that.wantedFor) &&
            Objects.equals(licenseIssue, that.licenseIssue) &&
            Objects.equals(issue, that.issue) &&
            Objects.equals(tagIssue, that.tagIssue) &&
            Objects.equals(statusName, that.statusName) &&
            Objects.equals(lane, that.lane) &&
            Objects.equals(direction, that.direction) &&
            Objects.equals(kph, that.kph) &&
            Objects.equals(ambush, that.ambush) &&
            Objects.equals(toll, that.toll) &&
            Objects.equals(fine, that.fine) &&
            Objects.equals(wantedBy, that.wantedBy) &&
            Objects.equals(licenseFine, that.licenseFine) &&
            Objects.equals(speedFine, that.speedFine) &&
            Objects.equals(handled, that.handled) &&
            Objects.equals(processingTime, that.processingTime) &&
            Objects.equals(ruleRcvd, that.ruleRcvd) &&
            Objects.equals(ruleIssue, that.ruleIssue) &&
            Objects.equals(ruleProcessed, that.ruleProcessed) &&
            Objects.equals(ruleSent, that.ruleSent) &&
            Objects.equals(when, that.when) &&
            Objects.equals(vehicle, that.vehicle) &&
            Objects.equals(stolen, that.stolen) &&
            Objects.equals(wanted, that.wanted) &&
            Objects.equals(gantryProcessed, that.gantryProcessed) &&
            Objects.equals(gantrySent, that.gantrySent) &&
            Objects.equals(status, that.status) &&
            Objects.equals(dataStatus, that.dataStatus) &&
            Objects.equals(threadRandNo, that.threadRandNo) &&
            Objects.equals(handledBy, that.handledBy) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            guid,
            plate,
            anpr,
            rfid,
            gantry,
            wantedFor,
            licenseIssue,
            issue,
            tagIssue,
            statusName,
            lane,
            direction,
            kph,
            ambush,
            toll,
            fine,
            wantedBy,
            licenseFine,
            speedFine,
            handled,
            processingTime,
            ruleRcvd,
            ruleIssue,
            ruleProcessed,
            ruleSent,
            when,
            vehicle,
            stolen,
            wanted,
            gantryProcessed,
            gantrySent,
            status,
            dataStatus,
            threadRandNo,
            handledBy,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "EventsCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (guid != null ? "guid=" + guid + ", " : "") +
            (plate != null ? "plate=" + plate + ", " : "") +
            (anpr != null ? "anpr=" + anpr + ", " : "") +
            (rfid != null ? "rfid=" + rfid + ", " : "") +
            (gantry != null ? "gantry=" + gantry + ", " : "") +
            (wantedFor != null ? "wantedFor=" + wantedFor + ", " : "") +
            (licenseIssue != null ? "licenseIssue=" + licenseIssue + ", " : "") +
            (issue != null ? "issue=" + issue + ", " : "") +
            (tagIssue != null ? "tagIssue=" + tagIssue + ", " : "") +
            (statusName != null ? "statusName=" + statusName + ", " : "") +
            (lane != null ? "lane=" + lane + ", " : "") +
            (direction != null ? "direction=" + direction + ", " : "") +
            (kph != null ? "kph=" + kph + ", " : "") +
            (ambush != null ? "ambush=" + ambush + ", " : "") +
            (toll != null ? "toll=" + toll + ", " : "") +
            (fine != null ? "fine=" + fine + ", " : "") +
            (wantedBy != null ? "wantedBy=" + wantedBy + ", " : "") +
            (licenseFine != null ? "licenseFine=" + licenseFine + ", " : "") +
            (speedFine != null ? "speedFine=" + speedFine + ", " : "") +
            (handled != null ? "handled=" + handled + ", " : "") +
            (processingTime != null ? "processingTime=" + processingTime + ", " : "") +
            (ruleRcvd != null ? "ruleRcvd=" + ruleRcvd + ", " : "") +
            (ruleIssue != null ? "ruleIssue=" + ruleIssue + ", " : "") +
            (ruleProcessed != null ? "ruleProcessed=" + ruleProcessed + ", " : "") +
            (ruleSent != null ? "ruleSent=" + ruleSent + ", " : "") +
            (when != null ? "when=" + when + ", " : "") +
            (vehicle != null ? "vehicle=" + vehicle + ", " : "") +
            (stolen != null ? "stolen=" + stolen + ", " : "") +
            (wanted != null ? "wanted=" + wanted + ", " : "") +
            (gantryProcessed != null ? "gantryProcessed=" + gantryProcessed + ", " : "") +
            (gantrySent != null ? "gantrySent=" + gantrySent + ", " : "") +
            (status != null ? "status=" + status + ", " : "") +
            (dataStatus != null ? "dataStatus=" + dataStatus + ", " : "") +
            (threadRandNo != null ? "threadRandNo=" + threadRandNo + ", " : "") +
            (handledBy != null ? "handledBy=" + handledBy + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
