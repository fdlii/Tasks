set PGHOST=localhost
set PGPORT=5432
set PGUSER=postgres
set PGPASSWORD=root
set DATABASE_NAME=book_store
set SCRIPT_DIR=D:\University\SENLA_Courses\Tasks\src\main\java\com\task_11_1\

psql -h %PGHOST% -p %PGPORT% -U %PGUSER% -c "DROP DATABASE IF EXISTS %DATABASE_NAME%;"
psql -h %PGHOST% -p %PGPORT% -U %PGUSER% -c "CREATE DATABASE %DATABASE_NAME%;"

psql -h %PGHOST% -p %PGPORT% -U %PGUSER% -d %DATABASE_NAME% -f %SCRIPT_DIR%create_tables.sql

pause