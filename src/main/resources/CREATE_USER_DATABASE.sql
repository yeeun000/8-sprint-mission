CREATE USER discodeit_user
    PASSWORD 'discodeit1234'
    CREATEDB;

SELECT rolname, rolsuper, rolcreatedb
FROM pg_roles
WHERE rolname = 'discodeit_user';

CREATE DATABASE discodeit
    WITH
    OWNER = discodeit_user
    ENCODING = 'UTF8'

\c discodeit

CREATE SCHEMA IF NOT EXISTS discodeit;
GRANT ALL PRIVILEGES ON SCHEMA discodeit TO discodeit_user;

ALTER DEFAULT PRIVILEGES IN SCHEMA discodeit GRANT ALL ON TABLES TO discodeit_user;



SELECT grantor,
       grantee,
       table_schema,
       privilege_type
FROM information_schema.role_table_grants
WHERE grantee = 'discodeit_user';

ALTER ROLE discodeit_user SET search_path TO discodeit, public;
SHOW search_path;
