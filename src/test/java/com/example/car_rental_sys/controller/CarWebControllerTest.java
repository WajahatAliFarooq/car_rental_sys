package com.example.car_rental_sys.controller;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.example.car_rental_sys.dto.CarDTO;
import com.example.car_rental_sys.service.CarService;

@WebMvcTest(CarWebController.class)
class CarWebControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockitoBean
	private CarService carService;

	private CarDTO sampleCar() {
		CarDTO dto = new CarDTO();
		dto.setId(1L);
		dto.setMake("Toyota");
		dto.setModel("Corolla");
		dto.setPlate("WA786");
		dto.setYear(2025);
		dto.setDailyPrice(100);
		dto.setAvailable(true);
		return dto;
	}

	@Test
	void testListCars() throws Exception {
		// Given
		when(carService.getAllCars()).thenReturn(List.of(sampleCar()));

		// When
		mockMvc.perform(get("/")).andExpect(status().isOk()).andExpect(view().name("car-list"))
				.andExpect(model().attributeExists("cars"));
		// Verify
		verify(carService, times(1)).getAllCars();
		verifyNoMoreInteractions(carService);
	}

	@Test
	void testShowCreateForm() throws Exception {
		// When
		mockMvc.perform(get("/new")).andExpect(status().isOk()).andExpect(view().name("car-form"))
				.andExpect(model().attributeExists("car"));
	}

	@Test
	void testCreateCar() throws Exception {
		// Given
		CarDTO carDTO = sampleCar();

		// When
		mockMvc.perform(post("/").param("make", carDTO.getMake()).param("model", carDTO.getModel())
				.param("plate", carDTO.getPlate()).param("year", String.valueOf(carDTO.getYear()))
				.param("dailyPrice", String.valueOf(carDTO.getDailyPrice()))
				.param("available", String.valueOf(carDTO.isAvailable())))
				// Then
				.andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("/"));

		// Verify
		verify(carService, times(1)).createCar(Mockito.any(CarDTO.class));
		verifyNoMoreInteractions(carService);
	}

	@Test
	void testShowEditForm() throws Exception {
		// Given
		CarDTO carDTO = sampleCar();
		when(carService.getCarById(1L)).thenReturn(carDTO);

		// When
		mockMvc.perform(get("/1/edit")).andExpect(status().isOk()).andExpect(view().name("car-form"))
				.andExpect(model().attributeExists("car")).andExpect(model().attributeExists("formAction"))
				.andExpect(model().attribute("formAction", "/1"));

		// Then
		verify(carService, times(1)).getCarById(1L);
		verifyNoMoreInteractions(carService);
	}

	@Test
	void testUpdateCar() throws Exception {
		// Given
		Long carId = 1L;

		// When
		mockMvc.perform(post("/{id}", carId).param("make", "Updated").param("model", "Updated").param("plate", "NEW123")
				.param("year", "2026").param("dailyPrice", "80").param("available", "true"))
				// Then
				.andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("/"));

		// Verify
		verify(carService, times(1)).updateCar(Mockito.eq(carId), Mockito.any());
		verifyNoMoreInteractions(carService);
	}

	@Test
	void testDeleteCar() throws Exception {
		// Given
		Long carId = 1L;

		// When
		mockMvc.perform(post("/{id}/delete", carId))
				// Then
				.andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("/"));

		// Verify
		verify(carService, times(1)).deleteCar(carId);
		verifyNoMoreInteractions(carService);
	}
}
