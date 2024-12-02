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
import androidx.core.content.ContextCompat
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
import com.snofed.publicapp.models.realmModels.Poi
import com.snofed.publicapp.models.realmModels.Resource
import com.snofed.publicapp.models.realmModels.Trail
import com.snofed.publicapp.models.realmModels.Zone
import com.snofed.publicapp.ui.clubsubmember.ViewModelClub.ActivityViewModelRealm
import com.snofed.publicapp.ui.clubsubmember.ViewModelClub.AreaViewModelRealm
import com.snofed.publicapp.ui.clubsubmember.ViewModelClub.PoisTypeViewModelRealm
import com.snofed.publicapp.ui.clubsubmember.ViewModelClub.PoisViewModelRealm
import com.snofed.publicapp.ui.clubsubmember.ViewModelClub.TaskCategoryViewModelRealm
import com.snofed.publicapp.ui.clubsubmember.ViewModelClub.ZoneTypeViewModelRealm
import com.snofed.publicapp.utils.SharedViewModel
import com.snofed.publicapp.utils.enums.SyncActionEnum
import dagger.hilt.android.AndroidEntryPoint
import io.realm.Realm


@AndroidEntryPoint
class FilterMapBottomSheetFragment : BottomSheetDialogFragment()
                                    ,PoisTypeAdapter.OnItemSelectedListener
                                    ,ZonesTypeAdapter.OnItemSelectedListener
                                    {

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

    //For Client Data
    private lateinit var viewModelPois: PoisViewModelRealm



    private lateinit var poisTypeAdapter: PoisTypeAdapter
    private lateinit var trailCategoryAdapter: MapTrailCategoryAdapter
    private lateinit var zonesTypeAdapter: ZonesTypeAdapter


    /*var allClientAreaNames : List<StatusItem> = emptyList()
    var allClientPoisType :  List<StatusItem> = emptyList()
    var allClientTaskCategory : List<StatusItem> = emptyList()
    var allClientZonesType : List<StatusItem> = emptyList()   */

    var allClientAreas : MutableList<StatusItem> = mutableListOf()
    var allPoisType : MutableList<StatusItem> = mutableListOf()
    var allTaskCategory : MutableList<StatusItem> = mutableListOf()
    var allZonesType : MutableList<StatusItem> = mutableListOf()
    var allActivity : MutableList<StatusItem> = mutableListOf()

    // Client MAP Data
    var clientTrails    : MutableList<Trail> = mutableListOf()
    var clientPois      : MutableList<Poi> = mutableListOf()
    var clientZones     : MutableList<Zone> = mutableListOf()
    var clientResources : MutableList<Resource> = mutableListOf()

    private var selectedAreaIds: List<String> = emptyList()
    private var selectedPoisIds: List<String> = emptyList()
    private var selectedTrailsIds: List<String> = emptyList()
    private var selectedZoneIds: List<String> = emptyList()


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
        //Add All Type in allActivity
        allActivity.add(StatusItem(
            id = "0",
            text = "ALL",
            iconPath = R.drawable.dinner.toString())
        )
        allActivity.addAll(allActivities.map { activity ->
            StatusItem(
                id = activity.id!!,
                text = activity.name ?: "No Name",)
        }.toMutableList())


        // Area
        viewModelArea = ViewModelProvider(this).get(AreaViewModelRealm::class.java)
        val clientAreas = viewModelArea.getAreasByClientId(clientId!!)
       // allClientAreaNames = allClientAreas.map { it.name!! }
        allClientAreas.add(StatusItem(
            id = "0",
            text = "All Areas",
        ))
        allClientAreas.addAll(clientAreas.map { area ->
            StatusItem(
                id = area.id!!,
                text = area.name ?: "No Name",
            )
        }.toMutableList())


        // Poi for Client
        viewModelPois = ViewModelProvider(this).get(PoisViewModelRealm::class.java)
        val clientPois = viewModelPois.getDistinctPioTypesByClientId(clientId!!)

        // POIType
        viewModelPoisType = ViewModelProvider(this).get(PoisTypeViewModelRealm::class.java)
        val allPoiTypes = viewModelPoisType.getAllPoiTypes()

        var filterPoisType = allPoiTypes.filter { poiType -> clientPois.contains(poiType.id) && poiType.syncAction != SyncActionEnum.DELETED.getValue() }
        //Add All Type in allZonesType
        allPoisType.add(StatusItem(
            id = "0",
            text = "ALL",
            iconPath = ContextCompat.getString(requireContext(), R.drawable.filter_all)
        ))

        allPoisType.addAll(filterPoisType.map { poiType ->
            StatusItem(
                id = poiType.id!!,
                text = poiType.name ?: "No Name",
                iconPath = poiType.iconPath ?: R.drawable.drawer_rounded_corners.toString())

        }.toMutableList())


        //Task Category
        viewModelTaskCategory = ViewModelProvider(this).get(TaskCategoryViewModelRealm::class.java)
        val allTaskCategories = viewModelTaskCategory.getAllTaskCategories()
        //Add All Type in allZonesType
        allTaskCategory.add(StatusItem(
            id = "0",
            text = "ALL"
        ))
        allTaskCategory.addAll(allTaskCategories.map { taskCategoryType ->
            StatusItem(
                id = taskCategoryType.id!!,
                text = taskCategoryType.name ?: "No Name",)
        }.toMutableList())



        //Zones Type
        viewModelZoneType = ViewModelProvider(this).get(ZoneTypeViewModelRealm::class.java)
        val allZoneTypes = viewModelZoneType.getAllZoneTypes()
        //Add All Type in allZonesType
        allZonesType.add(StatusItem(
            id = "0",
            text = "ALL"
        ))
        allZonesType.addAll(allZoneTypes.map { zoneType ->
            StatusItem(
                id = zoneType.id!!,
                text = zoneType.name ?: "No Name",)
        }.toMutableList())

        //Zone
       /* viewModelZoneType = ViewModelProvider(this).get(ZoneTypeViewModelRealm::class.java)
        val allZonesTypes = viewModelZoneType.getZonesByClientId(clientId!!)*/

        val selectedAreaId = sharedViewModel.getSelectedAreaId()

        val areaNamesList = allClientAreas.map { it.text }
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, areaNamesList)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.mySpinner.adapter = adapter
        val selectedAreaName = selectedAreaId.text
        val selectedIndex = areaNamesList.indexOf(selectedAreaName)
        if (selectedIndex >= 0) { // Check if the value exists in the list
            binding.mySpinner.setSelection(selectedIndex)
        }
        binding.mySpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                val selectedStatusItem = allClientAreas[position] // Get the StatusItem for the selected position
                val selectedId = selectedStatusItem.id
                val selectedText = selectedStatusItem.text
                val areaId = selectedId
                sharedViewModel.updateSelectedAreaIds(areaId,selectedText)
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

        // Get the selected IDs (either from SharedPreferences or ViewModel)
        val selectedPoisIds = sharedViewModel.getSelectedIds()
        val selectedTrailsIds = sharedViewModel.getSelectedTrailsIds()
        val selectedZoneIds = sharedViewModel.getSelectedZoneIds()

        // Set up RecyclerView and Adapter for PoisType
        poisTypeAdapter = PoisTypeAdapter(allPoisType,selectedPoisIds) { selectedIdsString ->
            Log.d("poisTypeAdapter", "Comma-separated IDs: $selectedIdsString")
            // Update the shared ViewModel with the new selected IDs
            Log.d("poisTypeAdapter", "Comma-separated IDs: $selectedIdsString")
            val selectedIds = selectedIdsString.split(",").filter { it.isNotBlank() }
            sharedViewModel.updateSelectedIds(selectedIds)

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
        trailCategoryAdapter = MapTrailCategoryAdapter(allActivity,selectedTrailsIds) { selectedIdsString ->
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
        zonesTypeAdapter = ZonesTypeAdapter(allZonesType,selectedZoneIds) { selectedIdsString ->
            Log.d("trailCategoryAdapter", "Comma-separated IDs: $selectedIdsString")
            // Update the shared ViewModel with the new selected IDs
            val selectedIds = selectedIdsString.split(",")
                .filter { it.isNotBlank() }
                .mapNotNull { it }
            sharedViewModel.updateSelectedZoneIds(selectedIds)
            /*if (selectedIds.isNotEmpty()) {
                // Update the shared ViewModel with the new selected IDs
                sharedViewModel.updateSelectedZoneIds(selectedIds)
            } else {
                Log.d("SelectedIDs", "No valid IDs selected")
            }*/

        }
        val layoutManager = GridLayoutManager(requireActivity(),3)
        binding.rvZonesType.layoutManager = layoutManager
        binding.rvZonesType.adapter = zonesTypeAdapter

    }

    override fun onStart() {
        super.onStart()
        val dialog = dialog as BottomSheetDialog
        val bottomSheet = dialog.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
        bottomSheet?.let {
            val behavior = BottomSheetBehavior.from(it)
            behavior.state = BottomSheetBehavior.STATE_COLLAPSED
            behavior.isFitToContents = true
            behavior.skipCollapsed = true
        }
    }

    override fun onItemsSelected(selectedIds: List<String>, type: String) {
        // Handle the selected IDs here, e.g., print them or pass them to another fragment
        if(type == "zone"){
            selectedZoneIds = selectedIds
            // Pass the selected ZONE IDs to the SharedViewModel
            sharedViewModel.updateSelectedZoneIds(selectedZoneIds)
        }
        else if(type == "pois"){
            selectedPoisIds = selectedIds
            // Pass the selected POI IDs to the SharedViewModel
            sharedViewModel.updateSelectedIds(selectedPoisIds)
        }
        else if(type == "trail"){
            selectedTrailsIds = selectedIds
            // Pass the selected TRAILS IDs to the SharedViewModel
            sharedViewModel.updateSelectedTrailsIds(selectedTrailsIds)
        }

        Log.d("SelectedPOIs", "Selected POIs IDs: $selectedPoisIds")
    }

}