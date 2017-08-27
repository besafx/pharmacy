app.controller('employeeCreateUpdateCtrl', ['TeamService' ,'EmployeeService', '$scope', '$rootScope', '$timeout', '$log', '$uibModalInstance', 'title', 'action', 'employee',
        function (TeamService, EmployeeService, $scope, $rootScope, $timeout, $log, $uibModalInstance, title, action, employee) {

            $timeout(function () {
                TeamService.findAllCombo().then(function (data) {
                    $scope.teams = data;
                });
            }, 2000);

            $scope.employee = employee;

            $scope.title = title;

            $scope.action = action;

            $scope.submit = function () {
                switch ($scope.action) {
                    case 'create' :
                        EmployeeService.create($scope.employee).then(function (data) {
                            $uibModalInstance.close(data);
                        });
                        break;
                    case 'update' :
                        EmployeeService.update($scope.employee).then(function (data) {
                            $scope.employee = data;
                        });
                        break;
                }
            };

            $scope.cancel = function () {
                $uibModalInstance.dismiss('cancel');
            };

        }]);