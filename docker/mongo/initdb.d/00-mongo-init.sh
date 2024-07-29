#!/bin/bash
set -e
mongo --eval "rs.initiate()"

function is_primary {
    local result=$(mongo --eval "db.isMaster().ismaster" --quiet)
    echo "$result"
}

while true; do
    is_primary_result=$(is_primary)
    if [ "$is_primary_result" = "true" ]; then
        echo "Now the primary node in the replica set!"
        break
    fi
    echo "Waiting to become the primary node..."
    sleep 2
done
