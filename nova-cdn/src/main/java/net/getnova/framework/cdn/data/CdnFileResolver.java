package net.getnova.framework.cdn.data;

import java.io.File;
import java.util.Optional;
import java.util.UUID;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CdnFileResolver {

  //  private final CdnModule cdnModule;
  private final CdnFileRepository cdnFileRepository;

  private static String getPath(final String id) {
    return id.substring(0, 2) + File.separatorChar + id;
  }

  public Optional<Result> resolve(final UUID id) {
    return this.cdnFileRepository.findById(id)
      .map(file -> new Result(new File(new File("data"), getPath(id.toString())), file));
  }

  @Data
  public static final class Result {

    private final File file;
    private final CdnFile cdnFile;
  }
}
