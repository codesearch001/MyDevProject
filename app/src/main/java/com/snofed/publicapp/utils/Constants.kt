package com.snofed.publicapp.utils

object Constants {
    const val TAG = "SNOFED_TAG"

    const val BASE_URL = "http://161.97.121.63:5000/api/"
    const val BASE_URL_IMAGE = "http://161.97.121.63:5000"
    const val USER_TOKEN = "token"
    const val USER_FIRST_NAME = "fullName"
    const val USER_USER_ID = "id"
    const val CLUB_CLIENT_ID = "clientId"
    const val CLUB_TRAILS_ID = "trailId"
    const val CLUB_PUBLIC_ID = "feedId"
    const val CLUB_DESC = "description"
    const val PREFS_TOKEN_FILE = "prefs_token_file"


    const val REGISTER = "account/register-public-user"
    const val LOGIN = "account/public-app/sign-in"
    //const val RECOVER_PASSWORD = "account/forgot-password"
    const val RECOVER_PASSWORD = "account/mobile-apps/forgot-password"
    const val BROWSE_CLUB_LIST = "public-app/sync/system-data?HasProTrails=false"
    const val BROWSE_CLUB_DETAILS= "public-app/sync/client?"
    const val GET_ALL_FEEDS = "public-app/workouts/feed?"
    const val GET_ALL_FEEDS_DETAILS= "public-app/workouts/{id}"
    const val CLUB_EVENT= "public-app/sync/events"
    const val CLUB_EVENT_DETAILS= "events/{id}"
    const val CLUB_TRAILS_DETAILS= "public-app/sync/trail/{id}"
    const val CLUB_TRAILS_DRAW_POLYLINES= "trails/getTrailPolyLinesOnRequest/{id}"
    const val CLUB_TRAILS_CHART= "trails/get-graph-data/{id}"
}