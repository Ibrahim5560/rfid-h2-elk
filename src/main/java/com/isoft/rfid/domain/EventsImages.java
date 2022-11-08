package com.isoft.rfid.domain;

import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * EventsImages (events_images) entity.\n@author Ibrahim Mohamed.
 */
@Entity
@Table(name = "events_images")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "eventsimages")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class EventsImages implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "guid", nullable = false)
    private String guid;

    @Column(name = "image_lp")
    private String imageLp;

    @Column(name = "image_thumb")
    private String imageThumb;

    @Column(name = "processing_time")
    private Long processingTime;

    @Column(name = "rule_rcvd")
    private Long ruleRcvd;

    @Column(name = "rule_sent")
    private Long ruleSent;

    @Column(name = "jhi_when")
    private Long when;

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

    @Column(name = "gantry")
    private Integer gantry;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public EventsImages id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getGuid() {
        return this.guid;
    }

    public EventsImages guid(String guid) {
        this.setGuid(guid);
        return this;
    }

    public void setGuid(String guid) {
        this.guid = guid;
    }

    public String getImageLp() {
        return this.imageLp;
    }

    public EventsImages imageLp(String imageLp) {
        this.setImageLp(imageLp);
        return this;
    }

    public void setImageLp(String imageLp) {
        this.imageLp = imageLp;
    }

    public String getImageThumb() {
        return this.imageThumb;
    }

    public EventsImages imageThumb(String imageThumb) {
        this.setImageThumb(imageThumb);
        return this;
    }

    public void setImageThumb(String imageThumb) {
        this.imageThumb = imageThumb;
    }

    public Long getProcessingTime() {
        return this.processingTime;
    }

    public EventsImages processingTime(Long processingTime) {
        this.setProcessingTime(processingTime);
        return this;
    }

    public void setProcessingTime(Long processingTime) {
        this.processingTime = processingTime;
    }

    public Long getRuleRcvd() {
        return this.ruleRcvd;
    }

    public EventsImages ruleRcvd(Long ruleRcvd) {
        this.setRuleRcvd(ruleRcvd);
        return this;
    }

    public void setRuleRcvd(Long ruleRcvd) {
        this.ruleRcvd = ruleRcvd;
    }

    public Long getRuleSent() {
        return this.ruleSent;
    }

    public EventsImages ruleSent(Long ruleSent) {
        this.setRuleSent(ruleSent);
        return this;
    }

    public void setRuleSent(Long ruleSent) {
        this.ruleSent = ruleSent;
    }

    public Long getWhen() {
        return this.when;
    }

    public EventsImages when(Long when) {
        this.setWhen(when);
        return this;
    }

    public void setWhen(Long when) {
        this.when = when;
    }

    public Long getGantryProcessed() {
        return this.gantryProcessed;
    }

    public EventsImages gantryProcessed(Long gantryProcessed) {
        this.setGantryProcessed(gantryProcessed);
        return this;
    }

    public void setGantryProcessed(Long gantryProcessed) {
        this.gantryProcessed = gantryProcessed;
    }

    public Long getGantrySent() {
        return this.gantrySent;
    }

    public EventsImages gantrySent(Long gantrySent) {
        this.setGantrySent(gantrySent);
        return this;
    }

    public void setGantrySent(Long gantrySent) {
        this.gantrySent = gantrySent;
    }

    public String getStatus() {
        return this.status;
    }

    public EventsImages status(String status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDataStatus() {
        return this.dataStatus;
    }

    public EventsImages dataStatus(String dataStatus) {
        this.setDataStatus(dataStatus);
        return this;
    }

    public void setDataStatus(String dataStatus) {
        this.dataStatus = dataStatus;
    }

    public String getThreadRandNo() {
        return this.threadRandNo;
    }

    public EventsImages threadRandNo(String threadRandNo) {
        this.setThreadRandNo(threadRandNo);
        return this;
    }

    public void setThreadRandNo(String threadRandNo) {
        this.threadRandNo = threadRandNo;
    }

    public Integer getGantry() {
        return this.gantry;
    }

    public EventsImages gantry(Integer gantry) {
        this.setGantry(gantry);
        return this;
    }

    public void setGantry(Integer gantry) {
        this.gantry = gantry;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof EventsImages)) {
            return false;
        }
        return id != null && id.equals(((EventsImages) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "EventsImages{" +
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
