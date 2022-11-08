package com.isoft.rfid.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.api.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.isoft.rfid.domain.EventsImages} entity. This class is used
 * in {@link com.isoft.rfid.web.rest.EventsImagesResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /events-images?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class EventsImagesCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter guid;

    private StringFilter imageLp;

    private StringFilter imageThumb;

    private LongFilter processingTime;

    private LongFilter ruleRcvd;

    private LongFilter ruleSent;

    private LongFilter when;

    private LongFilter gantryProcessed;

    private LongFilter gantrySent;

    private StringFilter status;

    private StringFilter dataStatus;

    private StringFilter threadRandNo;

    private IntegerFilter gantry;

    private Boolean distinct;

    public EventsImagesCriteria() {}

    public EventsImagesCriteria(EventsImagesCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.guid = other.guid == null ? null : other.guid.copy();
        this.imageLp = other.imageLp == null ? null : other.imageLp.copy();
        this.imageThumb = other.imageThumb == null ? null : other.imageThumb.copy();
        this.processingTime = other.processingTime == null ? null : other.processingTime.copy();
        this.ruleRcvd = other.ruleRcvd == null ? null : other.ruleRcvd.copy();
        this.ruleSent = other.ruleSent == null ? null : other.ruleSent.copy();
        this.when = other.when == null ? null : other.when.copy();
        this.gantryProcessed = other.gantryProcessed == null ? null : other.gantryProcessed.copy();
        this.gantrySent = other.gantrySent == null ? null : other.gantrySent.copy();
        this.status = other.status == null ? null : other.status.copy();
        this.dataStatus = other.dataStatus == null ? null : other.dataStatus.copy();
        this.threadRandNo = other.threadRandNo == null ? null : other.threadRandNo.copy();
        this.gantry = other.gantry == null ? null : other.gantry.copy();
        this.distinct = other.distinct;
    }

    @Override
    public EventsImagesCriteria copy() {
        return new EventsImagesCriteria(this);
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

    public StringFilter getImageLp() {
        return imageLp;
    }

    public StringFilter imageLp() {
        if (imageLp == null) {
            imageLp = new StringFilter();
        }
        return imageLp;
    }

    public void setImageLp(StringFilter imageLp) {
        this.imageLp = imageLp;
    }

    public StringFilter getImageThumb() {
        return imageThumb;
    }

    public StringFilter imageThumb() {
        if (imageThumb == null) {
            imageThumb = new StringFilter();
        }
        return imageThumb;
    }

    public void setImageThumb(StringFilter imageThumb) {
        this.imageThumb = imageThumb;
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
        final EventsImagesCriteria that = (EventsImagesCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(guid, that.guid) &&
            Objects.equals(imageLp, that.imageLp) &&
            Objects.equals(imageThumb, that.imageThumb) &&
            Objects.equals(processingTime, that.processingTime) &&
            Objects.equals(ruleRcvd, that.ruleRcvd) &&
            Objects.equals(ruleSent, that.ruleSent) &&
            Objects.equals(when, that.when) &&
            Objects.equals(gantryProcessed, that.gantryProcessed) &&
            Objects.equals(gantrySent, that.gantrySent) &&
            Objects.equals(status, that.status) &&
            Objects.equals(dataStatus, that.dataStatus) &&
            Objects.equals(threadRandNo, that.threadRandNo) &&
            Objects.equals(gantry, that.gantry) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            guid,
            imageLp,
            imageThumb,
            processingTime,
            ruleRcvd,
            ruleSent,
            when,
            gantryProcessed,
            gantrySent,
            status,
            dataStatus,
            threadRandNo,
            gantry,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "EventsImagesCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (guid != null ? "guid=" + guid + ", " : "") +
            (imageLp != null ? "imageLp=" + imageLp + ", " : "") +
            (imageThumb != null ? "imageThumb=" + imageThumb + ", " : "") +
            (processingTime != null ? "processingTime=" + processingTime + ", " : "") +
            (ruleRcvd != null ? "ruleRcvd=" + ruleRcvd + ", " : "") +
            (ruleSent != null ? "ruleSent=" + ruleSent + ", " : "") +
            (when != null ? "when=" + when + ", " : "") +
            (gantryProcessed != null ? "gantryProcessed=" + gantryProcessed + ", " : "") +
            (gantrySent != null ? "gantrySent=" + gantrySent + ", " : "") +
            (status != null ? "status=" + status + ", " : "") +
            (dataStatus != null ? "dataStatus=" + dataStatus + ", " : "") +
            (threadRandNo != null ? "threadRandNo=" + threadRandNo + ", " : "") +
            (gantry != null ? "gantry=" + gantry + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
