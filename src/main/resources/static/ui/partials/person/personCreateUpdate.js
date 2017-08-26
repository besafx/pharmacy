app.controller('personCreateUpdateCtrl', ['TeamService', 'PersonService', '$scope', '$rootScope', '$timeout', '$log', '$uibModalInstance', 'title', 'action', 'person',
        function (TeamService, PersonService, $scope, $rootScope, $timeout, $log, $uibModalInstance, title, action, person) {

            $timeout(function () {
                TeamService.findAllCombo().then(function (data) {
                    $scope.teams = data;
                });
            }, 2000);

            $scope.person = person;

            $scope.title = title;

            $scope.action = action;

            $scope.submit = function () {
                switch ($scope.action) {
                    case 'create' :
                        PersonService.create($scope.person).then(function (data) {
                            $uibModalInstance.close(data);
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