# J-NVM demo

Simple banking application.

## Code

### 3 classes:
* [Account.java](src/main/java/eu/telecomsudparis/jnvm/demo/Account.java): Simple entity, hold data, implement transfer between 2 accounts.
* [Bank.java](src/main/java/eu/telecomsudparis/jnvm/demo/Bank.java): Create accounts, keep them indexed in a Map, execute transfers between them.
* [Server.java](src/main/java/eu/telecomsudparis/jnvm/demo/Server.java): REST endpoints, request & reply handling.

### 2 tags:
* [volatile-variant](https://github.com/jnvm-project/jnvm-demo/tree/volatile-variant): no state persisted, volatile entities indexed in a volatile Map.
* [jnvm-variant](https://github.com/jnvm-project/jnvm-demo/tree/jnvm-variant): accounts persisted off-heap and indexed in a recoverable Map.

# Demo flow

[![asciicast](https://asciinema.org/a/499292.svg)](https://asciinema.org/a/499292)

## Volatile scenario

### 1. Executing transactions in a fault-free environment

    git checkout volatile-variant
    mvn clean install
    ./bin/server.sh start
    ./bin/client.sh load 1000
    ./bin/client.sh run 1000

Everything looks good, all transactions succeed

### 2. Checking for inconsistencies

    ./bin/client.sh total

Returns 0, no inconsistencies.

### 3. Crashing the server

    ./bin/server.sh crash

Client returns unable to connect replies

### 4. Restarting the server

    ./bin/server.sh start

Client returns « Account not found » errors.  
Indeed, the database was volatile, all data were lost

### 5. Repopulating the bank

    ./bin/client.sh load
    ./bin/client.sh run

Everything looks good, all transactions succeed.  
But mind that previous data were lost.

## Persistent scenario

### 1. Executing transactions in a fault-free environment

    git checkout jnvm-variant
    mvn clean install
    ./bin/server.sh start
    ./bin/client.sh load
    ./bin/client.sh run

Everything looks good, all transactions succeed

### 2. Checking for inconsistencies

     ./bin/client.sh total

Returns 0, no inconsistencies.

### 3. Crashing the server

    ./bin/server.sh crash

Client returns unable to connect replies

### 4. Restarting the server

    ./bin/server.sh start

Everything looks good, all transactions succeed.  
Indeed, persistent state was recovered;  
Client resumes request processing with no extra steps.

### 5. Checking for inconsistencies

     ./bin/client.sh total

Returns 0, no inconsistencies.
