package com.example.laporan.keuangan.entity;

import java.util.Date;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

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
	private Double hutang;
	private Double piutang;
	private Double selisih;
	private Double total;
	private Integer status;
	private String statusInfo;
}
