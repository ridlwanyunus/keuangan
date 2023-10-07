package com.example.laporan.keuangan.controller.rest;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.laporan.keuangan.entity.Transaksi;
import com.example.laporan.keuangan.response.ResponseTemplate;
import com.example.laporan.keuangan.response.StatisticWrapper;
import com.example.laporan.keuangan.response.TransaksiWrapper;
import com.example.laporan.keuangan.service.TransaksiService;
import com.example.laporan.keuangan.utils.TransaksiUtils;

@RestController
@RequestMapping("index")
public class IndexController {

	@Autowired
	TransaksiService transaksiService;
	
	@Autowired
	TransaksiUtils transaksiUtils;
	
	@GetMapping("load")
	public ResponseTemplate load() {
		ResponseTemplate response = new ResponseTemplate();
		
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

		String start = sdf.format(calendar.getTime());
		calendar.add(Calendar.MONTH, 1);
		calendar.add(Calendar.DAY_OF_MONTH, -1);
		String end = sdf.format(calendar.getTime());

		List<Transaksi> transaksis = transaksiService.findAll();
		BigDecimal cashIn = new BigDecimal(0);
		BigDecimal cashOut = new BigDecimal(0);
		BigDecimal hutang = new BigDecimal(0);
		BigDecimal piutang = new BigDecimal(0);
		
		for(Transaksi transaksi: transaksis) {
			if(transaksi.getIdAkun() == 1) {
				// Pengeluaran
				cashOut = cashOut.add(new BigDecimal(transaksi.getCashOut().toString())).subtract(new BigDecimal(transaksi.getCashIn().toString()));
			} else 
			if(transaksi.getIdAkun() == 2) {
				// Pemasukan
				cashIn = cashIn.add(new BigDecimal(transaksi.getCashIn().toString())).subtract(new BigDecimal(transaksi.getCashOut().toString()));
			} else 
			if(transaksi.getIdAkun() == 3) {
				// Hutang
				hutang = hutang.add(new BigDecimal(transaksi.getCashOut().toString())).subtract(new BigDecimal(transaksi.getCashIn().toString()));
			} else
			if(transaksi.getIdAkun() == 4) {
				// Piutang
				piutang = piutang.add(new BigDecimal(transaksi.getCashIn().toString())).subtract(new BigDecimal(transaksi.getCashOut().toString()));
			}
		}
		

		StatisticWrapper statistic = transaksiUtils.mappingCashInCashout(start, end);
		

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("cashIn", cashIn);
		map.put("cashOut", cashOut);
		map.put("hutang", hutang);
		map.put("piutang", piutang);
		map.put("cashInMonth", statistic.getCashIn());
		map.put("cashOutMonth", statistic.getCashOut());
		map.put("hutangMonth", statistic.getHutang());
		map.put("piutangMonth", statistic.getPiutang());
		
		response.setStatus(1);
		response.setMessage("Success load index");
		response.setData(map);
		
		return response;
	}
	
	@GetMapping("statistics")
	public ResponseTemplate statistics() {
		ResponseTemplate response = new ResponseTemplate();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		
		Calendar calendar = Calendar.getInstance();
		
		Map<Integer, BigDecimal> pemasukan = new HashMap<Integer, BigDecimal>();
		Map<Integer, BigDecimal> pengeluaran = new HashMap<Integer, BigDecimal>();
		
		pemasukan.put(0, new BigDecimal(0));
		pengeluaran.put(0, new BigDecimal(0));
		
		for(int i=1; i<=12; i++) {
			calendar.set(Calendar.MONTH, i-1);
			calendar.set(Calendar.DAY_OF_MONTH, 1);

			String start = sdf.format(calendar.getTime());
			System.out.println("start: "+start);
			
			calendar.add(Calendar.MONTH, 1);
			calendar.add(Calendar.DAY_OF_MONTH, -1);

			String end = sdf.format(calendar.getTime());
			System.out.println("end: "+end);
			
			StatisticWrapper statistic = transaksiUtils.mappingCashInCashout(start, end);
			BigDecimal cashIn = statistic.getCashIn();
			BigDecimal cashOut = statistic.getCashOut();
			BigDecimal piutang = statistic.getPiutang();
			BigDecimal hutang = statistic.getHutang();
			
			pemasukan.put(i, cashIn);
			pengeluaran.put(i, cashOut);
		}

		Map<String, Map<Integer, BigDecimal>> data = new HashMap<String, Map<Integer, BigDecimal>>();
		data.put("pemasukan", pemasukan);
		data.put("pengeluaran", pengeluaran);
		
		response.setStatus(1);
		response.setMessage("Success load index");
		response.setData(data);

		return response;
	}
	
}
