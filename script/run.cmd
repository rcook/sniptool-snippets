:<<"::CMDLITERAL"
@echo off
goto :CMDSCRIPT
::CMDLITERAL
#!/bin/bash
set -euo pipefail
IFS=$'\n\t'

script_dir=$(cd $(dirname $0); pwd -P)
root_dir=$(dirname $script_dir)
python_path=$root_dir/bin/python

if [ ! -e $python_path ]; then
    echo "Please create or update your virtual environment by running script/virtualenv"
    exit 1
fi

$python_path $*

exit $?
:CMDSCRIPT
setlocal
set args=%*
call :Main "%~dp0" "%~dp0.." || exit /b 1
exit /b 0

:Main
set x=%~f1
set script_dir=%x:~0,-1%
set root_dir=%~f2
set python_path=%root_dir%\Scripts\python.exe

if not exist "%python_path%" (
    echo Please create or update your virtual environment by running script\virtualenv
    exit /b 1
)

"%python_path%" %args% || exit /b 1
exit /b 0
