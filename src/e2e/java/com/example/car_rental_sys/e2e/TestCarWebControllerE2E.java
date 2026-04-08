package com.example.car_rental_sys.e2e;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import com.example.car_rental_sys.mysqlconn.TestContainerCon;
import com.example.car_rental_sys.service.CarService;

import io.github.bonigarcia.wdm.WebDriverManager;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class TestCarWebControllerE2E extends TestContainerCon {

	@LocalServerPort
	private int port;

	private WebDriver driver;
	private String baseUrl;

	@Autowired
	private CarService carService;

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
		carService.getAllCars().forEach(c -> carService.deleteCar(c.getId()));
	}

	@Test
	void testCreateNewCar() {

		// Given
		driver.get(baseUrl + "/");

		// When
		driver.findElement(By.cssSelector("a[href*='/new']")).click();

		driver.findElement(By.name("make")).sendKeys("Toyota");
		driver.findElement(By.name("model")).sendKeys("Corolla");
		driver.findElement(By.name("plate")).sendKeys("WA786");
		driver.findElement(By.name("year")).sendKeys("2025");

		WebElement priceField = driver.findElement(By.name("dailyPrice"));
		priceField.clear();
		priceField.sendKeys("100");

		WebElement available = driver.findElement(By.name("available"));
		if (!available.isSelected())
			available.click();

		driver.findElement(By.name("btn_submit")).click();

		// Then
		String tableText = driver.findElement(By.id("car_table")).getText();

		// Assert
		assertThat(tableText).contains("Toyota", "Corolla", "WA786", "100");
	}

}