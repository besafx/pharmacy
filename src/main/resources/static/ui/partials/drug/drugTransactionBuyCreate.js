app.controller('drugTransactionBuyCreateCtrl', ['DrugService', 'DrugUnitService', 'BillBuyService', 'TransactionBuyService', '$scope', '$rootScope', '$timeout', '$log', '$uibModalInstance', 'drug', 'ModalProvider',
    function (DrugService, DrugUnitService, BillBuyService, TransactionBuyService, $scope, $rootScope, $timeout, $log, $uibModalInstance, drug, ModalProvider) {

        $timeout(function () {
            BillBuyService.findAllCombo().then(function (data) {
                $scope.billBuys = data;
            });
            DrugUnitService.findByDrug(drug.id).then(function (data) {
                $scope.drugUnits = data;
            })
        }, 600);

        $scope.transactionBuy = {};

        $scope.transactionBuy.drug = drug;

        $scope.submit = function () {
            TransactionBuyService.create($scope.transactionBuy).then(function (data) {
                $uibModalInstance.close(data);
            });
        };

        $scope.cancel = function () {
            $uibModalInstance.dismiss('cancel');
        };

    }]);