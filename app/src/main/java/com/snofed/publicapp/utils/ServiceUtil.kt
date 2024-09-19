package com.snofed.publicapp.utils

object ServiceUtil {
    const val TAG = "SNOFED_TAG"

    const val BASE_URL = "http://161.97.121.63:5000/api/"
    const val BASE_URL_IMAGE = "http://161.97.121.63:5000"
    const val FEED_BACK_BASE_URL = "http://161.97.121.63:5005/api/"
    const val MEMBERSHIP_BASE_URL = "http://161.97.121.63:5006/api/"
    //FOR MEMBERSHIP IMAGE
    const val BASE_URL_MEMB_IMAGE = "http://161.97.121.63:5006"


    //FOR REGISTER
    const val REGISTER = "account/register-public-user"
    //FOR LOGIN
    const val LOGIN = "account/public-app/sign-in"
    //FOR FORGOT PASSWORD
    const val RECOVER_PASSWORD = "account/mobile-apps/forgot-password"
    //GET_USER_WORKOUTS
    const val GET_USER_WORKOUTS = "public-app/workouts/for-user/{id}"
    //BROWSE_CLUB_LIST
    const val BROWSE_CLUB_LIST = "public-app/sync/system-data?HasProTrails=false"

    const val BROWSE_CLUB_DETAILS = "public-app/sync/client?"

    const val GET_ALL_FEEDS = "public-app/workouts/feed?"

    const val GET_ALL_FEEDS_DETAILS = "public-app/workouts/{id}"

    const val CLUB_EVENT = "public-app/sync/events"

    const val CLUB_EVENT_DETAILS = "events/{id}"

    const val CLUB_TRAILS_DETAILS = "public-app/sync/trail/{id}"

    const val CLUB_TRAILS_DRAW_POLYLINES = "trails/getTrailPolyLinesOnRequest/{id}"

    const val CLUB_TRAILS_CHART = "trails/get-graph-data/{id}"

    const val CLUB_WORKOUT_RIDE = "public-app/workouts"

    const val UPLOAD_WORKOUT_IMAGES = "public-app/workouts/upload-images"

    /*** TASKING SERVICE FOR FEEDBACK ***/
    const val SYNC_PUBLIC_TASK_CATEGORIES = "tasking-service/task-categories/public-task-categories"

    const val SYNC_TASK_SUB_CATEGORIES = "tasking-service/tasks/get-task-by-public-user/{id}"

    const val SYNC_TASKS = "tasking-service/sync/tasks"

    const val SYNC_TASK_DETAILS = "tasking-service/tasks/{id}"

    const val BUY_MEMBERSHIP = "membership?"

}