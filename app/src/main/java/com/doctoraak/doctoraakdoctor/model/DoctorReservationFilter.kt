package com.doctoraak.doctoraakdoctor.model

import com.doctoraak.doctoraakdoctor.utils.DoctorReservationType

class DoctorReservationFilter(var date: String? = null,
                              var type: DoctorReservationType = DoctorReservationType.ALL) {

    fun clearFilter(): DoctorReservationFilter
    {
        date = null
        type = DoctorReservationType.ALL
        return this
    }
}