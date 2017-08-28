app.controller('falconCreateUpdateCtrl', ['CustomerService', 'FalconService', '$scope', '$rootScope', '$timeout', '$log', '$uibModalInstance', 'title', 'action', 'falcon',
        function (CustomerService, FalconService, $scope, $rootScope, $timeout, $log, $uibModalInstance, title, action, falcon) {

            $timeout(function () {
                CustomerService.findAllCombo().then(function (data) {
                    $scope.customers = data;
                });
            }, 2000);

            $scope.falcon = falcon;

            $scope.title = title;

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