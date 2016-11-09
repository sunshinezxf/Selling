/**
 * Created by sunshine on 2016/11/8.
 */
var gulp = require('gulp');

var sass = require('gulp-sass');

var uglify = require('gulp-uglify');

gulp.task('sass', function () {
    return gulp.src('/develop/backend/material/scss/*.scss').pipe(sass()).pipe(gulp.dest('/material/css/backend/'));
});

gulp.task('script', function () {
    return gulp.src('/develop/backend/material/js/*.js').pipe(uglify()).pipe(gulp.dest('/material/js/backend/'))
});

gulp.task('html', function () {
    return gulp.src('template/**.vm').pipe(gulp.dest('../../WEB-INF/template/backend/'));
});

gulp.watch('template/**.vm', ['html']);

gulp.task('build', function () {

});

gulp.task('default', function () {

});
