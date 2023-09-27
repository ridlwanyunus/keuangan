package com.example.laporan.keuangan;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.example.laporan.keuangan.entity.Akun;
import com.example.laporan.keuangan.service.AkunService;

@SpringBootApplication
public class KeuanganApplication implements CommandLineRunner {

	@Autowired
	private AkunService akunService;
	
	Logger logger = LoggerFactory.getLogger(getClass());
	
	public static void main(String[] args) {
		SpringApplication.run(KeuanganApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		// TODO Auto-generated method stub
		List<Akun> akuns = akunService.findAll();
		if(akuns.size() == 0) {
			logger.info(String.format("Inisialisasi database tabel akun %s", ""));
			Akun pengeluaran = new Akun(null,"Kas Keluar", 1, "pengeluaran", 1, "Aktif", new Date());
			akunService.save(pengeluaran);
			Akun pemasukan = new Akun(null,"Kas Masuk", 2, "pemasukan", 1, "Aktif", new Date());
			akunService.save(pemasukan);
			Akun hutang = new Akun(null,"Hutang", 3, "hutang", 1, "Aktif", new Date());
			akunService.save(hutang);
			Akun piutang = new Akun(null,"Piutang", 4, "piutang", 1, "Aktif", new Date());
			akunService.save(piutang);
		}
	}

}
