package com.parkiyo.parkiyo.service;

import com.parkiyo.parkiyo.dto.EntryRequest;
import com.parkiyo.parkiyo.dto.EntryResponse;
import org.springframework.stereotype.Service;

@Service
public class EntryService {

    // This is a basic version for now
    public EntryResponse processEntry(EntryRequest request) {

        // TODO: Add real logic later (slot assignment, duplicate check, etc.)

        System.out.println("Vehicle Entry Request Received:");
        System.out.println("License Plate: " + request.getLicensePlate());
        System.out.println("Vehicle Type : " + request.getVehicleType());

        EntryResponse response = new EntryResponse();
        response.setLicensePlate(request.getLicensePlate());
        response.setVehicleType(request.getVehicleType());
        response.setMessage("Entry request received successfully");
        response.setStatus("SUCCESS");

        return response;
    }
}