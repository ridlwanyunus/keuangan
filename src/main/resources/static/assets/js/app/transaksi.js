/**
 * 
 */
"use strict";

var transaksi = function() {

    var loadDatatable = function(response) {
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
                    "data": "creationDate"
                },
                {
                    "data": "nama"
                },
                {
                    "data": "cashIn"
                },
                {
                    "data": "cashOut"
                },
                {
                    "data": "tipeNama",
                    render: function(data, type, full, meta) {
                        var tipeNama = '';
                        if (full.idAkun == 1) {
                            tipeNama = '<span class="kt-badge kt-badge--inline kt-badge--danger">' + full.tipeNama + '</span>';
                        } else
                        if (full.idAkun == 2) {
                            tipeNama = '<span class="kt-badge kt-badge--inline kt-badge--success">' + full.tipeNama + '</span>';
                        } else
                        if (full.idAkun == 3) {
                            tipeNama = '<span class="kt-badge kt-badge--inline kt-badge--warning">' + full.tipeNama + '</span>';
                        } else
                        if (full.idAkun == 4) {
                            tipeNama = '<span class="kt-badge kt-badge--inline kt-badge--primary">' + full.tipeNama + '</span>';
                        }
                        return tipeNama;
                    }
                },
                {
                    "data": "budgetNama"
                },
                {
                    target: -1,
                    title: 'Actions',
                    orderable: false,
                    render: function(data, type, full, meta) {
                        return `
									<div class="kt-section__content kt-section__content--solid">
										<button type="button" data-id="` + full.idTransaksi + `" data-status="1" class="btn btn-sm btn-outline-warning btn-edit">Edit</button>
										<button type="button" data-id="` + full.idTransaksi + `" data-status="-1" class="btn btn-sm btn-outline-danger btn-delete">Hapus</button>
									</div>
								`
                    }
                }
            ]

        });
    }

    var generateEditButton = function() {
        $('#kt_table tbody').on('click', '.btn-edit', function(e) {
	
			
            var id = $(this).data('id');
            $.ajax({
                url: '/transaksi/find/' + id,
                type: 'GET',
                contentType: 'application/json',
                success: function(response) {
					
                    if (response.status == 1) {
						
                        $('#kt_modal').modal('toggle');
                        $('#idTransaksi').val(id);
                        $('#nama').val(response.data.nama);
                        $('#idAkun').val(response.data.idAkun);
						loadBudgetDefault(response.data.idBudget);
                        $('#cashIn').val(response.data.cashIn);
                        $('#cashOut').val(response.data.cashOut);
                        $('#creationDate').val(Utils.formatTransaksiAcceptedDate(response.data.creationDate));
                        $('#status').val(response.data.status);

                        $('#btn-save-transaksi').hide();
                        $('#btn-edit-transaksi').show();
                    }


                }
            });

        });
    }

    var generateDeleteButton = function() {
        $('#kt_table tbody').on('click', '.btn-delete', function(e) {
            var id = $(this).data('id');

            swal.fire({
                title: 'Are you sure?',
                text: "You won't be able to revert this!",
                type: 'warning',
                showCancelButton: true,
                confirmButtonText: 'Yes, delete it!'
            }).then(function(result) {
                if (result.value) {
                    $.ajax({
                        url: '/transaksi/delete/' + id,
                        type: 'GET',
                        success: function(response) {
                            if (response.status === 1) {
                                swal.fire({
                                    title: "Good job!",
                                    text: response.message,
                                    type: "success",
                                    buttonsStyling: false,
                                    confirmButtonText: "Confirm me!",
                                    confirmButtonClass: "btn btn-brand"
                                });
                                initDatatable()
                            } else {
                                swal.fire("Errors!", response.message, "error");
                            }

                        }

                    });
                }
            });
        });
    }

    var initDatatable = function() {
        $.ajax({
            url: '/transaksi/list',
            type: 'GET',
            contentType: 'application/json',
            success: function(response) {
                loadDatatable(response);
                generateEditButton();
                generateDeleteButton();
                summaryDatatable(response);
            }
        })
    }

    var searchDatatable = function() {
        $.ajax({
            url: '/transaksi/list/filter',
            type: 'GET',
            data: {
                'start': Utils.formatAcceptedDate($('#creationDateStart').val()),
                'end': Utils.formatAcceptedDate($('#creationDateEnd').val())
            },
            contentType: 'application/json',
            success: function(response) {
                loadDatatable(response);
                generateEditButton();
                generateDeleteButton();
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
    	$.each(datas, function(i, data){
    		if(data.idAkun == 1)
    			cashOut = cashOut + data.cashOut - data.cashIn;
    		else
    		if(data.idAkun == 2)
    			cashIn = cashIn + data.cashIn - data.cashOut;
    		else
    		if(data.idAkun == 3)
	    		hutang = hutang + data.cashOut - data.cashIn;
    		else
    		if(data.idAkun == 4)
    			piutang = piutang + data.cashIn - data.cashOut;
    			
    	});
    	var total = cashIn + cashOut + piutang + hutang;
    	
    	$('#percent-cashin').text(Math.ceil(cashIn/total*100));
    	$('#percent-cashout').text(Math.ceil(cashOut/total*100));
    	$('#percent-piutang').text(Math.ceil(piutang/total*100));
    	$('#percent-hutang').text(Math.ceil(hutang/total*100));
    	
    	$('#progressbar-cashin').css({"width": ""+Math.ceil(cashIn/total*100)+"%"});
    	$('#progressbar-cashout').css({"width": ""+Math.ceil(cashOut/total*100)+"%"});
    	$('#progressbar-piutang').css({"width": ""+Math.ceil(piutang/total*100)+"%"});
    	$('#progressbar-hutang').css({"width": ""+Math.ceil(hutang/total*100)+"%"});
    	
    	
    	$('#header-cashin').text(Utils.currencyFormat(cashIn/1000000)+"J");
    	$('#header-cashout').text(Utils.currencyFormat(cashOut/1000000)+"J");
    	$('#header-piutang').text(Utils.currencyFormat(piutang/1000000)+"J");
    	$('#header-hutang').text(Utils.currencyFormat(hutang/1000000)+"J");
    	
    	$('#detail-cashin').text(Utils.currencyFormat(cashIn));
    	$('#detail-cashout').text(Utils.currencyFormat(cashOut));
    	$('#detail-piutang').text(Utils.currencyFormat(piutang));
    	$('#detail-hutang').text(Utils.currencyFormat(hutang));
    	
    }

    var buttonHandler = function() {

        $('#btn-search').on('click', function() {
            searchDatatable();
        });

        $('#btn-new-record').on('click', function() {
            $('#idTransaksi').val('');
            $('#nama').val('');

            $('#cashIn').val('0');
            $('#cashOut').val('0');
            $('#idAkun').val('');
            $('#idBudget').val('')
            $('#status').val('1');

            $('#btn-save-transaksi').show();
            $('#btn-edit-transaksi').hide();
        })

        $('#btn-save-transaksi').on('click', function() {

            var nama = $('#nama').val();
            var creationDate = $('#creationDate').val();
            var idAkun = $('#idAkun').val();
            var idBudget = $('#idBudget').val();
            var cashIn = Utils.convertToNumber($('#cashIn').val());
            var cashOut = Utils.convertToNumber($('#cashOut').val());
            var status = $('#status').val();

            var data = {
                "nama": nama,
                "creationDate": creationDate,
                "idAkun": idAkun,
                "idBudget": idBudget,
                "cashIn": cashIn,
                "cashOut": cashOut,
                "status": status,
            }

            $.ajax({
                url: '/transaksi/add',
                type: 'POST',
                contentType: 'application/json',
                data: JSON.stringify(data),
                success: function(response) {
                    if (response.status === 1) {
                        swal.fire({
                            title: "Success!",
                            text: response.message,
                            type: "success",
                            buttonsStyling: false,
                            confirmButtonText: "Confirm me!",
                            confirmButtonClass: "btn btn-brand"
                        });
                        initDatatable();
                    } else {
                        swal.fire("Errors!", response.message, "error");
                    }

                }

            })
        });
        
        $('#idAkun').on('change', function(){
        	if($(this).val() == 1 || $(this).val() == 3) {
        		$('#cashIn').val(0);
        		$('#cashIn').prop('disabled', true);
        		
        		$('#cashOut').val(0);
        		$('#cashOut').prop('disabled', false);
        	} else
        	if($(this).val() == 2 || $(this).val() == 4) {
        		$('#cashIn').val(0);
        		$('#cashIn').prop('disabled', false);
        		
        		$('#cashOut').val(0);
        		$('#cashOut').prop('disabled', true);
        	}
        	
        	loadBudget();
        });

        $('#btn-edit-transaksi').on('click', function() {
            var id = $('#idTransaksi').val();
            var nama = $('#nama').val();
            var creationDate = $('#creationDate').val();
            var idAkun = $('#idAkun').val();
            var idBudget = $('#idBudget').val();
            var cashIn = Utils.convertToNumber($('#cashIn').val());
            var cashOut = Utils.convertToNumber($('#cashOut').val());
            var status = $('#status').val();

            var data = {
                "nama": nama,
                "creationDate": creationDate,
                "idAkun": idAkun,
                "idBudget": idBudget,
                "cashIn": cashIn,
                "cashOut": cashOut,
                "status": status,
            }

            $.ajax({
                url: '/transaksi/edit/' + id,
                type: 'POST',
                contentType: 'application/json',
                data: JSON.stringify(data),
                success: function(response) {
                    if (response.status === 1) {
                        swal.fire({
                            title: "Good job!",
                            text: response.message,
                            type: "success",
                            buttonsStyling: false,
                            confirmButtonText: "Confirm me!",
                            confirmButtonClass: "btn btn-brand"
                        });
                        initDatatable()
                    } else {
                        swal.fire("Errors!", response.message, "error");
                    }

                }

            })
        });



    }

    var sayHello = function() {
        console.log('Hello this is budget page');
    }

    var initDatepicker = function() {
        var arrows;
        if (KTUtil.isRTL()) {
            arrows = {
                leftArrow: '<i class="la la-angle-right"></i>',
                rightArrow: '<i class="la la-angle-left"></i>'
            }
        } else {
            arrows = {
                leftArrow: '<i class="la la-angle-left"></i>',
                rightArrow: '<i class="la la-angle-right"></i>'
            }
        }
		
		var start = new Date();
		start.setDate(1);
		
		var end = new Date();
		end.setDate(1);
		end.setMonth(end.getMonth() + 1);
		end.setDate(end.getDate() - 1);

        $('#creationDate').datepicker({
            rtl: KTUtil.isRTL(),
            todayHighlight: true,
            orientation: "bottom left",
            templates: arrows,
            format: 'dd-mm-yyyy',
            autoclose: true
        }).datepicker('setDate', new Date());

        $('#creationDateStart').datepicker({
            rtl: KTUtil.isRTL(),
            todayHighlight: true,
            orientation: "bottom left",
            templates: arrows,
            format: 'dd-mm-yyyy',
            autoclose: true
        }).datepicker('setDate', new Date(start));

        $('#creationDateEnd').datepicker({
            rtl: KTUtil.isRTL(),
            todayHighlight: true,
            orientation: "bottom left",
            templates: arrows,
            format: 'dd-mm-yyyy',
            autoclose: true
        }).datepicker('setDate', new Date(end));
    }

    var initAkun = function() {
        $.ajax({
            url: '/akun/list',
            type: 'GET',
            contentType: 'application/json',
            success: function(response) {
                var items = response.data;

                $.each(items, function(i, item) {
                    var o = new Option(item.tipeNama, item.idAkun);
                    $(o).html(item.tipeNama);
                    $('#idAkun').append(o);
                });
            }
        });
    }

    var loadBudget = function() {
        $.ajax({
            url: '/budget/list',
            type: 'GET',
            contentType: 'application/json',
            success: function(response) {
                var items = response.data;
				$('#idBudget').empty();
				
				var o = new Option("Pilih Budget", "");
				$(o).html("Pilih Budget");
				$('#idBudget').append(o);
				
                $.each(items, function(i, item) {
                    var o = new Option(item.nama, item.idBudget);
                    $(o).html(item.nama);
                    $('#idBudget').append(o);
                });
            }
        });
    }
    
    var loadBudgetDefault = function(idBudget) {
        $.ajax({
            url: '/budget/list',
            type: 'GET',
            contentType: 'application/json',
            success: function(response) {
                var items = response.data;
				$('#idBudget').empty();
				
				var o = new Option("Pilih Budget", "");
				$(o).html("Pilih Budget");
				$('#idBudget').append(o);
				
                $.each(items, function(i, item) {
                    var o = new Option(item.nama, item.idBudget);
                    $(o).html(item.nama);
                    $('#idBudget').append(o);
                });
                
                $('#idBudget').val(idBudget);
            }
        });
    }

    return {
        init: function() {
            sayHello();
            initDatatable();
            buttonHandler();
            initAkun();
            initDatepicker();
        }
    }

}();


jQuery(document).ready(function() {
    transaksi.init();
});