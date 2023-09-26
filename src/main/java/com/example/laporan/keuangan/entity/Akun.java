package com.example.laporan.keuangan.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

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
