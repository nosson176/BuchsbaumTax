-- ALTER TABLE year_details
-- DROP CONSTRAINT idx_3056590_primary;
--
-- ALTER TABLE year_details
-- ADD CONSTRAINT year_details_pk
-- PRIMARY KEY (year_name);

-- SELECT *
-- FROM information_schema.tables
-- WHERE table_schema = 'public';

CREATE INDEX idx1 ON contacts USING gin(to_tsvector('simple', contact_type||' '||contacts.main_detail||' '||secondary_detail||' '||memo||' '||state||' '||contacts.zip));

WITH words(w) AS (VALUES ('word1'), ('word2')),
     matching_contacts(id) AS (SELECT c.* FROM contacts c, words WHERE to_tsquery('simple', w) ~ to_tsvector('simple', contact_type||' '||contacts.main_detail||' '||secondary_detail||' '||memo||' '||state||' '||contacts.zip))



-- CREATE MATERIALIZED VIEW client_data
-- AS
-- SELECT c.id, to_tsvector(concat_ws(' ',c2.contact_type, c2.main_detail, c2.secondary_detail, c2.memo, c2.state, c2.zip,
--     fb.category, fb.tax_group, fb.tax_type, fb.part, fb.currency, fb.description, fb.depend,
--     ib.category, ib.tax_group, ib.tax_type, ib.job, ib.currency, ib.description, ib.depend,
--     f.tax_form, f.status, f.status_detail, f.memo, f.file_type, f.delivery_contact, f.second_delivery_contact, f.currency, f.filing_type, f.state,
--     l.alarm_time, l.note,
--     tp.category, tp.language, tp.relation, tp.first_name, tp.middle_initial, tp.last_name, tp.ssn, tp.informal,
--     f2.year, f2.status, f2.status_detail, f2.fee_type, f2.fee_type)) AS tsv
-- FROM clients c
-- JOIN contacts c2 on c.id = c2.client_id
-- JOIN fbar_breakdowns fb on c.id = fb.client_id
-- JOIN income_breakdowns ib on c.id = ib.client_id
-- JOIN tax_years ty on c.id = ty.client_id
-- JOIN filings f on ty.id = f.tax_year_id
-- JOIN logs l on c.id = l.client_id
-- JOIN tax_personals tp on c.id = tp.client_id
-- JOIN fees f2 on c.id = f2.client_id;
--
-- SELECT * FROM client_data
-- WHERE tsv ~ plainto_tsquery('buc');

-- SELECT c.*
-- FROM clients c
-- JOIN contacts c2 on c.id = c2.client_id
-- JOIN fbar_breakdowns fb on c.id = fb.client_id
-- JOIN income_breakdowns ib on c.id = ib.client_id
-- JOIN tax_years ty on c.id = ty.client_id
-- JOIN filings f on ty.id = f.tax_year_id
-- JOIN logs l on c.id = l.client_id
-- JOIN tax_personals tp on c.id = tp.client_id
-- JOIN fees f2 on c.id = f2.client_id
--
--
-- with found_rows as (
--   select format('%I.%I', table_schema, table_name) as table_name,
--          query_to_xml(format('select to_jsonb(t) as table_row
--                               from %I.%I as t
--                               where t::text like ''%%b%%'' ', table_schema, table_name),
--                       true, false, '') as table_rows
--   from information_schema.tables
--   where table_schema = 'public'
-- )
-- select table_name, x.table_row
-- from found_rows f
--   left join xmltable('//table/row'
--                      passing table_rows
--                        columns
--                          table_row text path 'table_row') as x on true

