package com.example.car_rental_sys.exception;

public class RentalException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public RentalException(Long id) {
		super("Car not found with id: " + id);
	}

	public RentalException(String type, Long rentalId) {
		super(type + " not found with id: " + rentalId);
	}
}
