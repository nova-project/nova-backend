package net.getnova.framework.web.server.http;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.StringTokenizer;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public final class HttpMimeTypeUtils {

  private static final Map<String, String> MIME_TYPES;

  static {
    Map<String, String> mineTypes;

    try (InputStream is = HttpMimeTypeUtils.class.getResourceAsStream("/mime.types");
      Reader isr = new InputStreamReader(is, StandardCharsets.US_ASCII);
      BufferedReader br = new BufferedReader(isr)) {
      mineTypes = parseReader(br);
    }
    catch (IOException e) {
      log.error("Unable to load mime types from class path file /mime.types", e);
      mineTypes = Collections.emptyMap();
    }

    MIME_TYPES = mineTypes;
  }

  private HttpMimeTypeUtils() {
    throw new UnsupportedOperationException();
  }

  public static Optional<String> getMediaTypes(final String filename) {
    return getExtensionByStringHandling(filename)
      .map(MIME_TYPES::get);
  }

  private static Map<String, String> parseReader(final BufferedReader br) throws IOException {
    final Map<String, String> result = new HashMap<>();

    String line;
    while ((line = br.readLine()) != null) {
      if (line.isEmpty() || line.charAt(0) == '#') {
        continue;
      }

      parseLine(line, result);
    }
    return result;
  }

  private static void parseLine(final String line, final Map<String, String> mimeTypes) throws IOException {
    final StringTokenizer tokenizer = new StringTokenizer(line, " \t\n\r\f");

    try {
      final String extension = tokenizer.nextToken().toLowerCase(Locale.ENGLISH);
      if (mimeTypes.containsKey(extension)) {
        return;
      }

      mimeTypes.put(extension, tokenizer.nextToken());
    }
    catch (NoSuchElementException e) {
      throw new IOException("Unable to load mime types - Wrong format, Line: \"" + line + "\"");
    }
  }

  private static Optional<String> getExtensionByStringHandling(final String filename) {
    if (filename == null) {
      return Optional.empty();
    }

    final int index = filename.lastIndexOf('.');

    if (index == -1) {
      return Optional.empty();
    }

    return Optional.of(filename.substring(index + 1).toLowerCase(Locale.ENGLISH));
  }
}
