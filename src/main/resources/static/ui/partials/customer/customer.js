app.controller("customerCtrl", ['CustomerService', 'OrderService', 'ModalProvider', '$scope', '$rootScope', '$state', '$timeout', '$uibModal',
    function (CustomerService, OrderService, ModalProvider, $scope, $rootScope, $state, $timeout, $uibModal) {

        var vm = this;

        /**************************************************************
         *                                                            *
         * GENERAL                                                    *
         *                                                            *
         *************************************************************/
        vm.state = $state;

        vm.toggleList = 1;

        vm.selected = {};

        vm.selectedOrder = {};

        vm.findPage = function (tableState) {

            var pagination = tableState.pagination;

            console.log(pagination.number);
            console.log(pagination.start);

            var start = pagination.start || 0;
            var number = pagination.number || 5;

            CustomerService.findPage(start, number).then(function (result) {
                vm.customers = result.content;
                tableState.pagination.numberOfPages = result.totalPages;
            });

        };

        vm.fetchTableData = function () {
            CustomerService.findPage(1, 10).then(function (data) {
                vm.customers = data.content;
                vm.setSelected(data[0]);
            });
        };

        vm.refreshOrdersByCustomer = function () {
            OrderService.findByCustomer(vm.selected.id).then(function (data) {
                vm.selected.orders = data;
                vm.setSelectedOrder(data[0]);
            });
        };

        vm.setSelected = function (object) {
            if (object) {
                angular.forEach(vm.customers, function (customer) {
                    if (object.id == customer.id) {
                        vm.selected = customer;
                        return customer.isSelected = true;
                    } else {
                        return customer.isSelected = false;
                    }
                });
            }
        };

        vm.setSelectedOrder = function (object) {
            if (object) {
                angular.forEach(vm.selected.orders, function (order) {
                    if (object.id == order.id) {
                        vm.selectedOrder = order;
                        return order.isSelected = true;
                    } else {
                        return order.isSelected = false;
                    }
                });
            }
        };

        vm.delete = function (customer) {
            if (customer) {
                $rootScope.showConfirmNotify("حذف البيانات", "هل تود حذف العميل وكل ما يتعلق به من حسابات فعلاً؟", "error", "fa-trash", function () {
                    CustomerService.remove(customer.id).then(function () {
                        var index = vm.customers.indexOf(customer);
                        vm.customers.splice(index, 1);
                        vm.setSelected(vm.customers[0]);
                    });
                });
                return;
            }

            $rootScope.showConfirmNotify("حذف البيانات", "هل تود حذف العميل وكل ما يتعلق به من حسابات فعلاً؟", "error", "fa-trash", function () {
                CustomerService.remove(vm.selected.id).then(function () {
                    var index = vm.customers.indexOf(vm.selected);
                    vm.customers.splice(index, 1);
                    vm.setSelected(vm.customers[0]);
                });
            });
        };

        vm.newCustomer = function () {
            ModalProvider.openCustomerCreateModel().result.then(function (data) {
                vm.customers.splice(0, 0, data);
                vm.newFalcon(data);
            }, function () {
                console.info('CustomerCreateModel Closed.');
            });
        };

        vm.newFalcon = function (customer) {
            $rootScope.showConfirmNotify("العمليات على قواعد البيانات", "هل تود ربط حساب صقر جديد بالعميل؟", "information", "fa-database", function () {
                $uibModal.open({
                    animation: true,
                    ariaLabelledBy: 'modal-title',
                    ariaDescribedBy: 'modal-body',
                    templateUrl: '/ui/partials/customer/customerFalconCreateUpdate.html',
                    controller: 'customerFalconCreateUpdateCtrl',
                    backdrop: 'static',
                    keyboard: false,
                    resolve: {
                        title: function () {
                            return $rootScope.lang === 'AR' ? 'انشاء حساب صقر جديد للعميل' : 'New Falcon Account By Account';
                        },
                        action: function () {
                            return 'create';
                        },
                        falcon: function () {
                            var falcon = {};
                            falcon.customer = customer;
                            return falcon;
                        }
                    }
                }).result.then(function (data) {
                    ///////////////////////// TO DO ///////////////////////////////
                    console.info(data);
                }, function () {
                    console.info('CustomerFalconCreateModel Closed.');
                });
            });
        };

        vm.enable = function () {
            CustomerService.enable(vm.selected).then(function (data) {
                vm.fetchTableData();
            });
        };

        vm.disable = function () {
            CustomerService.disable(vm.selected).then(function (data) {
                vm.fetchTableData();
            });
        };

        vm.rowMenu = [
            {
                html: '<div class="drop-menu">انشاء عميل جديد<span class="fa fa-pencil fa-lg"></span></div>',
                enabled: function () {
                    return $rootScope.contains($rootScope.me.team.authorities, ['ROLE_CUSTOMER_CREATE']);
                },
                click: function ($itemScope, $event, value) {
                    vm.newCustomer();
                }
            },
            {
                html: '<div class="drop-menu">تعديل بيانات العميل<span class="fa fa-edit fa-lg"></span></div>',
                enabled: function () {
                    return $rootScope.contains($rootScope.me.team.authorities, ['ROLE_CUSTOMER_UPDATE']);
                },
                click: function ($itemScope, $event, value) {
                    ModalProvider.openCustomerUpdateModel($itemScope.customer);
                }
            },
            {
                html: '<div class="drop-menu">تفعيل العميل<span class="fa fa-edit fa-lg"></span></div>',
                enabled: function () {
                    return $rootScope.contains($rootScope.me.team.authorities, ['ROLE_CUSTOMER_ENABLE']);
                },
                click: function ($itemScope, $event, value) {
                    vm.enable($itemScope.customer);
                }
            },
            {
                html: '<div class="drop-menu">تعطيل العميل<span class="fa fa-edit fa-lg"></span></div>',
                enabled: function () {
                    return $rootScope.contains($rootScope.me.team.authorities, ['ROLE_CUSTOMER_DISABLE']);
                },
                click: function ($itemScope, $event, value) {
                    vm.disable($itemScope.customer);
                }
            },
            {
                html: '<div class="drop-menu">حذف العميل<span class="fa fa-trash fa-lg"></span></div>',
                enabled: function () {
                    return $rootScope.contains($rootScope.me.team.authorities, ['ROLE_CUSTOMER_DELETE']);
                },
                click: function ($itemScope, $event, value) {
                    vm.delete($itemScope.customer);
                }
            },
            {
                html: '<div class="drop-menu">التفاصيل<span class="fa fa-info fa-lg"></span></div>',
                enabled: function () {
                    return true;
                },
                click: function ($itemScope, $event, value) {
                    ModalProvider.openCustomerDetailsModel($itemScope.customer);
                }
            }
        ];

        vm.printPending = function (order) {
            window.open('/report/order/pending/' + order.id + '/PDF');
        };

        vm.printDiagnosed = function (order) {
            window.open('/report/order/diagnosed/' + order.id + '/PDF');
        };

        vm.newOrder = function () {
            ModalProvider.openOrderCreateModel().result.then(function (data) {
                $rootScope.showConfirmNotify("طلبات الفحص", "هل تود طباعة الطلب ؟", "notification", "fa-info", function () {
                    vm.printPending(data);
                });
                vm.selected.orders.splice(0, 0, data);
            }, function () {
                console.info('OrderCreateModel Closed.');
            });
        };

        vm.deleteOrder = function (order) {
            $rootScope.showConfirmNotify("طلبات الفحص", "هل تود حذف الطلب فعلاً؟", "error", "fa-trash", function () {
                OrderService.remove(order.id).then(function () {
                    var index = vm.selected.orders.indexOf(order);
                    vm.selected.orders.splice(index, 1);
                    vm.setSelected(vm.selected.orders[0]);
                });
            });
        };

        vm.payOrder = function (order) {
            $rootScope.showConfirmNotify("طلبات الفحص", "هل تود تسديد مستحقات طلب الفحص فعلاً؟", "warning", "fa-money", function () {
                OrderService.pay(order.id).then(function (data) {
                    var index = vm.selected.orders.indexOf(order);
                    vm.selected.orders[index].paymentMethod = data.paymentMethod;
                });
            });
        };

        vm.rowMenuOrder = [
            {
                html: '<div class="drop-menu">انشاء طلب جديد<span class="fa fa-pencil fa-lg"></span></div>',
                enabled: function () {
                    return $rootScope.contains($rootScope.me.team.authorities, ['ROLE_ORDER_CREATE']);
                },
                click: function ($itemScope, $event, value) {
                    vm.newOrder();
                }
            },
            {
                html: '<div class="drop-menu">حذف الطلب<span class="fa fa-trash fa-lg"></span></div>',
                enabled: function () {
                    return $rootScope.contains($rootScope.me.team.authorities, ['ROLE_ORDER_DELETE']);
                },
                click: function ($itemScope, $event, value) {
                    vm.deleteOrder($itemScope.order);
                }
            },
            {
                html: '<div class="drop-menu">تسديد مستحقات طلب الفحص<span class="fa fa-money fa-lg"></span></div>',
                enabled: function ($itemScope) {
                    return $itemScope.order.paymentMethod==='Later';
                },
                click: function ($itemScope, $event, value) {
                    vm.payOrder($itemScope.order);
                }
            },
            {
                html: '<div class="drop-menu">طباعة طلب الفحص<span class="fa fa-print fa-lg"></span></div>',
                enabled: function () {
                    return true;
                },
                click: function ($itemScope, $event, value) {
                    vm.printPending($itemScope.order);
                }
            },
            {
                html: '<div class="drop-menu">طباعة التشخيص<span class="fa fa-print fa-lg"></span></div>',
                enabled: function () {
                    return true;
                },
                click: function ($itemScope, $event, value) {
                    vm.printDiagnosed($itemScope.order);
                }
            },
            {
                html: '<div class="drop-menu">طباعة تقرير مختصر<span class="fa fa-print fa-lg"></span></div>',
                enabled: function () {
                    return true;
                },
                click: function ($itemScope, $event, value) {
                    ModalProvider.openReportOrderByListModel(vm.selected.orders);
                }
            }
        ];

        $timeout(function () {
            vm.fetchTableData();
            window.componentHandler.upgradeAllRegistered();
        }, 1500);

    }]);