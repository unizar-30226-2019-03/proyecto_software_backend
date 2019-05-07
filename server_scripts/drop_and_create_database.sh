#!/usr/bin/env bash

if [ $# -ne 1 ]
then
      echo "Uso: create_database.sh host_remoto"
      exit
fi

psql -U unicast -h $1 -d postgres -c "DROP DATABASE unicast;"
psql -U unicast -h $1 -d postgres -c "CREATE DATABASE unicast;"
psql -U unicast -h $1 -d unicast < src/main/resources/schema.sql
psql -U unicast -h unicast-backend.cbao5ugsaufr.eu-west-3.rds.amazonaws.com -d unicast -c "GRANT ALL ON SCHEMA public TO unicast;"
psql -U unicast -h unicast-backend.cbao5ugsaufr.eu-west-3.rds.amazonaws.com -d unicast -c "GRANT ALL ON ALL TABLES IN SCHEMA public TO unicast;"
psql -U unicast -h unicast-backend.cbao5ugsaufr.eu-west-3.rds.amazonaws.com -d unicast -c "GRANT ALL ON ALL SEQUENCES IN SCHEMA public TO unicast;"
