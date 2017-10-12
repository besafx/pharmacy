app.controller("doctorCtrl", ['DoctorService', 'ModalProvider', '$scope', '$rootScope', '$state', '$timeout', '$location', '$anchorScroll',
    function (DoctorService, ModalProvider, $scope, $rootScope, $state, $timeout, $location, $anchorScroll) {

        $scope.selected = {};

        $scope.fetchTableData = function () {
            DoctorService.findAll().then(function (data) {
                $scope.doctors = data;
                $scope.setSelected(data[0]);
            });
        };

        $scope.setSelected = function (object) {
            if (object) {
                angular.forEach($scope.doctors, function (doctor) {
                    if (object.id == doctor.id) {
                        $scope.selected = doctor;
                        return doctor.isSelected = true;
                    } else {
                        return doctor.isSelected = false;
                    }
                });
            }
        };

        $scope.delete = function (doctor) {
            if (doctor) {
                $rootScope.showConfirmNotify("حذف البيانات", "هل تود حذف الطبيب فعلاً؟", "error", "fa-trash", function () {
                    DoctorService.remove(doctor.id).then(function () {
                        var index = $scope.doctors.indexOf(doctor);
                        $scope.doctors.splice(index, 1);
                        $scope.setSelected($scope.doctors[0]);
                    });
                });
                return;
            }

            $rootScope.showConfirmNotify("حذف البيانات", "هل تود حذف الطبيب فعلاً؟", "error", "fa-trash", function () {
                DoctorService.remove($scope.selected.id).then(function () {
                    var index = $scope.doctors.indexOf($scope.selected);
                    $scope.doctors.splice(index, 1);
                    $scope.setSelected($scope.doctors[0]);
                });
            });
        };

        $scope.newDoctor = function () {
            ModalProvider.openDoctorCreateModel().result.then(function (data) {
                $scope.doctors.splice(0,0,data);
            }, function () {
                console.info('DoctorCreateModel Closed.');
            });
        };

        $scope.enable = function () {
            DoctorService.enable($scope.selected).then(function (data) {
                $scope.fetchTableData();
            });
        };

        $scope.disable = function () {
            DoctorService.disable($scope.selected).then(function (data) {
                $scope.fetchTableData();
            });
        };


        $scope.rowMenu = [
            {
                html: '<div class="drop-menu">انشاء طبيب جديد<span class="fa fa-pencil fa-lg"></span></div>',
                enabled: function () {
                    return $rootScope.contains($rootScope.me.team.authorities, ['ROLE_DOCTOR_CREATE']);
                },
                click: function ($itemScope, $event, value) {
                    $scope.newDoctor();
                }
            },
            {
                html: '<div class="drop-menu">تعديل بيانات الطبيب<span class="fa fa-edit fa-lg"></span></div>',
                enabled: function () {
                    return $rootScope.contains($rootScope.me.team.authorities, ['ROLE_DOCTOR_UPDATE']);
                },
                click: function ($itemScope, $event, value) {
                    ModalProvider.openDoctorUpdateModel($itemScope.doctor);
                }
            },
            {
                html: '<div class="drop-menu">حذف الطبيب<span class="fa fa-trash fa-lg"></span></div>',
                enabled: function () {
                    return $rootScope.contains($rootScope.me.team.authorities, ['ROLE_DOCTOR_DELETE']);
                },
                click: function ($itemScope, $event, value) {
                    $scope.delete($itemScope.doctor);
                }
            }
        ];

        $timeout(function () {
            $scope.fetchTableData();
            $location.hash('doctorMenu');
            $anchorScroll();
            window.componentHandler.upgradeAllRegistered();
        }, 1500);

    }]);