package net.getnova.framework.cdn.data;

import java.util.Optional;
import java.util.UUID;
import org.springframework.stereotype.Repository;

@Repository
public interface CdnFileRepository /*extends JpaRepository<CdnFile, UUID>*/ {

  Optional<CdnFile> findById(UUID id);
}
