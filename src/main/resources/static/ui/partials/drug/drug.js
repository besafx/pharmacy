app.controller("drugCtrl", ['DrugService', 'ModalProvider', '$scope', '$rootScope', '$state', '$timeout',
    function (DrugService, ModalProvider, $scope, $rootScope, $state, $timeout) {

        $scope.selected = {};

        $scope.fetchTableData = function () {
            DrugService.findAll().then(function (data) {
                $scope.drugs = data;
                $scope.setSelected(data[0]);
            });
        };

        $scope.setSelected = function (object) {
            if (object) {
                angular.forEach($scope.drugs, function (drug) {
                    if (object.id == drug.id) {
                        $scope.selected = drug;
                        return drug.isSelected = true;
                    } else {
                        return drug.isSelected = false;
                    }
                });
            }
        };

        $scope.delete = function (drug) {
            if (drug) {
                $rootScope.showConfirmNotify("حذف البيانات", "هل تود حذف الدواء فعلاً؟", "error", "fa-trash", function () {
                    DrugService.remove(drug.id).then(function () {
                        var index = $scope.drugs.indexOf(drug);
                        $scope.drugs.splice(index, 1);
                        $scope.setSelected($scope.drugs[0]);
                    });
                });
                return;
            }

            $rootScope.showConfirmNotify("حذف البيانات", "هل تود حذف الدواء فعلاً؟", "error", "fa-trash", function () {
                DrugService.remove($scope.selected.id).then(function () {
                    var index = $scope.drugs.indexOf(selected);
                    $scope.drugs.splice(index, 1);
                    $scope.setSelected($scope.drugs[0]);
                });
            });
        };

        $scope.newDrug = function () {
            ModalProvider.openDrugCreateModel().result.then(function (data) {
                $scope.drugs.splice(0, 0, data);
            }, function () {
                console.info('DrugCreateModel Closed.');
            });
        };

        $scope.newDrugCategory = function () {
            ModalProvider.openDrugCategoryCreateModel().result.then(function (data) {

            }, function () {
                console.info('DrugCategoryCreateModel Closed.');
            });
        };

        $scope.enable = function () {
            DrugService.enable($scope.selected).then(function (data) {
                $scope.fetchTableData();
            });
        };

        $scope.disable = function () {
            DrugService.disable($scope.selected).then(function (data) {
                $scope.fetchTableData();
            });
        };

        $scope.rowMenu = [
            {
                html: '<div class="drop-menu">انشاء دواء جديد<span class="fa fa-pencil fa-lg"></span></div>',
                enabled: function () {
                    return $rootScope.contains($rootScope.me.team.authorities, ['ROLE_DRUG_CREATE']);
                },
                click: function ($itemScope, $event, value) {
                    $scope.newDrug();
                }
            },
            {
                html: '<div class="drop-menu">تعديل بيانات الدواء<span class="fa fa-edit fa-lg"></span></div>',
                enabled: function () {
                    return $rootScope.contains($rootScope.me.team.authorities, ['ROLE_DRUG_UPDATE']);
                },
                click: function ($itemScope, $event, value) {
                    ModalProvider.openDrugUpdateModel($itemScope.drug);
                }
            },
            {
                html: '<div class="drop-menu">حذف الدواء<span class="fa fa-trash fa-lg"></span></div>',
                enabled: function () {
                    return $rootScope.contains($rootScope.me.team.authorities, ['ROLE_DRUG_DELETE']);
                },
                click: function ($itemScope, $event, value) {
                    $scope.delete($itemScope.drug);
                }
            }
        ];

        $timeout(function () {
            window.componentHandler.upgradeAllRegistered();
            $scope.fetchTableData();
        }, 1500);

    }]);