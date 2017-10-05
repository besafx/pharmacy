app.factory("FundService",
    ['$http', '$log', function ($http, $log) {
        return {
            findDetectionsCostByDate: function (date) {
                return $http.get("/api/fund/findDetectionsCostByDate/" + date).then(function (response) {
                    return response.data;
                });
            },
            findSalesCostByDate: function (date) {
                return $http.get("/api/fund/findSalesCostByDate/" + date).then(function (response) {
                    return response.data;
                });
            }
        };
    }]);