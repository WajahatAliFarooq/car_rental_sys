package com.example.car_rental_sys.service;

import org.springframework.stereotype.Service;

import com.example.car_rental_sys.dto.CarDTO;
import com.example.car_rental_sys.entity.Car;
import com.example.car_rental_sys.repository.CarRepository;

@Service
public class CarService {

	private final CarRepository carRepository;

	public CarService(CarRepository carRepository) {
		this.carRepository = carRepository;
	}

	public CarDTO createCar(CarDTO carDTO) {
		Car car = carDTO.toEntity();
		Car saved = carRepository.save(car);
		return CarDTO.fromEntity(saved);
	}

}
