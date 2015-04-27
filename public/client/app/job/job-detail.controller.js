function JobDetailController($scope, Apply, job) {
    $scope.job = job;

    $scope.doApply = function (){

    	var applyObject = new Apply($scope.apply);
    	applyObject.job = job._id;
    	applyObject.$save();
    }

}

angular.module('app').controller('JobDetailCtrl', JobDetailController);
