package com.parkingsystem;

import com.parkingsystem.core.ParkingLot;
import com.parkingsystem.enums.SpotType;
import com.parkingsystem.enums.VehicleType;
import com.parkingsystem.models.ParkingSpot;
import com.parkingsystem.models.Ticket;
import com.parkingsystem.models.Vehicle;
import com.parkingsystem.services.impl.HourlyFeeCalculator;
import com.parkingsystem.services.impl.NearestSpotAllocator;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Scanner;

/**
 * Simple main class for the Smart Parking Lot System
 */
public class Main {
    public static void main(String[] args) {
        // Initialize parking lot system
        ParkingLot parkingLot = new ParkingLot(new NearestSpotAllocator(), new HourlyFeeCalculator());

        // Setup parking spots
        setupParkingSpots(parkingLot);

        // Display welcome message
        System.out.println("=== Smart Parking Lot System ===\nTotal Spots: " + parkingLot.getTotalSpots());

        // Run simple interactive menu
        runSystem(parkingLot);
    }

    private static void setupParkingSpots(ParkingLot parkingLot) {
        for (int i = 1; i <= 10; i++) parkingLot.addParkingSpot(new ParkingSpot("S" + i, SpotType.SMALL, 1));
        for (int i = 1; i <= 20; i++) parkingLot.addParkingSpot(new ParkingSpot("M" + i, SpotType.MEDIUM, 1));
        for (int i = 1; i <= 5; i++) parkingLot.addParkingSpot(new ParkingSpot("L" + i, SpotType.LARGE, 1));
    }

    private static void runSystem(ParkingLot parkingLot) {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("\n=== Smart Parking Lot System ===\n1. Park Vehicle\n2. Exit Vehicle\n3. Status\n4. View Parking History\n5. Exit System\nChoose: ");

            int choice = scanner.nextInt();
            scanner.nextLine(); // consume newline

            switch (choice) {
                case 1 -> parkVehicle(scanner, parkingLot);
                case 2 -> exitVehicle(scanner, parkingLot);
                case 3 -> showStatus(parkingLot);
                case 4 -> showParkingHistory(parkingLot);
                case 5 -> { System.out.println("Thank you for using the parking system!"); return; }
                default -> System.out.println("Invalid option. Please try again.");
            }
        }
    }

    private static void parkVehicle(Scanner scanner, ParkingLot parkingLot) {
        System.out.print("License plate: ");
        String licensePlate = scanner.nextLine();

        System.out.println("1. Motorcycle  2. Car  3. Truck\nVehicle type: ");
        int typeChoice = scanner.nextInt();
        scanner.nextLine(); // consume newline

        VehicleType vehicleType = switch (typeChoice) {
            case 1 -> VehicleType.MOTORCYCLE; case 2 -> VehicleType.CAR; case 3 -> VehicleType.TRUCK; default -> VehicleType.CAR;
        };

        Optional<Ticket> ticket = parkingLot.parkVehicle(new Vehicle(licensePlate, vehicleType));

        System.out.println(ticket.map(t -> "Ticket: " + t.getTicketId() + " | Spot: " + t.getAssignedSpot().getSpotId() + " | Entry: " + t.getFormattedEntryTime()).orElse("No available spots."));
    }

    private static void exitVehicle(Scanner scanner, ParkingLot parkingLot) {
        System.out.print("Ticket ID: ");
        Optional<Double> fare = parkingLot.exitVehicle(scanner.nextLine());
        System.out.println(fare.map(f -> "Fare: $" + String.format("%.2f", f)).orElse("Invalid ticket ID!"));
    }

    private static void showStatus(ParkingLot parkingLot) {
        System.out.println("\nSTATUS:\nTotal: " + parkingLot.getTotalSpots() + " | Occupied: " + parkingLot.getOccupiedSpots() + " | Available: " + (parkingLot.getTotalSpots() - parkingLot.getOccupiedSpots()));

        Map<String, Ticket> currentVehicles = parkingLot.getCurrentlyParkedVehicles();

        if (!currentVehicles.isEmpty()) {
            System.out.println("\nCurrently Parked:");
            currentVehicles.values().forEach(ticket -> System.out.printf("%s | %s (%s) | Spot %s | Entry: %s | Duration: %s%n",
                ticket.getTicketId(), ticket.getVehicle().getLicensePlate(), ticket.getVehicle().getType(),
                ticket.getAssignedSpot().getSpotId(), ticket.getFormattedEntryTime(), ticket.getFormattedDuration()));
        }
    }

    private static void showParkingHistory(ParkingLot parkingLot) {
        List<Ticket> history = parkingLot.getParkingHistory();

        System.out.println("\nPARKING HISTORY:");

        if (history.isEmpty()) { System.out.println("No completed parking sessions yet."); return; }

        double totalRevenue = history.stream().mapToDouble(Ticket::getParkingFee).sum();
        history.forEach(ticket -> System.out.printf("%s | %s | %s to %s | %s | $%.2f%n",
            ticket.getTicketId(), ticket.getVehicle().getLicensePlate(), ticket.getFormattedEntryTime(),
            ticket.getFormattedExitTime(), ticket.getFormattedDuration(), ticket.getParkingFee()));
        System.out.printf("Total Sessions: %d | Total Revenue: $%.2f%n", history.size(), totalRevenue);
    }
}
