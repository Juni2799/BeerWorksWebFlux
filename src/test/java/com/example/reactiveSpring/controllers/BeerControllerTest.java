package com.example.reactiveSpring.controllers;

import com.example.reactiveSpring.domain.Beer;
import com.example.reactiveSpring.model.BeerDTO;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers.mockOAuth2Login;

@SpringBootTest //Helps in loading the complete config and bootstrap database setup of our project
@AutoConfigureWebTestClient  //Helps in WebTestClient configuration for testing purposes.
class BeerControllerTest {

    //We use WebTestClient for Reactive application since Spring MockMVC is linked with the Servlet API
    // which is not available in Spring Reactive due to it's "blocking" nature.
    @Autowired
    WebTestClient webTestClient;

    @Test
    @Order(2)
    void testListBeers(){
        webTestClient.mutateWith(mockOAuth2Login())
                .get().uri(BeerController.BEER_PATH)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().valueEquals("Content-type", "application/json")
                .expectBody().jsonPath("$.size()").isEqualTo(3);
    }

    @Test
    @Order(1)  //This helps in running tests in a particular order. Please note that Spring will execute all tests marked with "Order" annotation FIRST and then the other ones.
    //but in the console output, it does not seem like that. Check in ChatGPT later.
    void testGetById(){
        webTestClient.mutateWith(mockOAuth2Login())
                .get().uri(BeerController.BEER_PATH_ID, 2)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().valueEquals("Content-type", "application/json")
                .expectBody(BeerDTO.class);
    }

    @Test
    void testGetByIdNotFound(){
        webTestClient.mutateWith(mockOAuth2Login())
                .get().uri(BeerController.BEER_PATH_ID, 999)
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    void testCreateNewBeer(){
        webTestClient.mutateWith(mockOAuth2Login())
                .post().uri(BeerController.BEER_PATH)
                .body(Mono.just(getTestBeer()), BeerDTO.class)
                .header("Content-type", "application/json")
                .exchange()
                .expectStatus().isCreated()
                .expectHeader().location("http://localhost:8080/api/v2/beer/4");
    }

    @Test
    void testCreateNewBeerBadData(){
        Beer testBeer = getTestBeer();
        testBeer.setBeerName("");

        webTestClient.mutateWith(mockOAuth2Login())
                .post().uri(BeerController.BEER_PATH)
                .body(Mono.just(testBeer), BeerDTO.class)
                .header("Content-type", "application/json")
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    @Order(3)
    void testUpdateExistingBeer(){
        webTestClient.mutateWith(mockOAuth2Login())
                .put().uri(BeerController.BEER_PATH_ID, 2)
                .body(Mono.just(getTestBeer()), BeerDTO.class)
                .exchange()
                .expectStatus().isNoContent();
    }

    @Test
    void testUpdateExistingBeerBadData(){
        Beer testBeer = getTestBeer();
        testBeer.setBeerName("");

        webTestClient.mutateWith(mockOAuth2Login())
                .put().uri(BeerController.BEER_PATH_ID, 2)
                .body(Mono.just(testBeer), BeerDTO.class)
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    void testUpdateExistingBeerNotFound(){
        webTestClient.mutateWith(mockOAuth2Login())
                .put().uri(BeerController.BEER_PATH_ID, 999)
                .body(Mono.just(getTestBeer()), BeerDTO.class)
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    void testModifyBeerIdNotFound(){
        webTestClient
                .mutateWith(mockOAuth2Login())
                .patch()
                .uri(BeerController.BEER_PATH_ID, 99)
                .body(Mono.just(getTestBeer()), BeerDTO.class)
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    @Order(999)
    void testDeleteBeer(){
        webTestClient.mutateWith(mockOAuth2Login())
                .delete().uri(BeerController.BEER_PATH_ID, 1)
                .exchange()
                .expectStatus().isNoContent();
    }

    @Test
    void testDeleteBeerNotFound(){
        webTestClient.mutateWith(mockOAuth2Login())
                .delete().uri(BeerController.BEER_PATH_ID, 999)
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    void testDeleteIdNotFound(){
        webTestClient.mutateWith(mockOAuth2Login())
                .delete().uri(BeerController.BEER_PATH_ID, 999)
                .exchange()
                .expectStatus().isNotFound();
    }

    Beer getTestBeer(){
        Beer beer = new Beer();
        beer.setBeerName("Space Dust");
        beer.setBeerStyle("IPA");
        beer.setPrice(BigDecimal.TEN);
        beer.setUpc("123213");
        beer.setQuantityOnHand(12);
        return beer;
    }
}