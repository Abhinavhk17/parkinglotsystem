# Parking Lot System

A modular, object-oriented Parking Lot Management System implemented in Java. This project demonstrates core design patterns, thread safety, and extensibility for real-world parking lot operations.

## Features
- **Vehicle Parking & Unparking**: Park and unpark vehicles, generating and closing tickets.
- **Parking Spot Allocation**: Pluggable allocation strategies (e.g., nearest spot).
- **Fee Calculation**: Pluggable fee calculation strategies (e.g., hourly rates).
- **Parking History**: Maintains a history of all parking tickets.
- **Thread Safety**: Uses concurrent collections and locks for safe multi-threaded access.

## Project Structure
```
src/
  com/parkingsystem/
    Main.java                  # Entry point
    core/
      ParkingLot.java          # Main parking lot logic
    enums/
      SpotType.java            # Types of parking spots
      VehicleType.java         # Types of vehicles
    models/
      ParkingSpot.java         # Parking spot model
      Ticket.java              # Ticket model
      Vehicle.java             # Vehicle model
    services/
      FeeCalculator.java       # Fee calculation interface
      ParkingSpotAllocator.java# Spot allocation interface
      impl/
        HourlyFeeCalculator.java   # Hourly fee calculation
        NearestSpotAllocator.java # Nearest spot allocation
```

## How It Works
1. **ParkingLot**: Central class managing parking spots, tickets, and history.
2. **ParkingSpotAllocator**: Strategy interface for allocating parking spots. Example: `NearestSpotAllocator`.
3. **FeeCalculator**: Strategy interface for calculating parking fees. Example: `HourlyFeeCalculator`.
4. **Models**: Represent vehicles, parking spots, and tickets.
5. **Enums**: Define types for spots and vehicles.

## Usage

### 1. Build
Compile the project using your preferred Java IDE or via command line:

```
javac -d out src/com/parkingsystem/**/*.java
```

### 2. Run
Run the main class:

```
java -cp out com.parkingsystem.Main
```





