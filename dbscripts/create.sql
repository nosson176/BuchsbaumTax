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

CREATE TABLE audits (
    id SERIAL PRIMARY KEY,
    auditable_id INTEGER,
    auditable_type TEXT,
    associated_id INTEGER,
    associated_type TEXT,
    user_id INTEGER,
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

CREATE TABLE billing_addresses (
    id SERIAL PRIMARY KEY,
    organization_id INTEGER,
    contact TEXT,
    address_1 TEXT,
    address_2 TEXT,
    city TEXT,
    state TEXT,
    zip INTEGER,
    phone TEXT,
    email TEXT
);

CREATE TABLE cads (
    id SERIAL PRIMARY KEY,
    order_id INTEGER,
    order_line_id INTEGER,
    file_filename TEXT,
    file_size INTEGER,
    file_content_type TEXT,
    description TEXT,
    created TIMESTAMPTZ DEFAULT now(),
    updated TIMESTAMPTZ DEFAULT now(),
    created_by_user_id INTEGER,
    internal BOOLEAN DEFAULT FALSE,
    customer_approved BOOLEAN DEFAULT FALSE,
    new_revision BOOLEAN DEFAULT FALSE,
    revision_status TEXT,
    note TEXT
);

CREATE TABLE checklist_items (
    id SERIAL PRIMARY KEY,
    archived BOOLEAN NOT NULL DEFAULT FALSE,
    client_id INTEGER,
    finished BOOLEAN NOT NULL DEFAULT FALSE,
    memo TEXT,
    tax_year_id TEXT,
    translated BOOLEAN NOT NULL DEFAULT FALSE,
    sort_number INTEGER
);

CREATE TABLE client_flags (
    id SERIAL PRIMARY KEY,
    client_id INTEGER,
    user_id INTEGER,
    flag INTEGER
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

CREATE TABLE documents (
    file_filename TEXT,
    file_content_type TEXT,
    file_size INTEGER,
    document_type TEXT,
    description TEXT,
    documentable_id INTEGER,
    created TIMESTAMPTZ DEFAULT now(),
    updated TIMESTAMPTZ DEFAULT now(),
    documentable_type TEXT,
    payment BOOLEAN NOT NULL DEFAULT FALSE,
    view TEXT,
    design_type TEXT
);

CREATE TABLE exchange_rates (
    id SERIAL PRIMARY KEY,
    rate FLOAT,
    show BOOLEAN NOT NULL DEFAULT TRUE,
    year_detail_id INTEGER,
    currency_id INTEGER
);

CREATE TABLE fabrics (
    id SERIAL PRIMARY KEY,
    order_line_id INTEGER,
    yards_needed FLOAT DEFAULT 0.0,
    warehouse_received BOOLEAN NOT NULL DEFAULT FALSE,
    factory_received BOOLEAN NOT NULL DEFAULT FALSE,
    customer_provided BOOLEAN NOT NULL DEFAULT TRUE,
    order_index INTEGER,
    placement TEXT,
    content TEXT,
    country_of_origin TEXT,
    repeat TEXT,
    order_material_id INTEGER,
    location TEXT,
    item_id INTEGER,
    item_color_id INTEGER,
    rotation INTEGER,
    orientation TEXT,
    price_per_yard FLOAT DEFAULT 0.0,
    total_fabric_amount INTEGER,
    receive_from_stock BOOLEAN DEFAULT FALSE,
    kovet_fabric BOOLEAN DEFAULT FALSE,
    unit_of_measure TEXT,
    catalog_item_id INTEGER
);

CREATE TABLE fbar_breakdown_tax_years (
    id SERIAL PRIMARY KEY,
    fbar_breakdown_id INTEGER,
    tax_year_id INTEGER,
    year_name_id INTEGER,
    year_detail_id INTEGER
);

CREATE TABLE fbar_breakdowns (
    id SERIAL PRIMARY KEY,
    client_id INTEGER,
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

CREATE TABLE fee_tax_years (
    id SERIAL PRIMARY KEY,
    tax_year_id INTEGER,
    fee_id INTEGER,
    year_name_id INTEGER,
    year_detail_id INTEGER
);

CREATE TABLE fees (
    id SERIAL PRIMARY KEY,
    client_id INTEGER,
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

CREATE TABLE finish_items (
    id SERIAL PRIMARY KEY,
    order_line_id INTEGER,
    order_finish_id INTEGER,
    finish_required BOOLEAN NOT NULL DEFAULT FALSE,
    finish_approved_date DATE,
    sent_date DATE,
    location TEXT,
    item_id INTEGER,
    approval_email_sent BOOLEAN DEFAULT FALSE
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
    tax_year_id INTEGER,
    year_name_id INTEGER,
    year_detail_id INTEGER
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
   client_id INTEGER,
   include BOOLEAN NOT NULL DEFAULT TRUE,
   depend TEXT,
   year_name TEXT,
   year_ids TEXT
);

CREATE TABLE internal_messages (
   id SERIAL PRIMARY KEY,
   created TIMESTAMPTZ DEFAULT now(),
   updated TIMESTAMPTZ DEFAULT now(),
   original_message_id INTEGER,
   message TEXT,
   sender_id INTEGER,
   conversation_id INTEGER
);

CREATE TABLE inventories (
    id SERIAL PRIMARY KEY,
    client_id INTEGER,
    item_id INTEGER,
    item_color_id INTEGER
);

CREATE TABLE inventory_receipts (
    id SERIAL PRIMARY KEY,
    received_date DATE,
    created TIMESTAMPTZ DEFAULT now(),
    updated TIMESTAMPTZ DEFAULT now()
);

CREATE TABLE item_colors (
     id SERIAL PRIMARY KEY,
     name TEXT,
     image_filename TEXT,
     image_content_type TEXT,
     image_size INTEGER,
     item_id INTEGER,
     created TIMESTAMPTZ DEFAULT now(),
     updated TIMESTAMPTZ DEFAULT now()
);

CREATE TABLE item_contents (
    id SERIAL PRIMARY KEY,
    name TEXT,
    percentage FLOAT DEFAULT 0.0,
    content TEXT,
    item_id INTEGER,
    created TIMESTAMPTZ DEFAULT now(),
    updated TIMESTAMPTZ DEFAULT now()
);


CREATE TABLE item_line_processes (
    id SERIAL PRIMARY KEY,
    item_id INTEGER,
    default_process_id INTEGER,
    days_to_complete INTEGER DEFAULT 14,
    created TIMESTAMPTZ DEFAULT now(),
    updated TIMESTAMPTZ DEFAULT now()
);

CREATE TABLE item_parts (
    id SERIAL PRIMARY KEY,
    part_id INTEGER,
    item_id INTEGER,
    created TIMESTAMPTZ DEFAULT now(),
    updated TIMESTAMPTZ DEFAULT now(),
    quantity INTEGER,
    vendor_id INTEGER,
    lead_time_days INTEGER,
    requires_tracking BOOLEAN
);


CREATE TABLE item_types (
    id SERIAL PRIMARY KEY,
    created TIMESTAMPTZ DEFAULT now(),
    updated TIMESTAMPTZ DEFAULT now(),
    name TEXT,
    group_type TEXT,
    item_code TEXT
);

CREATE TABLE items (
    id SERIAL PRIMARY KEY,
    part_type TEXT,
    name TEXT,
    description TEXT,
    image_filename TEXT,
    image_content_type TEXT,
    image_size INTEGER,
    created TIMESTAMPTZ DEFAULT now(),
    updated TIMESTAMPTZ DEFAULT now(),
    price FLOAT DEFAULT 0.0,
    process TEXT,
    group_name TEXT,
    width FLOAT,
    depth FLOAT,
    height FLOAT,
    diameter FLOAT,
    arm_height FLOAT,
    seat_height FLOAT,
    wood_species TEXT,
    back_cushion TEXT,
    seat_upholstery TEXT,
    back_count TEXT,
    seat_count TEXT,
    welt TEXT,
    stretcher TEXT,
    back_filling TEXT,
    seat_filling TEXT,
    nail_heads TEXT,
    color TEXT,
    pattern TEXT,
    item_number TEXT,
    legs TEXT,
    ferrule TEXT,
    glides TEXT,
    metal_finish TEXT,
    stitching TEXT,
    vendor_id INTEGER,
    country_of_origin TEXT,
    content TEXT,
    repeat TEXT,
    weight TEXT,
    yards FLOAT DEFAULT 0.0,
    square_feet FLOAT DEFAULT 0.0,
    certificate_of_origin_filename TEXT,
    certificate_of_origin_content_type TEXT,
    certificate_of_origin_size INTEGER,
    lead_time INTEGER,
    unit_of_measure TEXT,
    customer_id INTEGER,
    standard BOOLEAN NOT NULL DEFAULT FALSE,
    item_type TEXT,
    original_item_id INTEGER,
    duplicate BOOLEAN DEFAULT FALSE,
    family_code TEXT,
    version TEXT,
    difficulty INTEGER,
    sku TEXT,
    sleeper_size TEXT,
    sleeper_mechanism TEXT,
    mattress TEXT,
    electrical TEXT,
    pillow_fill TEXT,
    nail_head_size TEXT,
    catalog_fabric_id INTEGER
);

CREATE TABLE locations (
    id SERIAL PRIMARY KEY,
    address TEXT,
    city TEXT,
    state TEXT,
    zip TEXT,
    code TEXT,
    default_warehouse BOOLEAN NOT NULL DEFAULT FALSE,
    default_factory BOOLEAN NOT NULL DEFAULT FALSE,
    inactive BOOLEAN NOT NULL DEFAULT FALSE
);

CREATE TABLE log_tax_years (
    id SERIAL PRIMARY KEY,
    log_id INTEGER,
    tax_year_id INTEGER,
    year_name_id INTEGER,
    year_detail_id INTEGER
);

CREATE TABLE logs (
    id SERIAL PRIMARY KEY,
    archived BOOLEAN NOT NULL DEFAULT FALSE,
    note TEXT,
    log_date DATE,
    alarm_date DATE,
    alert BOOLEAN NOT NULL DEFAULT FALSE,
    alarm_complete BOOLEAN NOT NULL DEFAULT FALSE,
    alarm_user_id INTEGER,
    time_spent FLOAT,
    client_id INTEGER,
    alarm_time TEXT,
    priority INTEGER,
    year_name TEXT,
    alerted BOOLEAN NOT NULL DEFAULT FALSE,
    year_ids TEXT
);

CREATE TABLE notes (
    id SERIAL PRIMARY KEY,
    notable_id INTEGER,
    notable_type TEXT,
    order_line_id INTEGER,
    created_by_user_id INTEGER,
    resolved_by_user_id INTEGER,
    resolved_at TIMESTAMPTZ,
    text TEXT,
    resolve_text TEXT,
    resolved BOOLEAN NOT NULL DEFAULT FALSE,
    needs_resolution BOOLEAN NOT NULL DEFAULT TRUE,
    created TIMESTAMPTZ DEFAULT now(),
    updated TIMESTAMPTZ DEFAULT now(),
    note_type TEXT,
    cad_note BOOLEAN NOT NULL DEFAULT FALSE,
    due_date DATE,
    order_line_part_id INTEGER,
    from_order_notes BOOLEAN NOT NULL DEFAULT FALSE,
    task BOOLEAN DEFAULT FALSE,
    assigned_to_user_id INTEGER,
    priority TEXT
);

CREATE TABLE notifications (
    id SERIAL PRIMARY KEY,
    key TEXT,
    description TEXT,
    created TIMESTAMPTZ DEFAULT now(),
    updated TIMESTAMPTZ DEFAULT now()
);

CREATE TABLE order_documents (
    id SERIAL PRIMARY KEY,
    document_id INTEGER,
    order_id INTEGER,
    created TIMESTAMPTZ DEFAULT now(),
    updated TIMESTAMPTZ DEFAULT now()
);

CREATE TABLE order_finishes (
    id SERIAL PRIMARY KEY,
    order_id INTEGER,
    standard_finish_id INTEGER,
    file_filename TEXT,
    file_content_type TEXT,
    file_size INTEGER,
    name TEXT,
    item_id INTEGER
);

CREATE TABLE order_line_commissions (
    id SERIAL PRIMARY KEY,
    order_line_id INTEGER,
    order_rep_id INTEGER,
    commission_percentage FLOAT DEFAULT 0.0,
    fabric_commission_percentage FLOAT DEFAULT 0.0
);

CREATE TABLE order_line_documents (
    id SERIAL PRIMARY KEY,
    order_line_id INTEGER,
    document_id INTEGER,
    created TIMESTAMPTZ DEFAULT now(),
    updated TIMESTAMPTZ DEFAULT now()
);

CREATE TABLE order_line_parts (
    id SERIAL PRIMARY KEY,
    part_id INTEGER,
    order_line_id INTEGER,
    created TIMESTAMPTZ DEFAULT now(),
    updated TIMESTAMPTZ DEFAULT now(),
    unit_quantity INTEGER NOT NULL DEFAULT 0,
    quantity_received INTEGER NOT NULL DEFAULT 0,
    total_price FLOAT DEFAULT 0.0,
    process TEXT,
    po_number TEXT,
    description TEXT,
    unit_price FLOAT DEFAULT 0.0,
    factory_id INTEGER,
    vendor_id INTEGER,
    due_date DATE,
    lead_time INTEGER,
    unit_of_measure TEXT,
    material_type_id INTEGER,
    requires_tracking BOOLEAN
);

CREATE TABLE order_line_processes (
    id SERIAL PRIMARY KEY,
    order_line_id INTEGER,
    default_process_id INTEGER,
    subcontractor_name TEXT,
    name TEXT,
    days_to_complete INTEGER DEFAULT 14,
    start_date DATE,
    end_date DATE,
    process_order INTEGER,
    created TIMESTAMPTZ DEFAULT now(),
    updated TIMESTAMPTZ DEFAULT now(),
    shipment_line_id INTEGER,
    quantity_complete INTEGER DEFAULT 0
);

CREATE TABLE order_lines (
    id SERIAL PRIMARY KEY,
    order_id INTEGER,
    item_type TEXT,
    item_number TEXT,
    area TEXT,
    quantity INTEGER,
    image_filename TEXT,
    image_size INTEGER,
    image_content_type TEXT,
    approved_cad_filename TEXT,
    approved_cad_size INTEGER,
    approved_cad_content_type TEXT,
    finish_filename TEXT,
    finish_content_type TEXT,
    finish_size INTEGER,
    finish_name TEXT,
    unit_price FLOAT DEFAULT 0.0,
    width FLOAT,
    depth FLOAT,
    height FLOAT,
    diameter FLOAT,
    arm_height FLOAT,
    seat_height FLOAT,
    wood_species TEXT,
    back_cushion TEXT,
    seat_upholstery TEXT,
    back_count TEXT,
    seat_count TEXT,
    welt TEXT,
    stretcher TEXT,
    back_filling TEXT,
    seat_filling TEXT,
    nail_heads TEXT,
    cad_required BOOLEAN NOT NULL DEFAULT FALSE,
    cad_approved_date DATE,
    no_fabrics BOOLEAN NOT NULL DEFAULT FALSE,
    description TEXT,
    note TEXT,
    legs TEXT,
    ferrule TEXT,
    glides TEXT,
    metal_finish TEXT,
    stitching TEXT,
    factory_id INTEGER,
    pricing_note TEXT,
    cad_sent_date DATE,
    taxable BOOLEAN NOT NULL DEFAULT TRUE,
    commission_unit_price FLOAT DEFAULT 0.0,
    item_id INTEGER,
    sort_order INTEGER,
    internal_cad_filename TEXT,
    internal_cad_size INTEGER,
    internal_cad_content_type TEXT,
    carton_charge FLOAT DEFAULT 0.0,
    upholstery_amount FLOAT,
    pallet_charge FLOAT DEFAULT 0.0,
    difficulty INTEGER,
    sleeper_size TEXT,
    sleeper_mechanism TEXT,
    mattress TEXT,
    electrical TEXT,
    pillow_fill TEXT,
    nail_head_size TEXT,
    sku INTEGER
);


CREATE TABLE order_materials (
    id SERIAL PRIMARY KEY,
    order_id INTEGER,
    fabric_id INTEGER,
    vendor_id INTEGER,
    item_id INTEGER,
    item_color_id INTEGER,
    tot_qty_req INTEGER,
    tot_qty_rec INTEGER,
    tot_qty_ordered INTEGER,
    price_per_yard INTEGER
);

CREATE TABLE order_reps (
    id SERIAL PRIMARY KEY,
    order_id INTEGER,
    user_id INTEGER,
    customer BOOLEAN NOT NULL DEFAULT FALSE,
    commission_due FLOAT,
    hidden BOOLEAN NOT NULL DEFAULT FALSE,
    commission_percentage FLOAT,
    fabric_commission_percentage FLOAT
);

CREATE TABLE order_users (
    id SERIAL PRIMARY KEY,
    order_id INTEGER,
    user_id INTEGER,
    created TIMESTAMPTZ DEFAULT now(),
    updated TIMESTAMPTZ DEFAULT now()
);

CREATE TABLE orders (
    id SERIAL PRIMARY KEY,
    customer_id INTEGER,
    project_name TEXT,
    sales_order_number TEXT,
    po_number TEXT,
    required_date DATE,
    order_date DATE,
    product TEXT,
    project_manager TEXT,
    static_notes TEXT,
    customer_contact_id INTEGER,
    status TEXT,
    ship_date DATE,
    shipping_type TEXT,
    shipping_contact TEXT,
    shipping_address_1 TEXT,
    shipping_address_2 TEXT,
    shipping_city TEXT,
    shipping_state TEXT,
    shipping_zip TEXT,
    shipping_phone TEXT,
    shipping_email TEXT,
    specifier_id INTEGER,
    factory_id INTEGER,
    created_by_user_id INTEGER,
    order_type TEXT,
    rush_cad BOOLEAN NOT NULL DEFAULT FALSE,
    created_cad_task BOOLEAN DEFAULT FALSE,
    county_id INTEGER,
    not_taxable BOOLEAN NOT NULL DEFAULT FALSE,
    tax_rate FLOAT DEFAULT 0.0,
    billing_contact TEXT,
    billing_address_1 TEXT,
    billing_address_2 TEXT,
    billing_city TEXT,
    billing_state TEXT,
    billing_zip INTEGER,
    billing_phone TEXT,
    billing_email TEXT,
    terms TEXT,
    rush_order BOOLEAN NOT NULL DEFAULT FALSE,
    project_manager_id INTEGER,
    authorized_by_id INTEGER,
    authorized_at TIMESTAMPTZ,
    created TIMESTAMPTZ DEFAULT now(),
    updated TIMESTAMPTZ DEFAULT now(),
    cad_required_date DATE,
    estimate_number INTEGER,
    estimate_status TEXT,
    expiration_email_sent BOOLEAN NOT NULL DEFAULT FALSE,
    quote_date DATE,
    estimated_shipping_cost FLOAT DEFAULT 0.0,
    parent_estimate_id INTEGER,
    fully_shipped BOOLEAN NOT NULL DEFAULT FALSE,
    quote_sent_on TIMESTAMPTZ
);

CREATE TABLE organization_material_types (
    id SERIAL PRIMARY KEY,
    organization_id INTEGER,
    material_type TEXT,
    created TIMESTAMPTZ DEFAULT now(),
    updated TIMESTAMPTZ DEFAULT now(),
    part_types TEXT
);

CREATE TABLE organizations (
    id SERIAL PRIMARY KEY,
    name TEXT,
    created TIMESTAMPTZ DEFAULT now(),
    updated TIMESTAMPTZ DEFAULT now(),
    sales_rep_id INTEGER,
    organization_type TEXT NOT NULL DEFAULT 'customer',
    parent_company_id INTEGER,
    factory_code TEXT,
    terms TEXT,
    vendor_type TEXT,
    address TEXT,
    city TEXT,
    state TEXT,
    zip TEXT,
    priority_type TEXT
);

CREATE TABLE part_receipts (
    id SERIAL PRIMARY KEY,
    quantity INTEGER,
    order_line_part_id INTEGER,
    created TIMESTAMPTZ DEFAULT now(),
    updated TIMESTAMPTZ DEFAULT now(),
    purchase_order_line_id INTEGER,
    created_by_user_id INTEGER,
    received_date DATE,
    bin TEXT,
    quantity_remaining INTEGER,
    note TEXT,
    inventory_receipt_id INTEGER
);

CREATE TABLE payment_lines (
    id SERIAL PRIMARY KEY,
    amount FLOAT DEFAULT 0.0,
    created TIMESTAMPTZ DEFAULT now(),
    updated TIMESTAMPTZ DEFAULT now(),
    payment_id INTEGER,
    payable_id INTEGER,
    payable_type TEXT
);

CREATE TABLE payments (
    id SERIAL PRIMARY KEY,
    order_id INTEGER,
    document_id INTEGER,
    total FLOAT DEFAULT 0.0,
    check_number TEXT,
    date DATE,
    created TIMESTAMPTZ DEFAULT now(),
    updated TIMESTAMPTZ DEFAULT now(),
    payment_type TEXT,
    user_id INTEGER,
    customer_id INTEGER,
    vendor_id INTEGER,
    created_by_user_id INTEGER,
    rep_organization_id INTEGER
);

CREATE TABLE placements (
    id SERIAL PRIMARY KEY,
    name TEXT,
    abbreviation TEXT,
    created TIMESTAMPTZ DEFAULT now(),
    updated TIMESTAMPTZ DEFAULT now()
);

CREATE TABLE process_receipts (
    id SERIAL PRIMARY KEY,
    order_line_process_id INTEGER,
    quantity INTEGER,
    received_by_id INTEGER,
    production_date DATE,
    created TIMESTAMPTZ DEFAULT now(),
    updated TIMESTAMPTZ DEFAULT now(),
    line_number INTEGER
);

CREATE TABLE production_units (
    id SERIAL PRIMARY KEY,
    quantity INTEGER,
    order_line_id INTEGER,
    created TIMESTAMPTZ DEFAULT now(),
    updated TIMESTAMPTZ DEFAULT now(),
    order_line_part_id INTEGER,
    order_id INTEGER
);

CREATE TABLE purchase_order_lines (
    id SERIAL PRIMARY KEY,
    created TIMESTAMPTZ DEFAULT now(),
    updated TIMESTAMPTZ DEFAULT now(),
    quantity INTEGER,
    part_id INTEGER,
    purchase_order_id INTEGER,
    unit_price FLOAT DEFAULT 0.0,
    order_line_id INTEGER,
    due_date DATE,
    lead_time INTEGER,
    notes TEXT
);

CREATE TABLE purchase_orders (
    id SERIAL PRIMARY KEY,
    due_date DATE,
    created_by_user_id INTEGER,
    created TIMESTAMPTZ DEFAULT now(),
    updated TIMESTAMPTZ DEFAULT now(),
    po_number TEXT,
    vendor_id INTEGER,
    order_id INTEGER,
    deposit_percentage FLOAT DEFAULT 0.0,
    approved_by_user_id INTEGER,
    approved_on TIMESTAMPTZ,
    rush_approval BOOLEAN DEFAULT FALSE,
    voided BOOLEAN DEFAULT FALSE,
    ship_to_location_id INTEGER
);

CREATE TABLE receipts (
    id SERIAL PRIMARY KEY,
    fabric_id INTEGER,
    received_on DATE,
    yards FLOAT DEFAULT 0.0,
    received_to TEXT,
    tracking_number TEXT,
    user_id INTEGER,
    freight_company TEXT,
    packing_list_filename TEXT,
    packing_list_content_type TEXT,
    packing_list_size INTEGER,
    received_by_id INTEGER,
    notes TEXT,
    control_number TEXT,
    bin_number INTEGER,
    import_number INTEGER,
    number_of_rolls INTEGER,
    inventory_id INTEGER
);

CREATE TABLE role_authorizations (
    id SERIAL PRIMARY KEY,
    role_id INTEGER,
    authorization_id INTEGER,
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

CREATE TABLE scanned_items (
    id SERIAL PRIMARY KEY,
    order_id INTEGER,
    process_id INTEGER,
    user_id INTEGER,
    scan_time TIMESTAMPTZ,
    scanned_value TEXT,
    created TIMESTAMPTZ DEFAULT now(),
    updated TIMESTAMPTZ DEFAULT now(),
    production_unit_id INTEGER
);

CREATE TABLE shekelators (
    id SERIAL PRIMARY KEY,
    tax_year_id INTEGER,
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

CREATE TABLE shipment_lines (
    id SERIAL PRIMARY KEY,
    shipment_id INTEGER,
    order_line_id INTEGER,
    quantity INTEGER NOT NULL DEFAULT 0,
    weight FLOAT,
    notes TEXT,
    nmfc TEXT,
    order_line_class TEXT,
    created TIMESTAMPTZ DEFAULT now(),
    updated TIMESTAMPTZ DEFAULT now(),
    item_type TEXT,
    packaging TEXT,
    number_in_pack INTEGER,
    factory_quantity_shipped INTEGER
);

CREATE TABLE shipments (
    id SERIAL PRIMARY KEY,
    order_id INTEGER,
    tracking_number TEXT,
    freight_company TEXT,
    trailer_number TEXT,
    seal_number TEXT,
    instructions TEXT,
    shipment_date DATE,
    loaded_by_driver BOOLEAN NOT NULL DEFAULT FALSE,
    freight_counted_by_driver BOOLEAN NOT NULL DEFAULT FALSE,
    created TIMESTAMPTZ DEFAULT now(),
    updated TIMESTAMPTZ DEFAULT now(),
    shipping_bol_filename TEXT,
    shipping_bol_content_type TEXT,
    shipping_bol_size TEXT,
    location_id INTEGER,
    export_invoice_number TEXT,
    factory_shipment BOOLEAN NOT NULL DEFAULT FALSE,
    ship_to_location_id INTEGER,
    quoted_cost FLOAT,
    actual_cost FLOAT,
    client_cost FLOAT,
    taxable BOOLEAN NOT NULL DEFAULT FALSE,
    broker TEXT,
    invoiced BOOLEAN NOT NULL DEFAULT FALSE,
    invoice_number TEXT,
    invoiced_date DATE,
    shipped BOOLEAN NOT NULL DEFAULT FALSE,
    shipping_type TEXT,
    po_number TEXT
);

CREATE TABLE shipping_setup_lines (
    id SERIAL PRIMARY KEY,
    shipping_setup_id INTEGER,
    order_line_id INTEGER,
    quantity_to_ship INTEGER,
    production_date DATE
);

CREATE TABLE shipping_setups (
    id SERIAL PRIMARY KEY,
    order_id INTEGER,
    shipping_date DATE,
    ship_everything BOOLEAN NOT NULL DEFAULT FALSE,
    factory_id INTEGER
);

CREATE TABLE smartview_lines (
    id SERIAL PRIMARY KEY,
    created TIMESTAMPTZ DEFAULT now(),
    updated TIMESTAMPTZ DEFAULT now(),
    smartview_id INTEGER,
    group_number INTEGER,
    class_to_join TEXT,
    field_to_search TEXT,
    operator TEXT,
    search_value TEXT
);

CREATE TABLE smartviews (
    id SERIAL PRIMARY KEY,
    created TIMESTAMPTZ DEFAULT now(),
    updated TIMESTAMPTZ DEFAULT now(),
    user_id INTEGER,
    name TEXT,
    sort_number INTEGER,
    archived BOOLEAN NOT NULL DEFAULT FALSE,
    client_count INTEGER
);

CREATE TABLE standard_finishes (
    id SERIAL PRIMARY KEY,
    file_filename TEXT,
    file_content_type TEXT,
    file_size INTEGER,
    name TEXT
);

CREATE TABLE status_histories (
    id SERIAL PRIMARY KEY,
    created TIMESTAMPTZ DEFAULT now(),
    updated TIMESTAMPTZ DEFAULT now(),
    client_id INTEGER,
    old_status_id INTEGER,
    new_status_id INTEGER
);

CREATE TABLE tax_brackets (
    id SERIAL PRIMARY KEY,
    year_detail_id INTEGER,
    bracket_type TEXT,
    income_amount INTEGER,
    rate INTEGER
);

CREATE TABLE tax_floors (
    id SERIAL PRIMARY KEY,
    year_detail_id INTEGER,
    floor FLOAT,
    apply_percentage FLOAT,
    filing_type TEXT
);

CREATE TABLE tax_personals (
    id SERIAL PRIMARY KEY,
    archived BOOLEAN NOT NULL DEFAULT FALSE,
    client_id INTEGER,
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

CREATE TABLE tax_years (
    id SERIAL PRIMARY KEY,
    year_detail_id INTEGER,
    client_id INTEGER,
    shekelator_total FLOAT,
    archived BOOLEAN NOT NULL DEFAULT FALSE,
    year_name TEXT,
    fm_id TEXT,
    irs_history BOOLEAN NOT NULL DEFAULT FALSE,
    last_year_name TEXT
);

CREATE TABLE textees (
    id SERIAL PRIMARY KEY,
    inactive BOOLEAN NOT NULL DEFAULT FALSE,
    phone_number TEXT
);

CREATE TABLE time_slips (
    id SERIAL PRIMARY KEY,
    created TIMESTAMPTZ DEFAULT now(),
    updated TIMESTAMPTZ DEFAULT now(),
    user_id INTEGER,
    time_in TIMESTAMPTZ,
    time_out TIMESTAMPTZ,
    memo TEXT,
    seconds_in_shift INTEGER,
    all_ot BOOLEAN NOT NULL DEFAULT FALSE
);

CREATE TABLE user_notifications (
    id SERIAL PRIMARY KEY,
    user_id INTEGER,
    notification_id INTEGER,
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
    created TIMESTAMPTZ DEFAULT now(),
    updated TIMESTAMPTZ DEFAULT now(),
    allow_texting BOOLEAN NOT NULL DEFAULT FALSE,
    send_login_notifications BOOLEAN NOT NULL DEFAULT FALSE,
    notify_of_logins BOOLEAN NOT NULL DEFAULT FALSE,
    seconds_in_day INTEGER,
    selectable BOOLEAN NOT NULL DEFAULT FALSE,
    login TEXT,
    edit_time_slips BOOLEAN NOT NULL DEFAULT FALSE
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
    year_detail_id INTEGER
);

CREATE TABLE year_details (
    id SERIAL PRIMARY KEY,
    created TIMESTAMPTZ DEFAULT now(),
    updated TIMESTAMPTZ DEFAULT now(),
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
    show BOOLEAN NOT NULL DEFAULT TRUE
);

