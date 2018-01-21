app.controller('billBuyCreateAddItemCtrl', ['SupplierService', '$scope', '$rootScope', '$timeout', '$log', '$uibModalInstance',
    function (SupplierService, $scope, $rootScope, $timeout, $log, $uibModalInstance) {

        $scope.transactionBuy = {};

        $scope.submit = function () {
            $uibModalInstance.close($scope.transactionBuy);
        };

        $scope.cancel = function () {
            $uibModalInstance.dismiss('cancel');
        };

        $timeout(function () {
            window.componentHandler.upgradeAllRegistered();
        }, 1500);

    }]);