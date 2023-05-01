package com.DriverHiring.Taxi.ModelClasses;

public class NewRideRequestModel {
    String customerLat,customerLng,destinationLat,destinationLng,sourcelat,sourcelng;

    public NewRideRequestModel()
    {

    }

    public NewRideRequestModel(String customerLat, String customerLng, String destinationLat, String destinationLng, String sourcelat, String sourcelng) {
        this.customerLat = customerLat;
        this.customerLng = customerLng;
        this.destinationLat = destinationLat;
        this.destinationLng = destinationLng;
        this.sourcelat = sourcelat;
        this.sourcelng = sourcelng;
    }

    public String getCustomerLat() {
        return customerLat;
    }

    public void setCustomerLat(String customerLat) {
        this.customerLat = customerLat;
    }

    public String getCustomerLng() {
        return customerLng;
    }

    public void setCustomerLng(String customerLng) {
        this.customerLng = customerLng;
    }

    public String getDestinationLat() {
        return destinationLat;
    }

    public void setDestinationLat(String destinationLat) {
        this.destinationLat = destinationLat;
    }

    public String getDestinationLng() {
        return destinationLng;
    }

    public void setDestinationLng(String destinationLng) {
        this.destinationLng = destinationLng;
    }

    public String getSourcelat() {
        return sourcelat;
    }

    public void setSourcelat(String sourcelat) {
        this.sourcelat = sourcelat;
    }

    public String getSourcelng() {
        return sourcelng;
    }

    public void setSourcelng(String sourcelng) {
        this.sourcelng = sourcelng;
    }
}
