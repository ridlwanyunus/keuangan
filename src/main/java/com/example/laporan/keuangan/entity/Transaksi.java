package com.example.laporan.keuangan.entity;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.ColumnResult;
import javax.persistence.ConstructorResult;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedNativeQuery;
import javax.persistence.SqlResultSetMapping;
import javax.persistence.Table;

import com.example.laporan.keuangan.response.TransaksiWrapper;
import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@SqlResultSetMapping(
		name = "transaksiWrapperMapping",
		classes = {
			@ConstructorResult(
				targetClass = TransaksiWrapper.class, 
				columns = {
					@ColumnResult(name = "id_transaksi", type = Integer.class),
					@ColumnResult(name = "nama", type = String.class),
					@ColumnResult(name = "cash_in", type = BigDecimal.class),
					@ColumnResult(name = "cash_out", type = BigDecimal.class),
					@ColumnResult(name = "id_akun", type = Integer.class),
					@ColumnResult(name = "id_budget", type = Integer.class),
					@ColumnResult(name = "status", type = Integer.class),
					@ColumnResult(name = "status_info", type = String.class),
					@ColumnResult(name = "creation_date", type = Date.class),
					@ColumnResult(name = "tipe_nama", type = String.class),
					@ColumnResult(name = "budget_nama", type = String.class),
				}
			)
			
		}
	)

@NamedNativeQuery(
		name = "Transaksi.findAllWithWrapper", 
		query = "select \r\n"
				+ "	t.id_transaksi, t.nama, t.cash_in, t.cash_out, t.id_akun, t.id_budget, t.status, t.status_info, t.creation_date, a.tipe_nama, b.nama budget_nama  \r\n"
				+ "from \r\n"
				+ "	transaksi t \r\n"
				+ "left join \r\n"
				+ "	akun a \r\n"
				+ "on t.id_akun = a.id_akun \r\n"
				+ "left join \r\n"
				+ "	budget b \r\n"
				+ "on t.id_budget = b.id_budget \r\n"
				+ "order by t.creation_date \r\n",
		resultSetMapping = "transaksiWrapperMapping",
		resultClass = TransaksiWrapper.class
)


@Entity
@Table(name = "transaksi")
public class Transaksi {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer idTransaksi;
	private Integer idAkun;
	private Integer idBudget;
	private String nama;
	private BigDecimal cashIn;
	private BigDecimal cashOut;
	private Integer status;
	private String statusInfo;
	private Date creationDate;
	
	public Transaksi() {

	}
	public Transaksi(Integer idTransaksi, Integer idAkun, Integer idBudget, String nama, BigDecimal cashIn,
			BigDecimal cashOut, Integer status, String statusInfo, Date creationDate) {
		super();
		this.idTransaksi = idTransaksi;
		this.idAkun = idAkun;
		this.idBudget = idBudget;
		this.nama = nama;
		this.cashIn = cashIn;
		this.cashOut = cashOut;
		this.status = status;
		this.statusInfo = statusInfo;
		this.creationDate = creationDate;
	}
	public BigDecimal getCashIn() {
		return cashIn;
	}
	public void setCashIn(BigDecimal cashIn) {
		this.cashIn = cashIn;
	}
	public BigDecimal getCashOut() {
		return cashOut;
	}
	public void setCashOut(BigDecimal cashOut) {
		this.cashOut = cashOut;
	}
	public Integer getIdTransaksi() {
		return idTransaksi;
	}

	public void setIdTransaksi(Integer idTransaksi) {
		this.idTransaksi = idTransaksi;
	}

	public Integer getIdAkun() {
		return idAkun;
	}

	public void setIdAkun(Integer idAkun) {
		this.idAkun = idAkun;
	}

	public Integer getIdBudget() {
		return idBudget;
	}

	public void setIdBudget(Integer idBudget) {
		this.idBudget = idBudget;
	}

	public String getNama() {
		return nama;
	}

	public void setNama(String nama) {
		this.nama = nama;
	}


	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getStatusInfo() {
		return statusInfo;
	}

	public void setStatusInfo(String statusInfo) {
		this.statusInfo = statusInfo;
	}

	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}
	
	
}
