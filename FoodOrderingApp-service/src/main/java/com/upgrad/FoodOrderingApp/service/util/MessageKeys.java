package com.upgrad.FoodOrderingApp.service.util;

public final class MessageKeys {

    //Authorization Error Keys
    public static final String ATHR_001 = "ATHR-001";
    public static final String ATHR_002 = "ATHR-002";
    public static final String ATHR_003 = "ATHR-003";
    public static final String ATHR_004 = "ATHR-004";
    public static final String SAR_001 = "SAR-001";
    public static final String SAR_002 = "SAR-002";
    public static final String ANF_002 = "ANF-002";
    public static final String ANF_005 = "ANF-005";
    public static final String ANF_003 = "ANF-003";


    //Keys related to User
    public static final String CUSTOMER_IS_NOT_LOGGED_IN =
            "Customer is not Logged in.";

    public static final String CUSTOMER_IS_LOGGED_OUT =
            "Customer is logged out. Log in again to access this endpoint.";

    public static final String SESSION_IS_EXPIRED =
            "Your session is expired. Log in again to access this endpoint.";

    public static final String NO_FIELD_CAN_BE_EMPTY =
            "No field can be empty";

    public static final String INVALID_PINCODE =
            "Invalid pincode";

    public static final String NO_STATE_BY_THIS_ID =
            "No state by this id";

    public static final String ADDRESS_CANNOT_BE_EMPTY =
    "Address id can not be empty";

    public static final String NO_ADDRESS_BY_THIS_ID =
    "No address by this id";

    public static final String CUSTOMER_NOT_AUTHORIZED_TO_UPDATE =
    "You are not authorized to view/update/delete any one else's address";
}