/**
 * Created by sunshine on 2016/11/8.
 */
var gulp = require('gulp');

gulp.task('html', function () {
    return gulp.src('template/**.vm').pipe(gulp.dest('../../WEB-INF/template/backend/'));
});

gulp.watch('template/**.vm', ['html']);

gulp.task('build', function () {

});

gulp.task('default', function () {

});
