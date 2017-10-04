app.controller('billSellForOrderCreateCtrl', ['OrderService', 'DrugService', 'DrugUnitService', 'ModalProvider', 'BillSellService', '$scope', '$rootScope', '$timeout', '$log', '$uibModalInstance', 'title',
    function (OrderService, DrugService, DrugUnitService, ModalProvider, BillSellService, $scope, $rootScope, $timeout, $log, $uibModalInstance, title) {

        $scope.billSell = {};
        $scope.title = title;
        $scope.buffer = {};
        $scope.selected = {};

        $scope.setSelected = function (object) {
            if (object) {
                angular.forEach($scope.orders, function (order) {
                    if (object.id == order.id) {
                        $scope.selected = order;
                        $scope.billSell.order = order;
                        $scope.loadDrugs();
                        return order.isSelected = true;
                    } else {
                        return order.isSelected = false;
                    }
                });
            }
        };

        $scope.search = function () {

            var search = [];

            if ($scope.buffer.codeFrom) {
                search.push('codeFrom=');
                search.push($scope.buffer.codeFrom);
                search.push('&');
            }
            if ($scope.buffer.codeTo) {
                search.push('codeTo=');
                search.push($scope.buffer.codeTo);
                search.push('&');
            }
            if ($scope.buffer.dateTo) {
                search.push('dateTo=');
                search.push($scope.buffer.dateTo.getTime());
                search.push('&');
            }
            if ($scope.buffer.dateFrom) {
                search.push('dateFrom=');
                search.push($scope.buffer.dateFrom.getTime());
                search.push('&');
            }
            //
            if ($scope.buffer.customerName) {
                search.push('customerName=');
                search.push($scope.buffer.customerName);
                search.push('&');
            }
            if ($scope.buffer.customerMobile) {
                search.push('customerMobile=');
                search.push($scope.buffer.customerMobile);
                search.push('&');
            }
            if ($scope.buffer.customerIdentityNumber) {
                search.push('customerIdentityNumber=');
                search.push($scope.buffer.customerIdentityNumber);
                search.push('&');
            }
            //
            if ($scope.buffer.falconCode) {
                search.push('falconCode=');
                search.push($scope.buffer.falconCode);
                search.push('&');
            }
            if ($scope.buffer.falconType) {
                search.push('falconType=');
                search.push($scope.buffer.falconType);
                search.push('&');
            }
            if ($scope.buffer.weightTo) {
                search.push('weightTo=');
                search.push($scope.buffer.weightTo);
                search.push('&');
            }
            if ($scope.buffer.weightFrom) {
                search.push('weightFrom=');
                search.push($scope.buffer.weightFrom);
                search.push('&');
            }
            //
            OrderService.filter(search.join("")).then(function (data) {
                $scope.orders = data;
                $scope.setSelected(data[0]);
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
                // $uibModalInstance.close(data);
            });
        };

        $scope.cancel = function () {
            $uibModalInstance.dismiss('cancel');
        };

    }]);