//package ib.ganz.sipp_on.network
//
//import android.webkit.MimeTypeMap
//import ib.ganz.sipp_on.dataclass.*
//import ib.ganz.sipp_on.helper.Xecurity
//import ib.ganz.sipp_on.manager.SessionManager
//import io.reactivex.Single
//import io.reactivex.android.schedulers.AndroidSchedulers
//import io.reactivex.schedulers.Schedulers
//import okhttp3.MultipartBody
//import okhttp3.ResponseBody
//import retrofit2.Response
//
//object Srv {
//
//    private val service: IServices by lazy { ApiClient.retrofit.create(IServices::class.java) }
//
//    fun login(nik: String): Single<LoginResponse> = service
//        .login(Xecurity.getAuth(), nik, SessionManager.token)
//        .subscribeOn(Schedulers.io())
//        .observeOn(AndroidSchedulers.mainThread())
//
//    fun register(nik: String, email: String, noTelp: String): Single<RegisterResponse> = service
//        .register(Xecurity.getAuth(), nik, email, noTelp, SessionManager.token)
//        .subscribeOn(Schedulers.io())
//        .observeOn(AndroidSchedulers.mainThread())
//
//    fun logout(): Single<BaseResponse> = service
//        .logout(Xecurity.getAuth(), SessionManager.idUser)
//        .subscribeOn(Schedulers.io())
//        .observeOn(AndroidSchedulers.mainThread())
//
//    fun reActivate(): Single<BaseResponse> = service
//        .reActivate(Xecurity.getAuth(), SessionManager.idUser)
//        .subscribeOn(Schedulers.io())
//        .observeOn(AndroidSchedulers.mainThread())
//
//    fun getPdf(fileName: String): Single<Response<ResponseBody>> = service
//        .getPdf(Xecurity.getAuth(), fileName)
//        .subscribeOn(Schedulers.io())
//        .observeOn(AndroidSchedulers.mainThread())
//
//    fun pengecekan(
//        waktu_datang: String,
//        sesi: String,
//        identitas_pemohon: String,
//        identitas_kuasa: String,
//        identitas_bidang_tanah: String,
//        ttd: String
//    ): Single<BaseResponse> = service
//        .pengecekan(
//            MultipartBody.Part.createFormData("auth", Xecurity.getAuth()),
//            MultipartBody.Part.createFormData("waktu_datang", waktu_datang),
//            MultipartBody.Part.createFormData("sesi", sesi),
//            MultipartBody.Part.createFormData("id_user", SessionManager.idUser),
//            MultipartBody.Part.createFormData("identitas_pemohon", identitas_pemohon),
//            MultipartBody.Part.createFormData("identitas_kuasa", identitas_kuasa),
//            MultipartBody.Part.createFormData("identitas_bidang_tanah", identitas_bidang_tanah),
//            MultipartBody.Part.createFormData("ttd", ttd)
//        )
//        .subscribeOn(Schedulers.io())
//        .observeOn(AndroidSchedulers.mainThread())
//
//    fun roya(
//        waktu_datang: String,
//        sesi: String,
//        identitas_pemohon: String,
//        identitas_pemberi_ht: String,
//        identitas_kuasa: String,
//        identitas_bidang_tanah: String,
//        identitas_sertifikat_ht: String,
//        sk_roya: String,
//        ttd: String
//    ): Single<BaseResponse> = service
//        .roya(
//            MultipartBody.Part.createFormData("auth", Xecurity.getAuth()),
//            MultipartBody.Part.createFormData("waktu_datang", waktu_datang),
//            MultipartBody.Part.createFormData("sesi", sesi),
//            MultipartBody.Part.createFormData("id_user", SessionManager.idUser),
//            MultipartBody.Part.createFormData("identitas_pemohon", identitas_pemohon),
//            MultipartBody.Part.createFormData("identitas_pemberi_ht", identitas_pemberi_ht),
//            MultipartBody.Part.createFormData("identitas_kuasa", identitas_kuasa),
//            MultipartBody.Part.createFormData("identitas_bidang_tanah", identitas_bidang_tanah),
//            MultipartBody.Part.createFormData("identitas_sertifikat_ht", identitas_sertifikat_ht),
//            MultipartBody.Part.createFormData("sk_roya", sk_roya),
//            MultipartBody.Part.createFormData("ttd", ttd)
//        )
//        .subscribeOn(Schedulers.io())
//        .observeOn(AndroidSchedulers.mainThread())
//
//    fun getMimeType(url: String?): String {
//        val extension = MimeTypeMap.getFileExtensionFromUrl(url)
//        return if (extension != null) MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension) ?: "" else ""
//    }
//
//    fun getNotifikasi(): Single<MutableList<NotifikasiData>> = service
//        .getNotifikasi(Xecurity.getAuth(), SessionManager.idUser)
//        .subscribeOn(Schedulers.io())
//        .observeOn(AndroidSchedulers.mainThread())
//
//    fun gethistory(): Single<MutableList<PermohonanData>> = service
//        .gethistory(Xecurity.getAuth(), SessionManager.idUser)
//        .subscribeOn(Schedulers.io())
//        .observeOn(AndroidSchedulers.mainThread())
//
//    fun getDetailHistory(idPermohonan: String): Single<DetailPermohonanData> = service
//        .getDetailHistory(Xecurity.getAuth(), idPermohonan)
//        .subscribeOn(Schedulers.io())
//        .observeOn(AndroidSchedulers.mainThread())
//
//    fun bayar(bukti: String, idPermohonan: String): Single<BaseResponse> = service
//        .bayar(Xecurity.getAuth(), bukti, idPermohonan)
//        .subscribeOn(Schedulers.io())
//        .observeOn(AndroidSchedulers.mainThread())
//
//    fun getProv(): Single<MutableList<WilayahData>> = service
//        .getProv()
//        .subscribeOn(Schedulers.io())
//        .observeOn(AndroidSchedulers.mainThread())
//
//    fun getKab(idProv: String): Single<MutableList<WilayahData>> = service
//        .getKab(idProv)
//        .subscribeOn(Schedulers.io())
//        .observeOn(AndroidSchedulers.mainThread())
//
//    fun getKec(idKab: String): Single<MutableList<WilayahData>> = service
//        .getKec(idKab)
//        .subscribeOn(Schedulers.io())
//        .observeOn(AndroidSchedulers.mainThread())
//
//    fun getKel(idKec: String): Single<MutableList<WilayahData>> = service
//        .getKel(idKec)
//        .subscribeOn(Schedulers.io())
//        .observeOn(AndroidSchedulers.mainThread())
//
//    fun editProfil(nama: String, tempat_lahir: String, tgl_lahir: String, alamat: String, pekerjaan: String, no_telp: String): Single<UserData> = service
//        .editProfil(Xecurity.getAuth(), SessionManager.idUser, nama, tempat_lahir, tgl_lahir, alamat, pekerjaan, no_telp)
//        .subscribeOn(Schedulers.io())
//        .observeOn(AndroidSchedulers.mainThread())
//}