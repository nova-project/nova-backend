package net.getnova.framework.core;

public final class PathUtils {

  private PathUtils() {
    throw new UnsupportedOperationException();
  }

  /**
   * Strips one or multiple path components from the beginning of a path.
   * <pre>
   * stripComponent("/hello", "/hello/world") -&gt; "/world"
   * stripComponent("/hello/world", "/hello/world/123") -&gt; "/123"
   * </pre>
   *
   * @param component the component to strip from the path (must start with a {@code /}, multiple components separated
   *                  with a {@code /})
   * @param path      the path, where the components should be stripped
   * @return the path without the specified components
   * @throws IllegalArgumentException if the components does not start with a {@code /}
   */
  public static String stripComponents(final String component, final String path) {
    if (component.isEmpty() || (component.length() == 1 && component.charAt(0) == '/')) {
      return normalizePath(path);
    }

    if (component.charAt(0) != '/') {
      throw new IllegalArgumentException("component should start with \"/\"");
    }

    final int length = path.charAt(0) == '/' ? component.length() : component.length() - 1;

    return normalizePath(path.substring(length));
  }

  /**
   * Removes leading and trailing {@code /} from a path.
   * <pre>
   * normalizePath("/hello/") -&gt; "hello"
   * normalizePath("/hello/world") -&gt; "hello/world"
   * normalizePath("") -&gt; "/"
   * </pre>
   *
   * @param fullPath the path witch should be normalized
   * @return the normalized path
   */
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
