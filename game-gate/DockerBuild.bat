@echo off
rem must copy file...
xcopy ..\game-gate-scripts\src game-gate-scripts\src\ /s /e /q 2>nul

docker image build -t game-gate:releases .

if exist game-gate-scripts rd /s/q game-gate-scripts
