app.factory("OrderReceiptService",
    ['$http', '$log', function ($http, $log) {
        return {
            findOne: function (id) {
                return $http.get("/api/orderReceipt/findOne/" + id).then(function (response) {
                    return response.data;
                });
            },
            findByOrder: function (orderId) {
                return $http.get("/api/orderReceipt/findByOrder/" + orderId).then(function (response) {
                    return response.data;
                });
            },
            create: function (order) {
                return $http.post("/api/orderReceipt/create", order).then(function (response) {
                    return response.data;
                });
            },
            remove: function (id) {
                return $http.delete("/api/orderReceipt/delete/" + id);
            },
            filter: function (search) {
                return $http.get("/api/orderReceipt/filter?" + search).then(function (response) {
                    return response.data;
                });
            }
        };
    }]);