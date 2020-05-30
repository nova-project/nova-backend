package net.getnova.backend.config;

import lombok.extern.slf4j.Slf4j;
import org.yaml.snakeyaml.Yaml;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.lang.reflect.Field;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

@Slf4j
final class ConfigUtils {

    private static final String SPACING = "    ";
    private static final Yaml YAML = new Yaml();

    private ConfigUtils() {
        throw new UnsupportedOperationException();
    }

    static Set<ConfigValueData> parseConfig(final Object object) {
        final Set<ConfigValueData> values = new LinkedHashSet<>();

        for (final Field field : object.getClass().getDeclaredFields()) {
            if (!field.isAnnotationPresent(ConfigValue.class)) continue;
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
            final Object value = id == null || id.isBlank()
                    ? environment.get(valueData.getId())
                    : environment.get(id.toUpperCase() + "_" + valueData.getId().toUpperCase());
            if (value != null) setValue(valueData.getConfig(), valueData.getField(), value);
        }));
    }

    static void save(final File configFile, final Map<String, Set<ConfigValueData>> configs) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(configFile)))) {

            for (final Map.Entry<String, Set<ConfigValueData>> configEntry : configs.entrySet()) {
                final boolean lowLevel = configEntry.getKey() == null || configEntry.getKey().isBlank();
                final String spacing = lowLevel ? "" : SPACING;

                if (!lowLevel) {
                    writer.write(configEntry.getKey() + ":");
                    writer.newLine();
                }

                for (final ConfigValueData value : configEntry.getValue()) {
                    for (final String line : value.getComment()) {
                        writer.write(spacing + "# " + line);
                        writer.newLine();
                    }
                    writer.write(spacing + value.getId() + ": " + getValue(value.getConfig(), value.getField()));
                    writer.newLine();
                    writer.newLine();
                }
            }
        }
    }

    private static void setValue(final Object object, final Field field, final Object value) {
        field.setAccessible(true);
        try {
            // TODO Type converting
            field.set(object, value);
        } catch (IllegalAccessException e) {
            log.error("Unable to set value for field " + object.getClass().getName() + "." + field.getName() + ".", e);
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
}
