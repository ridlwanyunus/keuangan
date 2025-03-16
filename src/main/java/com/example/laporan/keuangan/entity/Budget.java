package com.example.laporan.keuangan.entity;

import java.util.Date;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="budget")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Budget {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer idBudget;
	private String nama;
	private Integer idAkun;
	private Double biaya;
	private Double capaian;
	private Double selisih;
	private Integer status;
	private String statusInfo;
	private Date creationDate;

}
