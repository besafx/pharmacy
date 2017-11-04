app.controller('orderCreateCtrl', ['OrderService', 'OrderDetectionTypeService', 'OrderAttachService', 'CustomerService', 'FalconService', 'DetectionTypeService', 'DoctorService', 'ModalProvider', '$uibModal', '$scope', '$rootScope', '$timeout', '$log', '$uibModalInstance', 'title', 'order',
    function (OrderService, OrderDetectionTypeService, OrderAttachService, CustomerService, FalconService, DetectionTypeService, DoctorService, ModalProvider, $uibModal, $scope, $rootScope, $timeout, $log, $uibModalInstance, title, order) {

        $scope.falcons = [];
        $scope.selectedFalcon = {};
        $scope.order = order;
        $scope.param = {};
        $scope.buffer = {};
        $scope.buffer.selectedDetectionType = {};
        $scope.orderDetectionTypeList = [];
        $scope.wrappers = [];
        $scope.receipt = {};
        $scope.title = title;

        $timeout(function () {
            $scope.refreshDetectionTypes();
            $scope.refreshDoctors();
        }, 2000);

        $scope.setSelectedFalcon = function (object) {
            if (object) {
                angular.forEach($scope.falcons, function (falcon) {
                    if (object.id == falcon.id) {
                        $scope.selectedFalcon = falcon;
                        $scope.order.falcon = falcon;
                        return falcon.isSelected = true;
                    } else {
                        return falcon.isSelected = false;
                    }
                });
            }
        };

        $scope.search = function () {

            var search = [];

            //
            if ($scope.param.customerName) {
                search.push('customerName=');
                search.push($scope.param.customerName);
                search.push('&');
            }
            if ($scope.param.customerMobile) {
                search.push('customerMobile=');
                search.push($scope.param.customerMobile);
                search.push('&');
            }
            if ($scope.param.customerIdentityNumber) {
                search.push('customerIdentityNumber=');
                search.push($scope.param.customerIdentityNumber);
                search.push('&');
            }
            //
            if ($scope.param.falconCode) {
                search.push('falconCode=');
                search.push($scope.param.falconCode);
                search.push('&');
            }
            if ($scope.param.falconType) {
                search.push('falconType=');
                search.push($scope.param.falconType);
                search.push('&');
            }
            if ($scope.param.weightTo) {
                search.push('weightTo=');
                search.push($scope.param.weightTo);
                search.push('&');
            }
            if ($scope.param.weightFrom) {
                search.push('weightFrom=');
                search.push($scope.param.weightFrom);
                search.push('&');
            }
            //
            FalconService.filter(search.join("")).then(function (data) {
                $scope.falcons = data;
                $scope.setSelectedFalcon(data[0]);
            });

        };

        $scope.newFalcon = function () {
            ModalProvider.openFalconCreateModel().result.then(function (data) {
                $scope.falcons.splice(0, 0, data);
            }, function () {
                console.info('FalconCreateModel Closed.');
            });
        };

        $scope.refreshDetectionTypes = function () {
            DetectionTypeService.findAllCombo().then(function (data) {
                $scope.detectionTypes = data;
            });
        };

        $scope.newDetectionType = function () {
            ModalProvider.openDetectionTypeCreateModel().result.then(function (data) {
                $scope.detectionTypes.splice(0, 0, data);
                $scope.buffer.selectedDetectionType = data;
            }, function () {
                console.info('DetectionTypeCreateModel Closed.');
            });
        };

        $scope.calculateCostSum = function () {
            $scope.totalCost = 0;
            $scope.totalCostAfterDiscount = 0;
            if ($scope.orderDetectionTypeList) {
                for (var i = 0; i < $scope.orderDetectionTypeList.length; i++) {
                    var orderDetectionType = $scope.orderDetectionTypeList[i];
                    $scope.totalCost = $scope.totalCost + orderDetectionType.detectionType.cost;
                }
                $scope.totalCostAfterDiscount = $scope.totalCost - (($scope.totalCost * $scope.order.discount) / 100);
            }
        };

        $scope.refreshDoctors = function () {
            DoctorService.findAll().then(function (data) {
                $scope.doctors = data;
                $scope.order.doctor = data[0];
            });
        };

        $scope.newDoctor = function () {
            ModalProvider.openDoctorCreateModel().result.then(function (data) {
                $scope.doctors.splice(0, 0, data);
                $scope.order.doctor = data;
            }, function () {
                console.info('DoctorCreateModel Closed.');
            });
        };

        //////////////////////////File Manager///////////////////////////////////
        $scope.uploadFiles = function () {
            document.getElementById('uploader').click();
        };

        $scope.initFiles = function (files) {

            angular.forEach(files, function (file) {
                var wrapper = {};
                wrapper.src = file;
                wrapper.name = file.name.substr(0, file.name.lastIndexOf('.')) || file.name;
                wrapper.mimeType = file.name.split('.').pop();
                wrapper.size = file.size;
                $scope.wrappers.push(wrapper);
            });

            var modalInstance = $uibModal.open({
                animation: true,
                ariaLabelledBy: 'modal-title',
                ariaDescribedBy: 'modal-body',
                templateUrl: '/ui/partials/order/orderAttachUpload.html',
                controller: 'orderAttachUploadCtrl',
                scope: $scope,
                backdrop: 'static',
                keyboard: false
            });

            modalInstance.result.then(function () {
                angular.forEach($scope.wrappers, function (wrapper) {
                    console.info(wrapper);
                });
            }, function () {
            });

        };
        //////////////////////////File Manager///////////////////////////////////

        //////////////////////////Scan Manager///////////////////////////////////
        $scope.scanToJpg = function () {
            scanner.scan(displayImagesOnPage,
                {
                    "output_settings": [
                        {
                            "type": "return-base64",
                            "format": "jpg"
                        }
                    ]
                }
            );
        };

        function dataURItoBlob(dataURI) {
            // convert base64/URLEncoded data component to raw binary data held in a string
            var byteString;
            if (dataURI.split(',')[0].indexOf('base64') >= 0)
                byteString = atob(dataURI.split(',')[1]);
            else
                byteString = unescape(dataURI.split(',')[1]);

            // separate out the mime component
            var mimeString = dataURI.split(',')[0].split(':')[1].split(';')[0];

            // write the bytes of the string to a typed array
            var ia = new Uint8Array(byteString.length);
            for (var i = 0; i < byteString.length; i++) {
                ia[i] = byteString.charCodeAt(i);
            }

            return new Blob([ia], {type: mimeString});
        }

        /** Processes the scan result */
        function displayImagesOnPage(successful, mesg, response) {
            var scannedImages = scanner.getScannedImages(response, true, false); // returns an array of ScannedImage
            var files = [];
            for (var i = 0; (scannedImages instanceof Array) && i < scannedImages.length; i++) {
                var blob = dataURItoBlob(scannedImages[i].src);
                var file = new File([blob], wrapper.name + '.jpg');
                files.push(file);
            }
            $scope.initFiles(files);
        }
        //////////////////////////Scan Manager///////////////////////////////////

        $scope.addDetectionTypeToList = function () {
            //Add To Table
            var orderDetectionType = {};
            orderDetectionType.detectionType = $scope.buffer.selectedDetectionType;
            $scope.orderDetectionTypeList.push(orderDetectionType);
            $scope.buffer.selectedDetectionType = {};
            $scope.calculateCostSum();
        };

        $scope.removeDetectionTypeFromList = function (index) {
            $scope.orderDetectionTypeList.splice(index, 1);
            $scope.calculateCostSum();
        };

        $scope.submit = function () {
            $scope.order.orderDetectionTypes = $scope.orderDetectionTypeList;
            //
            if($scope.receipt.paymentMethod!=='Later'){
                var orderReceipts = [];
                var orderReceipt = {};
                orderReceipt.receipt = $scope.receipt;
                orderReceipts.push(orderReceipt);
                $scope.order.orderReceipts = orderReceipts;
            }
            //
            OrderService.create($scope.order).then(function (data) {
                //رفع الملفات
                angular.forEach($scope.wrappers, function (wrapper) {
                    console.info($scope.wrappers);
                    OrderAttachService.upload(data, wrapper.name, wrapper.mimeType, wrapper.description, wrapper.src).then(function (data) {

                    });
                });
                $uibModalInstance.close(data);
            });
        };

        $scope.cancel = function () {
            $uibModalInstance.dismiss('cancel');
        };

    }]);