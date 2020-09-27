package net.getnova.backend.cdn.data;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import net.getnova.backend.cdn.CdnModule;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CdnFileResolver {

  private final CdnModule cdnModule;
  private final CdnFileRepository cdnFileRepository;

  public Result resolve(final UUID id) {
    final Optional<CdnFile> cdnFile = this.cdnFileRepository.findById(id);
    if (cdnFile.isEmpty()) return null;

    final String idString = id.toString();
    return new Result(
      new File(this.cdnModule.getDataDir(), idString.substring(0, 2) + File.separatorChar + idString),
      cdnFile.get()
    );
  }

  @Data
  public static final class Result {
    private final File file;
    private final CdnFile cdnFile;
  }
}
