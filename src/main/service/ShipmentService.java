package main.service;

import main.domain.Cargo;
import main.domain.Shipment;

public class ShipmentService {
    private final PricingService pricingService;
    private final PermissionService permissionService;
    private final ManifestRepository repository;
    private final NotificationService notificationService;

    public ShipmentService(PricingService pricingService, PermissionService permissionService, ManifestRepository repository, NotificationService notificationService) {
        this.pricingService = pricingService;
        this.permissionService = permissionService;
        this.repository = repository;
        this.notificationService = notificationService;
    }

    public String validateCalculatePrintSaveAndNotify(Shipment shipment) {
        if (shipment.getCustomer().isActive() && !shipment.getCargo().isEmpty()) {
            double totalWeight = 0;
            double totalValue = 0;
            boolean hazardous = false;

            for (Cargo item : shipment.getCargo()) {
                totalWeight += item.getWeight();
                totalValue += item.getDeclaredValue();
                if (item.isHazardous()) hazardous = true;
            }

            if (totalWeight > shipment.getShip().getCapacity()) return "ERROR_CAPACITY";
            if (hazardous && !permissionService.canCarryHazardous(shipment.getShip())) return "ERROR_PERMISSION";

            double total = pricingService.calculatePrice(shipment, totalWeight, totalValue, hazardous);
            total += pricingService.calculateInsurance(totalValue, hazardous, shipment.getCustomer());

            shipment.setTotal(total);
            shipment.setStatus("READY");
            String output;
            if (total > 2000) {
                output = "PRIORITY | " + shipment.getReference() + " | " + String.format("%.2f", total);
                repository.save(shipment);
                output += " | " + notificationService.confirmationFor(shipment);
            } else {
                output = "REGULAR | " + shipment.getReference() + " | " + String.format("%.2f", total);
                repository.save(shipment);
                output += " | " + notificationService.confirmationFor(shipment);
            }
            return output;

        } else  if (!shipment.getCustomer().isActive()){
            return "ERROR_CUSTOMER_INACTIVE";
        } else  if (shipment.getCustomer().isSuspended()){
            return "ERROR_CUSTOMER_IS_SUSPENDED";
        } else  if (shipment.getCargo().isEmpty()){
            return "ERROR_EMPTY";
        }

        return "ERROR_CONDRADICTING_CONDITIONS_ARE_BOTH_TRUE";
    }

}
