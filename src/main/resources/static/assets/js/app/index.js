/**
 * 
 */
"use strict";

var index = function() {

    var initPage = function() {
        $.ajax({
            url: '/index/load',
            type: 'GET',
            contentType: 'application/json',
            success: function(response) {
                summaryDatatable(response);
            }
        })
    }

    
    var summaryDatatable = function(response) {
    	var datas = response.data;

    	var cashIn = 0;
    	var cashOut = 0;
    	var piutang = 0;
    	var hutang = 0;
    	
    	var data = response.data;
    	var cashIn = data.cashIn;
    	var cashOut = data.cashOut;
    	var hutang = data.hutang;
    	var piutang = data.piutang;
    	
    	var cashInMonth = data.cashInMonth;
    	var cashOutMonth = data.cashOutMonth;
    	var hutangMonth = data.hutangMonth;
    	var piutangMonth = data.piutangMonth;
    	
    	
    	var saldoAtm = (cashIn + piutang) - (cashOut + hutang);
    	
    	$('#saldo-atm').text(Utils.currencyFormat(saldoAtm))
    	$('#total-cashin').text(Utils.currencyFormat(cashIn + piutang))
    	$('#total-cashout').text(Utils.currencyFormat(cashOut + hutang))
    	$('#total-cashin-month').text(Utils.currencyFormat(cashInMonth + piutangMonth))
    	$('#total-cashout-month').text(Utils.currencyFormat(cashOutMonth + hutangMonth))
    	var today = new Date();
    	var month = today.toLocaleString('default', { month: 'long' })
    	var tanggalToday = month +"  "+ today.getDate() + ", " + today.getFullYear();
    	var monthYear = month +" "+ today.getFullYear();
    	$('#tanggal-today').text(tanggalToday);
    	$('.month-year').text(monthYear);
    }

    return {
        init: function() {
            initPage();
        }
    }

}();


jQuery(document).ready(function() {
    index.init();
});