# Entity to SQL

IDEA 插件：根据 java 实体类生成建表语句，目前仅生成Mysql的建表语句。<br/>
有其它需求在 issue 提出，会尽快添加对应功能。<br/>

## annotate inputs

| name             | description                                                                                                                                                                                                                                                                                                                                                                                                   |
| ---------------- | ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| `primary = true` | 是否是主键                                                                                                                                                                                                                                    |
| `max = 64`       | 最大长度|
| `required = true`| 是否允许为 NULL|

**NOTE:** 每个标记都需要单独占用一行

## Example

```java
import java.time.LocalDateTime;

/**
 * <p>Title: Order</p>
 *
 * @author luoqiz
 * @version 1.0
 * @description TODO
 * @date 2021/1/4 17:26
 * @since 1.0
 */
public class Order {
    /**
     * 消息ID，原样返回
     * max =64
     * primary =true
     */
    private String msgId;
    /**
     * 消息来源
     * min = 1
     * max = 4
     * required = true
     */
    private String msgSrc;
    /**
     * 消息类型
     * applepay.order
     * <p>
     * min = 1
     * max = 64
     * required = true
     */
    private String msgType;
    /**
     * 报文请求时间，格式yyyy-MM-dd HH:mm:ss
     * required = true
     */
    private LocalDateTime requestTimestamp;
    /**
     * 商户订单号
     * 商户自行生成
     * 注意：长度不要超过32位。
     * min = 6
     * max = 32
     * required = true
     */
    private String merOrderId;
    /**
     * 请求系统预留字段
     * max = 255
     */
    private String srcReserve;
    /**
     * 商户号
     * min = 8
     * max = 32
     * required = true
     */
    private String mid;

    /**
     * 终端号
     * min = 2
     * max = 32
     * must = true
     * required = true
     */
    private String tid;
    /**
     * 机构商户号
     * APPDEFAULT
     * <p>
     * min = 8
     * max = 32
     */
    private String instMid;

    /**
     * 订单过期时间
     * <p>
     * 订单过期时间，为空则使用系统默认过期时间（30分钟），格式yyyy-MM-dd HH:mm:ss
     */
    private LocalDateTime expireTime;

    /**
     * 商品交易单号
     * 跟goods字段二选一，商品信息通过goods.add接口提前上送
     */
    private String goodsTradeNo;

    /**
     * 订单原始金额，单位分，用于记录前端系统打折前的金额
     * min = 1
     * max = 100000000
     */
    private Long originalAmount;
    /**
     * 商品ID
     */
    private String productId;
    /**
     * 支付总金额，单位分
     * min = 1
     * max = 100000000
     */
    private Number totalAmount;

    /**
     * 是否需要限制信用卡支付
     * 取值：true或false，默认false
     */
    private Boolean limitCreditCard;

}
```

生成 sql 语句

```sql
CREATE TABLE `order`
(
    `msg_id`            VARCHAR(64) NOT NULL COMMENT '消息ID，原样返回      max =64      primary =true',
    `msg_src`           CHAR(4)     NOT NULL COMMENT '消息来源      min = 1      max = 4      required = true',
    `msg_type`          VARCHAR(64) NOT NULL COMMENT '消息类型      applepay.order      <p>      min = 1      max = 64      required = true',
    `request_timestamp` DATETIME    NOT NULL COMMENT '报文请求时间，格式yyyy-MM-dd HH:mm:ss      required = true',
    `mer_order_id`      VARCHAR(32) NOT NULL COMMENT '商户订单号      商户自行生成      注意：长度不要超过32位。      min = 6      max = 32      required = true',
    `src_reserve`       VARCHAR(255) Null COMMENT '请求系统预留字段      max = 255',
    `mid`               VARCHAR(32) NOT NULL COMMENT '商户号      min = 8      max = 32      required = true',
    `tid`               VARCHAR(32) NOT NULL COMMENT '终端号      min = 2      max = 32      must = true      required = true',
    `inst_mid`          VARCHAR(32) Null COMMENT '机构商户号      APPDEFAULT      <p>      min = 8      max = 32',
    `expire_time`       DATETIME Null COMMENT '订单过期时间      <p>      订单过期时间，为空则使用系统默认过期时间（30分钟），格式yyyy-MM-dd HH:mm:ss',
    `goods_trade_no`    VARCHAR(512) Null COMMENT '商品交易单号      跟goods字段二选一，商品信息通过goods.add接口提前上送',
    `original_amount`   text Null COMMENT '订单原始金额，单位分，用于记录前端系统打折前的金额      min = 1      max = 100000000',
    `product_id`        VARCHAR(512) Null COMMENT '商品ID',
    `total_amount`      text Null COMMENT '支付总金额，单位分      min = 1      max = 100000000',
    `limit_credit_card` TINYINT Null COMMENT '是否需要限制信用卡支付      取值：true或false，默认false',
    `create_time`       timestamp NULL DEFAULT CURRENT_TIMESTAMP,
    `update_time`       timestamp   NOT NULL ON UPDATE CURRENT_TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (`msg_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
```