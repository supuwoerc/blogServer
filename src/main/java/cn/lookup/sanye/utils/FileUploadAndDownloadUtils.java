package cn.lookup.sanye.utils;

import cn.lookup.sanye.common.vo.UploadFile;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

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
     * 单个文件上传到默认位置
     *
     * @param oneFile
     * @param allowedExtension
     * @return
     * @throws Exception
     */
    public static List<UploadFile> upload(MultipartFile oneFile, String[] allowedExtension) throws Exception {
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
    public static List<UploadFile> upload(String dir, MultipartFile oneFile, String[] allowedExtension) throws Exception {
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
    public static List<UploadFile> upload(MultipartFile[] files, String[] allowedExtension) throws Exception {
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
    public static List<UploadFile> upload(String dir, MultipartFile[] files, String[] allowedExtension) throws Exception {
        assertAllowed(files, allowedExtension);
        ArrayList<UploadFile> uploadList = new ArrayList<>();
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        for (MultipartFile file : files) {
            String fileName = encodeFileName(file);
            final Calendar instance = Calendar.getInstance();
            File absoluteFile = getAbsoluteFile(dir, instance, fileName);
            file.transferTo(absoluteFile);
            UploadFile uploadFile = new UploadFile();
            uploadFile.setSize(file.getSize());
            uploadFile.setCreate_time(dateTimeFormatter.format(LocalDateTime.now()));
            uploadFile.setName(fileName);
            uploadFile.setSource_name(file.getOriginalFilename());
            uploadFile.setLocation(absoluteFile.getAbsolutePath());
            uploadFile.setUrl(getFileUrl(instance, fileName));
            uploadList.add(uploadFile);
        }
        return uploadList;
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
     * 下载文件名重新编码
     *
     * @param request  请求对象
     * @param fileName 文件名
     * @return 编码后的文件名
     */
    public static String setFileDownloadHeader(HttpServletRequest request, String fileName)
            throws UnsupportedEncodingException {
        final String agent = request.getHeader("USER-AGENT");
        String filename = fileName;
        if (agent.contains("MSIE")) {
            // IE浏览器
            filename = URLEncoder.encode(filename, "utf-8");
            filename = filename.replace("+", " ");
        } else if (agent.contains("Firefox")) {
            // 火狐浏览器
            filename = new String(fileName.getBytes(), "ISO8859-1");
        } else if (agent.contains("Chrome")) {
            // google浏览器
            filename = URLEncoder.encode(filename, "utf-8");
        } else {
            // 其它浏览器
            filename = URLEncoder.encode(filename, "utf-8");
        }
        return filename;
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
    public static File getAbsoluteFile(String dir, Calendar instance, String fileName) throws IOException {
        final int year = instance.get(Calendar.YEAR);
        final int month = instance.get(Calendar.MONTH) + 1;
        final int date = instance.get(Calendar.DATE);
        File file = new File(dir + File.separator + year + File.separator + month + File.separator + date + File.separator + fileName);
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }
        if (!file.exists()) {
            file.createNewFile();
        }
        return file;
    }

    /**
     * 返回访问图片路径(配合图片映射)
     *
     * @param instance
     * @param fileName
     * @return
     */
    public static String getFileUrl(Calendar instance, String fileName) {
        final int year = instance.get(Calendar.YEAR);
        final int month = instance.get(Calendar.MONTH) + 1;
        final int date = instance.get(Calendar.DATE);
        return "/upload-images/" + year + "/" + month + "/" + date + "/" + fileName;
    }

    /**
     * 生成唯一的文件名
     *
     * @param file
     * @return
     */
    public static String encodeFileName(MultipartFile file) {
        String uploadDate = simpleDateFormat.format(new Date());
        return UUID.randomUUID().toString() + "." + getExtension(file);
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

    /**
     * 获取文件名(从文件路径获取最后一个路径分隔符后面的文件名)
     *
     * @param path
     * @return
     */
    public static final String getFileName(String path) {
        String filePath = path.trim();
        int index = filePath.lastIndexOf(File.separator);
        return filePath.substring(index + 1);
    }
}
