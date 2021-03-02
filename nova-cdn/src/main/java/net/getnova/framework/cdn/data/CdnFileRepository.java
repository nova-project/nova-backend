package net.getnova.framework.cdn.data;

import java.util.Optional;
import java.util.UUID;

public interface CdnFileRepository /*extends JpaRepository<CdnFile, UUID>*/ {

  Optional<CdnFile> findById(UUID id);
}
