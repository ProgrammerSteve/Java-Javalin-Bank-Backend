### Setting up connection to postgresql database
- Navigate to directory: `src/main/resources`
- create a file called: `db.properties`
- Should follow the format:
```
jdbc.url=jdbc:postgresql://XXXXXXX/XXXXXXX
jdbc.username=XXXXXXX
jdbc.password=XXXXXXX
```

### Setting up the secret key for jwt
- Navigate to directory: `src/main/resources`
- create a file called: `application.properties`
- Should follow the format:
```
secret.key=XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
```
- Secret key should be long enough to be secure


### Setting up tables on database
- There is a one-to-many relationship between two tables: `users`, `accounts`
```
CREATE TABLE IF NOT EXISTS users (
    user_id SERIAL PRIMARY KEY,
    username VARCHAR(50) UNIQUE,
    password VARCHAR(50)
);

CREATE TABLE IF NOT EXISTS accounts (
    account_id SERIAL PRIMARY KEY,
    user_id INT UNIQUE,
    balance DECIMAL(10, 2) CHECK (balance >= 0),
    account_type VARCHAR(20) NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE
);
```
### Entity Relationship Diagram for this Backend
![](src/main/resources/java-bank-erd.png)

## Endpoints for users:
<hr/>

#### `/get_accounts_by_user_id` `GET` `Requires Jwt`
- Uses jwt payload to get user_id
- returns a list of accounts for the user
#### `/get_all_users` `GET`
- returns all users
#### `/register/{accountType}` `POST`
- path param for accountType takes in a String for the type of account wanted: "Checking", "Savings", "Roth IRA", etc. Would probably be an enum if I continued working on this.
- request body is in the format: **username (String)**, **password (String)**
`{"username":"XXXXXX","password":"XXXXXX"}`
<hr/>

## Endpoints for accounts:
<hr/>

#### `/view_balance` `POST` `Requires Jwt`
- request body is in the format: **accountId (Integer)**
  `{"accountId":"XXXXXX"}`
#### `/withdraw` `POST` `Requires Jwt`
- request body is in the format: **accountId (Integer)**, **amount (BigDecimal)**
  `{"accountId":"XXXXXX","amount":"XXXXXX"}`
#### `/deposit` `POST` `Requires Jwt`
- request body is in the format: **accountId (Integer)**, **amount (BigDecimal)**
  `{"accountId":"XXXXXX","amount":"XXXXXX"}`
<hr/>

## Endpoints for auth:
<hr/>

#### `/login` `POST`
- request body is in the format: **username (String)**, **password (String)**
  `{"username":"XXXXXX","password":"XXXXXX"}`

