app.factory("ReceiptService",
    ['$http', '$log', function ($http, $log) {
        return {
            findOne: function (id) {
                return $http.get("/api/receipt/findOne/" + id).then(function (response) {
                    return response.data;
                });
            },
            create: function (receipt) {
                return $http.post("/api/receipt/create", receipt).then(function (response) {
                    return response.data;
                });
            },
            remove: function (id) {
                return $http.delete("/api/receipt/delete/" + id);
            },
            filter: function (search) {
                return $http.get("/api/receipt/filter?" + search).then(function (response) {
                    return response.data;
                });
            }
        };
    }]);