package net.getnova.backend.sql.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.getnova.backend.json.JsonTransient;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.Version;

@Data
@MappedSuperclass
@NoArgsConstructor
@AllArgsConstructor
public abstract class TableModel {

    @JsonTransient
    @Version
    @Column(name = "version", nullable = false)
    private long version;
}
