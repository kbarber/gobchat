#!/bin/bash

# Gob Online Chat System V start/stop script
#
# description: Gob Online Chat Server
#
# chkconfig: - 99 01

case "$1" in
    start)
        ;;
    stop)
        ;;
    restart)
        ;;
    *)
        echo "Usage: $0 {start|stop|restart}"
        exit 1
esac

exit 0
