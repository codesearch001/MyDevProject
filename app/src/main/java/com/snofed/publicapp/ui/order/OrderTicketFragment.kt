package com.snofed.publicapp.ui.order

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.snofed.publicapp.R
import com.snofed.publicapp.databinding.FragmentOrderTicketBinding
import com.snofed.publicapp.ui.newticket.TicketViewModel
import com.snofed.publicapp.ui.order.adapter.TicketAdapter
import com.snofed.publicapp.ui.order.ticketing.OrderDTO
import com.snofed.publicapp.ui.order.ticketing.TicketDTO
import com.snofed.publicapp.utils.PreferencesNames
import com.snofed.publicapp.utils.TokenManager
import com.snofed.publicapp.utils.enums.TicketOrderTypeEnum
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class OrderTicketFragment : Fragment() {
    private var _binding: FragmentOrderTicketBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: TicketViewModel
    private lateinit var adapter: TicketAdapter
    private var isFirstButtonClicked = false
    private var clientId: String? = null // To store the passed category value
    private var ticketCategory: Int? = null // To store the passed category value
    private var isMultipleTicket: Boolean? = false // To store the passed category value

    var tickets :List<TicketDTO> = listOf()


    @Inject
    lateinit var tokenManager: TokenManager
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_order_ticket, container, false)
        _binding = FragmentOrderTicketBinding.inflate(inflater, container, false)

        clientId = arguments?.getString("clientId")
        ticketCategory = arguments?.getInt("ticketCategory")
        isMultipleTicket = arguments?.getBoolean("isMultipleTicket")

        Log.i("ClientId", "Id: $clientId")
        Log.d("NewTicketFragment", "Ticket category received: $ticketCategory")
        Log.d("NewTicketFragment", "Ticket isMultipleTicket received: $isMultipleTicket")

        viewModel = ViewModelProvider(requireActivity()).get(TicketViewModel::class.java)

        adapter = TicketAdapter(viewModel.tickets) { ticket ->
            // Handle delete action
            deleteTicket(ticket)
        }

        binding.rvOrderTicket.adapter = adapter
        binding.rvOrderTicket.layoutManager = LinearLayoutManager(requireContext())


        return binding.root
    }

    private fun deleteTicket(ticket: TicketDTO) {
        val index = viewModel.getTicketIndex(ticket)
        if (index != -1) { // Check if the ticket exists
            viewModel.removeTicketByIndex(index) // Remove from ViewModel
            adapter.updateTickets(viewModel.tickets) // Update adapter

            Toast.makeText(requireContext(), R.string.t_s_ticket_deleted, Toast.LENGTH_SHORT).show()

        } else {

            Toast.makeText(requireContext(),  R.string.t_s_ticket_not_found, Toast.LENGTH_SHORT).show()
        }

        displayTicketPrice()

        // Hide button layout if no tickets remain
        if (viewModel.tickets.isEmpty()) {
            binding.buttonLayout.visibility = View.GONE
            binding.tvSplashText.visibility = View.VISIBLE
        }
    }
    /*  @SuppressLint("NotifyDataSetChanged")
      private fun deleteTicket(ticket: TicketModel) {
          // Remove the ticket from ViewModel
          viewModel.removeTicket(ticket)
          // Clear price and other UI elements if necessary
          displayTicketPrice()
          // Notify the adapter that data has changed
          adapter.notifyDataSetChanged()

          // Optionally, show a message
          Toast.makeText(requireContext(), "Ticket deleted", Toast.LENGTH_SHORT).show()
      }*/

    @SuppressLint("SuspiciousIndentation")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.backBtn.setOnClickListener {
            it.findNavController().popBackStack()
        }
        // Retrieve arguments from the Bundle
        ticketCategory = arguments?.getInt("ticketCategory")
        isMultipleTicket = arguments?.getBoolean("isMultipleTicket")


        tickets = viewModel.getAllTickets()
        // If tickets are empty, hide the button layout
        if (tickets.isEmpty()) {
            binding.buttonLayout.visibility = View.GONE
            binding.tvSplashText.visibility = View.VISIBLE
        } else {
            binding.buttonLayout.visibility = View.VISIBLE
            displayTicketPrice()
        }
        ///send order
      val prepareOrder =  OrderDTO(
          null,
          TicketOrderTypeEnum.SINGLE_TICKET.ordinal,
          getResources().getString(R.string.backend_localization),
          tokenManager.getUserId(),
          tickets,
          clientId, PreferencesNames.CLIENT_CALLBACK_URL + clientId)

        // Convert object to JSON
        val gson = Gson()
        val json = gson.toJson(prepareOrder)
        Log.e("payData"  ,"hello  "+ json)

        binding.btnPayWithSwish.setOnClickListener {
            if (!isFirstButtonClicked) {
                // Activate the first button
                binding.btnPayWithSwish.setBackgroundResource(R.drawable.btn_buy_memberships_active)
                binding.btnPayWithSwish.setTextColor(Color.WHITE)

                // Deactivate the second button
                binding.btnPayWithCreditCard.setBackgroundResource(R.drawable.blue_outline_button) // Reset to inactive state
                binding.btnPayWithCreditCard.setTextColor(Color.parseColor("#03A9F4"))
                isFirstButtonClicked = true
            }
        }

        binding.btnPayWithCreditCard.setOnClickListener {
            if (isFirstButtonClicked) {
                // Activate the second button
                binding.btnPayWithCreditCard.setBackgroundResource(R.drawable.blue_bg_color)
                binding.btnPayWithCreditCard.setTextColor(Color.WHITE) // Set text to blue


                // Deactivate the first button
                binding.btnPayWithSwish.setBackgroundResource(R.drawable.blue_outline_button) // Reset to inactive state
                binding.btnPayWithSwish.setTextColor(Color.parseColor("#03A9F4")) // Set text to blue
                isFirstButtonClicked = false
            }
        }


        // Use the ticketCategory if needed
        if (ticketCategory != null) {
            Log.d("OrderTicketFragment", "Received Ticket Category: $ticketCategory")
            if (ticketCategory == 3) {
                binding.topAddOrderIcon.isVisible = false
            } else if (ticketCategory == 1 && isMultipleTicket == false) {
                binding.topAddOrderIcon.isVisible = false
            } else if (isMultipleTicket!!) {
                binding.topAddOrderIcon.isVisible = true
            }
        }

        binding.topAddOrderIcon.setOnClickListener {
            val bundle = Bundle()
            bundle.putString("clientId", clientId)
            bundle.putInt("ticketCategory", ticketCategory!!.toInt())
            bundle.putBoolean("isMultipleTicket", isMultipleTicket!!)
            val destination = R.id.newTicketFragment
            findNavController().navigate(destination, bundle)
            //it.findNavController().navigate(R.id.newTicketFragment)
        }


        // Assuming you want to display the last added ticket details
        if (viewModel.tickets.isNotEmpty()) {
            // val lastTicket = viewModel.tickets.last() // Get the last ticket added
            displayTicketPrice()
        }
    }

    @SuppressLint("SetTextI18n")
    private fun displayTicketPrice() {
        //binding.txtPayTotalPrice.text = "Total Price: " + ticket.price.toString()
        binding.txtPayTotalPrice.text = resources.getString(R.string.total_price) + viewModel.updatePrice()
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null // Prevent memory leaks
    }
}