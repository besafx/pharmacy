app.controller("diagnosisCtrl", ['OrderService', 'DiagnosisService', 'OrderDetectionTypeService', 'OrderAttachService', 'ModalProvider', '$uibModal', '$scope', '$rootScope', '$state', '$timeout',
    function (OrderService, DiagnosisService, OrderDetectionTypeService, OrderAttachService, ModalProvider, $uibModal, $scope, $rootScope, $state, $timeout) {

        $scope.selected = {};
        $scope.selected.orderDetectionTypes = [];
        $scope.selected.diagnoses = [];
        $scope.selectedOrderDetectionType = {};
        $scope.buffer = {};
        $scope.wrappers = [];

        $scope.items = [];
        $scope.items.push(
            {'id': 1, 'type': 'link', 'name': $rootScope.lang === 'AR' ? 'البرامج' : 'Application', 'link': 'menu'},
            {'id': 2, 'type': 'title', 'name': $rootScope.lang === 'AR' ? 'طلبات الفحص' : 'Detection Orders'}
        );

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

        $scope.setSelectedOrderDetectionType = function (object) {
            if (object) {
                angular.forEach($scope.selected.orderDetectionTypes, function (orderDetectionType) {
                    if (object.id == orderDetectionType.id) {
                        $scope.selectedOrderDetectionType = orderDetectionType;
                        return orderDetectionType.isSelected = true;
                    } else {
                        return orderDetectionType.isSelected = false;
                    }
                });
            }
        };

        $scope.openFilter = function () {
            var modalInstance = $uibModal.open({
                animation: true,
                ariaLabelledBy: 'modal-title',
                ariaDescribedBy: 'modal-body',
                templateUrl: '/ui/partials/order/orderFilter.html',
                controller: 'orderFilterCtrl',
                scope: $scope,
                backdrop: 'static',
                keyboard: false
            });

            modalInstance.result.then(function (buffer) {
                var search = [];

                if (buffer.codeFrom) {
                    search.push('codeFrom=');
                    search.push(buffer.codeFrom);
                    search.push('&');
                }
                if (buffer.codeTo) {
                    search.push('codeTo=');
                    search.push(buffer.codeTo);
                    search.push('&');
                }
                if (buffer.dateTo) {
                    search.push('dateTo=');
                    search.push(buffer.dateTo.getTime());
                    search.push('&');
                }
                if (buffer.dateFrom) {
                    search.push('dateFrom=');
                    search.push(buffer.dateFrom.getTime());
                    search.push('&');
                }
                //
                if (buffer.customerName) {
                    search.push('customerName=');
                    search.push(buffer.customerName);
                    search.push('&');
                }
                if (buffer.customerMobile) {
                    search.push('customerMobile=');
                    search.push(buffer.customerMobile);
                    search.push('&');
                }
                if (buffer.customerIdentityNumber) {
                    search.push('customerIdentityNumber=');
                    search.push(buffer.customerIdentityNumber);
                    search.push('&');
                }
                //
                if (buffer.falconCode) {
                    search.push('falconCode=');
                    search.push(buffer.falconCode);
                    search.push('&');
                }
                if (buffer.falconType) {
                    search.push('falconType=');
                    search.push(buffer.falconType);
                    search.push('&');
                }
                if (buffer.weightTo) {
                    search.push('weightTo=');
                    search.push(buffer.weightTo);
                    search.push('&');
                }
                if (buffer.weightFrom) {
                    search.push('weightFrom=');
                    search.push(buffer.weightFrom);
                    search.push('&');
                }
                //
                OrderService.filter(search.join("")).then(function (data) {
                    $scope.orders = data;
                    $scope.setSelected(data[0]);
                    $scope.items = [];
                    $scope.items.push(
                        {
                            'id': 1,
                            'type': 'link',
                            'name': $rootScope.lang === 'AR' ? 'البرامج' : 'Application',
                            'link': 'menu'
                        },
                        {
                            'id': 2,
                            'type': 'title',
                            'name': $rootScope.lang === 'AR' ? 'طلبات الفحص' : 'Detection Orders'
                        },
                        {'id': 3, 'type': 'title', 'name': $rootScope.lang === 'AR' ? 'بحث مخصص' : 'Filter'}
                    );
                });
            }, function () {
            });
        };

        $scope.deleteOrderDetectionType = function (orderDetectionType) {
            if (orderDetectionType) {
                $rootScope.showConfirmNotify("العيادة الطبية", "هل تود حذف خدمة الفحص فعلاً؟", "error", "fa-trash", function () {
                    OrderDetectionTypeService.remove(orderDetectionType.id).then(function (data) {
                        var index = $scope.selected.orderDetectionTypes.indexOf(data);
                        $scope.selected.orderDetectionTypes.splice(index, 1);
                    });
                });

            }
        };

        $scope.deleteDiagnosis = function (diagnosis) {
            if (diagnosis) {
                $rootScope.showConfirmNotify("العيادة الطبية", "هل تود حذف العلاج فعلاً؟", "error", "fa-trash", function () {
                    DiagnosisService.remove(diagnosis.id).then(function (data) {
                        var index = $scope.selected.diagnoses.indexOf(data);
                        $scope.selected.diagnoses.splice(index, 1);
                    });
                });

            }
        };

        $scope.newOrderDetectionType = function () {
            ModalProvider.openOrderDetectionTypeCreateModel($scope.selected).result.then(function (data) {
                $scope.selected.orderDetectionTypes.splice(0, 0, data);
            }, function () {
                console.info('OrderDetectionTypeCreateModel Closed.');
            });
        };

        $scope.newDiagnosis = function () {
            ModalProvider.openDiagnosisCreateModel($scope.selected).result.then(function (data) {
                Array.prototype.splice.apply($scope.selected.diagnoses, [1, 0].concat(data));
            }, function () {
                console.info('DiagnosisCreateModel Closed.');
            });
        };

        $scope.saveOrderNote = function () {
            if ($rootScope.contains($rootScope.me.team.authorities, ['ROLE_ORDER_SAVE_NOTE'])) {
                OrderService.saveNote($scope.selected, $scope.selected.note);
            }
        };

        $scope.saveOrderDetectionTypeCase = function (orderDetectionType) {
            if ($rootScope.contains($rootScope.me.team.authorities, ['ROLE_ORDER_DETECTION_TYPE_SAVE_CASE'])) {
                OrderDetectionTypeService.saveOrderDetectionTypeCase(orderDetectionType, orderDetectionType.done);
            }
        };

        $scope.refreshOrderDetectionTypeByOrder = function () {
            OrderDetectionTypeService.findByOrder($scope.selected).then(function (data) {
                $scope.selected.orderDetectionTypes = data;
            });
        };

        $scope.refreshDiagnosesByOrder = function () {
            DiagnosisService.findByOrderId($scope.selected.id).then(function (data) {
                $scope.selected.diagnosis = data;
            });
        };

        $scope.rowMenu = [
            {
                html: '<div class="drop-menu">اضافة نوع فحص للطلب<span class="fa fa-pencil fa-lg"></span></div>',
                enabled: function () {
                    return $rootScope.contains($rootScope.me.team.authorities, ['ROLE_ORDER_CREATE']);
                },
                click: function ($itemScope, $event, value) {
                    $scope.newOrderDetectionType();
                }
            }
        ];

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
                    OrderAttachService.upload($scope.selected, wrapper.name, wrapper.mimeType, wrapper.description, wrapper.src).then(function (data) {
                        if ($scope.selected.orderAttaches) {
                            $scope.selected.orderAttaches.splice(0, 0, data);
                        }
                    });
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

        $timeout(function () {
            window.componentHandler.upgradeAllRegistered();
            OrderService.findAll().then(function (data) {
                $scope.orders = data;
                $scope.setSelected(data[0]);
            });
        }, 1500);

    }]);