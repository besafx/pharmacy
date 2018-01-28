app.controller('insideSalesCreateCtrl', ['OrderService', 'DrugService', 'DrugUnitService', 'ModalProvider', 'BillSellService', 'TransactionBuyService', '$scope', '$rootScope', '$timeout', '$log', '$uibModalInstance', 'title',
    function (OrderService, DrugService, DrugUnitService, ModalProvider, BillSellService, TransactionBuyService,  $scope, $rootScope, $timeout, $log, $uibModalInstance, title) {

        $scope.title = title;

        $scope.billSell = {};

        $scope.orders = [];

        $scope.findOrdersByToday = function () {
            OrderService.findByToday("FILTER_ORDER_COMBO_DIAGNOSIS").then(function (data) {
                $scope.orders = _.filter(data, function(v) { return v.unTreatedCount > 0 });
            });
        };

        $scope.findOrdersByWeek = function () {
            OrderService.findByWeek("FILTER_ORDER_COMBO_DIAGNOSIS").then(function (data) {
                $scope.orders = _.filter(data, function(v) { return v.unTreatedCount > 0 });
            });
        };

        $scope.findOrdersByMonth = function () {
            OrderService.findByMonth("FILTER_ORDER_COMBO_DIAGNOSIS").then(function (data) {
                $scope.orders = _.filter(data, function(v) { return v.unTreatedCount > 0 });
            });
        };

        $scope.findOrdersByYear = function () {
            OrderService.findByYear("FILTER_ORDER_COMBO_DIAGNOSIS").then(function (data) {
                $scope.orders = _.filter(data, function(v) { return v.unTreatedCount > 0 });
            });
        };

        $scope.refreshOrder = function (order) {
            OrderService.findOne(order.id).then(function (data) {
                $scope.order = data;
                $scope.billSell.order = data;
                angular.forEach($scope.billSell.order.diagnoses, function (diagnosis) {
                    DrugService.findOne(diagnosis.drug.id).then(function (newDrug) {
                        TransactionBuyService.findByDrug(newDrug.id).then(function (newTransactionBuys) {
                            var index = $scope.billSell.order.diagnoses.indexOf(diagnosis);
                            $scope.billSell.order.diagnoses[index].drug = newDrug;
                            $scope.billSell.order.diagnoses[index].drug.transactionBuys = newTransactionBuys;
                        });
                    });
                });
            })
        };

        $scope.transactionBuyCalculation = function (transactionBuy) {
            DrugUnitService.getRelatedPrices(transactionBuy.id).then(function (data) {
                return transactionBuy.relatedPrices = data;
            });
        };

        $scope.removeDiagnosisFromOrder = function (index) {
            $scope.billSell.order.diagnoses.splice(index, 1);
        };

        $scope.calculateCostSum = function () {
            $scope.totalCost = 0;
            $scope.totalCostAfterDiscount = 0;
            for (var i = 0; i < $scope.billSell.order.diagnoses.length; i++) {
                var diagnosis = $scope.billSell.order.diagnoses[i];
                $scope.totalCost = $scope.totalCost + (diagnosis.related.obj3 * diagnosis.quantity);
            }
            $scope.totalCostAfterDiscount = $scope.totalCost - (($scope.totalCost * $scope.billSell.discount) / 100);
        };

        $scope.submit = function () {
            var transactionSellList = [];
            angular.forEach($scope.billSell.order.diagnoses, function (diagnosis) {
                if(diagnosis.treated === false){
                    var transactionSell = {};
                    transactionSell.transactionBuy = diagnosis.transactionBuy;
                    transactionSell.drug = diagnosis.drug;
                    transactionSell.drugUnit = diagnosis.related.obj1;
                    transactionSell.unitCost = diagnosis.related.obj3;
                    transactionSell.quantity = diagnosis.quantity;
                    transactionSell.note = diagnosis.note;
                    transactionSellList.push(transactionSell);
                }
            });
            $scope.billSell.transactionSells = transactionSellList;
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