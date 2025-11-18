package com.eventconnect.controller;

import com.eventconnect.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
//@CrossOrigin("*")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @GetMapping("/analytics")
    public ResponseEntity<Map<String, Object>> getAnalytics() {
        return ResponseEntity.ok(adminService.getAnalytics());
    }

    // Add manage users/events if needed, e.g., @DeleteMapping("/users/{id}")
}