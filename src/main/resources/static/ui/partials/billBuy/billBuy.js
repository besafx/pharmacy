app.controller("billBuyCtrl", ['BillBuyService', 'TransactionBuyService', 'ModalProvider', '$scope', '$rootScope', '$state', '$timeout', '$uibModal',
    function (BillBuyService, TransactionBuyService, ModalProvider, $scope, $rootScope, $state, $timeout, $uibModal) {

        $scope.selected = {};
        $scope.buffer = {};

        $scope.setSelected = function (object) {
            if (object) {
                angular.forEach($scope.billBuys, function (billBuy) {
                    if (object.id == billBuy.id) {
                        $scope.selected = billBuy;
                        return billBuy.isSelected = true;
                    } else {
                        return billBuy.isSelected = false;
                    }
                });
            }
        };

        $scope.openFilter = function () {
            var modalInstance = $uibModal.open({
                animation: true,
                ariaLabelledBy: 'modal-title',
                ariaDescribedBy: 'modal-body',
                templateUrl: '/ui/partials/billBuy/billBuyFilter.html',
                controller: 'billBuyFilterCtrl',
                scope: $scope,
                backdrop: 'static',
                keyboard: false
            });

            modalInstance.result.then(function (buffer) {
                var search = [];

                if (buffer.codeFrom) {
                    search.push('codeFrom=');
                    search.push(buffer.codeFrom);
                    search.push('&');
                }
                if (buffer.codeTo) {
                    search.push('codeTo=');
                    search.push(buffer.codeTo);
                    search.push('&');
                }
                //
                if (buffer.paymentMethodList) {
                    var orderConditions = [];
                    for (var i = 0; i < buffer.paymentMethodList.length; i++) {
                        paymentMethods.push(buffer.paymentMethodList[i]);
                    }
                    search.push('paymentMethods=');
                    search.push(paymentMethods);
                    search.push('&');
                }
                //
                if (buffer.dateTo) {
                    search.push('dateTo=');
                    search.push(buffer.dateTo.getTime());
                    search.push('&');
                }
                if (buffer.dateFrom) {
                    search.push('dateFrom=');
                    search.push(buffer.dateFrom.getTime());
                    search.push('&');
                }
                //
                if (buffer.suppliersList) {
                    var suppliers = [];
                    for (var i = 0; i < buffer.suppliersList.length; i++) {
                        suppliers.push(buffer.suppliersList[i].id);
                    }
                    search.push('suppliers=');
                    search.push(suppliers);
                    search.push('&');
                }
                //
                BillBuyService.filter(search.join("")).then(function (data) {
                    $scope.billBuys = data;
                    $scope.setSelected(data[0]);
                });
            }, function () {
            });
        };

        $scope.refreshTransactionBuyByBill = function () {
            if ($scope.selected) {
                TransactionBuyService.findByBillBuy($scope.selected.id).then(function (data) {
                    $scope.selected.transactionBuys = data
                });
            }
        };

        $scope.delete = function (billBuy) {
            if (billBuy) {
                $rootScope.showConfirmNotify("حذف البيانات", "هل تود حذف الفاتورة فعلاً؟", "error", "fa-trash", function () {
                    BillBuyService.remove(billBuy.id).then(function () {
                        var index = $scope.billBuys.indexOf(billBuy);
                        $scope.billBuys.splice(index, 1);
                        $scope.setSelected($scope.billBuys[0]);
                    });
                });
                return;
            }

            $rootScope.showConfirmNotify("حذف البيانات", "هل تود حذف الفاتورة فعلاً؟", "error", "fa-trash", function () {
                BillBuyService.remove($scope.selected.id).then(function () {
                    var index = $scope.billBuys.indexOf($scope.selected);
                    $scope.billBuys.splice(index, 1);
                    $scope.setSelected($scope.billBuys[0]);
                });
            });
        };

        $scope.deleteTransactionBuy = function (transactionBuy) {
            if (transactionBuy) {
                $rootScope.showConfirmNotify("حذف البيانات", "هل تود حذف الطلبية فعلاً؟", "error", "fa-trash", function () {
                    TransactionBuyService.remove(transactionBuy.id).then(function () {
                        var index = $scope.selected.transactionBuys.indexOf(transactionBuy);
                        $scope.selected.transactionBuys.splice(index, 1);
                        $scope.setSelected($scope.selected.transactionBuys[0]);
                        $scope.calculateCostSum();
                    });
                });

            }
        };

        $scope.newBillBuy = function () {
            ModalProvider.openBillBuyCreateModel().result.then(function (data) {
                if ($scope.billBuys) {
                    $scope.billBuys.splice(0, 0, data);
                }
            }, function () {
                console.info('BillBuyCreateModel Closed.');
            });
        };

        $scope.newTransactionBuy = function () {
            ModalProvider.openTransactionBuyCreateModel($scope.selected).result.then(function (data) {
                if ($scope.selected) {
                    $scope.selected.transactionBuys.splice(0, 0, data);
                    $scope.calculateCostSum();
                }
            }, function () {
                console.info('TransactionBuyCreateModel Closed.');
            });
        };

        $scope.calculateCostSum = function () {
            $scope.totalCost = 0;
            $scope.totalCostAfterDiscount = 0;
            if ($scope.selected.transactionBuys) {
                for (var i = 0; i < $scope.selected.transactionBuys.length; i++) {
                    var transactionBuy = $scope.selected.transactionBuys[i];
                    $scope.totalCost = $scope.totalCost + (transactionBuy.unitBuyCost * transactionBuy.quantity);
                }
                $scope.totalCostAfterDiscount = $scope.totalCost - (($scope.totalCost * $scope.selected.discount) / 100);
            }
        };

        $scope.rowMenu = [
            {
                html: '<div class="drop-menu">انشاء فاتورة جديد<span class="fa fa-pencil fa-lg"></span></div>',
                enabled: function () {
                    return $rootScope.contains($rootScope.me.team.authorities, ['ROLE_BILL_BUY_CREATE']);
                },
                click: function ($itemScope, $event, value) {
                    $scope.newBillBuy();
                }
            },
            {
                html: '<div class="drop-menu">حذف الفاتورة<span class="fa fa-trash fa-lg"></span></div>',
                enabled: function () {
                    return $rootScope.contains($rootScope.me.team.authorities, ['ROLE_BILL_BUY_DELETE']);
                },
                click: function ($itemScope, $event, value) {
                    $scope.delete($itemScope.billBuy);
                }
            }
        ];

        $timeout(function () {
            window.componentHandler.upgradeAllRegistered();
        }, 1500);

    }]);