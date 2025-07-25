package com.project.uber.UberApp.services;

import com.project.uber.UberApp.entities.Ride;
import com.project.uber.UberApp.entities.User;
import com.project.uber.UberApp.entities.Wallet;
import com.project.uber.UberApp.enums.TransactionMethod;

public interface WalletService {
    Wallet addMoneyToWallet(User user, Double amount, String transactionId, Ride ride, TransactionMethod transactionMethod);
    Wallet deductMoneyFromWallet(User user, Double amount, String transactionId, Ride ride, TransactionMethod transactionMethod);
    void withdrawMoneyFromWallet();
    Wallet findWalletById(Long walletId);
    Wallet createNewWallet(User user);
    Wallet findByUser(User user);
}
