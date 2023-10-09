# `init.sql`

This file is ran when the postgres container is created. It creates the database and the user that will be used by the application.

## Usage

When adding a new table to the database, add the table creation code to this file.

Example:

```sql
CREATE TABLE IF NOT EXISTS `TABLE` (
    `COLUMN` BIGINT DEFAULT nextval('{TABLE}_{COLUMN}_seq') PRIMARY KEY,
    `COLUMN` VARCHAR(255) NOT NULL,
    ...
);
```

For auto-incrementing columns, use the `nextval` function to generate the next value in the sequence. The sequence name should be in the format `{TABLE}_{COLUMN}_seq`.
