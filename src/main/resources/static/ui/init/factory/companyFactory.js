app.factory("CompanyService",
    ['$http', '$log', function ($http, $log) {
        return {
            findAll: function () {
                return $http.get("/api/company/findAll").then(function (response) {
                    return response.data;
                });
            },
            findOne: function (id) {
                return $http.get("/api/company/findOne/" + id).then(function (response) {
                    return response.data;
                });
            },
            update: function (company) {
                return $http.put("/api/company/update", company).then(function (response) {
                    return response.data;
                });
            },
            uploadCompanyLogo: function (file) {
                var fd = new FormData();
                fd.append('file', file);
                return $http.post("/uploadCompanyLogo", fd, {transformRequest: angular.identity, headers: {'Content-Type': undefined}}).then(function (response) {
                    return response.data;
                });
            }
        };
    }]);