app.controller("menuCtrl", ['$scope', '$rootScope', '$state', '$timeout', function ($scope, $rootScope, $state, $timeout) {
    $timeout(function () {
        window.componentHandler.upgradeAllRegistered();
    }, 1500);
}]);