app.factory("TransactionSellService",
    ['$http', '$log', function ($http, $log) {
        return {
            findOne: function (id) {
                return $http.get("/api/transactionSell/findOne/" + id).then(function (response) {
                    return response.data;
                });
            },
            findByBillSell: function (id) {
                return $http.get("/api/transactionSell/findByBillSell/" + id).then(function (response) {
                    return response.data;
                });
            },
            findByTransactionBuy: function (id) {
                return $http.get("/api/transactionSell/findByTransactionBuy/" + id).then(function (response) {
                    return response.data;
                });
            },
            create: function (transactionSell) {
                return $http.post("/api/transactionSell/create", transactionSell).then(function (response) {
                    return response.data;
                });
            },
            remove: function (id) {
                return $http.delete("/api/transactionSell/delete/" + id);
            },
            removeByBillSell: function (id) {
                return $http.delete("/api/transactionSell/deleteByBillSell/" + id);
            }
        };
    }]);