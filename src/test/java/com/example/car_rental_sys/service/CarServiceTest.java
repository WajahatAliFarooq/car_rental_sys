
package com.example.car_rental_sys.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.example.car_rental_sys.dto.CarDTO;
import com.example.car_rental_sys.entity.Car;
import com.example.car_rental_sys.exception.CarRentalException;
import com.example.car_rental_sys.repository.CarRepository;

class CarServiceTest {

	@Mock
	private CarRepository carRepository;

	@InjectMocks
	private CarService carService;

	private Car car;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);

		// Given
		car = new Car("Toyota", "Corolla", "WA786", 2025, 100);
		car.setId(1L);
	}

	@Test
	void testCreateCar() {
		// Given
		CarDTO dto = new CarDTO(null, "Toyota", "Corolla", "WA786", 2025, 100, true);
		when(carRepository.save(any(Car.class))).thenReturn(car);

		// When
		CarDTO saved = carService.createCar(dto);

		// Then
		// Assert
		assertNotNull(saved);
		assertEquals(car.getId(), saved.getId());

		// Verify
		verify(carRepository, times(1)).save(any(Car.class));
	}

	@Test
	void testGetAllCars() {
		// Given
		when(carRepository.findAll()).thenReturn(Arrays.asList(car));

		// When
		List<CarDTO> cars = carService.getAllCars();

		// Then
		// Assert
		assertEquals(1, cars.size());
	}

	@Test
	void testUpdateCar() {
		// Given
		Car updatedCar = new Car("Honda", "Civic", "WA786", 2025, 100);
		updatedCar.setId(1L);

		when(carRepository.findById(1L)).thenReturn(Optional.of(car));
		when(carRepository.save(any(Car.class))).thenReturn(updatedCar);

		CarDTO dto = new CarDTO(null, "Honda", "Civic", "WA786", 2025, 100, true);

		// When
		CarDTO updatedDto = carService.updateCar(1L, dto);

		// Then
		// Assert
		assertEquals("Honda", updatedDto.getMake());

		// Verify
		verify(carRepository, times(1)).save(any(Car.class));
	}

	@Test
	void testUpdateCar_CarNotFound_ThrowException() {
		// Given
		Long carId = 99L;
		when(carRepository.findById(carId)).thenReturn(Optional.empty());

		CarDTO dto = new CarDTO(null, "Honda", "Civic", "WA786", 2025, 100, true);

		// When
		CarRentalException exception = assertThrows(CarRentalException.class, () -> carService.updateCar(carId, dto));

		// Then
		// Assert
		assertEquals("Car not found with id: " + carId, exception.getMessage());

		// Verify
		verify(carRepository, never()).save(any());
	}

	@Test
	void testDeleteCar() {
		// Given
		when(carRepository.existsById(1L)).thenReturn(true);

		// When
		carService.deleteCar(1L);

		// Then
		// Verify
		verify(carRepository, times(1)).deleteById(1L);
	}

	@Test
	void testDeleteCar_NotFound() {
		// Given
		when(carRepository.existsById(2L)).thenReturn(false);

		// When
		CarRentalException exception = assertThrows(CarRentalException.class, () -> carService.deleteCar(2L));

		// Then
		// Assert
		assertEquals("Car not found with id: 2", exception.getMessage());
	}
}