package com.example.laporan.keuangan.response;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransaksiWrapper {
	private Integer idTransaksi;
	private String nama;
	private BigDecimal cashIn;
	private BigDecimal cashOut;
	private Integer idAkun;
	private Integer idBudget;
	private Integer status;
	private String statusInfo;
	@JsonFormat(pattern = "dd-MM-yyyy", timezone = "Asia/Jakarta")
	private Date creationDate;
	private String tipeNama;
	private String budgetNama;
	
}
