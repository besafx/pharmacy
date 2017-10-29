app.controller('falconCreateUpdateCtrl', ['CustomerService', 'FalconService', 'ModalProvider', '$scope', '$rootScope', '$timeout', '$log', '$uibModalInstance', 'title', 'action', 'falcon',
        function (CustomerService, FalconService, ModalProvider, $scope, $rootScope, $timeout, $log, $uibModalInstance, title, action, falcon) {

            $scope.customers = [];

            $timeout(function () {
                CustomerService.findAllCombo().then(function (data) {
                    $scope.customers = data;
                });
            }, 2000);

            $scope.falcon = falcon;

            $scope.title = title;

            $scope.action = action;

            $scope.newCustomer = function () {
                ModalProvider.openCustomerCreateModel().result.then(function (data) {
                    $scope.customers.splice(0, 0, data);
                    $scope.falcon.customer = data;
                }, function () {
                    console.info('CustomerCreateModel Closed.');
                });
            };

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