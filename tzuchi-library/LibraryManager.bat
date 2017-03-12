@echo off

setlocal

set JAVA_HOME=C:\Program Files\Java\jre6
set PATH=%JAVA_HOME%\bin
SET MYCLASS=lib\library.jar;lib\.

java -classpath %MYCLASS% asset.ui.LibraryManager %1 %2 %3 %4 %5

endlocal

@echo on
