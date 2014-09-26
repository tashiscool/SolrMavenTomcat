#!/bin/sh

sudo ./remove_cron_jobs.sh
/export/home/apps/rumba/apache-tomcat-6.0.20/bin/shutdown.sh
rm -Rf /export/home/apps/rumba/apache-tomcat-6.0.20/logs/*
