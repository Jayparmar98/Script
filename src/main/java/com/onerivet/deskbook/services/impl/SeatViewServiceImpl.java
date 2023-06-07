package com.onerivet.deskbook.services.impl;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.onerivet.deskbook.models.entity.City;
import com.onerivet.deskbook.models.entity.Floor;
import com.onerivet.deskbook.models.entity.SeatView;
import com.onerivet.deskbook.models.entity.WorkingDay;
import com.onerivet.deskbook.models.payload.DateRequestDto;
import com.onerivet.deskbook.models.payload.SeatViewDto;
import com.onerivet.deskbook.repository.SeatNumberRepo;
import com.onerivet.deskbook.services.SeatViewService;

import jakarta.transaction.Transactional;

@Transactional
@Service
public class SeatViewServiceImpl implements SeatViewService {

	@Autowired
	private SeatNumberRepo seatNumberRepo;

	@Autowired
	private ModelMapper modelMapper;

	@Override
	public List<SeatViewDto> getSeatView(DateRequestDto dateRequestDto, Integer cityId, Integer floorId) {

		LocalDate date = LocalDate.parse(dateRequestDto.getDate(), DateTimeFormatter.ofPattern("MM/dd/yyyy"));
		
		DayOfWeek day = date.getDayOfWeek();
		int dayId = day.getValue();

		List<SeatView> view = this.seatNumberRepo.getViewByDateAndCityAndFloorAndWorkingDay(date, new City(cityId), new Floor(floorId),
				new WorkingDay(dayId));
		System.out.println(view);
		view.stream()
				.filter(seatView -> seatView.getStatus().equals("Booked") || seatView.getStatus().equals("Reserved"))
				.forEach(seatView -> seatView.getSeat().setBooked(true));
		return view.stream().map((seatView) -> this.modelMapper.map(seatView, SeatViewDto.class))
				.collect(Collectors.toList());
	}

}
