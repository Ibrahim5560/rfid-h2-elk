package com.isoft.rfid.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.isoft.rfid.domain.EventsImages} entity.
 */
@Schema(description = "EventsImages (events_images) entity.\n@author Ibrahim Mohamed.")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class EventsImagesDTO implements Serializable {

    private Long id;

    @NotNull
    private String guid;

    private String imageLp;

    private String imageThumb;

    private Long processingTime;

    private Long ruleRcvd;

    private Long ruleSent;

    private Long when;

    private Long gantryProcessed;

    private Long gantrySent;

    private String status;

    private String dataStatus;

    private String threadRandNo;

    private Integer gantry;

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

    public String getImageLp() {
        return imageLp;
    }

    public void setImageLp(String imageLp) {
        this.imageLp = imageLp;
    }

    public String getImageThumb() {
        return imageThumb;
    }

    public void setImageThumb(String imageThumb) {
        this.imageThumb = imageThumb;
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

    public Integer getGantry() {
        return gantry;
    }

    public void setGantry(Integer gantry) {
        this.gantry = gantry;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof EventsImagesDTO)) {
            return false;
        }

        EventsImagesDTO eventsImagesDTO = (EventsImagesDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, eventsImagesDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "EventsImagesDTO{" +
            "id=" + getId() +
            ", guid='" + getGuid() + "'" +
            ", imageLp='" + getImageLp() + "'" +
            ", imageThumb='" + getImageThumb() + "'" +
            ", processingTime=" + getProcessingTime() +
            ", ruleRcvd=" + getRuleRcvd() +
            ", ruleSent=" + getRuleSent() +
            ", when=" + getWhen() +
            ", gantryProcessed=" + getGantryProcessed() +
            ", gantrySent=" + getGantrySent() +
            ", status='" + getStatus() + "'" +
            ", dataStatus='" + getDataStatus() + "'" +
            ", threadRandNo='" + getThreadRandNo() + "'" +
            ", gantry=" + getGantry() +
            "}";
    }
}
