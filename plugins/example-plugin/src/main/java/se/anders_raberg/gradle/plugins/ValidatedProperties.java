package se.anders_raberg.gradle.plugins;

import org.gradle.api.GradleException;

import java.io.File;
import java.util.Properties;
import java.util.regex.Pattern;

public class ValidatedProperties extends Properties {
    private final static Pattern TRAILING_WHITE_SPACE_PATTERN = Pattern.compile(".*[\\t ]+$");
    private final File _file;

    ValidatedProperties(File file) {
        super();
        _file = file;
    }

    @Override
    public synchronized Object put(Object key, Object value) {
        if (containsKey(key)) {
            throw new GradleException("Duplicate key [%s] found in '%s'".formatted(key, _file.getAbsolutePath()));
        }

        if (value instanceof String sValue) {
            if (TRAILING_WHITE_SPACE_PATTERN.matcher(sValue).matches()) {
                throw new GradleException(
                        "Trailing white space [%s=%s] found in '%s'".formatted(key, sValue, _file.getAbsolutePath()));
            }
        }
        return super.put(key, value);
    }
}
