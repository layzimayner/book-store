package com.example.demo.config;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties({"pageable"})
public abstract class PageImplMixin<T> {
}