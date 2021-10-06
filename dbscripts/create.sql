CREATE TABLE clients (
    id SERIAL PRIMARY KEY,
    status TEXT,
    owes_status TEXT,
    periodical TEXT,
    last_name TEXT,
    archived BOOLEAN NOT NULL DEFAULT FALSE,
    display_name TEXT,
    display_phone TEXT,
    created TIMESTAMPTZ DEFAULT NOW(),
    updated TIMESTAMPTZ DEFAULT NOW()
);

CREATE TABLE contacts (
    id SERIAL PRIMARY KEY,
    client_id INTEGER REFERENCES clients ON DELETE CASCADE,
    contact_type TEXT,
    memo TEXT,
    main_detail TEXT,
    secondary_detail TEXT,
    state TEXT,
    zip TEXT,
    enabled BOOLEAN NOT NULL DEFAULT TRUE,
    archived BOOLEAN NOT NULL DEFAULT FALSE
);

CREATE TABLE exchange_rates (
    id SERIAL PRIMARY KEY,
    currency TEXT,
    year TEXT,
    show BOOLEAN NOT NULL DEFAULT TRUE,
    rate FLOAT
);

CREATE TABLE fbar_breakdowns (
    id SERIAL PRIMARY KEY,
    client_id INTEGER REFERENCES clients ON DELETE CASCADE,
    years TEXT,
    category TEXT,
    tax_group TEXT,
    tax_type TEXT,
    part TEXT,
    currency TEXT,
    frequency INTEGER,
    documents TEXT,
    description TEXT,
    amount FLOAT,
    depend TEXT,
    include BOOLEAN NOT NULL DEFAULT TRUE,
    archived BOOLEAN NOT NULL DEFAULT FALSE
);

CREATE TABLE income_breakdowns (
    id SERIAL PRIMARY KEY,
    client_id INTEGER REFERENCES clients ON DELETE CASCADE,
    years TEXT,
    category TEXT,
    tax_group TEXT,
    tax_type TEXT,
    job TEXT,
    currency TEXT,
    frequency INTEGER,
    documents TEXT,
    description TEXT,
    amount FLOAT,
    exclusion BOOLEAN NOT NULL DEFAULT FALSE,
    include BOOLEAN NOT NULL DEFAULT TRUE,
    archived BOOLEAN NOT NULL DEFAULT FALSE,
    depend TEXT
);

CREATE TABLE year_details (
    year TEXT PRIMARY KEY,
    deduction_married_filing_jointly INTEGER,
    deduction_head_of_household INTEGER,
    deduction_single_and_married_filing_separately INTEGER,
    deduction_married_filing_jointly_amount INTEGER,
    deduction_married_filing_separately_amount INTEGER,
    deduction_single_and_head_of_household_amount INTEGER,
    ceiling_single_and_head_of_household INTEGER,
    exemption INTEGER,
    credit_8812_annual_deduction INTEGER,
    ceiling_self_employment INTEGER,
    exclusion_2555 INTEGER,
    foreign_annual TEXT,
    foreign_monthly TEXT,
    foreign_secondary_annual TEXT,
    foreign_secondary_monthly TEXT,
    dollar_annual TEXT,
    dollar_monthly TEXT,
    dollar_secondary_annual TEXT,
    dollar_secondary_monthly TEXT,
    additional_8812_child_credit TEXT,
    ceiling_married_filing_jointly INTEGER,
    ceiling_married_filing_separately INTEGER,
    show BOOLEAN NOT NULL DEFAULT TRUE,
    created TIMESTAMPTZ DEFAULT now(),
    updated TIMESTAMPTZ DEFAULT now()
);

CREATE TABLE tax_years (
    id SERIAL PRIMARY KEY,
    client_id INTEGER REFERENCES clients ON DELETE CASCADE,
    year TEXT REFERENCES year_details ON DELETE CASCADE,
    archived BOOLEAN NOT NULL DEFAULT FALSE,
    irs_history BOOLEAN NOT NULL DEFAULT FALSE
);

