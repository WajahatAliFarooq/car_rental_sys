package com.example.car_rental_sys.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.example.car_rental_sys.dto.CarDTO;
import com.example.car_rental_sys.service.CarService;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(CarController.class)
class CarControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockitoBean
	private CarService carService;

	@Autowired
	private ObjectMapper mapper;

	@Test
	void testCreateCar() throws Exception {
		// Given
		CarDTO carDTO = new CarDTO(null, "Toyota", "Corolla", "WA786", 2025, 100, true);
		CarDTO savedDTO = new CarDTO(1L, "Toyota", "Corolla", "WA786", 2025, 100, true);

		// When
		when(carService.createCar(any(CarDTO.class))).thenReturn(savedDTO);

		// Then
		mockMvc.perform(MockMvcRequestBuilders.post("/api/cars/create").contentType(MediaType.APPLICATION_JSON)
				.content(mapper.writeValueAsString(carDTO))).andExpect(status().isOk())
				.andExpect(jsonPath("$.id").value(1L)).andExpect(jsonPath("$.make").value("Toyota"));
		// Verify
		verify(carService, times(1)).createCar(any(CarDTO.class));
	}

	@Test
	void testGetAllCars() throws Exception {
		// Given
		CarDTO car1 = new CarDTO(1L, "Toyota", "Corolla", "WA786", 2025, 100, true);
		CarDTO car2 = new CarDTO(2L, "Peugeot", "308", "GK786", 2025, 100, true);

		// When
		when(carService.getAllCars()).thenReturn(Arrays.asList(car1, car2));

		// Then
		mockMvc.perform(MockMvcRequestBuilders.get("/api/cars/getall")).andExpect(status().isOk())
				.andExpect(jsonPath("$.length()").value(2)).andExpect(jsonPath("$[0].make").value("Toyota"))
				.andExpect(jsonPath("$[1].make").value("Peugeot"));
		// verify
		verify(carService, times(1)).getAllCars();
	}

	@Test
	void testUpdateCar() throws Exception {
		// Given
		CarDTO carDTO = new CarDTO(null, "Toyota", "Corolla", "WA786", 2025, 100, true);
		CarDTO updated = new CarDTO(1L, "Toyota", "Corolla", "WA786", 2025, 100, true);

		// When
		when(carService.updateCar(eq(1L), any(CarDTO.class))).thenReturn(updated);

		// Then
		mockMvc.perform(MockMvcRequestBuilders.put("/api/cars/update/1").contentType(MediaType.APPLICATION_JSON)
				.content(mapper.writeValueAsString(carDTO))).andExpect(status().isOk())
				.andExpect(jsonPath("$.make").value("Toyota"));

		// Verify
		verify(carService, times(1)).updateCar(anyLong(), any(CarDTO.class));
	}

	@Test
	void testDeleteCar() throws Exception {
		// When
		doNothing().when(carService).deleteCar(1L);

		// Then
		mockMvc.perform(MockMvcRequestBuilders.delete("/api/cars/delete/1")).andExpect(status().isOk());

		// Verify
		verify(carService, times(1)).deleteCar(1L);
	}

}
