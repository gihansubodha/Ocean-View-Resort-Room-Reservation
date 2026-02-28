package com.oceanview.service;

import com.oceanview.model.Bill;
import com.oceanview.model.Reservation;

public class ReportingService {

    public void printReservationSummary(Reservation r, Bill b) {
        System.out.println("===== Ocean View Resort - Reservation Summary =====");

        if (r == null) {
            System.out.println("No reservation data.");
            return;
        }

        System.out.println("Reservation ID: " + r.getReservationId());
        System.out.println("Reservation Code: " + r.getReservationCode());
        System.out.println("Guest ID: " + r.getGuestId());
        System.out.println("Room Type ID: " + r.getRoomTypeId());
        System.out.println("Room ID: " + r.getRoomId());
        System.out.println("Check-in: " + r.getCheckIn());
        System.out.println("Check-out: " + r.getCheckOut());
        System.out.println("No. of Guests: " + r.getNumGuests());
        System.out.println("Status: " + r.getStatus());
        System.out.println("Special Requests: " + r.getSpecialRequests());
        System.out.println("Created By: " + r.getCreatedBy());

        if (b != null) {
            System.out.println("--- Billing ---");
            System.out.println("Nights: " + b.getNights());
            System.out.println("Nightly Rate: " + b.getNightlyRate());
            System.out.println("Total: " + b.getTotal());
        }

        System.out.println("===================================================");
    }
}