app.controller('billBuyHeadCreateCtrl', ['DrugUnitService', 'SupplierService', 'ModalProvider', 'BillBuyService', '$scope', '$rootScope', '$timeout', '$log', '$uibModalInstance',
    function (DrugUnitService, SupplierService, ModalProvider, BillBuyService, $scope, $rootScope, $timeout, $log, $uibModalInstance) {

        $timeout(function () {
            $scope.refreshSuppliers();
        }, 2000);

        $scope.newSupplier = function () {
            ModalProvider.openSupplierCreateModel().result.then(function (data) {
                $scope.suppliers.splice(0, 0, data);
            }, function () {
                console.info('SupplierCreateModel Closed.');
            });
        };

        $scope.refreshSuppliers = function () {
            SupplierService.findAllCombo().then(function (data) {
                $scope.suppliers = data;
            });
        };

        $scope.submit = function () {
            BillBuyService.create($scope.billBuy).then(function (data) {
                $uibModalInstance.close(data);
            });
        };

        $scope.cancel = function () {
            $uibModalInstance.dismiss('cancel');
        };

    }]);