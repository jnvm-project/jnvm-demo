#!/bin/bash

SCRIPT_DIR=$(dirname "${BASH_SOURCE[0]}")

PIDFILE=$SCRIPT_DIR/pid

_start() {
  echo "Starting server..."
  cd $SCRIPT_DIR/..
  mvn exec:java &
  echo $! > $PIDFILE
}

_stop() {
  echo "Stopping server..."
  pid=`cat $PIDFILE`
  kill -TERM $pid
}

_crash() {
  echo "Sending SIGKILL to server..."
  pid=`cat $PIDFILE`
  kill -KILL $pid
}

case $1 in
  start)
    _start
    ;;
  stop)
    _stop
    ;;
  crash)
    _crash
    ;;
esac
