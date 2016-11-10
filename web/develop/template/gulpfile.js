/**
 * Created by sunshine on 2016/11/8.
 */
var gulp = require('gulp');

gulp.task('html', function () {
    return gulp.src('./**.vm').pipe(gulp.dest('../../WEB-INF/template/backend/'));
});

gulp.watch('./**.vm', ['html']);

gulp.task('build', function () {

});

gulp.task('default', function () {

});
