package cn.lookup.sanye.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.UUID;

/**
 * @Author: zhangqm<sanye>
 * @Date: 2021/8/5 15:27
 * @Desc: 文件上传工具类
 **/
@Component
public class FileUploadAndDownloadUtils {
    private static int FILE_NAME_MAX_LENGTH;  //文件名最大的长度
    private static String FILE_DEFAULT_DIR;  //文件默认上传的位置
    private static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

    @Value("${upload.name-max-length}")
    public void setFileNameMaxLength(int fileNameMaxLength) {
        FILE_NAME_MAX_LENGTH = fileNameMaxLength;
    }

    @Value("${upload.default-dir}")
    public void setFileDefaultDir(String fileDefaultDir) {
        FILE_DEFAULT_DIR = fileDefaultDir;
    }

    /**
     * 单个文件上传
     *
     * @param oneFile
     * @param allowedExtension
     * @return
     * @throws Exception
     */
    public static String upload(MultipartFile oneFile, String[] allowedExtension) throws Exception {
        MultipartFile[] files = new MultipartFile[1];
        files[0] = oneFile;
        return upload(files, allowedExtension);
    }

    /**
     * 单个文件上传指定文件位置
     *
     * @param dir
     * @param oneFile
     * @param allowedExtension
     * @return
     * @throws Exception
     */
    public static String upload(String dir, MultipartFile oneFile, String[] allowedExtension) throws Exception {
        MultipartFile[] files = new MultipartFile[1];
        files[0] = oneFile;
        return upload(dir, files, allowedExtension);
    }

    /**
     * 上传到默认位置
     *
     * @param files
     * @param allowedExtension
     * @return
     * @throws Exception
     */
    public static String upload(MultipartFile[] files, String[] allowedExtension) throws Exception {
        return upload(FILE_DEFAULT_DIR, files, allowedExtension);
    }

    /**
     * 文件上传
     *
     * @param dir              目录
     * @param files            文件
     * @param allowedExtension 允许的后缀
     * @return 文件路径
     */
    public static String upload(String dir, MultipartFile[] files, String[] allowedExtension) throws Exception {
        assertAllowed(files, allowedExtension);
        ArrayList<String> fileNames = new ArrayList<>();
        for (MultipartFile file : files) {
            String fileName = encodeFileName(file);
            File absoluteFile = getAbsoluteFile(dir, fileName);
            file.transferTo(absoluteFile);
            fileNames.add(absoluteFile.getAbsolutePath());
        }
        return fileNames.toString();
    }

    /**
     * 文件下载,写入输出流
     *
     * @param filePath
     * @param os
     */
    public static void download(String filePath, OutputStream os) {
        FileInputStream fileInputStream = null;
        try {
            File file = new File(filePath);
            if (!file.exists()) {
                throw new Exception("文件不存在");
            }
            fileInputStream = new FileInputStream(file);
            byte[] b = new byte[1024];
            int length;
            while ((length = fileInputStream.read(b)) > 0) {
                os.write(b, 0, length);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (fileInputStream != null) {
                try {
                    fileInputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 删除文件
     *
     * @param filePath
     * @return
     */
    public static boolean deleteFile(String filePath) {
        File file = new File(filePath);
        if (file.isFile() && file.exists()) {
            file.delete();
            return true;
        }
        return false;
    }

    /**
     * 生成文件到指定目录
     *
     * @param dir
     * @param fileName
     * @return
     */
    public static File getAbsoluteFile(String dir, String fileName) throws IOException {
        File file = new File(dir + File.separator + fileName);
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }
        if (!file.exists()) {
            file.createNewFile();
        }
        return file;
    }

    /**
     * 生成唯一的文件名
     *
     * @param file
     * @return
     */
    public static String encodeFileName(MultipartFile file) {
        String uploadDate = simpleDateFormat.format(new Date());
        return uploadDate + "_" + UUID.randomUUID().toString() + "." + getExtension(file);
    }

    /**
     * 文件合法性校验
     *
     * @param files
     * @param allowedExtension
     * @return
     */
    public static boolean assertAllowed(MultipartFile[] files, String[] allowedExtension) throws Exception {
        for (MultipartFile file : files) {
            String originalFilename = file.getOriginalFilename();
            if (originalFilename != null && originalFilename.length() > FILE_NAME_MAX_LENGTH) {
                throw new Exception("文件名长度太长");
            }
            if (!Arrays.asList(allowedExtension).contains(getExtension(file))) {
                throw new Exception("文件类型错误");
            }
        }
        return true;
    }

    /**
     * 获取文件名的后缀
     *
     * @param file 表单文件
     * @return 后缀名
     */
    public static final String getExtension(MultipartFile file) {
        String fileName = file.getOriginalFilename();
        String extension = null;
        if (fileName == null) {
            return null;
        } else {
            extension = fileName.substring(fileName.lastIndexOf(".") + 1);
        }
        return extension;
    }
}
