

/* @ngInject */
function JobService($resource) {
    return $resource('/api/jobs/:jobId');
}

angular.module('app').factory('Job', JobService);