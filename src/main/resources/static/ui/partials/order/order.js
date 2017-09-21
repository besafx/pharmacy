app.controller("orderCtrl", ['OrderService', 'DiagnosisService', 'OrderDetectionTypeService', 'OrderAttachService', 'ModalProvider', '$uibModal', '$scope', '$rootScope', '$state', '$timeout',
    function (OrderService, DiagnosisService, OrderDetectionTypeService, OrderAttachService, ModalProvider, $uibModal, $scope, $rootScope, $state, $timeout) {

        $scope.selected = {};
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

        $scope.findPending = function () {
            OrderService.findPending().then(function (data) {
                $scope.orders = data;
                $scope.setSelected($scope.orders[0]);
                $scope.items = [];
                $scope.items.push(
                    {
                        'id': 1,
                        'type': 'link',
                        'name': $rootScope.lang === 'AR' ? 'البرامج' : 'Application',
                        'link': 'menu'
                    },
                    {'id': 2, 'type': 'title', 'name': $rootScope.lang === 'AR' ? 'طلبات الفحص' : 'Detection Orders'},
                    {'id': 3, 'type': 'title', 'name': $rootScope.lang === 'AR' ? 'تحت التنفيذ' : 'Pending'}
                );
            });
        };

        $scope.findDiagnosed = function () {
            OrderService.findDiagnosed().then(function (data) {
                $scope.orders = data;
                $scope.setSelected($scope.orders[0]);
                $scope.items = [];
                $scope.items.push(
                    {
                        'id': 1,
                        'type': 'link',
                        'name': $rootScope.lang === 'AR' ? 'البرامج' : 'Application',
                        'link': 'menu'
                    },
                    {'id': 2, 'type': 'title', 'name': $rootScope.lang === 'AR' ? 'طلبات الفحص' : 'Detection Orders'},
                    {'id': 3, 'type': 'title', 'name': $rootScope.lang === 'AR' ? 'تم التشخيص' : 'Diagnosed'}
                );
            });
        };

        $scope.findDone = function () {
            OrderService.findDone().then(function (data) {
                $scope.orders = data;
                $scope.setSelected($scope.orders[0]);
                $scope.items = [];
                $scope.items.push(
                    {
                        'id': 1,
                        'type': 'link',
                        'name': $rootScope.lang === 'AR' ? 'البرامج' : 'Application',
                        'link': 'menu'
                    },
                    {'id': 2, 'type': 'title', 'name': $rootScope.lang === 'AR' ? 'طلبات الفحص' : 'Detection Orders'},
                    {'id': 3, 'type': 'title', 'name': $rootScope.lang === 'AR' ? 'مكتملة' : 'Done'}
                );
            });
        };

        $scope.findCanceled = function () {
            OrderService.findCanceled().then(function (data) {
                $scope.orders = data;
                $scope.setSelected($scope.orders[0]);
                $scope.items = [];
                $scope.items.push(
                    {
                        'id': 1,
                        'type': 'link',
                        'name': $rootScope.lang === 'AR' ? 'البرامج' : 'Application',
                        'link': 'menu'
                    },
                    {'id': 2, 'type': 'title', 'name': $rootScope.lang === 'AR' ? 'طلبات الفحص' : 'Detection Orders'},
                    {'id': 3, 'type': 'title', 'name': $rootScope.lang === 'AR' ? 'مُلغاه' : 'Canceled'}
                );
            });
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
                if (buffer.falconsList) {
                    var falcons = [];
                    for (var i = 0; i < buffer.falconsList.length; i++) {
                        falcons.push(buffer.falconsList[i].id);
                    }
                    search.push('falcons=');
                    search.push(falcons);
                    search.push('&');
                }
                //
                if (buffer.doctorsList) {
                    var doctors = [];
                    for (var i = 0; i < buffer.doctorsList.length; i++) {
                        doctors.push(buffer.doctorsList[i].id);
                    }
                    search.push('doctors=');
                    search.push(doctors);
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
            }, function () {});
        };

        $scope.delete = function (order) {
            if (order) {
                $rootScope.showConfirmNotify("الإستقبال", "هل تود حذف الطلب فعلاً؟", "error", "fa-trash", function () {
                    OrderService.remove(order.id).then(function () {
                        var index = $scope.orders.indexOf(order);
                        $scope.orders.splice(index, 1);
                        $scope.setSelected($scope.orders[0]);
                    });
                });
                return;
            }

            $rootScope.showConfirmNotify("الإستقبال", "هل تود حذف الطلب فعلاً؟", "error", "fa-trash", function () {
                OrderService.remove($scope.selected.id).then(function () {
                    var index = $scope.orders.indexOf($scope.selected);
                    $scope.orders.splice(index, 1);
                    $scope.setSelected($scope.orders[0]);
                });
            });
        };

        $scope.deleteOrderAttach = function (orderAttach) {
            if (orderAttach) {
                $rootScope.showConfirmNotify("الإستقبال", "هل تود حذف المستند فعلاً؟", "error", "fa-trash", function () {
                    OrderAttachService.remove(orderAttach).then(function (data) {
                        if(data===false) {
                            OrderAttachService.removeWhatever(orderAttach);
                        }
                        var index = $scope.selected.orderAttaches.indexOf(orderAttach);
                        $scope.selected.orderAttaches.splice(index, 1);
                    });
                });

            }
        };

        $scope.newOrder = function () {
            ModalProvider.openOrderCreateModel().result.then(function (data) {
                $rootScope.showConfirmNotify("الإستقبال", "هل تود طباعة الطلب ؟", "notification", "fa-info", function () {
                    $scope.printPending(data);
                });
                if($scope.orders){
                    $scope.orders.splice(0, 0, data);
                }
            }, function () {
                console.info('OrderCreateModel Closed.');
            });
        };

        $scope.newOrderDetectionType = function () {
            ModalProvider.openOrderDetectionTypeCreateModel($scope.selected).result.then(function (data) {
                if ($scope.selected.orderDetectionTypes) {
                    $scope.selected.orderDetectionTypes.splice(0, 0, data);
                }
                $rootScope.showConfirmNotify("الإستقبال", "هل تود طباعة الطلب ؟", "notification", "fa-info", function () {
                    $scope.printPending($scope.selected);
                });
            }, function () {
                console.info('OrderDetectionTypeCreateModel Closed.');
            });
        };

        $scope.newDiagnosis = function () {
            ModalProvider.openDiagnosisCreateModel($scope.selectedOrderDetectionType).result.then(function (data) {
                $rootScope.showConfirmNotify("العيادة البيطرية", "هل تود طباعة نتائج الفحص الطبي ؟", "notification", "fa-info", function () {
                    $scope.printDiagnosed($scope.selected);
                });
                if ($scope.selectedOrderDetectionType.diagnoses) {
                    $scope.selectedOrderDetectionType.diagnoses.splice(0, 0, data);
                }
            }, function () {
                console.info('DiagnosisCreateModel Closed.');
            });
        };

        $scope.printPending = function (order) {
            window.open('/report/order/pending/' + order.id + '/PDF');
        };

        $scope.printDiagnosed = function (order) {
            window.open('/report/order/diagnosed/' + order.id + '/PDF');
        };

        $scope.refreshOrderDetectionTypeByOrder = function () {
            OrderDetectionTypeService.findByOrder($scope.selected).then(function (data) {
                $scope.selected.orderDetectionTypes = data;
            });
        };

        $scope.refreshOrderAttachByOrder = function () {
            OrderAttachService.findByOrder($scope.selected).then(function (data) {
                $scope.selected.orderAttaches = data;
            });
        };

        $scope.refreshDiagnosesByOrderDetectionType = function () {
            DiagnosisService.findByOrderDetectionTypeId($scope.selectedOrderDetectionType.id).then(function (data) {
                $scope.selectedOrderDetectionType.diagnosis = data;
            });
        };

        $scope.rowMenu = [
            {
                html: '<div class="drop-menu">انشاء طلب جديد<span class="fa fa-pencil fa-lg"></span></div>',
                enabled: function () {
                    return $rootScope.contains($rootScope.me.team.authorities, ['ROLE_ORDER_CREATE']);
                },
                click: function ($itemScope, $event, value) {
                    $scope.newOrder();
                }
            },
            {
                html: '<div class="drop-menu">حذف الطلب<span class="fa fa-trash fa-lg"></span></div>',
                enabled: function () {
                    return $rootScope.contains($rootScope.me.team.authorities, ['ROLE_ORDER_DELETE']);
                },
                click: function ($itemScope, $event, value) {
                    $scope.delete($itemScope.order);
                }
            },
            {
                html: '<div class="drop-menu">طباعة الطلب<span class="fa fa-print fa-lg"></span></div>',
                enabled: function () {
                    return true;
                },
                click: function ($itemScope, $event, value) {
                    $scope.printPending($itemScope.order);
                }
            },
            {
                html: '<div class="drop-menu">طباعة تقرير مختصر<span class="fa fa-print fa-lg"></span></div>',
                enabled: function () {
                    return true;
                },
                click: function ($itemScope, $event, value) {
                    ModalProvider.openReportOrderByListModel($scope.orders);
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
            $scope.findPending();
        }, 1500);

    }]);