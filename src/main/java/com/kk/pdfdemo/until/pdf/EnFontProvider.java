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
public class EnFontProvider extends XMLWorkerFontProvider {
    private Logger logger = LoggerFactory.getLogger(EnFontProvider.class);

    @Override
    public Font getFont(final String fontname, final String encoding,
                        final boolean embedded, final float size, final int style,
                        final BaseColor color) {
        BaseFont bf;
        try {
            bf = BaseFont.createFont(BaseFont.HELVETICA, BaseFont.WINANSI, BaseFont.NOT_EMBEDDED);
        } catch (Exception e) {
            logger.error("设置字体异常：{}", e);
            return null;
        }
        Font font = new Font(bf, size, style, color);
        font.setColor(color);
        return font;
    }
}
