var gulp = require('gulp');
var uglify = require('gulp-uglify');
var concat = require('gulp-concat');
var cleanCSS = require('gulp-clean-css');
var strip = require('gulp-strip-comments');
var replace = require('gulp-replace');
var googleWebFonts = require('gulp-google-webfonts');

var options = {
    fontsDir: './font/googlefonts/',
    cssDir: './css/',
    cssFilename: 'myGoogleFonts.css'
};


gulp.task('fonts', function () {
    return gulp.src('./font/googlefonts/fonts.list')
        .pipe(googleWebFonts(options))
        .pipe(gulp.dest('./'));
});

gulp.task('css', function () {

    gulp.src([
        './css/animate.css',
        // './css/animation.css',
        './css/bootstrap.css',
        './css/fonts.css',
        './css/md-icons.css',
        './css/angular-toggle-switch-bootstrap-3.css',
        './css/highlight.css',
        './css/chat.css',
        './bootstrap-select/css/nya-bs-select.css',
        './css/font-awesome-animation.css',
        './kdate/css/jquery.calendars.picker.css',
        './css/select.css',
        './chosen/chosen.css'
    ])
        .pipe(replace('/*!', '/*'))
        .pipe(concat('app.css'))
        .pipe(cleanCSS({specialComments: 'all'}))
        .pipe(gulp.dest('./'));

});

