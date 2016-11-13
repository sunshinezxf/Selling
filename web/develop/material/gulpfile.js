/**
 * Created by sunshine on 2016/11/8.
 */
var gulp = require('gulp');

var sass = require('gulp-sass');

var uglify = require('gulp-uglify');

gulp.task('sass', function () {
    return gulp.src('./scss/**.scss').pipe(sass()).pipe(gulp.dest('../../material/css/backend/'));
});

gulp.task('script', function () {
    return gulp.src('./js/**.js').pipe(uglify()).pipe(gulp.dest('/material/js/backend/'))
});

gulp.task('build', function () {

});

gulp.task('default', function () {

});
