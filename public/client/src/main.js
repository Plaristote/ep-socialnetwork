
var app = angular.module('koreanMeetUpApp', [
  'ngRoute',
  'ui.calendar',
  'ngResource'  
]);

var userFactory = function($resource){
	return $resource('/api/users/:slug', { slug : '@slug' }, 
		{
			'update' : { method:'PUT' },
		});
};
app.factory('User', [ '$resource', userFactory ]);



/**
 * Configure the Routes
 */
app.config(['$routeProvider', function ($routeProvider) {
  $routeProvider
    // Home
    .when("/", {templateUrl: "../view/home.html", controller: "PageCtrl"})
    // Pages
    .when("/login", {templateUrl: "../view/accountLogin.html", controller: "PageCtrl"})
    .when("/accountCreate", {templateUrl: "../view/accountCreate.html", controller: "PageCtrl"})
    .when("/eventDisplay/:title/:desc/:start/:end/:id", {templateUrl: "../view/eventDisplay.html", controller: "PageCtrl"})
    
    // else 404
    .otherwise("/404", {templateUrl: "partials/404.html", controller: "PageCtrl"});
}]);

/**
 * Controls the Blog
 */
app.controller('BlogCtrl', function ($scope, $resource, $http /* $scope, $location, $http */) {
    var User = $resource('/api/users/:userId', {userId:'@id'});
    //var User = $http.get('http://localhost:9000/api/users/');
    $scope.users = User.query();
    
    $scope.reset = function () {
        alert('reset');
    };
    
    //{"first_name":"Rama","last_name":"Diouf","email":"rama@ramadiouf.com","gender":"female"}
    $scope.update = function (dataUser) {
        console.log($scope.users);
        var user = new User();
        user.first_name = dataUser.name;
        user.last_name = dataUser.lastname;
        user.email = dataUser.email;
        user.gender = dataUser.gender;
        user.$save();
        //$scope.users.push(user);

    };
});

/**
 * Controls all other Pages
 */
app.controller('PageCtrl', function (/* $scope, $location, $http */) {
  console.log("Page Controller reporting for duty.");

  // Activates the Carousel
  $('.carousel').carousel({
    interval: 5000
  });

  // Activates Tooltips for Social Links
  $('.tooltip-social').tooltip({
    selector: "a[data-toggle=tooltip]"
  });
});