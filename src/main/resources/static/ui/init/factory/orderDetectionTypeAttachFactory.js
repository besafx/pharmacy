app.factory("OrderDetectionTypeAttachService",
    ['$http', '$log', function ($http, $log) {
        return {
            upload: function (orderDetectionType, fileName, mimeType, description, file) {
                var fd = new FormData();
                fd.append('file', file);
                return $http.post("/api/orderDetectionTypeAttach/upload?orderDetectionTypeId=" + orderDetectionType.id + "&fileName=" + fileName + "&mimeType=" + mimeType + "&description=" + description + "&remote=true",
                    fd, {transformRequest: angular.identity, headers: {'Content-Type': undefined}}).then(function (response) {
                    return response.data;
                });
            },
            remove: function (orderDetectionTypeAttach) {
                return $http.delete("/api/orderDetectionTypeAttach/delete/" + orderDetectionTypeAttach.id).then(function (response) {
                    return response.data;
                });
            },
            removeWhatever: function (orderDetectionTypeAttach) {
                return $http.delete("/api/orderDetectionTypeAttach/deleteWhatever/" + orderDetectionTypeAttach.id);
            },
            findByOrderDetectionType: function (orderDetectionType) {
                return $http.get("/api/orderDetectionTypeAttach/findByOrderDetectionType/" + orderDetectionType.id).then(function (response) {
                    return response.data;
                });
            }
        };
    }]);