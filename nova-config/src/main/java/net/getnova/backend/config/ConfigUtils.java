package net.getnova.backend.config;

import lombok.extern.slf4j.Slf4j;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
final class ConfigUtils {

    private static final DumperOptions.LineBreak LINE_SEPARATOR = DumperOptions.LineBreak.UNIX;
    private static final String LINE_SEPARATOR_STRING = LINE_SEPARATOR.getString();
    private static final int INDENT = 2;
    private static final String INDENT_STRING = " ".repeat(INDENT);
    private static final Yaml YAML;

    static {
        final DumperOptions dumperOptions = new DumperOptions();
        dumperOptions.setLineBreak(LINE_SEPARATOR);
        dumperOptions.setIndent(INDENT);
        dumperOptions.setPrettyFlow(true);
        dumperOptions.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
        dumperOptions.setWidth(180);
        YAML = new Yaml(dumperOptions);
    }

    private ConfigUtils() {
        throw new UnsupportedOperationException();
    }

    static Set<ConfigValueData> parseConfig(final Object object) {
        final Set<ConfigValueData> values = new LinkedHashSet<>();

        for (final Field field : object.getClass().getDeclaredFields()) {
            if (!field.isAnnotationPresent(ConfigValue.class)) continue;
            if (Modifier.isFinal(field.getModifiers())) {
                log.warn("Can't update config values, because field {}.{} is final.", object.getClass().getName(), field.getName());
            }
            final ConfigValue configValue = field.getAnnotation(ConfigValue.class);
            values.add(new ConfigValueData(configValue.id(), configValue.comment(), field, object));
        }

        return values;
    }

    static void loadFromFile(final File file, final Map<String, Set<ConfigValueData>> configs) throws IOException {
        try (InputStream inputStream = new FileInputStream(file)) {
            final Map<String, Object> data = YAML.load(inputStream);

            if (data != null) configs.forEach((id, values) -> {
                final Map<String, Object> currentData = id == null || id.isBlank()
                        ? data : (Map<String, Object>) data.get(id);
                if (currentData != null) values.forEach(valueData -> {
                    final Object value = currentData.get(valueData.getId());
                    if (value != null) setValue(valueData.getConfig(), valueData.getField(), value);
                });
            });
        }
    }

    static void loadFromEnvironment(final Map<String, Set<ConfigValueData>> configs) {
        final Map<String, String> environment = System.getenv();

        configs.forEach((id, values) -> values.forEach(valueData -> {
            final String path = id == null || id.isBlank()
                    ? valueData.getId().toUpperCase()
                    : (id.toUpperCase() + "_" + valueData.getId().toUpperCase());
            final Object value = environment.get(path.replace('-', '_'));
            if (value != null) setValue(valueData.getConfig(), valueData.getField(), value);
        }));
    }

    static void save(final File configFile, final Map<String, Set<ConfigValueData>> configs) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(configFile)))) {
            final Map<String, Object> values = new LinkedHashMap<>();

            boolean lowLevel;
            Map<String, Object> currentMap;
            for (final Map.Entry<String, Set<ConfigValueData>> configEntry : configs.entrySet()) {
                lowLevel = configEntry.getKey() == null || configEntry.getKey().isEmpty() || configEntry.getKey().isBlank();
                currentMap = lowLevel ? values : new LinkedHashMap<>();

                for (final ConfigValueData value : configEntry.getValue())
                    currentMap.put(value.getId(), getValue(value.getConfig(), value.getField()));

                if (!lowLevel) values.put(configEntry.getKey(), currentMap);
            }

            /* Inject Config Comments */
            Map<Object, ConfigValueData> currentValues = null;
            String strippedLine;
            String[] tmp;
            boolean indent = false;
            for (final String line : YAML.dump(values).split(LINE_SEPARATOR_STRING)) {
                strippedLine = line.strip();
                if (strippedLine.startsWith("-") || strippedLine.startsWith("#") || !line.contains(":") || strippedLine.isEmpty()) {
                    writer.write(line + LINE_SEPARATOR_STRING);
                    continue;
                }

                tmp = strippedLine.split(":", 2);
                if (tmp.length < 1) {
                    writer.write(line + LINE_SEPARATOR_STRING);
                    continue;
                }

                if (strippedLine.endsWith(":")) {
                    currentValues = configs.get(strippedLine.substring(0, strippedLine.length() - 1))
                            .stream()
                            .collect(Collectors.toMap(ConfigValueData::getId, Function.identity()));
                    writer.write(line + LINE_SEPARATOR_STRING);
                    indent = true;
                } else {
                    if (!(strippedLine.endsWith(":") || line.startsWith(" "))) {
                        currentValues = configs.get("")
                                .stream()
                                .collect(Collectors.toMap(ConfigValueData::getId, Function.identity()));
                        indent = false;
                    }
                    writer.write((indent ? INDENT_STRING : "") + "# "
                            + String.join(LINE_SEPARATOR_STRING + (indent ? INDENT_STRING + "# " : "# "),
                            currentValues.get(tmp[0]).getComment())
                            + LINE_SEPARATOR_STRING);
                    writer.write(line + LINE_SEPARATOR_STRING + LINE_SEPARATOR_STRING);
                }
            }
        }
    }

    private static void setValue(final Object object, final Field field, final Object value) {
        field.setAccessible(true);
        final Class<?> required = getType(field.getType());
        final Class<?> provided = getType(value.getClass());
        try {
            if (required == provided) field.set(object, value);
            else required.getDeclaredConstructor(provided).newInstance(value);
        } catch (IllegalAccessException e) {
            log.error("Unable to set value for field " + object.getClass().getName() + "." + field.getName() + ".", e);
        } catch (NoSuchMethodException | InstantiationException | InvocationTargetException e) {
            log.error("Unable to convert a \"" + provided.getName() + "\" into a \"" + required.getName() + "\".", e);
        }
        field.setAccessible(false);
    }

    private static Object getValue(final Object object, final Field field) {
        field.setAccessible(true);
        Object value = null;
        try {
            value = field.get(object);
        } catch (IllegalAccessException e) {
            log.error("Unable to get value for field " + object.getClass().getName() + "." + field.getName() + ".", e);
        }
        field.setAccessible(false);
        return value;
    }

    private static Class<?> getType(final Class<?> type) {
        if (boolean.class.equals(type)) return Boolean.class;
        else if (byte.class.equals(type)) return Byte.class;
        else if (char.class.equals(type)) return Character.class;
        else if (double.class.equals(type)) return Double.class;
        else if (float.class.equals(type)) return Float.class;
        else if (int.class.equals(type)) return Integer.class;
        else if (long.class.equals(type)) return Long.class;
        else if (short.class.equals(type)) return Short.class;
        else return type;
    }
}
