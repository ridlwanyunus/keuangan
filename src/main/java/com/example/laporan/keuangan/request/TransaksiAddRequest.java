package com.example.laporan.keuangan.request;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransaksiAddRequest {
	private String nama;
	private Double cashIn;
	private Double cashOut;
	private Integer idAkun;
	private Integer idBudget;
	private Integer status;
	@JsonFormat(pattern = "dd-MM-yyyy")
	private Date creationDate;
}
