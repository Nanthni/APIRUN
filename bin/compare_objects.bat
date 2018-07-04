@echo off
cd..

Set Root=%cd%
Set Classpath=%Root%\lib\*;
call java -cp %Classpath% com.orion.regression.ObjectRegression