app.controller('insideSalesCreateCtrl', ['OrderService', 'DrugService', 'DrugUnitService', 'ModalProvider', 'BillSellService', '$scope', '$rootScope', '$timeout', '$log', '$uibModalInstance', 'title',
    function (OrderService, DrugService, DrugUnitService, ModalProvider, BillSellService, $scope, $rootScope, $timeout, $log, $uibModalInstance, title) {

        $scope.title = title;
        $scope.billSell = {};
        $scope.orders = [];
        $scope.findOrdersByToday = function () {
            OrderService.findByToday("FILTER_ORDER_COMBO_DIAGNOSIS").then(function (data) {
                $scope.orders = data;
            });
        };
        $scope.findOrdersByWeek = function () {
            OrderService.findByWeek("FILTER_ORDER_COMBO_DIAGNOSIS").then(function (data) {
                $scope.orders = data;
            });
        };
        $scope.findOrdersByMonth = function () {
            OrderService.findByMonth("FILTER_ORDER_COMBO_DIAGNOSIS").then(function (data) {
                $scope.orders = data;
            });
        };
        $scope.findOrdersByYear = function () {
            OrderService.findByYear("FILTER_ORDER_COMBO_DIAGNOSIS").then(function (data) {
                $scope.orders = data;
            });
        };
        $scope.refreshOrder = function (order) {
            OrderService.findOne(order.id).then(function (data) {
                $scope.order = data;
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
                if(diagnosis.treated === false){
                    var transactionSell = {};
                    transactionSell.transactionBuy = diagnosis.transactionBuy;
                    transactionSell.drug = diagnosis.drug;
                    transactionSell.drugUnit = diagnosis.related.obj1;
                    transactionSell.unitSellCost = diagnosis.related.obj3;
                    transactionSell.quantity = diagnosis.quantity;
                    transactionSell.note = diagnosis.note;
                    transactionSellList.push(transactionSell);
                }
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