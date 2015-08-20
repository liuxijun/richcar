set APP_DIR=new
md %APP_DIR%
set DEST_DIR=%APP_DIR%\WEB-INF\classes
xcopy /s /EXCLUDE:excludeFiles /d:08-20-2015 ..\out\artifacts\CarWeb_war_exploded %APP_DIR%
REM copy src\jdbc.online.properties %DEST_DIR%\jdbc.properties
REM copy src\log4j.online.properties %DEST_DIR%\log4j.properties
REM copy src\fortune_application.online.properties %DEST_DIR%\fortune_application.properties
pause