package com.example.model;

import java.io.Serializable;
import java.math.BigDecimal;

public class Ride implements Serializable {
    private int id;
    private Integer riderId;       // nullable
    private Integer driverId;      // nullable
    private String pickup;
    private String dropoff;
    private String status;         // ASSIGNED, ACCEPTED, ON_THE_WAY, IN_RIDE, COMPLETED
    private BigDecimal fare;       // nullable
    private BigDecimal distanceKm; // nullable

    public Ride() {}

    public Ride(int id, String pickup, String dropoff, String status) {
        this.id = id;
        this.pickup = pickup;
        this.dropoff = dropoff;
        this.status = status;
    }

    // --- getters/setters ---
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public Integer getRiderId() { return riderId; }
    public void setRiderId(Integer riderId) { this.riderId = riderId; }

    public Integer getDriverId() { return driverId; }
    public void setDriverId(Integer driverId) { this.driverId = driverId; }

    public String getPickup() { return pickup; }
    public void setPickup(String pickup) { this.pickup = pickup; }

    public String getDropoff() { return dropoff; }
    public void setDropoff(String dropoff) { this.dropoff = dropoff; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public BigDecimal getFare() { return fare; }
    public void setFare(BigDecimal fare) { this.fare = fare; }

    public BigDecimal getDistanceKm() { return distanceKm; }
    public void setDistanceKm(BigDecimal distanceKm) { this.distanceKm = distanceKm; }

    @Override
    public String toString() {
        return "Ride{" +
                "id=" + id +
                ", riderId=" + riderId +
                ", driverId=" + driverId +
                ", pickup='" + pickup + '\'' +
                ", dropoff='" + dropoff + '\'' +
                ", status='" + status + '\'' +
                ", fare=" + fare +
                ", distanceKm=" + distanceKm +
                '}';
    }
}
