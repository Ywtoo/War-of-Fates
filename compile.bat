@echo off
if not exist out (
  mkdir out
)
javac -d out src\characters\*.java src\battle\*.java src\arena\*.java src\ui\*.java src/Main.java
if errorlevel 1 (
  echo Compilacao falhou.
  pause
  exit /b 1
)
echo Compilacao concluida.