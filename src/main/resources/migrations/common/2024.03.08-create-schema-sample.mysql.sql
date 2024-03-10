-- liquibase formatted sql

-- changeset Tooba-Aziz:1674212523
CREATE TABLE sample (id VARCHAR(200) NOT NULL, created_by VARCHAR(200) DEFAULT '' NOT NULL, updated_by VARCHAR(200) DEFAULT '' NOT NULL, created_at timestamp DEFAULT NOW() NOT NULL, updated_at timestamp DEFAULT NOW() NOT NULL);