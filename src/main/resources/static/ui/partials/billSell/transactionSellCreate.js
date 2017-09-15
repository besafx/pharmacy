app.controller('transactionSellCreateCtrl', ['DrugService', 'DrugUnitService', 'BillSellService', 'TransactionSellService', '$scope', '$rootScope', '$timeout', '$log', '$uibModalInstance', 'title', 'billSell',
    function (DrugService, DrugUnitService, BillSellService, TransactionSellService, $scope, $rootScope, $timeout, $log, $uibModalInstance, title, billSell) {

        $timeout(function () {
            $scope.refreshDrugs();
            DrugUnitService.findAll().then(function (data) {
                $scope.drugUnits = data;
            })
        }, 2000);

        $scope.transactionSell = {};

        $scope.transactionSell.billSell = billSell;

        $scope.buffer = {};

        $scope.title = title;

        $scope.refreshBillSell = function () {
            BillSellService.findOne(billSell.id).then(function (data) {
                $scope.transactionSell.billSell = data;
            });
        };

        $scope.refreshDrugs = function () {
            DrugService.findAllCombo().then(function (data) {
                $scope.drugs = data;
            });
        };

        $scope.submit = function () {
            TransactionSellService.create($scope.transactionSell).then(function (data) {
                $uibModalInstance.close(data);
            });
        };

        $scope.cancel = function () {
            $uibModalInstance.dismiss('cancel');
        };

    }]);