package io.devpl.generator.domain;

public enum TemplateProvider {

    Velocity("Velocity", "Velocity"),
    FreeMarker("FreeMarker", "Velocity");

    private final String providerName;
    private final String provider;

    TemplateProvider(String providerName, String provider) {
        this.providerName = providerName;
        this.provider = provider;
    }

    public String getProviderName() {
        return providerName;
    }

    public String getProvider() {
        return provider;
    }
}
