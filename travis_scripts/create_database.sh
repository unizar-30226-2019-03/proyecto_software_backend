#!/usr/bin/env bash

#**********************************************
#******* Trabajo de Proyecto Software *********
#******* Unicast ******************************
#******* Fecha 22-5-2019 **********************
#******* Autores: *****************************
#******* Adrian Samatan Alastuey 738455 *******
#******* Jose Maria Vallejo Puyal 720004 ******
#******* Ruben Rodriguez Esteban 737215 *******
#**********************************************

# Script para llevar a cabo la creacion automatica de la base de datos de PostgreSQL

#Creacion de la base de datos
psql -c "CREATE DATABASE unicast;" -U postgres

# Creacion de un usuario "unicast_backend" con password "unicast_backend" 
# en PostgreSQL disinto de root para usar la base de datos
psql -c "CREATE USER unicast_backend WITH PASSWORD 'unicast_backend';" -U postgres

# Creacion de la base de datos en base al esquema contenido en el fichero schema.sql
psql -U postgres -d unicast -a -f src/main/resources/schema.sql

# Dar permisos al usuario "unicast_backend para poder emplear la base y gestionar los datos almacenados en ella"
psql -d unicast -c "GRANT ALL ON SCHEMA public TO unicast_backend;" -U postgres
psql -d unicast -c "GRANT ALL ON ALL TABLES IN SCHEMA public TO unicast_backend;" -U postgres
psql -d unicast -c "GRANT ALL ON ALL SEQUENCES IN SCHEMA public TO unicast_backend;" -U postgres
