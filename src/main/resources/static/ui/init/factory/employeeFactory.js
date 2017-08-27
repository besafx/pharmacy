app.factory("EmployeeService",
    ['$http', '$log', function ($http, $log) {
        return {
            findAll: function () {
                return $http.get("/api/employee/findAll").then(function (response) {
                    return response.data;
                });
            },
            findAllCombo: function () {
                return $http.get("/api/employee/findAllCombo").then(function (response) {
                    return response.data;
                });
            },
            findOne: function (id) {
                return $http.get("/api/employee/findOne/" + id).then(function (response) {
                    return response.data;
                });
            },
            create: function (employee) {
                return $http.post("/api/employee/create", employee).then(function (response) {
                    return response.data;
                });
            },
            remove: function (id) {
                return $http.delete("/api/employee/delete/" + id);
            },
            update: function (employee) {
                return $http.put("/api/employee/update", employee).then(function (response) {
                    return response.data;
                });
            },
            setGUILang: function (employee, lang) {
                return $http.get("/api/employee/setGUILang/" + employee.id + "/" + lang).then(function (response) {
                    return response.data;
                });
            },
            setDateType: function (employee, dateType) {
                return $http.get("/api/employee/setDateType/" + employee.id + "/" + dateType).then(function (response) {
                    return response.data;
                });
            },
            enable: function (employee) {
                return $http.get("/api/employee/enable/" + employee.id).then(function (response) {
                    return response.data;
                });
            },
            disable: function (employee) {
                return $http.get("/api/employee/disable/" + employee.id).then(function (response) {
                    return response.data;
                });
            }
        };
    }]);