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
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ActionsFragment : Fragment(),OnItemClickListener {
    private var _binding: FragmentActionsBinding? = null
    private val binding get() = _binding!!

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

        fun getItems(): List<GridItem> {
            return listOf(
            GridItem(1, R.drawable.activitiess,R.drawable.activities, "Activities"),
            //GridItem(2, R.drawable.maps,R.drawable.mapbg, "Map"),
            GridItem(3, R.drawable.signs, R.drawable.trailstatus,"Trails Status"),
            GridItem(4, R.drawable.eventss, R.drawable.events,"Events"),
            GridItem(5, R.drawable.card, R.drawable.shop,"Shops"),
            GridItem(6, R.drawable.document, R.drawable.linksanddocuments,"Links and Documents"),
            GridItem(7, R.drawable.world,  R.drawable.socialmedia, "Social Media"),
            GridItem(8, R.drawable.information, R.drawable.feedback,"FeedBack"),
            )
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

        if (itemId==3){
            findNavController().navigate(R.id.trailsStatusFragment)
        }
        if (itemId==4){
            findNavController().navigate(R.id.eventFragment)
        }
        if (itemId==5){
            findNavController().navigate(R.id.purchaseOptionsFragment)
        }
        if (itemId==6){
            findNavController().navigate(R.id.linksFragment)
        }
        if (itemId==8){
            findNavController().navigate(R.id.feedBackFragment)
        }

    }
}