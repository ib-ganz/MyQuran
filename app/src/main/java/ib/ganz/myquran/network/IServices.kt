//package ib.ganz.sipp_on.network
//
//import ib.ganz.sipp_on.dataclass.*
//import io.reactivex.Single
//import okhttp3.MultipartBody
//import okhttp3.ResponseBody
//import retrofit2.Response
//import retrofit2.http.*
//
//interface IServices {
//
//    @POST("login.php")
//    @FormUrlEncoded fun login(
//        @Field("auth")  auth: String,
//        @Field("nik")   nik: String,
//        @Field("token") token: String
//    ): Single<LoginResponse>
//
//    @POST("register.php")
//    @FormUrlEncoded fun register(
//        @Field("auth")      auth: String,
//        @Field("nik")       nik: String,
//        @Field("email")     email: String,
//        @Field("no_telp")   no_telp: String,
//        @Field("token")     token: String
//    ): Single<RegisterResponse>
//
//    @POST("logout.php")
//    @FormUrlEncoded fun logout(
//        @Field("auth")      auth: String,
//        @Field("id_user")   id_user: String
//    ): Single<BaseResponse>
//
//    @POST("activation.php")
//    @FormUrlEncoded fun reActivate(
//        @Field("re")   re: String,
//        @Field("id_user") idUser: String
//    ): Single<BaseResponse>
//
//    @GET("pdf.php")
//    @Streaming fun getPdf(
//        @Query("auth")      auth: String,
//        @Query("file_name") model: String
//    ): Single<Response<ResponseBody>>
//
//    @POST("pengecekan.php")
//    @Multipart fun pengecekan(
//        @Part auth:                     MultipartBody.Part,
//        @Part waktu_datang:             MultipartBody.Part,
//        @Part sesi:                     MultipartBody.Part,
//        @Part id_user:                  MultipartBody.Part,
//        @Part identitas_pemohon:        MultipartBody.Part,
//        @Part identitas_kuasa:          MultipartBody.Part,
//        @Part identitas_bidang_tanah:   MultipartBody.Part,
//        @Part ttd:                      MultipartBody.Part
//    ): Single<BaseResponse>
//
//    @POST("roya.php")
//    @Multipart fun roya(
//        @Part auth:                     MultipartBody.Part,
//        @Part waktu_datang:             MultipartBody.Part,
//        @Part sesi:                     MultipartBody.Part,
//        @Part id_user:                  MultipartBody.Part,
//        @Part identitas_pemohon:        MultipartBody.Part,
//        @Part identitas_pemberi_ht:     MultipartBody.Part,
//        @Part identitas_kuasa:          MultipartBody.Part,
//        @Part identitas_bidang_tanah:   MultipartBody.Part,
//        @Part identitas_sertifikat_ht:  MultipartBody.Part,
//        @Part sk_roya:                  MultipartBody.Part,
//        @Part ttd:                      MultipartBody.Part
//    ): Single<BaseResponse>
//
//    @GET("notifikasi.php")
//    fun getNotifikasi(
//        @Query("auth")      auth: String,
//        @Query("id_user")   id_user: String
//    ): Single<MutableList<NotifikasiData>>
//
//    @GET("history.php")
//    fun gethistory(
//        @Query("auth")      auth: String,
//        @Query("id_user")   id_user: String
//    ): Single<MutableList<PermohonanData>>
//
//    @GET("history.php")
//    fun getDetailHistory(
//            @Query("auth")          auth: String,
//            @Query("id_permohonan") id_permohonan: String
//    ): Single<DetailPermohonanData>
//
//    @POST("bayar.php")
//    @FormUrlEncoded fun bayar(
//        @Field("auth")          auth: String,
//        @Field("bukti")         bukti: String,
//        @Field("id_permohonan") id_permohonan: String
//    ): Single<BaseResponse>
//
//    @GET("wilayah.php") fun getProv(): Single<MutableList<WilayahData>>
//
//    @GET("wilayah.php") fun getKab(@Query("province_id") idProv: String): Single<MutableList<WilayahData>>
//
//    @GET("wilayah.php") fun getKec(@Query("regency_id") id_kab: String): Single<MutableList<WilayahData>>
//
//    @GET("wilayah.php") fun getKel(@Query("district_id") id_kec: String): Single<MutableList<WilayahData>>
//
//    @POST("profil.php")
//    @FormUrlEncoded fun editProfil(
//        @Field("auth")          auth: String,
//        @Field("id_user")       id_user: String,
//        @Field("nama")          nama: String,
//        @Field("tempat_lahir")  tempat_lahir: String,
//        @Field("tgl_lahir")     tgl_lahir: String,
//        @Field("alamat")        alamat: String,
//        @Field("pekerjaan")     pekerjaan: String,
//        @Field("no_telp")       no_telp: String
//    ): Single<UserData>
//}
