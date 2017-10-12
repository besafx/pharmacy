app.controller("falconCtrl", ['FalconService', 'OrderService', 'ModalProvider', '$scope', '$rootScope', '$state', '$timeout', '$location', '$anchorScroll',
    function (FalconService, OrderService, ModalProvider, $scope, $rootScope, $state, $timeout, $location, $anchorScroll) {

        $scope.selected = {};
        $scope.selectedOrder = {};

        $scope.fetchTableData = function () {
            FalconService.findAll().then(function (data) {
                $scope.falcons = data;
                $scope.setSelected(data[0]);
            });
        };

        $scope.refreshOrdersByFalcon = function () {
            OrderService.findByFalcon($scope.selected.id).then(function (data) {
                $scope.selected.orders = data;
                $scope.setSelectedOrder(data[0]);
            });
        };

        $scope.setSelected = function (object) {
            if (object) {
                angular.forEach($scope.falcons, function (falcon) {
                    if (object.id == falcon.id) {
                        $scope.selected = falcon;
                        $scope.refreshOrdersByFalcon();
                        return falcon.isSelected = true;
                    } else {
                        return falcon.isSelected = false;
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

        $scope.delete = function (falcon) {
            if (falcon) {
                $rootScope.showConfirmNotify("حذف البيانات", "هل تود حذف الحساب فعلاً؟", "error", "fa-trash", function () {
                    FalconService.remove(falcon.id).then(function () {
                        var index = $scope.falcons.indexOf(falcon);
                        $scope.falcons.splice(index, 1);
                        $scope.setSelected($scope.falcons[0]);
                    });
                });
                return;
            }

            $rootScope.showConfirmNotify("حذف البيانات", "هل تود حذف الحساب فعلاً؟", "error", "fa-trash", function () {
                FalconService.remove($scope.selected.id).then(function () {
                    var index = $scope.falcons.indexOf($scope.selected);
                    $scope.falcons.splice(index, 1);
                    $scope.setSelected($scope.falcons[0]);
                });
            });
        };

        $scope.newFalcon = function () {
            ModalProvider.openFalconCreateModel().result.then(function (data) {
                $scope.falcons.splice(0, 0, data);
            }, function () {
                console.info('FalconCreateModel Closed.');
            });
        };

        $scope.enable = function () {
            FalconService.enable($scope.selected).then(function (data) {
                $scope.fetchTableData();
            });
        };

        $scope.disable = function () {
            FalconService.disable($scope.selected).then(function (data) {
                $scope.fetchTableData();
            });
        };

        $scope.rowMenu = [
            {
                html: '<div class="drop-menu">انشاء حساب جديد<span class="fa fa-pencil fa-lg"></span></div>',
                enabled: function () {
                    return $rootScope.contains($rootScope.me.team.authorities, ['ROLE_FALCON_CREATE']);
                },
                click: function ($itemScope, $event, value) {
                    $scope.newFalcon();
                }
            },
            {
                html: '<div class="drop-menu">تعديل بيانات الحساب<span class="fa fa-edit fa-lg"></span></div>',
                enabled: function () {
                    return $rootScope.contains($rootScope.me.team.authorities, ['ROLE_FALCON_UPDATE']);
                },
                click: function ($itemScope, $event, value) {
                    ModalProvider.openFalconUpdateModel($itemScope.falcon);
                }
            },
            {
                html: '<div class="drop-menu">حذف الحساب<span class="fa fa-trash fa-lg"></span></div>',
                enabled: function () {
                    return $rootScope.contains($rootScope.me.team.authorities, ['ROLE_FALCON_DELETE']);
                },
                click: function ($itemScope, $event, value) {
                    $scope.delete($itemScope.falcon);
                }
            },
            {
                html: '<div class="drop-menu">التفاصيل<span class="fa fa-info fa-lg"></span></div>',
                enabled: function () {
                    return true;
                },
                click: function ($itemScope, $event, value) {
                    ModalProvider.openFalconDetailsModel($itemScope.falcon);
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
            $scope.fetchTableData();
            $location.hash('falconMenu');
            $anchorScroll();
            window.componentHandler.upgradeAllRegistered();
        }, 1500);

    }]);