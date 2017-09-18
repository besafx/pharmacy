app.controller("orderCtrl", ['OrderService', 'ModalProvider', '$uibModal', '$scope', '$rootScope', '$state', '$timeout',
    function (OrderService, ModalProvider, $uibModal, $scope, $rootScope, $state, $timeout) {

        $scope.selected = {};
        $scope.buffer = {};

        $scope.items = [];
        $scope.items.push(
            {'id': 1, 'type': 'link', 'name': $rootScope.lang === 'AR' ? 'البرامج' : 'Application', 'link': 'menu'},
            {'id': 2, 'type': 'title', 'name': $rootScope.lang === 'AR' ? 'طلبات الفحص' : 'Detection Orders'}
        );

        $scope.setSelected = function (object) {
            if (object) {
                angular.forEach($scope.orders, function (order) {
                    if (object.id == order.id) {
                        $scope.selected = order;
                        return order.isSelected = true;
                    } else {
                        return order.isSelected = false;
                    }
                });
            }
        };

        $scope.findPending = function () {
            OrderService.findPending().then(function (data) {
                $scope.orders = data;
                $scope.setSelected($scope.orders[0]);
                $scope.items = [];
                $scope.items.push(
                    {
                        'id': 1,
                        'type': 'link',
                        'name': $rootScope.lang === 'AR' ? 'البرامج' : 'Application',
                        'link': 'menu'
                    },
                    {'id': 2, 'type': 'title', 'name': $rootScope.lang === 'AR' ? 'طلبات الفحص' : 'Detection Orders'},
                    {'id': 3, 'type': 'title', 'name': $rootScope.lang === 'AR' ? 'تحت التنفيذ' : 'Pending'}
                );
            });
        };

        $scope.openFilter = function () {
            var modalInstance = $uibModal.open({
                animation: true,
                ariaLabelledBy: 'modal-title',
                ariaDescribedBy: 'modal-body',
                templateUrl: '/ui/partials/order/orderFilter.html',
                controller: 'orderFilterCtrl',
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
                if (buffer.orderConditionsList) {
                    var orderConditions = [];
                    for (var i = 0; i < buffer.orderConditionsList.length; i++) {
                        orderConditions.push(buffer.orderConditionsList[i]);
                    }
                    search.push('orderConditions=');
                    search.push(orderConditions);
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
                if (buffer.falconsList) {
                    var falcons = [];
                    for (var i = 0; i < buffer.falconsList.length; i++) {
                        falcons.push(buffer.falconsList[i].id);
                    }
                    search.push('falcons=');
                    search.push(falcons);
                    search.push('&');
                }
                //
                if (buffer.doctorsList) {
                    var doctors = [];
                    for (var i = 0; i < buffer.doctorsList.length; i++) {
                        doctors.push(buffer.doctorsList[i].id);
                    }
                    search.push('doctors=');
                    search.push(doctors);
                    search.push('&');
                }
                //
                OrderService.filter(search.join("")).then(function (data) {
                    $scope.orders = data;
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
                            'name': $rootScope.lang === 'AR' ? 'طلبات الفحص' : 'Detection Orders'
                        },
                        {'id': 3, 'type': 'title', 'name': $rootScope.lang === 'AR' ? 'بحث مخصص' : 'Filter'}
                    );
                });
            }, function () {});
        };

        $scope.delete = function (order) {
            if (order) {
                $rootScope.showConfirmNotify("حذف البيانات", "هل تود حذف الطلب فعلاً؟", "error", "fa-trash", function () {
                    OrderService.remove(order.id).then(function () {
                        var index = $scope.orders.indexOf(order);
                        $scope.orders.splice(index, 1);
                        $scope.setSelected($scope.orders[0]);
                    });
                });
                return;
            }

            $rootScope.showConfirmNotify("حذف البيانات", "هل تود حذف الطلب فعلاً؟", "error", "fa-trash", function () {
                OrderService.remove($scope.selected.id).then(function () {
                    var index = $scope.orders.indexOf($scope.selected);
                    $scope.orders.splice(index, 1);
                    $scope.setSelected($scope.orders[0]);
                });
            });
        };

        $scope.newOrder = function () {
            ModalProvider.openOrderCreateModel().result.then(function (data) {
                $rootScope.showConfirmNotify("الإستقبال", "هل تود طباعة الطلب ؟", "notification", "fa-info", function () {
                    $scope.print(data);
                });
                if($scope.orders){
                    $scope.orders.splice(0, 0, data);
                }
            }, function () {
                console.info('OrderCreateModel Closed.');
            });
        };

        $scope.print = function (order) {
            window.open('/report/order/' + order.id + '/PDF');
        };

        $scope.rowMenu = [
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
                html: '<div class="drop-menu">تعديل بيانات الطلب<span class="fa fa-edit fa-lg"></span></div>',
                enabled: function () {
                    return $rootScope.contains($rootScope.me.team.authorities, ['ROLE_ORDER_UPDATE']);
                },
                click: function ($itemScope, $event, value) {
                    ModalProvider.openOrderUpdateModel($itemScope.order);
                }
            },
            {
                html: '<div class="drop-menu">حذف الطلب<span class="fa fa-trash fa-lg"></span></div>',
                enabled: function () {
                    return $rootScope.contains($rootScope.me.team.authorities, ['ROLE_ORDER_DELETE']);
                },
                click: function ($itemScope, $event, value) {
                    $scope.delete($itemScope.order);
                }
            }
        ];

        $timeout(function () {
            window.componentHandler.upgradeAllRegistered();
            $scope.findPending();
        }, 1500);

    }]);