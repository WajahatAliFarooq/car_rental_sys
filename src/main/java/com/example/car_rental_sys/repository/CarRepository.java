package com.example.car_rental_sys.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.car_rental_sys.entity.Car;

public interface CarRepository extends JpaRepository<Car, Long> {
}
