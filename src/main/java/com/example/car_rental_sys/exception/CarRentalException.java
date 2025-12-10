package com.example.car_rental_sys.exception;

public class CarRentalException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public CarRentalException(Long id) {
        super("Car not found with id: " + id);
    }
}
