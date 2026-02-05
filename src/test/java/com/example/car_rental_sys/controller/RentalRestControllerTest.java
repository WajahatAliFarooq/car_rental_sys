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

import java.time.LocalDate;
import java.util.Arrays;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.example.car_rental_sys.dto.RentalDTO;
import com.example.car_rental_sys.service.RentalService;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(RentalRestController.class)
class RentalRestControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockitoBean
	private RentalService rentalService;

	@Autowired
	private ObjectMapper mapper;

	private final LocalDate start = LocalDate.now();
	private final LocalDate end = start.plusDays(2);

	private RentalDTO sampleRental() {
		return new RentalDTO(1L, "Wajahat", start, end, 200, 10L);
	}

	private RentalDTO sampleRental2() {
		return new RentalDTO(2L, "Ali", start, end, 300, 10L);
	}

	private RentalDTO sampleRentalWithoutId() {
		return new RentalDTO(null, "Wajahat", start, end, 200, 10L);
	}

	@Test
	void testCreateRental() throws Exception {
		// Given
		RentalDTO rentalDto = sampleRentalWithoutId();
		RentalDTO savedDTO = sampleRental();

		// When
		when(rentalService.createRental(any(RentalDTO.class))).thenReturn(savedDTO);

		// Then
		mockMvc.perform(MockMvcRequestBuilders.post("/api/rentals/create").contentType(MediaType.APPLICATION_JSON)
				.content(mapper.writeValueAsString(rentalDto))).andExpect(status().isOk())
				.andExpect(jsonPath("$.id").value(1L)).andExpect(jsonPath("$.renterName").value("Wajahat"));
		// Verify
		verify(rentalService, times(1)).createRental(any(RentalDTO.class));
	}

	@Test
	void testGetAllRentals() throws Exception {
		// Given
		RentalDTO r1 = sampleRental();
		RentalDTO r2 = sampleRental2();

		// When
		when(rentalService.getAllRentals()).thenReturn(Arrays.asList(r1, r2));

		// Then
		mockMvc.perform(MockMvcRequestBuilders.get("/api/rentals/getall")).andExpect(status().isOk())
				.andExpect(jsonPath("$.length()").value(2)).andExpect(jsonPath("$[0].renterName").value("Wajahat"))
				.andExpect(jsonPath("$[1].renterName").value("Ali"));
		// verify
		verify(rentalService, times(1)).getAllRentals();
	}

	@Test
	void testUpdateRental() throws Exception {
		// Given
		RentalDTO rentalDto = sampleRentalWithoutId();
		RentalDTO updated = sampleRental();

		// When
		when(rentalService.updateRental(eq(1L), any(RentalDTO.class))).thenReturn(updated);

		// Then
		mockMvc.perform(MockMvcRequestBuilders.put("/api/rentals/update/1").contentType(MediaType.APPLICATION_JSON)
				.content(mapper.writeValueAsString(rentalDto))).andExpect(status().isOk())
				.andExpect(jsonPath("$.renterName").value("Wajahat"));
		// Verify
		verify(rentalService, times(1)).updateRental(anyLong(), any(RentalDTO.class));
	}

	@Test
	void testDeleteRental() throws Exception {
		// When
		doNothing().when(rentalService).deleteRental(1L);

		// Then
		mockMvc.perform(MockMvcRequestBuilders.delete("/api/rentals/delete/1")).andExpect(status().isOk());

		// Verify
		verify(rentalService, times(1)).deleteRental(1L);
	}
}