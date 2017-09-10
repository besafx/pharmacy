app.controller('orderAttachUploadCtrl', ['$scope', '$rootScope', '$timeout', '$log', '$uibModalInstance', 'title',
    function ($scope, $rootScope, $timeout, $log, $uibModalInstance, title) {

        $scope.modalTitle = title;

        $scope.submit = function () {
            $uibModalInstance.close();
        };

        $scope.cancel = function () {
            $uibModalInstance.dismiss('cancel');
        };

        $timeout(function () {
            window.componentHandler.upgradeAllRegistered();
        }, 1500);

    }]);