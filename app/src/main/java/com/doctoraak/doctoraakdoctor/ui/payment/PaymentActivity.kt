package com.doctoraak.doctoraakdoctor.ui.payment

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.text.TextUtils
import android.util.Log
import android.widget.NumberPicker
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.viewpager.widget.ViewPager
import com.doctoraak.doctoraakdoctor.Adapter.PaymentPagerAdapter
import com.doctoraak.doctoraakdoctor.R
import com.doctoraak.doctoraakdoctor.Repository.Local.SessionManager
import com.doctoraak.doctoraakdoctor.databinding.ActivityPaymentBinding
import com.doctoraak.doctoraakdoctor.model.BillingData
import com.doctoraak.doctoraakdoctor.model.CardPaymentKeyRequest
import com.doctoraak.doctoraakdoctor.model.OrderRegisterationRequest
import com.doctoraak.doctoraakdoctor.ui.BaseActivity
import com.doctoraak.doctoraakdoctor.ui.DialogType
import com.doctoraak.doctoraakdoctor.ui.SweetDialog
import com.doctoraak.doctoraakdoctor.ui.doctorHome.DoctorHomeActivity
import com.doctoraak.doctoraakdoctor.ui.labHome.LabHomeActivity
import com.doctoraak.doctoraakdoctor.ui.pharmacyHome.PharmacyHomeActivity
import com.doctoraak.doctoraakdoctor.ui.radiologyHome.RadiologyHomeActivity
import com.doctoraak.doctoraakdoctor.utils.*
import com.example.androidsdk.IntentKeys
import com.example.androidsdk.Pay



class PaymentActivity : BaseActivity()
{
    private val viewModel by lazy { ViewModelProviders.of(this).get(PaymentViewModel::class.java) }
    private lateinit var binding: ActivityPaymentBinding

    companion object{
        private const val ID_INTENT = "ID_INTENT"
        private const val USER_TYPE_INTENT = "USER_TYPE_INTENT"

        fun launch(context: Context, id: Long, userType: UserType) =
            with(Intent(context, PaymentActivity::class.java))
            {
                putExtra(ID_INTENT, id)
                putExtra(USER_TYPE_INTENT, userType)
                context.startActivity(this)
            }
    }

