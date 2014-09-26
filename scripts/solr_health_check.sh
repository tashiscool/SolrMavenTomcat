# Report on the last modified data for Solr Cores
echo ''
echo 'Solr Search - Health Check'
echo '-----------------------------------------------'
more /export/home/apps/rumba/search2/search/product/conf/dataimport.properties | grep .last_index_time
