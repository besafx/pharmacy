app.factory("PersonService",
    ['$http', '$log', function ($http, $log) {
        return {
            findAll: function () {
                return $http.get("/api/person/findAll").then(function (response) {
                    return response.data;
                });
            },
            findAllSummery: function () {
                return $http.get("/api/person/findAllSummery").then(function (response) {
                    return response.data;
                });
            },
            findOne: function (id) {
                return $http.get("/api/person/findOne/" + id).then(function (response) {
                    return response.data;
                });
            },
            create: function (person) {
                return $http.post("/api/person/create", person).then(function (response) {
                    return response.data;
                });
            },
            remove: function (id) {
                return $http.delete("/api/person/delete/" + id);
            },
            update: function (person) {
                return $http.put("/api/person/update", person).then(function (response) {
                    return response.data;
                });
            },
            setGUILang: function (lang) {
                return $http.get("/api/person/setGUILang/" + lang).then(function (response) {
                    return response.data;
                });
            },
            setDateType: function (dateType) {
                return $http.get("/api/person/setDateType/" + dateType).then(function (response) {
                    return response.data;
                });
            },
            findActivePerson: function () {
                return $http.get("/api/person/findActivePerson").then(function (response) {
                    return response.data;
                });
            },
            findAuthorities: function () {
                return $http.get("/api/person/findAuthorities").then(function (response) {
                    return response.data;
                });
            }
        };
    }]);