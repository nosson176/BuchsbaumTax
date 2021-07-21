CREATE TABLE year_details (
    id SERIAL PRIMARY KEY,
    deduction_married_filing_jointly INTEGER,
    deduction_head_of_househould INTEGER,
    deduction_single_and_married_filing_separately INTEGER,
    deduction_married_filing_jointly_amount INTEGER,
    ceiling_married_filing_jointly INTEGER,
    deduction_married_filing_separately_amount INTEGER,
    ceiling_married_filing_separately INTEGER,
    deduction_single_and_head_of_household_amount INTEGER,
    ceiling_single_and_head_of_household INTEGER,
    exemption INTEGER,
    credit_8812_annual_deduction INTEGER,
    ceiling_self_employment INTEGER,
    exclusion_2555 INTEGER,
    foreign_annual INTEGER,
    foreign_monthly INTEGER,
    foreign_secondary_annual INTEGER,
    foreign_secondary_monthly INTEGER,
    dollar_annual INTEGER,
    dollar_monthly INTEGER,
    dollar_secondary_annual INTEGER,
    dollar_secondary_monthly INTEGER,
    additional_8812_child_credit INTEGER,
    year_name TEXT,
    string TEXT,
    show BOOLEAN NOT NULL DEFAULT TRUE,
    created TIMESTAMPTZ DEFAULT now(),
    updated TIMESTAMPTZ DEFAULT now()
);

CREATE TABLE tax_brackets (
    id SERIAL PRIMARY KEY,
    year_detail_id INTEGER REFERENCES year_details ON DELETE CASCADE,
    bracket_type TEXT,
    income_amount INTEGER,
    rate INTEGER
);

CREATE TABLE clients (
    id SERIAL PRIMARY KEY,
    archived BOOLEAN NOT NULL DEFAULT FALSE,
    first_name TEXT,
    last_name TEXT,
    status INTEGER
);

CREATE TABLE users (
  id SERIAL PRIMARY KEY,
  first_name TEXT,
  last_name TEXT
);

CREATE TABLE activity_histories (
    id SERIAL PRIMARY KEY,
    created TIMESTAMPTZ DEFAULT now(),
    updated TIMESTAMPTZ DEFAULT now(),
    user_id INTEGER,
    client_id INTEGER
);

