package com.example.laporan.keuangan.utils;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.example.laporan.keuangan.response.StatisticWrapper;
import com.example.laporan.keuangan.response.TransaksiWrapper;
import com.example.laporan.keuangan.service.TransaksiService;

@Component
public class TransaksiUtils {

	@Autowired
	private TransaksiService transaksiService;
	
	public StatisticWrapper mappingCashInCashout(String start, String end){
		
		StatisticWrapper statistic = new StatisticWrapper();
		
		
		List<TransaksiWrapper> transaksiWrappers = transaksiService.findByStartAndEndDate(start, end);
		BigDecimal cashInMonth = new BigDecimal(0);
		BigDecimal cashOutMonth = new BigDecimal(0);
		BigDecimal hutangMonth = new BigDecimal(0);
		BigDecimal piutangMonth = new BigDecimal(0);
		
		for(TransaksiWrapper transaksiWrapper: transaksiWrappers) {
			if(transaksiWrapper.getIdAkun() == 1) {
				// Pengeluaran
				cashOutMonth = cashOutMonth.add(new BigDecimal(transaksiWrapper.getCashOut().toString())).subtract(new BigDecimal(transaksiWrapper.getCashIn().toString()));
			} else 
			if(transaksiWrapper.getIdAkun() == 2) {
				// Pemasukan
				cashInMonth = cashInMonth.add(new BigDecimal(transaksiWrapper.getCashIn().toString())).subtract(new BigDecimal(transaksiWrapper.getCashOut().toString()));
			} else 
			if(transaksiWrapper.getIdAkun() == 3) {
				// Hutang
				hutangMonth = hutangMonth.add(new BigDecimal(transaksiWrapper.getCashOut().toString())).subtract(new BigDecimal(transaksiWrapper.getCashIn().toString()));
			} else
			if(transaksiWrapper.getIdAkun() == 4) {
				// Piutang
				piutangMonth = piutangMonth.add(new BigDecimal(transaksiWrapper.getCashIn().toString())).subtract(new BigDecimal(transaksiWrapper.getCashOut().toString()));
			}
		}
		
		statistic.setCashIn(cashInMonth);
		statistic.setCashOut(cashOutMonth);
		statistic.setPiutang(piutangMonth);
		statistic.setHutang(hutangMonth);
		
		return statistic;
	}
	
}
