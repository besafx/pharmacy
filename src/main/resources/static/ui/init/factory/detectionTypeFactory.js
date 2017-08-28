app.factory("DetectionTypeService",
    ['$http', '$log', function ($http, $log) {
        return {
            findAll: function () {
                return $http.get("/api/detectionType/findAll").then(function (response) {
                    return response.data;
                });
            },
            findAllCombo: function () {
                return $http.get("/api/detectionType/findAllCombo").then(function (response) {
                    return response.data;
                });
            },
            findOne: function (id) {
                return $http.get("/api/detectionType/findOne/" + id).then(function (response) {
                    return response.data;
                });
            },
            create: function (detectionType) {
                return $http.post("/api/detectionType/create", detectionType).then(function (response) {
                    return response.data;
                });
            },
            remove: function (id) {
                return $http.delete("/api/detectionType/delete/" + id);
            },
            update: function (detectionType) {
                return $http.put("/api/detectionType/update", detectionType).then(function (response) {
                    return response.data;
                });
            }
        };
    }]);