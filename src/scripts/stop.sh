
. variables.sh

$JAVA_HOME/bin/java \
 -Xmx2G \
 -XX:+UseG1GC \
 -Dserver.properties=$SERVER_PROPERTIES \
 -Dserver.action=shutdown \
 -cp $SERVER_HOME \
 -jar $SERVER_HOME/$SERVER_APP_NAME > /dev/null &


echo -n "waiting for server to stop"
for i in `seq 1 15`;
do
    if [ "$(is_responding)" == "false" ]
    then
    	break
    fi
    echo -n .
    sleep 1
done

if [ "$(is_responding)" == "false" ]
then
    echo " - success"
else
    echo " - failure"
fi

echo -n "waiting for server process to stop"
for i in `seq 1 15`;
do
    if [ "$(is_running)" == "false" ]
    then
    	break
    fi
    echo -n .
    sleep 1
done

if [ "$(is_running)" == "false" ]
then
    echo " - success"
else
    echo " - failure"
    PID=`cat ${PID_FILE}`
    echo "killing server process ${PID}"
    kill ${PID}
fi
