app.controller('updatePricesCtrl', ['DrugUnitService', 'TransactionBuyService', '$scope', '$rootScope', '$timeout', '$log', '$uibModalInstance', 'transactionBuy',
    function (DrugUnitService, TransactionBuyService,$scope, $rootScope, $timeout, $log, $uibModalInstance, transactionBuy) {

        $timeout(function () {
            DrugUnitService.findByDrug(transactionBuy.drug.id).then(function (data) {
                $scope.drugUnits = data;
            })
        }, 600);

        $scope.transactionBuy = transactionBuy;

        $scope.submit = function () {
            TransactionBuyService.updatePrices($scope.transactionBuy.id, $scope.transactionBuy.drugUnit.id, $scope.transactionBuy.unitBuyCost, $scope.transactionBuy.unitSellCost).then(function (data) {
                $uibModalInstance.close(data);
            });
        };

        $scope.cancel = function () {
            $uibModalInstance.dismiss('cancel');
        };

    }]);