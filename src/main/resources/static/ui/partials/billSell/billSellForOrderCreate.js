app.controller('billSellForOrderCreateCtrl', ['OrderService', 'DrugService', 'DrugUnitService', 'ModalProvider', 'BillSellService', '$scope', '$rootScope', '$timeout', '$log', '$uibModalInstance', 'title',
    function (OrderService, DrugService, DrugUnitService, ModalProvider, BillSellService, $scope, $rootScope, $timeout, $log, $uibModalInstance, title) {

        $scope.billSell = {};

        $scope.title = title;
        
        $timeout(function () {
            $scope.refreshOrders();
        }, 2000);

        $scope.refreshOrders = function () {
            OrderService.findAllCombo().then(function (data) {
                $scope.orders = data;
            });
        };
        
        $scope.loadDrugs = function () {
            OrderService.findOne($scope.billSell.order.id).then(function (data) {
                $scope.billSell.order = data;
                angular.forEach($scope.billSell.order.diagnoses, function (diagnosis) {
                    DrugService.findOne(diagnosis.drug.id).then(function (data) {
                        var index = $scope.billSell.order.diagnoses.indexOf(diagnosis);
                        $scope.billSell.order.diagnoses[index].drug = data;
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

        $scope.submit = function () {
            var transactionSellList = [];
            angular.forEach($scope.billSell.order.diagnoses, function (diagnosis) {
                var transactionSell = {};
                transactionSell.transactionBuy = diagnosis.transactionBuy;
                transactionSell.drug = diagnosis.drug;
                transactionSell.drugUnit = diagnosis.related.obj1;
                transactionSell.unitSellCost = diagnosis.related.obj3;
                transactionSell.quantity = diagnosis.quantity;
                transactionSell.note = diagnosis.note;
                transactionSellList.push(transactionSell);
            });
            $scope.billSell.transactionSells = transactionSellList;
            BillSellService.create($scope.billSell).then(function (data) {
                $uibModalInstance.close(data);
            });
        };

        $scope.cancel = function () {
            $uibModalInstance.dismiss('cancel');
        };

    }]);