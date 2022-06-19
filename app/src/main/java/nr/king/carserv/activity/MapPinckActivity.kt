package nr.king.carserv.activity


import android.content.Intent
import android.location.Geocoder
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.GoogleMap.OnMarkerDragListener
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import dagger.hilt.android.AndroidEntryPoint
import nr.king.carserv.R
import nr.king.carserv.databinding.MapViewBinding
import java.io.IOException
import java.util.*


@AndroidEntryPoint
class MapPinckActivity:AppCompatActivity(),OnMapReadyCallback {

     lateinit var  mapViewBinding: MapViewBinding
    private lateinit var mMap: GoogleMap
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mapViewBinding = DataBindingUtil.setContentView<MapViewBinding?>(this,R.layout.map_view).apply {
            val mapFragment =
                supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
            mapFragment!!.getMapAsync(this@MapPinckActivity)
        }

    }

    override fun onMapReady(googleMap: GoogleMap?) {
          mMap = googleMap!!
          val sydney = LatLng(-34.0, 151.0)
        mMap.addMarker(MarkerOptions().position(sydney).draggable(true).title("Marker in Sydney"))
          mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))
        mMap.uiSettings.isZoomControlsEnabled = true
          googleMap.setOnMarkerDragListener(object : OnMarkerDragListener {
              override fun onMarkerDragStart(marker: Marker) {}
              override fun onMarkerDrag(marker: Marker) {}
              override fun onMarkerDragEnd(marker: Marker) {
                  val latLng = marker.position
                  val geocoder = Geocoder(this@MapPinckActivity, Locale.getDefault())
                  val data = Intent()
                  data.putExtra("long", latLng.longitude)
                  data.putExtra("lat", latLng.latitude)
                  finish()
                  try {
                      val address = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1)[0]
                      data.putExtra("address", "$address")
                  } catch (e: IOException) {
                      e.printStackTrace()
                  }
                  setResult(RESULT_OK, data)
                  finish()
              }
          })
    }

}