package com.project.uber.UberApp.strategies.impl;

import com.project.uber.UberApp.entities.Driver;
import com.project.uber.UberApp.entities.Payment;
import com.project.uber.UberApp.entities.Rider;
import com.project.uber.UberApp.enums.PaymentStatus;
import com.project.uber.UberApp.enums.TransactionMethod;
import com.project.uber.UberApp.repositories.PaymentRepository;
import com.project.uber.UberApp.services.WalletService;
import com.project.uber.UberApp.strategies.PaymentStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WalletPaymentStrategy implements PaymentStrategy {

    private final WalletService walletService;
    private final PaymentRepository paymentRepository;

    @Override
    public void processPayment(Payment payment) {
        Rider rider = payment.getRide().getRider();
        walletService.deductMoneyFromWallet(rider.getUser(), payment.getAmount(), null,
                payment.getRide(), TransactionMethod.RIDE);

        Driver driver = payment.getRide().getDriver();

        double platformCommission = payment.getAmount() * PLATFORM_COMMISSION;
        double driverMoney = payment.getAmount() - platformCommission;

        walletService.addMoneyToWallet(driver.getUser(), driverMoney, null,
                payment.getRide(), TransactionMethod.RIDE);

        payment.setPaymentStatus(PaymentStatus.CONFIRMED);
        paymentRepository.save(payment);
    }
}
