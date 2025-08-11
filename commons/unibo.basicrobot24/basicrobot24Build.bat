echo ---------------------------------------------
echo basicrobot24Build ---------------------------
echo ---------------------------------------------
cd C:\Didattica2024\qak24\unibo.basicrobot24
docker rmi natbodocker/basicrobot24:3.0
docker rmi basicrobot24:3.0 
call gradlew distTar  
if %errorlevel% neq 0 (
    echo Errore durante l'esecuzione di Gradle
    exit /b %errorlevel%
)
docker build -t basicrobot24:3.0 .
docker tag basicrobot24:3.0 natbodocker/basicrobot24:3.0
docker push natbodocker/basicrobot24:3.0
