#!/bin/sh
## sourced from catalina.sh

## prevents brain-dead apps from leaving turds in the pwd; same root as $CATALINA_OUT
cd "${CATALINA_BASE}/logs"

## no longer support CATALINA_OPTS specified outside of ${CATALINA_HOME}/conf/config.sh
if [ -n "$CATALINA_OPTS" ]; then
    echo ">> [WARNING] [WARNING] [WARNING]"
    echo ">> CATALINA_OPTS can only be set via"
    echo ">>     ${CATALINA_HOME}/conf/config.sh"
    echo ">> "
    echo ">> IGNORING: ${CATALINA_OPTS}"
    echo ">> "
    echo ">> [WARNING] [WARNING] [WARNING]"
fi

## configure CATALINA_PID and give it a home
_pid_dir="${CATALINA_HOME}/var/run"
mkdir -p "${_pid_dir}"

CATALINA_PID="${_pid_dir}/catalina.pid"

## override CATALINA_OPTS that may have been provided; prepare for additional options
CATALINA_OPTS="-server"

. "${CATALINA_HOME}/conf/config.sh"

## stuffs the rumba_config directory into the classpath
CLASSPATH="${CATALINA_HOME}/conf/:${CLASSPATH}"


# memory tuning
CATALINA_OPTS="${CATALINA_OPTS} -Xms${INITIAL_HEAP_SIZE}"
CATALINA_OPTS="${CATALINA_OPTS} -Xmx${MAXIMUM_HEAP_SIZE}"
CATALINA_OPTS="${CATALINA_OPTS} -XX:MaxPermSize=${MAXIMUM_PERM_SIZE}"

echo "memory tuned $CATALINA_OPTS"
# map environment variables to system properties
CATALINA_OPTS="${CATALINA_OPTS} -Dport.shutdown=${PORT_SHUTDOWN}"
CATALINA_OPTS="${CATALINA_OPTS} -Dport.connector.http=${PORT_CONNECTOR_HTTP}"
CATALINA_OPTS="${CATALINA_OPTS} -Dport.connector.ajp=${PORT_CONNECTOR_AJP}"
CATALINA_OPTS="${CATALINA_OPTS} -Dtomcat.username=${TOMCAT_USERNAME}"
CATALINA_OPTS="${CATALINA_OPTS} -Dtomcat.password=${TOMCAT_PASSWORD}"
CATALINA_OPTS="${CATALINA_OPTS} -Dtomcat.maxThreads=${TOMCAT_MAX_THREADS}"

## enable JMX if PORT_JMX is provided
if [ -n "${PORT_JMX}" ]; then
    CATALINA_OPTS="${CATALINA_OPTS} -Dcom.sun.management.jmxremote"
    CATALINA_OPTS="${CATALINA_OPTS} -Dcom.sun.management.jmxremote.port=${PORT_JMX}"
    CATALINA_OPTS="${CATALINA_OPTS} -Dcom.sun.management.jmxremote.ssl=false"
    CATALINA_OPTS="${CATALINA_OPTS} -Dcom.sun.management.jmxremote.authenticate=false"
fi