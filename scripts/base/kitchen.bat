@echo off
setlocal

if "%KETTLE_DIR%" == "" call "%~dp0config.bat"
call "%~dp0init.bat"

call %KETTLE_DIR%\kitchen.bat %* /level:%KETTLE_LOG_LEVEL%
