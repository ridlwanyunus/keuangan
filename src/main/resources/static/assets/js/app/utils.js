var Utils = function(){
	
	var convertToNumber = function(value){
		return value.replace(/\D/g, '');
	}
	
	var formatAcceptedDate = function(value){
		var arr = value.split("-");
		return arr[2] + "-" + arr[1] + "-" + arr[0];
	}
	
	var formatTransaksiAcceptedDate = function(value){
		value = value.substring(0,10);
		console.log(value);
		var arr = value.split("-");
		return arr[2] + "-" + arr[1] + "-" + arr[0];
	}
	
	
	var currencyFormat = function(value) {
		var xx = new Intl.NumberFormat('id-ID', {
		  style: 'currency',
		  currency: 'IDR',
		  minimumFractionDigits: 0,
		  maximumFractionDigits: 2
		});
		return xx.format(value)
	}
	
	return {
		convertToNumber: function(value){
			return convertToNumber(value);
		},
		formatAcceptedDate: function(value){
			return formatAcceptedDate(value);
		},
		currencyFormat: function(value){
			return currencyFormat(value);
		},
		formatTransaksiAcceptedDate: function(value){
			return formatTransaksiAcceptedDate(value);
		}
	};
}();