app.controller('insideSalesFilterCtrl', ['CustomerService', '$scope', '$rootScope', '$timeout', '$log', '$uibModalInstance', 'title',
    function (CustomerService, $scope, $rootScope, $timeout, $log, $uibModalInstance, title) {

        $scope.buffer = {};

        $scope.title = title;

        $scope.submit = function () {
            $uibModalInstance.close($scope.paramInsideSales);
        };

        $scope.cancel = function () {
            $uibModalInstance.dismiss('cancel');
        };

        $timeout(function () {
            window.componentHandler.upgradeAllRegistered();
        }, 1500);

    }]);