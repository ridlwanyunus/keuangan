package com.example.laporan.keuangan.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResponseTemplate {

	private int status;
	private String message;
	private Object data;
	
}