    private var id: Long = 0
    private lateinit var userType: UserType
    private var Endpoint = "https://accept.paymobsolutions.com/api/acceptance/post_pay"
    private var IframeID = 36278
    private var PAYMENT_REQUEST_CODE = 54
    private var authToken = ""
    private val PAYMENT_AMOUNT = 20000
    private var success = ""


    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_payment)

        binding.lifecycleOwner = this
        binding.clickHandler = PaymentClickHandler()

        id = intent.getLongExtra(ID_INTENT, 0)
        userType = intent.getSerializableExtra(USER_TYPE_INTENT) as UserType

        observeData()

        if (savedInstanceState == null)
            viewModel.getPhone()

        initViews()
    }

    private fun initViews()
    {
        binding.viewPager.adapter = PaymentPagerAdapter()
        binding.viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {}
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}
            override fun onPageSelected(position: Int)
            {
                when(position)
                {
                    0 -> { // vodafone :
                        vodaphoneSelected()
                    }
                    1 -> { // fawry:
                        fawrySelected()
                    }
                    2 -> { // Visa:
                        visaSelected()
                    }
                }
            }
        })
    }

    private fun visaSelected()
    {
        if (getUserEmail() != null)
        {
            with(binding.etCode)
            {
                show()
                isEnabled = true
                setText("")
                hint = getString(R.string.email)
                inputType = InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS
            }
        }
        else
            binding.etCode.hide()

        binding.btnPayVisa.show()
    }

    private fun getUserEmail() = when (SessionManager.getUserType()) {
        UserType.PHARMACY -> SessionManager.getPharmacyData().also {
            it.email
        }
        UserType.LAB -> SessionManager.getLabData().also {
            it.email
        }
        UserType.RADIOLOGY -> SessionManager.getRadiologyData().also {
            it.email
        }
        else -> SessionManager.getDoctorData().also {
            it.email
        }
    }

    private fun vodaphoneSelected()
    {
        with(binding.etCode)
        {
            show()
            isEnabled = false
            setText(viewModel.phone.value?.phone ?: "")
            hint = getString(R.string.phone_number)
            error = null
            inputType = InputType.TYPE_NUMBER_VARIATION_NORMAL
        }

        binding.btnPayVisa.hide()
    }

    private fun fawrySelected()
    {
        with(binding.etCode)
        {
            show()
            isEnabled = true
            setText("")
            hint = getString(R.string.enter_code)
            inputType = InputType.TYPE_CLASS_NUMBER
        }
        binding.btnPayVisa.hide()
    }

    private fun startLoading()
    {
        binding.btnDone.isEnabled = false
        binding.loading.loadingView.show()
    }

    private fun stopLoading()
    {
        binding.btnDone.isEnabled = true
        binding.loading.loadingView.hide()
    }

    fun observeData()
    {
        viewModel.isLoading.observe(this, Observer {
            if (it) startLoading() else stopLoading()
        })

        viewModel.errorInt.observe(this, Observer {
            it.showError(this)
        })

        viewModel.errorMsg.observe(this, Observer { toast(it) })

        viewModel.phone.observe(this, Observer {
            if (binding.viewPager.currentItem == 0)
                binding.etCode.setText(it.phone ?: "")
        })

        viewModel.isFawry.observe(this, Observer {
            finishPayment()
        })

        viewModel.paymentAuthResponse.observe(this , Observer {
            authToken = it.token
            val request = OrderRegisterationRequest(authToken = it.token , merchantId = "${it.profile.id}"
                , amountCents = PAYMENT_AMOUNT.toString() , deliveryNeeded = "false")
            viewModel.orderRegisteration(request)
        })

        viewModel.orderRegisterationResponse.observe(this , Observer {
            var email: String
            var name: String
            var phone: String

            when (SessionManager.getUserType()!!) {
                UserType.PHARMACY -> SessionManager.getPharmacyData().also {
                    email = it.email
                    name = it.name
                    phone = it.phone
                }
                UserType.LAB -> SessionManager.getLabData().also {
                    email = it.email
                    name = it.name
                    phone = it.phone
                }
                UserType.RADIOLOGY -> SessionManager.getRadiologyData().also {
                    email = it.email
                    name = it.name
                    phone = it.phone
                }
                else -> SessionManager.getDoctorData().also {
                    email = it.email
                    name = it.name
                    phone = it.phone
                }
            }

            Log.d(" name ${name.split(" ")[0]}","okhttp")

            if (TextUtils.isEmpty(email))
                email = binding.etCode.text.toString()

            val billingData = BillingData(apartment = "NA" , email = email , floor = "NA" , firstName = name.split(" ")[0],
                street = "NA" , building = "NA" , phoneNumber = phone , shippingMethod = "NA" ,
                postalCode = "NA" , city = "NA" , country = "NA" , lastName = name.split(" ")[1]
                , state = "NA")
            val request = CardPaymentKeyRequest(authToken = authToken , integrationId = 21479, billingData = billingData,
                amountCents = PAYMENT_AMOUNT.toString() , expiration = 3600 , orderId = it.id , lockOrderWhenPaid = "false")
            viewModel.getCardPaymentToken(request)
        })

        viewModel.paymentTokenResponse.observe(this , Observer {
            openPayment(it.token)
        })

    }

    private fun openPayment(token : String){
        val payIntent = Intent(this, Pay::class.java)
        payIntent.putExtra(IntentKeys.PAYMENT_KEY, token)
        payIntent.putExtra(IntentKeys.ENDPOINT_URL, Endpoint)
        payIntent.putExtra(IntentKeys.IFRAMEID.toString(), IframeID)
        startActivityForResult(payIntent, PAYMENT_REQUEST_CODE)
    }

    private fun finishPayment()
    {
        when (userType)
        {
            UserType.DOCTOR -> startActivity(Intent(this@PaymentActivity
                , DoctorHomeActivity::class.java))
            UserType.LAB -> startActivity(Intent(this@PaymentActivity
                , LabHomeActivity::class.java))
            UserType.PHARMACY -> startActivity(Intent(this@PaymentActivity
                , PharmacyHomeActivity::class.java))
            UserType.RADIOLOGY -> startActivity(Intent(this@PaymentActivity
                , RadiologyHomeActivity::class.java))
        }
        finish()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?)
    {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 1)
        {
            if (resultCode == Activity.RESULT_OK) {
                var success = data?.getStringExtra("success")
//                Id = data?.getStringExtra("ID")
//                amount_cents = data?.getStringExtra("amount_cents")
//                integration_id = data?.getStringExtra("integration_id")
//                has_parent_transaction = data?.getStringExtra("has_parent_transaction")
//                txn_response_code = data?.getStringExtra("txn_response_code")
//                acq_response_code = data?.getStringExtra("acq_response_code")

                val Succeful = "true"
                val Declined = "false"
            }
        }
    }

    inner class PaymentClickHandler
    {
        fun onDone(code: String)
        {
            if (binding.viewPager.currentItem  == 1)
            {
                if (code.validateEmpty(binding.etlCode))
                    viewModel.confirmCode()
            }
            else
            {
                SweetDialog.newInstance(this@PaymentActivity, DialogType.WARNING).apply {
                    show()
                    setMessage(getString(R.string.your_account_not_active__send_fees_on_specified_number_to_active_account))
                    setOkClickListener {
                        it.dismiss()
                        finishPayment()
                    }
                    setCancelClickListener {
                        it.dismiss()
                    }
                }
            }
        }

        fun onOpenVisaClick()
        {
            if (getUserEmail() == null && !binding.etCode.text.toString().validateEmail(binding.etlCode))
                return

            viewModel.paymentAuth(getString(R.string.we_accept_api_key))
        }

        fun onVodaphonecashClick()
        {
            val index = binding.viewPager.currentItem
            if (index != 0) {
                if (index == 2){
                    binding.motionLayout.setTransition(R.id.start3, R.id.end3)
                    binding.motionLayout.transitionToEnd()
                }else{
                    binding.motionLayout.setTransition(R.id.end1 , R.id.start1)
                    binding.motionLayout.transitionToEnd()
                }
            }
            binding.viewPager.currentItem = 0
            vodaphoneSelected()
        }

        fun onFaweryClick()
        {
            val index = binding.viewPager.currentItem

            if (index != 1) {
                if (index == 0) {
                    binding.motionLayout.setTransition(R.id.start1, R.id.end1)
                    binding.motionLayout.transitionToEnd()
                }else{
                    binding.motionLayout.setTransition(R.id.end2 ,R.id.start2 )
                    binding.motionLayout.transitionToEnd()
                }
            }

            binding.viewPager.currentItem = 1
            fawrySelected()
        }

        fun onVisaClick()
        {
            val index = binding.viewPager.currentItem
            if (index != 2) {
                if (index == 1) {
                    binding.motionLayout.setTransition(R.id.start2, R.id.end2)
                    binding.motionLayout.transitionToEnd()
                }else{
                    binding.motionLayout.setTransition(R.id.end3 , R.id.start3)
                    binding.motionLayout.transitionToEnd()
                }
            }

            binding.viewPager.currentItem = 2
            visaSelected()
        }

        fun onSkip()
        {
            finishPayment()
        }
    }


}
