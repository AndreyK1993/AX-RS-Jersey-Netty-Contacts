CREATE DATABASE contacts_new_db;

USE contacts_new_db;

CREATE TABLE IF NOT EXISTS contacts
(
  id INTEGER NOT NULL AUTO_INCREMENT,
  first_name VARCHAR(128) NOT NULL,
  phone VARCHAR(20) NOT NULL,
  PRIMARY KEY (id)
);