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
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.snofed.publicapp.R
import com.snofed.publicapp.adapter.BrowseClubListAdapter
import com.snofed.publicapp.adapter.MapTrailCategoryAdapter
import com.snofed.publicapp.adapter.PoisTypeAdapter
import com.snofed.publicapp.adapter.ZonesTypeAdapter
import com.snofed.publicapp.databinding.FragmentMapExploreBinding
import com.snofed.publicapp.databinding.MapFilterBinding
import com.snofed.publicapp.ui.clubsubmember.ViewModelClub.ActivityViewModelRealm
import com.snofed.publicapp.ui.clubsubmember.ViewModelClub.AreaViewModelRealm
import com.snofed.publicapp.ui.clubsubmember.ViewModelClub.PoisTypeViewModelRealm
import com.snofed.publicapp.ui.clubsubmember.ViewModelClub.TaskCategoryViewModelRealm
import com.snofed.publicapp.ui.clubsubmember.ViewModelClub.ZoneTypeViewModelRealm
import dagger.hilt.android.AndroidEntryPoint
import io.realm.Realm


@AndroidEntryPoint
class FilterMapBottomSheetFragment : BottomSheetDialogFragment() {

    private var _binding: MapFilterBinding? = null
    private val binding get() = _binding!!

    private var actvities: MutableList<String> = mutableListOf()
    var clientId : String? =""

    private lateinit var viewModelArea: AreaViewModelRealm
    private lateinit var viewModelActivity: ActivityViewModelRealm
    private lateinit var viewModelPoisType: PoisTypeViewModelRealm
    private lateinit var viewModelTaskCategory: TaskCategoryViewModelRealm
    private lateinit var viewModelZoneType: ZoneTypeViewModelRealm

    private lateinit var poisTypeAdapter: PoisTypeAdapter
    private lateinit var trailCategoryAdapter: MapTrailCategoryAdapter
    private lateinit var zonesTypeAdapter: ZonesTypeAdapter


    var allClientAreaNames : List<String> = emptyList()
    var allClientPoisType :  List<String> = emptyList()
    var allClientTaskCategory : List<String> = emptyList()
    var allClientZonesType : List<String> = emptyList()

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

        binding.closeButton.setOnClickListener {
            dismiss()
        }

        // Activity
        viewModelActivity = ViewModelProvider(this).get(ActivityViewModelRealm::class.java)
        val allActivities = viewModelActivity.getAllActivities()
        allActivities.forEach {
            Log.d("MyActivity", "Activity: ${it.name}")
        }

        // Area
        viewModelArea = ViewModelProvider(this).get(AreaViewModelRealm::class.java)
        val allClientAreas = viewModelArea.getAreasByClientId(clientId!!)
        allClientAreaNames = allClientAreas.map { it.name!! }

        // POIs Type
        viewModelPoisType = ViewModelProvider(this).get(PoisTypeViewModelRealm::class.java)
        val allPOISTypes = viewModelPoisType.getAllPoiTypes()
        allClientPoisType = allPOISTypes.map { it.iconPath!! }
        allPOISTypes.forEach {
            Log.d("allClientPoisType", "allClientPoisType: ${it.iconPath}")
        }

        //Task Category
        viewModelTaskCategory = ViewModelProvider(this).get(TaskCategoryViewModelRealm::class.java)
        val allTaskCategories = viewModelTaskCategory.getAllTaskCategories()
        allClientTaskCategory = allTaskCategories.map { it.name!! }


        //Zones Type
        viewModelZoneType = ViewModelProvider(this).get(ZoneTypeViewModelRealm::class.java)
        val allZoneTypes = viewModelZoneType.getAllZoneTypes()
        allClientZonesType = allZoneTypes.map { it.name!! }
        allZoneTypes.forEach {
            Log.d("allZoneTypes", "allZoneTypes: ${it.name!!}")
        }



        //Zone
       /* viewModelZoneType = ViewModelProvider(this).get(ZoneTypeViewModelRealm::class.java)
        val allZonesTypes = viewModelZoneType.getZonesByClientId(clientId!!)*/


        // Set up RecyclerView and Adapter for Area
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, allClientAreaNames)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.mySpinner.adapter = adapter

        // Set up RecyclerView and Adapter fro PoisType
         poisTypeAdapter = PoisTypeAdapter(allClientPoisType)
         binding.rvPoisType.layoutManager = GridLayoutManager(requireActivity(), 5)
         binding.rvPoisType.adapter = poisTypeAdapter

        // Set up RecyclerView and Adapter fro PoisType
        trailCategoryAdapter = MapTrailCategoryAdapter(allClientTaskCategory)
        binding.rvTrailCategory.layoutManager = GridLayoutManager(requireActivity(), 4)
        binding.rvTrailCategory.adapter = trailCategoryAdapter

        // Set up RecyclerView and Adapter fro PoisType
        zonesTypeAdapter = ZonesTypeAdapter(allClientZonesType)
        val layoutManager = GridLayoutManager(requireActivity(), 4)
        binding.rvZonesType.layoutManager = layoutManager
        binding.rvZonesType.adapter = zonesTypeAdapter

    }

    override fun onStart() {
        super.onStart()
        val dialog = dialog as BottomSheetDialog
        val bottomSheet =
            dialog.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
        bottomSheet?.let {
            val behavior = BottomSheetBehavior.from(it)
            behavior.state = BottomSheetBehavior.STATE_COLLAPSED
            behavior.isFitToContents = true
            behavior.skipCollapsed = true
        }
    }

}