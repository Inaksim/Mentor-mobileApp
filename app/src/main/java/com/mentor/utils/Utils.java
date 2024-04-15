package com.mentor.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import android.widget.Toast;
import androidx.fragment.app.FragmentActivity;
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

    public static final String COURSE_NAME_EXTRA = "course_title";

    public static final String COURSE_SUBTITLE_EXTRA = "course_subtitle";

    public static final String COURSE_MAIN_IMAGE_EXTRA = "course_main_image";

    public static final String COURSE_IMAGES_EXTRA = "course_images";

    public static final String COURSE_DESCRIPTION_EXTRA = "course_description";

    public static final String COURSE_ID_EXTRA = "course_id";

    public static final String COURSE_JSON_EXTRA = "course_json";

    public static final String IS_FAVORITE_EXTRA = "is_favorite";

    public static final String CATEGORY_ID_EXTRA = "category_id";

    public static final String SEARCH_EXTRA = "search";

    public static boolean validateEmail(String email) {
        pattern = Pattern.compile(EMAIL_PATTERN);
        matcher = pattern.matcher(email);
        return matcher.matches();
    }

    /**
     * Проверяване на парола чрез Регулярен израз
     * @param password
     * @return връща true за валиден парола и false за невалиден
     * Password
     * */
    public static boolean validatePassword(String password) {
        //Compiling the regular expression
        Pattern pattern = Pattern.compile(PASSWORD_PATTERN);
        //Retrieving the matcher object
        Matcher matcher = pattern.matcher(password);
        //Checking the string for pattern match
        return matcher.matches();
    }

    /**
     * проверява за Null String object
     *
     * @param txt
     * @return true връща когато не е нула и false за null String
    object
     */
    public static boolean isNotNull(String txt){
        return txt != null && txt.trim().length() > 0;
    }

    public static void showToast(FragmentActivity activity, String message) {
        Toast.makeText(activity, message, Toast.LENGTH_LONG).show();
    }

}
