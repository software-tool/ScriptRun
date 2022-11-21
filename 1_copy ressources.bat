:: Copy resources of linked libraries to the current project

:: Basedir
set basedir=.

:: Common Code

copy %basedir%\srcApplicationFx\com\rwu\fx\fontsize\*.css %basedir%\src\main\resources\com\rwu\fx\fontsize
copy %basedir%\srcApplicationFx\com\rwu\fx\tabsize\*.css %basedir%\src\main\resources\com\rwu\fx\tabsize
copy %basedir%\srcApplicationFx\com\rwu\fx\dialog\*.css %basedir%\src\main\resources\com\rwu\fx\dialog

:: AnchorFX
copy %basedir%\resAnchorfx\* %basedir%\src\main\resources

pause