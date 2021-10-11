package com.claim.server.utils;

import com.claim.server.common.po.SUser;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * 通用工具类
 */
public class CommonUtil {

    private static final String datePattern = "yyyy-MM-dd";

    private static final String dateTimePattern = "yyyy-MM-dd HH:mm:ss";

    /**
     * 获取当前用户
     * @return
     */
    public static SUser getLoginUser() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        SUser user =  (SUser) request.getAttribute("loginUser");
        return user;
    }

    /**
     * 根据任务代码获取对应中文
     * @param taskType
     * @return
     */
    public static String getTaskTypeName(String taskType) {
        String taskTypeName;
        if (taskType.startsWith("Undwrt")) {
            String level = taskType.split("_")[1];
            taskTypeName = TaskTypeEnum.valueOf("Undwrt").getName() + level + "级";
        } else if(taskType.startsWith("ClaimApprove")) {
            String level = taskType.split("_")[1];
            taskTypeName = TaskTypeEnum.valueOf("ClaimApprove").getName() + level + "级";
        } else if(taskType.startsWith("EndCase")) {
            String level = taskType.split("_")[1];
            taskTypeName = TaskTypeEnum.valueOf("EndCase").getName() + level + "级";
        } else if(taskType.startsWith("PersonApprove")) {
            String level = taskType.split("_")[1];
            taskTypeName = TaskTypeEnum.valueOf("PersonApprove").getName() + level + "级";
        } else {
            taskTypeName = TaskTypeEnum.valueOf(taskType).getName();
        }
        return taskTypeName;
    }

    /**
     * 根据任务代码获取对应代码
     * @param taskType
     * @return
     */
    public static String getTaskTypeCode(String taskType) {
        String taskTypeCode;
        if (taskType.startsWith("Undwrt")) {
            taskTypeCode = TaskTypeEnum.valueOf("Undwrt").getCode();
        } else if(taskType.startsWith("ClaimApprove")) {
            taskTypeCode = TaskTypeEnum.valueOf("ClaimApprove").getCode();
        } else if(taskType.startsWith("EndCase")) {
            taskTypeCode = TaskTypeEnum.valueOf("EndCase").getCode();
        } else if(taskType.startsWith("PersonApprove")) {
            taskTypeCode = TaskTypeEnum.valueOf("PersonApprove").getCode();
        } else {
            taskTypeCode = TaskTypeEnum.valueOf(taskType).getCode();
        }
        return taskTypeCode;
    }

    /**
     * 根据任务状态代码获取对应中文
     * @param taskStatus
     * @return
     */
    public static String getTaskStatusName(String taskStatus) {
        String taskStatusName = "";
        switch (taskStatus) {
            case "0": taskStatusName = "未接收";break;
            case "1": taskStatusName = "正处理";break;
            case "2": taskStatusName = "已处理";break;
            case "-1": taskStatusName = "已退回";break;
            default: taskStatusName = "未知状态";
        }
        return taskStatusName;
    }

    /**
     * 异常转换成字符串
     * */
    public static String getStackTraceAsString(Exception e) {
        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        e.printStackTrace(printWriter);
        StringBuffer message = stringWriter.getBuffer();
        return message.toString();
    }

    /**
     * 转换时间
     * @param dateTime
     * @param pattern
     * @return
     */
    public static String formatDateTime(Date dateTime,String pattern) {
        if (dateTime == null){
            return "";
        }

        if (StringUtils.isBlank(pattern)) {
            pattern = dateTimePattern;
        }

        LocalDateTime localDateTime = dateTime.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern(pattern);
        return dtf.format(localDateTime);
    }

    /**
     * 转换时间
     * @param dateTime
     * pattern 默认 yyyy-MM-dd HH:mm:ss
     * @return
     */
    public static String formatDateTime(Date dateTime) {
        return formatDateTime(dateTime,dateTimePattern);
    }

    /**
     * 转换时间
     * @param timestamp
     * @param pattern
     * @return
     */
    public static String formatDateTime(Long timestamp, String pattern) {
        if (timestamp == null){
            return "";
        }

        if (StringUtils.isBlank(pattern)) {
            pattern = dateTimePattern;
        }

        LocalDateTime localDateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(timestamp),ZoneId.systemDefault());
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern(pattern);
        return dtf.format(localDateTime);
    }

    /**
     * 转换时间
     * @param timestamp
     * pattern 默认 yyyy-MM-dd HH:mm:ss
     * @return
     */
    public static String formatDateTime(Long timestamp) {
        return formatDateTime(timestamp,dateTimePattern);
    }

    /**
     * 转换时间
     * @param dateStr
     * @param pattern
     * @return
     */
    public static Date parseDateTime(String dateStr,String pattern) {
        if (dateStr == null){
            return null;
        }

        if (StringUtils.isBlank(pattern)) {
            pattern = dateTimePattern;
        }

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern(pattern);
        Instant instant = LocalDateTime.parse(dateStr,dtf).atZone(ZoneId.systemDefault()).toInstant();
        return Date.from(instant);
    }

    /**
     * 转换时间
     * @param dateStr
     * pattern 默认 yyyy-MM-dd HH:mm:ss
     * @return
     */
    public static Date parseDateTime(String dateStr) {
        return parseDateTime(dateStr,dateTimePattern);
    }
}
