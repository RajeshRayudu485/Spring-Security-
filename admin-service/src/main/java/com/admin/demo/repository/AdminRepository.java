package com.admin.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.admin.demo.model.Admin;

import java.util.Optional;

public interface AdminRepository extends JpaRepository<Admin, Long> {
    Optional<Admin> findByEmail(String email);
}

