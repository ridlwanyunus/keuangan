package com.example.laporan.keuangan.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.laporan.keuangan.entity.Akun;

@Repository
public interface AkunRepository extends JpaRepository<Akun, Integer>{

	@Query("select a from Akun a where idAkun = :idAkun")
	public Akun findByIdAkun(@Param("idAkun") Integer idAkun); 
	
	
}
