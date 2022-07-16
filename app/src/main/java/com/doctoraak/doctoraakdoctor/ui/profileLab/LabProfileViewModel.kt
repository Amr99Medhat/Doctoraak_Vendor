package com.doctoraak.doctoraakdoctor.ui.profileLab

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.doctoraak.doctoraakdoctor.model.*
import com.doctoraak.doctoraakdoctor.utils.SingleLiveData

class LabProfileViewModel : ViewModel()
{
    val lab by lazy { MutableLiveData<Lab>(Lab()) }
    val updateProfile by lazy { SingleLiveData<LabResponse>() }
    val isLoading by lazy { SingleLiveData<Boolean>() }
    val errorMsg by lazy { SingleLiveData<String>() }
    val errorInt by lazy { SingleLiveData<Pair<Int, String?>>() }

    fun updateProfileLab(lab: Lab) = LabProfileRepository.updateProfileLab(lab
        , success = { model: LabResponse ->
            isLoading.setValue(false)
            updateProfile.setValue(model)}
        , loading = { isLoading.setValue(true) }
        , errorMsg = { errorMsg.setValue(it); isLoading.setValue(false) }
        , error = {i, s ->
            errorInt.setValue(Pair(i, s)); isLoading.setValue(false)})

}