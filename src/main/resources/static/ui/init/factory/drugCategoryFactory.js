app.factory("DrugCategoryService",
    ['$http', '$log', function ($http, $log) {
        return {
            findAll: function () {
                return $http.get("/api/drugCategory/findAll").then(function (response) {
                    return response.data;
                });
            },
            findAllCombo: function () {
                return $http.get("/api/drugCategory/findAllCombo").then(function (response) {
                    return response.data;
                });
            },
            findOne: function (id) {
                return $http.get("/api/drugCategory/findOne/" + id).then(function (response) {
                    return response.data;
                });
            },
            create: function (drugCategory) {
                return $http.post("/api/drugCategory/create", drugCategory).then(function (response) {
                    return response.data;
                });
            },
            remove: function (id) {
                return $http.delete("/api/drugCategory/delete/" + id);
            },
            update: function (drugCategory) {
                return $http.put("/api/drugCategory/update", drugCategory).then(function (response) {
                    return response.data;
                });
            }
        };
    }]);