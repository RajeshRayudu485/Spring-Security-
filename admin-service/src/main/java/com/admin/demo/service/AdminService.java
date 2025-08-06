package com.admin.demo.service;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.admin.demo.model.Admin;
import com.admin.demo.repository.AdminRepository;

@Service
public class AdminService {

    @Autowired
    private AdminRepository adminRepository;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public Optional<Admin> findByEmail(String email) {
        return adminRepository.findByEmail(email);
    }

    public boolean emailExists(String email) {
        return adminRepository.findByEmail(email).isPresent();
    }

    public Admin registerAdmin(Admin admin) {
        // Hash password before saving
        admin.setPassword(passwordEncoder.encode(admin.getPassword()));
        return adminRepository.save(admin);
    }

    public boolean checkPassword(Admin admin, String rawPassword) {
        return passwordEncoder.matches(rawPassword, admin.getPassword());
    }

    public Admin getAdminById(Long adminId) {
        return adminRepository.findById(adminId).orElse(null);
    }

}
