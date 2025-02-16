package cz.casestudy.interview.products.controller;

import cz.casestudy.interview.products.model.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ProductMapper {


    @Mapping(target = "status", constant = "ACTIVE")
    Product toProduct(cz.casestudy.interview.product.rest.model.Product product);

}
