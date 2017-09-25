app.controller("diagnosisCtrl", ['OrderService', 'DiagnosisService', 'OrderDetectionTypeService', 'OrderDetectionTypeAttachService', 'DiagnosisAttachService', 'ModalProvider', '$uibModal', '$scope', '$rootScope', '$state', '$timeout',
    function (OrderService, DiagnosisService, OrderDetectionTypeService, OrderDetectionTypeAttachService, DiagnosisAttachService, ModalProvider, $uibModal, $scope, $rootScope, $state, $timeout) {

        $scope.selected = {};
        $scope.selected.diagnoses = [];
        $scope.selected.orderDetectionTypeAttaches = [];
        $scope.buffer = {};
        $scope.wrappers = [];

        $scope.items = [];
        $scope.items.push(
            {'id': 1, 'type': 'link', 'name': $rootScope.lang === 'AR' ? 'البرامج' : 'Application', 'link': 'menu'},
            {'id': 2, 'type': 'title', 'name': $rootScope.lang === 'AR' ? 'نتائج الفحص' : 'Detection Results'}
        );

        $scope.setSelected = function (object) {
            if (object) {
                angular.forEach($scope.orderDetectionTypes, function (orderDetectionType) {
                    if (object.id == orderDetectionType.id) {
                        $scope.selected = orderDetectionType;
                        return orderDetectionType.isSelected = true;
                    } else {
                        return orderDetectionType.isSelected = false;
                    }
                });
            }
        };

        $scope.findPending = function () {
            OrderDetectionTypeService.findPending().then(function (data) {
                $scope.orderDetectionTypes = data;
                $scope.setSelected($scope.orderDetectionTypes[0]);
                $scope.items = [];
                $scope.items.push(
                    {
                        'id': 1,
                        'type': 'link',
                        'name': $rootScope.lang === 'AR' ? 'البرامج' : 'Application',
                        'link': 'menu'
                    },
                    {'id': 2, 'type': 'title', 'name': $rootScope.lang === 'AR' ? 'خدمات الفحص' : 'Detection Services'},
                    {'id': 3, 'type': 'title', 'name': $rootScope.lang === 'AR' ? 'تحت التنفيذ' : 'Pending'}
                );
            });
        };

        $scope.findDiagnosed = function () {
            OrderDetectionTypeService.findDiagnosed().then(function (data) {
                $scope.orderDetectionTypes = data;
                $scope.setSelected($scope.orderDetectionTypes[0]);
                $scope.items = [];
                $scope.items.push(
                    {
                        'id': 1,
                        'type': 'link',
                        'name': $rootScope.lang === 'AR' ? 'البرامج' : 'Application',
                        'link': 'menu'
                    },
                    {'id': 2, 'type': 'title', 'name': $rootScope.lang === 'AR' ? 'خدمات الفحص' : 'Detection Services'},
                    {'id': 3, 'type': 'title', 'name': $rootScope.lang === 'AR' ? 'تم التشخيص' : 'Diagnosed'}
                );
            });
        };

        $scope.findDone = function () {
            OrderDetectionTypeService.findDone().then(function (data) {
                $scope.orderDetectionTypes = data;
                $scope.setSelected($scope.orderDetectionTypes[0]);
                $scope.items = [];
                $scope.items.push(
                    {
                        'id': 1,
                        'type': 'link',
                        'name': $rootScope.lang === 'AR' ? 'البرامج' : 'Application',
                        'link': 'menu'
                    },
                    {'id': 2, 'type': 'title', 'name': $rootScope.lang === 'AR' ? 'خدمات الفحص' : 'Detection Services'},
                    {'id': 3, 'type': 'title', 'name': $rootScope.lang === 'AR' ? 'مكتملة' : 'Done'}
                );
            });
        };

        $scope.findCanceled = function () {
            OrderDetectionTypeService.findCanceled().then(function (data) {
                $scope.orderDetectionTypes = data;
                $scope.setSelected($scope.orderDetectionTypes[0]);
                $scope.items = [];
                $scope.items.push(
                    {
                        'id': 1,
                        'type': 'link',
                        'name': $rootScope.lang === 'AR' ? 'البرامج' : 'Application',
                        'link': 'menu'
                    },
                    {'id': 2, 'type': 'title', 'name': $rootScope.lang === 'AR' ? 'خدمات الفحص' : 'Detection Services'},
                    {'id': 3, 'type': 'title', 'name': $rootScope.lang === 'AR' ? 'مُلغاه' : 'Canceled'}
                );
            });
        };

        $scope.openFilter = function () {
            var modalInstance = $uibModal.open({
                animation: true,
                ariaLabelledBy: 'modal-title',
                ariaDescribedBy: 'modal-body',
                templateUrl: '/ui/partials/diagnosis/diagnosisFilter.html',
                controller: 'diagnosisFilterCtrl',
                scope: $scope,
                backdrop: 'static',
                keyboard: false
            });

            modalInstance.result.then(function (buffer) {

                $scope.buffer = buffer;

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
                //
                if (buffer.orderConditionsList) {
                    var orderConditions = [];
                    for (var i = 0; i < buffer.orderConditionsList.length; i++) {
                        orderConditions.push(buffer.orderConditionsList[i]);
                    }
                    search.push('orderConditions=');
                    search.push(orderConditions);
                    search.push('&');
                }
                //
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
                OrderDetectionTypeService.filter(search.join("")).then(function (data) {
                    $scope.orderDetectionTypes = data;
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
                            'name': $rootScope.lang === 'AR' ? 'خدمات الفحص' : 'Detection Results'
                        },
                        {'id': 3, 'type': 'title', 'name': $rootScope.lang === 'AR' ? 'بحث مخصص' : 'Filter'}
                    );
                });
            }, function () {});
        };

        $scope.delete = function (orderDetectionType) {
            if (orderDetectionType) {
                $rootScope.showConfirmNotify("العيادة الطبية", "هل تود حذف خدمة الفحص فعلاً؟", "error", "fa-trash", function () {
                    OrderService.remove(orderDetectionType.id).then(function () {
                        var index = $scope.orderDetectionTypes.indexOf(orderDetectionType);
                        $scope.orderDetectionTypes.splice(index, 1);
                        $scope.setSelected($scope.orderDetectionTypes[0]);
                    });
                });
                return;
            }

            $rootScope.showConfirmNotify("العيادة الطبية", "هل تود حذف خدمة الفحص فعلاً؟", "error", "fa-trash", function () {
                OrderService.remove($scope.selected.id).then(function () {
                    var index = $scope.orderDetectionTypes.indexOf($scope.selected);
                    $scope.orderDetectionTypes.splice(index, 1);
                    $scope.setSelected($scope.orderDetectionTypes[0]);
                });
            });
        };

        $scope.deleteDiagnosisByOrderDetectionType = function (orderDetectionType) {
            if (orderDetectionType) {
                $rootScope.showConfirmNotify("العيادة الطبية", "هل تود حذف الوصفات الطبية فعلاً؟", "error", "fa-trash", function () {
                    DiagnosisService.removeByOrderDetectionType(orderDetectionType.id).then(function () {
                        var index = $scope.orderDetectionTypes.indexOf(orderDetectionType);
                        $scope.orderDetectionTypes[index].diagnoses = [];
                    });
                });
                return;
            }

            $rootScope.showConfirmNotify("العيادة الطبية", "هل تود حذف الوصفات الطبية فعلاً؟", "error", "fa-trash", function () {
                DiagnosisService.removeByOrderDetectionType($scope.selected.id).then(function () {
                    var index = $scope.orderDetectionTypes.indexOf($scope.selected);
                    $scope.orderDetectionTypes[index].diagnoses = [];
                });
            });
        };

        $scope.newDiagnosis = function () {
            ModalProvider.openDiagnosisCreateModel($scope.selected).result.then(function (data) {
                $scope.selected.diagnoses.splice(0, 0, data);
            }, function () {
                console.info('DiagnosisCreateModel Closed.');
            });
        };

        $scope.printDiagnosed = function (order) {
            window.open('/report/order/diagnosed/' + order.id + '/PDF');
        };

        $scope.rowMenu = [
            {
                html: '<div class="drop-menu">اضافة وصفة جديدة<span class="fa fa-pencil fa-lg"></span></div>',
                enabled: function () {
                    return $rootScope.contains($rootScope.me.team.authorities, ['ROLE_DIAGNOSIS_CREATE']);
                },
                click: function ($itemScope, $event, value) {
                    $scope.newDiagnosis();
                }
            },
            {
                html: '<div class="drop-menu">طباعة نتائج التشخيص<span class="fa fa-print fa-lg"></span></div>',
                enabled: function () {
                    return true;
                },
                click: function ($itemScope, $event, value) {
                    $scope.printDiagnosed($itemScope.orderDetectionType.order);
                }
            },
            {
                html: '<div class="drop-menu">حذف الوصفات الطبية<span class="fa fa-trash fa-lg"></span></div>',
                enabled: function () {
                    return $rootScope.contains($rootScope.me.team.authorities, ['ROLE_DIAGNOSIS_DELETE']);
                },
                click: function ($itemScope, $event, value) {
                    $scope.deleteDiagnosisByOrderDetectionType($itemScope.orderDetectionType);
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
                templateUrl: '/ui/partials/diagnosis/orderDetectionTypeAttachUpload.html',
                controller: 'orderDetectionTypeAttachUploadCtrl',
                scope: $scope,
                backdrop: 'static',
                keyboard: false
            });

            modalInstance.result.then(function () {
                angular.forEach($scope.wrappers, function (wrapper) {
                    console.info(wrapper);
                    OrderDetectionTypeAttachService.upload($scope.selected, wrapper.name, wrapper.mimeType, wrapper.description, wrapper.src).then(function (data) {
                        if ($scope.selected.orderDetectionTypeAttaches) {
                            $scope.selected.orderDetectionTypeAttaches.splice(0, 0, data);
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
            $scope.findPending();
        }, 1500);

    }]);