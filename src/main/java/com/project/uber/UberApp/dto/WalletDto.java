package com.project.uber.UberApp.dto;

import lombok.Data;

import java.util.List;

@Data
public class WalletDto {
    private Long id;
    private UserDto user;
    private Double balance;
    private List<WalletTransactionDto> transactions;
}
