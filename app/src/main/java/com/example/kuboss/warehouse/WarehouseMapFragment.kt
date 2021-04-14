package com.example.kuboss.warehouse

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.ViewModelProvider
import com.example.kuboss.R
import com.example.kuboss.database.WarehouseDatabase
import kotlinx.android.synthetic.main.fragment_warehouse_map.*
import java.io.File

class WarehouseMapFragment : Fragment() {
    private lateinit var viewModel: WarehouseViewModel
    private lateinit var imgView: ImageView
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val application = requireNotNull(this.activity).application
        val dataSource = WarehouseDatabase.getInstance(application).warehouseDatabaseDao
        val viewModelFactory = WarehouseViewModelFactory(dataSource, application)
        viewModel = ViewModelProvider(
            requireActivity().viewModelStore, viewModelFactory).get(WarehouseViewModel::class.java)
        return inflater.inflate(R.layout.fragment_warehouse_map, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        imgView = view.findViewById(R.id.map_img)
        val file = File(requireContext().filesDir, "warehousemap")
        imgView.setImageBitmap(BitmapFactory.decodeFile(file.absolutePath))

        val getImg = registerForActivityResult(ActivityResultContracts.GetContent()){ uri: Uri? ->
            imgView.setImageURI(uri)
            val bitmap = MediaStore.Images.Media.getBitmap(requireContext().contentResolver, uri)

            requireContext().openFileOutput("warehousemap", Context.MODE_PRIVATE).use{
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, it)
            }


        }
        val changeMapBtn: Button = view.findViewById(R.id.change_map_btn)
        changeMapBtn.setOnClickListener{

            getImg.launch("image/*")

        }
    }

}