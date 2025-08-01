package com.parkingsystem.core;

import com.parkingsystem.models.ParkingSpot;
import com.parkingsystem.models.Ticket;
import com.parkingsystem.models.Vehicle;
import com.parkingsystem.services.FeeCalculator;
import com.parkingsystem.services.ParkingSpotAllocator;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Main ParkingLot class that manages the entire parking system
 */
public class ParkingLot {
    private final List<ParkingSpot> parkingSpots = Collections.synchronizedList(new ArrayList<>());
    private final Map<String, Ticket> activeTickets = new ConcurrentHashMap<>();
    private final List<Ticket> parkingHistory = Collections.synchronizedList(new ArrayList<>());
    private final ParkingSpotAllocator spotAllocator;
    private final FeeCalculator feeCalculator;
    private final AtomicInteger ticketCounter = new AtomicInteger(1);
    private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();

    public ParkingLot(ParkingSpotAllocator spotAllocator, FeeCalculator feeCalculator) {
        this.spotAllocator = spotAllocator;
        this.feeCalculator = feeCalculator;
    }

    public void addParkingSpot(ParkingSpot spot) { parkingSpots.add(spot); }

    public synchronized Optional<Ticket> parkVehicle(Vehicle vehicle) {
        lock.writeLock().lock();
        try {
            Optional<ParkingSpot> assignedSpot = spotAllocator.findAvailableSpot(vehicle, getAvailableSpots());
            if (assignedSpot.isPresent()) {
                ParkingSpot spot = assignedSpot.get();
                if (!spot.isAvailable()) return Optional.empty();
                spot.occupySpot(vehicle);
                String ticketId = String.valueOf(ticketCounter.getAndIncrement());
                Ticket ticket = new Ticket(ticketId, vehicle, spot);
                activeTickets.put(ticketId, ticket);
                return Optional.of(ticket);
            }
            return Optional.empty();
        } finally { lock.writeLock().unlock(); }
    }

    public synchronized Optional<Double> exitVehicle(String ticketId) {
        lock.writeLock().lock();
        try {
            Ticket ticket = activeTickets.get(ticketId);
            if (ticket == null) return Optional.empty();
            ticket.setExitTime(LocalDateTime.now());
            double fee = feeCalculator.calculateFee(ticket);
            ticket.setParkingFee(fee);
            ticket.getAssignedSpot().freeSpot();
            activeTickets.remove(ticketId);
            parkingHistory.add(ticket);
            if (parkingHistory.size() > 50) parkingHistory.remove(0);
            return Optional.of(fee);
        } finally { lock.writeLock().unlock(); }
    }

    public List<ParkingSpot> getAvailableSpots() {
        lock.readLock().lock();
        try { return parkingSpots.stream().filter(ParkingSpot::isAvailable).toList(); }
        finally { lock.readLock().unlock(); }
    }

    public int getTotalSpots() { return parkingSpots.size(); }
    public int getOccupiedSpots() { return activeTickets.size(); }
    public List<Ticket> getParkingHistory() { lock.readLock().lock(); try { return new ArrayList<>(parkingHistory); } finally { lock.readLock().unlock(); } }
    public Map<String, Ticket> getCurrentlyParkedVehicles() { return new HashMap<>(activeTickets); }
}
