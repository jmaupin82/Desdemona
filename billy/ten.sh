#!/bin/sh
if [ "$1" == "" ] ; then
	echo Usage : ten 311445 to see game 311445 ;
	exit 1 ;
fi

wget -O /tmp/game_$1.sgf http://www.littlegolem.net/jsp/game/png.jsp?gid=$1

# adapt the location of the JVM if needed
/usr/local/java/bin/java -classpath billy.jar Billy /tmp/game_$1.sgf
