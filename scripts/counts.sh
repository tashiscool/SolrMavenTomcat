#!/bin/sh

echo -n "Organization: "
curl -s "http://localhost:8080/search/organization/admin/luke?indent=true&fl=none" | grep numDocs | sed -r 's/[^0-9]//g'
curl -s "http://localhost:8080/search/organization/dataimport?command=status&indent=true" | grep '<str name="status">' | perl -pe 's/\s*<str.*>(.*)<\/str>/$1/g;'
curl -s "http://localhost:8080/search/organization/dataimport?command=status&indent=true" | grep '<str name="">' | perl -pe 's/\s*<str.*>(.*)<\/str>/$1/g;'
echo

echo -n "daxproducts:         "
curl -s "http://localhost:8080/search/daxproducts/admin/luke?indent=true&fl=none" | grep numDocs | sed -r 's/[^0-9]//g'
curl -s "http://localhost:8080/search/daxproducts/dataimport?command=status&indent=true" | grep '<str name="status">' | perl -pe 's/\s*<str.*>(.*)<\/str>/$1/g;'
curl -s "http://localhost:8080/search/daxproducts/dataimport?command=status&indent=true" | grep '<str name="">' | perl -pe 's/\s*<str.*>(.*)<\/str>/$1/g;'
echo

echo -n "Product:      "
curl -s "http://localhost:8080/search/product/admin/luke?indent=true&fl=none" | grep numDocs | sed -r 's/[^0-9]//g'
curl -s "http://localhost:8080/search/product/dataimport?command=status&indent=true" | grep '<str name="status">' | perl -pe 's/\s*<str.*>(.*)<\/str>/$1/g;'
curl -s "http://localhost:8080/search/product/dataimport?command=status&indent=true" | grep '<str name="">' | perl -pe 's/\s*<str.*>(.*)<\/str>/$1/g;'
echo

echo -n "Resource:     "
curl -s "http://localhost:8080/search/resource/admin/luke?indent=true&fl=none" | grep numDocs | sed -r 's/[^0-9]//g'
curl -s "http://localhost:8080/search/resource/dataimport?command=status&indent=true" | grep '<str name="status">' | perl -pe 's/\s*<str.*>(.*)<\/str>/$1/g;'
curl -s "http://localhost:8080/search/resource/dataimport?command=status&indent=true" | grep '<str name="">' | perl -pe 's/\s*<str.*>(.*)<\/str>/$1/g;'
echo

echo -n "License:      "
curl -s "http://localhost:8080/search/license/admin/luke?indent=true&fl=none" | grep numDocs | sed -r 's/[^0-9]//g'
curl -s "http://localhost:8080/search/license/dataimport?command=status&indent=true" | grep '<str name="status">' | perl -pe 's/\s*<str.*>(.*)<\/str>/$1/g;'
curl -s "http://localhost:8080/search/license/dataimport?command=status&indent=true" | grep '<str name="">' | perl -pe 's/\s*<str.*>(.*)<\/str>/$1/g;'
echo
