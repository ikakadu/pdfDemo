package com.kk.pdfdemo.service;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import com.kk.pdfdemo.dto.CashOutTransQueryResp;
import com.kk.pdfdemo.until.pdf.HtmlUtils;
import com.kk.pdfdemo.until.pdf.PdfUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
public class PdfController {

   @Autowired
   PdfCreate pdfCreate;



    @GetMapping("/create/pdf1")
    public  String getReceiptPdf1(HttpServletRequest request, HttpServletResponse response) {
        CashOutTransQueryResp dto = new CashOutTransQueryResp();
        dto.setCashOutAccount("112233");
        dto.setCashOutAmount(23123L);
        dto.setCashOutBank("yinhhh");
        dto.setCashOutCurrency("CNY");
        dto.setCashOutFee(123L);
        dto.setDfState("2022-09-07");
        PdfCreate pdfCreate1 = new PdfCreate();
        return pdfCreate1.createWithdrawalReceiptPdf(dto,response,null);
    }

    @GetMapping("/create/pdf")
    public  String getReceiptPdf(HttpServletRequest request, HttpServletResponse response) {


        CashOutTransQueryResp dto = new CashOutTransQueryResp();
        dto.setCashOutAccount("112233");
        dto.setCashOutAmount(23123L);
        dto.setCashOutBank("yinhhh");
        dto.setCashOutCurrency("CNY");
        dto.setCashOutFee(123L);
        dto.setDfState("2022-09-07");


        Map<String, Object> receiptHtmlData = pdfCreate.getReceiptHtmlData(dto,null);
        // 组装模板名称
        String templateName = "trade_receipt.html";
        // 模板目录
//        String templateDir = new File( "templates").getPath();
        String resourcePath = ClassLoader.getSystemResource("templates/pdf").getPath();//   resourcePath="src/main/resources/templates/pdf";
        System.out.println(resourcePath);

//        String templateDir = new File(File.separator, "templates").getPath();
        String fileName = "提现回单_" + DateUtil.format(LocalDateTime.now(), DatePattern.PURE_DATETIME_PATTERN)+".pdf";
        // 获取模板内容

        String templateContent = HtmlUtils.fillData(new File(resourcePath, templateName).getPath(), receiptHtmlData);

        // css样式文件地址
        String cssFilePath = new File(resourcePath, "receipt_style.css").getPath();
//        String cssFilePath = System.getProperty("user.dir") + new File(templateDir, "receipt_style.css").getPath();

//        return PdfUtils.html2PdfAndDownload(templateContent, response, cssFilePath, SysConstants.ENCODING_UTF_8, fileName);
        ByteArrayOutputStream baos = PdfUtils.html2PdfOutputStream(templateContent, cssFilePath, "utf-8");
        return PdfUtils.downloadSinglePdf(response,fileName,baos);
    }




    @GetMapping("/create/pdfzip")
    public  String getReceiptPdfZip(HttpServletRequest request, HttpServletResponse response) throws Exception {
        List<CashOutTransQueryResp> cashOutTransQueryRespList = null;
        Map<String, byte[]> receiptPdfZip = pdfCreate.createWithdrawalReceiptPdfZip(cashOutTransQueryRespList);
        return PdfUtils.downloadBatchByFile(response, receiptPdfZip, "FILE_NAME_RECEIPT");

    }


}
