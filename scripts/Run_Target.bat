@echo off
setlocal

:: configuration
set BASEDIR=%~dp0
call %BASEDIR%\base\config.bat

cd /D %KETTLE_DIR%
call %BASEDIR%\base\kitchen.bat /level:Debug /file:"%KETTLE_HOME%\runtime\resolve_dependencies\main_resolve_dependencies.kjb" 
call %BASEDIR%\base\kitchen.bat /file:"%KETTLE_HOME%\runtime\executor\main_executor.kjb" /param:plugin=%1 /param:target=%2
endlocal
