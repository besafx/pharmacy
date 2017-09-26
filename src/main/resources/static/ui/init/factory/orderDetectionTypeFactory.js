app.factory("OrderDetectionTypeService",
    ['$http', '$log', function ($http, $log) {
        return {
            create: function (orderDetectionType) {
                return $http.post("/api/orderDetectionType/create", orderDetectionType).then(function (response) {
                    return response.data;
                });
            },
            saveOrderDetectionTypeCase: function (orderDetectionType, done) {
                return $http.get("/api/orderDetectionType/saveOrderDetectionTypeCase/" + orderDetectionType.id + "/" + done).then(function (response) {
                    return response.data;
                });
            },
            remove: function (id) {
                return $http.delete("/api/orderDetectionType/delete/" + id);
            },
            findOne: function (id) {
                return $http.get("/api/orderDetectionType/findOne/" + id).then(function (response) {
                    return response.data;
                });
            },
            findByOrder: function (order) {
                return $http.get("/api/orderDetectionType/findByOrder/" + order.id).then(function (response) {
                    return response.data;
                });
            },
            filter: function (search) {
                return $http.get("/api/orderDetectionType/filter?" + search).then(function (response) {
                    return response.data;
                });
            }
        };
    }]);