app.controller("customerCtrl", ['PersonService', 'ModalProvider', '$scope', '$rootScope', '$state', '$timeout',
    function (PersonService, ModalProvider, $scope, $rootScope, $state, $timeout) {

        $scope.selected = {};

        $scope.fetchTableData = function () {
            PersonService.findByPersonType('Customer').then(function (data) {
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
                    PersonService.remove(customer.id).then(function () {
                        var index = $scope.customers.indexOf(customer);
                        $scope.customers.splice(index, 1);
                        $scope.setSelected($scope.customers[0]);
                    });
                });
                return;
            }

            $rootScope.showConfirmNotify("حذف البيانات", "هل تود حذف العميل فعلاً؟", "error", "fa-trash", function () {
                PersonService.remove($scope.selected.id).then(function () {
                    var index = $scope.customers.indexOf(selected);
                    $scope.customers.splice(index, 1);
                    $scope.setSelected($scope.customers[0]);
                });
            });
        };

        $scope.newCustomer = function () {
            var person = {};
            person.personType = 'Customer';
            ModalProvider.openPersonCreateModel(person).result.then(function (data) {
                $scope.customers.splice(0,0,data);
            }, function () {
                console.info('CustomerCreateModel Closed.');
            });
        };

        $scope.updateCustomer = function (customer) {
            if(customer){
                ModalProvider.openPersonUpdateModel(customer);
            }else {
                ModalProvider.openPersonUpdateModel(selected);
            }
        };

        $scope.rowMenu = [
            {
                html: '<div class="drop-menu">انشاء عميل جديد<span class="fa fa-pencil fa-lg"></span></div>',
                enabled: function () {
                    return true
                },
                click: function ($itemScope, $event, value) {
                    $scope.newCustomer();
                }
            },
            {
                html: '<div class="drop-menu">تعديل بيانات العميل<span class="fa fa-edit fa-lg"></span></div>',
                enabled: function () {
                    return true
                },
                click: function ($itemScope, $event, value) {
                    $scope.updateCustomer($itemScope.customer);
                }
            },
            {
                html: '<div class="drop-menu">حذف العميل<span class="fa fa-trash fa-lg"></span></div>',
                enabled: function () {
                    return true
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