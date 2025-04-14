package com.example.reactiveSpring.repository;

import com.example.reactiveSpring.domain.Person;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PersonRepositoryImplTest {
    PersonRepository personRepository = new PersonRepositoryImpl();

    @Test
    void getMonoByIdBlock(){
        Mono<Person> personMono = personRepository.getById(1);

        Person person = personMono.block();  //this is not the preferred way to get data from Mono, instead we should use subscribe() method

        System.out.println(person.toString());
    }

    @Test
    void getMonoByIdSubscriber(){
        Mono<Person> personMono = personRepository.getById(2);

        //With the subscribe() method, we put "Back Pressure" on the Mono to process and provide our required data.
        personMono.subscribe(person -> {
            System.out.println(person.toString());
        });
    }

    @Test
    void getMonoByIdAndMapFirstName(){
        Mono<Person> personMono = personRepository.getById(3);

        personMono.map(Person::getFirstName).subscribe(System.out::println);
    }

    @Test
    void testFluxFirstBlock(){
        Flux<Person> personFlux = personRepository.findAll();

        Person person = personFlux.blockLast();

        System.out.println(person.toString());
    }

    @Test
    void testFluxSubscriber(){
        Flux<Person> personFlux = personRepository.findAll();

        personFlux.subscribe(person -> {
            System.out.println(person.toString());
        });
    }

    @Test
    void testFluxMap(){
        Flux<Person> personFlux = personRepository.findAll();

        personFlux.map(Person::getLastName)
                .subscribe(System.out::println);
    }

    @Test
    void testFluxToList(){
        Flux<Person> personFlux = personRepository.findAll();

        Mono<List<Person>> listMono = personFlux.collectList();

        listMono.subscribe(list -> {
            list.forEach(person -> System.out.println(person.getFirstName()));
        });
    }

    @Test
    void testFluxWithFilterOnName(){
        personRepository.findAll()
                .filter(person -> person.getFirstName().startsWith("A"))
                .subscribe(person -> System.out.println(person.toString()));
    }

    @Test
    void testFluxFilterToMono(){
        Mono<Person> personMono = personRepository.findAll()
                .filter(person -> person.getLastName().startsWith("J"))
                .next();

        personMono.subscribe(person -> System.out.println(person.toString()));
    }

    @Test
    void testFindPersonByIdNotFound() {
        Flux<Person> personFlux = personRepository.findAll();

        final Integer id = 8;

        Mono<Person> personMono = personFlux.filter(person -> person.getId() == id).single()
                .doOnError(throwable -> {
                    System.out.println("Error occurred in flux");
                    System.out.println(throwable.toString());
                });

        //If below subscribe() method is commented/removed then no "Back Pressure" is applied on the Mono<T>
        //due to which even though when error should occur, it does not, even for the Flux it doesn't. Hence, for error handling we need to apply
        //Back Pressure to trigger the chain of events that are acting on our Mono/Flux.
        personMono.subscribe(person -> {
            System.out.println(person.toString());
        }, throwable -> {
            System.out.println("Error occurred in the mono");
            System.out.println(throwable.toString());
        });
    }

    @Test
    void testEmptyMonoById(){
        Mono<Person> personMono = personRepository.getById(8);

        assertFalse(personMono.hasElement().block());
    }

    //Below 2 are alternate approaches to verify getById() method instead of using block() method.
    //StepVerifier is comparatively better option for this test but there is more to this class.
    @Test
    void testGetByIdFoundStepVerifier() {
        Mono<Person> personMono = personRepository.getById(3);

        StepVerifier.create(personMono).expectNextCount(1).verifyComplete();

        personMono.subscribe(person -> {
            System.out.println(person.getFirstName());
        });
    }

    @Test
    void testGetByIdNotFoundStepVerifier() {
        Mono<Person> personMono = personRepository.getById(6);

        StepVerifier.create(personMono).expectNextCount(0).verifyComplete();

        personMono.subscribe(person -> {
            System.out.println(person.getFirstName());
        });
    }
}