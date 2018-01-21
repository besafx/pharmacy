app.controller('detectionTypeDetailsReportCtrl', ['DetectionTypeService', '$scope', '$rootScope', '$timeout', '$uibModalInstance',
    function (DetectionTypeService, $scope, $rootScope, $timeout, $uibModalInstance) {

        $scope.buffer = {};

        $timeout(function () {
            DetectionTypeService.findAllCombo().then(function (data) {
                $scope.detectionTypes = data;
            });
        }, 600);

        $scope.submit = function () {
            window.open('/report/detectionType/details/' + $scope.buffer.detectionType.id + '?exportType=' + 'PDF');
        };

        $scope.cancel = function () {
            $uibModalInstance.dismiss('cancel');
        };
    }]);