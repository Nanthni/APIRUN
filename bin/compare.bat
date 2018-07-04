@echo off
cd..
Set Root=%cd%
echo Deleting input object JSON files..
del /f /q %Root%\input\objects\*.*
echo Deleting input relation JSON files..
del /f /q %Root%\input\relations\*.*
echo Deleting output object JSON files..
del /f /q %Root%\output\objects\*.*
echo Deleting output relation JSON files..
del /f /q %Root%\output\relations\*.*
Set Classpath=%Root%\lib\*;
call java -cp %Classpath% com.orion.regression.ObjectRegression
call java -cp %Classpath% com.orion.regression.RelationRegression