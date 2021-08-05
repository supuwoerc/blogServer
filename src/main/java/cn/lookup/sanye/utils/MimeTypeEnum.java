package cn.lookup.sanye.utils;

/**
 * @Author: zhangqm<sanye>
 * @Date: 2021/8/5 21:45
 * @Desc: 文件类型
 */
public enum MimeTypeEnum {
    IMAGE_PNG(new String[]{"image/jpg"}),
    IMAGE_JPG(new String[]{"image/jpg"}),
    IMAGE_JPEG(new String[]{"image/jpeg"}),
    IMAGE_BMP(new String[]{"image/bmp"}),
    IMAGE_GIF(new String[]{"image/gif"}),
    IMAGE_EXTENSION(new String[]{"bmp", "gif", "jpg", "jpeg", "png"}),
    FLASH_EXTENSION(new String[]{"swf", "flv"}),
    MEDIA_EXTENSION(new String[]{"swf", "flv", "mp3", "wav", "wma", "wmv", "mid", "avi", "mpg", "asf", "rm", "rmvb"}),
    DEFAULT_ALLOWED_EXTENSION(new String[]{"bmp", "gif", "jpg", "jpeg", "png",
            "doc", "docx", "xls", "xlsx", "ppt", "pptx", "html", "htm", "txt",
            "rar", "zip", "gz", "bz2", "pdf"});
    private String[] types;

    MimeTypeEnum(String[] arr) {
        this.types = arr;
    }

    public  String[] getTypes() {
        return types;
    }
}