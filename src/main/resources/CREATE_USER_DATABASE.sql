CREATE USER discodeit_user
    PASSWORD 'discodeit1234'
    CREATEDB;

SELECT rolname, rolsuper, rolcreatedb
FROM pg_roles
WHERE rolname = 'discodeit_user';

-- 2) 데이터베이스 생성 후 스키마 생성
-- 데이터베이스를 생성한다
CREATE DATABASE discodeitdb
    WITH
    OWNER = discodeit_user
    ENCODING = 'UTF8'
    LC_COLLATE = 'en_US.UTF8'
    LC_CTYPE = 'en_US.UTF8'
    TEMPLATE template0;

\c discodeitdb

CREATE SCHEMA IF NOT EXISTS discodeitdb;

-- 스키마 사용 권한을 부여한다
GRANT ALL PRIVILEGES ON SCHEMA discodeitdb TO discodeit_user;
GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA discodeitdb TO discodeit_user;

-- 권한을 확인한다
SELECT grantor,
       grantee,
       table_schema,
       privilege_type
FROM information_schema.role_table_grants
WHERE grantee = 'discodeit_user';

-- 3) 기본 검색 경로에 새로운 스키마를 추가한다
ALTER ROLE ohgiraffers SET search_path TO menudb, public;

-- 현재 세션의 검색 경로를 확인한다
SHOW search_path;
