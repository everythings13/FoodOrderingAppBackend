DROP TABLE IF EXISTS customer CASCADE;

CREATE TABLE customer
  (
     id             SERIAL,
     uuid           VARCHAR(200) UNIQUE NOT NULL,
     firstname      VARCHAR(30) NOT NULL,
     lastname       VARCHAR(30),
     email          VARCHAR(50),
     contact_number VARCHAR(30) UNIQUE NOT NULL,
     password       VARCHAR(255) NOT NULL,
     salt           VARCHAR(255) NOT NULL,
     PRIMARY KEY(id)
  );

DROP TABLE IF EXISTS category CASCADE;

CREATE TABLE category
  (
     id            SERIAL,
     uuid          VARCHAR(200) UNIQUE NOT NULL,
     category_name VARCHAR(255),
     PRIMARY KEY (id)
  );

DROP TABLE IF EXISTS coupon CASCADE;

CREATE TABLE coupon
  (
     id          SERIAL,
     uuid        VARCHAR(200) UNIQUE NOT NULL,
     coupon_name VARCHAR(255),
     percent     INTEGER NOT NULL,
     PRIMARY KEY (id)
  );

DROP TABLE IF EXISTS payment CASCADE;

CREATE TABLE payment
  (
     id           SERIAL,
     uuid         VARCHAR(200) UNIQUE NOT NULL,
     payment_name VARCHAR(255),
     PRIMARY KEY (id)
  );

DROP TABLE IF EXISTS state CASCADE;

CREATE TABLE state
  (
     id         SERIAL,
     uuid       VARCHAR(200) UNIQUE NOT NULL,
     state_name VARCHAR(30),
     PRIMARY KEY (id)
  );

DROP TABLE IF EXISTS address CASCADE;

CREATE TABLE address
  (
     id               SERIAL,
     uuid             VARCHAR(200) UNIQUE NOT NULL,
     flat_buil_number VARCHAR(255),
     locality         VARCHAR(255),
     city             VARCHAR(30),
     pincode          VARCHAR(30),
     state_id         INTEGER,
     active           INTEGER DEFAULT(1),
     PRIMARY KEY (id),
     FOREIGN KEY (state_id) REFERENCES state(id) ON DELETE CASCADE
  );

DROP TABLE IF EXISTS customer_address CASCADE;

CREATE TABLE customer_address
  (
     id          SERIAL,
     customer_id INTEGER NOT NULL,
     address_id  INTEGER NOT NULL,
     PRIMARY KEY (id),
     FOREIGN KEY (customer_id) REFERENCES customer(id) ON DELETE CASCADE,
     FOREIGN KEY (address_id) REFERENCES address(id) ON DELETE CASCADE
  );

DROP TABLE IF EXISTS item CASCADE;

CREATE TABLE item
  (
     id        SERIAL,
     uuid      VARCHAR(200) UNIQUE NOT NULL,
     item_name VARCHAR(30) NOT NULL,
     price     INTEGER NOT NULL,
     type      VARCHAR(10) NOT NULL,
     PRIMARY KEY (id)
  );

DROP TABLE IF EXISTS restaurant CASCADE;

CREATE TABLE restaurant
  (
     id                        SERIAL,
     uuid                      VARCHAR(200) UNIQUE NOT NULL,
     restaurant_name           VARCHAR(50) NOT NULL,
     photo_url                 VARCHAR(255),
     customer_rating           DECIMAL NOT NULL,
     average_price_for_two     INTEGER NOT NULL,
     number_of_customers_rated INTEGER NOT NULL DEFAULT 0,
     address_id                INTEGER NOT NULL,
     PRIMARY KEY(id),
     FOREIGN KEY (address_id) REFERENCES address(id) ON DELETE CASCADE
  );

DROP TABLE IF EXISTS orders CASCADE;

CREATE TABLE orders
  (
     id            SERIAL,
     uuid          VARCHAR(200) UNIQUE NOT NULL,
     bill          DECIMAL NOT NULL,
     coupon_id     INTEGER,
     discount      DECIMAL DEFAULT 0,
     date          TIMESTAMP NOT NULL,
     payment_id    INTEGER,
     customer_id   INTEGER NOT NULL,
     address_id    INTEGER NOT NULL,
          PRIMARY KEY(id),
          restaurant_id INTEGER NOT NULL,
     FOREIGN KEY (payment_id) REFERENCES payment(id),
     FOREIGN KEY (restaurant_id) REFERENCES restaurant(id),
     FOREIGN KEY (customer_id) REFERENCES customer(id) ON DELETE CASCADE,
     FOREIGN KEY (address_id) REFERENCES address(id),
     FOREIGN KEY (coupon_id) REFERENCES coupon(id)
  );

DROP TABLE IF EXISTS category_item CASCADE;

CREATE TABLE category_item
  (
     id          SERIAL,
     item_id     INTEGER NOT NULL,
     category_id INTEGER NOT NULL,
     PRIMARY KEY (id),
     FOREIGN KEY (item_id) REFERENCES item(id) ON DELETE CASCADE,
     FOREIGN KEY (category_id) REFERENCES category(id) ON DELETE CASCADE
  );

DROP TABLE IF EXISTS restaurant_item CASCADE;

CREATE TABLE restaurant_item
  (
     id            SERIAL,
     item_id       INTEGER NOT NULL,
     restaurant_id INTEGER NOT NULL,
     PRIMARY KEY (id),
     FOREIGN KEY (item_id) REFERENCES item(id) ON DELETE CASCADE,
     FOREIGN KEY (restaurant_id) REFERENCES restaurant(id) ON DELETE CASCADE
  );

DROP TABLE IF EXISTS order_item CASCADE;

CREATE TABLE order_item
  (
     id       SERIAL,
     order_id INTEGER NOT NULL,
     item_id  INTEGER NOT NULL,
     quantity INTEGER NOT NULL,
     price    INTEGER NOT NULL,
     PRIMARY KEY (id),
     FOREIGN KEY (item_id) REFERENCES item(id),
     FOREIGN KEY (order_id) REFERENCES orders(id) ON DELETE CASCADE
  );

DROP TABLE IF EXISTS restaurant_category CASCADE;

CREATE TABLE restaurant_category
  (
     id            SERIAL,
     restaurant_id INTEGER NOT NULL,
     category_id   INTEGER NOT NULL,
     PRIMARY KEY (id),
     FOREIGN KEY (category_id) REFERENCES category(id) ON DELETE CASCADE,
     FOREIGN KEY (restaurant_id) REFERENCES restaurant(id) ON DELETE CASCADE
  );

DROP TABLE IF EXISTS customer_auth CASCADE;

CREATE TABLE customer_auth
  (
     id           SERIAL,
     uuid         VARCHAR(200) UNIQUE NOT NULL,
     customer_id  INTEGER NOT NULL,
     access_token VARCHAR(500),
     login_at     TIMESTAMP,
     logout_at    TIMESTAMP,
     expires_at   TIMESTAMP,
     PRIMARY KEY (id),
     FOREIGN KEY (customer_id) REFERENCES customer(id) ON DELETE CASCADE
  );