package com.doctoraak.doctoraakdoctor.ui

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import com.doctoraak.doctoraakdoctor.R
import com.doctoraak.doctoraakdoctor.databinding.DialogPlacePickerBinding
import com.doctoraak.doctoraakdoctor.utils.Utils
import com.google.android.gms.common.api.Status
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.AutocompleteSupportFragment
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener
import com.google.android.material.snackbar.Snackbar
import java.io.IOException
import java.util.*

class PlacePickerDialog : DialogFragment(), OnMapReadyCallback, GoogleMap.OnMapLongClickListener
{
    private lateinit var binding: DialogPlacePickerBinding
    private var googleMap: GoogleMap? = null
    private var autocompleteSupportFragment: AutocompleteSupportFragment? = null
    private var selectedMarker: Marker? = null
    internal var listener: onPlacePickerListener? = null
    private var is_moved_to_current_location = false
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var selectedLatt: Double = -1.toDouble()
    private var selectedLong: Double = -1.toDouble()

    companion object
    {
        internal val TAG = "PlacePickerDialog"
        private val RESTORE_MAP_VIEW = "RESTORE_MAP_VIEW"
        private val RESTORE_SELECTED_LATT = "RESTORE_SELECTED_LATT"
        private val RESTORE_SELECTED_LONG = "RESTORE_SELECTED_LONG"
        private val REQ_CODE_LOCATION_PERMISSION = 5

        /** always Call SetListener(); */
        @JvmStatic
        fun newInstance(): PlacePickerDialog
        {
            val dialog = PlacePickerDialog()
            dialog.setStyle(STYLE_NORMAL, R.style.FullScreenDialogTheme)
            return dialog
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        binding = DataBindingUtil.inflate(inflater
            , R.layout.dialog_place_picker, container, false)

        var mapViewBundle: Bundle? = null
        if (savedInstanceState != null)
        {
            mapViewBundle = savedInstanceState.getBundle(RESTORE_MAP_VIEW)

            selectedLatt = savedInstanceState.getDouble(RESTORE_SELECTED_LATT, -1.toDouble())
            selectedLong = savedInstanceState.getDouble(RESTORE_SELECTED_LONG, -1.toDouble())
        }

        binding.btnOk.setOnClickListener {onPickLocationClick()}
        binding.fabCurrentLocation.setOnClickListener {requestCurrentLocation()}
        binding.ivClose.setOnClickListener {onCloseDialogClick()}

        binding.mapView.onCreate(mapViewBundle)
        binding.mapView.getMapAsync(this)

        initLocationRequest()
        setUpAutoComplete()

        return binding.root
    }

    private fun setUpAutoComplete()
    {
        if (!Places.isInitialized())
            Places.initialize(context!!, getString(R.string.map_api_key))

        autocompleteSupportFragment =
            fragmentManager!!.findFragmentById(R.id.autocomplete_fragment) as AutocompleteSupportFragment?
        autocompleteSupportFragment!!.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.ADDRESS, Place.Field.LAT_LNG))
        autocompleteSupportFragment!!.setOnPlaceSelectedListener(object : PlaceSelectionListener {
            override fun onPlaceSelected(place: Place) {
                Log.d("saif", "PlacePickerDialog: onPlaceSelected: $place")
                val latLng = place.latLng
                if (latLng != null) {
                    googleMap!!.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 12f))
                    selectLocation(latLng)
                }
            }
            override fun onError(status: Status) {
                Log.d("saif", "PlacePickerDialog: onError: $status")
            }
        })
    }

    private fun initLocationRequest()
    {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(activity!!)
    }

    private fun requestCurrentLocation()
    {
        if (ContextCompat.checkSelfPermission(activity!!, Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED)
        {
            val requestPermission = {
                ActivityCompat.requestPermissions(activity!!, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION)
                    , REQ_CODE_LOCATION_PERMISSION)
            }

            if (ActivityCompat.shouldShowRequestPermissionRationale(activity!!, Manifest.permission.ACCESS_FINE_LOCATION))
                AlertDialog.Builder(activity!!)
                    .setTitle(R.string.request_location_permission)
                    .setMessage(R.string.this_permission_is_required_for_getting_location)
                    .setPositiveButton(R.string.ok, { dialog, which -> requestPermission()})
                    .setNegativeButton(R.string.cancel, { dialog, which -> })
                    .show()
            else
                requestPermission()
        }
        else
            getCurrentLocation()
    }


    @SuppressLint("MissingPermission")
    private fun getCurrentLocation()
    {
        fusedLocationClient.lastLocation.addOnSuccessListener { lastLocation: Location? ->
            if (googleMap != null && lastLocation != null)
            {
                val latLng = LatLng(lastLocation.latitude, lastLocation.longitude)
                googleMap!!.moveCamera(CameraUpdateFactory
                    .newLatLngZoom(latLng, 18f))

                selectLocation(latLng)
            }
            else Snackbar.make(binding.viewGroup, getString(R.string.couldnt_get_current_location), Snackbar.LENGTH_SHORT).show()

        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray)
    {
        if (requestCode == REQ_CODE_LOCATION_PERMISSION) {
            if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                getCurrentLocation()
        }
    }

    private fun onCloseDialogClick() = dismiss()

    private fun onPickLocationClick()
    {
        if (selectedMarker == null) {
            Toast.makeText(activity, getString(R.string.no_location_was_selected), Toast.LENGTH_SHORT).show()
            return
        }

        if (listener == null) return

        if (Utils.checkInternetConnection(context!!))
        {
            try {
                val address = convertLatLongToAddress(selectedMarker!!.position)
                listener!!.onLocationSelected(address, selectedMarker!!.position)
                dismiss()
            } catch (e: IOException) {
                Log.e("saif", "convertLatLongToAddress: ", e)
                Snackbar.make(binding.viewGroup, R.string.io_exception_cant_get_location, Snackbar.LENGTH_SHORT).show()
            }
        } else
            Snackbar.make(binding.viewGroup, R.string.no_internet_connection, Snackbar.LENGTH_SHORT).show()
    }

    private fun convertLatLongToAddress(lng: LatLng): String
    {
        val geocoder = Geocoder(context, Locale.getDefault())
        val addressList = geocoder.getFromLocation(lng.latitude, lng.longitude, 1)
        val builder = StringBuilder()
        if (addressList != null && addressList.size > 0) {
            val address = addressList[0]

            for (i in 0 until address.maxAddressLineIndex) {
                builder.append(
                    if (TextUtils.isEmpty(address.getAddressLine(i)))
                        ""
                    else address.getAddressLine(i)
                )
            }
            builder.append(if (TextUtils.isEmpty(address.locality)) "" else address.locality + ", ")
                .append(if (TextUtils.isEmpty(address.subAdminArea)) "" else address.subAdminArea + ", ")
                .append(if (TextUtils.isEmpty(address.adminArea)) "" else address.adminArea + ", ")
                .append(if (TextUtils.isEmpty(address.countryName)) "" else address.countryName)

        }
        Log.d(TAG, "convertLatLongToAddress: address= $builder")

        return builder.toString()
    }

    override fun onMapReady(googleMap: GoogleMap)
    {
        this.googleMap = googleMap
        // this is the location of egypt just in case of any current location error.
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(26.9060999, 30.8768375), 6f))

        googleMap.setOnMapLongClickListener(this)

        if (selectedLatt != -1.toDouble())
        {
            selectedMarker = googleMap.addMarker(MarkerOptions()
                    .position(LatLng(selectedLatt, selectedLong)))
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(selectedLatt, selectedLong), 18f))
            return
        }

        if (!is_moved_to_current_location) {
            requestCurrentLocation()
            is_moved_to_current_location = true
        }
    }

    override fun onMapLongClick(latLng: LatLng)
    {
        selectLocation(latLng)
    }

    private fun selectLocation(latLng: LatLng)
    {
        if (selectedMarker == null)
            selectedMarker = googleMap!!.addMarker(MarkerOptions().position(latLng))
        else
            selectedMarker!!.setPosition(latLng)
    }

    override fun onSaveInstanceState(outState: Bundle)
    {
        var mapViewBundle = outState.getBundle(RESTORE_MAP_VIEW)
        if (mapViewBundle == null) {
            mapViewBundle = Bundle()
            outState.putBundle(RESTORE_MAP_VIEW, mapViewBundle)
        }

        binding.mapView.onSaveInstanceState(mapViewBundle)

        selectedMarker?.let {
            outState.putDouble(RESTORE_SELECTED_LATT, it.position.latitude)
            outState.putDouble(RESTORE_SELECTED_LONG, it.position.longitude)
        }
        super.onSaveInstanceState(outState)
    }


    override fun onResume() {
        super.onResume()

        binding.mapView.onResume()
        Snackbar.make(binding.viewGroup, getString(R.string.long_press_to_select_location_on_map), Snackbar.LENGTH_LONG).show()
    }

    override fun onStart() {
        super.onStart()
        binding.mapView.onStart()
    }

    override fun onStop() {
        super.onStop()
        binding.mapView.onStop()
    }

    override fun onPause() {
        binding.mapView.onPause()
        super.onPause()
    }

    override fun onDestroyView() {
        binding.mapView.onDestroy()
        autocompleteSupportFragment?.let {
            it.fragmentManager!!.beginTransaction().remove(it).commit()
        }
        super.onDestroyView()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        binding.mapView.onLowMemory()
    }


    interface onPlacePickerListener
    {
        fun onLocationSelected(address: String, position: LatLng)
    }
}
