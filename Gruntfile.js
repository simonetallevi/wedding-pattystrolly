module.exports = function(grunt) {

  // Project configuration.
  grunt.initConfig({
    pkg: grunt.file.readJSON('package.json'),
    clean: {
      build: {
        src: ['out']
      }
    },
    copy: {
        options: {},
        files: {
            cwd: 'web',     // set working folder / root to copy
            src: ['**/*.woff2','**/*.woff','**/*.otf',
                    '**/*.eot','**/*.svg','**/*.ttf',
                    '**/*.jpg'],    // copy all files and subfolders
            dest: 'out',    // destination folder
            expand: true    // required when using cwd
        }
    },
    concat: {
         options: {
           separator: ';',
         },
         dist: {
           src: ['web/vendor/**/*.js'],
           dest: 'out/js/vendor.min.js',
         },
    }
  });

  grunt.loadNpmTasks('grunt-contrib-concat');
  grunt.loadNpmTasks('grunt-contrib-copy');
  grunt.loadNpmTasks('grunt-contrib-clean');

  // Default task(s).
  grunt.registerTask('default', ['clean','concat','copy']);

};