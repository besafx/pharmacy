app.controller("detectionTypeCtrl", ['DetectionTypeService', 'ModalProvider', '$scope', '$rootScope', '$state', '$timeout',
    function (DetectionTypeService, ModalProvider, $scope, $rootScope, $state, $timeout) {

        $scope.selected = {};

        $scope.fetchTableData = function () {
            DetectionTypeService.findAll().then(function (data) {
                $scope.detectionTypes = data;
                $scope.setSelected(data[0]);
            });
        };

        $scope.setSelected = function (object) {
            if (object) {
                angular.forEach($scope.detectionTypes, function (detectionType) {
                    if (object.id == detectionType.id) {
                        $scope.selected = detectionType;
                        return detectionType.isSelected = true;
                    } else {
                        return detectionType.isSelected = false;
                    }
                });
            }
        };

        $scope.delete = function (detectionType) {
            if (detectionType) {
                $rootScope.showConfirmNotify("حذف البيانات", "هل تود حذف النوع فعلاً؟", "error", "fa-trash", function () {
                    DetectionTypeService.remove(detectionType.id).then(function () {
                        var index = $scope.detectionTypes.indexOf(detectionType);
                        $scope.detectionTypes.splice(index, 1);
                        $scope.setSelected($scope.detectionTypes[0]);
                    });
                });
                return;
            }

            $rootScope.showConfirmNotify("حذف البيانات", "هل تود حذف النوع فعلاً؟", "error", "fa-trash", function () {
                DetectionTypeService.remove($scope.selected.id).then(function () {
                    var index = $scope.detectionTypes.indexOf($scope.selected);
                    $scope.detectionTypes.splice(index, 1);
                    $scope.setSelected($scope.detectionTypes[0]);
                });
            });
        };

        $scope.newDetectionType = function () {
            ModalProvider.openDetectionTypeCreateModel().result.then(function (data) {
                $scope.detectionTypes.splice(0, 0, data);
            }, function () {
                console.info('DetectionTypeCreateModel Closed.');
            });
        };

        $scope.enable = function () {
            DetectionTypeService.enable($scope.selected).then(function (data) {
                $scope.fetchTableData();
            });
        };

        $scope.disable = function () {
            DetectionTypeService.disable($scope.selected).then(function (data) {
                $scope.fetchTableData();
            });
        };

        $scope.rowMenu = [
            {
                html: '<div class="drop-menu">انشاء نوع جديد<span class="fa fa-pencil fa-lg"></span></div>',
                enabled: function () {
                    return $rootScope.contains($rootScope.me.team.authorities, ['ROLE_DETECTION_TYPE_CREATE']);
                },
                click: function ($itemScope, $event, value) {
                    $scope.newDetectionType();
                }
            },
            {
                html: '<div class="drop-menu">تعديل بيانات النوع<span class="fa fa-edit fa-lg"></span></div>',
                enabled: function () {
                    return $rootScope.contains($rootScope.me.team.authorities, ['ROLE_DETECTION_TYPE_UPDATE']);
                },
                click: function ($itemScope, $event, value) {
                    ModalProvider.openDetectionTypeUpdateModel($itemScope.detectionType);
                }
            },
            {
                html: '<div class="drop-menu">حذف النوع<span class="fa fa-trash fa-lg"></span></div>',
                enabled: function () {
                    return $rootScope.contains($rootScope.me.team.authorities, ['ROLE_DETECTION_TYPE_DELETE']);
                },
                click: function ($itemScope, $event, value) {
                    $scope.delete($itemScope.detectionType);
                }
            }
        ];

        $timeout(function () {
            window.componentHandler.upgradeAllRegistered();
            $scope.fetchTableData();
        }, 1500);

    }]);