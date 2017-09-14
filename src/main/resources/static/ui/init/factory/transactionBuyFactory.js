app.factory("TransactionBuyService",
    ['$http', '$log', function ($http, $log) {
        return {
            findOne: function (id) {
                return $http.get("/api/transactionBuy/findOne/" + id).then(function (response) {
                    return response.data;
                });
            },
            findByBillBuy: function (id) {
                return $http.get("/api/transactionBuy/findByBillBuy/" + id).then(function (response) {
                    return response.data;
                });
            },
            create: function (transactionBuy) {
                return $http.post("/api/transactionBuy/create", transactionBuy).then(function (response) {
                    return response.data;
                });
            },
            remove: function (id) {
                return $http.delete("/api/transactionBuy/delete/" + id);
            },
            removeByBillBuy: function (id) {
                return $http.delete("/api/transactionBuy/deleteByBillBuy/" + id);
            }
        };
    }]);