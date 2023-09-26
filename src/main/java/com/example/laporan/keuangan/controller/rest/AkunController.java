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
import org.springframework.web.bind.annotation.RestController;

import com.example.laporan.keuangan.entity.Akun;
import com.example.laporan.keuangan.request.AkunAddRequest;
import com.example.laporan.keuangan.response.ResponseTemplate;
import com.example.laporan.keuangan.service.AkunService;
import com.example.laporan.keuangan.utils.AppConstant;


@RestController
@RequestMapping("akun")
public class AkunController {

	@Autowired
	private AkunService akunService;
	
	@GetMapping("find/{idAkun}")
	public ResponseTemplate findById(@PathVariable("idAkun") Integer idAkun) {
		ResponseTemplate response = new ResponseTemplate();
		Akun akun = akunService.findByIdAkun(idAkun);
		
		response.setStatus(1);
		response.setMessage("success get the records");
		response.setData(akun);
		
		return response;
	}
	
	@GetMapping("list")
	public ResponseTemplate list() {
		
		List<Akun> akuns = akunService.findAll();		
		ResponseTemplate response = new ResponseTemplate();
		response.setStatus(1);
		response.setMessage("success get the annotation");
		response.setData(akuns);
		
		return response;
	}
	
	@PostMapping("add")
	public ResponseTemplate save(@RequestBody AkunAddRequest request) {
		
		DozerBeanMapper dozer = new DozerBeanMapper();
		
		Akun akun = new Akun();
		
		akun = dozer.map(request, Akun.class);
		akun.setIdAkun(null);
		
		switch (request.getTipeAkun()){
		case 0: 
			akun.setTipeNama(AppConstant.AKUN_TYPENAME_PENGELUARAN);
			break;
		case 1: 
			akun.setTipeNama(AppConstant.AKUN_TYPENAME_PEMASUKAN);
			break;
		case 2: 
			akun.setTipeNama(AppConstant.AKUN_TYPENAME_HUTANG);
			break;
		case 3: 
			akun.setTipeNama(AppConstant.AKUN_TYPENAME_PIUTANG);
			break;
		default:
			break;
		}
		
		
		switch (request.getStatus()){
			case 0: 
				akun.setStatusInfo(AppConstant.AKUN_STATUSINFO_NONACTIVE);
				break;
			case 1: 
				akun.setStatusInfo(AppConstant.AKUN_STATUSINFO_ACTIVE);
				break;
			
			default:
				break;
		}
		
		akun.setCreationDate(new Date());
		
		akunService.save(akun);
		
		ResponseTemplate response = new ResponseTemplate();
		response.setStatus(1);
		response.setMessage("success save the annotation");
		response.setData(akun);
		
		return response;
	}
	
	@PostMapping("edit/{idAkun}")
	public ResponseTemplate update(@PathVariable("idAkun") Integer idAkun, @RequestBody AkunAddRequest request) {
		ResponseTemplate response = new ResponseTemplate();
		
		Akun findOneAkun = akunService.findByIdAkun(idAkun);
		
		if(findOneAkun == null) {
			response.setStatus(0);
			response.setMessage(String.format("Akun %s tidak ditemukan", request.getNama()));
			return response;
		}
		

		DozerBeanMapper dozer = new DozerBeanMapper();
		
		Akun akun = new Akun();
		akun = dozer.map(request, Akun.class);
		
		akun.setIdAkun(idAkun);
		
		switch (request.getTipeAkun()){
			case 0: 
				akun.setTipeNama(AppConstant.AKUN_TYPENAME_PENGELUARAN);
				break;
			case 1: 
				akun.setTipeNama(AppConstant.AKUN_TYPENAME_PEMASUKAN);
				break;
			case 2: 
				akun.setTipeNama(AppConstant.AKUN_TYPENAME_HUTANG);
				break;
			case 3: 
				akun.setTipeNama(AppConstant.AKUN_TYPENAME_PIUTANG);
				break;
			default:
				break;
		}
		
		
		switch (request.getStatus()){
			case 0: 
				akun.setStatusInfo(AppConstant.AKUN_STATUSINFO_NONACTIVE);
				break;
			case 1: 
				akun.setStatusInfo(AppConstant.AKUN_STATUSINFO_ACTIVE);
				break;
			
			default:
				break;
		}
		
		akun.setCreationDate(new Date());
		
		akunService.save(akun);
		
		
		response.setStatus(1);
		response.setMessage("success update the record");
		response.setData(akun);
		
		return response;
	}
	
	@GetMapping("/delete/{idAkun}")
	public ResponseTemplate delete(@PathVariable("idAkun") Integer idAkun) {
		ResponseTemplate response = new ResponseTemplate();
		try {
			
			Akun findOneAkun = akunService.findByIdAkun(idAkun);
			
			if(findOneAkun == null) {
				response.setStatus(0);
				response.setMessage("Tidak bisa dihapus karena akun tidak ditemukan");
				return response;
			}
			
			akunService.delete(findOneAkun);
			
			
			response.setStatus(1);
			response.setMessage("Berhasil menghapus akun");
			response.setData(findOneAkun);
		} catch (Exception e) {
			response.setStatus(0);
			response.setMessage(e.getMessage());
		}
		
		
		return response;
	}
	
}
