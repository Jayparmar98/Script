package com.onerivet.deskbook.models.payload;


import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class DateRequestDto {
	
	@JsonFormat(pattern = "MM/dd/yyyy")
	private String date;
	
}
