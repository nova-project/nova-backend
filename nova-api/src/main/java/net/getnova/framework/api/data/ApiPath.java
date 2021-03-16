package net.getnova.framework.api.data;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import lombok.AccessLevel;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class ApiPath {

  private final String raw;
  private final Pattern pattern;
  private final String[] variables;

  public static ApiPath of(final String path) {
    final StringBuilder regex = new StringBuilder();
    final List<String> variables = new LinkedList<>();

    final StringTokenizer tokenizer = new StringTokenizer(path, "/");
    boolean escape = false;

    while (tokenizer.hasMoreTokens()) {
      final String component = tokenizer.nextToken().strip();
      if (component.isEmpty()) {
        continue;
      }

      // todo: check for valid regex group name

      int end = component.length() - 1;
      if (component.charAt(0) == '{' && component.charAt(end) == '}') {
        final int separator = component.indexOf(':');
        if (separator == end - 1) {
          throw new IllegalArgumentException("Missing regex");
        }

        final String name = component.substring(1, separator == -1 ? end : separator);
        final String valueRegex = separator == -1 ? "[^\\/]+" : component.substring(separator + 1, end);

        if (name.length() == 0) {
          throw new IllegalArgumentException("Missing variable name");
        }

        if (escape) {
          regex.append("\\E");
          escape = false;
        }

        regex.append("\\/(").append(valueRegex).append(")");
        variables.add(name);
      }
      else {
        if (!escape) {
          regex.append("\\Q");
          escape = true;
        }

        regex.append("/").append(component.toLowerCase(Locale.ENGLISH));
      }
    }

    if (escape) {
      regex.append("\\E");
    }

    return new ApiPath(
      path,
      Pattern.compile(regex.toString(), Pattern.CASE_INSENSITIVE),
      variables.toArray(new String[0])
    );
  }

  public Optional<Matcher> match(final String input) {
    final Matcher matcher = this.pattern.matcher(input);

    if (!matcher.matches()) {
      return Optional.empty();
    }

    return Optional.of(matcher);
  }

  public Map<String, String> getVariables(final Matcher matcher) {
    final Map<String, String> variables = new HashMap<>(this.variables.length);

    for (int i = 0; i < this.variables.length; i++) {
      variables.put(this.variables[i], matcher.group(i + 1));
    }

    // todo: unmodifiable??
    return variables;
  }
}
