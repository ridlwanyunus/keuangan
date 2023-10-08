/**
 * 
 */
"use strict";

var laporanBudget = function() {

    var initPage = function() {
		loadBudget();
    }
    
    var buttonHandler = function(){
    	$('#search').on('click', function(){
    		var idBudget = $('#idBudget').val();
    		loadData(idBudget);
    		
    	});
    }
    
    var loadData = function(idBudget){
    	$.ajax({
            url: '/laporan/budget/'+idBudget,
            type: 'GET',
            contentType: 'application/json',
            success: function(response) {
            	$('tr.fc-list-item.fc-event-accent').empty();
            	
            	$('.fc-list-heading').empty();
                processor(response);
            }
        })
    };

    var loadBudget = function() {
        $.ajax({
            url: '/budget/list',
            type: 'GET',
            contentType: 'application/json',
            success: function(response) {
                var items = response.data;
				$('#idBudget').empty();
				
				var o = new Option("Pilih Jenis Anggaran", "");
				$(o).html("Pilih Jenis Anggaran");
				$('#idBudget').append(o);
				
                $.each(items, function(i, item) {
                    var o = new Option(item.nama, item.idBudget);
                    $(o).html(item.nama);
                    $('#idBudget').append(o);
                });
            }
        });
    }
    var processor = function(response) {
    	var datas = response.data;
    	var budgets = datas.budgets;
    	var transaksis = datas.transaksis;
    	
    	$.each(budgets, function(i, budget){
    	
	    	var tempTransaksi = []
    		$.each(transaksis, function(j, transaksi){
    			console.log(transaksi)
    			if(budget.idBudget == transaksi.idBudget){
	    			tempTransaksi.push(transaksi);
	    		}
    		});
    		
    		createDetail(budget.nama, budget.biaya, budget.capaian, tempTransaksi);
    	});
    }
    
    var createDetail = function(id, biaya, capaian, items){
    
	    var totalCashIn = 0;
    	var totalCashOut = 0;
    	
    	
    	
	    $.each(items, function(i, item){
	    	totalCashIn = totalCashIn + item.cashIn;
	    	totalCashOut = totalCashOut + item.cashOut;
    	});
		
		var progress = Utils.currencyFormat(capaian)+" dari "+Utils.currencyFormat(biaya);
		
    	var separator = '<tr class="fc-list-heading" style="background: white;" data-date="2023-09-29"><td class="fc-widget-header" style="background: white;" colspan="5"><a class="fc-list-heading-main" data-goto="{&quot;date&quot;:&quot;2023-09-29&quot;,&quot;type&quot;:&quot;day&quot;}">'+id+'</a><a class="fc-list-heading-alt" data-goto="{&quot;date&quot;:&quot;2023-09-29&quot;,&quot;type&quot;:&quot;day&quot;}">'+progress+'</a></td></tr>';
    	$("tbody").append(separator);
    	
    	var index = 1;
    	$.each(items, function(i, item){
    		var detailItem = '<tr class="fc-list-item fc-event-accent"><td class="fc-list-item-title fc-widget-content" style="width:5%"><span>'+index+'</span><div class="fc-description"></div></td><td class="fc-list-item-title fc-widget-content" style="width:10%"><span>'+item.creationDate+'</span><div class="fc-description"></div></td><td class="fc-list-item-title fc-widget-content" style="width:30%"><span>'+item.nama+'</span><div class="fc-description"></div></td><td class="fc-list-item-title fc-widget-content" style="width:25%;text-align:right"><span>'+Utils.currencyFormat(item.cashIn)+'</span><div class="fc-description"></div></td><td class="fc-list-item-title fc-widget-content" style="width:25%;text-align:right"><span>'+Utils.currencyFormat(item.cashOut)+'</span><div class="fc-description"></div></td></tr>';
	    	$("tbody").append(detailItem);
	    	index++;
	    	
    	});
    	
    	var total = '<tr class="fc-list-item fc-event-accent"><td class="fc-list-item-title fc-widget-content" style="width:5%"><a></a><div class="fc-description"></div></td><td class="fc-list-item-title fc-widget-content" style="width:10%"><a></a><div class="fc-description"></div></td><td class="fc-list-item-title fc-widget-content" style="width:30%"><a>Total</a><div class="fc-description"></div></td><td class="fc-list-item-title fc-widget-content" style="width:25%;text-align:right"><a>'+Utils.currencyFormat(totalCashIn)+'</a><div class="fc-description"></div></td><td class="fc-list-item-title fc-widget-content" style="width:25%;text-align:right"><a>'+Utils.currencyFormat(totalCashOut)+'</a><div class="fc-description"></div></td></tr>';
    	$("tbody").append(total);
    }
    
    var createTotal = function(id, item){
    	var separator = '<tr class="fc-list-heading" data-date=""><td class="fc-widget-header" colspan="5"><a class="fc-list-heading-main" data-goto="{&quot;date&quot;:&quot;2023-09-29&quot;,&quot;type&quot;:&quot;day&quot;}">'+id+'</a><a class="fc-list-heading-alt" data-goto="{&quot;date&quot;:&quot;2023-09-29&quot;,&quot;type&quot;:&quot;day&quot;}"></a></td></tr>';
    	$("tbody").append(separator);
    	
    	var index = '';
    	var detailItem = '<tr class="fc-list-item fc-event-accent"><td class="fc-list-item-title fc-widget-content" style="width:5%"><span>'+index+'</span><div class="fc-description"></div></td><td class="fc-list-item-title fc-widget-content" style="width:45%"><a>'+item.nama+'</a><div class="fc-description"></div></td><td class="fc-list-item-title fc-widget-content" style="width:25%;text-align:right"><a>'+Utils.currencyFormat(item.cashIn)+'</a><div class="fc-description"></div></td><td class="fc-list-item-title fc-widget-content" style="width:25%;text-align:right"><a>'+Utils.currencyFormat(item.cashOut)+'</a><div class="fc-description"></div></td></tr>';
	    $("tbody").append(detailItem);
    }

    return {
        init: function() {
            initPage();
            buttonHandler();
        }
    }

}();


jQuery(document).ready(function() {
    laporanBudget.init();
});