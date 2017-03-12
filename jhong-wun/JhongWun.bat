@echo off

setlocal

set PATH=%JAVA_HOME%\bin
SET MYCLASS=.;lib\jhong-wun.jar;

rem java -classpath %MYCLASS% jhongwun.manager.QuestionSetReader
java -classpath %MYCLASS% jhongwun.ui.JhongWunTest

endlocal

@echo on
