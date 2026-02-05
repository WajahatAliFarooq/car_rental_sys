package com.example.car_rental_sys.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.example.car_rental_sys.dto.RentalDTO;
import com.example.car_rental_sys.entity.Car;
import com.example.car_rental_sys.entity.Rental;
import com.example.car_rental_sys.exception.CarRentalException;
import com.example.car_rental_sys.repository.CarRepository;
import com.example.car_rental_sys.repository.RentalRepository;

class RentalServiceTest {

	@Mock
	private RentalRepository rentalRepository;

	@Mock
	private CarRepository carRepository;

	@InjectMocks
	private RentalService rentalService;

	private Car car;
	private Car car2;
	private Rental rental;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);

		car = new Car("Toyota", "Corolla", "WA786", 2025, 100);
		car.setId(1L);

		car2 = new Car("Honda", "Civic", "WA999", 2024, 120);
		car2.setId(2L);

		rental = new Rental("Wajahat", LocalDate.now(), LocalDate.now().plusDays(2), car);
		rental.setId(1L);
	}

	@Test
	void testCreateRental() {
		// Given
		when(carRepository.findById(1L)).thenReturn(Optional.of(car));
		when(rentalRepository.save(any(Rental.class))).thenReturn(rental);

		RentalDTO dto = new RentalDTO(null, "Wajahat", LocalDate.now(), LocalDate.now().plusDays(2), 200, 1L);

		// When
		RentalDTO saved = rentalService.createRental(dto);

		// Then
		// Assert
		assertEquals("Wajahat", saved.getRenterName());

		// Verify
		verify(rentalRepository, times(1)).save(any(Rental.class));
	}

	@Test
	void testCreateRental_NotFound() {
		// Given
		when(carRepository.findById(99L)).thenReturn(Optional.empty());

		RentalDTO dto = new RentalDTO(null, "Wajahat", LocalDate.now(), LocalDate.now().plusDays(2), 200, 99L);

		// When
		CarRentalException exception = assertThrows(CarRentalException.class, () -> rentalService.createRental(dto));

		// Then
		// Assert
		assertEquals("Car not found with id: 99", exception.getMessage());

		// Verify
		verify(rentalRepository, times(0)).save(any());
	}

	@Test
	void testGetAllRentals() {
		// Given
		when(rentalRepository.findAll()).thenReturn(Arrays.asList(rental));

		// When
		List<RentalDTO> rentals = rentalService.getAllRentals();

		// Then
		// Assert
		assertEquals(1, rentals.size());
	}

	@Test
	void testUpdateRental() {
		// Given
		LocalDate newStartDate = LocalDate.now().plusDays(1);
		LocalDate newEndDate = LocalDate.now().plusDays(5);

		rental.setCar(car2);

		RentalDTO dto = new RentalDTO(null, "Ali", newStartDate, newEndDate, 300, car.getId());

		when(rentalRepository.findById(1L)).thenReturn(Optional.of(rental));
		when(carRepository.findById(car.getId())).thenReturn(Optional.of(car));

		when(rentalRepository.save(any(Rental.class))).thenAnswer(invocation -> invocation.getArgument(0));

		// When
		RentalDTO updatedDto = rentalService.updateRental(1L, dto);

		// Then
		assertEquals("Ali", updatedDto.getRenterName());
		assertEquals(newStartDate, updatedDto.getStartDate());
		assertEquals(newEndDate, updatedDto.getEndDate());
		assertEquals(300, updatedDto.getTotalPrice());
		assertEquals(car.getId(), updatedDto.getCarId());

		// Capture
		ArgumentCaptor<Rental> captor = ArgumentCaptor.forClass(Rental.class);
		verify(rentalRepository).save(captor.capture());

		Rental savedRental = captor.getValue();

		// Assert
		assertEquals(car, savedRental.getCar());
		assertNotEquals(car2, savedRental.getCar());
	}

	@Test
	void testUpdateRental_RentalNotFound() {
		// Given
		when(rentalRepository.findById(5L)).thenReturn(Optional.empty());

		RentalDTO dto = new RentalDTO(null, "Ali", LocalDate.now(), LocalDate.now().plusDays(3), 300, 1L);

		// When
		CarRentalException exception = assertThrows(CarRentalException.class,
				() -> rentalService.updateRental(5L, dto));

		// Then
		// Assert
		assertEquals("Rental not found with id: 5", exception.getMessage());

		// Verify
		verify(rentalRepository, times(0)).save(any());
	}

	@Test
	void testUpdateRental_CarNotFound() {
		// Given
		when(rentalRepository.findById(1L)).thenReturn(Optional.of(rental));
		when(carRepository.findById(99L)).thenReturn(Optional.empty());

		RentalDTO dto = new RentalDTO(null, "Ali", LocalDate.now(), LocalDate.now().plusDays(3), 300, 99L);

		// When
		CarRentalException exception = assertThrows(CarRentalException.class,
				() -> rentalService.updateRental(1L, dto));

		// Then
		// Assert
		assertEquals("Car not found with id: 99", exception.getMessage());

		// Verify
		verify(rentalRepository, times(0)).save(any());
	}

	@Test
	void testDeleteRental() {
		// Given
		when(rentalRepository.existsById(1L)).thenReturn(true);

		// When
		rentalService.deleteRental(1L);

		// Then
		// Verify
		verify(rentalRepository, times(1)).deleteById(1L);
	}

	@Test
	void testDeleteRental_NotFound() {
		// Given
		when(rentalRepository.existsById(2L)).thenReturn(false);

		// When
		CarRentalException exception = assertThrows(CarRentalException.class, () -> rentalService.deleteRental(2L));

		// Then
		// Assert
		assertEquals("Rental not found with id: 2", exception.getMessage());

		// Verify
		verify(rentalRepository, never()).deleteById(any());

	}

	@Test
	void testGetRentalById() {
		// Given
		when(rentalRepository.findById(1L)).thenReturn(Optional.of(rental));

		// When
		RentalDTO dto = rentalService.getRentalById(1L);

		// Then
		assertEquals(1L, dto.getId());
		assertEquals("Wajahat", dto.getRenterName());

		verify(rentalRepository, times(1)).findById(1L);
	}

	@Test
	void testGetRentalById_NotFound() {
		// Given
		when(rentalRepository.findById(99L)).thenReturn(Optional.empty());

		// When //Then
		RuntimeException ex = assertThrows(RuntimeException.class, () -> rentalService.getRentalById(99L));
		// Assert
		assertEquals("Rental not found", ex.getMessage());
	}

}
