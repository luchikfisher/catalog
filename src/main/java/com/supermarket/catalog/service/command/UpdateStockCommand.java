package com.supermarket.catalog.service.command;

public record UpdateStockCommand(
        int amount
) {}