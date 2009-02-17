#!/bin/sh

rm -f ./tmp/*
touch ./tmp/remove.me

echo "Now creating source archive"
zip -r ./tmp/siovanus-src.zip ./ 
zip -d ./tmp/siovanus-src.zip tools/upload/ship.sh
zip -d ./tmp/siovanus-src.zip "bin/*"
zip -d ./tmp/siovanus-src.zip "tmp/*"
zip -d ./tmp/siovanus-src.zip "*/CVS/*"
echo "Now uploading source archive"
scp ./tmp/siovanus-src.zip hiroshi@dakusui.no-ip.info:/home/hiroshi/public_html/

echo "Finished"
