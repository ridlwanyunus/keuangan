package com.example.laporan.keuangan.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.laporan.keuangan.entity.Transaksi;
import com.example.laporan.keuangan.response.TransaksiWrapper;

@Repository
public interface TransaksiRepository extends JpaRepository<Transaksi, Integer> {

	@Query("select t from Transaksi t where idTransaksi = :idTransaksi")
	public Transaksi findByIdTransaksi(@Param("idTransaksi") Integer idTransaksi);
	
	@Query(nativeQuery = true)
	public List<TransaksiWrapper> findAllWithWrapper();
}
