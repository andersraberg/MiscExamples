package se.anders_raberg.proptest;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import org.apache.commons.lang3.StringUtils;

public class UseProps {

    public void readProps() {
        Properties props = new TrailingTrimProperties();
        try (InputStream in = getClass().getClassLoader().getResourceAsStream("test.properties")) {
            props.load(in);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        try (InputStream in = getClass().getClassLoader().getResourceAsStream("test.xml")) {
            props.loadFromXML(in);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // props.forEach((k,v) -> System.out.printf("[%s] = [%s]%n", k, v));
        // props.forEach((k,v) -> System.out.printf("[%s] = [%s]%n", k,
        // TrailingTrimProperties.trimQuotes(v.toString())));
        props.forEach((k, v) -> System.out.printf("[%s] = [%s]%n", k, StringUtils.unwrap(v.toString(), '\"')));
    }
}
