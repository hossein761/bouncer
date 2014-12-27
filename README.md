#Description
This is a RESTfull authorization and authentication library for Play! Framework 2 in Java. It is geared towards SPAs(single page applications) and mobile clients. 

The authorization part replicates the OAuth 2.0 flow.

[![Build Status](https://travis-ci.org/hossein761/bouncer.svg?branch=master)](https://travis-ci.org/hossein761/bouncer)


#Requirements
At least Play 2.3.x

#Installation
##Dependency
Add:

```"com.github.hossein761" %% "bouncer" % "1.0.1"```

to the list of dependencies.
##Database changes
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

The fields that are mentioned as "not null" are field in during registration process. The ```user``` table will be the main

table to save user information and the ```registration``` table holds a temporary token ('id') to differentiate if a user 

has confirmed the registration process (confirming his email) or not.

##Routs
Add ```->         /auth                	bouncer.Routes``` to your routs file. This will direct any requests to with the 
prefix ```/auth``` to one of the corresponding endpoints, as below:

```
POST        /login              
GET			/logout				
GET         /accessToken        
POST        /signUp             
GET         /signUpConfirm      
```

##Configurations
### Auth configurations
As the ```salt``` and ```iterations``` are saved in the database, the values below can change without breaking existing hashes:

You can find the algorithm and the descriptions here: http://crackstation.net/hashing-security.htm

```
auth {
    salt.byteSize=24
    hash.byteSize=24
    pbkdf2.iterations=1000
    authCode.expiry=3600 # 1 hour
    accessToken.expiry=1209600 # 2 weeks
    registrationToken.expiry=3600
}
```

### Email configurations
Email is sent for registration confirmation and password reset(not yet implemented).
```
smtp {
	host=localhost
	port=9999
	ssl=false
	user=USERNAME
	password=your_password
	from="your_email"
}
```

#Flow
On the bigger picture, Bouncer has two separate flows. One for ``Registration`` and the other for ``Login``.
## Registration Flow
TODO:
## Login Flow
TODO:


#Example javascript client:
TODO:

