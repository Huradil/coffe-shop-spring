package com.example.coffee_shop.dto;

import lombok.Data;

import java.util.Dictionary;
import java.util.List;
import java.util.Map;

@Data
public class OrderDto {
    Long userId;
    Map<Long, Integer> menuIds;

    public OrderDto(){}
}
