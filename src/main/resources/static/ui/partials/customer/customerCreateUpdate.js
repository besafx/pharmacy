app.controller('customerCreateUpdateCtrl', ['CustomerService', '$scope', '$rootScope', '$timeout', '$log', '$uibModalInstance', 'title', 'action', 'customer',
        function (CustomerService, $scope, $rootScope, $timeout, $log, $uibModalInstance, title, action, customer) {

            $scope.customer = customer;

            $scope.title = title;

            $scope.action = action;

            $scope.submit = function () {
                switch ($scope.action) {
                    case 'create' :
                        CustomerService.create($scope.customer).then(function (data) {
                            $uibModalInstance.close(data);
                        });
                        break;
                    case 'update' :
                        CustomerService.update($scope.customer).then(function (data) {
                            $scope.customer = data;
                        });
                        break;
                }
            };

            $scope.cancel = function () {
                $uibModalInstance.dismiss('cancel');
            };

        }]);