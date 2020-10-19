package net.getnova.backend.cdn.data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.getnova.backend.jpa.model.TableModelAutoId;

import java.time.OffsetDateTime;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "cdn_file")
public class CdnFile extends TableModelAutoId {

  @Column(name = "name", nullable = false, updatable = false, length = 128)
  private String name;

  @Column(name = "size", nullable = false, updatable = false)
  private long size;

  @Column(name = "uploaded", nullable = false, updatable = false)
  private OffsetDateTime uploaded;
}
