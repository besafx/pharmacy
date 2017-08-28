app.controller('detectionTypeCreateUpdateCtrl', ['DetectionTypeService', '$scope', '$rootScope', '$timeout', '$log', '$uibModalInstance', 'title', 'action', 'detectionType',
        function (DetectionTypeService, $scope, $rootScope, $timeout, $log, $uibModalInstance, title, action, detectionType) {

            $scope.detectionType = detectionType;

            $scope.title = title;

            $scope.action = action;

            $scope.submit = function () {
                switch ($scope.action) {
                    case 'create' :
                        DetectionTypeService.create($scope.detectionType).then(function (data) {
                            $uibModalInstance.close(data);
                        });
                        break;
                    case 'update' :
                        DetectionTypeService.update($scope.detectionType).then(function (data) {
                            $scope.detectionType = data;
                        });
                        break;
                }
            };

            $scope.cancel = function () {
                $uibModalInstance.dismiss('cancel');
            };

        }]);