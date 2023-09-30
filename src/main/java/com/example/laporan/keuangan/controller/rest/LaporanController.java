package com.example.laporan.keuangan.controller.rest;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.laporan.keuangan.response.ResponseTemplate;
import com.example.laporan.keuangan.response.TransaksiWrapper;
import com.example.laporan.keuangan.service.TransaksiService;

@RestController
@RequestMapping("laporan")
public class LaporanController {
	
	@Autowired
	private TransaksiService transaksiService;

	@GetMapping("/kas/{bulan}/{tahun}")
	public ResponseTemplate kas(@PathVariable("bulan") Integer bulan, @PathVariable("tahun") Integer tahun) {
		ResponseTemplate response = new ResponseTemplate();
		
		Calendar calendar = Calendar.getInstance();
		
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		calendar.set(Calendar.MONTH, bulan-1);
		calendar.set(Calendar.YEAR, tahun);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

		String start = sdf.format(calendar.getTime());
		calendar.add(Calendar.MONTH, 1);
		String end = sdf.format(calendar.getTime());
		
		List<TransaksiWrapper> transaksis = transaksiService.findByStartAndEndDate(start, end);
		
		response.setStatus(1);
		response.setMessage("Success");
		response.setData(transaksis);
		
		return response;
	}
	
	
	
}
