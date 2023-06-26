package de.cofinpro.recipeserver.security;

import de.cofinpro.recipeserver.web.dto.UserDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

@Testcontainers
@Disabled//IfEnvironmentVariable(named = "GITHUB_CI", matches = "true")
class TestcontainerSecurityIT {

    TestRestTemplate restTemplate = new TestRestTemplate();

    @Container
    static GenericContainer<?> recipeContainer = new GenericContainer<>(
            DockerImageName.parse("recipe-server:0.1.9-SNAPSHOT")
    ).withExposedPorts(8080);

    @Test
    void registerUnauthenticatedValidJson_AddsUser() {
        String url = "http://" + recipeContainer.getHost() + ":"
                + recipeContainer.getFirstMappedPort() + "/api/register";
        ResponseEntity<Void> response = restTemplate.postForEntity(url,
                new UserDto("hans.wurst@xyz.de", "12345678"), Void.class);
        Assertions.assertEquals(response.getStatusCode(), HttpStatus.OK);
    }
}
