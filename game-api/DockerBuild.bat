@echo off
rem must copy file...
xcopy ..\game-api-scripts\src game-api-scripts\src\ /s /e /q 2>nul

docker image build -t game-api:releases .

if exist game-api-scripts rd /s/q game-api-scripts
