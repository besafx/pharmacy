app.controller('orderCreateUpdateCtrl', ['OrderService', 'CustomerService', 'FalconService', 'DetectionTypeService', 'DoctorService', 'ModalProvider', '$uibModal', '$scope', '$rootScope', '$timeout', '$log', '$uibModalInstance', 'title', 'action', 'order',
    function (OrderService, CustomerService, FalconService, DetectionTypeService, DoctorService, ModalProvider, $uibModal, $scope, $rootScope, $timeout, $log, $uibModalInstance, title, action, order) {

        $timeout(function () {
            $scope.refreshCustomers();
            $scope.refreshDetectionTypes();
            $scope.refreshDoctors();
        }, 2000);

        $scope.order = order;

        $scope.buffer = {};

        $scope.buffer.detectionTypeList = [];

        $scope.wrappers = [];

        $scope.title = title;

        $scope.action = action;

        $scope.refreshCustomers = function () {
            CustomerService.findAll().then(function (data) {
                $scope.customers = data;
            })
        };

        $scope.newCustomer = function () {
            ModalProvider.openCustomerCreateModel().result.then(function (data) {
                $scope.customers.splice(0, 0, data);
            }, function () {
                console.info('CustomerCreateModel Closed.');
            });
        };

        $scope.refreshFalconsByCustomer = function () {
            FalconService.findByCustomer($scope.buffer.customer).then(function (data) {
                $scope.buffer.customer.falcons = data;
            });
        };

        $scope.newFalcon = function () {
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
                        falcon.customer = $scope.buffer.customer;
                        return falcon;
                    }
                }
            }).result.then(function (data) {
                $scope.buffer.customer.falcons.splice(0, 0, data);
            }, function () {
                console.info('CustomerFalconCreateModel Closed.');
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
            }, function () {
                console.info('DetectionTypeCreateModel Closed.');
            });
        };

        $scope.calculateCostSum = function () {
            $scope.total = 0;
            if ($scope.buffer.detectionTypeList) {
                for (var i = 0; i < $scope.buffer.detectionTypeList.length; i++) {
                    var detectionType = $scope.buffer.detectionTypeList[i];
                    $scope.total = $scope.total + detectionType.cost;
                }
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
            }, function () {
                console.info('DoctorCreateModel Closed.');
            });
        };

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
                keyboard: false,
                resolve: {
                    title: function () {
                        return 'رفع المستندات';
                    }
                }
            });

            modalInstance.result.then(function () {
                angular.forEach($scope.wrappers, function (wrapper) {
                    console.info(wrapper);
                });
            }, function () {});

        };

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

        $scope.submit = function () {
            switch ($scope.action) {
                case 'create' :
                    OrderService.create($scope.order).then(function (data) {
                        $uibModalInstance.close(data);
                    });
                    break;
                case 'update' :
                    OrderService.update($scope.order).then(function (data) {
                        $scope.order = data;
                    });
                    break;
            }
        };

        $scope.cancel = function () {
            $uibModalInstance.dismiss('cancel');
        };

    }]);