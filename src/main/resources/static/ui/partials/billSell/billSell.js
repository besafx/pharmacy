app.controller("billSellCtrl", ['BillSellService', 'TransactionSellService', 'ModalProvider', '$scope', '$rootScope', '$state', '$timeout', '$uibModal',
    function (BillSellService, TransactionSellService, ModalProvider, $scope, $rootScope, $state, $timeout, $uibModal) {

        $scope.selected = {};
        $scope.buffer = {};

        $scope.setSelected = function (object) {
            if (object) {
                angular.forEach($scope.billSells, function (billSell) {
                    if (object.id == billSell.id) {
                        $scope.selected = billSell;
                        return billSell.isSelected = true;
                    } else {
                        return billSell.isSelected = false;
                    }
                });
            }
        };

        $scope.openFilter = function () {
            var modalInstance = $uibModal.open({
                animation: true,
                ariaLabelledBy: 'modal-title',
                ariaDescribedBy: 'modal-body',
                templateUrl: '/ui/partials/billSell/billSellFilter.html',
                controller: 'billSellFilterCtrl',
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
                if (buffer.customersList) {
                    var customers = [];
                    for (var i = 0; i < buffer.customersList.length; i++) {
                        customers.push(buffer.customersList[i].id);
                    }
                    search.push('customers=');
                    search.push(customers);
                    search.push('&');
                }
                //
                BillSellService.filter(search.join("")).then(function (data) {
                    $scope.billSells = data;
                    $scope.setSelected(data[0]);
                });
            }, function () {
            });
        };

        $scope.refreshTransactionSellByBill = function () {
            if ($scope.selected) {
                TransactionSellService.findByBillSell($scope.selected.id).then(function (data) {
                    $scope.selected.transactionSells = data
                });
            }
        };

        $scope.delete = function (billSell) {
            if (billSell) {
                $rootScope.showConfirmNotify("حذف البيانات", "هل تود حذف الفاتورة فعلاً؟", "error", "fa-trash", function () {
                    BillSellService.remove(billSell.id).then(function () {
                        var index = $scope.billSells.indexOf(billSell);
                        $scope.billSells.splice(index, 1);
                        $scope.setSelected($scope.billSells[0]);
                    });
                });
                return;
            }

            $rootScope.showConfirmNotify("حذف البيانات", "هل تود حذف الفاتورة فعلاً؟", "error", "fa-trash", function () {
                BillSellService.remove($scope.selected.id).then(function () {
                    var index = $scope.billSells.indexOf($scope.selected);
                    $scope.billSells.splice(index, 1);
                    $scope.setSelected($scope.billSells[0]);
                });
            });
        };

        $scope.deleteTransactionSell = function (transactionSell) {
            if (transactionSell) {
                $rootScope.showConfirmNotify("حذف البيانات", "هل تود حذف الطلبية فعلاً؟", "error", "fa-trash", function () {
                    TransactionSellService.remove(transactionSell.id).then(function () {
                        var index = $scope.selected.transactionSells.indexOf(transactionSell);
                        $scope.selected.transactionSells.splice(index, 1);
                        $scope.setSelected($scope.selected.transactionSells[0]);
                        $scope.calculateCostSum();
                    });
                });

            }
        };

        $scope.newBillSell = function () {
            ModalProvider.openBillSellCreateModel().result.then(function (data) {
                $rootScope.showConfirmNotify("المبيعات", "هل تود طباعة الفاتورة ؟", "notification", "fa-info", function () {
                    $scope.print(data);
                });
                if ($scope.billSells) {
                    $scope.billSells.splice(0, 0, data);
                }
            }, function () {
                console.info('BillSellCreateModel Closed.');
            });
        };

        $scope.newTransactionSell = function () {
            ModalProvider.openTransactionSellCreateModel($scope.selected).result.then(function (data) {
                if ($scope.selected) {
                    $scope.selected.transactionSells.splice(0, 0, data);
                    $scope.calculateCostSum();
                }
                //اعطاء امر طباعة للفاتورة
            }, function () {
                console.info('TransactionSellCreateModel Closed.');
            });
        };

        $scope.calculateCostSum = function () {
            $scope.totalCost = 0;
            $scope.totalCostAfterDiscount = 0;
            if ($scope.selected.transactionSells) {
                for (var i = 0; i < $scope.selected.transactionSells.length; i++) {
                    var transactionSell = $scope.selected.transactionSells[i];
                    $scope.totalCost = $scope.totalCost + (transactionSell.unitSellCost * transactionSell.quantity);
                }
                $scope.totalCostAfterDiscount = $scope.totalCost - (($scope.totalCost * $scope.selected.discount) / 100);
            }
        };

        $scope.print = function (billSell) {
            window.open('/report/billSell/' + billSell.id + '/PDF');
        };

        $scope.rowMenu = [
            {
                html: '<div class="drop-menu">انشاء فاتورة جديد<span class="fa fa-pencil fa-lg"></span></div>',
                enabled: function () {
                    return $rootScope.contains($rootScope.me.team.authorities, ['ROLE_BILL_SELL_CREATE']);
                },
                click: function ($itemScope, $event, value) {
                    $scope.newBillSell();
                }
            },
            {
                html: '<div class="drop-menu">حذف الفاتورة<span class="fa fa-trash fa-lg"></span></div>',
                enabled: function () {
                    return $rootScope.contains($rootScope.me.team.authorities, ['ROLE_BILL_SELL_DELETE']);
                },
                click: function ($itemScope, $event, value) {
                    $scope.delete($itemScope.billSell);
                }
            },
            {
                html: '<div class="drop-menu">طباعة الفاتورة<span class="fa fa-print fa-lg"></span></div>',
                enabled: function () {
                    return true;
                },
                click: function ($itemScope, $event, value) {
                    $scope.print($itemScope.billSell);
                }
            }
        ];

        $timeout(function () {
            window.componentHandler.upgradeAllRegistered();
        }, 1500);

    }]);