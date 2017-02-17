package selling.sunshine.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.List;
import java.util.zip.CRC32;
import java.util.zip.CheckedOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;


/**
 * 文件压缩类
 * @author wxd
 */
public class ZipCompressor {   
    static final int BUFFER = 8192;   
  
    private File zipFile;   
    private Logger logger=LoggerFactory.getLogger(ZipCompressor.class);
    
    public ZipCompressor(String pathName) {   
        zipFile = new File(pathName);   
    }

    /**
     * 压缩一系列文件
     * @param pathList
     */
    public void compress(List<String> pathList) { 
    	ZipOutputStream out = null;   
    	try {  
			FileOutputStream fileOutputStream = new FileOutputStream(zipFile);   
			CheckedOutputStream cos = new CheckedOutputStream(fileOutputStream,   
					new CRC32());   
			out = new ZipOutputStream(cos);   
			String basedir = ""; 
			for (int i=0;i<pathList.size();i++){
				compress(new File(pathList.get(i)), out, basedir);   
			}
	    	out.close();  
    	} catch (Exception e) {   
			throw new RuntimeException(e);   
		} 
    }

    /**
     * 压缩文件
     * @param srcPathName
     */
    public void compress(String srcPathName) {   
        File file = new File(srcPathName);   
        if (!file.exists())   
            throw new RuntimeException(srcPathName + "不存在！");   
        try {   
            FileOutputStream fileOutputStream = new FileOutputStream(zipFile);   
            CheckedOutputStream cos = new CheckedOutputStream(fileOutputStream,   
                    new CRC32());   
            ZipOutputStream out = new ZipOutputStream(cos);   
            String basedir = "";   
            compress(file, out, basedir);   
            out.close();   
        } catch (Exception e) {   
            throw new RuntimeException(e);   
        }   
    }

    /**
     * 判断是文件还是目录，再调用相应的方法压缩
     * @param file
     * @param out
     * @param basedir
     */
    private void compress(File file, ZipOutputStream out, String basedir) {   
    	if (!file.exists()) {
			return;
		}
        /* 判断是目录还是文件 */  
        if (file.isDirectory()) {   
        	logger.debug("压缩：" + basedir + file.getName());             
            this.compressDirectory(file, out, basedir);   
        } else {  
        	logger.debug("压缩：" + basedir + file.getName());      
            this.compressFile(file, out, basedir);   
        }   
    }

    /**
     * 压缩一个目录
     * @param dir
     * @param out
     * @param basedir
     */
    private void compressDirectory(File dir, ZipOutputStream out, String basedir) {   
        if (!dir.exists())   
            return;   
  
        File[] files = dir.listFiles();   
        for (int i = 0; i < files.length; i++) {   
            /* 递归 */  
            compress(files[i], out, basedir + dir.getName() + "/");   
        }   
    }

    /**
     * 压缩一个文件
      * @param file
     * @param out
     * @param basedir
     */
    private void compressFile(File file, ZipOutputStream out, String basedir) {   
        if (!file.exists()) {   
            return;   
        }   
        try {   
            BufferedInputStream bis = new BufferedInputStream(   
                    new FileInputStream(file));   
            ZipEntry entry = new ZipEntry(basedir + file.getName());   
            out.putNextEntry(entry);   
            int count;   
            byte data[] = new byte[BUFFER];   
            while ((count = bis.read(data, 0, BUFFER)) != -1) {   
                out.write(data, 0, count);   
            }   
            bis.close();   
        } catch (Exception e) {   
            throw new RuntimeException(e);   
        }   
    }   
} 