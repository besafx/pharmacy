app.controller("fundCtrl", ['FundService', 'FundReceiptService', 'ModalProvider', '$scope', '$rootScope', '$state', '$timeout', '$uibModal',
    function (FundService, FundReceiptService, ModalProvider, $scope, $rootScope, $state, $timeout, $uibModal) {

        /**************************************************************
         *                                                            *
         * List                                                       *
         *                                                            *
         *************************************************************/
        $scope.buffer = {};
        $scope.selectedFund = {};
        $timeout(function () {
            FundService.get().then(function (data) {
                $scope.selectedFund = data;
            });
            $scope.findFundReceiptsInByWeek();
            $scope.findFundReceiptsOutByWeek();
            window.componentHandler.upgradeAllRegistered();
        }, 1500);
        $scope.submitFundUpdate = function () {
            FundService.update($scope.selectedFund).then(function (data) {
                $scope.selectedFund = data;
            });
        };

        /**************************************************************
         *                                                            *
         * Receipt In                                                 *
         *                                                            *
         *************************************************************/
        $scope.selectedReceiptIn = {};
        $scope.receiptsIn = [];
        $scope.itemsIn = [];
        $scope.itemsIn.push(
            {'id': 1, 'type': 'title', 'name': $rootScope.lang === 'AR' ? 'الصندوق' : 'Fund'},
            {'id': 2, 'type': 'title', 'name': $rootScope.lang === 'AR' ? 'سندات القبض' : 'Fund Cash In'}
        );

        $scope.setSelectedIn = function (object) {
            if (object) {
                angular.forEach($scope.receiptsIn, function (fundReceipt) {
                    if (object.id == fundReceipt.id) {
                        $scope.selectedReceiptIn = fundReceipt;
                        return fundReceipt.isSelected = true;
                    } else {
                        return fundReceipt.isSelected = false;
                    }
                });
            }
        };

        $scope.filterIn = function () {

            var search = [];

            //
            if ($scope.buffer.receiptCodeFrom) {
                search.push('receiptCodeFrom=');
                search.push($scope.buffer.receiptCodeFrom);
                search.push('&');
            }
            if ($scope.buffer.receiptCodeTo) {
                search.push('receiptCodeTo=');
                search.push($scope.buffer.receiptCodeTo);
                search.push('&');
            }
            //
            if ($scope.buffer.receiptDateFrom) {
                search.push('receiptDateFrom=');
                search.push($scope.buffer.receiptDateFrom.getTime());
                search.push('&');
            }
            if ($scope.buffer.receiptDateTo) {
                search.push('receiptDateTo=');
                search.push($scope.buffer.receiptDateTo.getTime());
                search.push('&');
            }
            //
            $scope.buffer.receiptType = 'In';
            search.push('receiptType=');
            search.push($scope.buffer.receiptType);
            search.push('&');
            //

            FundReceiptService.filter(search.join("")).then(function (data) {
                $scope.receiptsIn = data;
                $scope.setSelectedIn(data[0]);
                $scope.totalAmountIn = 0;
                angular.forEach(data, function (fundReceipt) {
                    $scope.totalAmountIn+=fundReceipt.receipt.amountNumber;
                });
                $scope.itemsIn = [];
                $scope.itemsIn.push(
                    {'id': 1, 'type': 'title', 'name': $rootScope.lang === 'AR' ? 'الصندوق' : 'Fund'},
                    {'id': 2, 'type': 'title', 'name': $rootScope.lang === 'AR' ? 'سندات القبض' : 'Fund Cash In'},
                    {'id': 3, 'type': 'title', 'name': $rootScope.lang === 'AR' ? 'بحث مخصص' : 'Custom Filters'}
                );
                $timeout(function () {
                    window.componentHandler.upgradeAllRegistered();
                }, 500);
            });
        };

        $scope.openFilterInModal = function () {
            $scope.buffer.receiptType = 'Out';
            var modalInstance = $uibModal.open({
                animation: true,
                ariaLabelledBy: 'modal-title',
                ariaDescribedBy: 'modal-body',
                templateUrl: '/ui/partials/fundReceipt/fundReceiptFilter.html',
                controller: 'fundReceiptFilterCtrl',
                scope: $scope,
                backdrop: 'static',
                keyboard: false,
                size:'lg'
            });

            modalInstance.result.then(function (buffer) {
                $scope.buffer = buffer;
                $scope.filterIn();
            }, function () {});
        };

        $scope.findFundReceiptsInByToday = function () {
            FundReceiptService.findByTodayIn().then(function (data) {
                $scope.receiptsIn = data;
                $scope.setSelectedIn(data[0]);
                $scope.totalAmountIn = 0;
                angular.forEach(data, function (fundReceipt) {
                    $scope.totalAmountIn+=fundReceipt.receipt.amountNumber;
                });
                $scope.itemsIn = [];
                $scope.itemsIn.push(
                    {'id': 1, 'type': 'title', 'name': $rootScope.lang === 'AR' ? 'الصندوق' : 'Fund'},
                    {'id': 2, 'type': 'title', 'name': $rootScope.lang === 'AR' ? 'سندات القبض' : 'Fund Cash In'},
                    {'id': 3, 'type': 'title', 'name': $rootScope.lang === 'AR' ? 'ايراد اليوم' : 'For Today'}
                );
                $timeout(function () {
                    window.componentHandler.upgradeAllRegistered();
                }, 500);
            });
        };

        $scope.findFundReceiptsInByWeek = function () {
            FundReceiptService.findByWeekIn().then(function (data) {
                $scope.receiptsIn = data;
                $scope.setSelectedIn(data[0]);
                $scope.totalAmountIn = 0;
                angular.forEach(data, function (fundReceipt) {
                    $scope.totalAmountIn+=fundReceipt.receipt.amountNumber;
                });
                $scope.itemsIn = [];
                $scope.itemsIn.push(
                    {'id': 1, 'type': 'title', 'name': $rootScope.lang === 'AR' ? 'الصندوق' : 'Fund'},
                    {'id': 2, 'type': 'title', 'name': $rootScope.lang === 'AR' ? 'سندات القبض' : 'Fund Cash In'},
                    {'id': 3, 'type': 'title', 'name': $rootScope.lang === 'AR' ? 'ايراد الاسبوع' : 'For Week'}
                );
                $timeout(function () {
                    window.componentHandler.upgradeAllRegistered();
                }, 500);
            });
        };

        $scope.findFundReceiptsInByMonth = function () {
            FundReceiptService.findByMonthIn().then(function (data) {
                $scope.receiptsIn = data;
                $scope.setSelectedIn(data[0]);
                $scope.totalAmountIn = 0;
                angular.forEach(data, function (fundReceipt) {
                    $scope.totalAmountIn+=fundReceipt.receipt.amountNumber;
                });
                $scope.itemsIn = [];
                $scope.itemsIn.push(
                    {'id': 1, 'type': 'title', 'name': $rootScope.lang === 'AR' ? 'الصندوق' : 'Fund'},
                    {'id': 2, 'type': 'title', 'name': $rootScope.lang === 'AR' ? 'سندات القبض' : 'Fund Cash In'},
                    {'id': 3, 'type': 'title', 'name': $rootScope.lang === 'AR' ? 'ايراد الشهر' : 'For Month'}
                );
                $timeout(function () {
                    window.componentHandler.upgradeAllRegistered();
                }, 500);
            });
        };

        $scope.findFundReceiptsInByYear = function () {
            FundReceiptService.findByYearIn().then(function (data) {
                $scope.receiptsIn = data;
                $scope.setSelectedIn(data[0]);
                $scope.totalAmountIn = 0;
                angular.forEach(data, function (fundReceipt) {
                    $scope.totalAmountIn+=fundReceipt.receipt.amountNumber;
                });
                $scope.itemsIn = [];
                $scope.itemsIn.push(
                    {'id': 1, 'type': 'title', 'name': $rootScope.lang === 'AR' ? 'الصندوق' : 'Fund'},
                    {'id': 2, 'type': 'title', 'name': $rootScope.lang === 'AR' ? 'سندات القبض' : 'Fund Cash In'},
                    {'id': 3, 'type': 'title', 'name': $rootScope.lang === 'AR' ? 'ايراد العام' : 'For Year'}
                );
                $timeout(function () {
                    window.componentHandler.upgradeAllRegistered();
                }, 500);
            });
        };

        $scope.deleteIn = function (fundReceipt) {
            if (fundReceipt) {
                $rootScope.showConfirmNotify("حذف البيانات", "هل تود حذف السند وكل ما يتعلق به من حسابات فعلاً؟", "error", "fa-trash", function () {
                    FundReceiptService.remove(fundReceipt.id).then(function () {
                        var index = $scope.receiptsIn.indexOf(fundReceipt);
                        $scope.receiptsIn.splice(index, 1);
                        $scope.setSelectedIn($scope.receiptsIn[0]);
                        $scope.totalAmountIn = 0;
                        angular.forEach($scope.receiptsIn, function (fundReceipt) {
                            $scope.totalAmountIn+=fundReceipt.receipt.amountNumber;
                        });
                    });
                });
                return;
            }

            $rootScope.showConfirmNotify("حذف البيانات", "هل تود حذف السند وكل ما يتعلق به من حسابات فعلاً؟", "error", "fa-trash", function () {
                FundReceiptService.remove($scope.selectedReceiptIn.id).then(function () {
                    var index = $scope.receiptsIn.indexOf($scope.selectedReceiptIn);
                    $scope.receiptsIn.splice(index, 1);
                    $scope.setSelectedIn($scope.receiptsIn[0]);
                    $scope.totalAmountIn = 0;
                    angular.forEach($scope.receiptsIn, function (fundReceipt) {
                        $scope.totalAmountIn+=fundReceipt.receipt.amountNumber;
                    });
                });
            });
        };

        $scope.newFundReceiptIn = function () {
            ModalProvider.openFundReceiptInCreateModel().result.then(function (data) {
                $scope.receiptsIn.splice(0, 0, data);
                $scope.selectedFund.balance+=data.receipt.amountNumber;
                $scope.totalAmountIn = 0;
                angular.forEach($scope.receiptsIn, function (fundReceipt) {
                    $scope.totalAmountIn+=fundReceipt.receipt.amountNumber;
                });
            }, function () {
                console.info('FundReceiptCreateModel Closed.');
            });
        };

        $scope.rowMenuIn = [
            {
                html: '<div class="drop-menu">انشاء سند جديد<span class="fa fa-pencil fa-lg"></span></div>',
                enabled: function () {
                    return $rootScope.contains($rootScope.me.team.authorities, ['ROLE_FUND_RECEIPT_IN_CREATE']);
                },
                click: function ($itemScope, $event, value) {
                    $scope.newFundReceiptIn();
                }
            },
            {
                html: '<div class="drop-menu">حذف السند<span class="fa fa-trash fa-lg"></span></div>',
                enabled: function () {
                    return $rootScope.contains($rootScope.me.team.authorities, ['ROLE_FUND_RECEIPT_IN_DELETE']);
                },
                click: function ($itemScope, $event, value) {
                    $scope.deleteIn($itemScope.fundReceipt);
                }
            }
        ];

        /**************************************************************
         *                                                            *
         * Receipt Out                                                *
         *                                                            *
         *************************************************************/
        $scope.selectedOut = {};
        $scope.receiptsOut = [];
        $scope.itemsOut = [];
        $scope.itemsOut.push(
            {'id': 1, 'type': 'title', 'name': $rootScope.lang === 'AR' ? 'الصندوق' : 'Fund'},
            {'id': 2, 'type': 'title', 'name': $rootScope.lang === 'AR' ? 'سندات الصرف' : 'Fund Cash Out'}
        );

        $scope.setSelectedOut = function (object) {
            if (object) {
                angular.forEach($scope.receiptsOut, function (fundReceipt) {
                    if (object.id == fundReceipt.id) {
                        $scope.selectedOut = fundReceipt;
                        return fundReceipt.isSelected = true;
                    } else {
                        return fundReceipt.isSelected = false;
                    }
                });
            }
        };

        $scope.filterOut = function () {

            var search = [];

            //
            if ($scope.buffer.receiptCodeFrom) {
                search.push('receiptCodeFrom=');
                search.push($scope.buffer.receiptCodeFrom);
                search.push('&');
            }
            if ($scope.buffer.receiptCodeTo) {
                search.push('receiptCodeTo=');
                search.push($scope.buffer.receiptCodeTo);
                search.push('&');
            }
            //
            if ($scope.buffer.receiptDateFrom) {
                search.push('receiptDateFrom=');
                search.push($scope.buffer.receiptDateFrom.getTime());
                search.push('&');
            }
            if ($scope.buffer.receiptDateTo) {
                search.push('receiptDateTo=');
                search.push($scope.buffer.receiptDateTo.getTime());
                search.push('&');
            }
            //
            $scope.buffer.receiptType = 'Out';
            search.push('receiptType=');
            search.push($scope.buffer.receiptType);
            search.push('&');
            //

            FundReceiptService.filter(search.join("")).then(function (data) {
                $scope.receiptsOut = data;
                $scope.setSelectedOut(data[0]);
                $scope.totalAmountOut = 0;
                angular.forEach(data, function (fundReceipt) {
                    $scope.totalAmountOut+=fundReceipt.receipt.amountNumber;
                });
                $scope.itemsOut = [];
                $scope.itemsOut.push(
                    {'id': 1, 'type': 'title', 'name': $rootScope.lang === 'AR' ? 'الصندوق' : 'Fund'},
                    {'id': 2, 'type': 'title', 'name': $rootScope.lang === 'AR' ? 'سندات الصرف' : 'Fund Cash Out'},
                    {'id': 3, 'type': 'title', 'name': $rootScope.lang === 'AR' ? 'بحث مخصص' : 'Custom Filters'}
                );
                $timeout(function () {
                    window.componentHandler.upgradeAllRegistered();
                }, 500);
            });
        };

        $scope.openFilterOutModal = function () {
            $scope.buffer.receiptType = 'Out';
            var modalInstance = $uibModal.open({
                animation: true,
                ariaLabelledBy: 'modal-title',
                ariaDescribedBy: 'modal-body',
                templateUrl: '/ui/partials/fundReceipt/fundReceiptFilter.html',
                controller: 'fundReceiptFilterCtrl',
                scope: $scope,
                backdrop: 'static',
                keyboard: false,
                size:'lg'
            });

            modalInstance.result.then(function (buffer) {
                $scope.buffer = buffer;
                $scope.filterOut();
            }, function () {});
        };

        $scope.findFundReceiptsOutByToday = function () {
            FundReceiptService.findByTodayOut().then(function (data) {
                $scope.receiptsOut = data;
                $scope.setSelectedOut(data[0]);
                $scope.totalAmountOut = 0;
                angular.forEach(data, function (fundReceipt) {
                    $scope.totalAmountOut+=fundReceipt.receipt.amountNumber;
                });
                $scope.itemsOut = [];
                $scope.itemsOut.push(
                    {'id': 1, 'type': 'title', 'name': $rootScope.lang === 'AR' ? 'الصندوق' : 'Fund'},
                    {'id': 2, 'type': 'title', 'name': $rootScope.lang === 'AR' ? 'سندات الصرف' : 'Fund Cash Out'},
                    {'id': 3, 'type': 'title', 'name': $rootScope.lang === 'AR' ? 'مصاريف اليوم' : 'For Today'}
                );
                $timeout(function () {
                    window.componentHandler.upgradeAllRegistered();
                }, 500);
            });
        };

        $scope.findFundReceiptsOutByWeek = function () {
            FundReceiptService.findByWeekOut().then(function (data) {
                $scope.receiptsOut = data;
                $scope.setSelectedOut(data[0]);
                $scope.totalAmountOut = 0;
                angular.forEach(data, function (fundReceipt) {
                    $scope.totalAmountOut+=fundReceipt.receipt.amountNumber;
                });
                $scope.itemsOut = [];
                $scope.itemsOut.push(
                    {'id': 1, 'type': 'title', 'name': $rootScope.lang === 'AR' ? 'الصندوق' : 'Fund'},
                    {'id': 2, 'type': 'title', 'name': $rootScope.lang === 'AR' ? 'سندات الصرف' : 'Fund Cash Out'},
                    {'id': 3, 'type': 'title', 'name': $rootScope.lang === 'AR' ? 'مصاريف الاسبوع' : 'For Week'}
                );
                $timeout(function () {
                    window.componentHandler.upgradeAllRegistered();
                }, 500);
            });
        };

        $scope.findFundReceiptsOutByMonth = function () {
            FundReceiptService.findByMonthOut().then(function (data) {
                $scope.receiptsOut = data;
                $scope.setSelectedOut(data[0]);
                $scope.totalAmountOut = 0;
                angular.forEach(data, function (fundReceipt) {
                    $scope.totalAmountOut+=fundReceipt.receipt.amountNumber;
                });
                $scope.itemsOut = [];
                $scope.itemsOut.push(
                    {'id': 1, 'type': 'title', 'name': $rootScope.lang === 'AR' ? 'الصندوق' : 'Fund'},
                    {'id': 2, 'type': 'title', 'name': $rootScope.lang === 'AR' ? 'سندات الصرف' : 'Fund Cash Out'},
                    {'id': 3, 'type': 'title', 'name': $rootScope.lang === 'AR' ? 'مصاريف الشهر' : 'For Month'}
                );
                $timeout(function () {
                    window.componentHandler.upgradeAllRegistered();
                }, 500);
            });
        };

        $scope.findFundReceiptsOutByYear = function () {
            FundReceiptService.findByYearOut().then(function (data) {
                $scope.receiptsOut = data;
                $scope.setSelectedOut(data[0]);
                $scope.totalAmountOut = 0;
                angular.forEach(data, function (fundReceipt) {
                    $scope.totalAmountOut+=fundReceipt.receipt.amountNumber;
                });
                $scope.itemsOut = [];
                $scope.itemsOut.push(
                    {'id': 1, 'type': 'title', 'name': $rootScope.lang === 'AR' ? 'الصندوق' : 'Fund'},
                    {'id': 2, 'type': 'title', 'name': $rootScope.lang === 'AR' ? 'سندات الصرف' : 'Fund Cash Out'},
                    {'id': 3, 'type': 'title', 'name': $rootScope.lang === 'AR' ? 'مصاريف العام' : 'For Year'}
                );
                $timeout(function () {
                    window.componentHandler.upgradeAllRegistered();
                }, 500);
            });
        };

        $scope.deleteOut = function (fundReceipt) {
            if (fundReceipt) {
                $rootScope.showConfirmNotify("حذف البيانات", "هل تود حذف السند وكل ما يتعلق به من حسابات فعلاً؟", "error", "fa-trash", function () {
                    FundReceiptService.remove(fundReceipt.id).then(function () {
                        var index = $scope.receiptsOut.indexOf(fundReceipt);
                        $scope.receiptsOut.splice(index, 1);
                        $scope.setSelectedOut($scope.receiptsOut[0]);
                        $scope.totalAmountOut = 0;
                        angular.forEach($scope.receiptsOut, function (fundReceipt) {
                            $scope.totalAmountOut+=fundReceipt.receipt.amountNumber;
                        });
                    });
                });
                return;
            }

            $rootScope.showConfirmNotify("حذف البيانات", "هل تود حذف السند وكل ما يتعلق به من حسابات فعلاً؟", "error", "fa-trash", function () {
                FundReceiptService.remove($scope.selectedOut.id).then(function () {
                    var index = $scope.receiptsOut.indexOf($scope.selectedOut);
                    $scope.receiptsOut.splice(index, 1);
                    $scope.setSelectedOut($scope.receiptsOut[0]);
                    $scope.totalAmountOut = 0;
                    angular.forEach($scope.receiptsOut, function (fundReceipt) {
                        $scope.totalAmountOut+=fundReceipt.receipt.amountNumber;
                    });
                });
            });
        };

        $scope.newFundReceiptOut = function () {
            ModalProvider.openFundReceiptOutCreateModel().result.then(function (data) {
                $scope.receiptsOut.splice(0, 0, data);
                $scope.selectedFund.balance-=data.receipt.amountNumber;
                $scope.totalAmountOut = 0;
                angular.forEach($scope.receiptsOut, function (fundReceipt) {
                    $scope.totalAmountOut+=fundReceipt.receipt.amountNumber;
                });
            }, function () {
                console.info('FundReceiptCreateModel Closed.');
            });
        };

        $scope.rowMenuOut = [
            {
                html: '<div class="drop-menu">انشاء سند جديد<span class="fa fa-pencil fa-lg"></span></div>',
                enabled: function () {
                    return $rootScope.contains($rootScope.me.team.authorities, ['ROLE_FUND_RECEIPT_OUT_CREATE']);
                },
                click: function ($itemScope, $event, value) {
                    $scope.newFundReceiptOut();
                }
            },
            {
                html: '<div class="drop-menu">حذف السند<span class="fa fa-trash fa-lg"></span></div>',
                enabled: function () {
                    return $rootScope.contains($rootScope.me.team.authorities, ['ROLE_FUND_RECEIPT_OUT_DELETE']);
                },
                click: function ($itemScope, $event, value) {
                    $scope.deleteOut($itemScope.fundReceipt);
                }
            }
        ];

    }]);