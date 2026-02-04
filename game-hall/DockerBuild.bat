@echo off
rem must copy file...
xcopy ..\game-hall-scripts\src game-hall-scripts\src\ /s /e /q 2>nul

docker image build -t game-hall:releases .

if exist game-hall-scripts rd /s/q game-hall-scripts
