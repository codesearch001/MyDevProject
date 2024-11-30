package com.snofed.publicapp.maps

import RealmRepository
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.snofed.publicapp.R
import com.snofed.publicapp.databinding.FragmentMapExploreBinding
import com.snofed.publicapp.databinding.MapFilterBinding
import com.snofed.publicapp.ui.clubsubmember.ViewModelClub.ActivityViewModelRealm
import com.snofed.publicapp.ui.clubsubmember.ViewModelClub.AreaViewModelRealm
import com.snofed.publicapp.ui.clubsubmember.ViewModelClub.ZoneTypeViewModelRealm
import dagger.hilt.android.AndroidEntryPoint
import io.realm.Realm


@AndroidEntryPoint
class FilterMapBottomSheetFragment : BottomSheetDialogFragment() {

    private var _binding: MapFilterBinding? = null
    private val binding get() = _binding!!

    private var actvities: MutableList<String> = mutableListOf()

    private lateinit var viewModelActivity: ActivityViewModelRealm
    private lateinit var viewModelArea: AreaViewModelRealm
    private lateinit var viewModelZoneType: ZoneTypeViewModelRealm

    var clientId : String? =""
    var allClientAreaNames : List<String> = emptyList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.RoundedBottomSheetDialog)
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        // return inflater.inflate(R.layout.map_filter, container, false)
        _binding = MapFilterBinding.inflate(inflater, container, false)
        clientId = arguments?.getString("clientId").toString()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Handle close icon click
        binding.closeButton.setOnClickListener {
            dismiss()
        }

        // Activity
        viewModelActivity = ViewModelProvider(this).get(ActivityViewModelRealm::class.java)
        // Call getAllActivities
        val allActivities = viewModelActivity.getAllActivities()
        // Use the activities, e.g., log them
        allActivities.forEach {
            Log.d("MyActivity", "Activity: ${it.name}")
        }

        // Area
        viewModelArea = ViewModelProvider(this).get(AreaViewModelRealm::class.java)
        // Call getAllActivities
        val allClientAreas = viewModelArea.getAreasByClientId(clientId!!)
        allClientAreaNames = allClientAreas.map { it.name!! }

        //Zone
        viewModelZoneType = ViewModelProvider(this).get(ZoneTypeViewModelRealm::class.java)
        val allZonesTypes = viewModelZoneType.getZonesByClientId(clientId!!)





        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, allClientAreaNames)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.mySpinner.adapter = adapter

       /* binding.mySpinner.setOnItemClickListener { parent, view, position, id  ->
            val selectedItem = itemList[position]

        }*/

    }

    override fun onStart() {
        super.onStart()
        val dialog = dialog as BottomSheetDialog
        val bottomSheet =
            dialog.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
        bottomSheet?.let {
            val behavior = BottomSheetBehavior.from(it)
            behavior.state = BottomSheetBehavior.STATE_EXPANDED
            behavior.isFitToContents = true
            behavior.skipCollapsed = true
        }
    }

}