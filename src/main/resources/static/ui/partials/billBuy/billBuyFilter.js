app.controller('billBuyFilterCtrl', ['SupplierService', '$scope', '$rootScope', '$timeout', '$log', '$uibModalInstance',
    function (SupplierService, $scope, $rootScope, $timeout, $log, $uibModalInstance) {

        $scope.buffer = {};

        $timeout(function () {
            SupplierService.findAllCombo().then(function (data) {
                $scope.suppliers = data;
            });
        }, 2000);

        $scope.submit = function () {
            $uibModalInstance.close($scope.buffer);
        };

        $scope.cancel = function () {
            $uibModalInstance.dismiss('cancel');
        };

        $timeout(function () {
            window.componentHandler.upgradeAllRegistered();
        }, 1500);

    }]);