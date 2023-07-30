package id.hikmah.stiki.tandur_1.v2.network

import id.hikmah.stiki.tandur_1.remote.model.LoginInfo
import id.hikmah.stiki.tandur_1.remote.model.LoginResponse
import id.hikmah.stiki.tandur_1.v2.model.*
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.*

interface APIService {

    //Register
    @POST("register")
    @Multipart
    fun register(
        @Part("id_district") idDistrict: Int,
        @Part("id_city") idCity: Int,
        @Part("id_province") idProvince: Int,
        @Part("email") email: String,
        @Part("ktp") ktp: String,
        @Part("name") name: String,
        @Part("password") password: String,
        @Part("telp") telp: String,
        @Part("address") address: String,
        @Part imgUser: MultipartBody.Part,
        @Part imgKtp: MultipartBody.Part,
        @Part imgKtpSelfie: MultipartBody.Part,
    ): Call<BaseModel>

    //Login
    @Headers("Content-Type: application/json")
    @POST("login")
    fun loginAccount(
        @Body loginInfo: LoginInfo
    ): Call<LoginResponse>

    //Province
    @GET("province")
    fun getProvince() : Call<ProvinceModel>

    //City
    @GET("city")
    fun getCity(
        @Query("id_province") idProvince: Int
    ) : Call<CityModel>

    //District
    @GET("district")
    fun getDistrict(
        @Query("id_city") idCity: Int
    ) : Call<DistrictModel>

    //Land
    @GET("land")
    fun getLand(
        @Header("Authorization") token: String,
        @Query("search") search: String? = null,
        @Query("sort") sort: String? = null,
    ) : Call<LandModel>//Land

    //Land
    @GET("land_user")
    fun getLandUser(
        @Header("Authorization") token: String,
        @Query("search") search: String? = null,
        @Query("sort") sort: String? = null,
    ) : Call<LandModel>//Land

    //Land
    @GET("detailland")
    fun getDetailLand(
        @Header("Authorization") token: String,
        @Query("id_land") idLand: String,
    ) : Call<LandModel>

    //Land
    @GET("list_product")
    fun getProduct(
        @Header("Authorization") token: String,
        @Query("search") search: String?,
        @Query("sort") sort: Int?,
        @Query("category") category: Int?,
        @Query("id_product_category") idProductCategory: Int?,
    ) : Call<ListProductModel>
    //Land

    //Land
    @GET("product_user")
    fun getproductUser(
        @Header("Authorization") token: String,
    ) : Call<ListProductModel>//Land

    @GET("product")
    fun getDetailProduct(
        @Header("Authorization") token: String,
        @Query("id_product") idProduct: String?
    ) : Call<ProductModel>

    //Insert Lahan
    @POST("land")
    @Multipart
    fun insertLahan(
        @Header("Authorization") token: String,
        @Part("name_land") name_land: String,
        @Part("address") address: String,
        @Part("province") province: String,
        @Part("city") city: String,
        @Part("district") district: String,
        @Part("location_land") location_land: String,
        @Part("nocertificate_land") nocertificate_land: String?,
        @Part("desc") desc: String,
        @Part("ownname_land") ownname_land: String,
        @Part("ownktp") ownktp: String,
        @Part("ownemail") ownemail: String,
        @Part("owntelp") owntelp: String,
        @Part("width_land") width_land: String,
        @Part("length_land") length_land: String,
        @Part("rule") rule: String,
        @Part("price") price: String,
        @Part("rating") rating: String,
        @Part("longtitude") longtitude: String,
        @Part("latitude") latitude: String,
        @Part("facility") facility: String,
        @Part foto_1: MultipartBody.Part,
        @Part foto_2: MultipartBody.Part,
        @Part gallery_1: MultipartBody.Part?,
        @Part gallery_2: MultipartBody.Part?,
        @Part gallery_3: MultipartBody.Part?,
        @Part gallery_4: MultipartBody.Part?,
        @Part gallery_5: MultipartBody.Part?,
    ): Call<BaseModel>

    //Insert Lahan
    @POST("product")
    @Multipart
    fun insertProduct(
        @Header("Authorization") token: String,
        @Part("name_product") name_product: String,
        @Part("category") category: String,
        @Part("id_product_category") id_product_category: String,
        @Part("price") price: String,
        @Part("weight") weight: String,
        @Part("stock") stock: String,
        @Part("condition") condition: String?,
        @Part("desc") desc: String,
        @Part("note") note: String,
        @Part gallery_1: MultipartBody.Part?,
        @Part gallery_2: MultipartBody.Part?,
        @Part gallery_3: MultipartBody.Part?,
        @Part gallery_4: MultipartBody.Part?,
        @Part gallery_5: MultipartBody.Part?,
    ): Call<BaseModel>

    //Insert Lahan
    @POST("product_update")
    @Multipart
    fun updateProduct(
        @Header("Authorization") token: String,
        @Part("name_product") name_product: String,
        @Part("category") category: String,
        @Part("id_product") id_product: String,
        @Part("id_product_category") id_product_category: String,
        @Part("price") price: String,
        @Part("weight") weight: String,
        @Part("stock") stock: String,
        @Part("condition") condition: String?,
        @Part("desc") desc: String,
        @Part("note") note: String,
        @Part gallery_1: MultipartBody.Part?,
        @Part gallery_2: MultipartBody.Part?,
        @Part gallery_3: MultipartBody.Part?,
        @Part gallery_4: MultipartBody.Part?,
        @Part gallery_5: MultipartBody.Part?,
    ): Call<BaseModel>

