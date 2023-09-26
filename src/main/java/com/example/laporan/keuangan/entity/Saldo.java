package com.example.laporan.keuangan.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "saldo")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Saldo {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer idSaldo;
	private Integer bulan;
	private Integer tahun;
	private Double masuk;
	private Double keluar;
	private Double total;
	private Integer status;
	private String statusInfo;
}
