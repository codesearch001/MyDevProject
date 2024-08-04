import android.content.Context
import com.mapbox.geojson.LineString
import com.mapbox.geojson.Point
import com.mapbox.maps.MapView
import com.mapbox.maps.extension.style.layers.addLayer
import com.mapbox.maps.extension.style.layers.generated.LineLayer
import com.mapbox.maps.extension.style.layers.properties.generated.LineCap
import com.mapbox.maps.extension.style.layers.properties.generated.LineJoin
import com.mapbox.maps.extension.style.sources.addSource
import com.mapbox.maps.extension.style.sources.generated.GeoJsonSource
import com.mapbox.maps.extension.style.sources.getSourceAs

class PolylineManager(private val context: Context, private val mapView: MapView) {

    private val polylineSourceId = "polyline-source"
    private val polylineLayerId = "polyline-layer"

    fun addPolyline(points: List<Point>, lineColor: String = "#FF0000", lineWidth: Double = 5.0) {
        val lineString = LineString.fromLngLats(points)
        val geoJsonSource = GeoJsonSource.Builder(polylineSourceId)
            .geometry(lineString)
            .build()
        mapView.getMapboxMap().getStyle { style ->
            style.addSource(geoJsonSource)

            val lineLayer = LineLayer(polylineLayerId, polylineSourceId)
                .lineCap(LineCap.ROUND)
                .lineJoin(LineJoin.ROUND)
                .lineWidth(lineWidth)
                .lineColor(lineColor)
            style.addLayer(lineLayer)
        }
    }

    fun updatePolyline(newPoints: List<Point>) {
        mapView.getMapboxMap().getStyle { style ->
            val source = style.getSourceAs<GeoJsonSource>(polylineSourceId)
            source?.geometry(LineString.fromLngLats(newPoints))
        }
    }

    fun removePolyline() {
        mapView.getMapboxMap().getStyle { style ->
            style.removeStyleLayer(polylineLayerId)
            style.removeStyleSource(polylineSourceId)
        }
    }
}
