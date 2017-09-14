app.controller("falconCtrl", ['FalconService', 'ModalProvider', '$scope', '$rootScope', '$state', '$timeout',
    function (FalconService, ModalProvider, $scope, $rootScope, $state, $timeout) {

        $scope.selected = {};

        $scope.fetchTableData = function () {
            FalconService.findAll().then(function (data) {
                $scope.falcons = data;
                $scope.setSelected(data[0]);
            });
        };

        $scope.setSelected = function (object) {
            if (object) {
                angular.forEach($scope.falcons, function (falcon) {
                    if (object.id == falcon.id) {
                        $scope.selected = falcon;
                        return falcon.isSelected = true;
                    } else {
                        return falcon.isSelected = false;
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
            }
        ];

        $timeout(function () {
            window.componentHandler.upgradeAllRegistered();
            $scope.fetchTableData();
        }, 1500);

    }]);