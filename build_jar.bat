@echo off
set "JAVA_HOME_PATH=C:\Program Files\Java\jdk1.8.0_202"
set "JAVAC=%JAVA_HOME_PATH%\bin\javac.exe"
set "JAR=%JAVA_HOME_PATH%\bin\jar.exe"

if not exist "%JAVAC%" (
    echo Error: JDK not found at %JAVA_HOME_PATH%
    echo Please ensure you have a JDK installed.
    pause
    exit /b 1
)

echo Compiling Art Gallery Project using %JAVAC%...
if not exist bin mkdir bin
dir /s /b src\*.java > sources.txt
"%JAVAC%" -encoding UTF-8 -d bin -cp "lib/*" @sources.txt
if %ERRORLEVEL% neq 0 (
    echo Compilation failed!
    pause
    exit /b %ERRORLEVEL%
)
del sources.txt

echo Packaging into ArtGallery.jar...
"%JAR%" cvfm ArtGallery.jar manifest.txt -C bin .
if %ERRORLEVEL% neq 0 (
    echo Packaging failed!
    pause
    exit /b %ERRORLEVEL%
)

echo Success! ArtGallery.jar created.
pause
