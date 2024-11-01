package io.devpl.backend.utils;

import io.devpl.sdk.annotations.NotEmpty;

/**
 * Formatter contract
 */
public interface SqlFormatter {

    String getId();

    /**
     * Format the source SQL string.
     *
     * @param dialect The original SQL dialect
     * @param source  The original SQL string
     * @return The formatted version
     */
    String format(@NotEmpty String dialect, @NotEmpty String source);

    SqlFormatter NONE = new SqlFormatter() {

        @Override
        public String getId() {
            return "none";
        }

        @Override
        public String format(String dialect, String source) {
            return source;
        }
    };
}
