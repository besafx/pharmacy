app.controller('drugTransactionBuyCreateCtrl', ['DrugService', 'DrugUnitService', 'BillBuyService', 'TransactionBuyService', '$scope', '$rootScope', '$timeout', '$log', '$uibModalInstance', 'drug', 'ModalProvider',
    function (DrugService, DrugUnitService, BillBuyService, TransactionBuyService, $scope, $rootScope, $timeout, $log, $uibModalInstance, drug, ModalProvider) {

        $timeout(function () {
            $scope.refreshBillBuys();
            DrugUnitService.findAll().then(function (data) {
                $scope.drugUnits = data;
            })
        }, 2000);

        $scope.transactionBuy = {};

        $scope.transactionBuy.drug = drug;

        $scope.refreshBillBuys = function () {
            BillBuyService.findAllCombo().then(function (data) {
                $scope.billBuys = data;
            });
        };

        $scope.newBillBuy = function () {
            ModalProvider.openBillBuyHeadCreateModel().result.then(function (data) {
                if ($scope.billBuys) {
                    $scope.billBuys.splice(0, 0, data);
                }
            }, function () {
                console.info('BillBuyHeadCreateModel Closed.');
            });
        };

        $scope.submit = function () {
            TransactionBuyService.create($scope.transactionBuy).then(function (data) {
                $uibModalInstance.close(data);
            });
        };

        $scope.cancel = function () {
            $uibModalInstance.dismiss('cancel');
        };

    }]);