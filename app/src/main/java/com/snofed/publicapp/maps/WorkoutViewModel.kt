package com.snofed.publicapp.maps

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import com.snofed.publicapp.db.WorkoutResponse
import com.snofed.publicapp.ridelog.NewRideWorkout
import com.snofed.publicapp.ridelog.NewWorkoutImage
import com.snofed.publicapp.ridelog.NewWorkoutPoint
import com.snofed.publicapp.utils.SnofedConstants
import com.snofed.publicapp.utils.SnofedUtils
import com.snofed.publicapp.utils.TokenManager
import io.realm.Realm
import io.realm.RealmList
import io.realm.Sort
import io.realm.kotlin.where
import org.json.JSONArray
import org.json.JSONObject
import java.util.UUID
import javax.inject.Inject


class WorkoutViewModel : ViewModel() {

    private val realm: Realm = Realm.getDefaultInstance()
   /* val workoutPointsList: LiveData<List<NewWorkoutPoint>> by lazy {
        MutableLiveData<List<NewWorkoutPoint>>()
    }*/
    private val _workout = MutableLiveData<NewRideWorkout>()
   // val workout: LiveData<NewRideWorkout> get() = _workout

    init {
        // Initialize or load existing workout data
        _workout.value = NewRideWorkout(
            id = "",
            syncAction = 0,
            publicUserId = "",
            publisherFullname = "",
            duration = 0,
            distance = 0.0,
            isPublic = true,
            isNewlyCreated = true,
            averagePace = 0.0,
            startTime = "",
            calories = 0,
            activityId = "",
            description = "",
            workoutPoints = RealmList(),
            leaderboardTime = 0,
            workoutImages = RealmList()
        )
    }

    private fun saveWorkoutToRealm() {
        val realm = Realm.getDefaultInstance()
        realm.executeTransaction { transactionRealm ->
            _workout.value?.let {
                transactionRealm.insertOrUpdate(it)
            }

        }
        //realm.refresh() // Force update to fetch the latest data
    }

    // Add or update workout details
    fun addNewRideWorkout(workout: NewRideWorkout) {
        _workout.value = workout
        saveWorkoutToRealm()
    }

    // Add a new workout point
//    fun addWorkoutPoint(point: NewWorkoutPoint) {
//        _workout.value?.workoutPoints = listOf(point) as RealmList<NewWorkoutPoint>
//        saveWorkoutToRealm()
//    }


    fun addWorkoutPoint(point: NewWorkoutPoint) {
        realm.executeTransaction { transactionRealm ->
            val workout = _workout.value
            workout?.workoutPoints?.add(point) ?: run {
                // If workoutPoints is null, initialize it and add the point
                _workout.value = workout?.apply {
                    workoutPoints = RealmList(point)
                }
            }
            transactionRealm.copyToRealmOrUpdate(workout)
        }
    }

   /* fun replaceWorkoutPoints(points: List<NewWorkoutPoint>) {
        realm.executeTransaction { transactionRealm ->
            val workout = _workout.value
            workout?.let {
                it.workoutPoints.clear()
                it.workoutPoints.addAll(points)
                transactionRealm.copyToRealmOrUpdate(it)
            }
        }
    }*/

    // Update workout duration and average pace
    fun addWorkoutDurationAndAvgPace(duration: Int, distance: Double, averagePace: Double) {
        _workout.value?.apply {
            this.duration = duration
            this.distance = distance
            this.averagePace = averagePace
        }
        saveWorkoutToRealm()
    }

    fun addWorkoutImage(image: NewWorkoutImage) {
        // Initialize the RealmList if it's null
        if (_workout.value?.workoutImages == null) {
            _workout.value?.workoutImages = RealmList()
        }

        // Add the new image to the RealmList
        _workout.value?.workoutImages?.add(image)

        // Save the workout to Realm
        saveWorkoutToRealm()
    }



    // Update workout description and visibility
    fun addDescription(description: String, isPublic: Boolean) {
        _workout.value?.apply {
            this.description = description
            this.isPublic = isPublic
        }
        saveWorkoutToRealm()
    }

    //Fetch data from realm db in array list
    fun fetchWorkoutByIdAsJsonList(workoutId: String): List<WorkoutResponse> {
        // Initialize Realm
        val realm = Realm.getDefaultInstance()

        // Create a Gson instance
        val gson = Gson()

        // List to hold the WorkoutResponse objects
        val workoutResponseList = mutableListOf<WorkoutResponse>()

        try {


            ///////////////////NewWorkoutPoint  // NewRideWorkout
            val obj = realm.where<NewRideWorkout>()
              //  .sort("id", Sort.DESCENDING)
                .sort("id", Sort.ASCENDING)
                .findFirst()

            if(obj != null){
                val latestId = obj.id
                val latestIdXYZ = obj.workoutPoints

                print("latestIdXYZ" + latestIdXYZ)
                print("latestId" + latestId)

                if (obj != null) {
                    // Convert the workout to a Realm-managed object copy
                    val workoutCopy = realm.copyFromRealm(obj)
                    // Convert the workout object to JSON string
                    val jsonString = gson.toJson(workoutCopy)
                    // Convert the JSON string to WorkoutResponse object
                    val workoutResponse = gson.fromJson(jsonString, WorkoutResponse::class.java)
                    // Add the WorkoutResponse object to the list
                    workoutResponseList.add(workoutResponse)
                }
            }

            // Fetch the workout from Realm
            //val workout = realm.where<NewRideWorkout>().equalTo("id", workoutId).findFirst()

           /* if (workout != null) {
                // Convert the workout to a Realm-managed object copy
                val workoutCopy = realm.copyFromRealm(workout)
                // Convert the workout object to JSON string
                val jsonString = gson.toJson(workoutCopy)
                // Convert the JSON string to WorkoutResponse object
                val workoutResponse = gson.fromJson(jsonString, WorkoutResponse::class.java)
                // Add the WorkoutResponse object to the list
                workoutResponseList.add(workoutResponse)
            }*/
        } catch (e: Exception) {
            // Handle the exception
            e.printStackTrace()
        } finally {
            // Close Realm instance
            realm.close()
        }

        // Return the list of WorkoutResponse objects
        return workoutResponseList
    }


    fun fetchMostRecentWorkout(workoutId: String): NewRideWorkout? {
        val realm = Realm.getDefaultInstance()
        val mostRecentWorkout = realm.where(NewRideWorkout::class.java)
            .equalTo("id",workoutId) // Sort by timestamp in descending order
            .findFirst() // Get the most recent entry
        return mostRecentWorkout?.let {
            realm.copyFromRealm(it) // Convert to plain object
        }?.also {
            realm.close() // Close Realm instance after use
        }
    }

    // Get workout images
    fun getWorkoutImages(): List<NewWorkoutImage> {
        return _workout.value?.workoutImages?.toList() ?: emptyList()
    }

    // Get workout points
    fun getWorkoutPoints(): ArrayList<NewWorkoutPoint> {
        return ArrayList(_workout.value?.workoutPoints?.toList() ?: emptyList())
    }

    // Get workout images by workout ID
    fun getWorkoutImages(workoutId: String): List<NewWorkoutImage> {
        return realm.where(NewWorkoutImage::class.java)
            .equalTo("id", workoutId)
            .findAll()
    }

    override fun onCleared() {
        super.onCleared()
        realm.close()
    }

}
