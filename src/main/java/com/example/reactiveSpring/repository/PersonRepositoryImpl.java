package com.example.reactiveSpring.repository;

import com.example.reactiveSpring.domain.Person;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class PersonRepositoryImpl implements PersonRepository{
    Person akshaan = new Person(1, "Akshaan", "Khanna");
    Person raju = new Person(2, "Arvind", "Raju");
    Person geo = new Person(3, "Abhinav", "Geo");
    Person jisbin = new Person(4, "Jisbin", "Joseph");

    @Override
    public Mono<Person> getById(Integer id) {
        return findAll().filter(person -> person.getId() == id).next();
    }

    @Override
    public Flux<Person> findAll() {
        return Flux.just(akshaan, raju, geo, jisbin);
    }
}
