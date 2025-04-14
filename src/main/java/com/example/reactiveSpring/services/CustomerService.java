package com.example.reactiveSpring.services;

import com.example.reactiveSpring.model.CustomerDTO;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface CustomerService {
    Flux<CustomerDTO> listCustomers();

    Mono<CustomerDTO> getCustomerById(Integer customerId);

    Mono<CustomerDTO> createNewCustomer(CustomerDTO customerDTO);

    Mono<CustomerDTO> updateExistingCustomerById(Integer id, CustomerDTO customerDTO);

    Mono<Void> deleteCustomerById(Integer customerId);

    Mono<CustomerDTO> modifyExistingCustomerById(Integer id, CustomerDTO customerDTO);
}
