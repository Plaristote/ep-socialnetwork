/* @ngInject */
function JobListCtrl($scope, jobList) {
	console.log(jobList)
    $scope.jobs = jobList;
}

angular.module('app').controller('JobListCtrl', JobListCtrl);
