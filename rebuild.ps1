param (
    [Parameter(Mandatory=$true)]
    [string]$ServiceName
)

cd $ServiceName
.\gradlew bootJar

cd ..
docker-compose up -d --build $ServiceName
