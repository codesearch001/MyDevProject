package com.snofed.publicapp.models.browseclubaction

/*data class GridItem()*/
/*data class GridItem(val id: Int,val imageResId: Int, val text: String)*/
data class GridItem( val id: Int,
                     val imageResId: Int,              // Resource ID for icon or second image
                     val backgroundImageResId: Int,  // Resource ID for background image
                     val title: String)
