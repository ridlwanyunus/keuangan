/**
 * 
 */
 "use strict";

var budget = function() {
	
	var initDatatable = function(){
		$.ajax({
			url: '/budget/list',
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
							"data": "nama"
						},
						{
							"data": "idAkun"
						},
						{
							"data": "biaya",
							render: function(data, meta){
								return `<span class="number-selector">`+data+`</span>`
							}
						},
						{
							"data": "capaian"
						},
						{
							"data": "selisih"
						},
						{
							target: -1,
							title: 'Actions',
							orderable: false,
							render: function(data, type, full, meta){
								return `
									<div class="kt-section__content kt-section__content--solid">
										<button type="button" data-id="`+full.idBudget+`" data-status="1" class="btn btn-sm btn-outline-warning btn-edit">Edit</button>
										<button type="button" data-id="`+full.idBudget+`" data-status="-1" class="btn btn-sm btn-outline-danger btn-delete">Hapus</button>
									</div>
								`
							}
						}
					]
					
				});
				
				$('.btn-edit').on('click', function(e){
					var id = $(this).data('id');
					
					$.ajax({
						url: '/budget/find/'+id,
						type: 'GET',
						contentType: 'application/json',
						success: function(response){
							
							if(response.status == 1){
								$('#kt_modal').modal('toggle');
								$('#idBudget').val(id);
								$('#nama').val(response.data.nama);
								$('#idAkun').val(response.data.idAkun);
								$('#biaya').val(response.data.biaya);
								$('#capaian').val(response.data.capaian);
								$('#status').val(response.data.status);
								
								$('#btn-save-budget').hide();
								$('#btn-edit-budget').show();
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
								url: '/budget/delete/'+id,
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
			$('#idBudget').val('');
			$('#nama').val('');
			$('#idAkun').val('');
			$('#biaya').val('0');
			$('#capaian').val('0');
			$('#status').val('1');
			
			$('#btn-save-budget').show();
			$('#btn-edit-budget').hide();
		})
		
		$('#btn-save-budget').on('click', function(){
			
			var nama = $('#nama').val();
			var idAkun = $('#idAkun').val();
			var biaya = Utils.convertToNumber($('#biaya').val());
			var capaian = Utils.convertToNumber($('#capaian').val());
			var status = $('#status').val();
			
			var data = {
				"nama": nama,
				"idAkun": idAkun,
				"biaya": biaya,
				"capaian": capaian,
				"status": status,
			}
			
			$.ajax({
				url: '/budget/add',
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
		
		$('#btn-edit-budget').on('click', function() {
			var id = $('#idBudget').val();
			var nama = $('#nama').val();
			var idAkun = $('#idAkun').val();
			var biaya = Utils.convertToNumber($('#biaya').val());
			var capaian = Utils.convertToNumber($('#capaian').val());
			var status = $('#status').val();
			
			var data = {
				"nama": nama,
				"idAkun": idAkun,
				"biaya": biaya,
				"capaian": capaian,
				"status": status,
			}

			$.ajax({
				url: '/budget/edit/' + id,
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
	
	var initAkun = function(){
		$.ajax({
			url: '/akun/list',
			type: 'GET',
			contentType: 'application/json',
			success: function(response){
				var items = response.data;
				
				$.each(items, function(i, item){
					console.log(item)
					var o = new Option(item.nama, item.idAkun);
					$(o).html(item.nama);
					$('#idAkun').append(o);
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
		}
	}
	
}();


jQuery(document).ready(function(){
	budget.init();
});