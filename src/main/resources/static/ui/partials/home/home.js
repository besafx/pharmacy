app.controller("homeCtrl", ['$scope', '$rootScope', '$timeout',
    function ($scope, $rootScope, $timeout) {
        $timeout(function () {
            window.componentHandler.upgradeAllRegistered();
        }, 1500);
    }]);
