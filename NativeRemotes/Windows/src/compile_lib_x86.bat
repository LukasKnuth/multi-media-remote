@echo off
REM Compiles the Windows-Binarys on my Windows 8 x86 machine.
REM Paths might not match for your machine! Also, this script should be run from the Visual C++ Command Prompt in order to use the cl-tool.
cl /EHsc /I "C:\Program Files\Java\jdk1.7.0\include" /I "C:\Program Files\Java\jdk1.7.0\include\win32" /LD /MD "C:\Program Files\Microsoft SDKs\Windows\v7.0A\Lib\User32.Lib" remote.cpp /Fe../bin/WindowsRemote_x86.dll
