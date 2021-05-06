package com.RNA_RECORDS.petsforadoption.GiveForAdoption

import android.content.Context
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Bundle
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.RNA_RECORDS.petsforadoption.R
import com.firebase.ui.auth.AuthUI
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar

class ChooseLocation :Fragment()
{
    lateinit var map: GoogleMap
    lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    lateinit var locationRequest: LocationRequest
    lateinit var locationCallback: LocationCallback
    var snackbarGPSoff:Snackbar?=null
    var snackbarConfirmLocation:Snackbar? = null


    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if(requestCode==1)
        {
            if(grantResults.isNotEmpty() && grantResults[0]== PackageManager.PERMISSION_GRANTED)
            {
                enableMyLocation()
            }
        }
    }

    private fun isPermissionGranted():Boolean
    {
        return ContextCompat.checkSelfPermission(requireContext(),android.Manifest.permission.ACCESS_FINE_LOCATION)== PackageManager.PERMISSION_GRANTED
    }

    private val callback = OnMapReadyCallback { googleMap ->

        map = googleMap
        /**
         * Manipulates the map once available.
         * This callback is triggered when the map is ready to be used.
         * This is where we can add markers or lines, add listeners or move the camera.
         * In this case, we just add a marker near Sydney, Australia.
         * If Google Play services is not installed on the device, the user will be prompted to
         * install it inside the SupportMapFragment. This method will only be triggered once the
         * user has installed Google Play services and returned to the app.
         */
        /**
         * Manipulates the map once available.
         * This callback is triggered when the map is ready to be used.
         * This is where we can add markers or lines, add listeners or move the camera.
         * In this case, we just add a marker near Sydney, Australia.
         * If Google Play services is not installed on the device, the user will be prompted to
         * install it inside the SupportMapFragment. This method will only be triggered once the
         * user has installed Google Play services and returned to the app.
         */

        makeLocationCallback()
        checkifGPSOn()
        enableMyLocation()
        makeSelectLocationDialog()
        map.setOnMapClickListener {
            map.addMarker(MarkerOptions().position(it).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)))
            makeConfirmLocationSnackBar(it)
        }
    }

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(AuthUI.getApplicationContext())
        locationRequest = LocationRequest.create()
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        locationRequest.interval = 120000
        return inflater.inflate(R.layout.fragment_adopt_a_pet, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)
    }
    private fun enableMyLocation()
    {
        if(isPermissionGranted())
        {
            if(checkifGPSOn()) {
                fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper())

            }
            else
            {
                showSnackBar()
            }
        }else
        {
            requestPermissions(arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),1)
        }
    }
    private fun makeConfirmLocationSnackBar(latLng: LatLng)
    {
         snackbarConfirmLocation = Snackbar.make(requireActivity().findViewById(android.R.id.content), "Confirm location?", Snackbar.LENGTH_INDEFINITE).setAction("Yes"){
            requireView().findNavController().navigate(ChooseLocationDirections.actionChooseLocationToFormForAdoptionFrag(latLng))
        }
        snackbarConfirmLocation!!.show()
    }
    private fun showSnackBar()
    {
         snackbarGPSoff = Snackbar.make(requireActivity().findViewById(android.R.id.content), "Ensure that GPS is switched on", Snackbar.LENGTH_INDEFINITE).setAction("RETRY"){
            enableMyLocation()
        }
        snackbarGPSoff!!.show()
    }
    private fun checkifGPSOn():Boolean
    {
        val manager: LocationManager = requireContext().getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            showSnackBar()
            false
        }else
        {
            true
        }

    }
    private fun makeSelectLocationDialog()
    {
        MaterialAlertDialogBuilder(context)
                .setTitle("Select your location on the map.")
                .setPositiveButton("OK") { dialog, which ->
                }
                .show()
    }
    private fun makeLocationCallback()
    {
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                super.onLocationResult(locationResult)
                for (location in locationResult.locations) {
                    if (location != null) {
                        val currentLocation = LatLng(location.latitude, location.longitude)
                        map.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 17f))

                    }else
                    {
                        showSnackBar()
                    }
                }
            }
        }
    }

    override fun onStop() {
        super.onStop()
        snackbarGPSoff?.dismiss()
        snackbarConfirmLocation?.dismiss()
    }
}