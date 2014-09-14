cd agent
call mvn clean package install assembly:single
cd ..
cd agent-test
call mvn clean package install assembly:single
cd ..