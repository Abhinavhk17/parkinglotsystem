package com.parkingsystem.models;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.Duration;

/**
 * Represents a parking ticket issued to a vehicle
 */
public class Ticket {
    private final String ticketId;
    private final Vehicle vehicle;
    private final ParkingSpot assignedSpot;
    private final LocalDateTime entryTime = LocalDateTime.now();
    private LocalDateTime exitTime;
    private double parkingFee;

    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public Ticket(String ticketId, Vehicle vehicle, ParkingSpot assignedSpot) {
        this.ticketId = ticketId;
        this.vehicle = vehicle;
        this.assignedSpot = assignedSpot;
    }

    public void setExitTime(LocalDateTime exitTime) { this.exitTime = exitTime; }
    public void setParkingFee(double parkingFee) { this.parkingFee = parkingFee; }

    public long getParkingDurationInHours() {
        return Duration.between(entryTime, exitTime != null ? exitTime : LocalDateTime.now()).toHours();
    }

    public long getParkingDurationInMinutes() {
        return Duration.between(entryTime, exitTime != null ? exitTime : LocalDateTime.now()).toMinutes();
    }

    public String getFormattedEntryTime() { return entryTime.format(TIME_FORMATTER); }
    public String getFormattedExitTime() { return exitTime != null ? exitTime.format(TIME_FORMATTER) : "Still Parked"; }

    public String getFormattedDuration() {
        long minutes = getParkingDurationInMinutes();
        long hours = minutes / 60;
        return hours > 0 ? String.format("%d hours %d minutes", hours, minutes % 60) : String.format("%d minutes", minutes);
    }

    // Getters
    public String getTicketId() { return ticketId; }
    public Vehicle getVehicle() { return vehicle; }
    public ParkingSpot getAssignedSpot() { return assignedSpot; }
    public LocalDateTime getEntryTime() { return entryTime; }
    public LocalDateTime getExitTime() { return exitTime; }
    public double getParkingFee() { return parkingFee; }

    @Override
    public String toString() {
        return "Ticket{ticketId='" + ticketId + "', vehicle=" + vehicle + ", entryTime=" + entryTime + ", exitTime=" + exitTime + ", parkingFee=" + parkingFee + '}';
    }
}
