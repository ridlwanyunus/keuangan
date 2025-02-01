/**
 * 
 */
"use strict";

var kasArus = function() {

    var initPage = function() {
    	var date = new Date();
    	var bulan = date.getMonth()+1;
    	var tahun = date.getFullYear();
    	
    	$('#bulan').val(bulan);
	    $('#tahun').val(tahun);
	    
	    tahunDropDown();
        loadData(bulan, tahun);
    }
    
    var tahunDropDown = function(){
		var date = new Date();
		var startYear = 2023;
		var endYear = date.getFullYear();
		
		for(var i = startYear; i<=endYear; i++ ){
			var o = new Option(i, i);
			$(o).html(i);
			$("#tahun").append(o);
		}
		
		$("#tahun option[value="+endYear+"]").attr('selected','selected');
		
	}
    
    var buttonHandler = function(){
    	$('#search').on('click', function(){
    		var bulan = $('#bulan').val();
    		var tahun = $('#tahun').val();
    		
    		loadData(bulan, tahun);
    		
    	});
    }
    
    var loadData = function(bulan, tahun){
    	$.ajax({
            url: '/laporan/kas/'+bulan+'/'+tahun,
            type: 'GET',
            contentType: 'application/json',
            success: function(response) {
            	$('tr.fc-list-item.fc-event-accent').empty();
            	
            	$('.fc-list-heading').empty();
                processor(response);
            }
        })
    };

    
    var processor = function(response) {
    	var datas = response.data;
    	
    	var itemPengeluaran = {}
    	var itemPemasukan = {}
    	var itemHutang = {}
    	var itemPiutang = {}
    	
    	var counterPengeluaran = 0;
    	var counterPemasukan = 0;
    	var counterHutang = 0;
    	var counterPiutang = 0;
    	
    	var totalPengeluaran = 0;
    	var totalPemasukan = 0;
    	var totalHutang = 0;
    	var totalPiutang = 0;
    	
    	$.each(datas, function(i, data){
    		if(data.idAkun == 1){
    			itemPengeluaran[counterPengeluaran] = data;
    			totalPengeluaran = totalPengeluaran + data.cashOut;
    			counterPengeluaran++;
    		}
    		else
    		if(data.idAkun == 2){
    			itemPemasukan[counterPemasukan] = data;
    			totalPemasukan = totalPemasukan + data.cashIn;
    			counterPemasukan++;
    		}
    		else
    		if(data.idAkun == 3){
	    		itemHutang[counterHutang] = data;
	    		totalHutang = totalHutang + data.cashOut;
	    		counterHutang++;
	    	}
    		else
    		if(data.idAkun == 4){
    			itemPiutang[counterPiutang] = data;
    			totalPiutang = totalPiutang + data.cashIn;
    			counterPiutang++;
    		}
    	});
    	createDetail('Pemasukan',itemPemasukan);
    	createDetail('Pengeluaran',itemPengeluaran);
    	createDetail('Piutang',itemPiutang);
    	createDetail('Hutang',itemHutang);
    	
    	var total = {
    		nama: "Total",
    		cashIn: totalPemasukan + totalPiutang,
    		cashOut: totalPengeluaran + totalHutang
    	};
    	
    	createTotal('Summary',total);
    }
    
    var createDetail = function(id, items){
		
    	var separator = '<tr class="fc-list-heading" style="background: white;" data-date="2023-09-29"><td class="fc-widget-header" style="background: white;" colspan="4"><a class="fc-list-heading-main" data-goto="{&quot;date&quot;:&quot;2023-09-29&quot;,&quot;type&quot;:&quot;day&quot;}">'+id+'</a><a class="fc-list-heading-alt" data-goto="{&quot;date&quot;:&quot;2023-09-29&quot;,&quot;type&quot;:&quot;day&quot;}"></a></td></tr>';
    	$("tbody").append(separator);
    	
    	var totalCashIn = 0;
    	var totalCashOut = 0;
    	var index = 1;
    	$.each(items, function(i, item){
    		var detailItem = '<tr class="fc-list-item fc-event-accent"><td class="fc-list-item-title fc-widget-content" style="width:5%"><span>'+index+'</span><div class="fc-description"></div></td><td class="fc-list-item-title fc-widget-content" style="width:45%"><span>'+item.nama+'</span><div class="fc-description"></div></td><td class="fc-list-item-title fc-widget-content" style="width:25%;text-align:right"><span>'+Utils.currencyFormat(item.cashIn)+'</span><div class="fc-description"></div></td><td class="fc-list-item-title fc-widget-content" style="width:25%;text-align:right"><span>'+Utils.currencyFormat(item.cashOut)+'</span><div class="fc-description"></div></td></tr>';
	    	$("tbody").append(detailItem);
	    	index++;
	    	
	    	totalCashIn = totalCashIn + item.cashIn;
	    	totalCashOut = totalCashOut + item.cashOut;
    	});
    	
    	var total = '<tr class="fc-list-item fc-event-accent"><td class="fc-list-item-title fc-widget-content" style="width:5%"><a></a><div class="fc-description"></div></td><td class="fc-list-item-title fc-widget-content" style="width:45%"><a>Total</a><div class="fc-description"></div></td><td class="fc-list-item-title fc-widget-content" style="width:25%;text-align:right"><a>'+Utils.currencyFormat(totalCashIn)+'</a><div class="fc-description"></div></td><td class="fc-list-item-title fc-widget-content" style="width:25%;text-align:right"><a>'+Utils.currencyFormat(totalCashOut)+'</a><div class="fc-description"></div></td></tr>';
    	$("tbody").append(total);
    }
    
    var createTotal = function(id, item){
    	var separator = '<tr class="fc-list-heading" data-date=""><td class="fc-widget-header" colspan="4"><a class="fc-list-heading-main" data-goto="{&quot;date&quot;:&quot;2023-09-29&quot;,&quot;type&quot;:&quot;day&quot;}">'+id+'</a><a class="fc-list-heading-alt" data-goto="{&quot;date&quot;:&quot;2023-09-29&quot;,&quot;type&quot;:&quot;day&quot;}"></a></td></tr>';
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
    kasArus.init();
});