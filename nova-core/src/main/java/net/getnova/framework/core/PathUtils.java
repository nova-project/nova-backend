package net.getnova.framework.core;

public final class PathUtils {

  private PathUtils() {
    throw new UnsupportedOperationException();
  }

  public static String stripSegments(final String segments, final String path) {
    if (segments.isEmpty() || (segments.length() == 1 && segments.charAt(0) == '/')) {
      return normalizePath(path);
    }

    if (segments.charAt(0) != '/') {
      throw new IllegalArgumentException("segments should start with \"/\"");
    }

    final int length = path.charAt(0) == '/' ? segments.length() : segments.length() - 1;

    return normalizePath(path.substring(length));
  }

  public static String normalizePath(final String fullPath) {
    if (fullPath.isEmpty()) {
      return "/";
    }

    boolean startsWithSlash = fullPath.charAt(0) == '/';
    if (fullPath.length() == 1 && startsWithSlash) {
      return fullPath;
    }

    String path = fullPath;
    if (!startsWithSlash) {
      path = '/' + path;
    }

    final int end = path.length() - 1;
    if (path.charAt(end) == '/') {
      path = path.substring(0, end);
    }

    return path;
  }
}
