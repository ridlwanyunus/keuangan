package com.example.laporan.keuangan.utils;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.example.laporan.keuangan.entity.Budget;
import com.example.laporan.keuangan.entity.Saldo;
import com.example.laporan.keuangan.entity.Transaksi;
import com.example.laporan.keuangan.response.SaldoWrapper;
import com.example.laporan.keuangan.response.StatisticWrapper;
import com.example.laporan.keuangan.response.TransaksiWrapper;
import com.example.laporan.keuangan.service.BudgetService;
import com.example.laporan.keuangan.service.SaldoService;
import com.example.laporan.keuangan.service.TransaksiService;

@Component
public class TransaksiUtils {

	@Autowired
	private TransaksiService transaksiService;
	
	@Autowired
	private BudgetService budgetService;
	
	@Autowired
	private SaldoService saldoService;
	
	@Autowired
	private ExecutorService executor;
	
	
	public void saldoRecalculate() {

		Thread saldoThread = new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				saldoRecalculateAdvanced();
			}
		});
		saldoThread.start();
		
	}
	
	public void saldoRecalculateAdvanced() {
		
		int tahunAwal = 2023;
		
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		int tahunAkhir = calendar.get(Calendar.YEAR);
		
		List<Future<?>> futures = new ArrayList<Future<?>>();
		
		for(int i=tahunAwal; i<=tahunAkhir; i++) {
			for(int bulan=0; bulan < 12; bulan++) {
				Future<?> f = executor.submit(new SaldoTask(i, bulan, saldoService, transaksiService));
				futures.add(f);
			}
		}
		
		try {
			System.out.println("###### All tasks are submitted.");
			for(Future<?> f: futures) {
				f.get();
			}
			System.out.println("###### All tasks are completed.");
			List<Saldo> saldos = new ArrayList<Saldo>();
			saldos = saldoService.findAll();
			
			List<Saldo> recalculateSaldos = new ArrayList<Saldo>();
			Double currentSaldo = Double.valueOf(0);
			
			for(Saldo saldo: saldos) {
				currentSaldo = currentSaldo + saldo.getSelisih();
				saldo.setTotal(currentSaldo);
				recalculateSaldos.add(saldo);
			}
			saldoService.saveAll(recalculateSaldos);
			System.out.println("###### Recalculate saldo is completed.");
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		

		
	}
	
	
	public void budgetRecalculate(Transaksi transaksi) {
		Budget budget = budgetService.findByIdBudget(transaksi.getIdBudget());
		
		List<TransaksiWrapper> transaksisWrappers = transaksiService.findByIdBudget(budget.getIdBudget());
		BigDecimal cashIn = new BigDecimal(0);
		BigDecimal cashOut = new BigDecimal(0);
		BigDecimal hutang = new BigDecimal(0);
		BigDecimal piutang = new BigDecimal(0);
		
		BigDecimal biaya = new BigDecimal(budget.getBiaya());
		BigDecimal capaian = new BigDecimal(0);
		BigDecimal selisih = new BigDecimal(0);
		
		for(TransaksiWrapper transaksisWrapper: transaksisWrappers) {
			switch(transaksisWrapper.getIdAkun()) {
				case 1:
					cashOut = cashOut.add(new BigDecimal(transaksisWrapper.getCashOut().toString()));
					break;
				case 2:
					cashIn = cashIn.add(new BigDecimal(transaksisWrapper.getCashIn().toString()));
					break;
				case 3:
					hutang = hutang.add(new BigDecimal(transaksisWrapper.getCashOut().toString()));
					break;
				case 4:
					piutang = piutang.add(new BigDecimal(transaksisWrapper.getCashIn().toString()));
					break;
					
			}
		}
		
		switch(budget.getIdAkun()) {
			case 1:
				capaian = cashOut.subtract(cashIn);
				break;
			case 2:
				capaian = cashIn.subtract(cashOut);
				break;
			case 3:
				capaian = hutang.subtract(piutang);
				break;
			case 4:
				capaian = piutang.subtract(hutang);
				break;
		}
		
		selisih = biaya.subtract(capaian);
		budget.setCapaian(capaian.doubleValue());
		budget.setSelisih(selisih.doubleValue());
		
		budgetService.save(budget);
		
	}
	
	public SaldoWrapper mappingCashInCashout(String start, String end){
		
		SaldoWrapper saldoWrapper = new SaldoWrapper();
		
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
		
		saldoWrapper.setCashIn(cashInMonth);
		saldoWrapper.setCashOut(cashOutMonth);
		saldoWrapper.setPiutang(piutangMonth);
		saldoWrapper.setHutang(hutangMonth);
		
		return saldoWrapper;
	}

}
