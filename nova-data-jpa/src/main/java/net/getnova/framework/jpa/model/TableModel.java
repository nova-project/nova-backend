package net.getnova.framework.jpa.model;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.Version;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.getnova.framework.json.JsonTransient;

@Getter
@Setter
@MappedSuperclass
@NoArgsConstructor
@AllArgsConstructor
public abstract class TableModel {

  @Version
  @JsonTransient
  @Column(name = "version", nullable = false)
  private long version;
}
