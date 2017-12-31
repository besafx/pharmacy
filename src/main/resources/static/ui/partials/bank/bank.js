app.controller("bankCtrl", ['BankService', 'BankReceiptService', 'ModalProvider', '$scope', '$rootScope', '$state', '$timeout', '$uibModal',
    function (BankService, BankReceiptService, ModalProvider, $scope, $rootScope, $state, $timeout, $uibModal) {

        /**************************************************************
         *                                                            *
         * List                                                       *
         *                                                            *
         *************************************************************/
        $scope.buffer = {};
        $scope.selectedBank = {};
        $timeout(function () {
            BankService.get().then(function (data) {
                $scope.selectedBank = data;
            });
            $scope.findBankReceiptsInByWeek();
            $scope.findBankReceiptsOutByWeek();
            window.componentHandler.upgradeAllRegistered();
        }, 1500);
        $scope.submitBankUpdate = function () {
            BankService.update($scope.selectedBank).then(function (data) {
                $scope.selectedBank = data;
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
            {'id': 1, 'type': 'title', 'name': $rootScope.lang === 'AR' ? 'البنك' : 'Bank'},
            {'id': 2, 'type': 'title', 'name': $rootScope.lang === 'AR' ? 'سندات القبض' : 'Bank Cash In'}
        );

        $scope.setSelectedIn = function (object) {
            if (object) {
                angular.forEach($scope.receiptsIn, function (bankReceipt) {
                    if (object.id == bankReceipt.id) {
                        $scope.selectedReceiptIn = bankReceipt;
                        return bankReceipt.isSelected = true;
                    } else {
                        return bankReceipt.isSelected = false;
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

            BankReceiptService.filter(search.join("")).then(function (data) {
                $scope.receiptsIn = data;
                $scope.setSelectedIn(data[0]);
                $scope.totalAmountIn = 0;
                angular.forEach(data, function (bankReceipt) {
                    $scope.totalAmountIn+=bankReceipt.receipt.amountNumber;
                });
                $scope.itemsIn = [];
                $scope.itemsIn.push(
                    {'id': 1, 'type': 'title', 'name': $rootScope.lang === 'AR' ? 'البنك' : 'Bank'},
                    {'id': 2, 'type': 'title', 'name': $rootScope.lang === 'AR' ? 'سندات القبض' : 'Bank Cash In'},
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
                templateUrl: '/ui/partials/bankReceipt/bankReceiptFilter.html',
                controller: 'bankReceiptFilterCtrl',
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

        $scope.findBankReceiptsInByToday = function () {
            BankReceiptService.findByTodayIn().then(function (data) {
                $scope.receiptsIn = data;
                $scope.setSelectedIn(data[0]);
                $scope.totalAmountIn = 0;
                angular.forEach(data, function (bankReceipt) {
                    $scope.totalAmountIn+=bankReceipt.receipt.amountNumber;
                });
                $scope.itemsIn = [];
                $scope.itemsIn.push(
                    {'id': 1, 'type': 'title', 'name': $rootScope.lang === 'AR' ? 'البنك' : 'Bank'},
                    {'id': 2, 'type': 'title', 'name': $rootScope.lang === 'AR' ? 'سندات القبض' : 'Bank Cash In'},
                    {'id': 3, 'type': 'title', 'name': $rootScope.lang === 'AR' ? 'تحصيل اليوم' : 'For Today'}
                );
                $timeout(function () {
                    window.componentHandler.upgradeAllRegistered();
                }, 500);
            });
        };

        $scope.findBankReceiptsInByWeek = function () {
            BankReceiptService.findByWeekIn().then(function (data) {
                $scope.receiptsIn = data;
                $scope.setSelectedIn(data[0]);
                $scope.totalAmountIn = 0;
                angular.forEach(data, function (bankReceipt) {
                    $scope.totalAmountIn+=bankReceipt.receipt.amountNumber;
                });
                $scope.itemsIn = [];
                $scope.itemsIn.push(
                    {'id': 1, 'type': 'title', 'name': $rootScope.lang === 'AR' ? 'البنك' : 'Bank'},
                    {'id': 2, 'type': 'title', 'name': $rootScope.lang === 'AR' ? 'سندات القبض' : 'Bank Cash In'},
                    {'id': 3, 'type': 'title', 'name': $rootScope.lang === 'AR' ? 'تحصيل الاسبوع' : 'For Week'}
                );
                $timeout(function () {
                    window.componentHandler.upgradeAllRegistered();
                }, 500);
            });
        };

        $scope.findBankReceiptsInByMonth = function () {
            BankReceiptService.findByMonthIn().then(function (data) {
                $scope.receiptsIn = data;
                $scope.setSelectedIn(data[0]);
                $scope.totalAmountIn = 0;
                angular.forEach(data, function (bankReceipt) {
                    $scope.totalAmountIn+=bankReceipt.receipt.amountNumber;
                });
                $scope.itemsIn = [];
                $scope.itemsIn.push(
                    {'id': 1, 'type': 'title', 'name': $rootScope.lang === 'AR' ? 'البنك' : 'Bank'},
                    {'id': 2, 'type': 'title', 'name': $rootScope.lang === 'AR' ? 'سندات القبض' : 'Bank Cash In'},
                    {'id': 3, 'type': 'title', 'name': $rootScope.lang === 'AR' ? 'تحصيل الشهر' : 'For Month'}
                );
                $timeout(function () {
                    window.componentHandler.upgradeAllRegistered();
                }, 500);
            });
        };

        $scope.findBankReceiptsInByYear = function () {
            BankReceiptService.findByYearIn().then(function (data) {
                $scope.receiptsIn = data;
                $scope.setSelectedIn(data[0]);
                $scope.totalAmountIn = 0;
                angular.forEach(data, function (bankReceipt) {
                    $scope.totalAmountIn+=bankReceipt.receipt.amountNumber;
                });
                $scope.itemsIn = [];
                $scope.itemsIn.push(
                    {'id': 1, 'type': 'title', 'name': $rootScope.lang === 'AR' ? 'البنك' : 'Bank'},
                    {'id': 2, 'type': 'title', 'name': $rootScope.lang === 'AR' ? 'سندات القبض' : 'Bank Cash In'},
                    {'id': 3, 'type': 'title', 'name': $rootScope.lang === 'AR' ? 'تحصيل العام' : 'For Year'}
                );
                $timeout(function () {
                    window.componentHandler.upgradeAllRegistered();
                }, 500);
            });
        };

        $scope.deleteIn = function (bankReceipt) {
            if (bankReceipt) {
                $rootScope.showConfirmNotify("حذف البيانات", "هل تود حذف السند وكل ما يتعلق به من حسابات فعلاً؟", "error", "fa-trash", function () {
                    BankReceiptService.remove(bankReceipt.id).then(function () {
                        var index = $scope.receiptsIn.indexOf(bankReceipt);
                        $scope.receiptsIn.splice(index, 1);
                        $scope.setSelectedIn($scope.receiptsIn[0]);
                        $scope.totalAmountIn = 0;
                        angular.forEach($scope.receiptsIn, function (bankReceipt) {
                            $scope.totalAmountIn+=bankReceipt.receipt.amountNumber;
                        });
                    });
                });
                return;
            }

            $rootScope.showConfirmNotify("حذف البيانات", "هل تود حذف السند وكل ما يتعلق به من حسابات فعلاً؟", "error", "fa-trash", function () {
                BankReceiptService.remove($scope.selectedReceiptIn.id).then(function () {
                    var index = $scope.receiptsIn.indexOf($scope.selectedReceiptIn);
                    $scope.receiptsIn.splice(index, 1);
                    $scope.setSelectedIn($scope.receiptsIn[0]);
                    $scope.totalAmountIn = 0;
                    angular.forEach($scope.receiptsIn, function (bankReceipt) {
                        $scope.totalAmountIn+=bankReceipt.receipt.amountNumber;
                    });
                });
            });
        };

        $scope.newBankReceiptIn = function () {
            ModalProvider.openBankReceiptInCreateModel().result.then(function (data) {
                $scope.receiptsIn.splice(0, 0, data);
                $scope.selectedBank.balance+=data.receipt.amountNumber;
                $scope.totalAmountIn = 0;
                angular.forEach($scope.receiptsIn, function (bankReceipt) {
                    $scope.totalAmountIn+=bankReceipt.receipt.amountNumber;
                });
            }, function () {
                console.info('BankReceiptCreateModel Closed.');
            });
        };

        $scope.rowMenuIn = [
            {
                html: '<div class="drop-menu">انشاء سند جديد<span class="fa fa-pencil fa-lg"></span></div>',
                enabled: function () {
                    return $rootScope.contains($rootScope.me.team.authorities, ['ROLE_BANK_RECEIPT_IN_CREATE']);
                },
                click: function ($itemScope, $event, value) {
                    $scope.newBankReceiptIn();
                }
            },
            {
                html: '<div class="drop-menu">حذف السند<span class="fa fa-trash fa-lg"></span></div>',
                enabled: function () {
                    return $rootScope.contains($rootScope.me.team.authorities, ['ROLE_BANK_RECEIPT_IN_DELETE']);
                },
                click: function ($itemScope, $event, value) {
                    $scope.deleteIn($itemScope.bankReceipt);
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
            {'id': 1, 'type': 'title', 'name': $rootScope.lang === 'AR' ? 'البنك' : 'Bank'},
            {'id': 2, 'type': 'title', 'name': $rootScope.lang === 'AR' ? 'سندات الصرف' : 'Bank Cash Out'}
        );

        $scope.setSelectedOut = function (object) {
            if (object) {
                angular.forEach($scope.receiptsOut, function (bankReceipt) {
                    if (object.id == bankReceipt.id) {
                        $scope.selectedOut = bankReceipt;
                        return bankReceipt.isSelected = true;
                    } else {
                        return bankReceipt.isSelected = false;
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

            BankReceiptService.filter(search.join("")).then(function (data) {
                $scope.receiptsOut = data;
                $scope.setSelectedOut(data[0]);
                $scope.totalAmountOut = 0;
                angular.forEach(data, function (bankReceipt) {
                    $scope.totalAmountOut+=bankReceipt.receipt.amountNumber;
                });
                $scope.itemsOut = [];
                $scope.itemsOut.push(
                    {'id': 1, 'type': 'title', 'name': $rootScope.lang === 'AR' ? 'البنك' : 'Bank'},
                    {'id': 2, 'type': 'title', 'name': $rootScope.lang === 'AR' ? 'سندات الصرف' : 'Bank Cash Out'},
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
                templateUrl: '/ui/partials/bankReceipt/bankReceiptFilter.html',
                controller: 'bankReceiptFilterCtrl',
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

        $scope.findBankReceiptsOutByToday = function () {
            BankReceiptService.findByTodayOut().then(function (data) {
                $scope.receiptsOut = data;
                $scope.setSelectedOut(data[0]);
                $scope.totalAmountOut = 0;
                angular.forEach(data, function (bankReceipt) {
                    $scope.totalAmountOut+=bankReceipt.receipt.amountNumber;
                });
                $scope.itemsOut = [];
                $scope.itemsOut.push(
                    {'id': 1, 'type': 'title', 'name': $rootScope.lang === 'AR' ? 'البنك' : 'Bank'},
                    {'id': 2, 'type': 'title', 'name': $rootScope.lang === 'AR' ? 'سندات الصرف' : 'Bank Cash Out'},
                    {'id': 3, 'type': 'title', 'name': $rootScope.lang === 'AR' ? 'دفعات اليوم' : 'For Today'}
                );
                $timeout(function () {
                    window.componentHandler.upgradeAllRegistered();
                }, 500);
            });
        };

        $scope.findBankReceiptsOutByWeek = function () {
            BankReceiptService.findByWeekOut().then(function (data) {
                $scope.receiptsOut = data;
                $scope.setSelectedOut(data[0]);
                $scope.totalAmountOut = 0;
                angular.forEach(data, function (bankReceipt) {
                    $scope.totalAmountOut+=bankReceipt.receipt.amountNumber;
                });
                $scope.itemsOut = [];
                $scope.itemsOut.push(
                    {'id': 1, 'type': 'title', 'name': $rootScope.lang === 'AR' ? 'البنك' : 'Bank'},
                    {'id': 2, 'type': 'title', 'name': $rootScope.lang === 'AR' ? 'سندات الصرف' : 'Bank Cash Out'},
                    {'id': 3, 'type': 'title', 'name': $rootScope.lang === 'AR' ? 'دفعات الاسبوع' : 'For Week'}
                );
                $timeout(function () {
                    window.componentHandler.upgradeAllRegistered();
                }, 500);
            });
        };

        $scope.findBankReceiptsOutByMonth = function () {
            BankReceiptService.findByMonthOut().then(function (data) {
                $scope.receiptsOut = data;
                $scope.setSelectedOut(data[0]);
                $scope.totalAmountOut = 0;
                angular.forEach(data, function (bankReceipt) {
                    $scope.totalAmountOut+=bankReceipt.receipt.amountNumber;
                });
                $scope.itemsOut = [];
                $scope.itemsOut.push(
                    {'id': 1, 'type': 'title', 'name': $rootScope.lang === 'AR' ? 'البنك' : 'Bank'},
                    {'id': 2, 'type': 'title', 'name': $rootScope.lang === 'AR' ? 'سندات الصرف' : 'Bank Cash Out'},
                    {'id': 3, 'type': 'title', 'name': $rootScope.lang === 'AR' ? 'دفعات الشهر' : 'For Month'}
                );
                $timeout(function () {
                    window.componentHandler.upgradeAllRegistered();
                }, 500);
            });
        };

        $scope.findBankReceiptsOutByYear = function () {
            BankReceiptService.findByYearOut().then(function (data) {
                $scope.receiptsOut = data;
                $scope.setSelectedOut(data[0]);
                $scope.totalAmountOut = 0;
                angular.forEach(data, function (bankReceipt) {
                    $scope.totalAmountOut+=bankReceipt.receipt.amountNumber;
                });
                $scope.itemsOut = [];
                $scope.itemsOut.push(
                    {'id': 1, 'type': 'title', 'name': $rootScope.lang === 'AR' ? 'البنك' : 'Bank'},
                    {'id': 2, 'type': 'title', 'name': $rootScope.lang === 'AR' ? 'سندات الصرف' : 'Bank Cash Out'},
                    {'id': 3, 'type': 'title', 'name': $rootScope.lang === 'AR' ? 'دفعات العام' : 'For Year'}
                );
                $timeout(function () {
                    window.componentHandler.upgradeAllRegistered();
                }, 500);
            });
        };

        $scope.deleteOut = function (bankReceipt) {
            if (bankReceipt) {
                $rootScope.showConfirmNotify("حذف البيانات", "هل تود حذف السند وكل ما يتعلق به من حسابات فعلاً؟", "error", "fa-trash", function () {
                    BankReceiptService.remove(bankReceipt.id).then(function () {
                        var index = $scope.receiptsOut.indexOf(bankReceipt);
                        $scope.receiptsOut.splice(index, 1);
                        $scope.setSelectedOut($scope.receiptsOut[0]);
                        $scope.totalAmountOut = 0;
                        angular.forEach($scope.receiptsOut, function (bankReceipt) {
                            $scope.totalAmountOut+=bankReceipt.receipt.amountNumber;
                        });
                    });
                });
                return;
            }

            $rootScope.showConfirmNotify("حذف البيانات", "هل تود حذف السند وكل ما يتعلق به من حسابات فعلاً؟", "error", "fa-trash", function () {
                BankReceiptService.remove($scope.selectedOut.id).then(function () {
                    var index = $scope.receiptsOut.indexOf($scope.selectedOut);
                    $scope.receiptsOut.splice(index, 1);
                    $scope.setSelectedOut($scope.receiptsOut[0]);
                    $scope.totalAmountOut = 0;
                    angular.forEach($scope.receiptsOut, function (bankReceipt) {
                        $scope.totalAmountOut+=bankReceipt.receipt.amountNumber;
                    });
                });
            });
        };

        $scope.newBankReceiptOut = function () {
            ModalProvider.openBankReceiptOutCreateModel().result.then(function (data) {
                $scope.receiptsOut.splice(0, 0, data);
                $scope.selectedBank.balance-=data.receipt.amountNumber;
                $scope.totalAmountOut = 0;
                angular.forEach($scope.receiptsOut, function (bankReceipt) {
                    $scope.totalAmountOut+=bankReceipt.receipt.amountNumber;
                });
            }, function () {
                console.info('BankReceiptCreateModel Closed.');
            });
        };

        $scope.rowMenuOut = [
            {
                html: '<div class="drop-menu">انشاء سند جديد<span class="fa fa-pencil fa-lg"></span></div>',
                enabled: function () {
                    return $rootScope.contains($rootScope.me.team.authorities, ['ROLE_BANK_RECEIPT_OUT_CREATE']);
                },
                click: function ($itemScope, $event, value) {
                    $scope.newBankReceiptOut();
                }
            },
            {
                html: '<div class="drop-menu">حذف السند<span class="fa fa-trash fa-lg"></span></div>',
                enabled: function () {
                    return $rootScope.contains($rootScope.me.team.authorities, ['ROLE_BANK_RECEIPT_OUT_DELETE']);
                },
                click: function ($itemScope, $event, value) {
                    $scope.deleteOut($itemScope.bankReceipt);
                }
            }
        ];

    }]);