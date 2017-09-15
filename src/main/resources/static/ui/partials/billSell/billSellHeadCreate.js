app.controller('billSellHeadCreateCtrl', ['DrugUnitService', 'CustomerService', 'ModalProvider', 'BillSellService', '$scope', '$rootScope', '$timeout', '$log', '$uibModalInstance',
    function (DrugUnitService, CustomerService, ModalProvider, BillSellService, $scope, $rootScope, $timeout, $log, $uibModalInstance) {

        $timeout(function () {
            $scope.refreshCustomers();
        }, 2000);

        $scope.newCustomer = function () {
            ModalProvider.openCustomerCreateModel().result.then(function (data) {
                $scope.customers.splice(0, 0, data);
            }, function () {
                console.info('CustomerCreateModel Closed.');
            });
        };

        $scope.refreshCustomers = function () {
            CustomerService.findAllCombo().then(function (data) {
                $scope.customers = data;
            });
        };

        $scope.submit = function () {
            BillSellService.create($scope.billSell).then(function (data) {
                $uibModalInstance.close(data);
            });
        };

        $scope.cancel = function () {
            $uibModalInstance.dismiss('cancel');
        };

    }]);