package com.mobicoolsoft.electronic.store.config;

public class AppConstants {

    public static final String DELETE_MSG = " deleted successfully!";

    public static final String USER_URL = "/api/users/";

    public static final String CATEGORY_URL = "/api/categories";

    public static final String PRODUCT_URL = "/api/products";

    public static final String EMPTY_IMAGE_MSG = "Image file should not be empty!";

    public static final String IMAGE_MSG = " image is uploaded successfully!";

    public static final String EXTENSION_MSG = "File extensions other than .jpeg, .jpg, .png are not allowed";

    public static final String PAGE_ERROR_MSG = "Page Size, Page Number should not be less than 1 and sortBy, sortDir properly inserted";

    /**
     * @implNote default constants for pagination
     */

    public static final String PAGE_NUMBER = "1";
    public static final String PAGE_SIZE = "10";
    public static final String SORT_DIR = "asc";
    public static final String SORT_USER_BY = "name";
    public static final String SORT_CATEGORY_BY = "title";
    public static final String SORT_PRODUCT_BY = "title";




}
