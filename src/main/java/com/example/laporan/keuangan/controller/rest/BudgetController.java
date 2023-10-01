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

import com.example.laporan.keuangan.entity.Budget;
import com.example.laporan.keuangan.request.BudgetAddRequest;
import com.example.laporan.keuangan.response.ResponseTemplate;
import com.example.laporan.keuangan.service.BudgetService;
import com.example.laporan.keuangan.utils.AppConstant;

@RestController
@RequestMapping("budget")
public class BudgetController {

	
	@Autowired
	private BudgetService budgetService;
	
	@GetMapping("find/{idBudget}")
	public ResponseTemplate findById(@PathVariable("idBudget") Integer idBudget) {
		ResponseTemplate response = new ResponseTemplate();
		Budget budget = budgetService.findByIdBudget(idBudget);
		
		response.setStatus(1);
		response.setMessage("success get the records");
		response.setData(budget);
		
		return response;
	}
	
	@GetMapping("find/akun/{idAkun}")
	public ResponseTemplate findByIdAkun(@PathVariable("idAkun") Integer idAkun) {
		ResponseTemplate response = new ResponseTemplate();
		List<Budget> budgets = budgetService.findByIdAkun(idAkun);
		
		response.setStatus(1);
		response.setMessage("success get the records");
		response.setData(budgets);
		
		return response;
	}
	
	@GetMapping("list")
	public ResponseTemplate list() {
		
		List<Budget> budgets = budgetService.findAll();		
		ResponseTemplate response = new ResponseTemplate();
		response.setStatus(1);
		response.setMessage("success get the annotation");
		response.setData(budgets);
		
		return response;
	}
	
	@PostMapping("add")
	public ResponseTemplate save(@RequestBody BudgetAddRequest request) {
		
		DozerBeanMapper dozer = new DozerBeanMapper();
		
		Budget budget = new Budget();
		
		budget = dozer.map(request, Budget.class);
		budget.setIdBudget(null);
		
		switch (request.getStatus()){
			case 0: 
				budget.setStatusInfo(AppConstant.BUDGET_STATUSINFO_NONACTIVE);
				break;
			case 1: 
				budget.setStatusInfo(AppConstant.BUDGET_STATUSINFO_ACTIVE);
				break;
			
			default:
				break;
		}
		Double selisih = request.getBiaya() - request.getCapaian();
		budget.setSelisih(selisih);
		budget.setCreationDate(new Date());
		
		budgetService.save(budget);
		
		ResponseTemplate response = new ResponseTemplate();
		response.setStatus(1);
		response.setMessage("success save the budget");
		response.setData(budget);
		
		return response;
	}
	
	@PostMapping("edit/{idBudget}")
	public ResponseTemplate update(@PathVariable("idBudget") Integer idBudget, @RequestBody BudgetAddRequest request) {
		ResponseTemplate response = new ResponseTemplate();
		
		Budget findOneBudget = budgetService.findByIdBudget(idBudget);
		
		if(findOneBudget == null) {
			response.setStatus(0);
			response.setMessage(String.format("Budget %s tidak ditemukan", request.getNama()));
			return response;
		}
		

		DozerBeanMapper dozer = new DozerBeanMapper();
		
		Budget budget = new Budget();
		budget = dozer.map(request, Budget.class);
		budget.setIdBudget(idBudget);
		
		switch (request.getStatus()){
			case 0: 
				budget.setStatusInfo(AppConstant.BUDGET_STATUSINFO_NONACTIVE);
				break;
			case 1: 
				budget.setStatusInfo(AppConstant.BUDGET_STATUSINFO_ACTIVE);
				break;
			
			default:
				break;
		}
		Double selisih = request.getBiaya() - request.getCapaian();
		budget.setSelisih(selisih);
		budget.setCreationDate(findOneBudget.getCreationDate());
		
		budgetService.save(budget);
		
		
		response.setStatus(1);
		response.setMessage("success update the record");
		response.setData(budget);
		
		return response;
	}
	
	@GetMapping("/delete/{idBudget}")
	public ResponseTemplate delete(@PathVariable("idBudget") Integer idBudget) {
		ResponseTemplate response = new ResponseTemplate();
		try {
			
			Budget findOneBudget = budgetService.findByIdBudget(idBudget);
			
			if(findOneBudget == null) {
				response.setStatus(0);
				response.setMessage("Tidak bisa dihapus karena budget tidak ditemukan");
				return response;
			}
			
			budgetService.delete(findOneBudget);
			
			
			response.setStatus(1);
			response.setMessage("Berhasil menghapus budget");
			response.setData(findOneBudget);
		} catch (Exception e) {
			response.setStatus(0);
			response.setMessage(e.getMessage());
		}
		
		
		return response;
	}
	
}
