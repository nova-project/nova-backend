package net.getnova.backend.sql.model;

import lombok.Data;
import net.getnova.backend.json.JsonTransient;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.Version;

@Data
@MappedSuperclass
public abstract class TableModel {

    @JsonTransient
    @Version
    @Column(name = "version")
    private long version;
}
