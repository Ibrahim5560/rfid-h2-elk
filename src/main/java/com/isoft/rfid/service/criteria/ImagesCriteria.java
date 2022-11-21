package com.isoft.rfid.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.api.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.isoft.rfid.domain.Images} entity. This class is used
 * in {@link com.isoft.rfid.web.rest.ImagesResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /images?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ImagesCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter guid;

    private StringFilter plate;

    private StringFilter anpr;

    private StringFilter rfid;

    private StringFilter dataStatus;

    private LongFilter gantry;

    private LongFilter lane;

    private LongFilter kph;

    private LongFilter ambush;

    private LongFilter direction;

    private LongFilter vehicle;

    private StringFilter issue;

    private StringFilter status;

    private Boolean distinct;

    public ImagesCriteria() {}

    public ImagesCriteria(ImagesCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.guid = other.guid == null ? null : other.guid.copy();
        this.plate = other.plate == null ? null : other.plate.copy();
        this.anpr = other.anpr == null ? null : other.anpr.copy();
        this.rfid = other.rfid == null ? null : other.rfid.copy();
        this.dataStatus = other.dataStatus == null ? null : other.dataStatus.copy();
        this.gantry = other.gantry == null ? null : other.gantry.copy();
        this.lane = other.lane == null ? null : other.lane.copy();
        this.kph = other.kph == null ? null : other.kph.copy();
        this.ambush = other.ambush == null ? null : other.ambush.copy();
        this.direction = other.direction == null ? null : other.direction.copy();
        this.vehicle = other.vehicle == null ? null : other.vehicle.copy();
        this.issue = other.issue == null ? null : other.issue.copy();
        this.status = other.status == null ? null : other.status.copy();
        this.distinct = other.distinct;
    }

    @Override
    public ImagesCriteria copy() {
        return new ImagesCriteria(this);
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
        final ImagesCriteria that = (ImagesCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(guid, that.guid) &&
            Objects.equals(plate, that.plate) &&
            Objects.equals(anpr, that.anpr) &&
            Objects.equals(rfid, that.rfid) &&
            Objects.equals(dataStatus, that.dataStatus) &&
            Objects.equals(gantry, that.gantry) &&
            Objects.equals(lane, that.lane) &&
            Objects.equals(kph, that.kph) &&
            Objects.equals(ambush, that.ambush) &&
            Objects.equals(direction, that.direction) &&
            Objects.equals(vehicle, that.vehicle) &&
            Objects.equals(issue, that.issue) &&
            Objects.equals(status, that.status) &&
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
            lane,
            kph,
            ambush,
            direction,
            vehicle,
            issue,
            status,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ImagesCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (guid != null ? "guid=" + guid + ", " : "") +
            (plate != null ? "plate=" + plate + ", " : "") +
            (anpr != null ? "anpr=" + anpr + ", " : "") +
            (rfid != null ? "rfid=" + rfid + ", " : "") +
            (dataStatus != null ? "dataStatus=" + dataStatus + ", " : "") +
            (gantry != null ? "gantry=" + gantry + ", " : "") +
            (lane != null ? "lane=" + lane + ", " : "") +
            (kph != null ? "kph=" + kph + ", " : "") +
            (ambush != null ? "ambush=" + ambush + ", " : "") +
            (direction != null ? "direction=" + direction + ", " : "") +
            (vehicle != null ? "vehicle=" + vehicle + ", " : "") +
            (issue != null ? "issue=" + issue + ", " : "") +
            (status != null ? "status=" + status + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
