package com.isoft.rfid.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.isoft.rfid.domain.Gantry} entity.
 */
@Schema(description = "Gantry (gantry) entity.\n@author Ibrahim Mohamed.")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class GantryDTO implements Serializable {

    private Long id;

    @NotNull
    private String guid;

    @NotNull
    private String nameEn;

    @NotNull
    private String nameAr;

    private Integer status;

    private String code;

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

    public String getNameEn() {
        return nameEn;
    }

    public void setNameEn(String nameEn) {
        this.nameEn = nameEn;
    }

    public String getNameAr() {
        return nameAr;
    }

    public void setNameAr(String nameAr) {
        this.nameAr = nameAr;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof GantryDTO)) {
            return false;
        }

        GantryDTO gantryDTO = (GantryDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, gantryDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "GantryDTO{" +
            "id=" + getId() +
            ", guid='" + getGuid() + "'" +
            ", nameEn='" + getNameEn() + "'" +
            ", nameAr='" + getNameAr() + "'" +
            ", status=" + getStatus() +
            ", code='" + getCode() + "'" +
            "}";
    }
}
