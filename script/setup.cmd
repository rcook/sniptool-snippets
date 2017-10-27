:<<"::CMDLITERAL"
@echo off
goto :CMDSCRIPT
::CMDLITERAL
#!/bin/bash
set -euo pipefail
IFS=$'\n\t'

script_dir=$(cd $(dirname $0); pwd -P)
root_dir=$(dirname $script_dir)

$script_dir/run $root_dir/setup.py $*

exit $?
:CMDSCRIPT
setlocal
set args=%*
call :Main "%~dp0" "%~dp0.."
exit /b %errorlevel%

:Main
set x=%~f1
set script_dir=%x:~0,-1%
set root_dir=%~f2
call "%script_dir%\run.cmd" "%root_dir%\setup.py" %args%
exit /b %errorlevel%
