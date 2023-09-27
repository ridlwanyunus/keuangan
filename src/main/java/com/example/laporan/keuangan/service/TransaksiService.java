package com.example.laporan.keuangan.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.laporan.keuangan.entity.Transaksi;
import com.example.laporan.keuangan.repository.TransaksiRepository;
import com.example.laporan.keuangan.response.TransaksiWrapper;

@Service
public class TransaksiService {

	@Autowired
	private TransaksiRepository repo;
	
	public List<Transaksi> findAll(){
		return repo.findAll();
	}
	
	public Transaksi findByIdTransaksi(Integer idTransaksi){
		return repo.findByIdTransaksi(idTransaksi);
	}
	
	public void save(Transaksi entity) {
		repo.save(entity);
	}
	
	public void delete(Transaksi transaksi) {
		repo.delete(transaksi);
	}
	
	public List<TransaksiWrapper> findAllWithWrapper(){
		return repo.findAllWithWrapper();
	}
	
	public List<TransaksiWrapper> findByStartAndEndDate(String start, String end){
		return repo.findByStartAndEndDateWithWrapper(start, end);
	}
}
