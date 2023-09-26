package com.example.laporan.keuangan.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.laporan.keuangan.entity.Saldo;

@Repository
public interface SaldoRepository extends JpaRepository<Saldo, Integer> {

	@Query("select s from Saldo s where idSaldo = :idSaldo")
	public Saldo findByIdSaldo(@Param("idSaldo") Integer idSaldo);
	
	
}
