@echo off
setlocal

:: configuration
set BASEDIR=%~dp0
call %BASEDIR%\base\config.bat

cd /D %KETTLE_DIR%
call %BASEDIR%\base\kitchen.bat /file:"%KETTLE_HOME%\runtime\executor\install_plugin.kjb" /param:plugin=%1
endlocal