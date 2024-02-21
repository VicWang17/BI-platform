package com.yupi.springbootinit.utils;

import cn.hutool.core.collection.CollUtil;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.support.ExcelTypeEnum;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.ResourceUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * excel 工具类
 */
@Slf4j
public class ExcelUtils {

    public static String excelToCsv(MultipartFile multipartFile){
//        File file = null;
//        try {
//            file = ResourceUtils.getFile("classpath:Data.xlsx");
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        }
        List<Map<Integer, String>> list = null;
        try {
            list = EasyExcel.read(multipartFile.getInputStream())
                    .excelType(ExcelTypeEnum.XLSX)
                    .sheet()
                    .headRowNumber(0)
                    .doReadSync();
        } catch (IOException e) {
            log.error("Excel data error.", e);
        }
        if (CollUtil.isEmpty(list)){
            return "";
        }

        StringBuilder stringBuilder = new StringBuilder();
        LinkedHashMap<Integer, String> headerMap = (LinkedHashMap) list.get(0);
        List<String> headerlist = headerMap.values().stream().filter(ObjectUtils::isNotEmpty).collect(Collectors.toList());

        stringBuilder.append(StringUtils.join(headerlist,",")); //.append("\n");
        for (int i =1;i< list.size();i++){
            LinkedHashMap<Integer, String> dataMap = (LinkedHashMap) list.get(i);
            List<String> datalist = dataMap.values().stream().filter(ObjectUtils::isNotEmpty).collect(Collectors.toList());
            stringBuilder.append(StringUtils.join(datalist,",")); //append("\n");
        }
        System.out.println(list);
        return stringBuilder.toString();
    }

    public static void main(String[] args){
        excelToCsv(null);
    }
}
