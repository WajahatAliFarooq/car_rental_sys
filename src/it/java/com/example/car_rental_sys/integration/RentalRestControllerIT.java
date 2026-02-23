package com.example.car_rental_sys.integration;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.example.car_rental_sys.dto.CarDTO;
import com.example.car_rental_sys.dto.RentalDTO;
import com.example.car_rental_sys.entity.Car;
import com.example.car_rental_sys.entity.Rental;
import com.example.car_rental_sys.mysqlconn.TestContainerCon;
import com.example.car_rental_sys.repository.CarRepository;
import com.example.car_rental_sys.repository.RentalRepository;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class RentalRestControllerIT extends TestContainerCon {

	@LocalServerPort
	private int port;

	@Autowired
	private RentalRepository rentalRepository;

	@Autowired
	private CarRepository carRepository;

	@Autowired
	private TestRestTemplate restTemplate;

	private String baseUrl;

	@BeforeEach
	void setup() {
		baseUrl = "http://localhost:" + port + "/api/rentals";

		rentalRepository.deleteAll();
		rentalRepository.flush();
		carRepository.deleteAll();
		carRepository.flush();
	}

	@AfterEach
	void teardown() {

		rentalRepository.deleteAll();
		rentalRepository.flush();
		carRepository.deleteAll();
		carRepository.flush();
	}

	private CarDTO sampleCar() {
		CarDTO car = new CarDTO();
		car.setMake("Toyota");
		car.setModel("Corolla");
		car.setPlate("WA786");
		car.setYear(2025);
		car.setDailyPrice(100);
		car.setAvailable(true);
		return car;
	}

	private RentalDTO sampleRental(Long carId) {
		RentalDTO rental = new RentalDTO();
		rental.setRenterName("Ali");
		rental.setCarId(carId);
		rental.setStartDate(LocalDate.now());
		rental.setEndDate(LocalDate.now().plusDays(2));
		rental.setTotalPrice(200);
		return rental;
	}

	@Test
	void testCreateRental() {
		// Given
		Car carEntity = carRepository.save(sampleCar().toEntity());

		RentalDTO dto = sampleRental(carEntity.getId());

		// When
		ResponseEntity<RentalDTO> response = restTemplate.postForEntity(baseUrl + "/create", dto, RentalDTO.class);

		// Then
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

		RentalDTO saved = response.getBody();

		// Assert
		Rental fromDb = rentalRepository.findById(saved.getId()).get();
		assertThat(fromDb.getRenterName()).isEqualTo("Ali");
		assertThat(fromDb.getCar().getId()).isEqualTo(carEntity.getId());
		assertThat(fromDb.getTotalPrice()).isEqualTo(200);
	}

	@Test
	void testGetAllRentals() {
		// Given
		Car carEntity = carRepository.save(sampleCar().toEntity());

		Rental rental1 = rentalRepository.save(sampleRental(carEntity.getId()).toEntity(carEntity));

		Rental rental2 = rentalRepository.save(sampleRental(carEntity.getId()).toEntity(carEntity));

		// When
		ResponseEntity<RentalDTO[]> response = restTemplate.getForEntity(baseUrl + "/getall", RentalDTO[].class);

		// Then
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

		RentalDTO[] rentals = response.getBody();

		// Assert
		assertThat(rentals).isNotNull().hasSize(2).extracting(RentalDTO::getId)
				.containsExactlyInAnyOrder(rental1.getId(), rental2.getId());
	}

	@Test
	void testUpdateRental() {
		// Given
		Car car1 = carRepository.save(sampleCar().toEntity());
		Rental saved = rentalRepository.save(sampleRental(car1.getId()).toEntity(car1));
		Car car2 = carRepository.save(new CarDTO(null, "Honda", "Civic", "HC123", 2024, 90, true).toEntity());

		RentalDTO updateDto = sampleRental(car2.getId());
		updateDto.setRenterName("Ahmed");
		updateDto.setTotalPrice(300);

		HttpEntity<RentalDTO> request = new HttpEntity<>(updateDto);

		// When
		ResponseEntity<RentalDTO> response = restTemplate.exchange(baseUrl + "/update/" + saved.getId(), HttpMethod.PUT,
				request, RentalDTO.class);

		// Then
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

		// Assert
		Rental updated = rentalRepository.findById(saved.getId()).get();
		assertThat(updated.getRenterName()).isEqualTo("Ahmed");
		assertThat(updated.getCar().getId()).isEqualTo(car2.getId());
		assertThat(updated.getTotalPrice()).isEqualTo(300);
	}

	@Test
	void testDeleteRental() {
		// Given
		Car carEntity = carRepository.save(sampleCar().toEntity());
		Rental saved = rentalRepository.save(sampleRental(carEntity.getId()).toEntity(carEntity));

		// When
		restTemplate.delete(baseUrl + "/delete/" + saved.getId());

		// Then
		assertThat(rentalRepository.findById(saved.getId())).isEmpty();
	}
}
