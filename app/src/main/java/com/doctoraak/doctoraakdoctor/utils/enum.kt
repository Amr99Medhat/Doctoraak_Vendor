package com.doctoraak.doctoraakdoctor.utils

import java.io.Serializable

enum class UserType : Serializable
{
    DOCTOR, PHARMACY, LAB, RADIOLOGY, MEDICAL_CENTER, OPTICAL_CENTER, HOSPITAL
}

enum class Gender(val value: String) : Serializable
{
    MALE("male"), FEMALE("female")
}

enum class Language : Serializable
{
    ENGLISH, ARABIC
}

enum class DoctorReservationType(val value: Int) : Serializable
{
    ALL(-1)/*all not in the server*/
    , NEW(1), CONSULTATION(2), CONTINUE(3);

    companion object {
        fun getType(value: Int) = when (value) {
            NEW.value -> NEW
            CONSULTATION.value -> CONSULTATION
            CONTINUE.value -> CONTINUE
            else -> ALL
        }
    }

}