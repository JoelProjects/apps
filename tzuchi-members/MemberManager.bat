@echo off

setlocal

set JAVA_HOME=C:\Program Files\Java\jdk1.5.0_11
set PATH=%JAVA_HOME%\bin
SET MYCLASS=lib\.;lib\members-common.jar;lib\members-ui.jar;lib\mysql-connector-java-3.1.14-bin.jar^
;lib\hibernate3.jar;lib\dom4j-1.6.1.jar;lib\log4j-1.2.11.jar;lib\commons-logging-1.0.4.jar;lib\commons-collections-2.1.1.jar^
;lib\c3p0-0.9.1.jar;lib\cglib-2.1.3.jar;lib\asm.jar;lib\jta.jar;lib\asm-attrs.jar;lib\antlr-2.7.6.jar

java -classpath %MYCLASS% members.ui.MembersManager %1 %2 %3 %4 %5

endlocal

@echo on
