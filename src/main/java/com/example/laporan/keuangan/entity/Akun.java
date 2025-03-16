package com.example.laporan.keuangan.entity;

import java.util.Date;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="akun")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Akun {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer idAkun;
	
	private String nama;
	
	private Integer tipeAkun;
	
	private String tipeNama;
	
	private Integer status;
	
	private String statusInfo;
	
	@Temporal(TemporalType.DATE)
	@JsonFormat(pattern = "dd-MM-yyyy")
	private Date creationDate;
	
	
}
