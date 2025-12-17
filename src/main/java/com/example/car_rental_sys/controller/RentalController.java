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

import com.example.car_rental_sys.dto.RentalDTO;
import com.example.car_rental_sys.service.RentalService;

@RestController
@RequestMapping("/api/rentals")
public class RentalController {

	private final RentalService rentalService;

	public RentalController(RentalService rentalService) {
		this.rentalService = rentalService;
	}

	@PostMapping("/create")
	public RentalDTO createRental(@RequestBody RentalDTO rentalDTO) {
		return rentalService.createRental(rentalDTO);
	}

	@GetMapping("/getall")
	public List<RentalDTO> getAllRentals() {
		return rentalService.getAllRentals();
	}

	@PutMapping("/update/{id}")
	public RentalDTO updateRental(@PathVariable Long id, @RequestBody RentalDTO rentalDTO) {
		return rentalService.updateRental(id, rentalDTO);
	}

	@DeleteMapping("/delete/{id}")
	public void deleteRental(@PathVariable Long id) {
		rentalService.deleteRental(id);
	}
}