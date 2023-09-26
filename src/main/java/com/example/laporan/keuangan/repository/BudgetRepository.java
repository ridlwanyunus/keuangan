package com.example.laporan.keuangan.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.laporan.keuangan.entity.Budget;

@Repository
public interface BudgetRepository extends JpaRepository<Budget, Integer> {

	@Query("select b from Budget b where idBudget = :idBudget")
	public Budget findByIdBudget(@Param("idBudget") Integer idBudget); 
	
	
}
