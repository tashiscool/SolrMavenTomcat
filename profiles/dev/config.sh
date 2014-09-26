TOMCAT_USERNAME="tomcat"
TOMCAT_PASSWORD="tomcat"

ROOT="/opt/app"
TOMCAT_ROOT="${ROOT}/tomcat"
SOLR_DATA_SYMLINK="${ROOT}"
SOLR_DATA_DIR="${ROOT}"

PORT_SHUTDOWN="8005"
PORT_CONNECTOR_HTTP="8081"
PORT_CONNECTOR_AJP="8009"
PORT_JMX="9999"

## the following are expanded via Maven filtering (ick)
TOMCAT_MAX_THREADS="400"

INITIAL_HEAP_SIZE="1024M"  # -Xms
MAXIMUM_HEAP_SIZE="6144M" # -Xmx
MAXIMUM_PERM_SIZE="1024M" # -XX:MaxPermSize=

## NEVER OVERWRITE CATALINA_OPTS.  Feel free to append to it, however! :-)
CATALINA_OPTS="${CATALINA_OPTS} -Dsolr.data.dir=${SOLR_DATA_SYMLINK} -XX:-DisableExplicitGC"

TOMCAT_LOG_DIR="/fs/logs/$( hostname )"
