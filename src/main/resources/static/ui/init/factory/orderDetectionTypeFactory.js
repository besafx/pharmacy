app.factory("OrderDetectionTypeService",
    ['$http', '$log', function ($http, $log) {
        return {
            create: function (orderDetectionType) {
                return $http.post("/api/orderDetectionType/create", orderDetectionType).then(function (response) {
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
            findPending: function () {
                return $http.get("/api/orderDetectionType/findPending").then(function (response) {
                    return response.data;
                });
            },
            findDiagnosed: function () {
                return $http.get("/api/orderDetectionType/findDiagnosed").then(function (response) {
                    return response.data;
                });
            },
            findDone: function () {
                return $http.get("/api/orderDetectionType/findDone").then(function (response) {
                    return response.data;
                });
            },
            findCanceled: function () {
                return $http.get("/api/orderDetectionType/findCanceled").then(function (response) {
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