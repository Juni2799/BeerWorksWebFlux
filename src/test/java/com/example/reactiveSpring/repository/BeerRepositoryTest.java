package com.example.reactiveSpring.repository;

import com.example.reactiveSpring.config.DatabaseConfig;
import com.example.reactiveSpring.domain.Beer;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.r2dbc.DataR2dbcTest;
import org.springframework.context.annotation.Import;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

@DataR2dbcTest
//Below annotation is used to import the annotations which are typically used during runtime of the application in the mentioned class, eg, "@EnableR2dbcAuditing"
@Import(DatabaseConfig.class)
class BeerRepositoryTest {
    @Autowired
    BeerRepository beerRepository;

    @Test
    void testSaveNewBeer(){
        beerRepository.save(getTestBeer())
                .subscribe(beer -> {
                    System.out.println(beer.toString());
                });
    }

    @Test
    void createTestJson() throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();

        System.out.println(objectMapper.writeValueAsString(getTestBeer()));
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