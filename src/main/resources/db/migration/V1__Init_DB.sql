create table if not exists clients
(
    id bigserial not null
        constraint clients_pkey
            primary key,
    first_name varchar(50) not null,
    last_name varchar(50) not null,
    date_of_birth date not null,
    occupation varchar(30) not null,
    email varchar(255) not null,
    phone_number varchar(50) not null,
    password varchar(255) not null,
    personal_data_process_agreement boolean not null,
    passport_id varchar(255) not null
        constraint clients_passport_id_uindex
            unique
);

alter table clients owner to postgres;

create unique index if not exists clients_email_uindex
    on clients (email);

create table if not exists visa_applications
(
    id bigserial not null
        constraint visa_applications_pkey
            primary key,
    application_status varchar(20) not null,
    appointment_date date not null,
    appointment_time varchar(10) not null,
    city varchar(30) not null,
    required_visa_type char not null,
    client_id bigint not null
        constraint visa_applications_clients_id_fk
            references clients
);

alter table visa_applications owner to postgres;

create table if not exists client_visas
(
    id bigserial not null
        constraint client_visas_pkey
            primary key,
    expiry_date date not null,
    issue_date date not null,
    visa_num varchar(50) not null,
    visa_type char not null,
    client_id bigint not null
        constraint client_visas_clients_id_fk
            references clients
);

alter table client_visas owner to postgres;

create table if not exists applications_status_history
(
    id bigserial not null
        constraint applications_status_history_pkey
            primary key,
    application_status varchar(30) not null,
    operator_id bigint not null,
    setting_date date not null,
    application_id bigint not null
        constraint applications_status_history_visa_applications_id_fk
            references visa_applications
);

alter table applications_status_history owner to postgres;

create table if not exists visa_documents
(
    id bigserial not null
        constraint visa_documents_pkey
            primary key,
    document_description varchar(255) not null,
    client_occupation varchar(30) not null,
    visa_type char not null
);

alter table visa_documents owner to postgres;

create table if not exists employees
(
    id bigserial not null
        constraint employees_pkey
            primary key,
    first_name varchar(50) not null,
    last_name varchar(50) not null,
    password varchar(255) not null,
    phone_number varchar(50) not null,
    city varchar(30) not null,
    position varchar(30) not null,
    email varchar default 255 not null
);

alter table employees owner to postgres;

create unique index if not exists employees_email_uindex
    on employees (email);