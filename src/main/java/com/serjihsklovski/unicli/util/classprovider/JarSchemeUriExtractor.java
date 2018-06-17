package com.serjihsklovski.unicli.util.classprovider;

import java.net.URI;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Helps to parse complex `jar:file` scheme URIs.
 * Extracts an inner part with `file` scheme as the URI
 * to a Jar file location (`getJarFileUri`), and a name
 * of a Jar entry to a resource (`getJarEntryName`).
 *
 * @see #extract(URI)
 * @see #getJarFileUri()
 * @see #getJarEntryName()
 */
public class JarSchemeUriExtractor {

    private static final String GROUP_JAR_FILE_LOCATION = "jarFileLocation";

    private static final String GROUP_JAR_ENTRY_NAME = "jarEntryName";

    private static final String JAR_SCHEME_URI_TEMPLATE = String.format("^jar:(?<%s>file:.*)!/?(?<%s>.*)",
            GROUP_JAR_FILE_LOCATION, GROUP_JAR_ENTRY_NAME);

    private URI jarFileUri;

    private String jarEntryName;

    private JarSchemeUriExtractor(URI jarSchemeUri) {
        Pattern pattern = Pattern.compile(JAR_SCHEME_URI_TEMPLATE);
        Matcher matcher = pattern.matcher(jarSchemeUri.toString());
        if (!matcher.matches()) {
            throw new RuntimeException("`jarSchemeUri` does not match to the `JAR_SCHEME_URI_TEMPLATE`.");
        }

        jarFileUri = URI.create(matcher.group(GROUP_JAR_FILE_LOCATION));
        jarEntryName = matcher.group(GROUP_JAR_ENTRY_NAME);
    }

    public static JarSchemeUriExtractor extract(URI jarSchemeUri) {
        return new JarSchemeUriExtractor(jarSchemeUri);
    }

    public URI getJarFileUri() {
        return jarFileUri;
    }

    public String getJarEntryName() {
        return jarEntryName;
    }

}
