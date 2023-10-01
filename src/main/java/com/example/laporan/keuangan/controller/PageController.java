package com.example.laporan.keuangan.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class PageController {

	
	@GetMapping("/")
	public ModelAndView index(Model model) {
		
		return new ModelAndView("index");
	}
	
	@GetMapping("/transaksi")
	public ModelAndView transaksi(Model model) {
		
		return new ModelAndView("transaksi");
	}
	
	@GetMapping("/akun")
	public ModelAndView akun(Model model) {
		
		return new ModelAndView("akun");
	}
	
	@GetMapping("/budget")
	public ModelAndView budget(Model model) {
		
		return new ModelAndView("budget");
	}
	
	@GetMapping("/kas")
	public ModelAndView kasArus(Model model) {
		
		return new ModelAndView("kas_arus");
	}
	
	@GetMapping("/laporan-budget")
	public ModelAndView laporanBudget(Model model) {
		
		return new ModelAndView("laporan_budget");
	}
	
}
