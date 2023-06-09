package com.onerivet.deskbook.util;

import java.time.LocalDateTime;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.onerivet.deskbook.exception.ResourceNotFoundException;
import com.onerivet.deskbook.models.entity.ColumnDetails;
import com.onerivet.deskbook.models.entity.Employee;
import com.onerivet.deskbook.models.entity.SeatConfiguration;
import com.onerivet.deskbook.models.entity.SeatNumber;
import com.onerivet.deskbook.models.payload.UpdateProfileDto;
import com.onerivet.deskbook.repository.DesignationRepo;
import com.onerivet.deskbook.repository.ModeOfWorkRepo;
import com.onerivet.deskbook.repository.SeatConfigurationRepo;
import com.onerivet.deskbook.repository.SeatNumberRepo;

@Component
public class UpdateSeatUtils {

	@Autowired
	private DesignationRepo designationRepo;

	@Autowired
	private ModeOfWorkRepo modeOfWorkRepo;

	@Autowired
	private SeatNumberRepo seatNumberRepo;

	@Autowired
	private SeatConfigurationRepo seatConfigurationRepo;

	@Autowired
	private ImageUtils imageUtils;

	@Value("${image.upload.path}")
	String path;



	public Employee getUpdatedEmployee(Employee employee, UpdateProfileDto newEmployeeDto) throws Exception {

		if (newEmployeeDto.getProfilePictureFileString() != null) {
			String fileExtension = imageUtils.base64ToFile(newEmployeeDto.getProfilePictureFileString(),
					newEmployeeDto.getFirstName(), employee.getId());
			employee.setProfilePictureFileName(employee.getId() + fileExtension);
			employee.setProfilePictureFilePath(
					path +  employee.getId() + fileExtension);

		} else {
			employee.setProfilePictureFileName(null);
			employee.setProfilePictureFilePath(null);
		}

		employee.setFirstName(newEmployeeDto.getFirstName());
		employee.setLastName(newEmployeeDto.getLastName());
		employee.setPhoneNumber(newEmployeeDto.getPhoneNumber());

		employee.setDesignation(this.designationRepo.findById(newEmployeeDto.getDesignation())
				.orElseThrow(() -> new ResourceNotFoundException(
						"Designation With id " + newEmployeeDto.getDesignation() + " not found.")));
		employee.setModeOfWork(this.modeOfWorkRepo.findById(newEmployeeDto.getModeOfWork())
				.orElseThrow(() -> new ResourceNotFoundException(
						"Mode of work With id " + newEmployeeDto.getModeOfWork() + " not found.")));

		employee.setModifiedBy(employee);
		employee.setModifiedDate(LocalDateTime.now());

		return employee;
	}

	public SeatConfiguration saveSeat(Employee employee, UpdateProfileDto newEmployeeDto) {

		SeatNumber seatNumber = this.seatNumberRepo.findById(newEmployeeDto.getSeat()).orElseThrow(
				() -> new ResourceNotFoundException("Seat With id " + newEmployeeDto.getSeat() + " not found."));

		seatNumber.setBooked(true);

		Map<String, ColumnDetails> map = seatNumberRepo.findColumnFloorCityBySeat(seatNumber.getId());

		ColumnDetails columnDetails = map.get("Column");
		if (columnDetails != null) {
			if (columnDetails.getFloor().getCity().getId() != newEmployeeDto.getCity())
				throw new IllegalArgumentException("Enter valid seat information");

			if (columnDetails.getFloor().getId() != newEmployeeDto.getFloor())
				throw new IllegalArgumentException("Enter valid seat information");

			if (columnDetails.getId() != newEmployeeDto.getColumn())
				throw new IllegalArgumentException("Enter Valid seat information");
		}

		SeatConfiguration employeeSeat = this.seatConfigurationRepo.findByEmployeeAndDeletedByNull(employee);
		if (employeeSeat == null) {
			employeeSeat = new SeatConfiguration();
		}

		SeatConfiguration bookedSeat = this.seatConfigurationRepo.findBySeatNumberAndDeletedByNull(seatNumber);

		if (bookedSeat != null && bookedSeat.getEmployee().getId() != employee.getId())
			throw new IllegalArgumentException("Already Booked");

		employeeSeat.setCreatedBy(employee);
		employeeSeat.setEmployee(employee);
		employeeSeat.setModifiedBy(employee);
		employeeSeat.setModifiedDate(LocalDateTime.now());
		employeeSeat.setSeatNumber(seatNumber);

		return employeeSeat;
	}

}
