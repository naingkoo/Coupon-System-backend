package com.coupon.model;

import java.util.List;

public class PurchaseRequest {

    private PurchaseDTO purchaseDTO;
    private List<PackageDTO> selectedPackages;

    public PurchaseDTO getPurchaseDTO() {
        return purchaseDTO;
    }

    public void setPurchaseDTO(PurchaseDTO purchaseDTO) {
        this.purchaseDTO = purchaseDTO;
    }

    public List<PackageDTO> getSelectedPackages() {
        return selectedPackages;
    }

    public void setSelectedPackages(List<PackageDTO> selectedPackages) {
        this.selectedPackages = selectedPackages;
    }
}
