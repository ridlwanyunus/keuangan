/**
 * 
 */
"use strict";

var index = function() {

    var initPage = function() {
        $.ajax({
            url: '/index/summary',
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
    	
    	var cashInBudget = data.cashInBudget;
    	var cashOutBudget  = data.cashOutBudget;
    	var hutangBudget  = data.hutangBudget;
    	var piutangBudget  = data.piutangBudget;
    	
    	var cashInMonth = data.cashInMonth;
    	var cashOutMonth = data.cashOutMonth;
    	var hutangMonth = data.hutangMonth;
    	var piutangMonth = data.piutangMonth;
    	
    	var totalBudget = (cashInBudget + piutangBudget) - (cashOutBudget + hutangBudget);
    	var saldoAtm = (cashIn + piutang) - (cashOut + hutang);
    	
    	var totalCash = saldoAtm + totalBudget;
    	
    	
    	$('#saldo-atm').text(Utils.currencyFormat(saldoAtm))
    	$('#total-cash').text(Utils.currencyFormat(totalCash))
    	$('#total-budget').text(Utils.currencyFormat(totalBudget))
    	$('#total-cashin-month').text(Utils.currencyFormat(cashInMonth + piutangMonth))
    	$('#total-cashout-month').text(Utils.currencyFormat(cashOutMonth + hutangMonth))
    	$('#total-cashin').text(Utils.currencyFormat(cashIn + piutang))
    	$('#total-cashout').text(Utils.currencyFormat(cashOut + hutang))
    	
    	var today = new Date();
    	var month = today.toLocaleString('default', { month: 'long' })
    	var tanggalToday = month +"  "+ today.getDate() + ", " + today.getFullYear();
    	var monthYear = month +" "+ today.getFullYear();
    	$('#tanggal-today').text(tanggalToday);
    	$('.month-year').text(monthYear);
    }
    
    var initStatistics = function() {
    	$('#statistic-year').text(new Date().getFullYear());
    
        $.ajax({
            url: '/index/statistics',
            type: 'GET',
            contentType: 'application/json',
            success: function(response) {
                demo2(response.data.pemasukan, response.data.pengeluaran);
            }
        })
    }
    
    
    var demo2 = function(cashIn, cashOut) {
	
		var pemasukan = [];
			
		$.each(cashIn, function(i, item){
			pemasukan.push([i, item])
		});
		
		var pengeluaran = [];
		
		$.each(cashOut, function(i, item){
			pengeluaran.push([i, item])
		});
		
		
		function randValue() {
			return (Math.floor(Math.random() * (1 + 40 - 20))) + 20;
		}
/*		var pemasukan = [
			[0, 0],
			[1, randValue()],
			[2, randValue()],
			[3, 2 + randValue()],
			[4, 3 + randValue()],
			[5, 5 + randValue()],
			[6, 10 + randValue()],
			[7, 15 + randValue()],
			[8, 20 + randValue()],
			[9, 25 + randValue()],
			[10, 30 + randValue()],
			[11, 35 + randValue()],
			[12, 25 + randValue()],
		];
		var pengeluaran = [
			[0, 0],
			[1, randValue() - 5],
			[2, randValue() - 5],
			[3, randValue() - 5],
			[4, 6 + randValue()],
			[5, 5 + randValue()],
			[6, 20 + randValue()],
			[7, 25 + randValue()],
			[8, 36 + randValue()],
			[9, 26 + randValue()],
			[10, 38 + randValue()],
			[11, 39 + randValue()],
			[12, 50 + randValue()],
		];*/

		var plot = $.plot($("#kt_flotcharts_2"), [{
			data: pemasukan,
			label: "Pemasukan",
			lines: {
				lineWidth: 1,
			},
			shadowSize: 0

		}, {
			data: pengeluaran,
			label: "Pengeluaran",
			lines: {
				lineWidth: 1,
			},
			shadowSize: 0
		}], {
			series: {
				lines: {
					show: true,
					lineWidth: 2,
					fill: true,
					fillColor: {
						colors: [{
							opacity: 0.05
						}, {
							opacity: 0.01
						}]
					}
				},
				points: {
					show: true,
					radius: 3,
					lineWidth: 1
				},
				shadowSize: 2
			},
			grid: {
				hoverable: true,
				clickable: true,
				tickColor: "#eee",
				borderColor: "#eee",
				borderWidth: 1
			},
			colors: [KTApp.getStateColor("success"), KTApp.getStateColor("danger")],
			xaxis: {
				ticks: 10,
				tickDecimals: 0,
				tickColor: "#eee",
			},
			yaxis: {
				ticks: 10,
				tickDecimals: 0,
				tickColor: "#eee",
			}
		});

		function showTooltip(x, y, contents) {
			$('<div id="tooltip">' + contents + '</div>').css({
				position: 'absolute',
				display: 'none',
				top: y + 5,
				left: x + 15,
				border: '1px solid #333',
				padding: '4px',
				color: '#fff',
				'border-radius': '3px',
				'background-color': '#333',
				opacity: 0.80
			}).appendTo("body").fadeIn(200);
		}

		var previousPoint = null;
		$("#chart_2").bind("plothover", function(event, pos, item) {
			$("#x").text(pos.x.toFixed(2));
			$("#y").text(pos.y.toFixed(2));

			if (item) {
				if (previousPoint != item.dataIndex) {
					previousPoint = item.dataIndex;

					$("#tooltip").remove();
					var x = item.datapoint[0].toFixed(2),
						y = item.datapoint[1].toFixed(2);

					showTooltip(item.pageX, item.pageY, item.series.label + " of " + x + " = " + y);
				}
			} else {
				$("#tooltip").remove();
				previousPoint = null;
			}
		});
	}
    
    var initHistory = function(){

    	$.ajax({
    		url: 'index/history',
    		type: 'GET',
    		contentType: 'application/json',
    		success: function(response){
    			console.log(response.data)
    			initStatusKeuangan(response.data[0])
    			loadDatatable(response);
    		}
    	});
    }
    
    var initStatusKeuangan = function(saldo){
    	if(saldo.selisih >= 0){
    	
    		var html = `<span class="kt-widget12__value" style="display:inline; color:#0abb87">+`+Utils.currencyFormat(saldo.selisih)+`</span>
							  <span class="kt-badge kt-badge--inline kt-badge--success" style="margin-left: 5px;">surplus</span>
							  `;
    		
    	} else 
    	if(saldo.selisih < 0){
    		var html = `<span class="kt-widget12__value" style="display:inline; color:#fd397a">`+Utils.currencyFormat(saldo.selisih)+`</span>
							  <span class="kt-badge kt-badge--inline kt-badge--danger" style="margin-left: 5px;">minus</span>
							  `;
    	}
    	$('#status-keuangan').html(html);
    }
    
    const month = [
    	"Januari",
    	"Februari",
    	"Maret",
    	"April",
    	"Mei",
    	"Juni",
    	"Juli",
    	"Agustus",
    	"September",
    	"Oktober",
    	"November",
    	"Desember"
    	
    ]
    
    var loadDatatable = function(response){
    	var table = $('#kt_table');

        table.DataTable({
            data: response.data,
            destroy: true,
            scrollY: '50vh',
            scrollX: true,
            scrollCollapse: true,
            ordering: false,
            columns: [{
                    "data": null,
                    render: function(data, type, full, meta) {
                        return meta.row + meta.settings._iDisplayStart + 1;
                    }
                },
                {
                    "data": "tahun"
                },
                {
                    "data": "bulan",
                    render: function(data, type, full, meta){
                    	var bulan = full.bulan - 1;
                    	return month[bulan];
                    }
                },
                {
                    "data": null,
                    render: function(data, type, full, meta){
                    	var currency = Utils.currencyFormat(full.masuk);
						return currency;                    	
                    }
                },
                {
                    "data": null,
                    render: function(data, type, full, meta){
                    	var currency = Utils.currencyFormat(full.keluar);
						return currency;                    	
                    }
                },
                {
                    "data": null,
                    render: function(data, type, full, meta) {
                    	var currency = Utils.currencyFormat(full.selisih);
                        var tipeNama = '';
                        if (full.status == 1) {
                            tipeNama = '<span class="kt-badge kt-badge--inline kt-badge--success">' + full.statusInfo + '</span><br><span style="color: #0abb87;">'+'+'+currency+'</span>';
                        } else
                        if (full.status == 0) {
                            tipeNama = '<span class="kt-badge kt-badge--inline kt-badge--primary">' + full.statusInfo + '</span>';
                        } else
                        if (full.status == -1) {
                            tipeNama = '<span class="kt-badge kt-badge--inline kt-badge--danger">' + full.statusInfo + '</span><br><span style="color: #fd397a;">'+currency+'</span>';
                        } 
                        return tipeNama;
                    }
                },
                {
                    "data": null,
                    render: function(data, type, full, meta){
                    	var currency = Utils.currencyFormat(full.total);
						return currency;                    	
                    }
                }
            ]

        });
    }

    return {
        init: function() {
            initPage();
            initStatistics();
            initHistory();
        }
    }

}();


jQuery(document).ready(function() {
    index.init();
});