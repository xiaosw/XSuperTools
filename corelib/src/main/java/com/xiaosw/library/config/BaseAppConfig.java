package com.xiaosw.library.config;

/**
 * @ClassName : {@link BaseAppConfig}
 * @Description :
 *
 * @Author xiaosw<xiaoshiwang@putao.com>
 * @Date 2016-09-09 12:12:02
 */
public class BaseAppConfig {
    /** getClass().getSimpleName() */
    private static final String TAG = "xiaosw-BaseAppConfig";

    /** 标记是否为调试模式 */
    public static final boolean DEBUG = true;

    /** GreenDao数据库名称(不加密) */
    public static final String DEFAULT_GREEN_DAO_DB_NAME = "xiaosw_super_tools.db";
    /** GreenDao数据库名称(加密) */
    public static final String DEFAULT_GREEN_DAO_DB_NAME_ENCRYPTED = "xiaosw_super_tools_encryption.db";
    /** GreenDao数据库名是否加密 */
    public static final boolean ENCRYPTED = true;
}
