package com.example.laporan.keuangan.response;

import java.math.BigDecimal;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StatisticWrapper {
	
	private BigDecimal cashIn;
	private BigDecimal cashOut;
	private BigDecimal piutang;
	private BigDecimal hutang;

}
