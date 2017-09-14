app.controller("employeeCtrl", ['EmployeeService', 'ModalProvider', '$scope', '$rootScope', '$state', '$timeout',
    function (EmployeeService, ModalProvider, $scope, $rootScope, $state, $timeout) {

        $scope.selected = {};

        $scope.fetchTableData = function () {
            EmployeeService.findAll().then(function (data) {
                $scope.employees = data;
                $scope.setSelected(data[0]);
            });
        };

        $scope.setSelected = function (object) {
            if (object) {
                angular.forEach($scope.employees, function (employee) {
                    if (object.id == employee.id) {
                        $scope.selected = employee;
                        return employee.isSelected = true;
                    } else {
                        return employee.isSelected = false;
                    }
                });
            }
        };

        $scope.delete = function (employee) {
            if (employee) {
                $rootScope.showConfirmNotify("حذف البيانات", "هل تود حذف الموظف فعلاً؟", "error", "fa-trash", function () {
                    EmployeeService.remove(employee.id).then(function () {
                        var index = $scope.employees.indexOf(employee);
                        $scope.employees.splice(index, 1);
                        $scope.setSelected($scope.employees[0]);
                    });
                });
                return;
            }

            $rootScope.showConfirmNotify("حذف البيانات", "هل تود حذف الموظف فعلاً؟", "error", "fa-trash", function () {
                EmployeeService.remove($scope.selected.id).then(function () {
                    var index = $scope.employees.indexOf($scope.selected);
                    $scope.employees.splice(index, 1);
                    $scope.setSelected($scope.employees[0]);
                });
            });
        };

        $scope.newEmployee = function () {
            ModalProvider.openEmployeeCreateModel().result.then(function (data) {
                $scope.employees.splice(0,0,data);
            }, function () {
                console.info('EmployeeCreateModel Closed.');
            });
        };

        $scope.enable = function () {
            EmployeeService.enable($scope.selected).then(function (data) {
                var index = $scope.employees.indexOf($scope.selected);
                $scope.employees[index] = data;
            });
        };

        $scope.disable = function () {
            EmployeeService.disable($scope.selected).then(function (data) {
                var index = $scope.employees.indexOf($scope.selected);
                $scope.employees[index] = data;
            });
        };

        $scope.rowMenu = [
            {
                html: '<div class="drop-menu">انشاء موظف جديد<span class="fa fa-pencil fa-lg"></span></div>',
                enabled: function () {
                    return $rootScope.contains($rootScope.me.team.authorities, ['ROLE_EMPLOYEE_CREATE']);
                },
                click: function ($itemScope, $event, value) {
                    $scope.newEmployee();
                }
            },
            {
                html: '<div class="drop-menu">تعديل بيانات الموظف<span class="fa fa-edit fa-lg"></span></div>',
                enabled: function () {
                    return $rootScope.contains($rootScope.me.team.authorities, ['ROLE_EMPLOYEE_UPDATE']);
                },
                click: function ($itemScope, $event, value) {
                    ModalProvider.openEmployeeUpdateModel($itemScope.employee);
                }
            },
            {
                html: '<div class="drop-menu">حذف الموظف<span class="fa fa-trash fa-lg"></span></div>',
                enabled: function () {
                    return $rootScope.contains($rootScope.me.team.authorities, ['ROLE_EMPLOYEE_DELETE']);
                },
                click: function ($itemScope, $event, value) {
                    $scope.delete($itemScope.employee);
                }
            }
        ];

        $timeout(function () {
            window.componentHandler.upgradeAllRegistered();
            $scope.fetchTableData();
        }, 1500);

    }]);