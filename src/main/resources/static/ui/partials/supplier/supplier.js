app.controller("supplierCtrl", ['SupplierService', 'ModalProvider', '$scope', '$rootScope', '$state', '$timeout', '$uibModal',
    function (SupplierService, ModalProvider, $scope, $rootScope, $state, $timeout, $uibModal) {

        $scope.selected = {};

        $scope.fetchTableData = function () {
            SupplierService.findAll().then(function (data) {
                $scope.suppliers = data;
                $scope.setSelected(data[0]);
            });
        };

        $scope.setSelected = function (object) {
            if (object) {
                angular.forEach($scope.suppliers, function (supplier) {
                    if (object.id == supplier.id) {
                        $scope.selected = supplier;
                        return supplier.isSelected = true;
                    } else {
                        return supplier.isSelected = false;
                    }
                });
            }
        };

        $scope.delete = function (supplier) {
            if (supplier) {
                $rootScope.showConfirmNotify("حذف البيانات", "هل تود حذف المورد وكل ما يتعلق به من حسابات فعلاً؟", "error", "fa-trash", function () {
                    SupplierService.remove(supplier.id).then(function () {
                        var index = $scope.suppliers.indexOf(supplier);
                        $scope.suppliers.splice(index, 1);
                        $scope.setSelected($scope.suppliers[0]);
                    });
                });
                return;
            }

            $rootScope.showConfirmNotify("حذف البيانات", "هل تود حذف المورد وكل ما يتعلق به من حسابات فعلاً؟", "error", "fa-trash", function () {
                SupplierService.remove($scope.selected.id).then(function () {
                    var index = $scope.suppliers.indexOf($scope.selected);
                    $scope.suppliers.splice(index, 1);
                    $scope.setSelected($scope.suppliers[0]);
                });
            });
        };

        $scope.newSupplier = function () {
            ModalProvider.openSupplierCreateModel().result.then(function (data) {
                $scope.suppliers.splice(0, 0, data);
            }, function () {
                console.info('SupplierCreateModel Closed.');
            });
        };

        $scope.enable = function () {
            SupplierService.enable($scope.selected).then(function (data) {
                $scope.fetchTableData();
            });
        };

        $scope.disable = function () {
            SupplierService.disable($scope.selected).then(function (data) {
                $scope.fetchTableData();
            });
        };

        $scope.rowMenu = [
            {
                html: '<div class="drop-menu">انشاء مورد جديد<span class="fa fa-pencil fa-lg"></span></div>',
                enabled: function () {
                    return $rootScope.contains($rootScope.me.team.authorities, ['ROLE_SUPPLIER_CREATE']);
                },
                click: function ($itemScope, $event, value) {
                    $scope.newSupplier();
                }
            },
            {
                html: '<div class="drop-menu">تعديل بيانات المورد<span class="fa fa-edit fa-lg"></span></div>',
                enabled: function () {
                    return $rootScope.contains($rootScope.me.team.authorities, ['ROLE_SUPPLIER_UPDATE']);
                },
                click: function ($itemScope, $event, value) {
                    ModalProvider.openSupplierUpdateModel($itemScope.supplier);
                }
            },
            {
                html: '<div class="drop-menu">حذف المورد<span class="fa fa-trash fa-lg"></span></div>',
                enabled: function () {
                    return $rootScope.contains($rootScope.me.team.authorities, ['ROLE_SUPPLIER_DELETE']);
                },
                click: function ($itemScope, $event, value) {
                    $scope.delete($itemScope.supplier);
                }
            }
        ];

        $timeout(function () {
            $scope.fetchTableData();
            window.componentHandler.upgradeAllRegistered();
        }, 1500);

    }]);