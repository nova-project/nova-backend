package net.getnova.backend.sql.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import net.getnova.backend.json.JsonTransient;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.Version;

@Getter
@MappedSuperclass
@NoArgsConstructor
@AllArgsConstructor
public abstract class TableModel {

  @Version
  @JsonTransient
  @Column(name = "version", nullable = false)
  private long version;
}
