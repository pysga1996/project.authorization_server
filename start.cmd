setlocal
FOR /F "tokens=*" %%i in (./environment/.env) do SET %%i
java -jar ./build/libs/lambda-auth-service-0.0.1-SNAPSHOT.jar
endlocal