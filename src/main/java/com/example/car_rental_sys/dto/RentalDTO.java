package com.example.car_rental_sys.dto;

import java.time.LocalDate;

import com.example.car_rental_sys.entity.Car;
import com.example.car_rental_sys.entity.Rental;

public class RentalDTO {

	private Long id;
	private String renterName;
	private LocalDate startDate;
	private LocalDate endDate;
	private int totalPrice;
	private Long carId;

	public RentalDTO() {
	}

	public RentalDTO(Long id, String renterName, LocalDate startDate, LocalDate endDate, int totalPrice, Long carId) {
		this.id = id;
		this.renterName = renterName;
		this.startDate = startDate;
		this.endDate = endDate;
		this.totalPrice = totalPrice;
		this.carId = carId;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getRenterName() {
		return renterName;
	}

	public void setRenterName(String renterName) {
		this.renterName = renterName;
	}

	public LocalDate getStartDate() {
		return startDate;
	}

	public void setStartDate(LocalDate startDate) {
		this.startDate = startDate;
	}

	public LocalDate getEndDate() {
		return endDate;
	}

	public void setEndDate(LocalDate endDate) {
		this.endDate = endDate;
	}

	public int getTotalPrice() {
		return totalPrice;
	}

	public void setTotalPrice(int totalPrice) {
		this.totalPrice = totalPrice;
	}

	public Long getCarId() {
		return carId;
	}

	public void setCarId(Long carId) {
		this.carId = carId;
	}

	public static RentalDTO fromEntity(Rental rental) {
		RentalDTO dto = new RentalDTO();
		dto.setId(rental.getId());
		dto.setRenterName(rental.getRenterName());
		dto.setStartDate(rental.getStartDate());
		dto.setEndDate(rental.getEndDate());
		dto.setTotalPrice(rental.getTotalPrice());
		dto.setCarId(rental.getCar() != null ? rental.getCar().getId() : null);
		return dto;
	}

	public Rental toEntity(Car car) {
		Rental rental = new Rental();
		rental.setId(this.id);
		rental.setRenterName(this.renterName);
		rental.setStartDate(this.startDate);
		rental.setEndDate(this.endDate);
		rental.setTotalPrice(this.totalPrice);
		rental.setCar(car);
		return rental;
	}
}