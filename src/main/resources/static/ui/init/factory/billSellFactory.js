app.factory("BillSellService",
    ['$http', '$log', function ($http, $log) {
        return {
            findAll: function () {
                return $http.get("/api/billSell/findAll").then(function (response) {
                    return response.data;
                });
            },
            findAllCombo: function () {
                return $http.get("/api/billSell/findAllCombo").then(function (response) {
                    return response.data;
                });
            },
            findOne: function (id) {
                return $http.get("/api/billSell/findOne/" + id).then(function (response) {
                    return response.data;
                });
            },
            create: function (billSell) {
                return $http.post("/api/billSell/create", billSell).then(function (response) {
                    return response.data;
                });
            },
            remove: function (id) {
                return $http.delete("/api/billSell/delete/" + id);
            },
            filter: function (search) {
                return $http.get("/api/billSell/filter?" + search).then(function (response) {
                    return response.data;
                });
            },
            filterInside: function (search) {
                return $http.get("/api/billSell/filterInside?" + search).then(function (response) {
                    return response.data;
                });
            },
            filterOutside: function (search) {
                return $http.get("/api/billSell/filterOutside?" + search).then(function (response) {
                    return response.data;
                });
            },
            findInsideSalesByToday: function () {
                return $http.get("/api/billSell/findInsideSalesByToday").then(function (response) {
                    return response.data;
                });
            },
            findInsideSalesByWeek: function () {
                return $http.get("/api/billSell/findInsideSalesByWeek").then(function (response) {
                    return response.data;
                });
            },
            findInsideSalesByMonth: function () {
                return $http.get("/api/billSell/findInsideSalesByMonth").then(function (response) {
                    return response.data;
                });
            },
            findInsideSalesByYear: function () {
                return $http.get("/api/billSell/findInsideSalesByYear").then(function (response) {
                    return response.data;
                });
            },
            findOutsideSalesByToday: function () {
                return $http.get("/api/billSell/findOutsideSalesByToday").then(function (response) {
                    return response.data;
                });
            },
            findOutsideSalesByWeek: function () {
                return $http.get("/api/billSell/findOutsideSalesByWeek").then(function (response) {
                    return response.data;
                });
            },
            findOutsideSalesByMonth: function () {
                return $http.get("/api/billSell/findOutsideSalesByMonth").then(function (response) {
                    return response.data;
                });
            },
            findOutsideSalesByYear: function () {
                return $http.get("/api/billSell/findOutsideSalesByYear").then(function (response) {
                    return response.data;
                });
            }
        };
    }]);