{#- .name: Windows batch file -#}
@echo off
setlocal
set args=%*
call :Main "%~dp0"
goto :eof

:Main
set x=%~f1
set this_dir=%x:~0,-1%
echo this_dir=%this_dir%
echo args=%args%
goto :eof
