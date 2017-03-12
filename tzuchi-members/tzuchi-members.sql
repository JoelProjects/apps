
    create table account_info (
        username varchar(45) not null,
        password varchar(45),
        member_id bigint,
        primary key (username)
    ) ENGINE=InnoDB;

    create table account_role (
        username varchar(45) not null,
        account_role_id integer not null,
        primary key (username, account_role_id)
    ) ENGINE=InnoDB;

    create table account_role_info (
        account_role_id integer not null auto_increment,
        account_role_name varchar(20),
        primary key (account_role_id)
    ) ENGINE=InnoDB;

    create table member_info (
        member_id bigint not null,
        first_name varchar(45),
        last_name varchar(45),
        c_first_name varchar(45),
        c_last_name varchar(45),
        home_phone varchar(20),
        cell_phone varchar(20),
        work_phone varchar(20),
        email varchar(45),
        primary key (member_id)
    ) ENGINE=InnoDB;

    alter table account_info 
        add index FK_account_info_1 (member_id), 
        add constraint FK_account_info_1 
        foreign key (member_id) 
        references member_info (member_id);

    alter table account_role 
        add index FK410D0348D7F8946D (username), 
        add constraint FK410D0348D7F8946D 
        foreign key (username) 
        references account_info (username);

    alter table account_role 
        add index FK410D0348FA97821F (account_role_id), 
        add constraint FK410D0348FA97821F 
        foreign key (account_role_id) 
        references account_role_info (account_role_id);
