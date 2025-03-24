package com.jishop.cart.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.util.List;
import java.util.Map;

public record GuestCartRequest(
   Long id,
   int quantity
){ }
