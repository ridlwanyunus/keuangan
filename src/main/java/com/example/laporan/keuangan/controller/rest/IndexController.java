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
import com.example.laporan.keuangan.response.TransaksiWrapper;
import com.example.laporan.keuangan.service.TransaksiService;

@RestController
@RequestMapping("index")
public class IndexController {

	@Autowired
	TransaksiService transaksiService;
	
	
	@GetMapping("load")
	public ResponseTemplate load() {
		ResponseTemplate response = new ResponseTemplate();
		
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

		String start = sdf.format(calendar.getTime());
		calendar.add(Calendar.MONTH, 1);
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

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("cashIn", cashIn);
		map.put("cashOut", cashOut);
		map.put("hutang", hutang);
		map.put("piutang", piutang);
		map.put("cashInMonth", cashInMonth);
		map.put("cashOutMonth", cashOutMonth);
		map.put("hutangMonth", hutangMonth);
		map.put("piutangMonth", piutangMonth);
		
		response.setStatus(1);
		response.setMessage("Success load index");
		response.setData(map);
		
				
		
		
		
		return response;
	}
	
	
}
