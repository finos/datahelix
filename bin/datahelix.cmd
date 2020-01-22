@echo off

set scriptPath=%~dp0
java -jar %scriptPath%\generator.jar "%*"
