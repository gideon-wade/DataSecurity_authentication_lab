start cmd /k "cd src && rmiregistry"
start cmd /k "cd src && java PrintServer"
cd src
java Client
cd ..