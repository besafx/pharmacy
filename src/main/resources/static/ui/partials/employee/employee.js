app.controller("employeeCtrl", ['EmployeeService', 'VacationTypeService', 'VacationService', 'ModalProvider', '$rootScope', '$state', '$timeout', '$location', '$anchorScroll',
    function (EmployeeService, VacationTypeService, VacationService, ModalProvider, $rootScope, $state, $timeout, $location, $anchorScroll) {

        var vm = this;

        vm.selected = {};
        vm.selectedVacationType = {};
        vm.selectedVacation = {};
        vm.selectedDeductionType = {};

        vm.employees = [];
        vm.vacationTypes = [];
        vm.vacations = [];
        vm.deductionTypes = [];

        vm.fetchTableData = function () {
            EmployeeService.findAll().then(function (data) {
                vm.employees = data;
                vm.setSelected(data[0]);
            });
        };

        vm.fetchVacationTypeData = function () {
            VacationTypeService.findAll().then(function (data) {
                vm.vacationTypes = data;
                vm.setSelectedVacationType(data[0]);
            });
        };

        vm.fetchVacationData = function () {
            VacationService.findAll().then(function (data) {
                vm.vacations = data;
                vm.setSelectedVacation(data[0]);
            });
        };

        vm.fetchDeductionTypeData = function () {
            DeductionTypeService.findAll().then(function (data) {
                vm.vacationTypes = data;
                vm.setSelectedDeductionType(data[0]);
            });
        };

        vm.setSelected = function (object) {
            if (object) {
                angular.forEach(vm.employees, function (employee) {
                    if (object.id == employee.id) {
                        vm.selected = employee;
                        return employee.isSelected = true;
                    } else {
                        return employee.isSelected = false;
                    }
                });
            }
        };

        vm.setSelectedVacationType = function (object) {
            if (object) {
                angular.forEach(vm.vacationTypes, function (vacationType) {
                    if (object.id == vacationType.id) {
                        vm.selectedVacationType = vacationType;
                        return vacationType.isSelected = true;
                    } else {
                        return vacationType.isSelected = false;
                    }
                });
            }
        };

        vm.setSelectedVacation = function (object) {
            if (object) {
                angular.forEach(vm.vacations, function (vacation) {
                    if (object.id == vacation.id) {
                        vm.selectedVacation = vacation;
                        return vacation.isSelected = true;
                    } else {
                        return vacation.isSelected = false;
                    }
                });
            }
        };

        vm.setSelectedDeductionType = function (object) {
            if (object) {
                angular.forEach(vm.deductionTypes, function (deductionType) {
                    if (object.id == deductionType.id) {
                        vm.selectedDeductionType = deductionType;
                        return deductionType.isSelected = true;
                    } else {
                        return deductionType.isSelected = false;
                    }
                });
            }
        };

        vm.delete = function (employee) {
            if (employee) {
                $rootScope.showConfirmNotify("حذف البيانات", "هل تود حذف الموظف فعلاً؟", "error", "fa-trash", function () {
                    EmployeeService.remove(employee.id).then(function () {
                        var index = vm.employees.indexOf(employee);
                        vm.employees.splice(index, 1);
                        vm.setSelected(vm.employees[0]);
                    });
                });
                return;
            }

            $rootScope.showConfirmNotify("حذف البيانات", "هل تود حذف الموظف فعلاً؟", "error", "fa-trash", function () {
                EmployeeService.remove(vm.selected.id).then(function () {
                    var index = vm.employees.indexOf(vm.selected);
                    vm.employees.splice(index, 1);
                    vm.setSelected(vm.employees[0]);
                });
            });
        };

        vm.deleteVacationType = function (vacationType) {
            if (vacationType) {
                $rootScope.showConfirmNotify("حذف البيانات", "هل تود حذف البند فعلاً؟", "error", "fa-trash", function () {
                    VacationTypeService.remove(vacationType.id).then(function () {
                        var index = vm.vacationTypes.indexOf(vacationType);
                        vm.vacationTypes.splice(index, 1);
                        vm.setSelected(vm.vacationTypes[0]);
                    });
                });
                return;
            }

            $rootScope.showConfirmNotify("حذف البيانات", "هل تود حذف البند فعلاً؟", "error", "fa-trash", function () {
                VacationTypeService.remove(vm.selected.id).then(function () {
                    var index = vm.vacationTypes.indexOf(vm.selected);
                    vm.vacationTypes.splice(index, 1);
                    vm.setSelected(vm.vacationTypes[0]);
                });
            });
        };

        vm.deleteVacation = function (vacation) {
            if (vacation) {
                $rootScope.showConfirmNotify("حذف البيانات", "هل تود حذف الاجازة فعلاً؟", "error", "fa-trash", function () {
                    VacationService.remove(vacation.id).then(function () {
                        var index = vm.vacations.indexOf(vacation);
                        vm.vacations.splice(index, 1);
                        vm.setSelected(vm.vacations[0]);
                    });
                });
                return;
            }

            $rootScope.showConfirmNotify("حذف البيانات", "هل تود حذف الاجازة فعلاً؟", "error", "fa-trash", function () {
                VacationService.remove(vm.selected.id).then(function () {
                    var index = vm.vacations.indexOf(vm.selected);
                    vm.vacations.splice(index, 1);
                    vm.setSelected(vm.vacations[0]);
                });
            });
        };

        vm.deleteDeductionType = function (deductionType) {
            if (deductionType) {
                $rootScope.showConfirmNotify("حذف البيانات", "هل تود حذف البند فعلاً؟", "error", "fa-trash", function () {
                    DeductionTypeService.remove(deductionType.id).then(function () {
                        var index = vm.deductionTypes.indexOf(deductionType);
                        vm.deductionTypes.splice(index, 1);
                        vm.setSelected(vm.deductionTypes[0]);
                    });
                });
                return;
            }

            $rootScope.showConfirmNotify("حذف البيانات", "هل تود حذف البند فعلاً؟", "error", "fa-trash", function () {
                DeductionTypeService.remove(vm.selected.id).then(function () {
                    var index = vm.deductionTypes.indexOf(vm.selected);
                    vm.deductionTypes.splice(index, 1);
                    vm.setSelected(vm.deductionTypes[0]);
                });
            });
        };

        vm.newEmployee = function () {
            ModalProvider.openEmployeeCreateModel().result.then(function (data) {
                vm.employees.splice(0,0,data);
            }, function () {
                console.info('EmployeeCreateModel Closed.');
            });
        };

        vm.newVacationType = function () {
            ModalProvider.openVacationTypeCreateModel().result.then(function (data) {
                vm.vacationTypes.splice(0,0,data);
            }, function () {
                console.info('VacationTypeCreateModel Closed.');
            });
        };

        vm.newVacation = function () {
            ModalProvider.openVacationCreateModel().result.then(function (data) {
                vm.vacations.splice(0,0,data);
            }, function () {
                console.info('VacationCreateModel Closed.');
            });
        };

        vm.newDeductionType = function () {
            ModalProvider.openDeductionTypeCreateModel().result.then(function (data) {
                vm.deductionTypes.splice(0,0,data);
            }, function () {
                console.info('DeductionTypeCreateModel Closed.');
            });
        };

        vm.enable = function () {
            EmployeeService.enable(vm.selected).then(function (data) {
                var index = vm.employees.indexOf(vm.selected);
                vm.employees[index] = data;
            });
        };

        vm.disable = function () {
            EmployeeService.disable(vm.selected).then(function (data) {
                var index = vm.employees.indexOf(vm.selected);
                vm.employees[index] = data;
            });
        };

        vm.rowMenu = [
            {
                html: '<div class="drop-menu">انشاء موظف جديد<span class="fa fa-pencil fa-lg"></span></div>',
                enabled: function () {
                    return $rootScope.contains($rootScope.me.team.authorities, ['ROLE_EMPLOYEE_CREATE']);
                },
                click: function ($itemScope, $event, value) {
                    vm.newEmployee();
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
                    vm.delete($itemScope.employee);
                }
            }
        ];

        vm.rowMenuVacationType = [
            {
                html: '<div class="drop-menu">انشاء بند جديد<span class="fa fa-pencil fa-lg"></span></div>',
                enabled: function () {
                    return $rootScope.contains($rootScope.me.team.authorities, ['ROLE_VACATION_TYPE_CREATE']);
                },
                click: function ($itemScope, $event, value) {
                    vm.newVacationType();
                }
            },
            {
                html: '<div class="drop-menu">تعديل بيانات البند<span class="fa fa-edit fa-lg"></span></div>',
                enabled: function () {
                    return $rootScope.contains($rootScope.me.team.authorities, ['ROLE_VACATION_TYPE_UPDATE']);
                },
                click: function ($itemScope, $event, value) {
                    ModalProvider.openVacationTypeUpdateModel($itemScope.vacationType);
                }
            },
            {
                html: '<div class="drop-menu">حذف البند<span class="fa fa-trash fa-lg"></span></div>',
                enabled: function () {
                    return $rootScope.contains($rootScope.me.team.authorities, ['ROLE_VACATION_TYPE_DELETE']);
                },
                click: function ($itemScope, $event, value) {
                    vm.deleteVacationType($itemScope.vacationType);
                }
            }
        ];

        vm.rowMenuVacation = [
            {
                html: '<div class="drop-menu">انشاء اجازة جديدة<span class="fa fa-pencil fa-lg"></span></div>',
                enabled: function () {
                    return $rootScope.contains($rootScope.me.team.authorities, ['ROLE_VACATION_CREATE']);
                },
                click: function ($itemScope, $event, value) {
                    vm.newVacation();
                }
            },
            {
                html: '<div class="drop-menu">تعديل بيانات الاجازة<span class="fa fa-edit fa-lg"></span></div>',
                enabled: function () {
                    return $rootScope.contains($rootScope.me.team.authorities, ['ROLE_VACATION_UPDATE']);
                },
                click: function ($itemScope, $event, value) {
                    ModalProvider.openVacationUpdateModel($itemScope.vacation);
                }
            },
            {
                html: '<div class="drop-menu">حذف الاجازة<span class="fa fa-trash fa-lg"></span></div>',
                enabled: function () {
                    return $rootScope.contains($rootScope.me.team.authorities, ['ROLE_VACATION_DELETE']);
                },
                click: function ($itemScope, $event, value) {
                    vm.deleteVacation($itemScope.vacation);
                }
            }
        ];

        vm.rowMenuDeductionType = [
            {
                html: '<div class="drop-menu">انشاء بند جديد<span class="fa fa-pencil fa-lg"></span></div>',
                enabled: function () {
                    return $rootScope.contains($rootScope.me.team.authorities, ['ROLE_DEDUCTION_TYPE_CREATE']);
                },
                click: function ($itemScope, $event, value) {
                    vm.newDeductionType();
                }
            },
            {
                html: '<div class="drop-menu">تعديل بيانات البند<span class="fa fa-edit fa-lg"></span></div>',
                enabled: function () {
                    return $rootScope.contains($rootScope.me.team.authorities, ['ROLE_DEDUCTION_TYPE_UPDATE']);
                },
                click: function ($itemScope, $event, value) {
                    ModalProvider.openDeductionTypeUpdateModel($itemScope.vacationType);
                }
            },
            {
                html: '<div class="drop-menu">حذف البند<span class="fa fa-trash fa-lg"></span></div>',
                enabled: function () {
                    return $rootScope.contains($rootScope.me.team.authorities, ['ROLE_DEDUCTION_TYPE_DELETE']);
                },
                click: function ($itemScope, $event, value) {
                    vm.deleteDeductionType($itemScope.vacationType);
                }
            }
        ];

        $timeout(function () {
            $location.hash('employeeMenu');
            $anchorScroll();
            window.componentHandler.upgradeAllRegistered();
        }, 1500);

    }]);