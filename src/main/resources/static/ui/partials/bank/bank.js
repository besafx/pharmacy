app.controller("bankCtrl", ['BankService', 'DepositService', 'FundService', 'ModalProvider', '$scope', '$rootScope', '$state', '$timeout', '$uibModal', '$location', '$anchorScroll',
    function (BankService, DepositService, FundService, ModalProvider, $scope, $rootScope, $state, $timeout, $uibModal, $location, $anchorScroll) {

        $scope.selected = {};
        $scope.buffer = {};
        $scope.buffer.date = new Date();

        $scope.fetchTableData = function () {
            BankService.findAll().then(function (data) {
                $scope.banks = data;
                $scope.setSelected(data[0]);
            });
        };

        $scope.calculateFunds = function () {
            FundService.findDetectionsCostByDate($scope.buffer.date.getTime()).then(function (data) {
                $scope.buffer.detectionsCost = data;
            });
            FundService.findSalesCostByDate($scope.buffer.date.getTime()).then(function (data) {
                $scope.buffer.salesCost = data;
            });
        };

        $scope.setSelected = function (object) {
            if (object) {
                angular.forEach($scope.banks, function (bank) {
                    if (object.id == bank.id) {
                        $scope.selected = bank;
                        return bank.isSelected = true;
                    } else {
                        return bank.isSelected = false;
                    }
                });
            }
        };

        $scope.delete = function (bank) {
            if (bank) {
                $rootScope.showConfirmNotify("حذف البيانات", "هل تود حذف حساب البنك وكل ما يتعلق به من حسابات فعلاً؟", "error", "fa-trash", function () {
                    BankService.remove(bank.id).then(function () {
                        var index = $scope.banks.indexOf(bank);
                        $scope.banks.splice(index, 1);
                        $scope.setSelected($scope.banks[0]);
                    });
                });
                return;
            }

            $rootScope.showConfirmNotify("حذف البيانات", "هل تود حذف حساب البنك وكل ما يتعلق به من حسابات فعلاً؟", "error", "fa-trash", function () {
                BankService.remove($scope.selected.id).then(function () {
                    var index = $scope.banks.indexOf($scope.selected);
                    $scope.banks.splice(index, 1);
                    $scope.setSelected($scope.banks[0]);
                });
            });
        };

        $scope.newBank = function () {
            ModalProvider.openBankCreateModel().result.then(function (data) {
                if ($scope.banks) {
                    $scope.banks.splice(0, 0, data);
                }
            }, function () {
                console.info('BankCreateModel Closed.');
            });
        };

        $scope.newDeposit = function () {
            ModalProvider.openDepositCreateModel($scope.selected).result.then(function (data) {
                if ($scope.selected.deposits && data) {
                    $scope.selected.deposits.splice(0, 0, data);
                }
            }, function () {
                console.info('DepositCreateModel Closed.');
            });
        };

        $scope.refreshDepositsByBank = function () {
            DepositService.findByBank($scope.selected.id).then(function (data) {
                $scope.selected.deposits = data;
            })
        };

        $scope.newWithdraw = function () {
            ModalProvider.openWithdrawCreateModel($scope.selected).result.then(function (data) {
                if ($scope.selected.withdraws && data) {
                    $scope.selected.withdraws.splice(0, 0, data);
                }
            }, function () {
                console.info('WithdrawCreateModel Closed.');
            });
        };

        $scope.refreshWithdrawsByBank = function () {
            WithdrawService.findByBank($scope.selected.id).then(function (data) {
                $scope.selected.withdraws = data;
            });
        };

        $scope.rowMenu = [
            {
                html: '<div class="drop-menu">انشاء حساب بنك جديد<span class="fa fa-pencil fa-lg"></span></div>',
                enabled: function () {
                    return $rootScope.contains($rootScope.me.team.authorities, ['ROLE_BANK_CREATE']);
                },
                click: function ($itemScope, $event, value) {
                    $scope.newBank();
                }
            },
            {
                html: '<div class="drop-menu">تعديل بيانات حساب البنك<span class="fa fa-edit fa-lg"></span></div>',
                enabled: function () {
                    return $rootScope.contains($rootScope.me.team.authorities, ['ROLE_BANK_UPDATE']);
                },
                click: function ($itemScope, $event, value) {
                    ModalProvider.openBankUpdateModel($itemScope.bank);
                }
            },
            {
                html: '<div class="drop-menu">حذف حساب البنك<span class="fa fa-trash fa-lg"></span></div>',
                enabled: function () {
                    return $rootScope.contains($rootScope.me.team.authorities, ['ROLE_BANK_DELETE']);
                },
                click: function ($itemScope, $event, value) {
                    $scope.delete($itemScope.bank);
                }
            }
        ];

        $timeout(function () {
            $scope.fetchTableData();
            $scope.calculateFunds();
            $location.hash('bankMenu');
            $anchorScroll();
            window.componentHandler.upgradeAllRegistered();
        }, 1500);

    }]);