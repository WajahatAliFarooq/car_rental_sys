package com.example.car_rental_sys.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.example.car_rental_sys.dto.RentalDTO;
import com.example.car_rental_sys.entity.Car;
import com.example.car_rental_sys.entity.Rental;
import com.example.car_rental_sys.exception.RentalException;
import com.example.car_rental_sys.repository.CarRepository;
import com.example.car_rental_sys.repository.RentalRepository;

@Service
public class RentalService {

	private final RentalRepository rentalRepository;
	private final CarRepository carRepository;

	public RentalService(RentalRepository rentalRepository, CarRepository carRepository) {
		this.rentalRepository = rentalRepository;
		this.carRepository = carRepository;
	}

	public RentalDTO createRental(RentalDTO rentalDTO) {
		Car car = carRepository.findById(rentalDTO.getCarId())
				.orElseThrow(() -> new RentalException(rentalDTO.getCarId()));

		Rental rental = rentalDTO.toEntity(car);
		Rental saved = rentalRepository.save(rental);
		return RentalDTO.fromEntity(saved);
	}

	public List<RentalDTO> getAllRentals() {
		return rentalRepository.findAll().stream().map(RentalDTO::fromEntity).collect(Collectors.toList());
	}

	public RentalDTO updateRental(Long id, RentalDTO rentalDTO) {
		Rental existingRental = rentalRepository.findById(id).orElseThrow(() -> new RentalException("Rental", id));

		Car car = carRepository.findById(rentalDTO.getCarId())
				.orElseThrow(() -> new RentalException(rentalDTO.getCarId()));

		existingRental.setRenterName(rentalDTO.getRenterName());
		existingRental.setStartDate(rentalDTO.getStartDate());
		existingRental.setEndDate(rentalDTO.getEndDate());
		existingRental.setTotalPrice(rentalDTO.getTotalPrice());
		existingRental.setCar(car);

		Rental updated = rentalRepository.save(existingRental);
		return RentalDTO.fromEntity(updated);
	}

	public void deleteRental(Long id) {
		if (!rentalRepository.existsById(id)) {
			throw new RentalException("Rental", id);
		}
		rentalRepository.deleteById(id);
	}
}