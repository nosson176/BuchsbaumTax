ALTER TABLE year_details
DROP CONSTRAINT idx_3056590_primary;

ALTER TABLE year_details
ADD CONSTRAINT year_details_pk
PRIMARY KEY (year_name);