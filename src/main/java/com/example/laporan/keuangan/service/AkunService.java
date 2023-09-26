package com.example.laporan.keuangan.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.laporan.keuangan.entity.Akun;
import com.example.laporan.keuangan.repository.AkunRepository;

@Service
public class AkunService {

	@Autowired
	private AkunRepository repo;
	
	
	public List<Akun> findAll(){
		return repo.findAll();
	}
	
	public Akun findByIdAkun(Integer idAkun) {
		return repo.findByIdAkun(idAkun);
	}
	
	public void save(Akun akun) {
		repo.save(akun);
	}
	
	public void delete(Akun akun) {
		repo.delete(akun); 
	}
	
}
