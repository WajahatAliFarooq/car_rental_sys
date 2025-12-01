package com.example.car_rental_sys.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
public class CarControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockitoBean
	private CarService carService;

	private ObjectMapper mapper = new ObjectMapper();

	@Test
	void testCreateCar() throws Exception {
		CarDTO carDTO = new CarDTO(null, "Toyota", "Corolla", "WA786", 2025, 100, true);
		CarDTO savedDTO = new CarDTO(1L, "Toyota", "Corolla", "WA786", 2025, 100, true);

		when(carService.createCar(any(CarDTO.class))).thenReturn(savedDTO);

		mockMvc.perform(MockMvcRequestBuilders.post("/api/cars/create").contentType(MediaType.APPLICATION_JSON)
				.content(mapper.writeValueAsString(carDTO))).andExpect(status().isOk())
				.andExpect(jsonPath("$.id").value(1L)).andExpect(jsonPath("$.make").value("Toyota"));
	}

}
