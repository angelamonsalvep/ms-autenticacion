package com.angelapmonsalve.microservices.autenticacion.api;

import com.angelapmonsalve.microservices.autenticacion.api.handler.LoginHandler;
import com.angelapmonsalve.microservices.autenticacion.api.router.RouterRest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.reactive.server.WebTestClient;

@ContextConfiguration(classes = {RouterRest.class, LoginHandler.class})
@WebFluxTest
class RouterRestTest {

    @Autowired
    private WebTestClient webTestClient;

    /*@Test
    void testLoginEndpoint() {
        webTestClient.post()
                .uri("/api/v1/login")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue("{\"username\":\"usuario@example.com\",\"password\":\"password123\"}")
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class)
                .value(response -> Assertions.assertThat(response).contains("token"));
    }*/
}
