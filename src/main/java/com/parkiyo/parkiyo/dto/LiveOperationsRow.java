package com.parkiyo.parkiyo.dto;

import com.parkiyo.parkiyo.model.ParkingRecord;
import com.parkiyo.parkiyo.model.Vehicle;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/** Admin Live Ops table: parking session or vehicle registered but never entered. */
@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class LiveOperationsRow {

    private final boolean registeredOnly;
    private final ParkingRecord parkingRecord;
    private final Vehicle vehicle;

    public static LiveOperationsRow parkingSession(ParkingRecord record) {
        return new LiveOperationsRow(false, record, null);
    }

    public static LiveOperationsRow registeredVehicle(Vehicle vehicle) {
        return new LiveOperationsRow(true, null, vehicle);
    }
}
