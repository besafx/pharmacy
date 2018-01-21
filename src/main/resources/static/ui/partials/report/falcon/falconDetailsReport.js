app.controller('falconDetailsReportCtrl', ['FalconService', '$scope', '$rootScope', '$timeout', '$uibModalInstance',
    function (FalconService, $scope, $rootScope, $timeout, $uibModalInstance) {

        $scope.buffer = {};

        $timeout(function () {
            FalconService.findAllCombo().then(function (data) {
                $scope.falcons = data;
            });
        }, 2000);

        $scope.submit = function () {
            var ids = [];
            ids.push($scope.buffer.falcon.id);
            window.open('/report/falcons?ids=' + ids + '&exportType=PDF');
        };

        $scope.cancel = function () {
            $uibModalInstance.dismiss('cancel');
        };
    }]);