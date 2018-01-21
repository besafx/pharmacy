app.factory("PersonService",
    ['$http', '$log', function ($http, $log) {
        return {
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
            setStyle: function (style) {
                return $http.get("/api/person/setStyle/" + style).then(function (response) {
                    return response.data;
                });
            },
            findActivePerson: function () {
                return $http.get("/api/person/findActivePerson").then(function (response) {
                    return response.data;
                });
            },
            uploadPersonPhoto: function (file) {
                var fd = new FormData();
                fd.append('file', file);
                return $http.post("/uploadUserPhoto", fd, {transformRequest: angular.identity, headers: {'Content-Type': undefined}}).then(function (response) {
                    return response.data;
                });
            }
        };
    }]);