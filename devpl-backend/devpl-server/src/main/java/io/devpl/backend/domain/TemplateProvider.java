package io.devpl.backend.domain;

public enum TemplateProvider {

    Velocity("Velocity", "Velocity", "vm"),
    Beetl("Beetl", "Beetl", "btl"),
    Enjoy("JFinal Enjoy", "Enjoy", "ej"),
    FreeMarker("FreeMarker", "FreeMarker", "ftl");

    private final String providerName;
    private final String provider;
    private final String extension;

    TemplateProvider(String providerName, String provider, String extension) {
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

    public static TemplateProvider findByName(String providerName) {
        for (TemplateProvider provider : values()) {
            if (provider.providerName.equalsIgnoreCase(providerName)) {
                return provider;
            }
        }
        return null;
    }
}
