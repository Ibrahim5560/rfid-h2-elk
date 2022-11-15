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

    private StringFilter guid;

    private StringFilter plate;

    private StringFilter anpr;

    private StringFilter rfid;

    private StringFilter dataStatus;

    private LongFilter gantry;

    private LongFilter kph;

    private LongFilter ambush;

    private LongFilter direction;

    private LongFilter vehicle;

    private StringFilter issue;

    private StringFilter status;

    private LongFilter handledBy;

    private LongFilter gantryProcessed;

    private LongFilter gantrySent;

    private LongFilter when;

    private LongFilter toll;

    private LongFilter ruleRcvd;

    private StringFilter wantedFor;

    private LongFilter fine;

    private StringFilter licenseIssue;

    private LongFilter wantedBy;

    private LongFilter ruleProcessed;

    private LongFilter speedFine;

    private LongFilter lane;

    private StringFilter tagIssue;

    private StringFilter statusName;

    private LongFilter licenseFine;

    private LongFilter stolen;

    private BooleanFilter wanted;

    private LongFilter ruleSent;

    private LongFilter handled;

    private StringFilter ruleIssue;

    private Boolean distinct;

    public EventsCriteria() {}

    public EventsCriteria(EventsCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.guid = other.guid == null ? null : other.guid.copy();
        this.plate = other.plate == null ? null : other.plate.copy();
        this.anpr = other.anpr == null ? null : other.anpr.copy();
        this.rfid = other.rfid == null ? null : other.rfid.copy();
        this.dataStatus = other.dataStatus == null ? null : other.dataStatus.copy();
        this.gantry = other.gantry == null ? null : other.gantry.copy();
        this.kph = other.kph == null ? null : other.kph.copy();
        this.ambush = other.ambush == null ? null : other.ambush.copy();
        this.direction = other.direction == null ? null : other.direction.copy();
        this.vehicle = other.vehicle == null ? null : other.vehicle.copy();
        this.issue = other.issue == null ? null : other.issue.copy();
        this.status = other.status == null ? null : other.status.copy();
        this.handledBy = other.handledBy == null ? null : other.handledBy.copy();
        this.gantryProcessed = other.gantryProcessed == null ? null : other.gantryProcessed.copy();
        this.gantrySent = other.gantrySent == null ? null : other.gantrySent.copy();
        this.when = other.when == null ? null : other.when.copy();
        this.toll = other.toll == null ? null : other.toll.copy();
        this.ruleRcvd = other.ruleRcvd == null ? null : other.ruleRcvd.copy();
        this.wantedFor = other.wantedFor == null ? null : other.wantedFor.copy();
        this.fine = other.fine == null ? null : other.fine.copy();
        this.licenseIssue = other.licenseIssue == null ? null : other.licenseIssue.copy();
        this.wantedBy = other.wantedBy == null ? null : other.wantedBy.copy();
        this.ruleProcessed = other.ruleProcessed == null ? null : other.ruleProcessed.copy();
        this.speedFine = other.speedFine == null ? null : other.speedFine.copy();
        this.lane = other.lane == null ? null : other.lane.copy();
        this.tagIssue = other.tagIssue == null ? null : other.tagIssue.copy();
        this.statusName = other.statusName == null ? null : other.statusName.copy();
        this.licenseFine = other.licenseFine == null ? null : other.licenseFine.copy();
        this.stolen = other.stolen == null ? null : other.stolen.copy();
        this.wanted = other.wanted == null ? null : other.wanted.copy();
        this.ruleSent = other.ruleSent == null ? null : other.ruleSent.copy();
        this.handled = other.handled == null ? null : other.handled.copy();
        this.ruleIssue = other.ruleIssue == null ? null : other.ruleIssue.copy();
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

    public StringFilter getGuid() {
        return guid;
    }

    public StringFilter guid() {
        if (guid == null) {
            guid = new StringFilter();
        }
        return guid;
    }

    public void setGuid(StringFilter guid) {
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

    public LongFilter getGantry() {
        return gantry;
    }

    public LongFilter gantry() {
        if (gantry == null) {
            gantry = new LongFilter();
        }
        return gantry;
    }

    public void setGantry(LongFilter gantry) {
        this.gantry = gantry;
    }

    public LongFilter getKph() {
        return kph;
    }

    public LongFilter kph() {
        if (kph == null) {
            kph = new LongFilter();
        }
        return kph;
    }

    public void setKph(LongFilter kph) {
        this.kph = kph;
    }

    public LongFilter getAmbush() {
        return ambush;
    }

    public LongFilter ambush() {
        if (ambush == null) {
            ambush = new LongFilter();
        }
        return ambush;
    }

    public void setAmbush(LongFilter ambush) {
        this.ambush = ambush;
    }

    public LongFilter getDirection() {
        return direction;
    }

    public LongFilter direction() {
        if (direction == null) {
            direction = new LongFilter();
        }
        return direction;
    }

    public void setDirection(LongFilter direction) {
        this.direction = direction;
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

    public LongFilter getHandledBy() {
        return handledBy;
    }

    public LongFilter handledBy() {
        if (handledBy == null) {
            handledBy = new LongFilter();
        }
        return handledBy;
    }

    public void setHandledBy(LongFilter handledBy) {
        this.handledBy = handledBy;
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

    public LongFilter getToll() {
        return toll;
    }

    public LongFilter toll() {
        if (toll == null) {
            toll = new LongFilter();
        }
        return toll;
    }

    public void setToll(LongFilter toll) {
        this.toll = toll;
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

    public LongFilter getFine() {
        return fine;
    }

    public LongFilter fine() {
        if (fine == null) {
            fine = new LongFilter();
        }
        return fine;
    }

    public void setFine(LongFilter fine) {
        this.fine = fine;
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

    public LongFilter getWantedBy() {
        return wantedBy;
    }

    public LongFilter wantedBy() {
        if (wantedBy == null) {
            wantedBy = new LongFilter();
        }
        return wantedBy;
    }

    public void setWantedBy(LongFilter wantedBy) {
        this.wantedBy = wantedBy;
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

    public LongFilter getSpeedFine() {
        return speedFine;
    }

    public LongFilter speedFine() {
        if (speedFine == null) {
            speedFine = new LongFilter();
        }
        return speedFine;
    }

    public void setSpeedFine(LongFilter speedFine) {
        this.speedFine = speedFine;
    }

    public LongFilter getLane() {
        return lane;
    }

    public LongFilter lane() {
        if (lane == null) {
            lane = new LongFilter();
        }
        return lane;
    }

    public void setLane(LongFilter lane) {
        this.lane = lane;
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

    public LongFilter getLicenseFine() {
        return licenseFine;
    }

    public LongFilter licenseFine() {
        if (licenseFine == null) {
            licenseFine = new LongFilter();
        }
        return licenseFine;
    }

    public void setLicenseFine(LongFilter licenseFine) {
        this.licenseFine = licenseFine;
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

    public StringFilter getRuleIssue() {
        return ruleIssue;
    }

    public StringFilter ruleIssue() {
        if (ruleIssue == null) {
            ruleIssue = new StringFilter();
        }
        return ruleIssue;
    }

    public void setRuleIssue(StringFilter ruleIssue) {
        this.ruleIssue = ruleIssue;
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
            Objects.equals(dataStatus, that.dataStatus) &&
            Objects.equals(gantry, that.gantry) &&
            Objects.equals(kph, that.kph) &&
            Objects.equals(ambush, that.ambush) &&
            Objects.equals(direction, that.direction) &&
            Objects.equals(vehicle, that.vehicle) &&
            Objects.equals(issue, that.issue) &&
            Objects.equals(status, that.status) &&
            Objects.equals(handledBy, that.handledBy) &&
            Objects.equals(gantryProcessed, that.gantryProcessed) &&
            Objects.equals(gantrySent, that.gantrySent) &&
            Objects.equals(when, that.when) &&
            Objects.equals(toll, that.toll) &&
            Objects.equals(ruleRcvd, that.ruleRcvd) &&
            Objects.equals(wantedFor, that.wantedFor) &&
            Objects.equals(fine, that.fine) &&
            Objects.equals(licenseIssue, that.licenseIssue) &&
            Objects.equals(wantedBy, that.wantedBy) &&
            Objects.equals(ruleProcessed, that.ruleProcessed) &&
            Objects.equals(speedFine, that.speedFine) &&
            Objects.equals(lane, that.lane) &&
            Objects.equals(tagIssue, that.tagIssue) &&
            Objects.equals(statusName, that.statusName) &&
            Objects.equals(licenseFine, that.licenseFine) &&
            Objects.equals(stolen, that.stolen) &&
            Objects.equals(wanted, that.wanted) &&
            Objects.equals(ruleSent, that.ruleSent) &&
            Objects.equals(handled, that.handled) &&
            Objects.equals(ruleIssue, that.ruleIssue) &&
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
            dataStatus,
            gantry,
            kph,
            ambush,
            direction,
            vehicle,
            issue,
            status,
            handledBy,
            gantryProcessed,
            gantrySent,
            when,
            toll,
            ruleRcvd,
            wantedFor,
            fine,
            licenseIssue,
            wantedBy,
            ruleProcessed,
            speedFine,
            lane,
            tagIssue,
            statusName,
            licenseFine,
            stolen,
            wanted,
            ruleSent,
            handled,
            ruleIssue,
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
            (dataStatus != null ? "dataStatus=" + dataStatus + ", " : "") +
            (gantry != null ? "gantry=" + gantry + ", " : "") +
            (kph != null ? "kph=" + kph + ", " : "") +
            (ambush != null ? "ambush=" + ambush + ", " : "") +
            (direction != null ? "direction=" + direction + ", " : "") +
            (vehicle != null ? "vehicle=" + vehicle + ", " : "") +
            (issue != null ? "issue=" + issue + ", " : "") +
            (status != null ? "status=" + status + ", " : "") +
            (handledBy != null ? "handledBy=" + handledBy + ", " : "") +
            (gantryProcessed != null ? "gantryProcessed=" + gantryProcessed + ", " : "") +
            (gantrySent != null ? "gantrySent=" + gantrySent + ", " : "") +
            (when != null ? "when=" + when + ", " : "") +
            (toll != null ? "toll=" + toll + ", " : "") +
            (ruleRcvd != null ? "ruleRcvd=" + ruleRcvd + ", " : "") +
            (wantedFor != null ? "wantedFor=" + wantedFor + ", " : "") +
            (fine != null ? "fine=" + fine + ", " : "") +
            (licenseIssue != null ? "licenseIssue=" + licenseIssue + ", " : "") +
            (wantedBy != null ? "wantedBy=" + wantedBy + ", " : "") +
            (ruleProcessed != null ? "ruleProcessed=" + ruleProcessed + ", " : "") +
            (speedFine != null ? "speedFine=" + speedFine + ", " : "") +
            (lane != null ? "lane=" + lane + ", " : "") +
            (tagIssue != null ? "tagIssue=" + tagIssue + ", " : "") +
            (statusName != null ? "statusName=" + statusName + ", " : "") +
            (licenseFine != null ? "licenseFine=" + licenseFine + ", " : "") +
            (stolen != null ? "stolen=" + stolen + ", " : "") +
            (wanted != null ? "wanted=" + wanted + ", " : "") +
            (ruleSent != null ? "ruleSent=" + ruleSent + ", " : "") +
            (handled != null ? "handled=" + handled + ", " : "") +
            (ruleIssue != null ? "ruleIssue=" + ruleIssue + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
