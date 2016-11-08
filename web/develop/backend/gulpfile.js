/**
 * Created by sunshine on 2016/11/8.
 */
var gulp = require('gulp');

gulp.task('sass', function () {
    return gulp.src('/develop/backend/material/scss/*.scss').pipe(sass()).pipe(gulp.dest('/material/css/backend/'));
});

gulp.task('script', function () {
    return gulp.src('/develop/backend/material/js/*.js').pipe(uglify()).pipe(gulp.dest('/material/js/backend/'))
});

gulp.task('build', function () {

});

gulp.task('default', function () {

});
