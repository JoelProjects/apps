@echo off

setlocal

@rem set JAVA_HOME=C:\Program Files\Java\jdk1.5.0_14
set JAVA_HOME=C:\Program Files\Java\jre6
set PATH=%JAVA_HOME%\bin
SET MYCLASS=lib\store.jar;lib\.

java -classpath %MYCLASS% store.ui.StoreManager %1 %2 %3 %4 %5

endlocal

@echo on
