package main.domain;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Shipment {
    private final String reference;
    private final Customer customer;
    private final Planet origin;
    private final Planet destination;
    private final Ship ship;
    private final LocalDate departureDate;
    private final List<Cargo> cargo = new ArrayList<>();
    private double total;
    private String status = "CREATED";

    public Shipment(String reference, Customer customer, Planet origin, Planet destination, Ship ship, LocalDate departureDate) {
        this.reference = reference;
        this.customer = customer;
        this.origin = origin;
        this.destination = destination;
        this.ship = ship;
        this.departureDate = departureDate;
    }



    public List<Cargo> getCargo() { return cargo; }
    public void addCargo(Cargo item) { cargo.add(item); }

    public double getTotal() { return total; }
    public void setTotal(double total) { this.total = total; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getReference() { return reference; }

    public Customer getCustomer() { return customer; }

    public Planet getOrigin() { return origin; }

    public Planet getDestination() { return destination; }

    public Ship getShip() { return ship; }

    public LocalDate getDepartureDate() { return departureDate; }

}
