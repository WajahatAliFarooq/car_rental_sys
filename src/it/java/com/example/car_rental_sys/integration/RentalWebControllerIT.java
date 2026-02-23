package com.example.car_rental_sys.integration;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import com.example.car_rental_sys.dto.CarDTO;
import com.example.car_rental_sys.dto.RentalDTO;
import com.example.car_rental_sys.mysqlconn.TestContainerCon;
import com.example.car_rental_sys.service.CarService;
import com.example.car_rental_sys.service.RentalService;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class RentalWebControllerIT extends TestContainerCon {

	@Autowired
	private RentalService rentalService;

	@Autowired
	private CarService carService;

	@LocalServerPort
	private int port;

	private WebDriver driver;
	private String baseUrl;

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

	@BeforeEach
	void setup() {
		baseUrl = "http://localhost:" + port + "/rentals";
		driver = new HtmlUnitDriver(true);

		cleanupDatabase();
	}

	@AfterEach
	void teardown() {
		driver.quit();
		cleanupDatabase();
	}

	private void cleanupDatabase() {
		rentalService.getAllRentals().forEach(r -> rentalService.deleteRental(r.getId()));
		carService.getAllCars().forEach(c -> carService.deleteCar(c.getId()));
	}

	@Test
	void testListRentals() {

		// Given
		CarDTO savedCar = carService.createCar(sampleCar());
		RentalDTO savedRental = rentalService.createRental(sampleRental(savedCar.getId()));
		// When
		driver.get(baseUrl);

		// Then
		String tableText = driver.findElement(By.id("rental_table")).getText();

		// Assert
		assertThat(tableText).contains("Ali", savedCar.getId().toString(), "200");

		// Verify
		driver.findElement(By.cssSelector("a[href*='/rentals/" + savedRental.getId() + "/edit']"));
	}

	@Test
	void testCreateRental() {

		// Given
		CarDTO savedCar = carService.createCar(sampleCar());

		driver.get(baseUrl + "/new");

		// When
		driver.findElement(By.name("renterName")).sendKeys("Ali");

		WebElement carIdField = driver.findElement(By.name("carId"));
		carIdField.clear();
		carIdField.sendKeys(savedCar.getId().toString());

		driver.findElement(By.name("startDate")).sendKeys("2025-02-01");
		driver.findElement(By.name("endDate")).sendKeys("2025-02-05");

		WebElement totalPrice = driver.findElement(By.name("totalPrice"));
		totalPrice.clear();
		totalPrice.sendKeys("200");

		driver.findElement(By.name("btn_submit")).click();

		// Then
		RentalDTO created = rentalService.getAllRentals().stream().filter(r -> r.getRenterName().equals("Ali"))
				.findFirst().orElseThrow();

		// Assert
		assertThat(created.getCarId()).isEqualTo(savedCar.getId());
		assertThat(created.getTotalPrice()).isEqualTo(200);

		// Verify
		assertThat(rentalService.getAllRentals()).hasSize(1);
	}

	@Test
	void testUpdateRental() {

		// Given

		CarDTO savedCar = carService.createCar(sampleCar());
		RentalDTO savedRental = rentalService.createRental(sampleRental(savedCar.getId()));

		// When
		driver.get(baseUrl + "/" + savedRental.getId() + "/edit");

		WebElement renterField = driver.findElement(By.name("renterName"));
		renterField.clear();
		renterField.sendKeys("Ali");

		WebElement priceField = driver.findElement(By.name("totalPrice"));
		priceField.clear();
		priceField.sendKeys("200");

		driver.findElement(By.name("btn_submit")).click();

		// Then
		RentalDTO updated = rentalService.getRentalById(savedRental.getId());

		// Assert
		assertThat(updated.getRenterName()).isEqualTo("Ali");
		assertThat(updated.getTotalPrice()).isEqualTo(200);

		// Verify
		assertThat(updated.getCarId()).isEqualTo(savedCar.getId());
	}

}
