@echo off
setlocal

:: configuration
set BASEDIR=%~dp0
call %BASEDIR%\base\config.bat

cd /D %KETTLE_DIR%
call %BASEDIR%\base\kitchen.bat /file:"%KETTLE_HOME%\runtime\build_package\build_package.kjb" /param:org=%1 /param:name=%2 /param:rev=%3
endlocal
