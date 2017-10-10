app.controller('vacationTypeCreateUpdateCtrl', ['VacationTypeService', '$scope', '$rootScope', '$timeout', '$log', '$uibModalInstance', 'title', 'action', 'vacationType',
        function (VacationTypeService, $scope, $rootScope, $timeout, $log, $uibModalInstance, title, action, vacationType) {

            $scope.vacationType = vacationType;

            $scope.title = title;

            $scope.action = action;

            $scope.submit = function () {
                switch ($scope.action) {
                    case 'create' :
                        VacationTypeService.create($scope.vacationType).then(function (data) {
                            $uibModalInstance.close(data);
                        });
                        break;
                    case 'update' :
                        VacationTypeService.update($scope.vacationType).then(function (data) {
                            $scope.vacationType = data;
                        });
                        break;
                }
            };

            $scope.cancel = function () {
                $uibModalInstance.dismiss('cancel');
            };

        }]);