package com.RNA_RECORDS.petsforadoption.AdoptAPet

import android.content.Context
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Bundle
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.RNA_RECORDS.petsforadoption.R
import com.RNA_RECORDS.petsforadoption.Utils.Post
import com.firebase.ui.auth.AuthUI.getApplicationContext
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.VisibleRegion
import com.google.android.material.snackbar.Snackbar
import java.nio.file.attribute.PosixFileAttributeView

class AdoptAPetFragment : Fragment() {
    lateinit var map:GoogleMap
    lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    lateinit var locationRequest: LocationRequest
    lateinit var locationCallback: LocationCallback
    lateinit var viewModel:AdoptPetViewModel
    var snackbar:Snackbar? = null
    lateinit var progressBar: ProgressBar
    lateinit var viewAdopt:View

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if(requestCode==1)
        {
            if(grantResults.isNotEmpty() && grantResults[0]==PackageManager.PERMISSION_GRANTED)
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

        makeLocationCallback()
        checkifGPSOn()
        enableMyLocation()

    }

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        viewAdopt = inflater.inflate(R.layout.fragment_adopt_a_pet, container, false)
        viewModel = ViewModelProvider(this).get(AdoptPetViewModel::class.java)
        progressBar = viewAdopt.findViewById(R.id.progressBar)


        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getApplicationContext())
        locationRequest = LocationRequest.create()
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        locationRequest.interval = 120000
        return viewAdopt
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
                progressBar.visibility = View.VISIBLE
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
    private fun showSnackBar()
    {
         snackbar = Snackbar.make(requireView(), "Ensure that GPS is switched on", Snackbar.LENGTH_INDEFINITE).setAction("RETRY"){
            enableMyLocation()
        }
        snackbar!!.show()
    }

    private fun checkifGPSOn():Boolean
    {
        val manager:LocationManager = requireContext().getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            showSnackBar()
            false
        }else
        {
            true
        }

    }
    private fun makeLocationCallback()
    {
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                super.onLocationResult(locationResult)
                for (location in locationResult.locations) {
                    if (location != null) {
                        val currentLocation = LatLng(location.latitude, location.longitude)
                        map.addMarker(MarkerOptions().icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)).position(currentLocation).title("Current Location"))
                        map.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 10f))

                    }else
                    {
                        showSnackBar()
                    }
                }
                progressBar.visibility = View.INVISIBLE
                addMarkersFromFireBase()
            }
        }
    }
    private fun addMarkersFromFireBase()
    {

        var counter:Int = 0
        viewModel.postListLiveData.observe(this, Observer {listFromFirebase->
            for(post:Post in listFromFirebase)
            {
                val postLatLng = LatLng(post.latitude,post.longitude)
                map.addMarker(MarkerOptions().position(LatLng(post.latitude,post.longitude)))
                if(!isVisibleOnMap(postLatLng))
                {
                    counter++
                }

               }
            if(counter==listFromFirebase.size)
            {
                makeSorrySnackbar()
            }
            map.setOnMarkerClickListener {marker ->
                val markerClickedLatitude:Double = marker.position.latitude
                for(post in listFromFirebase)
                {
                    if(post.latitude == markerClickedLatitude)
                    {
                        viewAdopt.findNavController().navigate(AdoptAPetFragmentDirections.actionAdoptAPetFragmentToMapMarkerClicked(post))
                    }
                }

                true
            }

        })

    }


    override fun onStop() {
        super.onStop()
        snackbar?.dismiss()
    }
    private fun isVisibleOnMap(latLng: LatLng?): Boolean {
        val vr: VisibleRegion = map.projection.visibleRegion
        return vr.latLngBounds.contains(latLng)
    }
    private fun makeSorrySnackbar()
    {
        if(activity!=null && isAdded) {
            val snackbar: Snackbar = Snackbar.make(requireActivity().findViewById(android.R.id.content), "There are no pets available near your location. Please check back later.", Snackbar.LENGTH_INDEFINITE).setAction("OK"){}
            snackbar.show()
        }}

}