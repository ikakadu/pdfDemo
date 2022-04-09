package com.kk.pdfdemo.until.pdf;


import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Font;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.tool.xml.XMLWorkerFontProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 字体
 *
 **/

public class AsianFontProvider extends XMLWorkerFontProvider {
//    private Logger logger = LoggerFactory.getLogger(com.lagofx.mchtweb.util.pdf.AsianFontProvider.class);
    Logger log = LoggerFactory.getLogger(getClass());
    @Override
    public Font getFont(final String fontname, final String encoding,
                        final boolean embedded, final float size, final int style,
                        final BaseColor color) {
        BaseFont bf = null;
        try {
            bf = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H", BaseFont.NOT_EMBEDDED);
        } catch (Exception e) {
            log.error("设置字体异常：{}", e);
            return null;
        }
        Font font = new Font(bf, size, style, color);
        font.setColor(color);
        return font;
    }
}
