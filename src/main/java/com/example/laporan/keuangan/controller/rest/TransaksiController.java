package com.example.laporan.keuangan.controller.rest;

import java.util.Date;
import java.util.List;

import org.dozer.DozerBeanMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.laporan.keuangan.entity.Transaksi;
import com.example.laporan.keuangan.request.TransaksiAddRequest;
import com.example.laporan.keuangan.response.ResponseTemplate;
import com.example.laporan.keuangan.response.TransaksiWrapper;
import com.example.laporan.keuangan.service.TransaksiService;
import com.example.laporan.keuangan.utils.AppConstant;
import com.example.laporan.keuangan.utils.TransaksiUtils;

@RestController
@RequestMapping("transaksi")
public class TransaksiController {

	
	@Autowired
	private TransaksiService transaksiService;
	
	@Autowired
	private TransaksiUtils transaksiUtils;
	
	@GetMapping("find/{idTransaksi}")
	public ResponseTemplate findById(@PathVariable("idTransaksi") Integer idTransaksi) {
		ResponseTemplate response = new ResponseTemplate();
		Transaksi akun = transaksiService.findByIdTransaksi(idTransaksi);
		
		response.setStatus(1);
		response.setMessage("success get the records");
		response.setData(akun);
		
		return response;
	}
	
	@GetMapping("list")
	public ResponseTemplate list() {
				
		List<TransaksiWrapper> transaksis = transaksiService.findAllWithWrapper();
		ResponseTemplate response = new ResponseTemplate();
		response.setStatus(1);
		response.setMessage("success get the transaksi");
		response.setData(transaksis);
		
		return response;
	}
	
	@GetMapping("list/filter")
	public ResponseTemplate listWithFilter(@RequestParam("start") String start, @RequestParam("end") String end) {
	
		List<TransaksiWrapper> transaksis = transaksiService.findByStartAndEndDate(start, end);
		ResponseTemplate response = new ResponseTemplate();
		response.setStatus(1);
		response.setMessage("success get the transaksi");
		response.setData(transaksis);
		
		return response;
	}
	
	@PostMapping("add")
	public ResponseTemplate save(@RequestBody TransaksiAddRequest request) {
		
		DozerBeanMapper dozer = new DozerBeanMapper();
		
		Transaksi transaksi = new Transaksi();
		
		transaksi = dozer.map(request, Transaksi.class);
		transaksi.setIdTransaksi(null);
		
		switch (request.getStatus()){
			case 0: 
				transaksi.setStatusInfo(AppConstant.TRANSAKSI_STATUSINFO_NONACTIVE);
				break;
			case 1: 
				transaksi.setStatusInfo(AppConstant.TRANSAKSI_STATUSINFO_ACTIVE);
				break;
			
			default:
				break;
		}
		
		transaksiService.save(transaksi);
		
		if(transaksi.getIdBudget() != null)
			transaksiUtils.budgetRecalculate(transaksi);
		
		ResponseTemplate response = new ResponseTemplate();
		response.setStatus(1);
		response.setMessage("success save the annotation");
		response.setData(transaksi);
		
		return response;
	}
	
	@PostMapping("edit/{idTransaksi}")
	public ResponseTemplate update(@PathVariable("idTransaksi") Integer idTransaksi, @RequestBody TransaksiAddRequest request) {
		ResponseTemplate response = new ResponseTemplate();
		
		Transaksi findOneTransaksi = transaksiService.findByIdTransaksi(idTransaksi);
		
		if(findOneTransaksi == null) {
			response.setStatus(0);
			response.setMessage(String.format("Transaksi %s tidak ditemukan", request.getNama()));
			return response;
		}
		

		DozerBeanMapper dozer = new DozerBeanMapper();
		
		Transaksi transaksi = new Transaksi();
		transaksi = dozer.map(request, Transaksi.class);
		transaksi.setIdTransaksi(idTransaksi);
		
		
		switch (request.getStatus()){
			case 0: 
				transaksi.setStatusInfo(AppConstant.TRANSAKSI_STATUSINFO_NONACTIVE);
				break;
			case 1: 
				transaksi.setStatusInfo(AppConstant.TRANSAKSI_STATUSINFO_ACTIVE);
				break;
			
			default:
				break;
		}

		transaksiService.save(transaksi);
		if(transaksi.getIdBudget() != null)
			transaksiUtils.budgetRecalculate(transaksi);
		
		
		response.setStatus(1);
		response.setMessage("success update the record");
		response.setData(transaksi);
		
		return response;
	}
	
	@GetMapping("/delete/{idTransaksi}")
	public ResponseTemplate delete(@PathVariable("idTransaksi") Integer idTransaksi) {
		ResponseTemplate response = new ResponseTemplate();
		try {
			
			Transaksi findOneTransaksi = transaksiService.findByIdTransaksi(idTransaksi);
			
			if(findOneTransaksi == null) {
				response.setStatus(0);
				response.setMessage("Tidak bisa dihapus karena transaksi tidak ditemukan");
				return response;
			}
			
			transaksiService.delete(findOneTransaksi);
			if(findOneTransaksi.getIdBudget() != null)
				transaksiUtils.budgetRecalculate(findOneTransaksi);
			
			response.setStatus(1);
			response.setMessage("Berhasil menghapus transaksi");
			response.setData(findOneTransaksi);
		} catch (Exception e) {
			response.setStatus(0);
			response.setMessage(e.getMessage());
		}
		
		
		return response;
	}
	
}
