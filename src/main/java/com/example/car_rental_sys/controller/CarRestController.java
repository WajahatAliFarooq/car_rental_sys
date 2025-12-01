package com.example.car_rental_sys.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.car_rental_sys.dto.CarDTO;
import com.example.car_rental_sys.service.CarService;

@RestController
@RequestMapping("/api/cars")
class CarController {

	private final CarService carService;

	public CarController(CarService carService) {
		this.carService = carService;
	}

	@PostMapping("/create")
	public CarDTO createCar(@RequestBody CarDTO carDTO) {
		return carService.createCar(carDTO);
	}
}
