package com.snofed.publicapp.utils

object ServiceUtil {

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

    const val USER_SETTINGS = "public-app/sync/user/settings"

    //FOR FORGOT PASSWORD
    const val RECOVER_PASSWORD = "account/mobile-apps/forgot-password"

    //USER_PROFILE_SETTING
    const val USER_PROFILE_SETTING = "public-app/sync/user/settings"

    //USER_PROFILE_IMAGE
    const val USER_PROFILE_IMAGE = "public-app/upload/profile-image/{userId}"

    const val UPLOAD_WORKOUT_IMAGES = "public-app/workouts/upload-images"

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

    const val CLUB_FAV_ITEMS = "public-app/sync/client?"

    const val CLUB_WORKOUT_RIDE = "public-app/workouts"

    const val SUBSCRIBE_TO_CLUB = "public-app/subscribe"

    const val UNSUBSCRIBE_FROM_CLUB = "public-app/unsubscribe"



    /*** TASKING SERVICE FOR FEEDBACK ***/
    const val SYNC_PUBLIC_TASK_CATEGORIES = "tasking-service/task-categories/public-task-categories"

    const val SYNC_TASK_SUB_CATEGORIES = "tasking-service/tasks/get-task-by-public-user/{id}"

    const val SYNC_TASKS = "tasking-service/sync/tasks"

    const val SYNC_TASK_DETAILS = "tasking-service/tasks/{id}"

    /**********************HISTORY********************/
    const val TICKET_PURCHASE_ORDER_HISTORY = "ticket-order/history/{userRef}"

    const val TICKET_PURCHASE_ORDER_HISTORY_PRO_TRAILS = "/api/tickets/pro-trails/{userId}"

    const val GET_TICKET_TYPE = "ticket-type/client/{clientId}"

    /**********************Payment Gateway API********************/

    const val SEND_ORDER_DIRECT = "direct2internet/new-order"

    const val SEND_ORDER_SWISH = "swish/new-order/m-commerce"

    /**********************MEMBERSHIP********************/
    const val GET_MEMBERSHIP = "membership?"

    //const val GET_ACTIVE_MEMBERSHIP = "membership/active/{userId}"
    const val GET_ACTIVE_MEMBERSHIP = "membership/history/v2/{userId}"    

    //api/membership/history/v2/{userId}
    //const val GET_ALL_MEMBERSHIP = "membership"
    //const val GET_ACTIVE_MEMBERSHIP = "membership/active/{userRef}"

    const val GET_BENIFET_MEMBERSHIP = "membership/{id}"
    const val ACTIVATE_MEMBERSHIP_WITH_CODE = "membership/activate/{userRef}/{activationCode}"

}