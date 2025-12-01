package com.example.car_rental_sys.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.car_rental_sys.entity.Rental;

public interface RentalRepository extends JpaRepository<Rental, Long> {
}
