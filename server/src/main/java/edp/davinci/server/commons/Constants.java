/*
 * <<
 *  Davinci
 *  ==
 *  Copyright (C) 2016 - 2020 EDP
 *  ==
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 *  >>
 *
 */

package edp.davinci.server.commons;

import java.io.File;
import java.util.regex.Pattern;

/**
 * 常量
 */
public class Constants {

    /**
     * 当前用户
     */
    public static final String CURRENT_USER = "CURRENT_USER";

    /**
     * 当前平台
     */
    public static final String CURRENT_PLATFORM = "CURRENT_PLATFORM";

    /**
     * auth code key
     */
    public static final String AUTH_CODE = "authCode";

    /**
     * Token 前缀
     */
    public static final String TOKEN_PREFIX = "Bearer";

    /**
     * Token header名称
     */
    public static final String TOKEN_HEADER_STRING = "Authorization";

    /**
     * Token 用户名
     */
    public static final String TOKEN_USER_NAME = "token_user_name";

    /**
     * Token 密码
     */
    public static final String TOKEN_USER_PASSWORD = "token_user_password";

    /**
     * Token 创建时间
     */
    public static final String TOKEN_CREATE_TIME = "token_create_time";

    public static final String DEFAULT_COPY_SUFFIX = "_COPY";

    /**
     * 常用图片格式
     */
    public static final String REG_IMG_FORMAT = "^.+(.JPEG|.jpeg|.JPG|.jpg|.PNG|.png|.GIF|.gif)$";
    public static final Pattern PATTERN_IMG_FORMAT = Pattern.compile(REG_IMG_FORMAT);

    /**
     * 邮箱格式
     */
    public static final String REG_EMAIL_FORMAT = "^[a-zA-Z_0-9.-]{1,64}@([a-zA-Z0-9-]{1,200}.){1,5}[a-zA-Z]{1,6}$";
    public static final Pattern PATTERN_EMAIL_FORMAT = Pattern.compile(Constants.REG_EMAIL_FORMAT);

    public static final Pattern PATTERN_DB_COLUMN_TYPE = Pattern.compile("^.*\\s*\\(.*\\)$");

    public static final String DIR_DOWNLOAD = File.separator + "download" + File.separator;

    public static final String DIR_SHARE_DOWNLOAD = File.separator + "share" + File.separator + "download" + File.separator;

    public static final String DIR_EMAIL = File.separator + "email" + File.separator;

    public static final String DIR_TEMP = File.separator + "tempFiles" + File.separator;

    public static final String HTTP_PROTOCOL = "http";

    public static final String HTTPS_PROTOCOL = "https";

    public static final String PROTOCOL_SEPARATOR = "://";

    public static final String JDBC_DATASOURCE_DEFAULT_VERSION = "Default";

    /**
     * api基本路径
     */
    public static final String BASE_API_PATH = "/api/v3";

    /**
     * auth基本路径
     */
    public static final String AUTH_API_PATH = "/auth/v3";

    /**
     * 用户激活/重发激活邮件模板
     */
    public static final String USER_ACTIVATE_EMAIL_TEMPLATE = "mail/userActivateEmailTemplate";

    public static final String EMAIL_DEFAULT_TEMPLATE = "mail/emailDefaultTemplate";

    /**
     * 用户重置密码邮件模板
     */
    public static final String USER_REST_PASSWORD_EMAIL_TEMPLATE = "mail/userRestPasswordEmailTemplate";

    /**
     * 用户激活/重发激活邮件主题
     */
    public static final String USER_ACTIVATE_EMAIL_SUBJECT = "[Davinci] 用户激活";

    /**
     * 用户重置密码邮件主题
     */
    public static final String USER_REST_PASSWORD_EMAIL_SUBJECT = "[Davinci] 重置密码";

    /**
     * 用户头像上传地址
     */
    public static final String USER_AVATAR_PATH = "/image/user/";

    /**
     * 组织头像上传地址
     */
    public static final String ORG_AVATAR_PATH = "/image/organization/";

    /**
     * display封面图地址
     */
    public static final String DISPLAY_AVATAR_PATH = "/image/display/";

    /**
     * 邀请组织成员邮件主题
     * inviter username
     * organization
     */
    public static final String INVITE_ORG_MEMBER_MAIL_SUBJECT = "[Davinci] %s has invited you to join the %s organization";

    /**
     * 邀请组织成员邮件模板
     */
    public static final String INVITE_ORG_MEMBER_MAIL_TEMPLATE = "mail/inviteOrgMemberTemplate";

    /**
     * 分割符号
     */
    public static final String SPLIT_CHAR_STRING = ":-:";

    /**
     * sql ST模板
     */
    public static final String SQL_TEMPLATE = "templates/sql/sqlTemplate.stg";

    /**
     * excel 表头，数据格式化js
     */
    public static final String FORMAT_CELL_VALUE_JS = "templates/js/formatCellValue.js";

    /**
     * 格式化全局参数js
     */
    public static final String FORMAT_QUERY_PARAM_JS = "templates/js/formatQueryParam.js";

    /**
     * 定时任务发送邮件模板
     */
    public static final String SCHEDULE_MAIL_TEMPLATE = "mail/scheduleEmailTemplate";

    /**
     * 点赞project
     */
    public static final String STAR_TARGET_PROJECT = "project";

    public static final String REG_USER_PASSWORD = ".{6,20}";

    public static final String LDAP_USER_PASSWORD = "LDAP";

    public static final String DAVINCI_TOPIC_CHANNEL = "DAVINCI_TOPIC_CHANNEL";

    public static final int INVALID_SHEET_NAME_LENGTH = 28;

    private static final String REG_INVALID_SHEET_NAME = "[\\!\\\\\\/\\?\\*\\[\\]\\:]";

    public static final Pattern PATTERN_INVALID_SHEET_NAME = Pattern.compile(REG_INVALID_SHEET_NAME);
}
