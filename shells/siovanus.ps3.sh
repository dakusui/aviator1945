#!/bin/sh

export LD_LIBRARY_PATH=`pwd`
export CLASSPATH=./siovanus.jar:./Joystick.jar
export VMARGS="-Dsun.java2d.opengl=true -client -Xms212m -Xmx212m"
export LOG=siovanus.ps3.log
export APPARGS="-video=FULL -session=siovanus.SSession -bgquality=LOW -frame=DROP -joystick=DISABLED"
java -cp $CLASSPATH  $VMARGS avis.session.ASession $APPARGS > $LOG
