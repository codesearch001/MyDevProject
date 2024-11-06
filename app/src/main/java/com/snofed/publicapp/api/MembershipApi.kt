package com.snofed.publicapp.api


import com.snofed.publicapp.membership.model.ActiveMembership
import com.snofed.publicapp.membership.model.MembershipDetails
import com.snofed.publicapp.membership.model.BuyMembership
import com.snofed.publicapp.purchasehistory.model.TicketPurchaseHistory
import com.snofed.publicapp.ui.order.model.TicketTypeData
import com.snofed.publicapp.ui.order.ticketing.OrderDTO
import com.snofed.publicapp.ui.order.ticketing.OrderResponseDTO
import com.snofed.publicapp.ui.order.ticketing.SwishResponseDTO
import com.snofed.publicapp.utils.ServiceUtil.GET_ACTIVE_MEMBERSHIP
import com.snofed.publicapp.utils.ServiceUtil.GET_BENIFET_MEMBERSHIP
import com.snofed.publicapp.utils.ServiceUtil.GET_MEMBERSHIP
import com.snofed.publicapp.utils.ServiceUtil.GET_TICKET_TYPE
import com.snofed.publicapp.utils.ServiceUtil.SEND_ORDER_DIRECT
import com.snofed.publicapp.utils.ServiceUtil.SEND_ORDER_SWISH
import com.snofed.publicapp.utils.ServiceUtil.TICKET_PURCHASE_ORDER_HISTORY
import com.snofed.publicapp.utils.ServiceUtil.TICKET_PURCHASE_ORDER_HISTORY_PRO_TRAILS
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface MembershipApi {
    //GET_MEMBERSHIP
    @GET(GET_MEMBERSHIP)
    suspend fun getMembership(@Header("Accept-Language") acceptLanguage: String,
                              @Query("ClientId") clientId: String): Response<BuyMembership>

 /*   //GET_ALL_MEMBERSHIP
    @GET(GET_ALL_MEMBERSHIP)
    suspend fun getALLMembership(@Header("Accept-Language") acceptLanguage: String): Response<BuyMembership>
*/
    //GET_ACTIVE_MEMBERSHIP
    @GET(GET_ACTIVE_MEMBERSHIP)
    suspend fun getActiveMembership(@Header("Accept-Language") acceptLanguage: String,
                                    @Path("userId") userId: String): Response<ActiveMembership>
                                    //@Path("userRef") userRef: String): Response<ActiveMembership>

    //TICKET_PURCHASE_ORDER_HISTORY
    @GET(TICKET_PURCHASE_ORDER_HISTORY)
    suspend fun getPurchaseOrderHistory(@Header("Accept-Language") acceptLanguage: String,
                                        @Path("userRef") id: String): Response<TicketPurchaseHistory>    //TICKET_PURCHASE_ORDER_HISTORY

    //TICKET_PURCHASE_ORDER_HISTORY_PRO_TRAILS
    @GET(TICKET_PURCHASE_ORDER_HISTORY_PRO_TRAILS)
    suspend fun getPurchaseOrderHistoryProTrails(@Header("Accept-Language") acceptLanguage: String,
                                        @Path("userRef") id: String): Response<TicketPurchaseHistory>
    //GET_TICKET_TYPE
    @GET(GET_TICKET_TYPE)
    suspend fun getTicketType(@Header("Accept-Language") acceptLanguage: String,
                                        @Path("clientId") id: String): Response<TicketTypeData>

    //SEND_ORDER_DIRECT
    @POST(SEND_ORDER_DIRECT)
    suspend fun getSendOrderDirect(@Header("Accept-Language") acceptLanguage: String, @Body orderDTO: OrderDTO ): Response<OrderResponseDTO>

    //SEND_ORDER_SWISH
    @POST(SEND_ORDER_SWISH)
    suspend fun getSendOrderSwish(@Header("Accept-Language") acceptLanguage: String, @Body orderDTO: OrderDTO): Response<SwishResponseDTO>

    //TICKET_PURCHASE_ORDER_HISTORY
    @GET(GET_BENIFET_MEMBERSHIP)
    suspend fun getBenfitsMembership(@Header("Accept-Language") acceptLanguage: String,
                                        @Path("id") id: String): Response<MembershipDetails>

}