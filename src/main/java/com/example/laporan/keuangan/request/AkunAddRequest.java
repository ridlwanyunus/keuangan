package com.example.laporan.keuangan.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AkunAddRequest {
	
	private String nama;
	private Integer tipeAkun;
	private Integer status;
}
