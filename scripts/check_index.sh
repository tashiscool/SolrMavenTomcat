#!/bin/sh

echo "++++++++++++++++++"
echo "Core Checker: uses the lucene index checker to verify the integrity of a solr index."
echo "usage: ./check_index"
echo "++++++++++++++++++"
echo -e "Please enter the index name to check: \c "
read index
echo "++++++++++++++++++"
echo "Checking... $index"
echo "++++++++++++++++++"

# set this to wherever your config is
BASE_DIR="/export/home/apps/solr_3.6_wip"
# set this to wherever your lucene core jar is - it will be in the exploded tomcat webapps area
LUCENE_CORE_JAR="/export/home/apps/apache-tomcat-6.0.35/webapps/search/WEB-INF/lib/lucene-core-3.6.0.jar"

if [ -d "$BASE_DIR"/search/data/$index/index/ ]; then
  java -classpath "$LUCENE_CORE_JAR" org.apache.lucene.index.CheckIndex "$BASE_DIR"/search/data/$index/index/
else
  echo "++++++++++++++++++"
  echo "Index does not exist.  Skipping."
  echo "++++++++++++++++++"
fi

echo "+++++search 2+++++"
if [ -d "$BASE_DIR"/search2/data/$index/index/ ]; then
  java -classpath "$LUCENE_CORE_JAR" org.apache.lucene.index.CheckIndex "$BASE_DIR"/search2/data/$index/index/
else
  echo "++++++++++++++++++"
  echo "Index does not exist.  Skipping."
  echo "++++++++++++++++++"
fi

echo "+++++ Done.+++++++"

