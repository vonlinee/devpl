package io.devpl.backend.domain;

/**
 * 模板引擎类型
 */
public enum TemplateEngineType {

    /**
     * <a href="https://velocity.apache.org/">Velocity</a>
     */
    VELOCITY("Velocity", "Velocity", "vm"),

    /**
     * <a href="http://ibeetl.com/">Bee Template Language</a>
     */
    BEETL("Beetl", "Beetl", "btl"),

    /**
     * <a href="https://gitee.com/jfinal/enjoy">JFinal Enjoy</a>
     */
    ENJOY("JFinal Enjoy", "Enjoy", "ej"),

    /**
     * <a href="https://freemarker.apache.org/index.html">Apache FreeMarker</a>
     */
    FREE_MARKER("FreeMarker", "FreeMarker", "ftl");

    private final String providerName;
    private final String provider;
    private final String extension;

    TemplateEngineType(String providerName, String provider, String extension) {
        this.providerName = providerName;
        this.provider = provider;
        this.extension = extension;
    }

    public String getProviderName() {
        return providerName;
    }

    public String getProvider() {
        return provider;
    }

    public String getExtension() {
        return extension;
    }

    public static TemplateEngineType findByName(String providerName) {
        for (TemplateEngineType provider : values()) {
            if (provider.providerName.equalsIgnoreCase(providerName)) {
                return provider;
            }
        }
        return null;
    }
}
