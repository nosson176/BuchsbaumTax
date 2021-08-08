CREATE TABLE textees (
    id SERIAL PRIMARY KEY,
    inactive BOOLEAN NOT NULL DEFAULT FALSE,
    phone_number TEXT
);

CREATE TABLE year_details (
    id SERIAL PRIMARY KEY,
    deduction_married_filing_jointly INTEGER,
    deduction_head_of_household INTEGER,
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

CREATE TABLE users (
    id SERIAL PRIMARY KEY,
    first_name TEXT,
    last_name TEXT,
    email TEXT,
    gender TEXT,
    file_filename TEXT,
    file_content_type TEXT,
    country TEXT,
    file_size TEXT,
    crypted_password TEXT NOT NULL,
    password_salt TEXT NOT NULL,
    persistence_token TEXT NOT NULL,
    single_access_token TEXT NOT NULL,
    perishable_token TEXT NOT NULL,
    login_count INTEGER NOT NULL DEFAULT 0,
    failed_login_count INTEGER NOT NULL DEFAULT 0,
    last_request_at TIMESTAMPTZ,
    current_login_at TIMESTAMPTZ,
    last_login_at TIMESTAMPTZ,
    current_login_ip TEXT,
    last_login_ip TEXT,
    user_type TEXT,
    active BOOLEAN NOT NULL DEFAULT TRUE,
    permanent BOOLEAN NOT NULL DEFAULT FALSE,
    boolean BOOLEAN NOT NULL DEFAULT FALSE,
    allow_texting BOOLEAN NOT NULL DEFAULT FALSE,
    send_login_notifications BOOLEAN NOT NULL DEFAULT FALSE,
    notify_of_logins BOOLEAN NOT NULL DEFAULT FALSE,
    seconds_in_day INTEGER,
    selectable BOOLEAN NOT NULL DEFAULT FALSE,
    login TEXT,
    edit_time_slips BOOLEAN NOT NULL DEFAULT FALSE,
    created TIMESTAMPTZ DEFAULT now(),
    updated TIMESTAMPTZ DEFAULT now()
);

CREATE TABLE clients (
    id SERIAL PRIMARY KEY,
    archived BOOLEAN NOT NULL DEFAULT FALSE,
    first_name TEXT,
    last_name TEXT,
    status_id INTEGER,
    owes_status_id INTEGER,
    periodical TEXT,
    sort_number INTEGER,
    flags TEXT,
    checklist_memo TEXT,
    list_name_display TEXT,
    list_phone_display TEXT,
    list_last_log TEXT,
    current_status TEXT,
    year_of_first_log TEXT,
    year_of_last_log TEXT,
    date_of_first_log DATE
);

CREATE TABLE activity_histories (
    id SERIAL PRIMARY KEY,
    created TIMESTAMPTZ DEFAULT now(),
    updated TIMESTAMPTZ DEFAULT now(),
    user_id INTEGER REFERENCES users ON DELETE CASCADE,
    client_id INTEGER REFERENCES clients ON DELETE CASCADE
);

CREATE TABLE audits (
    id SERIAL PRIMARY KEY,
    auditable_id INTEGER,
    auditable_type TEXT,
    associated_id INTEGER,
    associated_type TEXT,
    user_id INTEGER REFERENCES users ON DELETE CASCADE,
    user_type TEXT,
    username TEXT,
    action TEXT,
    audited_changes TEXT,
    version INTEGER DEFAULT 0,
    comment TEXT,
    remote_address TEXT,
    request_uuid TEXT,
    created TIMESTAMPTZ DEFAULT now()
);

CREATE TABLE authorizations (
    id SERIAL PRIMARY KEY,
    key TEXT,
    description TEXT,
    created TIMESTAMPTZ DEFAULT now(),
    updated TIMESTAMPTZ DEFAULT now()
);

CREATE TABLE tax_years (
    id SERIAL PRIMARY KEY,
    year_detail_id INTEGER REFERENCES year_details ON DELETE CASCADE,
    client_id INTEGER REFERENCES clients ON DELETE CASCADE,
    shekelator_total FLOAT,
    archived BOOLEAN NOT NULL DEFAULT FALSE,
    year_name TEXT,
    fm_id TEXT,
    irs_history BOOLEAN NOT NULL DEFAULT FALSE,
    last_year_name TEXT
);

CREATE TABLE checklist_items (
    id SERIAL PRIMARY KEY,
    archived BOOLEAN NOT NULL DEFAULT FALSE,
    client_id INTEGER REFERENCES clients ON DELETE CASCADE,
    finished BOOLEAN NOT NULL DEFAULT FALSE,
    memo TEXT,
    tax_year_id INTEGER REFERENCES tax_years ON DELETE CASCADE,
    translated BOOLEAN NOT NULL DEFAULT FALSE,
    sort_number INTEGER
);

CREATE TABLE client_flags (
    id SERIAL PRIMARY KEY,
    client_id INTEGER REFERENCES clients ON DELETE CASCADE,
    user_id INTEGER REFERENCES users ON DELETE CASCADE,
    flag INTEGER
);

CREATE TABLE contacts (
    id SERIAL PRIMARY KEY,
    client_id INTEGER,
    disabled BOOLEAN NOT NULL DEFAULT FALSE,
    contact_type_id INTEGER,
    memo TEXT,
    main_detail TEXT,
    secondary_detail TEXT,
    state TEXT,
    zip INTEGER,
    archived BOOLEAN NOT NULL DEFAULT FALSE
);

CREATE TABLE counties (
    id SERIAL PRIMARY KEY,
    tax_rate FLOAT DEFAULT 0.0,
    countie_name TEXT
);

CREATE TABLE exchange_rates (
    id SERIAL PRIMARY KEY,
    rate FLOAT,
    show BOOLEAN NOT NULL DEFAULT TRUE,
    year_detail_id INTEGER REFERENCES year_details ON DELETE CASCADE,
    currency_id INTEGER
);

CREATE TABLE fbar_breakdowns (
    id SERIAL PRIMARY KEY,
    client_id INTEGER REFERENCES clients ON DELETE CASCADE,
    archived BOOLEAN NOT NULL DEFAULT FALSE,
    category_id INTEGER,
    tax_group_id INTEGER,
    tax_type_id INTEGER,
    part_id INTEGER,
    amount FLOAT,
    currency_id INTEGER,
    frequency INTEGER,
    documents TEXT,
    description TEXT,
    depend TEXT,
    include BOOLEAN NOT NULL DEFAULT TRUE,
    year_name TEXT,
    year_ids TEXT
);

CREATE TABLE fbar_breakdown_tax_years (
    id SERIAL PRIMARY KEY,
    fbar_breakdown_id INTEGER REFERENCES fbar_breakdowns ON DELETE CASCADE,
    tax_year_id INTEGER REFERENCES tax_years ON DELETE CASCADE,
    year_name_id INTEGER,
    year_detail_id INTEGER REFERENCES year_details ON DELETE CASCADE
);

CREATE TABLE fees (
    id SERIAL PRIMARY KEY,
    client_id INTEGER REFERENCES clients ON DELETE CASCADE,
    status_id INTEGER,
    status_detail_id INTEGER,
    fee_type_id INTEGER,
    date_fee DATE,
    manual_amount FLOAT,
    paid_amount FLOAT,
    rate FLOAT,
    notes TEXT,
    archived BOOLEAN NOT NULL DEFAULT FALSE,
    include BOOLEAN NOT NULL DEFAULT TRUE,
    sum BOOLEAN NOT NULL DEFAULT FALSE,
    year_name TEXT,
    currency TEXT,
    year_ids TEXT
);

CREATE TABLE fee_tax_years (
    id SERIAL PRIMARY KEY,
    tax_year_id INTEGER REFERENCES tax_years ON DELETE CASCADE,
    fee_id INTEGER REFERENCES fees ON DELETE CASCADE,
    year_name_id INTEGER,
    year_detail_id INTEGER REFERENCES year_details ON DELETE CASCADE
);

CREATE TABLE filings (
    id SERIAL PRIMARY KEY,
    tax_form_id INTEGER,
    status_id INTEGER,
    status_detail_id INTEGER,
    status_date DATE,
    filing_type TEXT,
    state TEXT,
    memo TEXT,
    owes FLOAT,
    paid FLOAT,
    owes_fee FLOAT,
    paid_fee FLOAT,
    include_in_refund BOOLEAN NOT NULL DEFAULT FALSE,
    include_fee BOOLEAN NOT NULL DEFAULT FALSE,
    file_type_id INTEGER,
    refund FLOAT,
    rebate FLOAT,
    delivery_contact_id INTEGER,
    second_delivery_contact_id INTEGER,
    date_filed DATE,
    tax_year_id INTEGER,
    completed BOOLEAN NOT NULL DEFAULT FALSE,
    currency TEXT,
    sort_order INTEGER
);

CREATE TABLE im_recipients (
   id SERIAL PRIMARY KEY,
   created TIMESTAMPTZ DEFAULT now(),
   updated TIMESTAMPTZ DEFAULT now(),
   internal_message_id INTEGER,
   recipient_id INTEGER,
   growled BOOLEAN NOT NULL DEFAULT FALSE,
   received BOOLEAN NOT NULL DEFAULT FALSE
);

CREATE TABLE income_breakdown_tax_years (
    id SERIAL PRIMARY KEY,
    income_breakdown_id INTEGER,
    tax_year_id INTEGER REFERENCES tax_years ON DELETE CASCADE,
    year_name_id INTEGER,
    year_detail_id INTEGER REFERENCES year_details ON DELETE CASCADE
);

CREATE TABLE income_breakdowns (
   id SERIAL PRIMARY KEY,
   archived BOOLEAN NOT NULL DEFAULT FALSE,
   category_id INTEGER,
   tax_group_id INTEGER,
   tax_type_id INTEGER,
   job_id INTEGER,
   currency_id INTEGER,
   frequency INTEGER,
   documents TEXT,
   description TEXT,
   amount FLOAT,
   exclusion BOOLEAN NOT NULL DEFAULT FALSE,
   client_id INTEGER REFERENCES clients ON DELETE CASCADE,
   include BOOLEAN NOT NULL DEFAULT TRUE,
   depend TEXT,
   year_name TEXT,
   year_ids TEXT
);

CREATE TABLE internal_messages (
   id SERIAL PRIMARY KEY,
   original_message_id INTEGER,
   message TEXT,
   sender_id INTEGER,
   conversation_id INTEGER,
   created TIMESTAMPTZ DEFAULT now(),
   updated TIMESTAMPTZ DEFAULT now()
);

CREATE TABLE log_tax_years (
    id SERIAL PRIMARY KEY,
    log_id INTEGER,
    tax_year_id INTEGER REFERENCES tax_years ON DELETE CASCADE,
    year_name_id INTEGER,
    year_detail_id INTEGER REFERENCES year_details ON DELETE CASCADE
);

CREATE TABLE logs (
    id SERIAL PRIMARY KEY,
    archived BOOLEAN NOT NULL DEFAULT FALSE,
    note TEXT,
    log_date DATE,
    alarm_date DATE,
    alert BOOLEAN NOT NULL DEFAULT FALSE,
    alarm_complete BOOLEAN NOT NULL DEFAULT FALSE,
    alarm_user_id INTEGER REFERENCES users ON DELETE CASCADE,
    time_spent FLOAT,
    client_id INTEGER REFERENCES clients ON DELETE CASCADE,
    alarm_time TEXT,
    priority INTEGER,
    year_name TEXT,
    alerted BOOLEAN NOT NULL DEFAULT FALSE,
    year_ids TEXT
);

CREATE TABLE notifications (
    id SERIAL PRIMARY KEY,
    key TEXT,
    description TEXT,
    created TIMESTAMPTZ DEFAULT now(),
    updated TIMESTAMPTZ DEFAULT now()
);

CREATE TABLE placements (
    id SERIAL PRIMARY KEY,
    name TEXT,
    abbreviation TEXT,
    created TIMESTAMPTZ DEFAULT now(),
    updated TIMESTAMPTZ DEFAULT now()
);

CREATE TABLE roles (
    id SERIAL PRIMARY KEY,
    name TEXT,
    all_authorizations BOOLEAN NOT NULL DEFAULT FALSE,
    created TIMESTAMPTZ DEFAULT now(),
    updated TIMESTAMPTZ DEFAULT now()
);


CREATE TABLE role_authorizations (
    id SERIAL PRIMARY KEY,
    role_id INTEGER REFERENCES roles,
    authorization_id INTEGER,
    created TIMESTAMPTZ DEFAULT now(),
    updated TIMESTAMPTZ DEFAULT now()
);

CREATE TABLE shekelators (
    id SERIAL PRIMARY KEY,
    tax_year_id INTEGER REFERENCES tax_years ON DELETE CASCADE,
    filing_status TEXT,
    children_under_16 INTEGER,
    children_over_16 INTEGER,
    childcare_count INTEGER,
    childcare_expenses FLOAT,
    primary_income FLOAT,
    primary_foreign_tax FLOAT,
    secondary_income FLOAT,
    secondary_foreign_tax FLOAT,
    self_employment_income FLOAT,
    self_employment_foreign_tax FLOAT,
    passive_income FLOAT,
    passive_foreign_tax FLOAT,
    total FLOAT,
    primary_currency_id INTEGER,
    secondary_currency_id INTEGER,
    passive_exclusion_2555 BOOLEAN NOT NULL DEFAULT FALSE,
    secondary_exclusion_2555 BOOLEAN NOT NULL DEFAULT FALSE,
    primary_exclusion_2555 BOOLEAN NOT NULL DEFAULT FALSE,
    self_employment_exclusion_2555 BOOLEAN NOT NULL DEFAULT FALSE
);

CREATE TABLE smartviews (
    id SERIAL PRIMARY KEY,
    created TIMESTAMPTZ DEFAULT now(),
    updated TIMESTAMPTZ DEFAULT now(),
    user_id INTEGER REFERENCES users ON DELETE CASCADE,
    name TEXT,
    sort_number INTEGER,
    archived BOOLEAN NOT NULL DEFAULT FALSE,
    client_count INTEGER
);

CREATE TABLE smartview_lines (
    id SERIAL PRIMARY KEY,
    created TIMESTAMPTZ DEFAULT now(),
    updated TIMESTAMPTZ DEFAULT now(),
    smartview_id INTEGER REFERENCES smartviews ON DELETE CASCADE,
    group_number INTEGER,
    class_to_join TEXT,
    field_to_search TEXT,
    operator TEXT,
    search_value TEXT
);

CREATE TABLE status_histories (
    id SERIAL PRIMARY KEY,
    client_id INTEGER REFERENCES clients ON DELETE CASCADE,
    old_status_id INTEGER,
    new_status_id INTEGER,
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

CREATE TABLE tax_floors (
    id SERIAL PRIMARY KEY,
    year_detail_id INTEGER REFERENCES year_details ON DELETE CASCADE ,
    floor FLOAT,
    apply_percentage FLOAT,
    filing_type TEXT
);

CREATE TABLE tax_personals (
    id SERIAL PRIMARY KEY,
    archived BOOLEAN NOT NULL DEFAULT FALSE,
    client_id INTEGER REFERENCES clients ON DELETE CASCADE,
    category_id INTEGER,
    first_name TEXT,
    middle_initial TEXT,
    last_name TEXT,
    date_of_birth DATE,
    ssn TEXT,
    informal TEXT,
    relation_id INTEGER,
    language_id INTEGER,
    include BOOLEAN NOT NULL DEFAULT TRUE
);

CREATE TABLE time_slips (
    id SERIAL PRIMARY KEY,
    user_id INTEGER REFERENCES users ON DELETE CASCADE,
    time_in TIMESTAMPTZ,
    time_out TIMESTAMPTZ,
    memo TEXT,
    seconds_in_shift INTEGER,
    all_ot BOOLEAN NOT NULL DEFAULT FALSE,
    created TIMESTAMPTZ DEFAULT now(),
    updated TIMESTAMPTZ DEFAULT now()
);

CREATE TABLE user_notifications (
    id SERIAL PRIMARY KEY,
    user_id INTEGER REFERENCES users ON DELETE CASCADE,
    notification_id INTEGER,
    created TIMESTAMPTZ DEFAULT now(),
    updated TIMESTAMPTZ DEFAULT now()
);

CREATE TABLE value_lists (
    id SERIAL PRIMARY KEY,
    sort_order INTEGER,
    key TEXT,
    value TEXT,
    parent_id INTEGER,
    translation_needed BOOLEAN NOT NULL DEFAULT FALSE,
    passive BOOLEAN NOT NULL DEFAULT FALSE,
    self_employment BOOLEAN NOT NULL DEFAULT FALSE,
    show BOOLEAN NOT NULL DEFAULT TRUE,
    sub_type TEXT,
    include BOOLEAN NOT NULL DEFAULT TRUE,
    year_detail_id INTEGER REFERENCES year_details ON DELETE CASCADE
);



