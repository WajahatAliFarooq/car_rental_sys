package com.example.car_rental_sys.mysqlconn;

import java.time.Duration;

import org.junit.jupiter.api.BeforeAll;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.junit.jupiter.Testcontainers;

@SuppressWarnings("resource")
@Testcontainers
@ActiveProfiles("ite2etest")
public abstract class TestContainerCon {

	private static final String IMAGE_VERSION = "mysql:5.7";
	private static MySQLContainer<?> mysqlContainer;

	static {
		mysqlContainer = new MySQLContainer<>(IMAGE_VERSION).withDatabaseName("mydb").withUsername("root")
				.withPassword("pass");

		mysqlContainer.setWaitStrategy(Wait.forListeningPort().withStartupTimeout(Duration.ofMinutes(2)));
	}

	@BeforeAll
	public static void startContainer() {
		mysqlContainer.start();
		System.setProperty("DB_URL", mysqlContainer.getJdbcUrl());
		System.setProperty("DB_USERNAME", mysqlContainer.getUsername());
		System.setProperty("DB_PASSWORD", mysqlContainer.getPassword());

	}

}