app.controller("diagnosisCtrl", ['OrderService', 'DiagnosisService', 'OrderDetectionTypeService', 'OrderAttachService', 'ModalProvider', '$uibModal', '$scope', '$rootScope', '$state', '$timeout',
    function (OrderService, DiagnosisService, OrderDetectionTypeService, OrderAttachService, ModalProvider, $uibModal, $scope, $rootScope, $state, $timeout) {

        $scope.buffer = {};
        $scope.wrappers = [];

        $scope.items = [];
        $scope.items.push(
            {'id': 1, 'type': 'link', 'name': $rootScope.lang === 'AR' ? 'البرامج' : 'Application', 'link': 'menu'},
            {'id': 2, 'type': 'title', 'name': $rootScope.lang === 'AR' ? 'نتائج الفحص' : 'Detection Results'}
        );

        $scope.refreshOrder = function (order) {
            OrderService.findOne(order.id).then(function (data) {
                return order = data;
            });
        };

        $scope.findOrdersByFalcon = function (falcon, code) {
            OrderService.findByFalconAndCodeNot(falcon.id, code).then(function (data) {
                return falcon.orders = data;
            });
        };

        $scope.findOrdersByCustomer = function (customer, code) {
            OrderService.findByFalconCustomerAndCodeNot(customer.id, code).then(function (data) {
                return customer.orders = data;
            });
        };

        $scope.findOrdersByToday = function () {
            OrderService.findByToday().then(function (data) {
                $scope.orders = data;
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
                    {'id': 3, 'type': 'title', 'name': $rootScope.lang === 'AR' ? 'طلبات اليوم' : 'Orders For Today'}
                );
                $timeout(function () {
                    window.componentHandler.upgradeAllRegistered();
                }, 500);
            });
        };

        $scope.findOrdersByWeek = function () {
            OrderService.findByWeek().then(function (data) {
                $scope.orders = data;
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
                    {'id': 3, 'type': 'title', 'name': $rootScope.lang === 'AR' ? 'طلبات الاسبوع' : 'Orders For Week'}
                );
                $timeout(function () {
                    window.componentHandler.upgradeAllRegistered();
                }, 500);
            });
        };

        $scope.findOrdersByMonth = function () {
            OrderService.findByMonth().then(function (data) {
                $scope.orders = data;
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
                    {'id': 3, 'type': 'title', 'name': $rootScope.lang === 'AR' ? 'طلبات الشهر' : 'Orders For Month'}
                );
                $timeout(function () {
                    window.componentHandler.upgradeAllRegistered();
                }, 500);
            });
        };

        $scope.findOrdersByYear = function () {
            OrderService.findByYear().then(function (data) {
                $scope.orders = data;
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
                    {'id': 3, 'type': 'title', 'name': $rootScope.lang === 'AR' ? 'طلبات العام' : 'Orders For Year'}
                );
                $timeout(function () {
                    window.componentHandler.upgradeAllRegistered();
                }, 500);
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
                if (buffer.customerName) {
                    search.push('customerName=');
                    search.push(buffer.customerName);
                    search.push('&');
                }
                if (buffer.customerMobile) {
                    search.push('customerMobile=');
                    search.push(buffer.customerMobile);
                    search.push('&');
                }
                if (buffer.customerIdentityNumber) {
                    search.push('customerIdentityNumber=');
                    search.push(buffer.customerIdentityNumber);
                    search.push('&');
                }
                //
                if (buffer.falconCode) {
                    search.push('falconCode=');
                    search.push(buffer.falconCode);
                    search.push('&');
                }
                if (buffer.falconType) {
                    search.push('falconType=');
                    search.push(buffer.falconType);
                    search.push('&');
                }
                if (buffer.weightTo) {
                    search.push('weightTo=');
                    search.push(buffer.weightTo);
                    search.push('&');
                }
                if (buffer.weightFrom) {
                    search.push('weightFrom=');
                    search.push(buffer.weightFrom);
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
                            'name': $rootScope.lang === 'AR' ? 'نتائج الفحص' : 'Detection Results'
                        },
                        {'id': 3, 'type': 'title', 'name': $rootScope.lang === 'AR' ? 'بحث مخصص' : 'Filter'}
                    );
                });
            }, function () {
            });
        };

        $scope.deleteOrderDetectionType = function (orderDetectionType, order) {
            $rootScope.showConfirmNotify("العيادة", "هل تود حذف خدمة الفحص فعلاً؟", "error", "fa-trash", function () {
                OrderDetectionTypeService.remove(orderDetectionType.id).then(function () {
                    var index = order.orderDetectionTypes.indexOf(orderDetectionType);
                    return order.orderDetectionTypes.splice(index, 1);
                });
            });
        };

        $scope.deleteDiagnosis = function (diagnosis, order) {
            if (diagnosis) {
                $rootScope.showConfirmNotify("العيادة الطبية", "هل تود حذف العلاج فعلاً؟", "error", "fa-trash", function () {
                    DiagnosisService.remove(diagnosis.id).then(function () {
                        var index = order.diagnoses.indexOf(diagnosis);
                        return order.diagnoses.splice(index, 1);
                    });
                });

            }
        };

        $scope.newOrderDetectionType = function (order) {
            ModalProvider.openOrderDetectionTypeCreateModel(order).result.then(function (data) {
                return order.orderDetectionTypes.splice(0, 0, data);
            }, function () {
                console.info('OrderDetectionTypeCreateModel Closed.');
            });
        };

        $scope.newDiagnosis = function (order) {
            ModalProvider.openDiagnosisCreateModel(order).result.then(function (data) {
                Array.prototype.splice.apply(order.diagnoses, [1, 0].concat(data));
            }, function () {
                console.info('DiagnosisCreateModel Closed.');
            });
        };

        $scope.saveOrderNote = function (order) {
            if ($rootScope.contains($rootScope.me.team.authorities, ['ROLE_ORDER_SAVE_NOTE'])) {
                OrderService.saveNote(order, order.note);
            }
        };

        $scope.saveOrderDetectionTypeCase = function (orderDetectionType) {
            if ($rootScope.contains($rootScope.me.team.authorities, ['ROLE_ORDER_DETECTION_TYPE_SAVE_CASE'])) {
                OrderDetectionTypeService.saveOrderDetectionTypeCase(orderDetectionType, orderDetectionType.done);
            }
        };

        $scope.refreshOrderDetectionTypeByOrder = function (order) {
            OrderDetectionTypeService.findByOrder(order).then(function (data) {
                order.orderDetectionTypes = data;
                $timeout(function () {
                    window.componentHandler.upgradeAllRegistered();
                }, 500);
            });
        };

        $scope.refreshDiagnosesByOrder = function (order) {
            DiagnosisService.findByOrderId(order.id).then(function (data) {
                order.diagnosis = data;
            });
        };

        $timeout(function () {
            $scope.findOrdersByWeek();
        }, 1500);

    }]);