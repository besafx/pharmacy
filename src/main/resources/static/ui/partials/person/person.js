app.controller("personCtrl", ['PersonService', 'ModalProvider', '$scope', '$rootScope', '$state', '$timeout',
    function (PersonService, ModalProvider, $scope, $rootScope, $state, $timeout) {

        $scope.selected = {};

        $scope.fetchTableData = function () {
            PersonService.findAllSummery().then(function (data) {
                $scope.persons = data;
                $scope.setSelected(data[0]);
            });
        };

        $scope.setSelected = function (object) {
            if (object) {
                angular.forEach($scope.persons, function (person) {
                    if (object.id == person.id) {
                        $scope.selected = person;
                        return person.isSelected = true;
                    } else {
                        return person.isSelected = false;
                    }
                });
            }
        };

        $scope.removeRow = function (id) {
            var index = -1;
            var personsArr = eval($scope.persons);
            for (var i = 0; i < personsArr.length; i++) {
                if (personsArr[i].id === id) {
                    index = i;
                    break;
                }
            }
            if (index === -1) {
                alert("Something gone wrong");
            }
            $scope.persons.splice(index, 1);
        };

        $scope.delete = function (person) {
            if (person) {
                $rootScope.showConfirmNotify("حذف البيانات", "هل تود حذف المستخدم فعلاً؟", "error", "fa-trash", function () {
                    PersonService.remove(person.id).then(function () {
                        $scope.removeRow(person.id);
                    });
                });
                return;
            }

            $rootScope.showConfirmNotify("حذف البيانات", "هل تود حذف المستخدم فعلاً؟", "error", "fa-trash", function () {
                PersonService.remove($scope.selected.id).then(function () {
                    $scope.removeRow(person.id);
                });
            });
        };

        $scope.rowMenu = [
            {
                html: '<div class="drop-menu">انشاء مستخدم جديد<span class="fa fa-pencil fa-lg"></span></div>',
                enabled: function () {
                    return true
                },
                click: function ($itemScope, $event, value) {
                    ModalProvider.openPersonCreateModel();
                }
            },
            {
                html: '<div class="drop-menu">تعديل بيانات المستخدم<span class="fa fa-edit fa-lg"></span></div>',
                enabled: function () {
                    return true
                },
                click: function ($itemScope, $event, value) {
                    ModalProvider.openPersonUpdateModel($itemScope.person);
                }
            },
            {
                html: '<div class="drop-menu">حذف المستخدم<span class="fa fa-trash fa-lg"></span></div>',
                enabled: function () {
                    return true
                },
                click: function ($itemScope, $event, value) {
                    $scope.delete($itemScope.person);
                }
            },
            {
                html: '<div class="drop-menu">طباعة تقرير مختصر<span class="fa fa-print fa-lg"></span></div>',
                enabled: function () {
                    return true
                },
                click: function ($itemScope, $event, value) {
                    ModalProvider.openPersonsReportModel($scope.persons);
                }
            }
        ];

        $timeout(function () {
            window.componentHandler.upgradeAllRegistered();
            $scope.fetchTableData();
        }, 1500);

    }]);