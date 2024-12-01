package com.snofed.publicapp.maps

import RealmRepository
import StatusItem
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
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
import com.snofed.publicapp.utils.SharedViewModel
import dagger.hilt.android.AndroidEntryPoint
import io.realm.Realm


@AndroidEntryPoint
class FilterMapBottomSheetFragment : BottomSheetDialogFragment(),PoisTypeAdapter.OnItemSelectedListener  {
    private lateinit var sharedViewModel: SharedViewModel
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


    /*var allClientAreaNames : List<StatusItem> = emptyList()
    var allClientPoisType :  List<StatusItem> = emptyList()
    var allClientTaskCategory : List<StatusItem> = emptyList()
    var allClientZonesType : List<StatusItem> = emptyList()   */

    var allClientAreaNames : MutableList<StatusItem> = mutableListOf()
    var allClientPoisType : MutableList<StatusItem> = mutableListOf()
    var allClientTaskCategory : MutableList<StatusItem> = mutableListOf()
    var allClientZonesType : MutableList<StatusItem> = mutableListOf()

    private var selectedPoisIds: List<String> = emptyList()

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
        sharedViewModel = ViewModelProvider(requireActivity()).get(SharedViewModel::class.java)
        binding.closeButton.setOnClickListener {
            dismiss()
        }

        // Activity
        viewModelActivity = ViewModelProvider(this).get(ActivityViewModelRealm::class.java)
        val allActivities = viewModelActivity.getAllActivities()


        // Area
        viewModelArea = ViewModelProvider(this).get(AreaViewModelRealm::class.java)
        val clientAreas = viewModelArea.getAreasByClientId(clientId!!)
       // allClientAreaNames = allClientAreas.map { it.name!! }
        allClientAreaNames = clientAreas.map { area ->
            StatusItem(
                id = area.id!!,
                text = area.name ?: "No Name",
            )
        }.toMutableList()

        // POIs Type
        viewModelPoisType = ViewModelProvider(this).get(PoisTypeViewModelRealm::class.java)
        val allPOISTypes = viewModelPoisType.getAllPoiTypes()
        //allClientPoisType = allPOISTypes.map { it.iconPath!! }
        allClientPoisType = allPOISTypes.map { poisType ->
            StatusItem(
                id = poisType.id!!,
                text = poisType.name ?: "No Name",
                iconPath = poisType.iconPath ?: R.drawable.dinner.toString())

        }.toMutableList()


        //Task Category
        viewModelTaskCategory = ViewModelProvider(this).get(TaskCategoryViewModelRealm::class.java)
        val allTaskCategories = viewModelTaskCategory.getAllTaskCategories()
        //allClientTaskCategory = allTaskCategories.map { it.name!! }
        allClientTaskCategory = allTaskCategories.map { taskCategoryType ->
            StatusItem(
                id = taskCategoryType.id!!,
                text = taskCategoryType.name ?: "No Name",)
        }.toMutableList()



        //Zones Type
        viewModelZoneType = ViewModelProvider(this).get(ZoneTypeViewModelRealm::class.java)
        val allZoneTypes = viewModelZoneType.getAllZoneTypes()
        //allClientZonesType = allZoneTypes.map { it.name!! }
        allClientZonesType = allZoneTypes.map { zoneType ->
            StatusItem(
                id = zoneType.id!!,
                text = zoneType.name ?: "No Name",)
        }.toMutableList()

        //Zone
       /* viewModelZoneType = ViewModelProvider(this).get(ZoneTypeViewModelRealm::class.java)
        val allZonesTypes = viewModelZoneType.getZonesByClientId(clientId!!)*/


