/**
 * 
 */
 "use strict";

var transaksi = function() {
	
	var initDatatable = function(){
		$.ajax({
			url: '/transaksi/list',
			type: 'GET',
			contentType: 'application/json',
			success: function(response){
				var table = $('#kt_table');
				
				table.DataTable({
					data: response.data,
					destroy: true,
					scrollY: '50vh',
					scrollX: true,
					scrollCollapse: true,
					ordering: false,
					columns: [
						{
							"data": null,
							render: function(data, type, full, meta){
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
							render: function(data, type, full, meta){
								var tipeNama = '';
								if(full.idAkun == 1){
									tipeNama = '<span class="kt-badge kt-badge--inline kt-badge--danger">'+full.tipeNama+'</span>';
								} else 
								if(full.idAkun == 2){
									tipeNama = '<span class="kt-badge kt-badge--inline kt-badge--success">'+full.tipeNama+'</span>';
								} else 
								if(full.idAkun == 3){
									tipeNama = '<span class="kt-badge kt-badge--inline kt-badge--warning">'+full.tipeNama+'</span>';
								} else 
								if(full.idAkun == 4){
									tipeNama = '<span class="kt-badge kt-badge--inline kt-badge--primary">'+full.tipeNama+'</span>';
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
							render: function(data, type, full, meta){
								return `
									<div class="kt-section__content kt-section__content--solid">
										<button type="button" data-id="`+full.idTransaksi+`" data-status="1" class="btn btn-sm btn-outline-warning btn-edit">Edit</button>
										<button type="button" data-id="`+full.idTransaksi+`" data-status="-1" class="btn btn-sm btn-outline-danger btn-delete">Hapus</button>
									</div>
								`
							}
						}
					]
					
				});
				
				$('.btn-edit').on('click', function(e){
					var id = $(this).data('id');
					
					$.ajax({
						url: '/transaksi/find/'+id,
						type: 'GET',
						contentType: 'application/json',
						success: function(response){
							
							if(response.status == 1){
								$('#kt_modal').modal('toggle');
								$('#idTransaksi').val(id);
								$('#nama').val(response.data.nama);
								$('#idAkun').val(response.data.idAkun);
								$('#idBudget').val(response.data.idBudget);
								$('#cashIn').val(response.data.cashIn);
								$('#cashOut').val(response.data.cashOut);
								$('#creationDate').val(response.data.creationDate);
								$('#status').val(response.data.status);
								
								$('#btn-save-transaksi').hide();
								$('#btn-edit-transaksi').show();
							}
							
							
						}
					});

				});
				
				$('.btn-delete').on('click', function(e){
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
								url: '/transaksi/delete/'+id,
								type: 'GET',
								success: function(response){
									if(response.status === 1){
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
		})
	}
	
	var buttonHandler = function(){
		
		$('#btn-new-record').on('click', function(){
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
		
		$('#btn-save-transaksi').on('click', function(){
			
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
				success: function(response){
					if(response.status === 1){
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
	
	var sayHello = function(){
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
        }).datepicker('setDate', new Date());
        
        $('#creationDateEnd').datepicker({
            rtl: KTUtil.isRTL(),
            todayHighlight: true,
            orientation: "bottom left",
            templates: arrows,
            format: 'dd-mm-yyyy',
            autoclose: true
        }).datepicker('setDate', new Date());
	}
	
	var initAkun = function(){
		$.ajax({
			url: '/akun/list',
			type: 'GET',
			contentType: 'application/json',
			success: function(response){
				var items = response.data;
				
				$.each(items, function(i, item){
					var o = new Option(item.tipeNama, item.idAkun);
					$(o).html(item.tipeNama);
					$('#idAkun').append(o);
				});
			}
		});
	}
	
	var initBudget = function(){
		$.ajax({
			url: '/budget/list',
			type: 'GET',
			contentType: 'application/json',
			success: function(response){
				var items = response.data;
				
				$.each(items, function(i, item){
					var o = new Option(item.nama, item.idBudget);
					$(o).html(item.nama);
					$('#idBudget').append(o);
				});
			}
		});
	}
	
	return {
		init: function(){
			sayHello();
			initDatatable();
			buttonHandler();
			initAkun();
			initBudget();
			initDatepicker();
		}
	}
	
}();


jQuery(document).ready(function(){
	transaksi.init();
});