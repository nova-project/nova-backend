package net.getnova.backend.network.server.http;

import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MimeType;
import org.springframework.util.MimeTypeUtils;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

public final class HttpMimeType {

  private static final String MIME_TYPES_FILE_NAME = "/mime.types";
  private static final MultiValueMap<String, MimeType> fileExtensionToMediaTypes = parseMimeTypes();

  private HttpMimeType() {
  }

  private static MultiValueMap<String, MimeType> parseMimeTypes() {
    final InputStream is = HttpMimeType.class.getResourceAsStream(MIME_TYPES_FILE_NAME);
    try (BufferedReader reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.US_ASCII))) {
      final MultiValueMap<String, MimeType> result = new LinkedMultiValueMap<>();
      String line;
      while ((line = reader.readLine()) != null) {
        if (line.isEmpty() || line.charAt(0) == '#') continue;
        final String[] tokens = StringUtils.tokenizeToStringArray(line, " \t\n\r\f");
        final MimeType mediaType = MimeTypeUtils.parseMimeType(tokens[0]);
        for (int i = 1; i < tokens.length; i++) result.add(tokens[i].toLowerCase(Locale.ENGLISH), mediaType);
      }
      return result;
    } catch (IOException e) {
      throw new IllegalStateException("Unable to load mime types form file: " + MIME_TYPES_FILE_NAME, e);
    }
  }

  public static Optional<MimeType> getMediaType(final String filename) {
    return getMediaTypes(filename).stream().findFirst();
  }

  public static List<MimeType> getMediaTypes(final String filename) {
    return Optional.ofNullable(StringUtils.getFilenameExtension(filename))
      .map(s -> fileExtensionToMediaTypes.get(s.toLowerCase(Locale.ENGLISH)))
      .orElse(Collections.emptyList());
  }
}
