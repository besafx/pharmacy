app.factory("BankReceiptService",
    ['$http', '$log', function ($http, $log) {
        return {
            findOne: function (id) {
                return $http.get("/api/bankReceipt/findOne/" + id).then(function (response) {
                    return response.data;
                });
            },
            findByBank: function (bankId) {
                return $http.get("/api/bankReceipt/findByBank/" + bankId).then(function (response) {
                    return response.data;
                });
            },
            createIn: function (bankReceipt) {
                return $http.post("/api/bankReceipt/createIn", bankReceipt).then(function (response) {
                    return response.data;
                });
            },
            createOut: function (bankReceipt) {
                return $http.post("/api/bankReceipt/createOut", bankReceipt).then(function (response) {
                    return response.data;
                });
            },
            remove: function (id) {
                return $http.delete("/api/bankReceipt/delete/" + id);
            },
            filter: function (search) {
                return $http.get("/api/bankReceipt/filter?" + search).then(function (response) {
                    return response.data;
                });
            },
            findByTodayIn: function () {
                return $http.get("/api/bankReceipt/findByToday/In").then(function (response) {
                    return response.data;
                });
            },
            findByWeekIn: function () {
                return $http.get("/api/bankReceipt/findByWeek/In").then(function (response) {
                    return response.data;
                });
            },
            findByMonthIn: function () {
                return $http.get("/api/bankReceipt/findByMonth/In").then(function (response) {
                    return response.data;
                });
            },
            findByYearIn: function () {
                return $http.get("/api/bankReceipt/findByYear/In").then(function (response) {
                    return response.data;
                });
            },
            findByTodayOut: function () {
                return $http.get("/api/bankReceipt/findByToday/Out").then(function (response) {
                    return response.data;
                });
            },
            findByWeekOut: function () {
                return $http.get("/api/bankReceipt/findByWeek/Out").then(function (response) {
                    return response.data;
                });
            },
            findByMonthOut: function () {
                return $http.get("/api/bankReceipt/findByMonth/Out").then(function (response) {
                    return response.data;
                });
            },
            findByYearOut: function () {
                return $http.get("/api/bankReceipt/findByYear/Out").then(function (response) {
                    return response.data;
                });
            }
        };
    }]);