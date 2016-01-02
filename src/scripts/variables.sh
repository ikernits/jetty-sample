JAVA_HOME=/Library/Java/JavaVirtualMachines/jdk1.8.0_45.jdk/Contents/Home
SERVER_PROPERTIES=../server.properties
SERVER_HOME=../
SERVER_APP_NAME=jetty.sample.jar
SERVER_OUT_LOG=./local/server.out
SERVER_PORT=8080
PID_FILE=./local/server.pid

function is_running()
{
    if [ ! -f ${PID_FILE} ]
    then
        echo "false"
    else
        kill -0 `cat ${PID_FILE}` 1>/dev/null 2>/dev/null
        kill_reponse=$?
        if [ "$kill_reponse" == "0" ]
        then
            echo "true";
        else
            rm -f ${PID_FILE}
            echo "false"
        fi
    fi
}

function is_responding()
{
    curl http://localhost:${SERVER_PORT}/status 1>/dev/null 2>/dev/null
    curl_response=$?
    if [ "$curl_response" == "0" ]
    then
        echo "true";
    else
        echo "false";
    fi
}