    //Rent Lahan
    @POST("rent")
    fun rentLahan(
        @Header("Authorization") token: String,
        @Body paymentRentLandModel: PaymentRentLandModel
    ): Call<RentResponseModel>

    //Detail Rent
    @GET("detailrent")
    fun getDetailRent(
        @Header("Authorization") token: String,
        @Query("id_rent") idRent: String,
    ) : Call<RentModel>

    //Upload Bukti Transfer
    @POST("evidence_rent")
    @Multipart
    fun uploadBuktiTransfer(
        @Header("Authorization") token: String,
        @Part("id_rent") idRent: String,
        @Part evidence_transfer: MultipartBody.Part
    ): Call<BaseModel>

    //Upload Bukti Transfer
    @POST("evidence_product")
    @Multipart
    fun uploadBuktiTransferProduk(
        @Header("Authorization") token: String,
        @Part("id_purchase") idPurchase: String,
        @Part evidence_transfer: MultipartBody.Part
    ): Call<BaseModel>

    //Kategori
    @GET("product_category")
    fun getCategory(
        @Header("Authorization") token: String,
        @Query("category") category: Int,
    ) : Call<CategoryModel>

    //Purchase Product
    @POST("purchase_product")
    fun purchaseProduct(
        @Header("Authorization") token: String,
        @Body requestProductModel: RequestProductModel
    ): Call<PurchaseResponseModel>

    //Detail Purchase
    @GET("detail_purchase_product")
    fun getDetailPurchase(
        @Header("Authorization") token: String,
        @Query("id_purchase") idPurchase: String,
    ) : Call<NewPurchaseResponseModel>

    //History Rent
    @GET("rent")
    fun getHistoryRent(
        @Header("Authorization") token: String
    ) : Call<HistoryRentModel>

    //History Rent
    @GET("list_purchase_product")
    fun getHistoryProduct(
        @Header("Authorization") token: String
    ) : Call<HistoryProductModel>

    //Log Notification
    @GET("message_notif")
    fun getLogNotification(
        @Header("Authorization") token: String
    ) : Call<NotificationResponseModel>

    @FormUrlEncoded
    @PUT("message_notif")
    fun putNotification(
        @Header("Authorization") token: String,
        @Field("id_log") idLog: Int
    ) : Call<BaseModel>

    @FormUrlEncoded
    @PUT("update_token")
    fun putUserToken(
        @Header("Authorization") token: String,
        @Field("user_token") userToken: String
    ) : Call<BaseModel>

    //Log Notification Product
    @GET("review_product")
    fun getReviewProduct(
        @Header("Authorization") token: String,
        @Query("id_product") idProduct: String
    ) : Call<ReviewProductResponseModel>

    //Log Notification land
    @GET("review_land")
    fun getReviewLand(
        @Header("Authorization") token: String,
        @Query("id_land") idLand: String
    ) : Call<ReviewLandResponseModel>

    //Review product
    @FormUrlEncoded
    @POST("review_product")
    fun postReviewProduct(
        @Header("Authorization") token: String,
        @Field("id_product") idProduct: String,
        @Field("id_purchase") idPurchase: String,
        @Field("rating") rating: String,
        @Field("review_title") reviewTitle: String,
        @Field("review_content") reviewContent: String,
    ): Call<BaseModel>

    //Review land
    @FormUrlEncoded
    @POST("review_land")
    fun postReviewLand(
        @Header("Authorization") token: String,
        @Field("id_land") idLand: String,
        @Field("id_rent") idRent: String,
        @Field("rating") rating: String,
        @Field("review_title") reviewTitle: String,
        @Field("review_content") reviewContent: String,
    ): Call<BaseModel>

    @GET("tutorial")
    fun getTutorial(
        @Header("Authorization") token: String,
    ) : Call<TutorialResponseModel>

    @GET("tutorial_detail")
    fun getDetailTutorial(
        @Header("Authorization") token: String,
        @Query("id_tutorial") idTutorial: String
    ) : Call<TutorialDetailResponseModel>

    //user update
    @POST("user_update")
    @Multipart
    fun updateProfile(
        @Header("Authorization") token: String,
        @Part("id_district") idDistrict: Int,
        @Part("id_city") idCity: Int,
        @Part("id_province") idProvince: Int,
        @Part("email") email: String,
        @Part("ktp") ktp: String,
        @Part("name") name: String,
        @Part("telp") telp: String,
        @Part("address") address: String,
        @Part("user_id") userId: String,
        @Part imgUser: MultipartBody.Part?,
    ): Call<LoginResponse>

    //Insert Lahan
    @POST("land_update")
    @Multipart
    fun updateLahan(
        @Header("Authorization") token: String,
        @Part("name_land") name_land: String,
        @Part("address") address: String,
        @Part("province") province: String,
        @Part("city") city: String,
        @Part("district") district: String,
        @Part("location_land") location_land: String,
        @Part("nocertificate_land") nocertificate_land: String?,
        @Part("desc") desc: String,
        @Part("ownname_land") ownname_land: String,
        @Part("ownktp") ownktp: String,
        @Part("ownemail") ownemail: String,
        @Part("owntelp") owntelp: String,
        @Part("width_land") width_land: String,
        @Part("length_land") length_land: String,
        @Part("rule") rule: String,
        @Part("price") price: String,
        @Part("rating") rating: String,
        @Part("longtitude") longtitude: String,
        @Part("latitude") latitude: String,
        @Part("facility") facility: String,
        @Part("id_land") idLand: String,
    ): Call<BaseModel>
}