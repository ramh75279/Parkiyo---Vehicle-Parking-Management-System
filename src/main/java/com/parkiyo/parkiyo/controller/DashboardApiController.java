package com.parkiyo.parkiyo.controller;

import com.parkiyo.parkiyo.dto.DashboardStats;
import com.parkiyo.parkiyo.dto.SlotOccupancySummary;
import com.parkiyo.parkiyo.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/dashboard")
@PreAuthorize("hasRole('ADMIN')")
@RequiredArgsConstructor
public class DashboardApiController {

    private final DashboardService dashboardService;

    @GetMapping("/stats")
    public Map<String, Object> getStats() {
        DashboardStats stats = dashboardService.getAdminDashboardStats();
        SlotOccupancySummary slots = dashboardService.getSlotOccupancySummary();

        Map<String, Object> response = new HashMap<>();
        response.put("totalSlots", stats.getTotalSlots());
        response.put("occupiedSlots", stats.getOccupiedSlots());
        response.put("availableSlots", stats.getAvailableSlots());
        response.put("totalUsers", stats.getTotalUsers());
        response.put("totalVehicles", stats.getTotalVehicles());
        response.put("activeRecords", stats.getActiveRecords());
        response.put("totalRevenue", stats.getTotalRevenue());
        response.put("occupancyRate", slots.getOccupancyRate());
        response.put("unreadNotifications", dashboardService.getAdminUnreadNotificationCount());
        return response;
    }
}