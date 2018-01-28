app.controller('customerFalconCreateUpdateCtrl', ['CustomerService', 'FalconService', '$scope', '$rootScope', '$timeout', '$log', '$uibModalInstance', 'action', 'falcon',
        function (CustomerService, FalconService, $scope, $rootScope, $timeout, $log, $uibModalInstance, action, falcon) {

            $scope.falcon = falcon;

            $scope.action = action;

            $scope.submit = function () {
                switch ($scope.action) {
                    case 'create' :
                        FalconService.create($scope.falcon).then(function (data) {
                            $uibModalInstance.close(data);
                        });
                        break;
                    case 'update' :
                        FalconService.update($scope.falcon).then(function (data) {
                            $scope.falcon = data;
                        });
                        break;
                }
            };

            $scope.cancel = function () {
                $uibModalInstance.dismiss('cancel');
            };

        }]);