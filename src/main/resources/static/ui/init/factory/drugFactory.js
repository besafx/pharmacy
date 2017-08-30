app.factory("DrugService",
    ['$http', '$log', function ($http, $log) {
        return {
            findAll: function () {
                return $http.get("/api/drug/findAll").then(function (response) {
                    return response.data;
                });
            },
            findAllCombo: function () {
                return $http.get("/api/drug/findAllCombo").then(function (response) {
                    return response.data;
                });
            },
            findOne: function (id) {
                return $http.get("/api/drug/findOne/" + id).then(function (response) {
                    return response.data;
                });
            },
            create: function (drug) {
                return $http.post("/api/drug/create", drug).then(function (response) {
                    return response.data;
                });
            },
            remove: function (id) {
                return $http.delete("/api/drug/delete/" + id);
            },
            update: function (drug) {
                return $http.put("/api/drug/update", drug).then(function (response) {
                    return response.data;
                });
            }
        };
    }]);