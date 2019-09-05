::关闭回显,什么是回显?，就是会把命令也输出,而不会是只输出命令的结果
@echo off
echo "======= begin build your APP ======="
::定位到要调用的可执行文件的目录[这个目录按实际情况更改]
cd /d "C:\Users\HLWK-06\Desktop\tenant\"
::call指令的具体作用是调用其他.cmd/.bat文件,同时在调用其他可执行文件时,可以添加参数
call  gradlew.bat as