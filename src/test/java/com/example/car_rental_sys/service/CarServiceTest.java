
package com.example.car_rental_sys.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
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
import org.mockito.ArgumentCaptor;
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
		Car existingCar = new Car("Toyota", "Yaris", "OLD123", 2020, 50);
		existingCar.setId(1L);
		existingCar.setAvailable(false);

		when(carRepository.findById(1L)).thenReturn(Optional.of(existingCar));
		when(carRepository.save(any(Car.class))).thenAnswer(invocation -> invocation.getArgument(0));

		CarDTO dto = new CarDTO(null, "Honda", "Civic", "WA786", 2025, 100, true);

		// When
		CarDTO updatedDto = carService.updateCar(1L, dto);

		// Then
		assertEquals("Honda", updatedDto.getMake());
		assertEquals("Civic", updatedDto.getModel());
		assertEquals("WA786", updatedDto.getPlate());
		assertEquals(2025, updatedDto.getYear());
		assertEquals(100, updatedDto.getDailyPrice());
		assertTrue(updatedDto.isAvailable());

		// Then
		ArgumentCaptor<Car> captor = ArgumentCaptor.forClass(Car.class);
		verify(carRepository).save(captor.capture());

		Car savedCar = captor.getValue();

		assertEquals("Honda", savedCar.getMake());
		assertEquals("Civic", savedCar.getModel());
		assertEquals("WA786", savedCar.getPlate());
		assertEquals(2025, savedCar.getYear());
		assertEquals(100, savedCar.getDailyPrice());
		assertTrue(savedCar.isAvailable());
	}

	@Test
	void testUpdateCar_NotFound() {
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

	@Test
	void testGetCarById() {
		// Given
		when(carRepository.findById(1L)).thenReturn(Optional.of(car));

		// When
		CarDTO result = carService.getCarById(1L);

		// Then
		assertNotNull(result);
		assertEquals(car.getId(), result.getId());
		assertEquals(car.getMake(), result.getMake());

		// Verify
		verify(carRepository, times(1)).findById(1L);
	}

	@Test
	void testGetCarById_NotFound() {
		// Given
		when(carRepository.findById(99L)).thenReturn(Optional.empty());

		// When
		assertThrows(CarRentalException.class, () -> {
			carService.getCarById(99L);
		});

		// Then
		// Verify
		verify(carRepository).findById(99L);
	}

}