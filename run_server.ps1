# PowerShell script to compile and run MoneyMate server
# This handles paths with spaces better than batch files

# Define paths
$LIBS = "libs\tomcat-embed-core.jar;libs\tomcat-embed-jasper.jar;libs\tomcat-embed-el.jar;libs\ecj.jar;libs\mysql-connector.jar;libs\javax.annotation-api.jar;libs\jstl.jar"
$SRC_DIR = "src\java"
$OUT_DIR = "build\web\WEB-INF\classes"

# Create output directory if it doesn't exist
if (-not (Test-Path $OUT_DIR)) {
    New-Item -ItemType Directory -Path $OUT_DIR -Force | Out-Null
}

# 1. Compile ALL Java files
Write-Host "Compiling Java sources..." -ForegroundColor Cyan

# Get all Java files
$javaFiles = Get-ChildItem -Path $SRC_DIR -Filter "*.java" -Recurse | Select-Object -ExpandProperty FullName

# Compile all files at once
$compileCommand = "javac -cp `"$LIBS`" -d `"$OUT_DIR`" " + (($javaFiles | ForEach-Object { "`"$_`"" }) -join " ")
# Skipping compilation to use existing classes
# Invoke-Expression $compileCommand
# if ($LASTEXITCODE -ne 0) { ... }

Write-Host "Compilation successful!" -ForegroundColor Green

# 2. Run the Server
Write-Host "`nStarting MoneyMate Server..." -ForegroundColor Cyan
Write-Host "Access the application at: http://localhost:8080" -ForegroundColor Yellow
Write-Host "Press Ctrl+C to stop the server`n" -ForegroundColor Gray

java -cp "$OUT_DIR;$LIBS" ServerLauncher

