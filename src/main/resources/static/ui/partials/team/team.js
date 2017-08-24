app.controller("teamCtrl", ['TeamService', 'ModalProvider', '$rootScope', '$scope', '$timeout', '$state',
    function (TeamService, ModalProvider, $rootScope, $scope, $timeout, $state) {

        $scope.selected = {};

        $scope.fetchTableData = function () {
            TeamService.findAll().then(function (data) {
                $scope.teams = data;
                $scope.setSelected(data[0]);
            });
        };

        $scope.setSelected = function (object) {
            if (object) {
                angular.forEach($scope.teams, function (team) {
                    if (object.id == team.id) {
                        $scope.selected = team;
                        return team.isSelected = true;
                    } else {
                        return team.isSelected = false;
                    }
                });
            }
        };

        $scope.delete = function (team) {
            if (team) {
                $rootScope.showConfirmNotify("حذف البيانات", "هل تود حذف المجموعة فعلاً؟", "error", "fa-trash", function () {
                    TeamService.remove(team.id).then(function () {

                    });
                });
                return;
            }
            $rootScope.showConfirmNotify("حذف البيانات", "هل تود حذف المجموعة فعلاً؟", "error", "fa-trash", function () {
                TeamService.remove($scope.selected.id).then(function () {

                });
            });
        };

        $scope.rowMenu = [
            {
                html: '<div class="drop-menu">انشاء مجموعة جديدة<span class="fa fa-pencil fa-lg"></span></div>',
                enabled: function () {
                    return true
                },
                click: function ($itemScope, $event, value) {
                    ModalProvider.openTeamCreateModel();
                }
            },
            {
                html: '<div class="drop-menu">تعديل بيانات المجموعة<span class="fa fa-edit fa-lg"></span></div>',
                enabled: function () {
                    return true
                },
                click: function ($itemScope, $event, value) {
                    ModalProvider.openTeamUpdateModel($itemScope.team);
                }
            },
            {
                html: '<div class="drop-menu">حذف المجموعة<span class="fa fa-trash fa-lg"></span></div>',
                enabled: function () {
                    return true
                },
                click: function ($itemScope, $event, value) {
                    $scope.delete($itemScope.team);
                }
            }
        ];

        $timeout(function () {
            window.componentHandler.upgradeAllRegistered();
            $scope.fetchTableData();
        }, 1500);

    }]);
