var Utils = function(){
	
	var convertToNumber = function(value){
		return value.replace(/\D/g, '');
	}
	
	return {
		convertToNumber: function(value){
			return convertToNumber(value);
		}
	};
}();