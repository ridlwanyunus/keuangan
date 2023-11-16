package com.example.laporan.keuangan.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.laporan.keuangan.entity.Saldo;
import com.example.laporan.keuangan.repository.SaldoRepository;

@Service
public class SaldoService {

	@Autowired
	private SaldoRepository repo;
	
	
	public List<Saldo> findAll(){
		return repo.findAll();
	}
	
	public Saldo findByIdSaldo(Integer idSaldo) {
		return repo.findByIdSaldo(idSaldo);
	}
	
	public Saldo findByTahunAndBulan(Integer tahun, Integer bulan) {
		return repo.findByTahunAndBulan(tahun, bulan);
	}
	
	public List<Saldo> findByTahun(Integer tahun){
		return repo.findByTahun(tahun);
	}
	
	public void save(Saldo saldo) {
		repo.save(saldo);
	}
	
	public void delete(Saldo saldo) {
		repo.delete(saldo);
	}
	
}
