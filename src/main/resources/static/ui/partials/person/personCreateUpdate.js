app.controller('personCreateUpdateCtrl',
    ['TeamService', 'PersonService', '$scope', '$rootScope', '$timeout', '$log', '$uibModalInstance', 'title', 'action', 'person',
        function (TeamService, PersonService, $scope, $rootScope, $timeout, $log, $uibModalInstance, title, action, person) {

            $timeout(function () {
                TeamService.findAllSummery().then(function (data) {
                    $scope.teams = data;
                });
            }, 2000);

            if (person) {
                $scope.person = person;
            } else {
                $scope.person = {};
            }

            $scope.title = title;

            $scope.action = action;

            $scope.submit = function () {
                switch ($scope.action) {
                    case 'create' :
                        PersonService.create($scope.person).then(function (data) {
                            $scope.person = {};
                            $scope.from.$setPristine();
                        });
                        break;
                    case 'update' :
                        PersonService.update($scope.person).then(function (data) {
                            $scope.person = data;
                        });
                        break;
                }
            };

            $scope.cancel = function () {
                $uibModalInstance.dismiss('cancel');
            };

        }]);