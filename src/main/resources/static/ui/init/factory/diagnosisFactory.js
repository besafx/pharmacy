app.factory("DiagnosisService",
    ['$http', '$log', function ($http, $log) {
        return {
            findAll: function () {
                return $http.get("/api/diagnosis/findAll").then(function (response) {
                    return response.data;
                });
            },
            findOne: function (id) {
                return $http.get("/api/diagnosis/findOne/" + id).then(function (response) {
                    return response.data;
                });
            },
            findByOrderId: function (id) {
                return $http.get("/api/diagnosis/findByOrderId/" + id).then(function (response) {
                    return response.data;
                });
            },
            create: function (diagnosis) {
                return $http.post("/api/diagnosis/create", diagnosis).then(function (response) {
                    return response.data;
                });
            },
            createAll: function (diagnosisWrapper) {
                return $http.post("/api/diagnosis/createAll", diagnosisWrapper).then(function (response) {
                    return response.data;
                });
            },
            remove: function (id) {
                return $http.delete("/api/diagnosis/delete/" + id);
            },
            removeByOrder: function (id) {
                return $http.delete("/api/diagnosis/deleteByOrder/" + id);
            }
        };
    }]);