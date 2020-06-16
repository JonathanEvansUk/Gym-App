create table exercise_activity_entity (id bigint not null auto_increment, notes varchar(255), exercise_id bigint, workout_id bigint, primary key (id)) engine=InnoDB;
create table exercise_entity (id bigint not null auto_increment, information varchar(255), muscle_group varchar(255), name varchar(255), primary key (id)) engine=InnoDB;
create table exercise_set_entity (set_type varchar(31) not null, id bigint not null auto_increment, number_of_reps integer, status integer, weight varchar(255), weight_kg double precision, exercise_activity_id bigint, primary key (id)) engine=InnoDB;
create table workout_entity (id bigint not null auto_increment, notes varchar(255), performed_at_timestamp_utc datetime, workout_type varchar(255), primary key (id)) engine=InnoDB;
alter table exercise_activity_entity add constraint FKmgvj0h3k1ek7iby9enpn2c0ml foreign key (exercise_id) references exercise_entity (id);
alter table exercise_activity_entity add constraint FKs63e7dy833l3jv5v7b9fi7bbt foreign key (workout_id) references workout_entity (id);
alter table exercise_set_entity add constraint FKmiyrjomq9r21jsnlue0o3a670 foreign key (exercise_activity_id) references exercise_activity_entity (id);
create table exercise_activity_entity (id bigint not null auto_increment, notes varchar(255), exercise_id bigint, workout_id bigint, primary key (id)) engine=InnoDB
create table exercise_entity (id bigint not null auto_increment, information varchar(255), muscle_group varchar(255), name varchar(255), primary key (id)) engine=InnoDB
create table exercise_set_entity (set_type varchar(31) not null, id bigint not null auto_increment, number_of_reps integer, status integer, weight varchar(255), weight_kg double precision, exercise_activity_id bigint, primary key (id)) engine=InnoDB
create table workout_entity (id bigint not null auto_increment, notes varchar(255), performed_at_timestamp_utc datetime, workout_type varchar(255), primary key (id)) engine=InnoDB
alter table exercise_activity_entity add constraint FKmgvj0h3k1ek7iby9enpn2c0ml foreign key (exercise_id) references exercise_entity (id)
alter table exercise_activity_entity add constraint FKs63e7dy833l3jv5v7b9fi7bbt foreign key (workout_id) references workout_entity (id)
alter table exercise_set_entity add constraint FKmiyrjomq9r21jsnlue0o3a670 foreign key (exercise_activity_id) references exercise_activity_entity (id)
create table exercise_activity_entity (id bigint not null auto_increment, notes varchar(255), exercise_id bigint, workout_id bigint, primary key (id)) engine=InnoDB
create table exercise_entity (id bigint not null auto_increment, information varchar(255), muscle_group varchar(255), name varchar(255), primary key (id)) engine=InnoDB
create table exercise_set_entity (set_type varchar(31) not null, id bigint not null auto_increment, number_of_reps integer, status integer, weight varchar(255), weight_kg double precision, exercise_activity_id bigint, primary key (id)) engine=InnoDB
create table workout_entity (id bigint not null auto_increment, notes varchar(255), performed_at_timestamp_utc datetime, workout_type varchar(255), primary key (id)) engine=InnoDB
alter table exercise_activity_entity add constraint FKmgvj0h3k1ek7iby9enpn2c0ml foreign key (exercise_id) references exercise_entity (id)
alter table exercise_activity_entity add constraint FKs63e7dy833l3jv5v7b9fi7bbt foreign key (workout_id) references workout_entity (id)
alter table exercise_set_entity add constraint FKmiyrjomq9r21jsnlue0o3a670 foreign key (exercise_activity_id) references exercise_activity_entity (id)
create table exercise_activity_entity (id bigint not null auto_increment, notes varchar(255), exercise_id bigint, workout_id bigint, primary key (id)) engine=InnoDB
create table exercise_entity (id bigint not null auto_increment, information varchar(255), muscle_group varchar(255), name varchar(255), primary key (id)) engine=InnoDB
create table exercise_set_entity (set_type varchar(31) not null, id bigint not null auto_increment, number_of_reps integer, status integer, weight varchar(255), weight_kg double precision, exercise_activity_id bigint, primary key (id)) engine=InnoDB
create table workout_entity (id bigint not null auto_increment, notes varchar(255), performed_at_timestamp_utc datetime, workout_type varchar(255), primary key (id)) engine=InnoDB
alter table exercise_activity_entity add constraint FKmgvj0h3k1ek7iby9enpn2c0ml foreign key (exercise_id) references exercise_entity (id)
alter table exercise_activity_entity add constraint FKs63e7dy833l3jv5v7b9fi7bbt foreign key (workout_id) references workout_entity (id)
alter table exercise_set_entity add constraint FKmiyrjomq9r21jsnlue0o3a670 foreign key (exercise_activity_id) references exercise_activity_entity (id)
create table exercise_activity_entity (id bigint not null auto_increment, notes varchar(255), exercise_id bigint, workout_id bigint, primary key (id)) engine=InnoDB
create table exercise_entity (id bigint not null auto_increment, information varchar(255), muscle_group varchar(255), name varchar(255), primary key (id)) engine=InnoDB
create table exercise_set_entity (set_type varchar(31) not null, id bigint not null auto_increment, number_of_reps integer, status integer, weight varchar(255), weight_kg double precision, exercise_activity_id bigint, primary key (id)) engine=InnoDB
create table workout_entity (id bigint not null auto_increment, notes varchar(255), performed_at_timestamp_utc datetime, workout_type varchar(255), primary key (id)) engine=InnoDB
alter table exercise_activity_entity add constraint FKmgvj0h3k1ek7iby9enpn2c0ml foreign key (exercise_id) references exercise_entity (id)
alter table exercise_activity_entity add constraint FKs63e7dy833l3jv5v7b9fi7bbt foreign key (workout_id) references workout_entity (id)
alter table exercise_set_entity add constraint FKmiyrjomq9r21jsnlue0o3a670 foreign key (exercise_activity_id) references exercise_activity_entity (id)
create table exercise_activity_entity (id bigint not null auto_increment, notes varchar(255), exercise_id bigint, workout_id bigint, primary key (id)) engine=InnoDB;
create table exercise_entity (id bigint not null auto_increment, information varchar(255), muscle_group varchar(255), name varchar(255), primary key (id)) engine=InnoDB;
create table exercise_set_entity (set_type varchar(31) not null, id bigint not null auto_increment, number_of_reps integer, status integer, weight varchar(255), weight_kg double precision, exercise_activity_id bigint, primary key (id)) engine=InnoDB;
create table workout_entity (id bigint not null auto_increment, notes varchar(255), performed_at_timestamp_utc datetime, workout_type varchar(255), primary key (id)) engine=InnoDB;
alter table exercise_activity_entity add constraint FKmgvj0h3k1ek7iby9enpn2c0ml foreign key (exercise_id) references exercise_entity (id);
alter table exercise_activity_entity add constraint FKs63e7dy833l3jv5v7b9fi7bbt foreign key (workout_id) references workout_entity (id);
alter table exercise_set_entity add constraint FKmiyrjomq9r21jsnlue0o3a670 foreign key (exercise_activity_id) references exercise_activity_entity (id);
