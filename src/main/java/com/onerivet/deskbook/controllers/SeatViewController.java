package com.onerivet.deskbook.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.onerivet.deskbook.models.payload.DateRequestDto;
import com.onerivet.deskbook.models.payload.SeatViewDto;
import com.onerivet.deskbook.models.response.GenericResponse;
import com.onerivet.deskbook.services.SeatViewService;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@RestController
@Validated
@SecurityRequirement(name = "bearerAuth")
@RequestMapping("/api/deskbook/seat-view")
public class SeatViewController {
	
	@Autowired
	private SeatViewService seatViewService;
	
	@GetMapping("/cities/{cityId}/floors/{floorId}")
	public GenericResponse<List<SeatViewDto>> getView(@RequestBody DateRequestDto dateRequestDto, @PathVariable("cityId") int cityId, @PathVariable("floorId") int floorId) {
		GenericResponse<List<SeatViewDto>> response = new GenericResponse<>(this.seatViewService.getSeatView(dateRequestDto, cityId, floorId), null);
		
		return response;
	}

}
