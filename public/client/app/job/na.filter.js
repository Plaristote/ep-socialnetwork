function NaFilter (){
	return function (input){

		if(input)
			return input;

		return 'N/A';
	}
}

angular.module('app').filter('na', NaFilter);