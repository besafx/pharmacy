app.controller('customerDetailsCtrl', ['CustomerService', 'FalconService', 'OrderService', 'ModalProvider', '$scope', '$rootScope', '$timeout', '$log', '$uibModalInstance', '$uibModal', 'customer',
    function (CustomerService, FalconService, OrderService, ModalProvider, $scope, $rootScope, $timeout, $log, $uibModalInstance, $uibModal, customer) {

        $scope.customer = customer;

        $scope.refreshCustomer = function () {
            CustomerService.findOneDetails($scope.customer.id).then(function (data) {
                $scope.customer = data;
            })
        };

        $scope.refreshFalcons = function () {
            FalconService.findByCustomer($scope.customer).then(function (data) {
                $scope.customer.falcons = data;
            });
        };

        $scope.refreshOrders = function () {
            OrderService.findByCustomer($scope.customer.id).then(function (data) {
                $scope.customer.orders = data;
            });
        };

        $scope.newFalcon = function (customer) {
            $rootScope.showConfirmNotify("العمليات على قواعد البيانات", "هل تود ربط حساب صقر جديد بالعميل؟", "information", "fa-database", function () {
                ModalProvider.openFalconCreateByCustomerModel(customer).result.then(function (data) {
                    return customer.falcons.push(data);
                }, function () {});
            });
        };

        $scope.cancel = function () {
            $uibModalInstance.dismiss('cancel');
        };

        $timeout(function () {
            $scope.refreshCustomer();
            window.componentHandler.upgradeAllRegistered();
        }, 600);

    }]);