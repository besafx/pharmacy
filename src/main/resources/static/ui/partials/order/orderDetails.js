app.controller('orderDetailsCtrl', [
    'OrderService',
    'DiagnosisService',
    'OrderDetectionTypeService',
    'OrderAttachService',
    'OrderReceiptService',
    'ModalProvider',
    '$uibModal',
    '$scope',
    '$rootScope',
    '$timeout',
    '$log',
    '$uibModalInstance',
    'order',
    function (OrderService,
              DiagnosisService,
              OrderDetectionTypeService,
              OrderAttachService,
              OrderReceiptService,
              ModalProvider,
              $uibModal,
              $scope,
              $rootScope,
              $timeout,
              $log,
              $uibModalInstance,
              order) {

        $scope.wrappers = [];
        $scope.order = order;
        $scope.refreshOrder = function (order) {
            OrderService.findOne(order.id).then(function (data) {
                return order = data;
            });
        };
        $scope.printPending = function (order) {
            window.open('/report/order/pending/' + order.id + '/PDF');
        };
        $scope.newOrderReceipt = function (order) {
            ModalProvider.openOrderReceiptCreateModel(order).result.then(function (data) {
                return order.orderReceipts.splice(0, 0, data);
            }, function () {
            });
        };
        $scope.deleteOrderAttach = function (orderAttach, order) {
            $rootScope.showConfirmNotify("الإستقبال", "هل تود حذف المستند فعلاً؟", "error", "fa-trash", function () {
                OrderAttachService.remove(orderAttach).then(function (data) {
                    if (data === false) {
                        OrderAttachService.removeWhatever(orderAttach);
                    }
                    var index = order.orderAttaches.indexOf(orderAttach);
                    return order.orderAttaches.splice(index, 1);
                });
            });
        };
        $scope.deleteOrderDetectionType = function (orderDetectionType, order) {
            $rootScope.showConfirmNotify("الإستقبال", "هل تود حذف خدمة الفحص فعلاً؟", "error", "fa-trash", function () {
                OrderDetectionTypeService.remove(orderDetectionType.id).then(function () {
                    var index = order.orderDetectionTypes.indexOf(orderDetectionType);
                    return order.orderDetectionTypes.splice(index, 1);
                });
            });
        };
        $scope.deleteOrderReceipt = function (orderReceipt, order) {
            $rootScope.showConfirmNotify("الإستقبال", "هل تود حذف سند القبض فعلاً؟", "error", "fa-trash", function () {
                OrderReceiptService.remove(orderReceipt.id).then(function () {
                    var index = order.orderReceipts.indexOf(orderReceipt);
                    return order.orderReceipts.splice(index, 1);
                });
            });
        };
        $scope.newOrderDetectionType = function (order) {
            ModalProvider.openOrderDetectionTypeCreateModel(order).result.then(function (data) {
                if (order.orderDetectionTypes) {
                    return order.orderDetectionTypes.splice(0, 0, data);
                }
            }, function () {
            });
        };
        $scope.refreshOrderDetectionTypeByOrder = function (order) {
            OrderDetectionTypeService.findByOrder(order).then(function (data) {
                return order.orderDetectionTypes = data;
            });
        };
        $scope.refreshOrderAttachByOrder = function (order) {
            OrderAttachService.findByOrder(order).then(function (data) {
                return order.orderAttaches = data;
            });
        };
        $scope.findOrdersByFalcon = function (falcon, code) {
            OrderService.findByFalconAndCodeNot(falcon.id, code).then(function (data) {
                return falcon.orders = data;
            });
        };
        $scope.findOrdersByCustomer = function (customer, code) {
            OrderService.findByFalconCustomerAndCodeNot(customer.id, code).then(function (data) {
                return customer.orders = data;
            });
        };
        $scope.deleteDiagnosis = function (diagnosis, order) {
            if (diagnosis) {
                $rootScope.showConfirmNotify("العيادة الطبية", "هل تود حذف العلاج فعلاً؟", "error", "fa-trash", function () {
                    DiagnosisService.remove(diagnosis.id).then(function () {
                        var index = order.diagnoses.indexOf(diagnosis);
                        return order.diagnoses.splice(index, 1);
                    });
                });

            }
        };
        $scope.newDiagnosis = function (order) {
            ModalProvider.openDiagnosisCreateModel(order).result.then(function (data) {
                Array.prototype.splice.apply(order.diagnoses, [1, 0].concat(data));
            }, function () {
                console.info('DiagnosisCreateModel Closed.');
            });
        };
        $scope.saveOrderNote = function (order) {
            if ($rootScope.contains($rootScope.me.team.authorities, ['ROLE_ORDER_SAVE_NOTE'])) {
                OrderService.saveNote(order, order.note);
            }
        };
        $scope.saveOrderDetectionTypeCase = function (orderDetectionType) {
            if ($rootScope.contains($rootScope.me.team.authorities, ['ROLE_ORDER_DETECTION_TYPE_SAVE_CASE'])) {
                OrderDetectionTypeService.saveOrderDetectionTypeCase(orderDetectionType, orderDetectionType.done);
            }
        };
        $scope.refreshDiagnosesByOrder = function (order) {
            DiagnosisService.findByOrderId(order.id).then(function (data) {
                order.diagnosis = data;
            });
        };

        /***********************************
         *                                 *
         * START FILE UPLOADER             *
         *                                 *
         **********************************/
        $scope.orderForUpload = {};
        $scope.uploadFiles = function (order) {
            $scope.orderForUpload = order;
            document.getElementById('uploader-order').click();
        };
        $scope.initFiles = function (files) {

            $scope.wrappers = [];

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
                    OrderAttachService.upload($scope.orderForUpload, wrapper.name, wrapper.mimeType, wrapper.description, wrapper.src).then(function (data) {
                        if ($scope.orderForUpload.orderAttaches) {
                            return $scope.orderForUpload.orderAttaches.splice(0, 0, data);
                        }
                    });
                });
            }, function () {
            });

        };
        /***********************************
         *                                 *
         * END FILE UPLOADER               *
         *                                 *
         **********************************/

        /***********************************
         *                                 *
         * START SCAN MANAGER              *
         *                                 *
         **********************************/
        $scope.scanToJpg = function (order) {
            $scope.orderForUpload = order;
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
        /***********************************
         *                                 *
         * END SCAN MANAGER                *
         *                                 *
         **********************************/

        $scope.cancel = function () {
            $uibModalInstance.dismiss('cancel');
        };
        $timeout(function () {
            $scope.refreshOrder($scope.order);
            window.componentHandler.upgradeAllRegistered();
        }, 600);

    }]);