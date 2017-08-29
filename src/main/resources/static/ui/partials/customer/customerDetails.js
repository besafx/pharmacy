app.controller('customerDetailsCtrl', ['CustomerService', 'FalconService', 'ModalProvider', '$scope', '$rootScope', '$timeout', '$log', '$uibModalInstance', '$uibModal', 'customer',
    function (CustomerService, FalconService, ModalProvider, $scope, $rootScope, $timeout, $log, $uibModalInstance, $uibModal, customer) {

        $scope.customer = customer;

        $scope.refreshCustomer = function () {
            CustomerService.findOne($scope.customer.id).then(function (data) {
                $scope.customer = data;
            })
        };

        $scope.refreshFalcons = function () {
            FalconService.findByCustomer($scope.customer).then(function (data) {
                $scope.customer.falcons = data;
            });
        };

        $scope.newFalcon = function (customer) {
            $rootScope.showConfirmNotify("العمليات على قواعد البيانات", "هل تود ربط حساب صقر جديد بالعميل؟", "information", "fa-database", function () {
                $uibModal.open({
                    animation: true,
                    ariaLabelledBy: 'modal-title',
                    ariaDescribedBy: 'modal-body',
                    templateUrl: '/ui/partials/customer/customerFalconCreateUpdate.html',
                    controller: 'customerFalconCreateUpdateCtrl',
                    backdrop: 'static',
                    keyboard: false,
                    resolve: {
                        title: function () {
                            return $rootScope.lang === 'AR' ? 'انشاء حساب صقر جديد' : 'New Falcon Account';
                        },
                        action: function () {
                            return 'create';
                        },
                        falcon: function () {
                            var falcon = {};
                            falcon.customer = customer;
                            return falcon;
                        }
                    }
                }).result.then(function (data) {
                    ///////////////////////// TO DO ///////////////////////////////
                    console.info(data);
                }, function () {
                    console.info('CustomerFalconCreateModel Closed.');
                });
            });
        };

        $scope.cancel = function () {
            $uibModalInstance.dismiss('cancel');
        };

        $timeout(function () {
            window.componentHandler.upgradeAllRegistered();
        }, 1500);

    }]);