@echo off
echo Starting > debug_output.txt
git status >> debug_output.txt 2>&1
git branch -a >> debug_output.txt 2>&1
echo REMOTES >> debug_output.txt
git remote -v >> debug_output.txt 2>&1
echo Done >> debug_output.txt
