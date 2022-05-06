#!/bin/bash

PROXY="localhost:8080"


_curlwrapper() {
    method=$1
    url=$2

    curl -f -s -X $method $url

    case $? in
        0)
          ;;
        22)
          echo -n "Server error"
          ;;
        *)
          echo -n "Failed"
          ;;
    esac
}

_balance() {
    _curlwrapper get $PROXY/balance/$1
    echo ""
}

_create() {
    [ $# -eq 2 ] \
      && _curlwrapper post $PROXY/create/$1/$2 \
      || _curlwrapper post $PROXY/create/$1
    echo ""
}

_credit() {
    _curlwrapper put $PROXY/credit/$1/$2
}

_transfer() {
    _curlwrapper put $PROXY/transfer/$1/$2/$3
}

_clearall() {
    _curlwrapper post $PROXY/clear/all
    echo ""
}

_total() {
    _curlwrapper post $PROXY/total
    echo ""
}

case $1 in
  balance)
    _balance $2
    ;;
  create)
    _create $2 $3
    ;;
  credit)
    _credit $2 $3
    ;;
  transfer)
    _transfer $2 $3 $4
    ;;
  clearall)
    _clearall
    ;;
  total)
    _total
    ;;
  *)
    echo "Not a function"
    ;;
esac
