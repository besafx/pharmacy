app.controller('vacationCreateUpdateCtrl', ['VacationService', 'EmployeeService', 'VacationTypeService', '$scope', '$rootScope', '$timeout', '$log', '$uibModalInstance', 'title', 'action', 'vacation',
        function (VacationService, EmployeeService, VacationTypeService, $scope, $rootScope, $timeout, $log, $uibModalInstance, title, action, vacation) {

            $scope.vacation = vacation;

            $scope.title = title;

            $scope.action = action;

            $timeout(function () {
                EmployeeService.findAllCombo().then(function (data) {
                    $scope.employees = data;
                });
                VacationTypeService.findAll().then(function (data) {
                    $scope.vacationTypes = data;
                });
            }, 1000);

            $scope.submit = function () {
                switch ($scope.action) {
                    case 'create' :
                        VacationService.create($scope.vacation).then(function (data) {
                            $uibModalInstance.close(data);
                        });
                        break;
                    case 'update' :
                        VacationService.update($scope.vacation).then(function (data) {
                            $scope.vacation = data;
                        });
                        break;
                }
            };

            $scope.cancel = function () {
                $uibModalInstance.dismiss('cancel');
            };

        }]);