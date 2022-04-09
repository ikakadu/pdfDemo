package com.kk.pdfdemo.until.pdf;

import com.itextpdf.awt.geom.Rectangle2D;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import com.itextpdf.text.pdf.parser.PdfReaderContentParser;
import com.itextpdf.tool.xml.XMLWorkerHelper;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;



/**
 * pdf工具类
 *
 **/
@Slf4j
public class PdfUtils {
    private static final String PDF = "PDF";

//    private static Logger log = LoggerFactory.getLogger(PdfUtils.class);

    /**
     * html转换成pdf文件
     *
     * @param htmlContent html内容
     * @param outFilePath 输出文件路径
     * @param cssFilePath css文件路径
     * @param encoding    编码
     */
    public static boolean html2Pdf(String htmlContent, String outFilePath, String cssFilePath, String encoding) {
        InputStream inCssFile = null;
        try {
            // 页面大小
            Rectangle rect = new Rectangle(PageSize.A4);

            // 创建文档对象
            Document document = new Document(rect);
            // 创建pdf写入对象
            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(outFilePath));
            // 文档对象open
            document.open();

            // 设置css文件
            if (org.apache.commons.lang3.StringUtils.isNotBlank(cssFilePath)) {
                inCssFile = new FileInputStream(cssFilePath);
            }

            // 设置编码格式
            Charset charset = null;
            if (org.apache.commons.lang3.StringUtils.isNotBlank(encoding)) {
                charset = Charset.forName(encoding);
            }

            // step 4
            XMLWorkerHelper.getInstance().parseXHtml(writer, document, new ByteArrayInputStream(htmlContent.getBytes()), inCssFile, charset, new AsianFontProvider());
            // 关闭文档对象
            document.close();
            log.info("html转换pdf文件成功，路径：{}", outFilePath);
            return true;
        } catch (DocumentException e) {
            log.error("html转换pdf文件异常：{}", e);
        } catch (IOException e) {
            log.error("html转换pdf文件异常：{}", e);
        } finally {
            try {
                if(inCssFile !=null){
                    inCssFile.close();
                }
            } catch (IOException e) {
                log.error("html转换pdf文件异常：{}", e);
            }
        }
        return false;
    }

    /**
     * 给pdf文件添加水印
     *
     * @param sourcePath    要加水印的原pdf文件
     * @param destPath      添加水印后的pdf文件
     * @param markContent   需要计算水印位置的文字内容
     * @param markImagePath 水印图片路径
     * @param width         水印图片的宽度
     * @param height        水印图片的高度
     * @param alpha         透明度(0.0 -- 1.0, 0.0为完全透明，1.0为完全不透明)
     * @throws Exception
     */
    public static void addPdfMark(String sourcePath, String destPath, String markContent, String markImagePath, float width, float height, float alpha) throws Exception {
        PdfReader reader = new PdfReader(sourcePath, PDF.getBytes());

        // 添加水印之后的pdf文件
        PdfStamper stamp = new PdfStamper(reader, new FileOutputStream(destPath));

        Double markLocationX = 0.0;
        Double markLocationY = 0.0;
        int pageSize = 1;

        if (org.apache.commons.lang3.StringUtils.isNotBlank(markContent)) {
            //新建一个PDF解析对象
            PdfReaderContentParser parser = new PdfReaderContentParser(reader);

            for (int i = 1; i <= reader.getNumberOfPages(); i++) {
                //新建一个ImageRenderListener对象，该对象实现了RenderListener接口，作为处理PDF的主要类
                CustomRenderListener listener = new CustomRenderListener();
                //解析PDF，并处理里面的文字
                parser.processContent(i, listener);
                // 根据pdf中的指定内容计算出的水印的位置
                List<Map<String, Rectangle2D.Float>> listText = listener.rowsTextRect;
                for (int k = 0; k < listText.size(); k++) {
                    Map<String, Rectangle2D.Float> map = listText.get(k);
                    for (Map.Entry<String, Rectangle2D.Float> entry : map.entrySet()) {
                        if (entry.getKey().contains(markContent)) {
                            pageSize = i;
                            markLocationX = entry.getValue().getX() + entry.getValue().getWidth();
                            markLocationY = entry.getValue().getY() - entry.getValue().getHeight();
                            break;
                        }
                    }
                }
            }
        }

        try {
            // 插入水印
            Image img = Image.getInstance(markImagePath);
            if (width <= 0 || height <= 0) {
                width = img.getWidth();
                height = img.getHeight();
            }
            img.scaleAbsolute(width, height);
            // 在字下面
            img.setAlignment(Image.UNDERLYING);
            // 水印的位置，根据pdf中的相对内容计算出的位置
            img.setAbsolutePosition(Float.parseFloat(markLocationX.toString()), Float.parseFloat(markLocationY.toString()));
            PdfContentByte under = stamp.getUnderContent(pageSize);
            PdfGState gs = new PdfGState();
            // 设置透明度为0.3
            gs.setFillOpacity(alpha);
            under.setGState(gs);
            under.addImage(img);
        } catch (Exception e) {
            log.error("pdf文件：{}，添加水印异常：{}", sourcePath, e);
        } finally {
            // 关闭
            stamp.close();
            reader.close();
        }
    }


    public static String html2PdfAndDownload(String htmlContent, HttpServletResponse response, String cssFilePath, String encoding, String fileName) throws Exception {
        InputStream inCssFile = null;
        try {
            // 页面大小
            Rectangle rect = new Rectangle(PageSize.A4);

            // 创建文档对象
            Document document = new Document(rect);


            ByteArrayOutputStream baos = new ByteArrayOutputStream();

            // 创建pdf写入对象
            PdfWriter writer = PdfWriter.getInstance(document, baos);
            // 文档对象open
            document.open();

            // 设置css文件
            if (org.apache.commons.lang3.StringUtils.isNotBlank(cssFilePath)) {
                inCssFile = new FileInputStream(cssFilePath);
            }

            // 设置编码格式
            Charset charset = null;
            if (org.apache.commons.lang3.StringUtils.isNotBlank(encoding)) {
                charset = Charset.forName(encoding);
            }

            // step 4
            XMLWorkerHelper.getInstance().parseXHtml(writer, document, new ByteArrayInputStream(htmlContent.getBytes()), inCssFile, charset, new AsianFontProvider());

            // 关闭文档对象
            document.close();

            //6，设置请求返回类型
            response.setContentType("application/pdf");
            response.setHeader("Content-Disposition", "attachment; filename=" + fileName);
            response.setContentLength(baos.size());
            OutputStream out = response.getOutputStream();
            baos.writeTo(out);
            out.flush();
            out.close();

            log.info("pdf文件下载成功");
            return "pdf文件下载成功";
        } catch (Exception e) {
            log.error("html转换pdf文件异常：{}", e);
            throw new RuntimeException("pdf文件下载失败");
        } finally {
            try {
                if(inCssFile !=null){
                    inCssFile.close();
                }
            } catch (IOException e) {
                log.error("html转换pdf文件异常：{}", e);
                throw new RuntimeException("pdf文件下载失败");
            }
        }
    }

    public static ByteArrayOutputStream html2PdfOutputStream(String htmlContent,  String cssFilePath, String encoding)  {
        InputStream inCssFile = null;
        try {
            // 页面大小
            Rectangle rect = new Rectangle(PageSize.A4);

            // 创建文档对象
            Document document = new Document(rect);


            ByteArrayOutputStream baos = new ByteArrayOutputStream();

            // 创建pdf写入对象
            PdfWriter writer = PdfWriter.getInstance(document, baos);
            // 文档对象open
            document.open();

            // 设置css文件
            if (org.apache.commons.lang3.StringUtils.isNotBlank(cssFilePath)) {
                inCssFile = new FileInputStream(cssFilePath);
            }

            // 设置编码格式
            Charset charset = null;
            if (org.apache.commons.lang3.StringUtils.isNotBlank(encoding)) {
                charset = Charset.forName(encoding);
            }

            // step 4
            XMLWorkerHelper.getInstance().parseXHtml(writer, document, new ByteArrayInputStream(htmlContent.getBytes()), inCssFile, charset, new AsianFontProvider());

            // 关闭文档对象
            document.close();


            log.info("pdf文件流生成成功");
            return baos;
        } catch (Exception e) {
            log.error("pdf文件流生成失败：{}", e);
            throw new RuntimeException("pdf文件流生成失败");
        } finally {
            try {
                if(inCssFile !=null){
                    inCssFile.close();
                }
            } catch (IOException e) {
                log.error("pdf文件流生成失败：{}", e);
                throw new RuntimeException("pdf文件流生成失败");
            }
        }
    }

    public static String downloadSinglePdf(HttpServletResponse response,  String fileName ,ByteArrayOutputStream baos) {
        try {
            response.setContentType("application/pdf");
            response.setHeader("Access-Control-Expose-Headers", "Content-Disposition");
            response.setHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode(fileName, "UTF-8"));
            response.setContentLength(baos.size());
            OutputStream out = response.getOutputStream();
            baos.writeTo(out);
            out.flush();
            out.close();
            log.info("pdf文件下载成功");
            return "pdf文件下载成功";
        } catch (IOException e) {
            log.error("html转换pdf文件异常：{}", e);
            throw new RuntimeException("pdf文件下载失败");
        }
    }


    public static String downloadBatchByFile(HttpServletResponse response, Map<String, byte[]> files, String zipName) throws Exception {
        try{
            response.reset();
            zipName = URLEncoder.encode(zipName, "UTF-8");
            response.setContentType("application/vnd.ms-excel;charset=UTF-8");
            response.setHeader("Access-Control-Expose-Headers", "Content-Disposition");
            response.setHeader("Content-Disposition", "attachment;filename=" + zipName + ".zip");
            ZipOutputStream zos = new ZipOutputStream(response.getOutputStream());
            BufferedOutputStream bos = new BufferedOutputStream(zos);

            for(Map.Entry<String, byte[]> entry : files.entrySet()){
                String fileName = entry.getKey();            //每个zip文件名
                byte[]    file = entry.getValue();            //这个zip文件的字节

                BufferedInputStream bis = new BufferedInputStream(new ByteArrayInputStream(file));
                zos.putNextEntry(new ZipEntry(fileName));

                int len = 0;
                byte[] buf = new byte[10 * 1024];
                while( (len=bis.read(buf, 0, buf.length)) != -1){
                    bos.write(buf, 0, len);
                }
                bis.close();
                bos.flush();
            }
            bos.close();
            log.info("pdf压缩包下载成功");
            return "pdf压缩包下载成功";
        }catch(Exception e){
            log.error("pdf压缩包下载失败：{}", e);
            throw new RuntimeException("pdf压缩包下载失败");
        }
    }
}
