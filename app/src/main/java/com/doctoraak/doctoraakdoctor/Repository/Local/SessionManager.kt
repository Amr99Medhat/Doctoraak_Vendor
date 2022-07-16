package com.doctoraak.doctoraakdoctor.Repository.Local

import android.content.Context
import android.content.SharedPreferences
import android.net.ConnectivityManager
import com.doctoraak.doctoraakdoctor.R
import com.doctoraak.doctoraakdoctor.model.*
import com.doctoraak.doctoraakdoctor.utils.Language
import com.doctoraak.doctoraakdoctor.utils.UserType
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class SessionManager(context: Context)
{
    init {
        pref = context.getSharedPreferences("Doctoraak", Context.MODE_PRIVATE)
    }

    companion object
    {
        private const val CITY_KEY: String = "CITY_KEY"
        private const val AREA_KEY: String = "AREA_KEY"
        private const val CONTACT_KEY: String = "CONTACT_KEY"
        private const val SPECIALIZATION_KEY: String = "SPECIALIZATION_KEY"
        private const val INSURANCE_KEY: String = "INSURANCE_KEY"
        private const val DEGREE_KEY: String = "DEGREE_KEY"

        private const val USER_DATA_KEY: String = "USER_DATA"
        private const val USER_TYPE_KEY: String = "USER_TYPE"
        private const val USER_API_TOKEN_KEY: String = "USER_API_TOKEN"
        private const val USER_ID_KEY: String = "USER_ID_KEY"
        private const val LANGUAGE_KEY: String = "LANGUAGE_KEY"
        private const val USER_REGISTER_MOBILE_VERIFICATION_MODE: String = "USER_REGISTER_MOBILE_VERIFICATION_MODE"

        private lateinit var pref: SharedPreferences

        fun isLogIn() = pref.contains(
            USER_DATA_KEY
        )

        fun getLanguage(): String = pref.getString(
            LANGUAGE_KEY, Language.ARABIC.name)!!

        fun saveLanguage(lang: Language) = pref.edit()
            .putString(LANGUAGE_KEY, lang.name)
            .apply()

        fun setUserType(userType: UserType) = pref.edit()
            .putString(USER_TYPE_KEY, userType.name).apply()

        fun logIn(userType: UserType, user: User) = pref.edit()
            .putString(USER_TYPE_KEY, userType.name)
            .putString(USER_API_TOKEN_KEY, user.api_token)
            .putLong(USER_ID_KEY, user.id)
            .putString(USER_DATA_KEY, Gson().toJson(user))
            .remove(USER_REGISTER_MOBILE_VERIFICATION_MODE)
            .apply()

        fun getDoctorData() = Gson().fromJson(
            pref.getString(
                USER_DATA_KEY, ""), Doctor::class.java)

        fun getPharmacyData() = Gson().fromJson(
            pref.getString(
                USER_DATA_KEY, ""), Pharmacy::class.java)

        fun getLabData() = Gson().fromJson(
            pref.getString(
                USER_DATA_KEY, ""), Lab::class.java)

        fun getRadiologyData() = Gson().fromJson(
            pref.getString(
                USER_DATA_KEY, ""), Radiology::class.java)

        fun setUserIdForRegisterMobileVerificationMode(user_id: Long) = pref.edit()
            .putLong(USER_REGISTER_MOBILE_VERIFICATION_MODE, user_id)
            .apply()

        fun getUserIdForRegisterMobileVerificationMode() = pref.getLong(
            USER_REGISTER_MOBILE_VERIFICATION_MODE, -1L)

        fun clearUserIdForRegisterMobileVerificationMode() = pref.edit()
            .remove(USER_REGISTER_MOBILE_VERIFICATION_MODE)
            .apply()

        fun getUserType() = pref.getString(
            USER_TYPE_KEY, "")?.let { UserType.valueOf(it) }

        fun getApiToken() = pref.getString(
            USER_API_TOKEN_KEY, "")!!

        fun getUserId() = pref.getLong(
            USER_ID_KEY, -1)

        fun logOut() = pref.edit()
            .remove(USER_TYPE_KEY)
            .remove(USER_ID_KEY)
            .remove(USER_API_TOKEN_KEY)
            .remove(USER_DATA_KEY)
            .remove(LANGUAGE_KEY)
            .apply()

        /////////////////////////// Main Function ///////////////////////////

        fun saveSpecialization(list: List<Specialization>)
                = pref.edit().putString(
            SPECIALIZATION_KEY, Gson().toJson(list)).apply()

        fun getSpecialization(): List<Specialization>
        {
            var list: List<Specialization>? = Gson().fromJson(
                pref.getString(
                    SPECIALIZATION_KEY, "")
                , object : TypeToken<List<Specialization>>() {}.type)

            if (list == null)
                list = listOf()

            return list
        }

        fun saveDegree(list: List<Degree>)
                = pref.edit().putString(
            DEGREE_KEY, Gson().toJson(list)).apply()

        fun getDegree(): List<Degree>
        {
            var list: List<Degree>? = Gson().fromJson(
                pref.getString(
                    DEGREE_KEY, "")
                , object : TypeToken<List<Degree>>() {}.type)

            if (list == null)
                list = listOf()

            return list
        }

        fun saveInsurance(list: List<Insurance>)
                = pref.edit().putString(
            INSURANCE_KEY, Gson().toJson(list)).apply()

        fun getInsurance(): List<Insurance>
        {
            var list: List<Insurance>? = Gson().fromJson(
                pref.getString(
                    INSURANCE_KEY, "")
                , object : TypeToken<List<Insurance>>() {}.type)

            if (list == null)
                list = listOf()

            return list
        }

        fun saveCity(list: List<City>)
                = pref.edit().putString(
            CITY_KEY, Gson().toJson(list)).apply()

        fun getCity(): List<City>
        {
            var list: List<City>? = Gson().fromJson(
                pref.getString(
                    CITY_KEY, "")
                , object : TypeToken<List<City>>() {}.type)

            if (list == null)
                list = listOf()

            return list
        }

        fun saveArea(list: List<Area>)
                = pref.edit().putString(
            AREA_KEY, Gson().toJson(list)).apply()

        fun getArea(): List<Area>
        {
            var list: List<Area>? = Gson().fromJson(
                pref.getString(
                    AREA_KEY, "")
                , object : TypeToken<List<Area>>() {}.type)

            if (list == null)
                list = listOf()

            return list
        }

        fun saveContact(list: List<String>)
                = pref.edit().putString(
            CONTACT_KEY, Gson().toJson(list)).apply()

        fun getContact(): List<String>
        {
            var list: List<String>? = Gson().fromJson(
                pref.getString(
                    CONTACT_KEY, "")
                , object : TypeToken<List<String>>() {}.type)

            if (list == null)
                list = listOf()

            return list
        }

        ///////////////////////////  ///////////////  ////////////////////
    }

}