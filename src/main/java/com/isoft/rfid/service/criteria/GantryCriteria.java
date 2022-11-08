package com.isoft.rfid.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.api.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.isoft.rfid.domain.Gantry} entity. This class is used
 * in {@link com.isoft.rfid.web.rest.GantryResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /gantries?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class GantryCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter guid;

    private StringFilter nameEn;

    private StringFilter nameAr;

    private IntegerFilter status;

    private StringFilter code;

    private Boolean distinct;

    public GantryCriteria() {}

    public GantryCriteria(GantryCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.guid = other.guid == null ? null : other.guid.copy();
        this.nameEn = other.nameEn == null ? null : other.nameEn.copy();
        this.nameAr = other.nameAr == null ? null : other.nameAr.copy();
        this.status = other.status == null ? null : other.status.copy();
        this.code = other.code == null ? null : other.code.copy();
        this.distinct = other.distinct;
    }

    @Override
    public GantryCriteria copy() {
        return new GantryCriteria(this);
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

    public StringFilter getNameEn() {
        return nameEn;
    }

    public StringFilter nameEn() {
        if (nameEn == null) {
            nameEn = new StringFilter();
        }
        return nameEn;
    }

    public void setNameEn(StringFilter nameEn) {
        this.nameEn = nameEn;
    }

    public StringFilter getNameAr() {
        return nameAr;
    }

    public StringFilter nameAr() {
        if (nameAr == null) {
            nameAr = new StringFilter();
        }
        return nameAr;
    }

    public void setNameAr(StringFilter nameAr) {
        this.nameAr = nameAr;
    }

    public IntegerFilter getStatus() {
        return status;
    }

    public IntegerFilter status() {
        if (status == null) {
            status = new IntegerFilter();
        }
        return status;
    }

    public void setStatus(IntegerFilter status) {
        this.status = status;
    }

    public StringFilter getCode() {
        return code;
    }

    public StringFilter code() {
        if (code == null) {
            code = new StringFilter();
        }
        return code;
    }

    public void setCode(StringFilter code) {
        this.code = code;
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
        final GantryCriteria that = (GantryCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(guid, that.guid) &&
            Objects.equals(nameEn, that.nameEn) &&
            Objects.equals(nameAr, that.nameAr) &&
            Objects.equals(status, that.status) &&
            Objects.equals(code, that.code) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, guid, nameEn, nameAr, status, code, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "GantryCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (guid != null ? "guid=" + guid + ", " : "") +
            (nameEn != null ? "nameEn=" + nameEn + ", " : "") +
            (nameAr != null ? "nameAr=" + nameAr + ", " : "") +
            (status != null ? "status=" + status + ", " : "") +
            (code != null ? "code=" + code + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
