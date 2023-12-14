package com.example.playtomictonyaymane.ui.dashboard

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.drawable.Drawable
import android.location.Geocoder
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.TextView
import okhttp3.OkHttpClient
import java.net.URLEncoder
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.beust.klaxon.JsonArray
import com.beust.klaxon.JsonObject
import com.beust.klaxon.Parser
import com.example.playtomictonyaymane.R
import com.example.playtomictonyaymane.databinding.FragmentDiscoveryBinding
import com.google.android.material.snackbar.Snackbar
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Request
import okhttp3.Response
import org.osmdroid.config.Configuration
import org.osmdroid.events.MapEventsReceiver
import org.osmdroid.library.BuildConfig
import org.osmdroid.views.MapView
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.overlay.ItemizedIconOverlay
import org.osmdroid.views.overlay.ItemizedOverlayWithFocus
import org.osmdroid.views.overlay.MapEventsOverlay
import org.osmdroid.views.overlay.OverlayItem
import java.io.IOException

class DashboardFragment : Fragment()  {

    private lateinit var searchField: TextView
    private lateinit var searchButton: Button
    private lateinit var clearButton: Button
    private lateinit var mapView: MapView
    private lateinit var locationManager: LocationManager
    private lateinit var locationListener: LocationListener
    private val REQUESTCODE = 100

    private var _binding: FragmentDiscoveryBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val dashboardViewModel =
            ViewModelProvider(this).get(DashboardViewModel::class.java)

        _binding = FragmentDiscoveryBinding.inflate(inflater, container, false)
        val root: View = binding.root

        Configuration.getInstance().userAgentValue = BuildConfig.LIBRARY_PACKAGE_NAME


        mapView = binding.mapView
        mapView.setTileSource(TileSourceFactory.MAPNIK)
        val mReceive: MapEventsReceiver = object : MapEventsReceiver {
            override fun singleTapConfirmedHelper(p: GeoPoint): Boolean {
                Snackbar.make(
                    binding.root,
                    "Lat= + $p.latitude.toString(), Lon=$p.longitude.toString()",
                    Snackbar.LENGTH_LONG
                ).show()
                return true
            }

            override fun longPressHelper(p: GeoPoint): Boolean {
                Snackbar.make(binding.root, "Long press", Snackbar.LENGTH_LONG).show()
                return true
            }
        }
        mapView.overlays.add(MapEventsOverlay(mReceive))
        mapView.controller.setZoom(10.0)

        return root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize view variables from the binding
        searchField = binding.searchField
        searchButton = binding.searchButton
        clearButton = binding.clearButton



        // Set default center and add marker
        val center = GeoPoint(51.2301298, 4.4117949)
        mapView.controller.setCenter(center)
        addMarker(center)

        // Set onClickListeners for search and clear buttons
        binding.searchButton.setOnClickListener {
            val client = OkHttpClient()

            val urlSearch = getString(R.string.urlSearch)
            val stringSearch = URLEncoder.encode(binding.searchField.text.toString(), "UTF-8")

            val request = Request.Builder()
                .url("$urlSearch)$stringSearch&format=json")
                .build()

            client.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    activity?.runOnUiThread {
                        Snackbar.make(requireView(), e.toString(), Snackbar.LENGTH_LONG).show()
                    }
                }

                override fun onResponse(call: Call, response: Response) {
                    response.use {
                        if (!response.isSuccessful) throw IOException("Unexpected code $response")

                        val parser: Parser = Parser.default()
                        val jsonString = StringBuilder(response.body!!.string())

                        val addresses = parser.parse(jsonString) as JsonArray<JsonObject>
                        val lat = addresses[0]["lat"] as String
                        val lon = addresses[0]["lon"] as String

                        activity?.runOnUiThread {
                            val address = GeoPoint(lat.toDouble(), lon.toDouble())
                            addMarker(address)
                            mapView.controller.setCenter(address)
                            mapView.controller.setZoom(16.0)
                        }
                    }
                }
            })
            hideSoftKeyBoard()
        }

        binding.clearButton.setOnClickListener {
            mapView.overlays.clear()
            mapView.invalidate()
        }

        // Set up location manager and check permissions
        locationManager = requireActivity().getSystemService(Context.LOCATION_SERVICE) as LocationManager
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            Snackbar.make(requireView(), "GPS not enabled", Snackbar.LENGTH_LONG).show()
        } else {
            locationListener = MyLocationListener()
            locationManager = requireActivity().getSystemService(Context.LOCATION_SERVICE) as LocationManager
        }


        checkPermissions()
    }

    private fun addMarker(location: GeoPoint) {
        val myLocationOverlayItem = OverlayItem("Here", "Current Position", location)
        val myCurrentLocationMarker: Drawable? = ResourcesCompat.getDrawable(
            resources, R.drawable.marker, null
        )
        myLocationOverlayItem.setMarker(myCurrentLocationMarker)
        val items = ArrayList<OverlayItem>()
        items.add(myLocationOverlayItem)
        val mOverlay = ItemizedOverlayWithFocus(requireContext(), items,
            object : ItemizedIconOverlay.OnItemGestureListener<OverlayItem?> {
                override fun onItemSingleTapUp(index: Int, item: OverlayItem?): Boolean {
                    Snackbar.make(binding.root, "You tapped me!", Snackbar.LENGTH_LONG).show()
                    return true
                }

                override fun onItemLongPress(index: Int, item: OverlayItem?): Boolean {
                    // Eventuele acties bij het lang indrukken op de pin
                    return true
                }
            })
        mapView.overlays.add(mOverlay)
        mapView.invalidate()
    }


    private fun checkPermissions() {
        val permissions: MutableList<String> = ArrayList()
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            permissions.add(Manifest.permission.ACCESS_FINE_LOCATION)
        }
        if (permissions.isNotEmpty()) {
            val params = permissions.toTypedArray()
            requestPermissions(params, REQUESTCODE)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            REQUESTCODE -> {
                val perms: MutableMap<String, Int> = HashMap()
                // fill with results
                var i = 0
                while(i < permissions.size) {
                    perms[permissions[i]] = grantResults[i]
                    i++
                }
            }
            else -> super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        }
    }

    private inner class MyLocationListener : LocationListener {
        override fun onLocationChanged(loc: Location) {
            // Handle location change events here
            // Update map or show location-related information
            Snackbar.make(
                requireView(),
                "Location changed to ${loc.latitude} / ${loc.longitude}",
                Snackbar.LENGTH_LONG
            ).show()
        }
    }

    private fun hideSoftKeyBoard() {
        val imm = requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        if(imm.isAcceptingText) {
            imm.hideSoftInputFromWindow(requireActivity().currentFocus?.windowToken, 0)
        }
    }





}