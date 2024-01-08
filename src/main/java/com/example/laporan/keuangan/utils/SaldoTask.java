package com.example.laporan.keuangan.utils;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import com.example.laporan.keuangan.entity.Saldo;
import com.example.laporan.keuangan.response.SaldoWrapper;
import com.example.laporan.keuangan.response.TransaksiWrapper;
import com.example.laporan.keuangan.service.SaldoService;
import com.example.laporan.keuangan.service.TransaksiService;

public class SaldoTask implements Runnable {

	private Integer tahun;
	private Integer bulan;
	private SaldoService saldoService;
	private TransaksiService transaksiService;

	public SaldoTask(Integer tahun, Integer bulan, SaldoService saldoService, TransaksiService transaksiService) {
		super();
		this.tahun = tahun;
		this.bulan = bulan;
		this.saldoService = saldoService;
		this.transaksiService = transaksiService;
	}

	@Override
	public void run() {
		try {
			Calendar calendar = Calendar.getInstance();
			
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			
			Double currentSaldo = Double.valueOf(0);
			
			calendar.set(tahun, bulan, 1);
			String start = sdf.format(calendar.getTime());
			
			calendar.add(calendar.MONTH, 1);
			calendar.add(calendar.DAY_OF_MONTH, -1);
			String end = sdf.format(calendar.getTime());
			
			// Get transaction of this month
			SaldoWrapper saldoWrapper = this.mappingCashInCashout(start, end);
			
			// Sum of saldo from month 1 to 12 in the same year
			currentSaldo = currentSaldo + (saldoWrapper.getCashIn().doubleValue() + saldoWrapper.getPiutang().doubleValue()) - (saldoWrapper.getCashOut().doubleValue() + saldoWrapper.getHutang().doubleValue());
			
			System.out.println(String.format("%s tanggal %s sampai %s", Thread.currentThread().getName(),start, end));
			
			Saldo saldo = this.saldoService.findByTahunAndBulan(tahun, bulan+1);
			if(saldo == null) {
				saldo = new Saldo();
				saldo.setTahun(tahun);
				saldo.setBulan(bulan + 1);
			} 
			
			Double keluar = saldoWrapper.getCashOut().doubleValue();
			Double masuk = saldoWrapper.getCashIn().doubleValue();
			Double hutang = saldoWrapper.getHutang().doubleValue();
			Double piutang = saldoWrapper.getPiutang().doubleValue();
			Double selisih = (masuk + piutang) - (keluar + hutang);
			Double total = currentSaldo;

			saldo.setMasuk(masuk);
			saldo.setKeluar(keluar);
			saldo.setHutang(hutang);
			saldo.setPiutang(piutang);
			saldo.setSelisih(selisih);
			saldo.setTotal(total);
			
			if(selisih > 0) {
				saldo.setStatus(1);
				saldo.setStatusInfo("surplus");
			} else 
			if (selisih == 0) {
				saldo.setStatus(0);
				saldo.setStatusInfo("tetap");
			} else {
				saldo.setStatus(-1);
				saldo.setStatusInfo("minus");
			}
			
			saldoService.save(saldo);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
	}
	
	public SaldoWrapper mappingCashInCashout(String start, String end){
		
		SaldoWrapper saldoWrapper = new SaldoWrapper();
		
		List<TransaksiWrapper> transaksiWrappers = this.transaksiService.findByStartAndEndDate(start, end);
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
		
		saldoWrapper.setCashIn(cashInMonth);
		saldoWrapper.setCashOut(cashOutMonth);
		saldoWrapper.setPiutang(piutangMonth);
		saldoWrapper.setHutang(hutangMonth);
		
		return saldoWrapper;
	}

}
