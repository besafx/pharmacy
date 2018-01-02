app.factory("FundReceiptService",
    ['$http', '$log', function ($http, $log) {
        return {
            findOne: function (id) {
                return $http.get("/api/fundReceipt/findOne/" + id).then(function (response) {
                    return response.data;
                });
            },
            findByBank: function (fundId) {
                return $http.get("/api/fundReceipt/findByBank/" + fundId).then(function (response) {
                    return response.data;
                });
            },
            createIn: function (fundReceipt) {
                return $http.post("/api/fundReceipt/createIn", fundReceipt).then(function (response) {
                    return response.data;
                });
            },
            createOut: function (fundReceipt) {
                return $http.post("/api/fundReceipt/createOut", fundReceipt).then(function (response) {
                    return response.data;
                });
            },
            remove: function (id) {
                return $http.delete("/api/fundReceipt/delete/" + id);
            },
            filter: function (search) {
                return $http.get("/api/fundReceipt/filter?" + search).then(function (response) {
                    return response.data;
                });
            },
            findByTodayIn: function () {
                return $http.get("/api/fundReceipt/findByToday/In").then(function (response) {
                    return response.data;
                });
            },
            findByWeekIn: function () {
                return $http.get("/api/fundReceipt/findByWeek/In").then(function (response) {
                    return response.data;
                });
            },
            findByMonthIn: function () {
                return $http.get("/api/fundReceipt/findByMonth/In").then(function (response) {
                    return response.data;
                });
            },
            findByYearIn: function () {
                return $http.get("/api/fundReceipt/findByYear/In").then(function (response) {
                    return response.data;
                });
            },
            findByTodayOut: function () {
                return $http.get("/api/fundReceipt/findByToday/Out").then(function (response) {
                    return response.data;
                });
            },
            findByWeekOut: function () {
                return $http.get("/api/fundReceipt/findByWeek/Out").then(function (response) {
                    return response.data;
                });
            },
            findByMonthOut: function () {
                return $http.get("/api/fundReceipt/findByMonth/Out").then(function (response) {
                    return response.data;
                });
            },
            findByYearOut: function () {
                return $http.get("/api/fundReceipt/findByYear/Out").then(function (response) {
                    return response.data;
                });
            }
        };
    }]);