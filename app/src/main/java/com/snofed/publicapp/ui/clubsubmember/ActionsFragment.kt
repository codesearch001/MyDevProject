package com.snofed.publicapp.ui.clubsubmember

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.snofed.publicapp.R
import com.snofed.publicapp.adapter.BrowseClubListAdapter
import com.snofed.publicapp.adapter.ClubActionAdapter
import com.snofed.publicapp.databinding.FragmentActionsBinding
import com.snofed.publicapp.models.browseclubaction.GridItem
import com.snofed.publicapp.utils.OnItemClickListener
import com.snofed.publicapp.utils.ToastUtils
import com.snofed.publicapp.utils.TokenManager
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ActionsFragment : Fragment(),OnItemClickListener {

    private var _binding: FragmentActionsBinding? = null
    private val binding get() = _binding!!
    @Inject
    lateinit var tokenManager: TokenManager

    private var clientId: String? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
       // return inflater.inflate(R.layout.fragment_actions, container, false)
        _binding = FragmentActionsBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.becomeMemberButton.setOnClickListener {
            val bundle = Bundle()
            bundle.putString("clientId", tokenManager.getClientId().toString())
            val destination = R.id.buyMembershipFragment
            findNavController().navigate(destination, bundle)
        }

        binding.goexplore.setOnClickListener {
            findNavController().navigate(R.id.mapExploreFragment)
        }

        fun getItems(): List<GridItem> {
            return listOf(
            GridItem(1, R.drawable.signs, R.drawable.trailstatus,resources.getString(R.string.t_trails_status)),
            GridItem(2, R.drawable.eventss, R.drawable.events,resources.getString(R.string.t_events)),
            GridItem(3, R.drawable.card, R.drawable.shop,resources.getString(R.string.t_shops)),
            GridItem(4, R.drawable.document, R.drawable.linksanddocuments,resources.getString(R.string.t_links_and_doc)),
            GridItem(5, R.drawable.world,  R.drawable.socialmedia, resources.getString(R.string.t_social_media)),
            GridItem(6, R.drawable.information, R.drawable.feedback,resources.getString(R.string.t_feedback)),)
        }

        binding.recyclerView.layoutManager = GridLayoutManager(context, 2) // 2 columns
        binding.recyclerView.adapter = ClubActionAdapter(getItems(),this,requireActivity())
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    override fun onItemClick(itemId: Int) {

        ToastUtils.showToast(requireActivity(), "Item clicked with id: ${itemId}")
        Log.i("Click","Clickbbbbb "+itemId.toString())

        if (itemId==1){
            findNavController().navigate(R.id.trailsStatusFragment)
        }
        if (itemId==2){
            findNavController().navigate(R.id.clubEventFragment)
        }
        if (itemId==3){
            val bundle = Bundle()
            bundle.putString("clientId", tokenManager.getClientId().toString())
            val destination = R.id.purchaseOptionsFragment
            findNavController().navigate(destination, bundle)
           // findNavController().navigate(R.id.purchaseOptionsFragment)
        }
        if (itemId==4){
            findNavController().navigate(R.id.linksFragment)
        }
        if (itemId==5){
            findNavController().navigate(R.id.twitterFragment)
        }
        if (itemId==6){
            findNavController().navigate(R.id.feedBackFragment)
        }
    }
}