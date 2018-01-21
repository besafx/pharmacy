app.controller('outsideSalesCreateCtrl', [
    'TransactionBuyService',
    'DrugService',
    'DrugUnitService',
    'CustomerService',
    'ModalProvider',
    'BillSellService',
    '$scope',
    '$rootScope',
    '$timeout',
    '$log',
    '$uibModalInstance',
    '$uibModal',
    'title',
    'billSell',
    function (
        TransactionBuyService,
        DrugService,
        DrugUnitService,
        CustomerService,
        ModalProvider,
        BillSellService,
        $scope,
        $rootScope,
        $timeout,
        $log,
        $uibModalInstance,
        $uibModal,
        title,
        billSell) {

        $timeout(function () {
            $scope.refreshDrugs();
        }, 2000);

        $scope.billSell = billSell;

        $scope.buffer = {};

        $scope.transactionSellList = [];

        $scope.title = title;

        $scope.receipt = {};

        $scope.totalCost = 0;

        $scope.refreshDrugs = function () {
            DrugService.findAllCombo().then(function (data) {
                $scope.drugs = data;
            });
        };

        $scope.calculateCostSum = function () {
            $scope.totalCost = 0;
            $scope.totalCostAfterDiscount = 0;
            if ($scope.transactionSellList) {
                for (var i = 0; i < $scope.transactionSellList.length; i++) {
                    var transactionSell = $scope.transactionSellList[i];
                    $scope.totalCost = $scope.totalCost + (transactionSell.unitSellCost * transactionSell.quantity);
                }
                $scope.totalCostAfterDiscount = $scope.totalCost - (($scope.totalCost * $scope.billSell.discount) / 100);
            }
        };

        $scope.openDrugChooser = function () {
            var modalInstance = $uibModal.open({
                animation: true,
                ariaLabelledBy: 'modal-title',
                ariaDescribedBy: 'modal-body',
                templateUrl: '/ui/partials/billSell/outsideSalesCreateAddItem.html',
                controller: 'outsideSalesCreateAddItemCtrl',
                scope: $scope,
                backdrop: 'static',
                keyboard: false
            });

            modalInstance.result.then(function (transactionSell) {
                transactionSell.drugUnit = $scope.buffer.related.obj1;
                transactionSell.unitSellCost = $scope.buffer.related.obj3;
                $scope.transactionSellList.push(transactionSell);
                $scope.buffer = {};
                $scope.relatedPrices = {};
                $scope.calculateCostSum();
            }, function () {
            });
        };

        $scope.removeTransactionSellFromList = function (index) {
            $scope.transactionSellList.splice(index, 1);
            $scope.calculateCostSum();
        };

        $scope.submit = function () {
            $scope.billSell.transactionSells = $scope.transactionSellList;
            //Ignore Creating Receipt In case Of Selecting Later Options For Payment Method
            if($scope.billSell.receipt.paymentMethod!=='Later'){
                var billSellReceipts = [];
                var billSellReceipt = {};
                billSellReceipt.receipt = $scope.billSell.receipt;
                billSellReceipts.push(billSellReceipt);
                $scope.billSell.billSellReceipts = billSellReceipts;
            }
            BillSellService.create($scope.billSell).then(function (data) {
                $uibModalInstance.close(data);
            });
        };

        $scope.cancel = function () {
            $uibModalInstance.dismiss('cancel');
        };

    }]);