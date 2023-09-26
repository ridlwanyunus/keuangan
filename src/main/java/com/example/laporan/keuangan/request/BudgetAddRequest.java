package com.example.laporan.keuangan.request;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BudgetAddRequest {

	private String nama;
	private Integer idAkun;
	private Double biaya;
	private Double capaian;
	private Integer status;
}
