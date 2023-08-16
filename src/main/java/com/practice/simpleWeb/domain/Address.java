package com.practice.simpleWeb.domain;

import lombok.Getter;

import javax.persistence.Embeddable;

@Embeddable
@Getter
public class Address {

    private String city;
    private String zipcode;
    private String detailAddress;
}
