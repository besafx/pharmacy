app.controller('deductionCreateUpdateCtrl', ['DeductionService', 'EmployeeService', 'DeductionTypeService', '$scope', '$rootScope', '$timeout', '$log', '$uibModalInstance', 'title', 'action', 'deduction',
        function (DeductionService, EmployeeService, DeductionTypeService, $scope, $rootScope, $timeout, $log, $uibModalInstance, title, action, deduction) {

            $scope.deduction = deduction;

            $scope.title = title;

            $scope.action = action;

            $timeout(function () {
                EmployeeService.findAllCombo().then(function (data) {
                    $scope.employees = data;
                });
                DeductionTypeService.findAll().then(function (data) {
                    $scope.deductionTypes = data;
                });
            }, 1000);

            $scope.submit = function () {
                switch ($scope.action) {
                    case 'create' :
                        DeductionService.create($scope.deduction).then(function (data) {
                            $uibModalInstance.close(data);
                        });
                        break;
                    case 'update' :
                        DeductionService.update($scope.deduction).then(function (data) {
                            $scope.deduction = data;
                        });
                        break;
                }
            };

            $scope.cancel = function () {
                $uibModalInstance.dismiss('cancel');
            };

        }]);