app.factory("TransactionSellDetectionService",
    ['$http', '$log', function ($http, $log) {
        return {
            findOne: function (id) {
                return $http.get("/api/transactionSellDetection/findOne/" + id).then(function (response) {
                    return response.data;
                });
            },
            findByBillSellDetection: function (id) {
                return $http.get("/api/transactionSellDetection/findByBillSellDetection/" + id).then(function (response) {
                    return response.data;
                });
            },
            create: function (transactionSellDetection) {
                return $http.post("/api/transactionSellDetection/create", transactionSellDetection).then(function (response) {
                    return response.data;
                });
            },
            remove: function (id) {
                return $http.delete("/api/transactionSellDetection/delete/" + id);
            },
            removeByBillSellDetection: function (id) {
                return $http.delete("/api/transactionSellDetection/deleteByBillSellDetection/" + id);
            }
        };
    }]);