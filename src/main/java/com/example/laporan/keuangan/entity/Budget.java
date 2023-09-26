package com.example.laporan.keuangan.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

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
