# RUN INSTRUCTIONS: type .\run_test.ps1 in terminal, and follow instructions

# Compile the java files
javac Server.java NaiveClient.java BufferClient.java

# Prompt for inputs
$port = Read-Host "Enter server port"
$maxClients = Read-Host "Enter max concurrent clients for server"
$numTerminals = Read-Host "Enter number of client terminals to open"

# 1. Launch Server in a new terminal
Start-Process powershell -ArgumentList "-NoExit", "-Command", "java Server $port $maxClients"

# Enter string and n concurrent windows
Read-Host "Configure server window (enter str and N) first, then press Enter to launch BufferClients..."

# 2. Launch BufferClients in new terminal
for ($i = 1; $i -le $numTerminals; $i++) {
    Start-Process powershell -ArgumentList "-NoExit", "-Command", "Write-Host 'BufferClient $i'; java BufferClient 127.0.0.1 $port"
}

Read-Host "Press Enter to launch NaiveClients..."

# 3. Launch NaiveClients in new terminal
for ($i = 1; $i -le $numTerminals; $i++) {
    Start-Process powershell -ArgumentList "-NoExit", "-Command", "Write-Host 'NaiveClient $i'; java NaiveClient 127.0.0.1 $port"
}

Write-Host "All test windows launched!"