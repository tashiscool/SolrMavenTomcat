#!/bin/sh

# This script will DELETE ALL DATA from the specified Solr instance.
# Is this what you *REALLY* want to do?

if [ ! -n "$1" ]; then
  echo "Usage: `basename $0` SOLR_URL"
  echo "    SOLR_URL: Should include full URL to the Solr application, including trailing slash. Example: http://localhost:8080/search/"
  echo "              * SOLR_URL only accepts the following value: http://localhost:8080/search/"
  exit 1
fi

if [ "$1" != "http://localhost:8080/search/" ]; then
  echo "Usage: `basename $0` SOLR_URL"
  echo "    SOLR_URL: Should include full URL to the Solr application, including trailing slash. Example: http://localhost:8080/search/"
  echo "              * SOLR_URL only accepts the following value: http://localhost:8080/search/"
  exit 1
fi


SOLR_URL=$1

curl ${SOLR_URL}product/update?commit=true -H "Content-Type: text/xml" --data-binary '<delete><query>*:*</query></delete>'


