package com.mentor.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utils {
    private static Pattern pattern;
    private static Matcher matcher;

    private static final String EMAIL_PATTERN = "^[_A-Za-z0-9-+]+(\\.[_A-Za-z0-9-]+)*@" + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

    //regex for checking password length 6 characters and at least one digit
    private static final String PASSWORD_PATTERN = "((?=.*\\d)(?=.*[a-z])(?=.*[A-Z]).{6,20})";

    public static final String EMAIL_EXTRA = "email";

    public static final String CURRENT_USER_EXTRA = "current_user";

    public static final String USER_EXTRA = "user";

    public static final String USER_ID_EXTRA = "user_id";

    public static final String VERIFY_CODE_EXTRA = "user";

    public static final String FIRST_NAME_EXTRA = "first_name";

    public static final String LAST_NAME_EXTRA = "last_name";

    public static final String PARENT_EXTRA = "parent";

    public static final String LOCATION_NAME_EXTRA = "location_title";

    public static final String LOCATION_SUBTITLE_EXTRA = "location_subtitle";

    public static final String LOCATION_MAIN_IMAGE_EXTRA = "location_main_image";

    public static final String LOCATION_IMAGES_EXTRA = "location_images";

    public static final String LOCATION_DESCRIPTION_EXTRA = "location_description";

    public static final String LOCATION_ID_EXTRA = "location_id";

    public static final String LOCATION_JSON_EXTRA = "location_json";

    public static final String IS_FAVORITE_EXTRA = "is_favorite";

    public static final String CATEGORY_ID_EXTRA = "category_id";

    public static final String SEARCH_EXTRA = "search";

    public static boolean validateEmail(String email) {
        pattern = Pattern.compile(EMAIL_PATTERN);
        matcher = pattern.matcher(email);
        return matcher.matches();
    }

    public static boolean validatePassword(String password) {
        pattern = Pattern.compile(PASSWORD_PATTERN);
        matcher = pattern.matcher(password);
        return matcher.matches();
    }

}
