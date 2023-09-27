/**
 * 
 */
 "use strict";

var akun = function() {
	
	var initDatatable = function(){
		$.ajax({
			url: '/akun/list',
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
							"data": null,
							render: function(data, type, full, meta){
								var tipeNama = '';
								if(full.tipeAkun == 1){
									tipeNama = '<span class="kt-badge kt-badge--inline kt-badge--danger">'+full.tipeNama+'</span>';
								} else 
								if(full.tipeAkun == 2){
									tipeNama = '<span class="kt-badge kt-badge--inline kt-badge--success">'+full.tipeNama+'</span>';
								} else 
								if(full.tipeAkun == 3){
									tipeNama = '<span class="kt-badge kt-badge--inline kt-badge--warning">'+full.tipeNama+'</span>';
								} else 
								if(full.tipeAkun == 4){
									tipeNama = '<span class="kt-badge kt-badge--inline kt-badge--primary">'+full.tipeNama+'</span>';
								}
								return tipeNama;
							}
						},
						{
							"data": "creationDate"
						},
						{
							target: -1,
							title: 'Actions',
							orderable: false,
							render: function(data, type, full, meta){
								return `
									<div class="kt-section__content kt-section__content--solid">
										<button type="button" data-id="`+full.idAkun+`" data-status="1" class="btn btn-sm btn-outline-warning btn-edit">Edit</button>
										<button type="button" data-id="`+full.idAkun+`" data-status="-1" class="btn btn-sm btn-outline-danger btn-delete">Hapus</button>
									</div>
								`
							}
						}
					]
					
				});
				
				$('.btn-edit').on('click', function(e){
					var id = $(this).data('id');
					
					$.ajax({
						url: '/akun/find/'+id,
						type: 'GET',
						contentType: 'application/json',
						success: function(response){
							
							if(response.status == 1){
								$('#kt_modal').modal('toggle');
								$('#idAkun').val(id);
								$('#nama').val(response.data.nama);
								$('#tipeAkun').val(response.data.tipeAkun);
								$('#status').val(response.data.status);
								
								$('#btn-save-akun').hide();
								$('#btn-edit-akun').show();
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
								url: '/akun/delete/'+id,
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
			$('#idAkun').val('');
			$('#nama').val('');
			$('#tipeAkun').val('0');
			$('#status').val('1');
			
			$('#btn-save-akun').show();
			$('#btn-edit-akun').hide();
		})
		
		$('#btn-save-akun').on('click', function(){
			
			var nama = $('#nama').val();
			var tipeAkun = $('#tipeAkun').val();
			var status = $('#status').val();
			
			var data = {
				"nama": nama,
				"tipeAkun": tipeAkun,
				"status": status
			}
			
			$.ajax({
				url: '/akun/add',
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
			            initDatatable()
					} else {
						swal.fire("Errors!", response.message, "error");
					}
					
				}
				
			})
		});
		
		$('#btn-edit-akun').on('click', function() {
			var id = $('#idAkun').val();
			var nama = $('#nama').val();
			var tipeAkun = $('#tipeAkun').val();
			var status = $('#status').val();
			
			var data = {
				"nama": nama,
				"tipeAkun": tipeAkun,
				"status": status
			}

			$.ajax({
				url: '/akun/edit/' + id,
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
		console.log('Hello this is akun page');
	}
	
	
	
	return {
		init: function(){
			sayHello();
			initDatatable();
			buttonHandler();
		}
	}
	
}();


jQuery(document).ready(function(){
	akun.init();
});