app.controller("falconCtrl", ['FalconService', 'OrderService', 'ModalProvider', '$scope', '$rootScope', '$state', '$timeout',
    function (FalconService, OrderService, ModalProvider, $scope, $rootScope, $state, $timeout) {

        $scope.falcons = [];
        $scope.param = {};

        $scope.search = function () {

            var search = [];

            //
            if ($scope.param.customerName) {
                search.push('customerName=');
                search.push($scope.param.customerName);
                search.push('&');
            }
            if ($scope.param.customerMobile) {
                search.push('customerMobile=');
                search.push($scope.param.customerMobile);
                search.push('&');
            }
            if ($scope.param.customerIdentityNumber) {
                search.push('customerIdentityNumber=');
                search.push($scope.param.customerIdentityNumber);
                search.push('&');
            }
            //
            if ($scope.param.falconCode) {
                search.push('falconCode=');
                search.push($scope.param.falconCode);
                search.push('&');
            }
            if ($scope.param.falconType) {
                search.push('falconType=');
                search.push($scope.param.falconType);
                search.push('&');
            }
            if ($scope.param.weightTo) {
                search.push('weightTo=');
                search.push($scope.param.weightTo);
                search.push('&');
            }
            if ($scope.param.weightFrom) {
                search.push('weightFrom=');
                search.push($scope.param.weightFrom);
                search.push('&');
            }
            //
            FalconService.filter(search.join("")).then(function (data) {
                $scope.falcons = data;
            });

        };

        $scope.delete = function (falcon) {
            if (falcon) {
                $rootScope.showConfirmNotify("حذف البيانات", "هل تود حذف الحساب فعلاً؟", "error", "fa-trash", function () {
                    FalconService.remove(falcon.id).then(function () {
                        var index = $scope.falcons.indexOf(falcon);
                        $scope.falcons.splice(index, 1);
                    });
                });
                return;
            }

            $rootScope.showConfirmNotify("حذف البيانات", "هل تود حذف الحساب فعلاً؟", "error", "fa-trash", function () {
                FalconService.remove($scope.selected.id).then(function () {
                    var index = $scope.falcons.indexOf($scope.selected);
                    $scope.falcons.splice(index, 1);
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

            });
        };

        $scope.disable = function () {
            FalconService.disable($scope.selected).then(function (data) {

            });
        };

        $scope.print = function () {
            var ids = [];
            angular.forEach($scope.falcons, function (falcon) {
                if(falcon.isSelected===true){
                    ids.push(falcon.id);
                }
            });
            window.open('/report/falcons?ids=' + ids + '&exportType=PDF');
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

        $timeout(function () {
            window.componentHandler.upgradeAllRegistered();
        }, 1500);

    }]);