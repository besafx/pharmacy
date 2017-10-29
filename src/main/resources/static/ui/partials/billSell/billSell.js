app.controller("billSellCtrl", ['BillSellService', 'TransactionSellService', 'ModalProvider', '$scope', '$rootScope', '$state', '$timeout', '$uibModal', '$location', '$anchorScroll',
    function (BillSellService, TransactionSellService, ModalProvider, $scope, $rootScope, $state, $timeout, $uibModal, $location, $anchorScroll) {

        $scope.selected = {};
        $scope.selected.transactionSells = [];
        $scope.buffer = {};
        $scope.buffer.viewInsideSalesTable = true;
        $scope.billSells = [];

        $scope.items = [];
        $scope.items.push(
            {'id': 1, 'type': 'link', 'name': $rootScope.lang === 'AR' ? 'البرامج' : 'Application', 'link': 'menu'},
            {'id': 2, 'type': 'title', 'name': $rootScope.lang === 'AR' ? 'فواتير البيع' : 'Bill Sales'}
        );

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
                $scope.buffer = buffer;
                $scope.refreshSalesTable();
            }, function () {
            });
        };

        $scope.refreshSalesTable = function () {
            var search = [];
            //
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
            //
            if ($scope.buffer.paymentMethodList) {
                var paymentMethods = [];
                for (var i = 0; i < $scope.buffer.paymentMethodList.length; i++) {
                    paymentMethods.push($scope.buffer.paymentMethodList[i]);
                }
                search.push('paymentMethods=');
                search.push(paymentMethods);
                search.push('&');
            }
            //
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
            if ($scope.buffer.orderCodeFrom) {
                search.push('orderCodeFrom=');
                search.push($scope.buffer.orderCodeFrom);
                search.push('&');
            }
            if ($scope.buffer.orderCodeTo) {
                search.push('orderCodeTo=');
                search.push($scope.buffer.orderCodeTo);
                search.push('&');
            }
            //
            if ($scope.buffer.orderFalconCode) {
                search.push('orderFalconCode=');
                search.push($scope.buffer.orderFalconCode);
                search.push('&');
            }
            if ($scope.buffer.orderCustomerName) {
                search.push('orderCustomerName=');
                search.push($scope.buffer.orderCustomerName);
                search.push('&');
            }
            //
            search.push('viewInsideSalesTable=');
            search.push($scope.buffer.viewInsideSalesTable);
            search.push('&');
            //
            BillSellService.filter(search.join("")).then(function (data) {
                $scope.billSells = data;
                $scope.setSelected(data[0]);

                $scope.items = [];
                $scope.items.push(
                    {
                        'id': 1,
                        'type': 'link',
                        'name': $rootScope.lang === 'AR' ? 'البرامج' : 'Application',
                        'link': 'menu'
                    },
                    {
                        'id': 2,
                        'type': 'title',
                        'name': $rootScope.lang === 'AR' ? 'فواتير البيع' : 'Bill Sales'
                    }
                );
                if($scope.buffer.viewInsideSalesTable){
                    $scope.items.push(
                        {
                            'id': 3,
                            'type': 'link',
                            'name': $rootScope.lang === 'AR' ? 'مبيعات داخلية' : 'Inside Sales',
                            'link': 'menu'
                        }
                    );
                }else{
                    $scope.items.push(
                        {
                            'id': 3,
                            'type': 'link',
                            'name': $rootScope.lang === 'AR' ? 'مبيعات خارجية' : 'Outside Sales',
                            'link': 'menu'
                        }
                    );
                }

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
                $rootScope.showConfirmNotify("فواتير البيع", "هل تود حذف الفاتورة فعلاً؟", "error", "fa-trash", function () {
                    BillSellService.remove(billSell.id).then(function () {
                        var index = $scope.billSells.indexOf(billSell);
                        $scope.billSells.splice(index, 1);
                        $scope.setSelected($scope.billSells[0]);
                    });
                });
                return;
            }

            $rootScope.showConfirmNotify("فواتير البيع", "هل تود حذف الفاتورة فعلاً؟", "error", "fa-trash", function () {
                BillSellService.remove($scope.selected.id).then(function () {
                    var index = $scope.billSells.indexOf($scope.selected);
                    $scope.billSells.splice(index, 1);
                    $scope.setSelected($scope.billSells[0]);
                });
            });
        };

        $scope.payBillSell = function (billSell) {
            if (billSell) {
                $rootScope.showConfirmNotify("فواتير البيع", "هل تود تسديد الفاتورة فعلاً؟", "warning", "fa-money", function () {
                    BillSellService.pay(billSell.id).then(function (data) {
                        var index = $scope.billSells.indexOf(billSell);
                        $scope.billSells[index].paymentMethod = data.paymentMethod;
                    });
                });
                return;
            }

            $rootScope.showConfirmNotify("فواتير البيع", "هل تود تسديد الفاتورة فعلاً؟", "warning", "fa-money", function () {
                BillSellService.pay($scope.selected.id).then(function (data) {
                    var index = $scope.billSells.indexOf($scope.selected);
                    $scope.billSells[index].paymentMethod = data.paymentMethod;
                });
            });
        };

        $scope.deleteTransactionSell = function (transactionSell) {
            if (transactionSell) {
                $rootScope.showConfirmNotify("فواتير البيع", "هل تود حذف الطلبية فعلاً؟", "error", "fa-trash", function () {
                    TransactionSellService.remove(transactionSell.id).then(function () {
                        var index = $scope.selected.transactionSells.indexOf(transactionSell);
                        $scope.selected.transactionSells.splice(index, 1);
                        $scope.setSelected($scope.selected.transactionSells[0]);
                    });
                });

            }
        };

        $scope.newBillSell = function () {
            ModalProvider.openBillSellCreateModel().result.then(function (data) {
                $rootScope.showConfirmNotify("المبيعات", "هل تود طباعة الفاتورة ؟", "notification", "fa-info", function () {
                    $scope.print(data);
                });
                $scope.billSells.splice(0, 0, data);
                $scope.setSelected(data);
            }, function () {
                console.info('BillSellCreateModel Closed.');
            });
        };

        $scope.newBillSellForOrder = function () {
            ModalProvider.openBillSellForOrderCreateModel().result.then(function (data) {
                $rootScope.showConfirmNotify("المبيعات", "هل تود طباعة الفاتورة ؟", "notification", "fa-info", function () {
                    $scope.print(data);
                });
                $scope.billSells.splice(0, 0, data);
                $scope.setSelected(data);
            }, function () {
                console.info('BillSellCreateModel Closed.');
            });
        };

        $scope.newTransactionSell = function () {
            ModalProvider.openTransactionSellCreateModel($scope.selected).result.then(function (data) {
                $scope.selected.transactionSells.splice(0, 0, data);
            }, function () {
                console.info('TransactionSellCreateModel Closed.');
            });
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
                html: '<div class="drop-menu">تسديد الفاتورة<span class="fa fa-money fa-lg"></span></div>',
                enabled: function ($itemScope) {
                    return $itemScope.billSell.paymentMethod==='Later';
                },
                click: function ($itemScope, $event, value) {
                    $scope.payBillSell($itemScope.billSell);
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
            $location.hash('billSellMenu');
            $anchorScroll();
            window.componentHandler.upgradeAllRegistered();
        }, 1500);

    }]);