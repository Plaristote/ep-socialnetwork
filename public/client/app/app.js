angular.module('app', ['ngRoute', 'ngResource']);


function RouteConfig($routeProvider) {
    $routeProvider
    // Home
    .when("/", {templateUrl: "../view/home.html", controller: "PageCtrl"})
    
    $routeProvider.when('/jobs', {
        templateUrl: 'app/job/job-list.html',
        controller: 'JobListCtrl',
        resolve: {
            jobList: function(Job) {
                return Job.query().$promise;
            }

        }
    });

    $routeProvider.when('/jobs/:jobId', {
        templateUrl: 'app/job/job-detail.html',
        controller: 'JobDetailCtrl',
        resolve: {
            job: function($route, Job) {
                return Job.get({
                    jobId: $route.current.params.jobId
                }).$promise;
            }
        }
    });



    $routeProvider.otherwise('/jobs');

}

angular.module('app').config(RouteConfig);
