app.factory("OrderDetectionTypeService",
    ['$http', '$log', function ($http, $log) {
        return {
            create: function (orderDetectionType) {
                return $http.post("/api/orderDetectionType/create", orderDetectionType).then(function (response) {
                    return response.data;
                });
            },
            findByOrder: function (order) {
                return $http.get("/api/orderDetectionType/findByOrder/" + order.id).then(function (response) {
                    return response.data;
                });
            }
        };
    }]);