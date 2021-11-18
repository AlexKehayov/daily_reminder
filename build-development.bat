@echo off
echo.
echo [93m --------------------  BUILDING DAILY REMINDER [PROFILE: LOCAL]  -------------------- [0m
echo.

call %USERPROFILE%/java_17.bat
call mvn clean install -s settings.xml -P local -DskipTests || goto :error

echo.
echo [92m -------------------- BUILT SUCCESSFUL  -------------------- [0m
echo.

:error
if %errorlevel% neq 0 (
	echo.
	echo [91m -------------------- FAIL -------------------- [0m
	echo.
)
exit /b %errorlevel%

