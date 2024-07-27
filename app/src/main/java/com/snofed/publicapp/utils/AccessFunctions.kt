package com.snofed.publicapp.utils

import android.text.Html

public class AccessFunctions {

    fun removePTags(htmlString: String): String {
        // Remove <p> tags using regex
        val regex = Regex("<p[^>]*>|</p>")
        var strippedString = htmlString.replace(regex, "")

        // Use Html.fromHtml to handle other HTML tags if needed
        strippedString = Html.fromHtml(strippedString).toString()

        return strippedString
    }

}