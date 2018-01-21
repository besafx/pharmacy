app.controller('outsideSalesCreateAddItemCtrl', ['DrugUnitService', 'TransactionBuyService', '$scope', '$rootScope', '$timeout', '$log', '$uibModalInstance',
    function (DrugUnitService, TransactionBuyService, $scope, $rootScope, $timeout, $log, $uibModalInstance) {

        $scope.transactionSell = {};

        $scope.transactionBuyCalculation = function () {
            DrugUnitService.getRelatedPrices($scope.transactionSell.transactionBuy.id).then(function (data) {
                $scope.relatedPrices = data;
            });
        };

        $scope.refreshTransactionBuys = function () {
            TransactionBuyService.findByDrug($scope.transactionSell.drug.id).then(function (data) {
                $scope.transactionSell.drug.transactionBuys = data
            });
        };

        $scope.submit = function () {
            $uibModalInstance.close($scope.transactionSell);
        };

        $scope.cancel = function () {
            $uibModalInstance.dismiss('cancel');
        };

        $timeout(function () {
            window.componentHandler.upgradeAllRegistered();
        }, 1500);

    }]);