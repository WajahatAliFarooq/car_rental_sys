package com.example.car_rental_sys.controller;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
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

	@GetMapping("getall")
	public List<CarDTO> getAllCars() {
		return carService.getAllCars();
	}

	@PutMapping("/update/{id}")
	public CarDTO updateCar(@PathVariable Long id, @RequestBody CarDTO carDTO) {
		return carService.updateCar(id, carDTO);
	}

	@DeleteMapping("/delete/{id}")
	public void deleteCar(@PathVariable Long id) {
		carService.deleteCar(id);
	}
}
