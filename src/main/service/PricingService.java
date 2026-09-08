package main.service;

import main.domain.Customer;
import main.domain.Planet;
import main.domain.Shipment;

import java.time.LocalDate;

public class PricingService {
    public double increaseByFivePercent(double price) { return price + price * 0.05; }
    public double increaseByTenPercent(double price) { return price + price * 0.10; }
    public double increaseByTwentyPercent(double price) { return price + price * 0.20; }

        //change all gets from parameters to inside calculatePrice
    public double calculatePrice(Shipment shipment, double weight, double declaredValue, boolean hazardous) {
        double result = weight * 2.25;
        final String originSector = shipment.getOrigin().getName();
        final int originSecurity = shipment.getOrigin().getSecurityLevel();
        final String destinationSector = shipment.getDestination().getSector();
        final int destinationSecurity = shipment.getDestination().getSecurityLevel();
        final int loyaltyYears = shipment.getCustomer().getLoyaltyYears();
        final boolean active = shipment.getCustomer().isActive();
        final boolean suspended = shipment.getCustomer().isSuspended();
        final LocalDate departureDate = shipment.getDepartureDate();


        if (declaredValue > 10000) result += declaredValue * 0.015;

        if (hazardous) result = increaseByTwentyPercent(result);

        if (originSecurity >= 4 || destinationSecurity >= 4) result += 125;

        if (!originSector.equals(destinationSector)) result += 80;

        if (departureDate.getMonthValue() == 12 || departureDate.getMonthValue() <= 2) result += 45;

        if (loyaltyYears >= 5 && active && !suspended) result *= 0.90;

        return result;
    }

    public double calculateInsurance(double value, boolean hazardous, Customer customer) {
        value = value * 0.02;
        if (hazardous) value += 75;
        if (customer.getLoyaltyYears() >= 10) value -= 10;
        return Math.max(value, 0);
    }

    public String priceCategory(double price) {
        double temporaryPrice = price;
        return temporaryPrice > 1000 ? "HIGH" : "STANDARD";
    }

    public double calculateRouteSurcharge(Planet origin, Planet destination) {
        int temporary = origin.getSecurityLevel() + destination.getSecurityLevel();
        double surcharge = temporary * 12.5;
        temporary = origin.getSector().equals(destination.getSector()) ? 0 : 1;
        surcharge += temporary * 80;
        return surcharge;
    }

    public Object pricingSummary(double total) {
        return total >= 2000 ? "PRIORITY" : "REGULAR";
    }
}
