app.factory("BillSellDetectionService",
    ['$http', '$log', function ($http, $log) {
        return {
            findAll: function () {
                return $http.get("/api/billSellDetection/findAll").then(function (response) {
                    return response.data;
                });
            },
            findOne: function (id) {
                return $http.get("/api/billSellDetection/findOne/" + id).then(function (response) {
                    return response.data;
                });
            },
            create: function (billSellDetection) {
                return $http.post("/api/billSellDetection/create", billSellDetection).then(function (response) {
                    return response.data;
                });
            },
            remove: function (id) {
                return $http.delete("/api/billSellDetection/delete/" + id);
            }
        };
    }]);