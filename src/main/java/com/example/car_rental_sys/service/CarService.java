package com.example.car_rental_sys.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.car_rental_sys.dto.CarDTO;
import com.example.car_rental_sys.entity.Car;
import com.example.car_rental_sys.exception.CarRentalException;
import com.example.car_rental_sys.repository.CarRepository;
import com.example.car_rental_sys.repository.RentalRepository;

@Service
public class CarService {

	private final CarRepository carRepository;
	private final RentalRepository rentalRepository;

	public CarService(CarRepository carRepository, RentalRepository rentalRepository) {
		this.carRepository = carRepository;
		this.rentalRepository = rentalRepository;
	}

	public CarDTO createCar(CarDTO carDTO) {
		Car car = carDTO.toEntity();
		Car saved = carRepository.save(car);
		return CarDTO.fromEntity(saved);
	}

	public List<CarDTO> getAllCars() {
		return carRepository.findAll().stream().map(CarDTO::fromEntity).toList();
	}

	public CarDTO updateCar(Long id, CarDTO carDTO) {
		Car existingCar = carRepository.findById(id).orElseThrow(() -> new CarRentalException(id));

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
			throw new CarRentalException(id);
		}

		if (rentalRepository.existsByCarId(id)) {
			throw new CarRentalException("Car cannot be deleted, it has active rentals");
		}

		carRepository.deleteById(id);
	}

	public CarDTO getCarById(Long id) {
		return carRepository.findById(id).map(CarDTO::fromEntity).orElseThrow(() -> new CarRentalException(id));
	}

}
