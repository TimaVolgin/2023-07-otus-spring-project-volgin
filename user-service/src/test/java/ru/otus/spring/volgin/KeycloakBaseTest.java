package ru.otus.spring.volgin;

import dasniko.testcontainers.keycloak.KeycloakContainer;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.ContextConfiguration;

/**
 * Базовый класс с поднятием testcontainer для keycloak
 */
@SpringBootTest
@ContextConfiguration(initializers = KeycloakBaseTest.Initializer.class)
public class KeycloakBaseTest {

    private static final KeycloakContainer keycloakContainer;
    static {
        keycloakContainer = new KeycloakContainer("jboss/keycloak:16.1.1")
                .withRealmImportFile("export.json");
        keycloakContainer.start();
    }

    public static class Initializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
        public void initialize(ConfigurableApplicationContext configurableApplicationContext) {
            String authServerUrl = "http://localhost:" + keycloakContainer.getHttpPort() + "/auth";
            TestPropertyValues.of(
                    "keycloak.auth-server-url=" + authServerUrl
            ).applyTo(configurableApplicationContext.getEnvironment());
        }
    }
}
