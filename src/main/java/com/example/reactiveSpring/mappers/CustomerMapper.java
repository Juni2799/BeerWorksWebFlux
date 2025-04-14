package com.example.reactiveSpring.mappers;

import com.example.reactiveSpring.domain.Customer;
import com.example.reactiveSpring.model.CustomerDTO;
import org.mapstruct.Mapper;

@Mapper
public interface CustomerMapper {
    CustomerDTO customerToCustomerDTO(Customer customer);
    Customer customerDtoToCustomer(CustomerDTO customerDTO);
}
