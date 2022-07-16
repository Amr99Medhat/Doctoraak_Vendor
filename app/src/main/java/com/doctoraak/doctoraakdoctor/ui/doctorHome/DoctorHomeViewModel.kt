package com.doctoraak.doctoraakdoctor.ui.doctorHome

import android.text.format.DateUtils
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.doctoraak.doctoraakdoctor.model.*
import com.doctoraak.doctoraakdoctor.utils.DoctorReservationType
import com.doctoraak.doctoraakdoctor.utils.SingleLiveData
import java.text.SimpleDateFormat
import java.util.*

class DoctorHomeViewModel : ViewModel()
{
    val selectedReservationFilter by lazy { MutableLiveData<DoctorReservationFilter>(DoctorReservationFilter()) }
    val allClinics by lazy { SingleLiveData<AllClinicsResponse>() }
    private val clinicOrders by lazy { SingleLiveData<ReservationResponse>() }
    val isLoading by lazy { SingleLiveData<Boolean>() }
    val errorMsg by lazy { SingleLiveData<String>() }
    val errorInt by lazy { SingleLiveData<Pair<Int, String?>>() }
    val clinicAvailability by lazy { SingleLiveData<BaseResponse>() }
    val updateWorkingHour by lazy { SingleLiveData<BaseResponse>() }


    val clinicReservationFiltered = Transformations.switchMap(selectedReservationFilter) { filter ->
        Log.d("saif", "clinicReservationFiltered   filter= ${filter.type}  ,  ${filter.date}")
        if (selectedReservationFilter.value?.type == DoctorReservationType.ALL
            && selectedReservationFilter.value?.date.isNullOrEmpty())
            MutableLiveData<List<Reservation>>(clinicOrders.value?.data)
        else
            MutableLiveData<List<Reservation>>(clinicOrders.value?.data?.filter {

                Log.d("saif", "clinicReservationFiltered: filter is= ${(it.date == filter.date)}")
                (filter.type == DoctorReservationType.ALL || it.type == filter.type.value)
                        && (filter.date.isNullOrEmpty() || it.date == filter.date)
            })
    }

    fun getClinicOrders(api_token: String, doctor_id: Long, clinic_id: Long)
            = DoctorHomeRepository.getClinicOrders(api_token, doctor_id, clinic_id
        , success = { model: ReservationResponse ->
            isLoading.setValue(false)
            clinicOrders.setValue(model)
            selectedReservationFilter.value = selectedReservationFilter.value?.clearFilter()
        }
        , loading = { isLoading.setValue(true) }
        , errorMsg = { errorMsg.setValue(it); isLoading.setValue(false) }
        , error = {i, s ->
            errorInt.setValue(Pair(i, s)); isLoading.setValue(false)})

    fun getAllClinics(api_token: String, doctor_id: Long)
            = DoctorHomeRepository.getAllClinics(api_token, doctor_id
        , success = { model: AllClinicsResponse ->
            isLoading.setValue(false)
            allClinics.setValue(model)}
        , loading = { isLoading.setValue(true) }
        , errorMsg = { errorMsg.setValue(it); isLoading.setValue(false) }
        , error = {i, s ->
            errorInt.setValue(Pair(i, s)); isLoading.setValue(false)})

    fun updateClinicWorkingHours(api_token: String, clinic_id: Long, doctor_id: Long, shiftWorkingHours: List<ShiftWorkingHour>)
            = DoctorHomeRepository.updateClinicWorkingHours(api_token, clinic_id, doctor_id, shiftWorkingHours
        , success = { model: BaseResponse ->
            isLoading.setValue(false)
            updateWorkingHour.setValue(model)}
        , loading = { isLoading.setValue(true) }
        , errorMsg = { errorMsg.setValue(it); isLoading.setValue(false) }
        , error = {i, s ->
            errorInt.setValue(Pair(i, s)); isLoading.setValue(false)})


}