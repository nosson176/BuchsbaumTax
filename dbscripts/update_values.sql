-- filings table
ALTER TABLE filings
ADD COLUMN tax_form TEXT,
ADD COLUMN status TEXT,
ADD COLUMN status_detail TEXT,
ADD COLUMN file_type TEXT;

UPDATE filings f
SET tax_form = value
FROM value_lists v
WHERE v.id = f.tax_form_id;

UPDATE filings f
SET status = value
FROM value_lists v
WHERE v.id = f.status_id;

UPDATE filings f
SET status_detail = value
FROM value_lists v
WHERE v.id = f.status_detail_id;

UPDATE filings f
SET file_type = value
FROM value_lists v
WHERE v.id = f.file_type_id;

UPDATE filings f
SET currency = value
FROM value_lists v
WHERE v.id = CAST(f.currency AS INTEGER);

ALTER TABLE filings
DROP COLUMN tax_form_id,
DROP COLUMN status_id,
DROP COLUMN status_detail_id,
DROP COLUMN file_type_id;

-- income_breakdowns table
ALTER TABLE income_breakdowns
ADD COLUMN category TEXT,
ADD COLUMN tax_group TEXT,
ADD COLUMN tax_type TEXT,
ADD COLUMN job TEXT,
ADD COLUMN currency TEXT;

UPDATE income_breakdowns ib
SET category = value
FROM value_lists v
WHERE v.id = ib.category_id;

UPDATE income_breakdowns ib
SET tax_group = value
FROM value_lists v
WHERE v.id = ib.tax_group_id;

UPDATE income_breakdowns ib
SET tax_type = value
FROM value_lists v
WHERE v.id = ib.tax_type_id;

UPDATE income_breakdowns ib
SET job = value
FROM value_lists v
WHERE v.id = ib.job_id;

UPDATE income_breakdowns ib
SET currency = value
FROM value_lists v
WHERE v.id = ib.currency_id;

ALTER TABLE income_breakdowns
DROP COLUMN category_id,
DROP COLUMN tax_group_id,
DROP COLUMN tax_type_id,
DROP COLUMN job_id,
DROP COLUMN currency_id;

-- fbar_breakdowns table
ALTER TABLE fbar_breakdowns
ADD COLUMN category TEXT,
ADD COLUMN tax_group TEXT,
ADD COLUMN tax_type TEXT,
ADD COLUMN part TEXT,
ADD COLUMN currency TEXT;

UPDATE fbar_breakdowns fb
SET category = value
FROM value_lists v
WHERE v.id = fb.category_id;

UPDATE fbar_breakdowns fb
SET tax_group = value
FROM value_lists v
WHERE v.id = fb.tax_group_id;

UPDATE fbar_breakdowns fb
SET tax_type = value
FROM value_lists v
WHERE v.id = fb.tax_type_id;

UPDATE fbar_breakdowns fb
SET part = value
FROM value_lists v
WHERE v.id = fb.part_id;

UPDATE fbar_breakdowns fb
SET currency = value
FROM value_lists v
WHERE v.id = fb.currency_id;

ALTER TABLE fbar_breakdowns
DROP COLUMN category_id,
DROP COLUMN tax_group_id,
DROP COLUMN tax_type_id,
DROP COLUMN part_id,
DROP COLUMN currency_id;

-- contacts table
ALTER TABLE contacts
ADD COLUMN contact_type TEXT;

UPDATE contacts c
SET contact_type = value
FROM value_lists v
WHERE v.id = c.contact_type_id;

ALTER TABLE contacts
DROP COLUMN contact_type_id;

-- tax_personals table
ALTER TABLE tax_personals
ADD COLUMN category TEXT,
ADD COLUMN relation TEXT,
ADD COLUMN language TEXT;

UPDATE tax_personals t
SET category = value
FROM value_lists v
WHERE v.id = t.category_id;

UPDATE tax_personals t
SET relation = value
FROM value_lists v
WHERE v.id = t.relation_id;

UPDATE tax_personals t
SET language = value
FROM value_lists v
WHERE v.id = t.language_id;

ALTER TABLE tax_personals
DROP COLUMN category_id,
DROP COLUMN relation_id,
DROP COLUMN language_id;