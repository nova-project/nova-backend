package net.getnova.framework.cdn.data;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CdnFileRepository extends JpaRepository<CdnFile, UUID> {

}
