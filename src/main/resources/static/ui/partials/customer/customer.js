app.controller("customerCtrl", ['CustomerService', 'OrderService', 'ModalProvider', '$scope', '$rootScope', '$state', '$timeout', '$uibModal',
    function (CustomerService, OrderService, ModalProvider, $scope, $rootScope, $state, $timeout, $uibModal) {

        $scope.selected = {};
        $scope.selectedOrder = {};

        $scope.fetchTableData = function () {
            CustomerService.findAll().then(function (data) {
                $scope.customers = data;
                $scope.setSelected(data[0]);
            });
        };

        $scope.refreshOrdersByCustomer = function () {
            OrderService.findByCustomer($scope.selected.id).then(function (data) {
                $scope.selected.orders = data;
                $scope.setSelectedOrder(data[0]);
            });
        };

        $scope.setSelected = function (object) {
            if (object) {
                angular.forEach($scope.customers, function (customer) {
                    if (object.id == customer.id) {
                        $scope.selected = customer;
                        $scope.refreshOrdersByCustomer();
                        return customer.isSelected = true;
                    } else {
                        return customer.isSelected = false;
                    }
                });
            }
        };

        $scope.setSelectedOrder = function (object) {
            if (object) {
                angular.forEach($scope.selected.orders, function (order) {
                    if (object.id == order.id) {
                        $scope.selectedOrder = order;
                        return order.isSelected = true;
                    } else {
                        return order.isSelected = false;
                    }
                });
            }
        };

        $scope.delete = function (customer) {
            if (customer) {
                $rootScope.showConfirmNotify("حذف البيانات", "هل تود حذف العميل وكل ما يتعلق به من حسابات فعلاً؟", "error", "fa-trash", function () {
                    CustomerService.remove(customer.id).then(function () {
                        var index = $scope.customers.indexOf(customer);
                        $scope.customers.splice(index, 1);
                        $scope.setSelected($scope.customers[0]);
                    });
                });
                return;
            }

            $rootScope.showConfirmNotify("حذف البيانات", "هل تود حذف العميل وكل ما يتعلق به من حسابات فعلاً؟", "error", "fa-trash", function () {
                CustomerService.remove($scope.selected.id).then(function () {
                    var index = $scope.customers.indexOf($scope.selected);
                    $scope.customers.splice(index, 1);
                    $scope.setSelected($scope.customers[0]);
                });
            });
        };

        $scope.newCustomer = function () {
            ModalProvider.openCustomerCreateModel().result.then(function (data) {
                $scope.customers.splice(0, 0, data);
                $scope.newFalcon(data);
            }, function () {
                console.info('CustomerCreateModel Closed.');
            });
        };

        $scope.newFalcon = function (customer) {
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

        $scope.enable = function () {
            CustomerService.enable($scope.selected).then(function (data) {
                $scope.fetchTableData();
            });
        };

        $scope.disable = function () {
            CustomerService.disable($scope.selected).then(function (data) {
                $scope.fetchTableData();
            });
        };

        $scope.rowMenu = [
            {
                html: '<div class="drop-menu">انشاء عميل جديد<span class="fa fa-pencil fa-lg"></span></div>',
                enabled: function () {
                    return $rootScope.contains($rootScope.me.team.authorities, ['ROLE_CUSTOMER_CREATE']);
                },
                click: function ($itemScope, $event, value) {
                    $scope.newCustomer();
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
                html: '<div class="drop-menu">حذف العميل<span class="fa fa-trash fa-lg"></span></div>',
                enabled: function () {
                    return $rootScope.contains($rootScope.me.team.authorities, ['ROLE_CUSTOMER_DELETE']);
                },
                click: function ($itemScope, $event, value) {
                    $scope.delete($itemScope.customer);
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

        $scope.printPending = function (order) {
            window.open('/report/order/pending/' + order.id + '/PDF');
        };

        $scope.printDiagnosed = function (order) {
            window.open('/report/order/diagnosed/' + order.id + '/PDF');
        };

        $scope.newOrder = function () {
            ModalProvider.openOrderCreateModel().result.then(function (data) {
                $rootScope.showConfirmNotify("طلبات الفحص", "هل تود طباعة الطلب ؟", "notification", "fa-info", function () {
                    $scope.printPending(data);
                });
                $scope.selected.orders.splice(0, 0, data);
            }, function () {
                console.info('OrderCreateModel Closed.');
            });
        };

        $scope.deleteOrder = function (order) {
            $rootScope.showConfirmNotify("طلبات الفحص", "هل تود حذف الطلب فعلاً؟", "error", "fa-trash", function () {
                OrderService.remove(order.id).then(function () {
                    var index = $scope.selected.orders.indexOf(order);
                    $scope.selected.orders.splice(index, 1);
                    $scope.setSelected($scope.selected.orders[0]);
                });
            });
        };

        $scope.payOrder = function (order) {
            $rootScope.showConfirmNotify("طلبات الفحص", "هل تود تسديد مستحقات طلب الفحص فعلاً؟", "warning", "fa-money", function () {
                OrderService.pay(order.id).then(function (data) {
                    var index = $scope.selected.orders.indexOf(order);
                    $scope.selected.orders[index].paymentMethod = data.paymentMethod;
                });
            });
        };

        $scope.rowMenuOrder = [
            {
                html: '<div class="drop-menu">انشاء طلب جديد<span class="fa fa-pencil fa-lg"></span></div>',
                enabled: function () {
                    return $rootScope.contains($rootScope.me.team.authorities, ['ROLE_ORDER_CREATE']);
                },
                click: function ($itemScope, $event, value) {
                    $scope.newOrder();
                }
            },
            {
                html: '<div class="drop-menu">حذف الطلب<span class="fa fa-trash fa-lg"></span></div>',
                enabled: function () {
                    return $rootScope.contains($rootScope.me.team.authorities, ['ROLE_ORDER_DELETE']);
                },
                click: function ($itemScope, $event, value) {
                    $scope.deleteOrder($itemScope.order);
                }
            },
            {
                html: '<div class="drop-menu">تسديد مستحقات طلب الفحص<span class="fa fa-money fa-lg"></span></div>',
                enabled: function ($itemScope) {
                    return $itemScope.order.paymentMethod==='Later';
                },
                click: function ($itemScope, $event, value) {
                    $scope.payOrder($itemScope.order);
                }
            },
            {
                html: '<div class="drop-menu">طباعة طلب الفحص<span class="fa fa-print fa-lg"></span></div>',
                enabled: function () {
                    return true;
                },
                click: function ($itemScope, $event, value) {
                    $scope.printPending($itemScope.order);
                }
            },
            {
                html: '<div class="drop-menu">طباعة التشخيص<span class="fa fa-print fa-lg"></span></div>',
                enabled: function () {
                    return true;
                },
                click: function ($itemScope, $event, value) {
                    $scope.printDiagnosed($itemScope.order);
                }
            },
            {
                html: '<div class="drop-menu">طباعة تقرير مختصر<span class="fa fa-print fa-lg"></span></div>',
                enabled: function () {
                    return true;
                },
                click: function ($itemScope, $event, value) {
                    ModalProvider.openReportOrderByListModel($scope.selected.orders);
                }
            }
        ];

        $timeout(function () {
            window.componentHandler.upgradeAllRegistered();
            $scope.fetchTableData();
        }, 1500);

    }]);