#!/usr/bin/env bash

psql -c "CREATE DATABASE unicast;" -U postgres
psql -c "CREATE USER unicast_backend WITH PASSWORD 'unicast_backend';" -U postgres
psql -c "GRANT ALL ON SCHEMA public TO unicast_backend;" -U postgres
psql -U postgres -d unicast -a -f src/main/resources/schema.sql
psql -c "GRANT ALL ON ALL TABLES IN SCHEMA public TO unicast_backend;" -U postgres
psql -c "GRANT ALL ON ALL SEQUENCES IN SCHEMA public TO unicast_backend;" -U postgres
