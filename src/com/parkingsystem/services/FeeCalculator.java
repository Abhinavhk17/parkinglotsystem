package com.parkingsystem.services;

import com.parkingsystem.models.Ticket;

/**
 * Interface for fee calculation strategy (Strategy Pattern + DIP)
 */
public interface FeeCalculator {
    double calculateFee(Ticket ticket);
}