CREATE TABLE users (
    id SERIAL PRIMARY KEY,
    first_name TEXT,
    last_name TEXT,
    username TEXT,
    send_login_notifications BOOLEAN NOT NULL DEFAULT FALSE,
    notify_of_logins BOOLEAN NOT NULL DEFAULT FALSE,
    seconds_in_day INTEGER,
    allow_texting BOOLEAN NOT NULL DEFAULT FALSE,
    selectable BOOLEAN NOT NULL DEFAULT FALSE,
    user_type TEXT,
    password TEXT,
    created TIMESTAMPTZ DEFAULT now(),
    updated TIMESTAMPTZ DEFAULT now()
);

CREATE TABLE sessions (
    token TEXT PRIMARY KEY,
    user_id INTEGER NOT NULL REFERENCES users ON DELETE CASCADE,
    created TIMESTAMPTZ DEFAULT NOW()
);

CREATE TABLE logs (
    id SERIAL PRIMARY KEY,
    client_id INTEGER REFERENCES clients ON DELETE CASCADE,
    years TEXT,
    alarm_user_id INTEGER REFERENCES users ON DELETE CASCADE,
    alert BOOLEAN NOT NULL DEFAULT FALSE,
    alarm_complete BOOLEAN NOT NULL DEFAULT FALSE,
    alarm_date DATE,
    alarm_time TEXT,
    log_date DATE,
    priority INTEGER,
    note TEXT,
    seconds_spent INTEGER,
    archived BOOLEAN NOT NULL DEFAULT FALSE,
    alerted BOOLEAN NOT NULL DEFAULT FALSE
);

CREATE TABLE tax_personals (
    id SERIAL PRIMARY KEY,
    client_id INTEGER REFERENCES clients ON DELETE CASCADE,
    category TEXT,
    include BOOLEAN NOT NULL DEFAULT TRUE,
    language TEXT,
    relation TEXT,
    first_name TEXT,
    middle_initial TEXT,
    last_name TEXT,
    date_of_birth DATE,
    ssn TEXT,
    informal TEXT,
    archived BOOLEAN NOT NULL DEFAULT FALSE
);

CREATE TABLE filings (
    id SERIAL PRIMARY KEY,
    tax_form TEXT,
    status TEXT,
    status_detail TEXT,
    status_date DATE,
    memo TEXT,
    include_in_refund BOOLEAN NOT NULL DEFAULT FALSE,
    owes FLOAT,
    paid FLOAT,
    include_fee BOOLEAN NOT NULL DEFAULT FALSE,
    owes_fee FLOAT,
    paid_fee FLOAT,
    file_type TEXT,
    refund FLOAT,
    rebate FLOAT,
    completed BOOLEAN NOT NULL DEFAULT FALSE,
    delivery_contact TEXT,
    second_delivery_contact TEXT,
    date_filed DATE,
    currency TEXT,
    filing_type TEXT,
    state TEXT,
    tax_year_id INTEGER,
    sort_order INTEGER,
    amount FLOAT
);

