package com.kk.pdfdemo.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class CashOutTransQueryResp {
    private String cashOutId;

    /**
     * 商户号
     *
     * Table:     t_cash_out_trans
     * Column:    mcht_no
     * Length:  18
     */
    private String mchtNo;

    /**
     * 提现金额
     *
     * Table:     t_cash_out_trans
     * Column:    cash_out_amount
     * Length:  19
     */
    private Long cashOutAmount;

    /**
     * 提现账户
     *
     * Table:     t_cash_out_trans
     * Column:    cash_out_account
     * Length:  32
     */
    private String cashOutAccount;

    /**
     * 提现银行
     *
     * Table:     t_cash_out_trans
     * Column:    cash_out_bank
     * Length:  128
     */
    private String cashOutBank;

    /**
     * 提现卡号
     *
     * Table:     t_cash_out_trans
     * Column:    cash_out_card_num
     * Length:  32
     */
    private String cashOutCardNum;

    /**
     * 预计到账金额
     *
     * Table:     t_cash_out_trans
     * Column:    cash_out_expect_amount
     * Length:  19
     */
    private Long cashOutExpectAmount;

    /**
     * Table:     t_cash_out_trans
     * Column:    cash_out_currency
     * Length:  6
     */
    private String cashOutCurrency;

    /**
     * 提现卡币种
     *
     * Table:     t_cash_out_trans
     * Column:    cash_out_card_currency
     * Length:  6
     */
    private String cashOutCardCurrency;

    /**
     * 提现汇率
     *
     * Table:     t_cash_out_trans
     * Column:    cash_out_rate
     * Length:  18
     */
    private BigDecimal cashOutRate;

    /**
     * 提现时间
     *
     * Table:     t_cash_out_trans
     * Column:    cash_out_time
     * Length:  19
     */
    private Date cashOutTime;

    /**
     * 提现手续费
     *
     * Table:     t_cash_out_trans
     * Column:    cash_out_fee
     * Length:  19
     */
    private Long cashOutFee;

    /**
     * 提现状态：01提现中，02提现成功，03提现失败
     *
     * Table:     t_cash_out_trans
     * Column:    cash_out_state
     * Length:  4
     */
    private String cashOutState;

    /**
     * 换汇状态:01换汇中，02换汇成功，03换汇失败，04换汇垫资成功，05换汇垫资失败
     *
     * Table:     t_cash_out_trans
     * Column:    exchange_state
     * Length:  4
     */
    private String exchangeState;

    /**
     * 发起换汇时间
     *
     * Table:     t_cash_out_trans
     * Column:    exchange_time
     * Length:  19
     */
    private Date exchangeTime;

    /**
     * 付款状态:01付款中，02跨境人民币付款成功，03跨境人民币付款失败，04跨境人民币垫资失败，05跨境人民币垫资成功
     *
     * Table:     t_cash_out_trans
     * Column:    payment_state
     * Length:  4
     */
    private String paymentState;

    /**
     * 发起付款时间
     *
     * Table:     t_cash_out_trans
     * Column:    payment_time
     * Length:  19
     */
    private Date paymentTime;

    /**
     * 单笔代付状态：01代付中，02成功，03失败
     *
     * Table:     t_cash_out_trans
     * Column:    df_state
     * Length:  4
     */
    private String dfState;

    /**
     * 单笔代付时间
     *
     * Table:     t_cash_out_trans
     * Column:    df_time
     * Length:  19
     */
    private Date dfTime;

    /**
     * 总流程状态：01开始提现交易，02提现成功，03提现失败，04换汇处理中，05换汇成功，06换汇失败，07跨境人民币付款中，08跨境人民币付款成功，09跨境人民币付款失败，10代付中，11代付成功，12代付失败
     *
     * Table:     t_cash_out_trans
     * Column:    state
     * Length:  4
     */
    //private String state;

    private Date gmtUpdate;//更新时间

    private String mchtName;//商户名称

    private String cashOutFeeYuanStringValue;//

    private String cashOutRateYuanStringValue;//

    private String cashOutExpectAmountYuanStringValue;

    private String cashOutAmountYuanStringValue;

    private String cashOutTimeStringFormat;

    private String swiftBic;
}