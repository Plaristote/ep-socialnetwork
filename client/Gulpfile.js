var gulp         = require('gulp');
var run_sequence = require('run-sequence');
var bower        = require('gulp-bower');
var bower_files  = require('main-bower-files');
var concat       = require('gulp-concat');
var sass         = require('gulp-sass');
var coffee       = require('gulp-coffee');
var gulp_if      = require('gulp-if');
var neat         = require('node-neat');
var include      = require('gulp-include');
var eco          = require('gulp-eco');

var paths = {
  scss: [
    'vendor/bower/normalize.css',
    'vendor/bower/**/*.css',
    'vendor/bower/**/*.scss',
    'src/scss/application.scss'
  ],
  javascript: [
    'vendor/bower/jquery.js',
    'vendor/bower/underscore.js',
    'vendor/bower/**/*.js',
    'tmp/jst.js',
    'src/coffee/**/*.coffee'
  ],
  javascriptToCompile: [
    'vendor/bower/jquery.js',
    'vendor/bower/underscore.js',
    'vendor/bower/**/*.js',
    'tmp/jst.js',
    'src/coffee/application.coffee'
  ],
  eco: [
    'src/templates/**/*.eco'
  ]
};

gulp.task('bower-files', ['bower'], function() {
  return gulp.src(bower_files()).pipe(gulp.dest('./vendor/bower'));
});

gulp.task('bower', function() {
  return bower();
});

gulp.task('scss', function() {
  return gulp.src(paths.scss).
    pipe(sass({
      includePaths: [ 'src/scss', 'vendor/bower' ].concat(neat.includePaths)
    })).
    pipe(concat('style.css')).
    pipe(gulp.dest('../public/stylesheets'));
});

gulp.task('eco', function() {
  return gulp.src(paths.eco).
    pipe(eco()).
    pipe(concat('jst.js')).
    pipe(gulp.dest('tmp/'));
});

gulp.task('javascript', function() {
  return gulp.src(paths.javascriptToCompile).
    pipe(include()).
    pipe(gulp_if(/[.]coffee$/, coffee())).
    pipe(concat('application.js')).
    pipe(gulp.dest('../public/javascripts'));
});

gulp.task('recompile', function() {
  run_sequence('bower', 'bower-files', ['scss', 'eco', 'javascript']);
});

gulp.task('default', ['recompile'], function() {
  gulp.watch(['bower.json'],         ['bower-files']);
  gulp.watch(['src/scss/**/*.scss'], ['scss']);
  gulp.watch(paths.javascript,       ['javascript']);
  gulp.watch(paths.eco,              ['eco', 'javascript']);
});
