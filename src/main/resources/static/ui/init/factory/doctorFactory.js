app.factory("DoctorService",
    ['$http', '$log', function ($http, $log) {
        return {
            findAll: function () {
                return $http.get("/api/doctor/findAll").then(function (response) {
                    return response.data;
                });
            },
            findAllCombo: function () {
                return $http.get("/api/doctor/findAllCombo").then(function (response) {
                    return response.data;
                });
            },
            findOne: function (id) {
                return $http.get("/api/doctor/findOne/" + id).then(function (response) {
                    return response.data;
                });
            },
            create: function (doctor) {
                return $http.post("/api/doctor/create", doctor).then(function (response) {
                    return response.data;
                });
            },
            remove: function (id) {
                return $http.delete("/api/doctor/delete/" + id);
            },
            update: function (doctor) {
                return $http.put("/api/doctor/update", doctor).then(function (response) {
                    return response.data;
                });
            },
            setGUILang: function (doctor, lang) {
                return $http.get("/api/doctor/setGUILang/" + doctor.id + "/" + lang).then(function (response) {
                    return response.data;
                });
            },
            setDateType: function (doctor, dateType) {
                return $http.get("/api/doctor/setDateType/" + doctor.id + "/" + dateType).then(function (response) {
                    return response.data;
                });
            },
            enable: function (doctor) {
                return $http.get("/api/doctor/enable/" + doctor.id).then(function (response) {
                    return response.data;
                });
            },
            disable: function (doctor) {
                return $http.get("/api/doctor/disable/" + doctor.id).then(function (response) {
                    return response.data;
                });
            }
        };
    }]);