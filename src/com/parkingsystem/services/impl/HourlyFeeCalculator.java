package com.parkingsystem.services.impl;

import com.parkingsystem.enums.VehicleType;
import com.parkingsystem.models.Ticket;
import com.parkingsystem.services.FeeCalculator;

public class HourlyFeeCalculator implements FeeCalculator {
    @Override
    public double calculateFee(Ticket ticket) {
        long hours = Math.max(1, ticket.getParkingDurationInHours());
        double rate = switch (ticket.getVehicle().getType()) {
            case MOTORCYCLE -> 5.0;
            case CAR -> 10.0;
            case TRUCK -> 20.0;
        };
        return Math.max(2.0, hours * rate);
    }
}
