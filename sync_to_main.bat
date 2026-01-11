@echo off
echo Checkout main > sync_log.txt
git checkout main >> sync_log.txt 2>&1
if %ERRORLEVEL% NEQ 0 exit /b %ERRORLEVEL%

echo Merging nelson >> sync_log.txt
git merge nelson >> sync_log.txt 2>&1
if %ERRORLEVEL% NEQ 0 exit /b %ERRORLEVEL%

echo Pushing main >> sync_log.txt
git push origin main >> sync_log.txt 2>&1
if %ERRORLEVEL% NEQ 0 exit /b %ERRORLEVEL%

echo Checkout back to nelson >> sync_log.txt
git checkout nelson >> sync_log.txt 2>&1
echo Done >> sync_log.txt
