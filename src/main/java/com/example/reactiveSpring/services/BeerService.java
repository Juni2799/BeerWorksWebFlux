package com.example.reactiveSpring.services;

import com.example.reactiveSpring.model.BeerDTO;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface BeerService {
    Flux<BeerDTO> listBeers();

    Mono<BeerDTO> getBeerById(Integer beerId);

    Mono<BeerDTO> createNewBeer(BeerDTO beerDTO);

    Mono<BeerDTO> updateExistingBeerById(Integer id, BeerDTO beerDTO);

    Mono<Void> deleteBeerById(Integer beerId);

    Mono<BeerDTO> modifyExistingBeerById(Integer id, BeerDTO beerDTO);
}
