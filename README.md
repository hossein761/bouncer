#Warning!: This project is still in progress and might contain bugs.
#Description
This is a RESTfull authorization and authentication library for Play! Framework 2 in Java. It is geared towards SPAs(single page applications) and mobile clients. 

The authorization part replicates the OAuth 2.0 flow.
 

#Requirements
At least Play 2.3.x

#Installation
Add:

```"nl.bouncer" %% "bouncer" % "1.0-SNAPSHOT"```

to the list of dependencies.

Running the project with this dependency will create two new tables called ```user``` and ```registration``` in your database with the given columns:

```sql
table registration (
  id                        varchar(255) not null,
  user_id                   varchar(255),
)
table user (
  id                        varchar(255) not null,
  first_name                varchar(255) not null,
  last_name                 varchar(255) not null,
  full_name                 varchar(255),
  email                     varchar(255) not null,
  user_name                 varchar(255) not null,
  phone_number              varchar(255),
  address                   varchar(255),
  postal_code               varchar(255),
  city                      varchar(255),
  country                   varchar(255),
  geo_location              varchar(255),
  about                     varchar(255),
  profile_img_url           varchar(255),
  password_hash             varchar(255) not null,
  salt                      varchar(255) not null,
  iterations                integer not null,
  last_login_time           timestamp,
  status                    integer not null,
  created_time              timestamp not null,
  updated_time              timestamp not null
  )
```

#TODO: complete