-- CREATE TABLE textees (
--     id SERIAL PRIMARY KEY,
--     inactive BOOLEAN NOT NULL DEFAULT FALSE,
--     phone_number TEXT
-- );
--
--
-- CREATE TABLE activity_histories (
--     id SERIAL PRIMARY KEY,
--     created TIMESTAMPTZ DEFAULT now(),
--     updated TIMESTAMPTZ DEFAULT now(),
--     user_id INTEGER REFERENCES users ON DELETE CASCADE,
--     client_id INTEGER REFERENCES clients ON DELETE CASCADE
-- );
--
-- CREATE TABLE audits (
--     id SERIAL PRIMARY KEY,
--     auditable_id INTEGER,
--     auditable_type TEXT,
--     associated_id INTEGER,
--     associated_type TEXT,
--     user_id INTEGER REFERENCES users ON DELETE CASCADE,
--     user_type TEXT,
--     username TEXT,
--     action TEXT,
--     audited_changes TEXT,
--     version INTEGER DEFAULT 0,
--     comment TEXT,
--     remote_address TEXT,
--     request_uuid TEXT,
--     created TIMESTAMPTZ DEFAULT now()
-- );
--
-- CREATE TABLE authorizations (
--     id SERIAL PRIMARY KEY,
--     key TEXT,
--     description TEXT,
--     created TIMESTAMPTZ DEFAULT now(),
--     updated TIMESTAMPTZ DEFAULT now()
-- );
--
-- CREATE TABLE checklist_items (
--     id SERIAL PRIMARY KEY,
--     archived BOOLEAN NOT NULL DEFAULT FALSE,
--     client_id INTEGER REFERENCES clients ON DELETE CASCADE,
--     finished BOOLEAN NOT NULL DEFAULT FALSE,
--     memo TEXT,
--     tax_year_id INTEGER REFERENCES tax_years ON DELETE CASCADE,
--     translated BOOLEAN NOT NULL DEFAULT FALSE,
--     sort_number INTEGER
-- );
--
-- CREATE TABLE client_flags (
--     id SERIAL PRIMARY KEY,
--     client_id INTEGER REFERENCES clients ON DELETE CASCADE,
--     user_id INTEGER REFERENCES users ON DELETE CASCADE,
--     flag INTEGER
-- );
--
-- CREATE TABLE counties (
--     id SERIAL PRIMARY KEY,
--     tax_rate FLOAT DEFAULT 0.0,
--     countie_name TEXT
-- );
--
-- CREATE TABLE fbar_breakdown_tax_years (
--     id SERIAL PRIMARY KEY,
--     fbar_breakdown_id INTEGER REFERENCES fbar_breakdowns ON DELETE CASCADE,
--     tax_year_id INTEGER REFERENCES tax_years ON DELETE CASCADE,
--     year_name_id INTEGER,
--     year_detail_id INTEGER REFERENCES year_details ON DELETE CASCADE
-- );
--
-- CREATE TABLE fees (
--     id SERIAL PRIMARY KEY,
--     client_id INTEGER REFERENCES clients ON DELETE CASCADE,
--     status_id INTEGER,
--     status_detail_id INTEGER,
--     fee_type_id INTEGER,
--     date_fee DATE,
--     manual_amount FLOAT,
--     paid_amount FLOAT,
--     rate FLOAT,
--     notes TEXT,
--     archived BOOLEAN NOT NULL DEFAULT FALSE,
--     include BOOLEAN NOT NULL DEFAULT TRUE,
--     sum BOOLEAN NOT NULL DEFAULT FALSE,
--     year_name TEXT,
--     currency TEXT,
--     year_ids TEXT
-- );
--
-- CREATE TABLE fee_tax_years (
--     id SERIAL PRIMARY KEY,
--     tax_year_id INTEGER REFERENCES tax_years ON DELETE CASCADE,
--     fee_id INTEGER REFERENCES fees ON DELETE CASCADE,
--     year_name_id INTEGER,
--     year_detail_id INTEGER REFERENCES year_details ON DELETE CASCADE
-- );
--
-- CREATE TABLE im_recipients (
--    id SERIAL PRIMARY KEY,
--    created TIMESTAMPTZ DEFAULT now(),
--    updated TIMESTAMPTZ DEFAULT now(),
--    internal_message_id INTEGER,
--    recipient_id INTEGER,
--    growled BOOLEAN NOT NULL DEFAULT FALSE,
--    received BOOLEAN NOT NULL DEFAULT FALSE
-- );
--
-- CREATE TABLE income_breakdown_tax_years (
--     id SERIAL PRIMARY KEY,
--     income_breakdown_id INTEGER,
--     tax_year_id INTEGER REFERENCES tax_years ON DELETE CASCADE,
--     year_name_id INTEGER,
--     year_detail_id INTEGER REFERENCES year_details ON DELETE CASCADE
-- );
--
-- CREATE TABLE income_breakdowns (
--    id SERIAL PRIMARY KEY,
--    archived BOOLEAN NOT NULL DEFAULT FALSE,
--    category_id INTEGER,
--    tax_group_id INTEGER,
--    tax_type_id INTEGER,
--    job_id INTEGER,
--    currency_id INTEGER,
--    frequency INTEGER,
--    documents TEXT,
--    description TEXT,
--    amount FLOAT,
--    exclusion BOOLEAN NOT NULL DEFAULT FALSE,
--    client_id INTEGER REFERENCES clients ON DELETE CASCADE,
--    include BOOLEAN NOT NULL DEFAULT TRUE,
--    depend TEXT,
--    year_name TEXT,
--    year_ids TEXT
-- );
--
-- CREATE TABLE internal_messages (
--    id SERIAL PRIMARY KEY,
--    original_message_id INTEGER,
--    message TEXT,
--    sender_id INTEGER,
--    conversation_id INTEGER,
--    created TIMESTAMPTZ DEFAULT now(),
--    updated TIMESTAMPTZ DEFAULT now()
-- );
--
-- CREATE TABLE log_tax_years (
--     id SERIAL PRIMARY KEY,
--     log_id INTEGER,
--     tax_year_id INTEGER REFERENCES tax_years ON DELETE CASCADE,
--     year_name_id INTEGER,
--     year_detail_id INTEGER REFERENCES year_details ON DELETE CASCADE
-- );
--
-- CREATE TABLE logs (
--     id SERIAL PRIMARY KEY,
--     archived BOOLEAN NOT NULL DEFAULT FALSE,
--     note TEXT,
--     log_date DATE,
--     alarm_date DATE,
--     alert BOOLEAN NOT NULL DEFAULT FALSE,
--     alarm_complete BOOLEAN NOT NULL DEFAULT FALSE,
--     alarm_user_id INTEGER REFERENCES users ON DELETE CASCADE,
--     time_spent FLOAT,
--     client_id INTEGER REFERENCES clients ON DELETE CASCADE,
--     alarm_time TEXT,
--     priority INTEGER,
--     year_name TEXT,
--     alerted BOOLEAN NOT NULL DEFAULT FALSE,
--     year_ids TEXT
-- );
--
-- CREATE TABLE notifications (
--     id SERIAL PRIMARY KEY,
--     key TEXT,
--     description TEXT,
--     created TIMESTAMPTZ DEFAULT now(),
--     updated TIMESTAMPTZ DEFAULT now()
-- );
--
-- CREATE TABLE placements (
--     id SERIAL PRIMARY KEY,
--     name TEXT,
--     abbreviation TEXT,
--     created TIMESTAMPTZ DEFAULT now(),
--     updated TIMESTAMPTZ DEFAULT now()
-- );
--
-- CREATE TABLE roles (
--     id SERIAL PRIMARY KEY,
--     name TEXT,
--     all_authorizations BOOLEAN NOT NULL DEFAULT FALSE,
--     created TIMESTAMPTZ DEFAULT now(),
--     updated TIMESTAMPTZ DEFAULT now()
-- );
--
--
-- CREATE TABLE role_authorizations (
--     id SERIAL PRIMARY KEY,
--     role_id INTEGER REFERENCES roles,
--     authorization_id INTEGER,
--     created TIMESTAMPTZ DEFAULT now(),
--     updated TIMESTAMPTZ DEFAULT now()
-- );
--
-- CREATE TABLE shekelators (
--     id SERIAL PRIMARY KEY,
--     tax_year_id INTEGER REFERENCES tax_years ON DELETE CASCADE,
--     filing_status TEXT,
--     children_under_16 INTEGER,
--     children_over_16 INTEGER,
--     childcare_count INTEGER,
--     childcare_expenses FLOAT,
--     primary_income FLOAT,
--     primary_foreign_tax FLOAT,
--     secondary_income FLOAT,
--     secondary_foreign_tax FLOAT,
--     self_employment_income FLOAT,
--     self_employment_foreign_tax FLOAT,
--     passive_income FLOAT,
--     passive_foreign_tax FLOAT,
--     total FLOAT,
--     primary_currency_id INTEGER,
--     secondary_currency_id INTEGER,
--     passive_exclusion_2555 BOOLEAN NOT NULL DEFAULT FALSE,
--     secondary_exclusion_2555 BOOLEAN NOT NULL DEFAULT FALSE,
--     primary_exclusion_2555 BOOLEAN NOT NULL DEFAULT FALSE,
--     self_employment_exclusion_2555 BOOLEAN NOT NULL DEFAULT FALSE
-- );
--
-- CREATE TABLE smartviews (
--     id SERIAL PRIMARY KEY,
--     created TIMESTAMPTZ DEFAULT now(),
--     updated TIMESTAMPTZ DEFAULT now(),
--     user_id INTEGER REFERENCES users ON DELETE CASCADE,
--     name TEXT,
--     sort_number INTEGER,
--     archived BOOLEAN NOT NULL DEFAULT FALSE,
--     client_count INTEGER
-- );
--
-- CREATE TABLE smartview_lines (
--     id SERIAL PRIMARY KEY,
--     created TIMESTAMPTZ DEFAULT now(),
--     updated TIMESTAMPTZ DEFAULT now(),
--     smartview_id INTEGER REFERENCES smartviews ON DELETE CASCADE,
--     group_number INTEGER,
--     class_to_join TEXT,
--     field_to_search TEXT,
--     operator TEXT,
--     search_value TEXT
-- );
--
-- CREATE TABLE status_histories (
--     id SERIAL PRIMARY KEY,
--     client_id INTEGER REFERENCES clients ON DELETE CASCADE,
--     old_status_id INTEGER,
--     new_status_id INTEGER,
--     created TIMESTAMPTZ DEFAULT now(),
--     updated TIMESTAMPTZ DEFAULT now()
-- );
--
-- CREATE TABLE tax_brackets (
--     id SERIAL PRIMARY KEY,
--     year_detail_id INTEGER REFERENCES year_details ON DELETE CASCADE,
--     bracket_type TEXT,
--     income_amount INTEGER,
--     rate INTEGER
-- );
--
-- CREATE TABLE tax_floors (
--     id SERIAL PRIMARY KEY,
--     year_detail_id INTEGER REFERENCES year_details ON DELETE CASCADE ,
--     floor FLOAT,
--     apply_percentage FLOAT,
--     filing_type TEXT
-- );
--
-- CREATE TABLE time_slips (
--     id SERIAL PRIMARY KEY,
--     user_id INTEGER REFERENCES users ON DELETE CASCADE,
--     time_in TIMESTAMPTZ,
--     time_out TIMESTAMPTZ,
--     memo TEXT,
--     seconds_in_shift INTEGER,
--     all_ot BOOLEAN NOT NULL DEFAULT FALSE,
--     created TIMESTAMPTZ DEFAULT now(),
--     updated TIMESTAMPTZ DEFAULT now()
-- );
--
-- CREATE TABLE user_notifications (
--     id SERIAL PRIMARY KEY,
--     user_id INTEGER REFERENCES users ON DELETE CASCADE,
--     notification_id INTEGER,
--     created TIMESTAMPTZ DEFAULT now(),
--     updated TIMESTAMPTZ DEFAULT now()
-- );
--
-- CREATE TABLE value_lists (
--     id SERIAL PRIMARY KEY,
--     sort_order INTEGER,
--     key TEXT,
--     value TEXT,
--     parent_id INTEGER,
--     translation_needed BOOLEAN NOT NULL DEFAULT FALSE,
--     passive BOOLEAN NOT NULL DEFAULT FALSE,
--     self_employment BOOLEAN NOT NULL DEFAULT FALSE,
--     show BOOLEAN NOT NULL DEFAULT TRUE,
--     sub_type TEXT,
--     include BOOLEAN NOT NULL DEFAULT TRUE,
--     year_detail_id INTEGER REFERENCES year_details ON DELETE CASCADE
-- );



