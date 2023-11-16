package com.example.laporan.keuangan.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.laporan.keuangan.entity.Saldo;

@Repository
public interface SaldoRepository extends JpaRepository<Saldo, Integer> {

	@Query("select s from Saldo s where idSaldo = :idSaldo")
	public Saldo findByIdSaldo(@Param("idSaldo") Integer idSaldo);

	@Query("select s from Saldo s where tahun = :tahun and bulan = :bulan")
	public Saldo findByTahunAndBulan(@Param("tahun") Integer tahun, @Param("bulan") Integer bulan);

	@Query("select s from Saldo s where tahun = :tahun order by tahun, bulan desc")
	public List<Saldo> findByTahun(@Param("tahun") Integer tahun);

	@Query("select s from Saldo s where keluar != 0 or masuk !=0 order by tahun, bulan desc")
	public List<Saldo> findAllWithTransaction();
	
}
