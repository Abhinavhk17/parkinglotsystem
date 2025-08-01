package com.parkingsystem.services;

import com.parkingsystem.models.ParkingSpot;
import com.parkingsystem.models.Vehicle;
import java.util.List;
import java.util.Optional;

/**
 * Interface for parking spot allocation strategy (Strategy Pattern + DIP)
 */
public interface ParkingSpotAllocator {
    Optional<ParkingSpot> findAvailableSpot(Vehicle vehicle, List<ParkingSpot> availableSpots);
}
