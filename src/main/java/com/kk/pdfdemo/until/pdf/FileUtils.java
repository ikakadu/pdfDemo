package com.kk.pdfdemo.until.pdf;

import cn.hutool.core.io.FileTypeUtil;
import cn.hutool.core.io.FileUtil;
import com.kk.pdfdemo.constant.SysConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import java.io.*;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * 文件工具类
 *
 **/
@Slf4j
public class FileUtils {

	private static List<String> imgFiltTypeList = Collections.unmodifiableList(Arrays.asList("jpg", "png", "jpeg"));

	public static String ReadFile(String filePath) throws IOException{
		return ReadFile(filePath, SysConstants.ENCODING_UTF_8);
	}

	public static String ReadFile(String filePath, String charset) throws IOException {
		File file = new File(filePath);
		return org.apache.commons.io.FileUtils.readFileToString(file, charset);
	}

	public static void WriteFile(String content, String filePath, boolean append) throws IOException {
		try (FileWriter fw = new FileWriter(filePath, append)) {
			fw.append(content);
			fw.flush();
		} catch (IOException e) {
			log.error("", e);
			throw e;
		}
	}

	public static String getImageExtension(byte[] data) {
		try (ImageInputStream iis = ImageIO.createImageInputStream(new ByteArrayInputStream(data))) {
			Iterator<ImageReader> imageReaders = ImageIO.getImageReaders(iis);
			while (imageReaders.hasNext()) {
				ImageReader reader = (ImageReader) imageReaders.next();
				return reader.getFormatName();
			}
		} catch (Exception e) {
			log.error("", e);
		}
		return "jpeg";
	}

	/**
	 * 将文件转换成byte数组
	 * @param tradeFile
	 * @return
	 */
	public static byte[] File2byte(File tradeFile) {
		byte[] buffer = null;
		try (FileInputStream fis = new FileInputStream(tradeFile);
			 ByteArrayOutputStream bos = new ByteArrayOutputStream();) {
			byte[] b = new byte[1024];
			int n;
			while ((n = fis.read(b)) != -1) {
				bos.write(b, 0, n);
			}
			buffer = bos.toByteArray();
		} catch (FileNotFoundException e) {
			log.error("", e);
		} catch (IOException e) {
			log.error("", e);
		}
		return buffer;
	}

	public static boolean checkUploadFileTypeIsImage(MultipartFile imgFile) throws IOException {
		String type = FileUtil.extName(imgFile.getOriginalFilename()).toLowerCase();
		if (!imgFiltTypeList.contains(type)) {
			return false;
		}

		type = FileTypeUtil.getType(imgFile.getInputStream());
		if (!imgFiltTypeList.contains(type)) {
			return false;
		}
		return true;
	}

}
