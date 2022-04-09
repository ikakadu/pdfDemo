package com.kk.pdfdemo.service;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.kk.pdfdemo.constant.SysConstants;
import com.kk.pdfdemo.dto.CashOutTransQueryResp;
import com.kk.pdfdemo.until.pdf.HtmlUtils;
import com.kk.pdfdemo.until.pdf.PdfUtils;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.kk.pdfdemo.constant.SysConstants.*;

@Component
public class PdfCreate {



    public String createWithdrawalReceiptPdf(CashOutTransQueryResp CashOutTransQueryResp, HttpServletResponse response, String bicTitle) {
        Map<String, Object> map = getReceiptHtmlData(CashOutTransQueryResp, bicTitle);
        return createWithdrawalReceiptPdf(map, response);
    }

    public  Map<String, Object> getReceiptHtmlData(CashOutTransQueryResp cashOutTransQueryResp,String bicTitle) {
        /*String templateDir = new File(File.separator, "templates").getPath();
        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("dto", CashOutTransQueryResp);
        dataMap.put("lklLogo", System.getProperty("user.dir") + new File(templateDir, "lklLogo.png").getPath());
        dataMap.put("ckcgImagePath", System.getProperty("user.dir") + new File(templateDir, "ckcg.png").getPath());
        return dataMap;*/
        String templateDir = nasBasePath + pdfTemplatePath;
        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("dto", cashOutTransQueryResp);
        dataMap.put("lklLogo", templateDir + "lklLogo.png");
        dataMap.put("logoImage", templateDir + "logoImage.png");
        dataMap.put("ckcgImagePath", templateDir + "ckcg.png");
        dataMap.put("bicTitle", StrUtil.isNotBlank(bicTitle) ? bicTitle : "SWIFT BIC");
        return dataMap;
    }

    public String createWithdrawalReceiptPdf(Map<String, Object> receiptHtmlData, HttpServletResponse response) {
        // 组装模板名称
        String templateName = "trade_receipt" + SysConstants.HTML_SUFFIX;
        // 模板目录

        String templateDir = nasBasePath + pdfTemplatePath;
        String fileName = FILE_NAME_WITHDRAWAL_FUND + "_" + DateUtil.format(LocalDateTime.now(), DatePattern.PURE_DATETIME_PATTERN) + ".pdf";
        // 获取模板内容
        String templateContent = HtmlUtils.fillData(templateDir + templateName, receiptHtmlData);
        // css样式文件地址
        String cssFilePath = templateDir + "receipt_style.css";
        ByteArrayOutputStream baos = PdfUtils.html2PdfOutputStream(templateContent, cssFilePath, SysConstants.ENCODING_UTF_8);
        return PdfUtils.downloadSinglePdf(response, fileName, baos);
    }

    public  Map<String, byte[]>  createWithdrawalReceiptPdfZip(List<CashOutTransQueryResp> cashOutTransQueryRespList) {
        Map<String, byte[]> files = new HashMap<>();
        for (CashOutTransQueryResp dto: cashOutTransQueryRespList) {
            Map<String, Object> receiptHtmlData = getReceiptHtmlData(dto,"SWIFT BIC");

            // 组装模板名称
            String templateName = "trade_receipt.html";
            // 模板目录
            String templateDir = new File(File.separator, "templates").getPath();
            String fileName = "提现回单" + "_" + dto.getCashOutId()+".pdf";
            // 获取模板内容
            String templateContent = HtmlUtils.fillData(System.getProperty("user.dir") + new File(templateDir, templateName).getPath(), receiptHtmlData);

            // css样式文件地址
            String cssFilePath = System.getProperty("user.dir") + new File(templateDir, "receipt_style.css").getPath();

            ByteArrayOutputStream baos = PdfUtils.html2PdfOutputStream(templateContent, cssFilePath, "UTF-8");
            files.put(fileName,baos.toByteArray());
        }
        return files;
    }


}
