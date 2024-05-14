package io.devpl.backend.utils;

/**
 * Formatter contract
 */
public interface SqlFormatter {
    /**
     * Format the source SQL string.
     *
     * @param dialect The original SQL dialect
     * @param source  The original SQL string
     * @return The formatted version
     */
    String format(String dialect, String source);
}
