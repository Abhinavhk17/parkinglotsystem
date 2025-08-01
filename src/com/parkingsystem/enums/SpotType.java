package com.parkingsystem.enums;

public enum SpotType {
    SMALL, MEDIUM, LARGE;

    public boolean canFit(VehicleType vehicleType) {
        switch (vehicleType) {
            case MOTORCYCLE: return true; // Can park anywhere
            case CAR: return this != SMALL; // Medium or Large only
            case TRUCK: return this == LARGE; // Large only
            default: return false;
        }
    }
}