gulp.task('scripts', function () {

    gulp.src([

        './js/material.js',
        './js/mdl-ext.js',
        './js/fontawesome.js',
        './js/jquery.js',

        './kdate/js/jquery.calendars.js',
        './kdate/js/jquery.calendars-ar.js',
        './kdate/js/jquery.calendars-ar-EG.js',
        './kdate/js/jquery.plugin.js',
        './kdate/js/jquery.calendars.picker.js',
        './kdate/js/jquery.calendars.picker-ar.js',
        './kdate/js/jquery.calendars.picker-ar-EG.js',
        './kdate/js/jquery.calendars.picker.lang.js',
        './kdate/js/jquery.calendars.ummalqura.js',
        './kdate/js/jquery.calendars.ummalqura-ar.js',
        './kdate/js/jquery.calendars.plus.js',

        './js/jquery-ui.js',
        './js/angular.js',
        // './js/angular-locale_ar.js',
        './js/angular-sanitize.js',
        './js/angular-ui-router.js',
        './js/angular-animate.js',
        './js/angular-touch.js',

        './angular-spinner/spin.js',
        './angular-spinner/angular-spinner.js',
        './angular-spinner/angular-loading-spinner.js',

        './js/ui-bootstrap.js',
        './js/ui-bootstrap-tpls.js',

        './js/select.js',

        './sockjs/sockjs.js',
        './stomp-websocket/lib/stomp.js',
        './ng-stomp/ng-stomp.js',
        './kdate/kdate.module.js',
        './kdate/kdate.filter.js',
        './kdate/kdate.picker.js',
        './js/underscore.js',
        './js/lrDragNDrop.js',
        './js/contextMenu.js',
        './js/lrStickyHeader.js',
        './js/smart-table.js',
        './js/stStickyHeader.js',
        './js/angular-pageslide-directive.js',
        './js/elastic.js',
        './js/scrollglue.js',
        './ng-upload/angular-file-upload.js',
        './bootstrap-select/js/nya-bs-select.js',
        './js/angular-css.js',
        './js/angular-timer-all.js',
        './full-screen/angular-fullscreen.js',
        './animate-counter/angular-counter.js',
        './js/angular-focusmanager.js',
        './js/jcs-auto-validate.js',
        './js/angular-toggle-switch.js',
        './js/chosen.jquery.js',
        './chosen/angular-chosen.js',
        './js/sortable.js',
        './js/FileSaver.js',
        './js/jquery.noty.packaged.js',
        './angular-chart/Chart.js',
        './angular-chart/angular-chart.js',

        './init/config/config.js',
        './init/factory/companyFactory.js',
        './init/factory/customerFactory.js',
        './init/factory/supplierFactory.js',
        './init/factory/detectionTypeFactory.js',
        './init/factory/diagnosisFactory.js',
        './init/factory/doctorFactory.js',
        './init/factory/drugFactory.js',
        './init/factory/drugUnitFactory.js',
        './init/factory/drugCategoryFactory.js',
        './init/factory/employeeFactory.js',
        './init/factory/vacationFactory.js',
        './init/factory/vacationTypeFactory.js',
        './init/factory/deductionTypeFactory.js',
        './init/factory/deductionFactory.js',
        './init/factory/salaryFactory.js',
        './init/factory/falconFactory.js',
        './init/factory/orderAttachFactory.js',
        './init/factory/orderDetectionTypeAttachFactory.js',
        './init/factory/orderDetectionTypeFactory.js',
        './init/factory/orderFactory.js',
        './init/factory/orderReceiptFactory.js',
        './init/factory/personFactory.js',
        './init/factory/receiptFactory.js',
        './init/factory/teamFactory.js',
        './init/factory/teamRuleFactory.js',
        './init/factory/billBuyFactory.js',
        './init/factory/billBuyReceiptFactory.js',
        './init/factory/transactionBuyFactory.js',
        './init/factory/billSellDetectionFactory.js',
        './init/factory/transactionSellDetectionFactory.js',
        './init/factory/billSellFactory.js',
        './init/factory/billSellReceiptFactory.js',
        './init/factory/transactionSellFactory.js',
        './init/factory/bankFactory.js',
        './init/factory/bankReceiptFactory.js',
        './init/factory/fundFactory.js',
        './init/factory/fundReceiptFactory.js',

        './init/service/service.js',
        './init/directive/directive.js',
        './init/filter/filter.js',
        './init/run/run.js',

        './partials/home/home.js',
        './partials/menu/menu.js',
        './partials/company/company.js',

        './partials/team/team.js',
        './partials/team/teamCreateUpdate.js',

        './partials/customer/customerDetails.js',
        './partials/customer/customerFilter.js',
        './partials/customer/customerCreateUpdate.js',
        './partials/customer/customerFalconCreateUpdate.js',

        './partials/supplier/supplier.js',
        './partials/supplier/supplierCreateUpdate.js',

        './partials/fund/fund.js',
        './partials/fund/fundReceiptFilter.js',
        './partials/fund/fundReceiptCreate.js',
        './partials/fund/fundTransferToBank.js',

        './partials/bank/bank.js',
        './partials/bank/bankReceiptFilter.js',
        './partials/bank/bankReceiptCreate.js',

        './partials/doctor/doctor.js',
        './partials/doctor/doctorCreateUpdate.js',

        './partials/employee/employee.js',
        './partials/employee/employeeCreateUpdate.js',
        './partials/employee/vacationType/vacationTypeCreateUpdate.js',
        './partials/employee/vacation/vacationCreateUpdate.js',
        './partials/employee/deductionType/deductionTypeCreateUpdate.js',
        './partials/employee/deduction/deductionCreateUpdate.js',
        './partials/employee/salary/salaryCreateUpdate.js',

        './partials/detectionType/detectionType.js',
        './partials/detectionType/detectionTypeCreateUpdate.js',
        './partials/detectionType/detectionTypeHeavyWork.js',

        './partials/drug/drugFilter.js',
        './partials/drug/drugCreateUpdate.js',
        './partials/drug/drugDetails.js',
        './partials/drug/drugHeavyWork.js',
        './partials/drug/drugCategoryCreateUpdate.js',
        './partials/drug/drugCategoryHeavyWork.js',
        './partials/drug/drugTransactionBuyCreate.js',

        './partials/receipt/receipt.js',
        './partials/receipt/receiptCreateUpdate.js',

        './partials/billBuy/billBuy.js',
        './partials/billBuy/billBuyFilter.js',
        './partials/billBuy/billBuyDetails.js',
        './partials/billBuy/billBuyCreate.js',
        './partials/billBuy/billBuyCreateAddItem.js',
        './partials/billBuy/transactionBuyCreate.js',
        './partials/billBuy/billBuyReceiptCreate.js',
        './partials/billBuy/updatePrices.js',
        './partials/billBuy/updateQuantity.js',

        './partials/billSell/insideSalesCreate.js',
        './partials/billSell/insideSalesFilter.js',
        './partials/billSell/insideSalesDetails.js',

        './partials/billSell/outsideSalesCreate.js',
        './partials/billSell/outsideSalesCreateAddItem.js',
        './partials/billSell/outsideSalesFilter.js',
        './partials/billSell/outsideSalesDetails.js',

        './partials/billSell/billSellReceiptCreate.js',
        './partials/billSell/transactionSellCreate.js',

        './partials/falcon/falconCreateUpdate.js',
        './partials/falcon/falconFilter.js',
        './partials/falcon/falconDetails.js',

        './partials/order/orderCreate.js',
        './partials/order/orderDetectionTypeCreate.js',
        './partials/order/orderReceiptCreate.js',
        './partials/order/orderFilter.js',
        './partials/order/orderDetails.js',
        './partials/order/orderAttachUpload.js',

        './partials/diagnosis/diagnosis.js',
        './partials/diagnosis/diagnosisCreate.js',

        './partials/report/person/personsIn.js',

        './partials/report/detectionType/detectionTypeDetailsReport.js',

        './partials/report/customer/customerDetailsReport.js',

        './partials/report/falcon/falconDetailsReport.js',

        './partials/report/supplier/supplierDetailsReport.js',

        './partials/report/order/orderByDate.js',
        './partials/report/order/ordersDebtByDate.js',
        './partials/report/order/orderByList.js',
        './partials/report/order/orderDetailsByList.js',
        './partials/report/order/orderDetailsByDate.js',
        './partials/report/order/orderDetection.js',
        './partials/report/order/orderResult.js',

        './partials/report/drug/drugDetailsReport.js',

        './partials/report/billBuy/billBuysByDate.js',
        './partials/report/billBuy/billBuysDetailsByDate.js',

        './partials/report/billSell/insideSalesByDate.js',
        './partials/report/billSell/outsideSalesByDate.js',

        './partials/report/billSell/insideSalesDetailsByDate.js',
        './partials/report/billSell/outsideSalesDetailsByDate.js',

        './partials/help/help.js',
        './partials/profile/profile.js',
        './partials/about/about.js'

    ])
        .pipe(strip())
        .pipe(concat('app.js'))
        .pipe(uglify())
        .pipe(gulp.dest('./'))

});

gulp.task('default', ['css', 'scripts']);