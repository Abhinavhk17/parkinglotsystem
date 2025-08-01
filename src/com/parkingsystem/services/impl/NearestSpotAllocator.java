package com.parkingsystem.services.impl;

import com.parkingsystem.enums.VehicleType;
import com.parkingsystem.models.ParkingSpot;
import com.parkingsystem.models.Vehicle;
import com.parkingsystem.services.ParkingSpotAllocator;

import java.util.List;
import java.util.Optional;

public class NearestSpotAllocator implements ParkingSpotAllocator {

    @Override
    public Optional<ParkingSpot> findAvailableSpot(Vehicle vehicle, List<ParkingSpot> availableSpots) {
        return availableSpots.stream()
                .filter(spot -> spot.canFitVehicle(vehicle))
                .min((spot1, spot2) -> {
                    int priority1 = getSpotPriority(vehicle.getType(), spot1.getSpotType());
                    int priority2 = getSpotPriority(vehicle.getType(), spot2.getSpotType());
                    return priority1 != priority2 ? Integer.compare(priority1, priority2) : spot1.getSpotId().compareTo(spot2.getSpotId());
                });
    }

    private int getSpotPriority(VehicleType vehicleType, com.parkingsystem.enums.SpotType spotType) {
        return switch (vehicleType) {
            case MOTORCYCLE -> spotType.ordinal();
            case CAR -> spotType == com.parkingsystem.enums.SpotType.MEDIUM ? 0 : 1;
            case TRUCK -> 0;
        };
    }
}
