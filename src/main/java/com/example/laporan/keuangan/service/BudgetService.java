package com.example.laporan.keuangan.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.laporan.keuangan.entity.Budget;
import com.example.laporan.keuangan.repository.BudgetRepository;

@Service
public class BudgetService {

	@Autowired
	private BudgetRepository repo;
		
	
	public List<Budget> findAll(){
		return repo.findAll();
	}
	
	public Budget findByIdBudget(Integer idBudget) {
		return repo.findByIdBudget(idBudget);
	}
	
	public List<Budget> findByIdAkun(Integer idAkun){
		return repo.findByIdAkun(idAkun);
	}
	
	public void save(Budget budget) {
		repo.save(budget);
	}
	
	public void delete(Budget budget) {
		repo.delete(budget); 
	}
	
	
}
