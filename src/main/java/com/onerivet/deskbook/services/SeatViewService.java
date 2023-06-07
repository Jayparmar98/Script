package com.onerivet.deskbook.services;

import java.util.List;

import com.onerivet.deskbook.models.payload.DateRequestDto;
import com.onerivet.deskbook.models.payload.SeatViewDto;

public interface SeatViewService {
	
	public List<SeatViewDto> getSeatView(DateRequestDto dateRequestDto, Integer cityId, Integer floorId);

}
