package com.example.car_rental_sys.dto;

import com.example.car_rental_sys.entity.Car;

public class CarDTO {

	private Long id;
	private String make;
	private String model;
	private String plate;
	private int year;
	private int dailyPrice;
	private boolean available;

	public CarDTO() {
	}

	public CarDTO(Long id, String make, String model, String plate, int year, int dailyPrice, boolean available) {
		this.id = id;
		this.make = make;
		this.model = model;
		this.plate = plate;
		this.year = year;
		this.dailyPrice = dailyPrice;
		this.available = available;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getMake() {
		return make;
	}

	public void setMake(String make) {
		this.make = make;
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public String getPlate() {
		return plate;
	}

	public void setPlate(String plate) {
		this.plate = plate;
	}

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public int getDailyPrice() {
		return dailyPrice;
	}

	public void setDailyPrice(int dailyPrice) {
		this.dailyPrice = dailyPrice;
	}

	public boolean isAvailable() {
		return available;
	}

	public void setAvailable(boolean available) {
		this.available = available;
	}

	public static CarDTO fromEntity(Car car) {
		CarDTO dto = new CarDTO();
		dto.setId(car.getId());
		dto.setMake(car.getMake());
		dto.setModel(car.getModel());
		dto.setPlate(car.getPlate());
		dto.setYear(car.getYear());
		dto.setDailyPrice(car.getDailyPrice());
		dto.setAvailable(car.isAvailable());

		return dto;
	}

	public Car toEntity() {
		Car car = new Car();
		car.setId(this.id);
		car.setMake(this.make);
		car.setModel(this.model);
		car.setPlate(this.plate);
		car.setYear(this.year);
		car.setDailyPrice(this.dailyPrice);
		car.setAvailable(this.available);
		return car;
	}
}