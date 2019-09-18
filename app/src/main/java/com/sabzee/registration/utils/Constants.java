package com.sabzee.registration.utils;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.Response;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

/**
 * Created by delaroy on 6/26/18.
 */

public interface Constants {
   /* String AUTHORIZATION = "AUTHORIZATION";
    String TOKEN = "token";
    String USER_BASE_URL = "http://sabzishoppee.in/Vendor/index.php/api/";
    String KEY_FIRMNAME = "firmname";
    String KEY_USERNAME = "username";
    String KEY_MOBILENUM = "mobilenum";
    String KEY_EMAIL = "email";
    String KEY_ADDRES = "addres";
    String KEY_AREA = "area";
    String KEY_CITYY = "cityy";*/
    @Multipart
    @POST("api/appuserinst/")
    Call<Response> uploadImage(@Part("firmname") RequestBody firmname,
                               @Part("ownername") RequestBody ownername,
                               @Part("email") RequestBody email,
                               @Part("mobile") RequestBody mobile,
                               @Part("address") RequestBody address,
                               @Part("area") RequestBody area,
                               @Part("city") RequestBody city,
                               @Part("pincode") RequestBody pincode,
                               @Part MultipartBody.Part image,
                               @Part MultipartBody.Part image1,
                               @Part MultipartBody.Part image2);


}
