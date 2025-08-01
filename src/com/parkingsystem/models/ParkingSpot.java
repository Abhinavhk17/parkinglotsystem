package com.parkingsystem.models;

import com.parkingsystem.enums.SpotType;

public class ParkingSpot {
    private final String spotId;
    private final SpotType spotType;
    private final int floor;
    private volatile boolean isOccupied;
    private volatile Vehicle parkedVehicle;

    public ParkingSpot(String spotId, SpotType spotType, int floor) {
        this.spotId = spotId;
        this.spotType = spotType;
        this.floor = floor;
    }

    public synchronized boolean isAvailable() { return !isOccupied; }
    public synchronized boolean canFitVehicle(Vehicle vehicle) { return isAvailable() && spotType.canFit(vehicle.getType()); }

    public synchronized void occupySpot(Vehicle vehicle) {
        if (!canFitVehicle(vehicle)) throw new IllegalStateException("Cannot park vehicle in this spot");
        this.isOccupied = true;
        this.parkedVehicle = vehicle;
    }

    public synchronized void freeSpot() { this.isOccupied = false; this.parkedVehicle = null; }

    public String getSpotId() { return spotId; }
    public SpotType getSpotType() { return spotType; }
    public int getFloor() { return floor; }
    public synchronized boolean isOccupied() { return isOccupied; }
    public synchronized Vehicle getParkedVehicle() { return parkedVehicle; }
}
