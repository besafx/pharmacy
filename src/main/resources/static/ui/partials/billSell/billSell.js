app.controller("billSellCtrl", ['BillSellService', 'TransactionSellService', 'OrderService', 'ModalProvider', '$scope', '$rootScope', '$state', '$timeout', '$uibModal',
    function (BillSellService, TransactionSellService, OrderService, ModalProvider, $scope, $rootScope, $state, $timeout, $uibModal) {

        /**************************************************************
         *                                                            *
         * Variables                                                  *
         *                                                            *
         *************************************************************/
        var vm = this;

        vm.buffer = {};

        vm.insideBillSells = [];
        vm.outsideBillSells = [];

        vm.items = [];
        vm.items.push(
            {'id': 1, 'type': 'link', 'name': $rootScope.lang === 'AR' ? 'البرامج' : 'Application', 'link': 'menu'},
            {'id': 2, 'type': 'title', 'name': $rootScope.lang === 'AR' ? 'فواتير البيع' : 'Bill Sales'}
        );

        /**************************************************************
         *                                                            *
         * Bill Sell                                                  *
         *                                                            *
         *************************************************************/
        vm.refreshBillSell = function (billSell) {
            BillSellService.findOne(billSell.id).then(function (data) {
                return billSell = data;
            });
        };

        vm.refreshOrderByBillSell = function (billSell) {
            OrderService.findOne(billSell.order.id).then(function (data) {
                return billSell.order = data;
            });
        };

        vm.refreshTransactionSellByBill = function (billSell) {
            TransactionSellService.findByBillSell(billSell.id).then(function (data) {
                return billSell.transactionSells = data
            });
        };

        vm.deleteTransactionSell = function (transactionSell) {
            if (transactionSell) {
                $rootScope.showConfirmNotify("فواتير البيع", "هل تود حذف الطلبية فعلاً؟", "error", "fa-trash", function () {
                    TransactionSellService.remove(transactionSell.id).then(function () {
                        var index = vm.selected.transactionSells.indexOf(transactionSell);
                        vm.selected.transactionSells.splice(index, 1);
                        vm.setSelected(vm.selected.transactionSells[0]);
                    });
                });

            }
        };

        vm.newTransactionSell = function (billSell) {
            ModalProvider.openTransactionSellCreateModel(billSell).result.then(function (data) {
                return billSell.transactionSells.splice(0, 0, data);
            }, function () {
                console.info('TransactionSellCreateModel Closed.');
            });
        };

        vm.newBillSellReceipt = function (billSell) {
            ModalProvider.openBillSellReceiptCreateModel(billSell).result.then(function (data) {
                if (billSell.billSellReceipts) {
                    return billSell.billSellReceipts.splice(0, 0, data);
                }
            }, function () {
                console.info('OrderReceiptCreateModel Closed.');
            });
        };

        vm.print = function (billSell) {
            window.open('/report/billSell/' + billSell.id + '/PDF');
        };

        /**************************************************************
         *                                                            *
         * Inside Sales                                               *
         *                                                            *
         *************************************************************/
        vm.openInsideSalesFilter = function () {
            var modalInstance = $uibModal.open({
                animation: true,
                ariaLabelledBy: 'modal-title',
                ariaDescribedBy: 'modal-body',
                templateUrl: '/ui/partials/billSell/billSellFilter.html',
                controller: 'billSellFilterCtrl',
                scope: $scope,
                backdrop: 'static',
                keyboard: false,
                resolve: {
                    title: function () {
                        return $rootScope.lang==='AR' ? 'البحث فى المبيعات الداخلية' : 'Searching For Inside Sales';
                    }
                }
            });

            modalInstance.result.then(function (buffer) {
                vm.buffer = buffer;
                vm.refreshInsideSalesTable();
            }, function () {});
        };

        vm.refreshInsideSalesTable = function () {
            var search = [];
            //
            if (vm.buffer.codeFrom) {
                search.push('codeFrom=');
                search.push(vm.buffer.codeFrom);
                search.push('&');
            }
            if (vm.buffer.codeTo) {
                search.push('codeTo=');
                search.push(vm.buffer.codeTo);
                search.push('&');
            }
            //
            if (vm.buffer.dateTo) {
                search.push('dateTo=');
                search.push(vm.buffer.dateTo.getTime());
                search.push('&');
            }
            if (vm.buffer.dateFrom) {
                search.push('dateFrom=');
                search.push(vm.buffer.dateFrom.getTime());
                search.push('&');
            }
            //
            if (vm.buffer.orderCodeFrom) {
                search.push('orderCodeFrom=');
                search.push(vm.buffer.orderCodeFrom);
                search.push('&');
            }
            if (vm.buffer.orderCodeTo) {
                search.push('orderCodeTo=');
                search.push(vm.buffer.orderCodeTo);
                search.push('&');
            }
            //
            if (vm.buffer.orderFalconCode) {
                search.push('orderFalconCode=');
                search.push(vm.buffer.orderFalconCode);
                search.push('&');
            }
            if (vm.buffer.orderCustomerName) {
                search.push('orderCustomerName=');
                search.push(vm.buffer.orderCustomerName);
                search.push('&');
            }
            //
            search.push('viewInsideSalesTable=');
            search.push(true);
            search.push('&');
            //
            BillSellService.filter(search.join("")).then(function (data) {

                vm.insideBillSells = data;

                vm.items = [];
                vm.items.push(
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
                    },
                    {
                        'id': 3,
                        'type': 'link',
                        'name': $rootScope.lang === 'AR' ? 'مبيعات داخلية' : 'Inside Sales',
                        'link': 'menu'
                    }
                );

            });
        };

        vm.findInsideSalesByToday = function () {
            BillSellService.findInsideSalesByToday().then(function (data) {

                vm.insideBillSells = data;

                vm.items = [];
                vm.items.push(
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
                    },
                    {
                        'id': 3,
                        'type': 'title',
                        'name': $rootScope.lang === 'AR' ? 'مبيعات داخلية' : 'Inside Sales'
                    },
                    {
                        'id': 4,
                        'type': 'title',
                        'name': $rootScope.lang === 'AR' ? 'فواتير اليوم' : 'Today Bills'
                    }
                );

                $timeout(function () {
                    window.componentHandler.upgradeAllRegistered();
                }, 500);
            });
        };

        vm.findInsideSalesByWeek = function () {
            BillSellService.findInsideSalesByWeek().then(function (data) {

                vm.insideBillSells = data;

                vm.items = [];
                vm.items.push(
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
                    },
                    {
                        'id': 3,
                        'type': 'title',
                        'name': $rootScope.lang === 'AR' ? 'مبيعات داخلية' : 'Inside Sales'
                    },
                    {
                        'id': 4,
                        'type': 'title',
                        'name': $rootScope.lang === 'AR' ? 'فواتير الاسبوع' : 'Week Bills'
                    }
                );

                $timeout(function () {
                    window.componentHandler.upgradeAllRegistered();
                }, 500);
            });
        };

        vm.findInsideSalesByMonth = function () {
            BillSellService.findInsideSalesByMonth().then(function (data) {

                vm.insideBillSells = data;

                vm.items = [];
                vm.items.push(
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
                    },
                    {
                        'id': 3,
                        'type': 'title',
                        'name': $rootScope.lang === 'AR' ? 'مبيعات داخلية' : 'Inside Sales'
                    },
                    {
                        'id': 4,
                        'type': 'title',
                        'name': $rootScope.lang === 'AR' ? 'فواتير الشهر' : 'Month Bills'
                    }
                );

                $timeout(function () {
                    window.componentHandler.upgradeAllRegistered();
                }, 500);
            });
        };

        vm.findInsideSalesByYear = function () {
            BillSellService.findInsideSalesByYear().then(function (data) {

                vm.insideBillSells = data;

                vm.items = [];
                vm.items.push(
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
                    },
                    {
                        'id': 3,
                        'type': 'title',
                        'name': $rootScope.lang === 'AR' ? 'مبيعات داخلية' : 'Inside Sales'
                    },
                    {
                        'id': 4,
                        'type': 'title',
                        'name': $rootScope.lang === 'AR' ? 'فواتير العام' : 'Year Bills'
                    }
                );

                $timeout(function () {
                    window.componentHandler.upgradeAllRegistered();
                }, 500);
            });
        };

        vm.newBillSellForOrder = function () {
            ModalProvider.openBillSellForOrderCreateModel().result.then(function (data) {
                $rootScope.showConfirmNotify("المبيعات", "هل تود طباعة الفاتورة ؟", "notification", "fa-info", function () {
                    vm.print(data);
                });
                vm.insideBillSells.splice(0, 0, data);
            }, function () {
                console.info('BillSellCreateModel Closed.');
            });
        };

        vm.deleteInsideBillSell = function (billSell) {
            $rootScope.showConfirmNotify("فواتير البيع", "هل تود حذف الفاتورة فعلاً؟", "error", "fa-trash", function () {
                BillSellService.remove(billSell.id).then(function () {
                    var index = vm.insideBillSells.indexOf(billSell);
                    vm.insideBillSells.splice(index, 1);
                });
            });
        };

        /**************************************************************
         *                                                            *
         * Outside Sales                                              *
         *                                                            *
         *************************************************************/
        vm.openOutsideSalesFilter = function () {
            var modalInstance = $uibModal.open({
                animation: true,
                ariaLabelledBy: 'modal-title',
                ariaDescribedBy: 'modal-body',
                templateUrl: '/ui/partials/billSell/billSellFilter.html',
                controller: 'billSellFilterCtrl',
                scope: $scope,
                backdrop: 'static',
                keyboard: false,
                resolve: {
                    title: function () {
                        return $rootScope.lang==='AR' ? 'البحث فى المبيعات الخارجية' : 'Searching For Outside Sales';
                    }
                }
            });

            modalInstance.result.then(function (buffer) {
                vm.buffer = buffer;
                vm.refreshOutsideSalesTable();
            }, function () {});
        };

        vm.refreshOutsideSalesTable = function () {
            var search = [];
            //
            if (vm.buffer.codeFrom) {
                search.push('codeFrom=');
                search.push(vm.buffer.codeFrom);
                search.push('&');
            }
            if (vm.buffer.codeTo) {
                search.push('codeTo=');
                search.push(vm.buffer.codeTo);
                search.push('&');
            }
            //
            if (vm.buffer.dateTo) {
                search.push('dateTo=');
                search.push(vm.buffer.dateTo.getTime());
                search.push('&');
            }
            if (vm.buffer.dateFrom) {
                search.push('dateFrom=');
                search.push(vm.buffer.dateFrom.getTime());
                search.push('&');
            }
            //
            if (vm.buffer.orderCodeFrom) {
                search.push('orderCodeFrom=');
                search.push(vm.buffer.orderCodeFrom);
                search.push('&');
            }
            if (vm.buffer.orderCodeTo) {
                search.push('orderCodeTo=');
                search.push(vm.buffer.orderCodeTo);
                search.push('&');
            }
            //
            if (vm.buffer.orderFalconCode) {
                search.push('orderFalconCode=');
                search.push(vm.buffer.orderFalconCode);
                search.push('&');
            }
            if (vm.buffer.orderCustomerName) {
                search.push('orderCustomerName=');
                search.push(vm.buffer.orderCustomerName);
                search.push('&');
            }
            //
            search.push('viewInsideSalesTable=');
            search.push(false);
            search.push('&');
            //
            BillSellService.filter(search.join("")).then(function (data) {
                vm.outsideBillSells = data;

                vm.items = [];
                vm.items.push(
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
                    },
                    {
                        'id': 3,
                        'type': 'link',
                        'name': $rootScope.lang === 'AR' ? 'مبيعات خارجية' : 'Outside Sales',
                        'link': 'menu'
                    }

                );


            });
        };

        vm.findOutsideSalesByToday = function () {
            BillSellService.findOutsideSalesByToday().then(function (data) {

                vm.outsideBillSells = data;

                vm.items = [];
                vm.items.push(
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
                    },
                    {
                        'id': 3,
                        'type': 'title',
                        'name': $rootScope.lang === 'AR' ? 'مبيعات خارجية' : 'Outside Sales'
                    },
                    {
                        'id': 4,
                        'type': 'title',
                        'name': $rootScope.lang === 'AR' ? 'فواتير اليوم' : 'Today Bills'
                    }
                );

                $timeout(function () {
                    window.componentHandler.upgradeAllRegistered();
                }, 500);
            });
        };

        vm.findOutsideSalesByWeek = function () {
            BillSellService.findOutsideSalesByWeek().then(function (data) {

                vm.outsideBillSells = data;

                vm.items = [];
                vm.items.push(
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
                    },
                    {
                        'id': 3,
                        'type': 'title',
                        'name': $rootScope.lang === 'AR' ? 'مبيعات خارجية' : 'Outside Sales'
                    },
                    {
                        'id': 4,
                        'type': 'title',
                        'name': $rootScope.lang === 'AR' ? 'فواتير الاسبوع' : 'Week Bills'
                    }
                );

                $timeout(function () {
                    window.componentHandler.upgradeAllRegistered();
                }, 500);
            });
        };

        vm.findOutsideSalesByMonth = function () {
            BillSellService.findOutsideSalesByMonth().then(function (data) {

                vm.outsideBillSells = data;

                vm.items = [];
                vm.items.push(
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
                    },
                    {
                        'id': 3,
                        'type': 'title',
                        'name': $rootScope.lang === 'AR' ? 'مبيعات خارجية' : 'Outside Sales'
                    },
                    {
                        'id': 4,
                        'type': 'title',
                        'name': $rootScope.lang === 'AR' ? 'فواتير الشهر' : 'Month Bills'
                    }
                );

                $timeout(function () {
                    window.componentHandler.upgradeAllRegistered();
                }, 500);
            });
        };

        vm.findOutsideSalesByYear = function () {
            BillSellService.findOutsideSalesByYear().then(function (data) {

                vm.outsideBillSells = data;

                vm.items = [];
                vm.items.push(
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
                    },
                    {
                        'id': 3,
                        'type': 'title',
                        'name': $rootScope.lang === 'AR' ? 'مبيعات خارجية' : 'Outside Sales'
                    },
                    {
                        'id': 4,
                        'type': 'title',
                        'name': $rootScope.lang === 'AR' ? 'فواتير العام' : 'Year Bills'
                    }
                );

                $timeout(function () {
                    window.componentHandler.upgradeAllRegistered();
                }, 500);
            });
        };

        vm.newBillSell = function () {
            ModalProvider.openBillSellCreateModel().result.then(function (data) {
                $rootScope.showConfirmNotify("المبيعات", "هل تود طباعة الفاتورة ؟", "notification", "fa-info", function () {
                    vm.print(data);
                });
                vm.outsideBillSells.splice(0, 0, data);
                vm.setSelected(data);
            }, function () {
                console.info('BillSellCreateModel Closed.');
            });
        };

        vm.deleteOutsideBillSell = function (billSell) {
            $rootScope.showConfirmNotify("فواتير البيع", "هل تود حذف الفاتورة فعلاً؟", "error", "fa-trash", function () {
                BillSellService.remove(billSell.id).then(function () {
                    var index = vm.outsideBillSells.indexOf(billSell);
                    vm.outsideBillSells.splice(index, 1);
                });
            });
        };


        /**************************************************************
         *                                                            *
         * General                                                    *
         *                                                            *
         *************************************************************/
        $timeout(function () {
            window.componentHandler.upgradeAllRegistered();
        }, 1500);

    }]);