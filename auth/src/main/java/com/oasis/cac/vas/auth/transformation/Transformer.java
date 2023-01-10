package com.oasis.cac.vas.auth.transformation;

import org.apache.commons.lang3.StringUtils;

public abstract class Transformer {

    static final String TRANSFORMATION_DATA_FOLDER = "data-transforms";
    static final String EXCEL_FOLDER = "excel";
    static final String JSON_FOLDER = "json";
    /////////json
    static final String USER_ROLES_FILE_NAME = "user_roles_and_privileges.json";
    static final String PRIVILEGES = "privileges.json";
    static final String SETTINGS_FILE_NAME = "settings.json";
    static final String PORTAL_ACCOUNT_TYPES = "portal_acccount_types.json";


    //////////////////////// Adding users /////////////////////////////////////
    public static final String ADMIN_GENERAL_USER_FILE_NAME = "admin_general_user.json";
    public static final String ADMIN_LICENSED_USER_FILE_NAME = "admin_licensed_user.json";

    //////////////////
    public static final String PAYMENT_METHODS_FILE_NAME = "payment_methods.json";

    public static String getStringValue(org.apache.poi.ss.usermodel.Cell cell) {
        if (cell == null) {
            return "";
        }
        cell.setCellType(org.apache.poi.ss.usermodel.Cell.CELL_TYPE_STRING);
        String value = StringUtils.trim(cell.getStringCellValue());
        return value == null ? "" : value;
    }
}
