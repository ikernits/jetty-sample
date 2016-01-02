
. variables.sh

$JAVA_HOME/bin/java \
 -Xmx2G \
 -XX:+UseG1GC \
 -Dserver.properties=$SERVER_PROPERTIES \
 -Dserver.action=start \
 -cp $SERVER_HOME \
 -jar $SERVER_HOME/$SERVER_APP_NAME > ${SERVER_OUT_LOG} &

PID=$!

echo ${PID} > ${PID_FILE}
echo server application started pid = ${PID}
disown

echo -n "waiting for server to respond."
for i in `seq 1 60`;
do
    if [ "$(is_responding)" == "true" ]
    then
    	echo " success"
    	exit
    fi	 
    echo -n .
    sleep 1
done    

echo " failure"
echo "server failed to start in 60 seconds"

./stop.sh

