package com.example.car_rental_sys.integration;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.example.car_rental_sys.dto.CarDTO;
import com.example.car_rental_sys.entity.Car;
import com.example.car_rental_sys.mysqlconn.TestContainerCon;
import com.example.car_rental_sys.repository.CarRepository;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CarRestControllerIT extends TestContainerCon {

	@LocalServerPort
	private int port;

	@Autowired
	private CarRepository carRepository;

	@Autowired
	private TestRestTemplate restTemplate;

	private String baseUrl;

	@BeforeEach
	public void setup() {
		baseUrl = "http://localhost:" + port + "/api/cars";
		carRepository.deleteAll();
		carRepository.flush();
	}

	@AfterEach
	void teardown() {
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

	@Test
	public void testCreateCar() {
		// Given
		CarDTO dto = sampleCar();

		// When
		ResponseEntity<CarDTO> response = restTemplate.postForEntity(baseUrl + "/create", dto, CarDTO.class);

		// Then
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

		CarDTO saved = response.getBody();

		// Assert
		Car fromDb = carRepository.findById(saved.getId()).get();
		assertThat(fromDb.getMake()).isEqualTo("Toyota");
		assertThat(fromDb.getModel()).isEqualTo("Corolla");
		assertThat(fromDb.getPlate()).isEqualTo("WA786");
		assertThat(fromDb.getYear()).isEqualTo(2025);
		assertThat(fromDb.getDailyPrice()).isEqualTo(100);
		assertThat(fromDb.isAvailable()).isTrue();
	}

}
