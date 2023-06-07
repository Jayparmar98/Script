@echo off

setlocal

set JDK_PATH=C:\Program Files\Java\jdk-17

cd "C:\jenkins_workspace\workspace\DeskBook-FEB\Deskbook-Backend-Java-Feb\"

call "%JDK_PATH%\bin\javac" -version
call "%JDK_PATH%\bin\java" -version

call mvn clean install

endlocal
