app.controller("customerCtrl", ['CustomerService', 'ModalProvider', '$scope', '$rootScope', '$state', '$timeout',
    function (CustomerService, ModalProvider, $scope, $rootScope, $state, $timeout) {

        $scope.selected = {};

        $scope.fetchTableData = function () {
            CustomerService.findAll().then(function (data) {
                $scope.customers = data;
                $scope.setSelected(data[0]);
            });
        };

        $scope.setSelected = function (object) {
            if (object) {
                angular.forEach($scope.customers, function (customer) {
                    if (object.id == customer.id) {
                        $scope.selected = customer;
                        return customer.isSelected = true;
                    } else {
                        return customer.isSelected = false;
                    }
                });
            }
        };

        $scope.delete = function (customer) {
            if (customer) {
                $rootScope.showConfirmNotify("حذف البيانات", "هل تود حذف العميل فعلاً؟", "error", "fa-trash", function () {
                    CustomerService.remove(customer.id).then(function () {
                        var index = $scope.customers.indexOf(customer);
                        $scope.customers.splice(index, 1);
                        $scope.setSelected($scope.customers[0]);
                    });
                });
                return;
            }

            $rootScope.showConfirmNotify("حذف البيانات", "هل تود حذف العميل فعلاً؟", "error", "fa-trash", function () {
                CustomerService.remove($scope.selected.id).then(function () {
                    var index = $scope.customers.indexOf(selected);
                    $scope.customers.splice(index, 1);
                    $scope.setSelected($scope.customers[0]);
                });
            });
        };

        $scope.newCustomer = function () {
            ModalProvider.openCustomerCreateModel().result.then(function (data) {
                $scope.customers.splice(0, 0, data);
            }, function () {
                console.info('CustomerCreateModel Closed.');
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
            }
        ];

        $timeout(function () {
            window.componentHandler.upgradeAllRegistered();
            $scope.fetchTableData();
        }, 1500);

    }]);