package com.example.car_rental_sys.integration;

import static org.assertj.core.api.Assertions.assertThat;

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
import com.example.car_rental_sys.mysqlconn.TestContainerCon;
import com.example.car_rental_sys.service.CarService;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CarWebControllerIT extends TestContainerCon {

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

	@BeforeEach
	void setup() {
		baseUrl = "http://localhost:" + port;
		driver = new HtmlUnitDriver(true);

		cleanupDatabase();
	}

	@AfterEach
	void teardown() {
		driver.quit();
		cleanupDatabase();
	}
	
	private void cleanupDatabase() {
	    carService.getAllCars().forEach(c -> carService.deleteCar(c.getId()));
	}

	@Test
	void testListCars() {

		// Given
		CarDTO savedCar = carService.createCar(sampleCar());

		// When
		driver.get(baseUrl);

		// Then
		String tableText = driver.findElement(By.id("car_table")).getText();

		// Assert
		assertThat(tableText).contains("Toyota", "Corolla", "WA786", "100", "true");

		// Verify
		driver.findElement(By.cssSelector("a[href*='/" + savedCar.getId() + "/edit']"));
	}

	@Test
	void testCreateCar() {

		// Given
		driver.get(baseUrl + "/new");

		// When
		driver.findElement(By.name("make")).sendKeys("Toyota");
		driver.findElement(By.name("model")).sendKeys("Corolla");
		driver.findElement(By.name("plate")).sendKeys("WA786");
		driver.findElement(By.name("year")).sendKeys("2025");

		WebElement priceField = driver.findElement(By.name("dailyPrice"));
		priceField.clear();
		priceField.sendKeys("100");

		WebElement available = driver.findElement(By.name("available"));
		if (!available.isSelected()) {
			available.click();
		}

		driver.findElement(By.name("btn_submit")).click();

		// Then
		driver.get(baseUrl);

		CarDTO created = carService.getAllCars().stream().filter(c -> c.getPlate().equals("WA786")).findFirst()
				.orElseThrow();

		// Assert
		assertThat(created.getMake()).isEqualTo("Toyota");
		assertThat(created.getDailyPrice()).isEqualTo(100);

		// Verify
		assertThat(carService.getAllCars().size()).isEqualTo(1);
	}

	@Test
	void testUpdateCar() {

		// Given
		CarDTO savedCar = carService.createCar(sampleCar());

		// When
		driver.get(baseUrl + "/" + savedCar.getId() + "/edit");

		WebElement model = driver.findElement(By.name("model"));
		model.clear();
		model.sendKeys("Corolla");

		WebElement price = driver.findElement(By.name("dailyPrice"));
		price.clear();
		price.sendKeys("100");

		driver.findElement(By.name("btn_submit")).click();

		// Then
		CarDTO updated = carService.getCarById(savedCar.getId());

		// Assert
		assertThat(updated.getModel()).isEqualTo("Corolla");
		assertThat(updated.getDailyPrice()).isEqualTo(100);

		// Verify
		assertThat(updated.getMake()).isEqualTo("Toyota");
	}

}
