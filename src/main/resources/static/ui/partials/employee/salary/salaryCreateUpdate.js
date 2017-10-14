app.controller('salaryCreateUpdateCtrl', ['SalaryService', 'EmployeeService', '$scope', '$rootScope', '$timeout', '$log', '$uibModalInstance', 'title', 'action', 'salary',
        function (SalaryService, EmployeeService, $scope, $rootScope, $timeout, $log, $uibModalInstance, title, action, salary) {

            $scope.salary = salary;

            $scope.title = title;

            $scope.action = action;

            $timeout(function () {
                EmployeeService.findAllCombo().then(function (data) {
                    $scope.employees = data;
                });
            }, 1000);

            $scope.submit = function () {
                switch ($scope.action) {
                    case 'create' :
                        SalaryService.create($scope.salary).then(function (data) {
                            $uibModalInstance.close(data);
                        });
                        break;
                    case 'update' :
                        SalaryService.update($scope.salary).then(function (data) {
                            $scope.salary = data;
                        });
                        break;
                }
            };

            $scope.cancel = function () {
                $uibModalInstance.dismiss('cancel');
            };

        }]);