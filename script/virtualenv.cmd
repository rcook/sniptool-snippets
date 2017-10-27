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
pip_path=$root_dir/bin/pip

if [ ! -e $python_path ]; then
    virtualenv $root_dir
fi

$pip_path install -q -r $root_dir/requirements.txt

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
set pip_path=%root_dir%\Scripts\pip.exe

if not exist "%python_path%" (
    cd /d "%root_dir%"
    python -m virtualenv . || exit /b 1
)

"%pip_path%" install -q -r "%root_dir%\requirements.txt" || exit /b 1
exit /b 0
