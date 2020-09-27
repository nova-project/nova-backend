package net.getnova.backend.cdn.data;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface CdnFileRepository extends JpaRepository<CdnFile, UUID> {
}
