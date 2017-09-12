app.controller('drugHeavyWorkCtrl', ['$scope', '$rootScope', '$timeout', '$uibModalInstance', 'FileUploader',
    function ($scope, $rootScope, $timeout, $uibModalInstance, FileUploader) {

        $scope.buffer = {};

        $scope.downloadExcelFile = function () {
            window.open('/api/heavy-work/drug/write/' + $scope.buffer.rowCount);
        };

        $scope.uploadExcelFile = function () {
            document.getElementById('input').click();
        };

        $scope.cancel = function () {
            $uibModalInstance.dismiss('cancel');
        };

        var uploader = $scope.uploader = new FileUploader({
            url: '/api/heavy-work/drug/read/'
        });
        uploader.filters.push({
            name: 'syncFilter',
            fn: function (item, options) {
                return this.queue.length < 10;
            }
        });
        uploader.filters.push({
            name: 'asyncFilter',
            fn: function (item, options, deferred) {
                setTimeout(deferred.resolve, 1e3);
            }
        });
        uploader.onAfterAddingFile = function (fileItem) {
            uploader.uploadAll();
        };
        uploader.onSuccessItem = function (fileItem, response, status, headers) {

        };

    }]);