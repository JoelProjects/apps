
    create table invoice (
        invoice_id integer not null auto_increment,
        invoice_no varchar(20),
        handled_by varchar(25),
        purchased_by varchar(50),
        invoice_date datetime,
        total double precision,
        primary key (invoice_id)
    ) ENGINE=InnoDB;

    create table invoice_item (
        invoice_item_id integer not null auto_increment,
        quantity integer,
        price double precision,
        total double precision,
        invoice_id integer,
        item_id integer,
        primary key (invoice_item_id)
    ) ENGINE=InnoDB;

    create table item_history (
        hist_id integer not null auto_increment,
        modified_by varchar(25),
        mod_date datetime,
        updated_quantity integer,
        invoice_number varchar(20),
        invoice_date datetime,
        item_id integer,
        primary key (hist_id)
    ) ENGINE=InnoDB;

    create table item_info (
        item_id integer not null auto_increment,
        item_no varchar(10),
        barcode varchar(15),
        name varchar(50),
        price double precision,
        quantity integer,
        type_id integer,
        primary key (item_id)
    ) ENGINE=InnoDB;

    create table item_type_info (
        type_id integer not null auto_increment,
        type_name varchar(20),
        primary key (type_id)
    ) ENGINE=InnoDB;

    alter table invoice_item 
        add index FK_invoice_item_2 (item_id), 
        add constraint FK_invoice_item_2 
        foreign key (item_id) 
        references item_info (item_id);

    alter table invoice_item 
        add index FK_invoice_item_1 (invoice_id), 
        add constraint FK_invoice_item_1 
        foreign key (invoice_id) 
        references invoice (invoice_id);

    alter table item_history 
        add index FK_item_history_1 (item_id), 
        add constraint FK_item_history_1 
        foreign key (item_id) 
        references item_info (item_id);

    alter table item_info 
        add index FK_item_info_1 (type_id), 
        add constraint FK_item_info_1 
        foreign key (type_id) 
        references item_type_info (type_id);
