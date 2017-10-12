app.controller('deductionTypeCreateUpdateCtrl', ['DeductionTypeService', '$scope', '$rootScope', '$timeout', '$log', '$uibModalInstance', 'title', 'action', 'deductionType',
        function (DeductionTypeService, $scope, $rootScope, $timeout, $log, $uibModalInstance, title, action, deductionType) {

            $scope.deductionType = deductionType;

            $scope.title = title;

            $scope.action = action;

            $scope.submit = function () {
                switch ($scope.action) {
                    case 'create' :
                        DeductionTypeService.create($scope.deductionType).then(function (data) {
                            $uibModalInstance.close(data);
                        });
                        break;
                    case 'update' :
                        DeductionTypeService.update($scope.deductionType).then(function (data) {
                            $scope.deductionType = data;
                        });
                        break;
                }
            };

            $scope.cancel = function () {
                $uibModalInstance.dismiss('cancel');
            };

        }]);