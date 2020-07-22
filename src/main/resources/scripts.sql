insert into app_country(name, is_active) values ('Thailand', true);
insert into app_country(name, is_active) values ('France', true);
insert into app_country(name, is_active) values ('USA', true);

insert into app_city(name, is_active, country_id) values ('Bangkok', true, 1);
insert into app_city(name, is_active, country_id) values ('Chiang Mai', true, 1);
insert into app_city(name, is_active, country_id) values ('Paris', true, 2);
insert into app_city(name, is_active, country_id) values ('Bordeaux', true, 2);
insert into app_city(name, is_active, country_id) values ('Los Angeles', true, 3);
insert into app_city(name, is_active, country_id) values ('Chicago', true, 3);

INSERT INTO app_user (
	birth_date,
	email,
	user_is_email_confirmed,
	first_name,
	gender,
	last_name,
	login,
	is_married,	
	password,
	salary,
	city_id,
	country_id
) VALUES (
	'1981-08-14',
	'test@test.test',
	true,
	'Manu',
	'M',
	'Mura',
	'manu',
	false,
	'$2y$10$IznbfyU/juNaMyLsqYEZJ.Q0wNHo1eubFktZKw9sBTU2R8HyRixTi', -- test1234
	120,
	1,
	1
);

drop table if exists oauth_client_details;
create table oauth_client_details (
  client_id VARCHAR(255) PRIMARY KEY,
  resource_ids VARCHAR(255),
  client_secret VARCHAR(255),
  scope VARCHAR(255),
  authorized_grant_types VARCHAR(255),
  web_server_redirect_uri VARCHAR(255),
  authorities VARCHAR(255),
  access_token_validity INTEGER,
  refresh_token_validity INTEGER,
  additional_information VARCHAR(4096),
  autoapprove VARCHAR(255)
);
 
drop table if exists oauth_client_token;
create table oauth_client_token (
  token_id VARCHAR(255),
  -- token LONG VARBINARY,
  token BYTEA, 
  authentication_id VARCHAR(255) PRIMARY KEY,
  user_name VARCHAR(255),
  client_id VARCHAR(255)
);
 
drop table if exists oauth_access_token;
create table oauth_access_token (
  token_id VARCHAR(255),
  token BYTEA, -- LONG VARBINARY,
  authentication_id VARCHAR(255) PRIMARY KEY,
  user_name VARCHAR(255),
  client_id VARCHAR(255),
  authentication BYTEA, --LONG VARBINARY,
  refresh_token VARCHAR(255)
);
 
drop table if exists oauth_refresh_token;
create table oauth_refresh_token (
  token_id VARCHAR(255),
  token BYTEA, --LONG VARBINARY,
  authentication BYTEA --LONG VARBINARY
);
 
drop table if exists oauth_code;
create table oauth_code (
  code VARCHAR(255), 
  authentication BYTEA --LONG VARBINARY
);
 
drop table if exists oauth_approvals;
create table oauth_approvals (
    userId VARCHAR(255),
    clientId VARCHAR(255),
    scope VARCHAR(255),
    status VARCHAR(10),
    expiresAt TIMESTAMP,
    lastModifiedAt TIMESTAMP
);
 
drop table if exists ClientDetails;
create table ClientDetails (
  appId VARCHAR(255) PRIMARY KEY,
  resourceIds VARCHAR(255),
  appSecret VARCHAR(255),
  scope VARCHAR(255),
  grantTypes VARCHAR(255),
  redirectUrl VARCHAR(255),
  authorities VARCHAR(255),
  access_token_validity INTEGER,
  refresh_token_validity INTEGER,
  additionalInformation VARCHAR(4096),
  autoApproveScopes VARCHAR(255)
);

insert into oauth_client_details (
  client_id,
  client_secret,
  scope,
  authorized_grant_types,
  refresh_token_validity,
  additional_information
) values (
  'app-device-mgmt',
  '$2a$10$FcvbnR1bdRYbjU1IB6ou3ewQvzo42iIOaYfU/ODRSTl.mJDuQfbd.', -- secret
  'read,write',
  'password,refresh_token',
  86400,
  '{}'
);