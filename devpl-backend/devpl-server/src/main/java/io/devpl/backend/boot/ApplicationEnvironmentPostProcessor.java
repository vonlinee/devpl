package io.devpl.backend.boot;

import io.devpl.sdk.util.CollectionUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.boot.env.PropertiesPropertySourceLoader;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.env.PropertySource;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.util.List;

/**
 * <a href="https://docs.spring.io/spring-boot/docs/2.0.4.RELEASE/reference/htmlsingle/#boot-features-external-config-profile-specific-properties">reference</a>
 */
@Slf4j
public class ApplicationEnvironmentPostProcessor implements EnvironmentPostProcessor {

    PropertiesPropertySourceLoader loader = new PropertiesPropertySourceLoader();

    @Override
    public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {
        MutablePropertySources propertySources = environment.getPropertySources();

        // loadCustomProperties(propertySources);
    }

    void adjustConfigFile(MutablePropertySources propertySources) {
        propertySources.stream().filter(propertySource -> propertySource.getName().contains("application.properties")).findFirst().ifPresent(ps -> {
            String activeProfile = (String) ps.getProperty("spring.profiles.active");
            if (activeProfile != null) {
                if ((!activeProfile.contains("dev") && !activeProfile.contains("test"))) {
                    final int index = propertySources.precedenceOf(ps);
                    if (index > 0) {
                        propertySources.addFirst(ps);
                    }
                }
            }
        });
    }

    public void loadCustomProperties(MutablePropertySources propertySources) {
        try {
            ClassPathResource file = new ClassPathResource("config/allprofiles.properties");
            if (file.exists()) {
                List<PropertySource<?>> propertySourcesOfAllProfiles = loader.load(file.getFilename(), file);
                if (!CollectionUtils.isEmpty(propertySourcesOfAllProfiles)) {
                    propertySources.addFirst(propertySourcesOfAllProfiles.get(0));
                }
            }
        } catch (IOException e) {
            log.error("exception when load properties of all profiles", e);
        }
    }
}
