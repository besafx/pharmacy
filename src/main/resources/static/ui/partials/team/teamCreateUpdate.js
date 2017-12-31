app.controller('teamCreateUpdateCtrl', ['TeamService', 'TeamRuleService', '$scope', '$rootScope', '$timeout', '$log', '$uibModalInstance', 'title', 'action', 'team',
    function (TeamService, TeamRuleService, $scope, $rootScope, $timeout, $log, $uibModalInstance, title, action, team) {

        $timeout(function () {
            TeamRuleService.findAll().then(function (data) {

                $scope.roles = data;

                if (team) {
                    $scope.team = team;
                    if (typeof team.authorities === 'string') {
                        $scope.team.authorities = team.authorities.split(',');
                    }
                    //
                    angular.forEach($scope.team.authorities, function (auth) {
                        angular.forEach($scope.roles, function (role) {
                            if (role.value === auth) {
                                return role.selected = true;
                            }
                        })
                    });
                } else {
                    $scope.team = {};
                }

            });
        }, 600);

        $scope.title = title;

        $scope.action = action;

        $scope.submit = function () {
            $scope.team.authorities = [];
            angular.forEach($scope.roles, function (role) {
                if (role.selected) {
                    $scope.team.authorities.push(role.value);
                }
            });
            $scope.team.authorities = $scope.team.authorities.join();
            switch ($scope.action) {
                case 'create' :
                    TeamService.create($scope.team).then(function (data) {
                        $uibModalInstance.close(data);
                    });
                    break;
                case 'update' :
                    TeamService.update($scope.team).then(function (data) {
                        $scope.team = data;
                        $scope.team.authorities = team.authorities.split(',');
                    });
                    break;
            }
        };

        $scope.cancel = function () {
            $uibModalInstance.dismiss('cancel');
        };

    }]);