        val areaNamesList = allClientAreaNames.map { it.text }
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, areaNamesList)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.mySpinner.adapter = adapter
        val selectedAreaName = "Select"
        val selectedIndex = areaNamesList.indexOf(selectedAreaName)
        if (selectedIndex >= 0) { // Check if the value exists in the list
            binding.mySpinner.setSelection(selectedIndex)
        }
        binding.mySpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                val selectedStatusItem = allClientAreaNames[position] // Get the StatusItem for the selected position
                val selectedId = selectedStatusItem.id
                val selectedText = selectedStatusItem.text

                Log.d("SpinnerSelection", "Selected ID: $selectedId, Selected Name: $selectedText")
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // Handle no selection case if required
            }
        }
       /* // Set up RecyclerView and Adapter fro PoisType
         poisTypeAdapter = PoisTypeAdapter(allClientPoisType)
         binding.rvPoisType.layoutManager = GridLayoutManager(requireActivity(), 5)
         binding.rvPoisType.adapter = poisTypeAdapter*/


        // Set up RecyclerView and Adapter for PoisType
        poisTypeAdapter = PoisTypeAdapter(allClientPoisType) { selectedIdsString ->
            Log.d("poisTypeAdapter", "Comma-separated IDs: $selectedIdsString")
            // Update the shared ViewModel with the new selected IDs
            val selectedIds = selectedIdsString.split(",")
                .filter { it.isNotBlank() }  
                .mapNotNull { it }

            if (selectedIds.isNotEmpty()) {
                // Update the shared ViewModel with the new selected IDs
                sharedViewModel.updateSelectedIds(selectedIds)
            } else {
                Log.d("SelectedIDs", "No valid IDs selected")
            }

        }
        binding.rvPoisType.layoutManager = GridLayoutManager(requireActivity(), 5)
        binding.rvPoisType.adapter = poisTypeAdapter




        // Set up RecyclerView and Adapter fro PoisType
        trailCategoryAdapter = MapTrailCategoryAdapter(allClientTaskCategory) { selectedIdsString ->
            Log.d("trailCategoryAdapter", "Comma-separated IDs: $selectedIdsString")
            // Update the shared ViewModel with the new selected IDs
            val selectedIds = selectedIdsString.split(",")
                .filter { it.isNotBlank() }
                .mapNotNull { it }

            if (selectedIds.isNotEmpty()) {
                // Update the shared ViewModel with the new selected IDs
                sharedViewModel.updateSelectedTrailsIds(selectedIds)
            } else {
                Log.d("SelectedIDs", "No valid IDs selected")
            }

        }
        binding.rvTrailCategory.layoutManager = GridLayoutManager(requireActivity(), 2)
        binding.rvTrailCategory.adapter = trailCategoryAdapter

       /* trailCategoryAdapter = MapTrailCategoryAdapter(allClientTaskCategory)
        binding.rvTrailCategory.layoutManager = GridLayoutManager(requireActivity(), 2)
        binding.rvTrailCategory.adapter = trailCategoryAdapter
*/

        // Set up RecyclerView and Adapter fro PoisType
        zonesTypeAdapter = ZonesTypeAdapter(allClientZonesType) { selectedIdsString ->
            Log.d("trailCategoryAdapter", "Comma-separated IDs: $selectedIdsString")
            // Update the shared ViewModel with the new selected IDs
            val selectedIds = selectedIdsString.split(",")
                .filter { it.isNotBlank() }
                .mapNotNull { it }

            if (selectedIds.isNotEmpty()) {
                // Update the shared ViewModel with the new selected IDs
                sharedViewModel.updateSelectedZoneIds(selectedIds)
            } else {
                Log.d("SelectedIDs", "No valid IDs selected")
            }

        }
        val layoutManager = GridLayoutManager(requireActivity(), 2)
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

    override fun onItemsSelected(selectedIds: List<String>) {
        // Handle the selected IDs here, e.g., print them or pass them to another fragment
        selectedPoisIds = selectedIds
        // Pass the selected POI IDs to the SharedViewModel
        sharedViewModel.updateSelectedIds(selectedPoisIds)
        Log.d("SelectedPOIs", "Selected POIs IDs: $selectedPoisIds")
    }

}