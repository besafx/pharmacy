app.factory("TeamService",
    ['$http', '$log', function ($http, $log) {
        return {
            findAll: function () {
                return $http.get("/api/team/findAll").then(function (response) {
                    return response.data;
                });
            },
            findAllSummery: function () {
                return $http.get("/api/team/findAllSummery").then(function (response) {
                    return response.data;
                });
            },
            findOne: function (id) {
                return $http.get("/api/team/findOne/" + id).then(function (response) {
                    return response.data;
                });
            },
            create: function (team) {
                return $http.post("/api/team/create", team).then(function (response) {
                    return response.data;
                });
            },
            remove: function (id) {
                return $http.delete("/api/team/delete/" + id);
            },
            update: function (team) {
                return $http.put("/api/team/update", team).then(function (response) {
                    return response.data;
                });
            }
        };
    }]);