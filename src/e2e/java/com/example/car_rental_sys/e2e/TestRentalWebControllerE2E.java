package com.example.car_rental_sys.e2e;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.Duration;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import com.example.car_rental_sys.dto.CarDTO;
import com.example.car_rental_sys.mysqlconn.TestContainerCon;
import com.example.car_rental_sys.service.CarService;
import com.example.car_rental_sys.service.RentalService;

import io.github.bonigarcia.wdm.WebDriverManager;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class TestRentalWebControllerE2E extends TestContainerCon {

	@LocalServerPort
	private int port;

	private WebDriver driver;
	private String baseUrl;

	@Autowired
	private CarService carService;

	@Autowired
	private RentalService rentalService;

	@BeforeAll
	static void setupClass() {
		WebDriverManager.chromedriver().setup();
	}

	@BeforeEach
	void setup() {
		ChromeOptions options = new ChromeOptions();
		options.addArguments("--headless");
		driver = new ChromeDriver(options);
		baseUrl = "http://localhost:" + port;
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
	void testCreateNewRental() {
		// Given
		CarDTO savedCar = carService.createCar(sampleCar());

		driver.get(baseUrl + "/rentals");

		// When
		driver.findElement(By.cssSelector("a[href*='/new']")).click();

		driver.findElement(By.name("renterName")).sendKeys("Ali");
		driver.findElement(By.name("carId")).sendKeys(savedCar.getId().toString());
		JavascriptExecutor js = (JavascriptExecutor) driver;

		WebElement startDate = driver.findElement(By.name("startDate"));
		WebElement endDate = driver.findElement(By.name("endDate"));

		js.executeScript("arguments[0].value='2026-02-01';", startDate);
		js.executeScript("arguments[0].value='2026-02-05';", endDate);

		driver.findElement(By.name("totalPrice")).sendKeys("500");

		driver.findElement(By.name("btn_submit")).click();

		// Then
		driver.get("http://localhost:" + port + "/rentals");

		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("rental_table")));

		// Assert
		WebElement rentalTable = driver.findElement(By.id("rental_table"));

		assertThat(rentalTable.getText()).contains("Ali").contains(savedCar.getId().toString());
	}
}