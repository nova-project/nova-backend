package net.getnova.backend.cdn.data;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CdnFileResolver {

  //  private final CdnModule cdnModule;
  private final CdnFileRepository cdnFileRepository;

  public Optional<Result> resolve(final UUID id) {
    return this.cdnFileRepository.findById(id)
      .map(file -> new Result(new File(new File("data"), getPath(id.toString())), file));
  }

  private static String getPath(final String id) {
    return id.substring(0, 2) + File.separatorChar + id;
  }

  @Data
  public static final class Result {
    private final File file;
    private final CdnFile cdnFile;
  }
}
