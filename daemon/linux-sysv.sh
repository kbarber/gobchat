#!/bin/bash

# Gob Online Chat System V start/stop script
#
# description: Gob Online Chat Server
#
# chkconfig: - 99 01

JAVA_HOME=/usr/java/j2sdk1.4.1_01/
RUNSERVER=/home/ken/dev/gob-working/gobchat/daemon/gobd
PIDFILE=/tmp/gob.pid
RUSER=ken
    
if [ ! -f $RUNSERVER ] ; then
    echo "Cannot find script: $RUNSERVER"
    exit 1
fi

start() {
    echo -n "Starting gob: "
    if [ ! -f $PIDFILE ] ; then
        su -c $RUNSERVER $RUSER
        touch $PIDFILE
        ps axw | grep server.jar | grep -v grep | awk '{print $1}' > $PIDFILE
        echo "OK"
    else
        echo "A pid file $PIDFILE already exists, is the program already running?"
    fi
}

stop() {
    echo -n "Stopping gob: "
    if [ -f $PIDFILE ] ; then
        local line
        read line < $PIDFILE
        kill $line
        rm $PIDFILE
        echo "OK"
    else
        echo "A pid file $PIDFILE does not exist. Perhaps the server is not running?"
    fi
}

case "$1" in
    start)
        start
        ;;
    stop)
        stop
        ;;
    restart)
        stop
        sleep 2
        start
        ;;
    *)
        echo "Usage: $0 {start|stop|restart}"
        exit 1
esac

exit 0
