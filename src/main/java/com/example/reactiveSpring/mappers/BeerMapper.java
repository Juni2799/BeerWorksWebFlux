package com.example.reactiveSpring.mappers;

import com.example.reactiveSpring.domain.Beer;
import com.example.reactiveSpring.model.BeerDTO;
import org.mapstruct.Mapper;

@Mapper
public interface BeerMapper {
    BeerDTO beerToBeerDTO(Beer beer);

    Beer beerDtoToBeer(BeerDTO beerDTO);
}
