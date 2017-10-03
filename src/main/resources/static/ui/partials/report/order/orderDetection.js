app.controller('orderDetectionCtrl', ['OrderService', '$scope', '$rootScope', '$timeout', '$log', '$uibModalInstance',
    function (OrderService, $scope, $rootScope, $timeout, $log, $uibModalInstance) {

        $scope.buffer = {};
        $scope.selected = {};

        $scope.setSelected = function (object) {
            if (object) {
                angular.forEach($scope.orders, function (order) {
                    if (object.id == order.id) {
                        $scope.selected = order;
                        return order.isSelected = true;
                    } else {
                        return order.isSelected = false;
                    }
                });
            }
        };

        $scope.search = function () {

            var search = [];

            if ($scope.buffer.codeFrom) {
                search.push('codeFrom=');
                search.push($scope.buffer.codeFrom);
                search.push('&');
            }
            if ($scope.buffer.codeTo) {
                search.push('codeTo=');
                search.push($scope.buffer.codeTo);
                search.push('&');
            }
            if ($scope.buffer.dateTo) {
                search.push('dateTo=');
                search.push($scope.buffer.dateTo.getTime());
                search.push('&');
            }
            if ($scope.buffer.dateFrom) {
                search.push('dateFrom=');
                search.push($scope.buffer.dateFrom.getTime());
                search.push('&');
            }
            //
            if ($scope.buffer.customerName) {
                search.push('customerName=');
                search.push($scope.buffer.customerName);
                search.push('&');
            }
            if ($scope.buffer.customerMobile) {
                search.push('customerMobile=');
                search.push($scope.buffer.customerMobile);
                search.push('&');
            }
            if ($scope.buffer.customerIdentityNumber) {
                search.push('customerIdentityNumber=');
                search.push($scope.buffer.customerIdentityNumber);
                search.push('&');
            }
            //
            if ($scope.buffer.falconCode) {
                search.push('falconCode=');
                search.push($scope.buffer.falconCode);
                search.push('&');
            }
            if ($scope.buffer.falconType) {
                search.push('falconType=');
                search.push($scope.buffer.falconType);
                search.push('&');
            }
            if ($scope.buffer.weightTo) {
                search.push('weightTo=');
                search.push($scope.buffer.weightTo);
                search.push('&');
            }
            if ($scope.buffer.weightFrom) {
                search.push('weightFrom=');
                search.push($scope.buffer.weightFrom);
                search.push('&');
            }
            //
            OrderService.filter(search.join("")).then(function (data) {
                $scope.orders = data;
                $scope.setSelected(data[0]);
            });

        };

        $scope.submit = function () {
            if(!$scope.selected.id){
                $rootScope.showNotify('التقارير', 'فضلاً اختر الطلب المراد طباعته أولاً', 'error', 'fa-ban', 'topCenter');
                return;
            }
            window.open('/report/order/pending/' + $scope.selected.id + '/PDF');
        };

        $scope.cancel = function () {
            $uibModalInstance.dismiss('cancel');
        };

        $timeout(function () {
            window.componentHandler.upgradeAllRegistered();
        }, 1500);

    }]);