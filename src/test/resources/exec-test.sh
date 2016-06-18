

LOCK_FILE=$1
SLEEP_TIME=$2
SLEEP_TERM_TIME=$3
OUT_MODE=$4

if [ "${LOCK_FILE}" == "" ]
then
  echo lock file must be set >& 2
  exit 1
fi

if [ "${SLEEP_TIME}" == "" ]
then
  echo sleep time must be set >& 2
  exit 2
fi

if [ "${SLEEP_TERM_TIME}" == "" ]
then
  echo sleep termination time must be set >& 2
  exit 2
fi

echo "normal output"
echo "error output" >& 2

trap "" SIGPIPE
trap "sleep ${SLEEP_TERM_TIME} && rm -f ${LOCK_FILE} && exit -1" SIGTERM


echo lock file is ${LOCK_FILE}, sleep time ${SLEEP_TIME}
echo "writing lock file ${LOCK_FILE}"
echo lock > ${LOCK_FILE}
echo "sleeping ${SLEEP_TIME} seconds"

if [ "${OUT_MODE}" == "" ]
then
  sleep ${SLEEP_TIME} &
  wait $!
fi

if [ "${OUT_MODE}" == "stdout" ]
then
  for i in `seq 1 100000`;
  do
    echo $i + ": xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx"
  done
fi

if [ "${OUT_MODE}" == "stderr" ]
then
  for i in `seq 1 100000`;
  do
    echo $i + ": xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx" 1>&2
  done
fi

if [ "${OUT_MODE}" == "both" ]
then
  for i in `seq 1 100000`;
  do
    echo $i + ": xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx"
    echo $i + ": xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx" 1>&2
  done
fi

echo "removing lock file ${LOCK_FILE}"
rm -f ${LOCK_FILE}



