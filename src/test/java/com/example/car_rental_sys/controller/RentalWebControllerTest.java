package com.example.car_rental_sys.controller;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.example.car_rental_sys.dto.RentalDTO;
import com.example.car_rental_sys.service.RentalService;

@WebMvcTest(RentalWebController.class)
class RentalWebControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockitoBean
	private RentalService rentalService;

	private final LocalDate start = LocalDate.now();
	private final LocalDate end = start.plusDays(2);

	private RentalDTO sampleRental() {

		RentalDTO dto = new RentalDTO();
		dto.setId(1L);
		dto.setRenterName("Ali");
		dto.setCarId(10L);
		dto.setStartDate(start);
		dto.setEndDate(end);
		dto.setTotalPrice(200);
		return dto;
	}

	@Test
	void testListRentals() throws Exception {
		// Given
		Mockito.when(rentalService.getAllRentals()).thenReturn(List.of(sampleRental()));

		// When
		mockMvc.perform(get("/rentals")).andExpect(status().isOk()).andExpect(view().name("rental-list"))
				.andExpect(model().attributeExists("rentals"));
		// Verify
		Mockito.verify(rentalService, times(1)).getAllRentals();
		verifyNoMoreInteractions(rentalService);
	}

	@Test
	void testShowCreateForm() throws Exception {
		// When
		mockMvc.perform(get("/rentals/new")).andExpect(status().isOk()).andExpect(view().name("rental-form"))
				.andExpect(model().attributeExists("rental"));
	}

	@Test
	void testCreateRental() throws Exception {
		// Given
		RentalDTO rentalDTO = sampleRental();

		// When
		mockMvc.perform(post("/rentals").param("renterName", rentalDTO.getRenterName())
				.param("carId", String.valueOf(rentalDTO.getCarId())).param("startDate", start.toString())
				.param("endDate", end.toString()).param("totalPrice", String.valueOf(rentalDTO.getTotalPrice())))
				// Then
				.andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("/rentals"));

		// Verify
		Mockito.verify(rentalService, times(1)).createRental(Mockito.any(RentalDTO.class));
		verifyNoMoreInteractions(rentalService);
	}

	@Test
	void testShowEditForm() throws Exception {
		// Given
		Mockito.when(rentalService.getAllRentals()).thenReturn(List.of(sampleRental()));

		// When
		mockMvc.perform(get("/rentals/1/edit")).andExpect(status().isOk()).andExpect(view().name("rental-form"))
				.andExpect(model().attributeExists("rental"));
	}

	@Test
	void testUpdateRental() throws Exception {
		// Given
		Long rentalId = 1L;

		// When
		mockMvc.perform(post("/rentals/{id}", rentalId).param("renterName", "Updated").param("carId", "2")
				.param("startDate", start.toString()).param("endDate", end.toString()).param("totalPrice", "500"))
				// Then
				.andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("/rentals"));

		// Verify
		Mockito.verify(rentalService, times(1)).updateRental(Mockito.eq(rentalId), Mockito.any());
		verifyNoMoreInteractions(rentalService);
	}

	@Test
	void testDeleteRental() throws Exception {
		// Given
		Long rentalId = 1L;

		// When
		mockMvc.perform(post("/rentals/{id}/delete", rentalId))
				// Then
				.andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("/rentals"));

		// Verify
		Mockito.verify(rentalService, times(1)).deleteRental(rentalId);
		verifyNoMoreInteractions(rentalService);
	}
}
