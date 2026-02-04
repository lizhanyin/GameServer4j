@echo off
rem must copy file...

docker image build -t game-manage:releases .

if exist game-manage-scripts rd /s/q game-manage-scripts
