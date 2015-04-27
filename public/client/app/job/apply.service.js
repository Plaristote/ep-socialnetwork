/* @ngInject */
function ApplyService($resource) {
    return $resource('/api/applies/:applyId');
}

angular.module('app').factory('Apply', ApplyService);