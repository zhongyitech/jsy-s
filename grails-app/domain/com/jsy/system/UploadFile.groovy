package com.jsy.system

class UploadFile {

    //文档类型：方便下载
    String fileType;
//    byte[] fileData;

    //文档名称
    String fileName
    //文件提取标识
    String filePath

    static constraints = {
        fileType nullable: true
    }
}
