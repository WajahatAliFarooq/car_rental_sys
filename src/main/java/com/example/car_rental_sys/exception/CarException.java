package com.example.car_rental_sys.exception;

public class CarException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public CarException(Long id) {
        super("Car not found with id: " + id);
    }
}
