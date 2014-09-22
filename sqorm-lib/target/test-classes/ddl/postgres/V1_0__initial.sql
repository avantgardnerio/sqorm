-- Customer
CREATE TABLE customer (
  customer_id integer NOT NULL,
  name character varying NOT NULL,
  version integer DEFAULT 1 NOT NULL
);
ALTER TABLE ONLY customer
ADD CONSTRAINT customer_pk PRIMARY KEY (customer_id);

-- Order
CREATE TABLE "Orders" (
  "OrderId" integer NOT NULL,
  customer_id integer NOT NULL,
  version integer NOT NULL
);
ALTER TABLE ONLY "Orders"
ADD CONSTRAINT orders_pk PRIMARY KEY ("OrderId");

-- OrderDetails
CREATE TABLE "OrderDetails" (
  "OrderDetailsId" uuid NOT NULL,
  "OrderId" integer NOT NULL,
  item_name character varying NOT NULL,
  version integer NOT NULL
);
ALTER TABLE ONLY "OrderDetails"
ADD CONSTRAINT order_details_pk PRIMARY KEY ("OrderDetailsId");

-- Foreign keys
ALTER TABLE ONLY customer
ADD CONSTRAINT customer_order_fk FOREIGN KEY (customer_id) REFERENCES customer(customer_id) DEFERRABLE INITIALLY DEFERRED;

ALTER TABLE ONLY "OrderDetails"
ADD CONSTRAINT "OrderDetails_OrderId_fkey" FOREIGN KEY ("OrderId") REFERENCES "Orders"("OrderId") DEFERRABLE INITIALLY DEFERRED;
