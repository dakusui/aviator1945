#!/bin/sh

echo "Now creating binary archive"
rm -fr ./tmp/*
touch ./tmp/remove.me
jar cvf ./tmp/siovanus.jar -C ./bin ./

zip -j ./tmp/siovanus-ex.zip ./shells/*.bat
zip -j ./tmp/siovanus-ex.zip ./shells/*.sh
zip -j ./tmp/siovanus-ex.zip ./lib/*
zip -j ./tmp/siovanus-ex.zip ./tmp/siovanus.jar
echo "Now uploading binary archive"
scp ./tmp/siovanus-ex.zip hiroshi@dakusui.no-ip.info:/home/hiroshi/public_html/

echo "Finished"
