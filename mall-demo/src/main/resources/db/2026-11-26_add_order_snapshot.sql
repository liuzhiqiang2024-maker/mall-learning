-- 订单快照：下单时固化商品名与单价，避免商品调价污染历史订单
ALTER TABLE orders
    ADD COLUMN product_name varchar(100) NULL AFTER product_id,
  ADD COLUMN unit_price   decimal(10,2) NULL AFTER product_name;

-- 存量回填：只能用当前商品信息补，历史真实单价不可追回
UPDATE orders o
    JOIN product p ON o.product_id = p.id
    SET o.product_name = p.product_name,
        o.unit_price   = p.price;
