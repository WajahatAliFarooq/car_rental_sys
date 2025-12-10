package com.example.car_rental_sys.service;

import java.util.List;
import java.util.stream.Collectors;

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

	public List<CarDTO> getAllCars() {
		return carRepository.findAll().stream().map(CarDTO::fromEntity).collect(Collectors.toList());
	}

	public CarDTO updateCar(Long id, CarDTO carDTO) {
		Car existingCar = carRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("Car not found with id: " + id));

		existingCar.setMake(carDTO.getMake());
		existingCar.setModel(carDTO.getModel());
		existingCar.setPlate(carDTO.getPlate());
		existingCar.setYear(carDTO.getYear());
		existingCar.setDailyPrice(carDTO.getDailyPrice());
		existingCar.setAvailable(carDTO.isAvailable());

		Car updated = carRepository.save(existingCar);
		return CarDTO.fromEntity(updated);
	}

	public void deleteCar(Long id) {
		if (!carRepository.existsById(id)) {
			throw new RuntimeException("Car not found with id: " + id);
		}
		carRepository.deleteById(id);
	}

}
