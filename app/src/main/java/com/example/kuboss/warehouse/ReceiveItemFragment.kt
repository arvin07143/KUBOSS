package com.example.kuboss.warehouse

import android.app.Activity
import android.content.ContentValues
import android.graphics.Bitmap
import android.graphics.Color
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.adapter.FragmentViewHolder
import androidx.viewpager2.widget.ViewPager2
import com.example.kuboss.R
import com.example.kuboss.adapter.ReceivedItemAdapter
import com.example.kuboss.database.Material
import com.example.kuboss.database.WarehouseDatabase
import com.example.kuboss.databinding.FragmentReceiveItemBinding
import com.example.kuboss.databinding.FragmentReceiveItemFormBinding
import com.example.kuboss.databinding.FragmentReceiveItemListBinding
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.google.zxing.BarcodeFormat
import com.google.zxing.EncodeHintType
import com.google.zxing.qrcode.QRCodeWriter
import java.io.ByteArrayOutputStream
import java.io.File
import java.util.*

class ReceiveItemFragment : Fragment() {
    private lateinit var viewPager: ViewPager2
    private lateinit var tabLayout: TabLayout
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val binding: FragmentReceiveItemBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_receive_item, container, false)

        viewPager = binding.pager
        tabLayout = binding.tabs

        return binding.root
    }
    private inner class PagerAdapter(fragment:Fragment) : FragmentStateAdapter(fragment){

        private val mFragments = arrayOf(ReceiveItemForm(),ReceiveItemList())

        override fun getItemCount(): Int = mFragments.size

        override fun createFragment(position: Int): Fragment {
            Log.e("ATTACH","ATTACH $position")
            return mFragments[position]
        }

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewPager.adapter = PagerAdapter(this)
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            when(position){
                0 -> tab.text = "New Shipment"
                1 -> tab.text = "Received Shipments"
            }
        }.attach()
    }
}


    class ReceiveItemForm : Fragment(){
        override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View {

            val binding: FragmentReceiveItemFormBinding =
                DataBindingUtil.inflate(inflater, R.layout.fragment_receive_item_form, container, false)
            val application = requireNotNull(this.activity).application
            val dataSource = WarehouseDatabase.getInstance(application).warehouseDatabaseDao
            val viewModelFactory = ReceiveItemViewModelFactory(dataSource, application)
            val receiveItemViewModel = ViewModelProvider(
                this, viewModelFactory
            ).get(ReceiveItemViewModel::class.java)

            binding.lifecycleOwner = this
            binding.receiveItemViewModel = receiveItemViewModel

            binding.receiveConfirmBtn.tag = "CONFIRM" //Confirm
            binding.receiveMaterialId.editText?.setText(receiveItemViewModel.getNewMaterialID())
            binding.receiveConfirmBtn.setOnClickListener {
                if (binding.receiveConfirmBtn.tag == "CONFIRM"){ //Confirm
                    binding.receiveSku.isEnabled = false
                    binding.receiveName.isEnabled = false
                    binding.receiveQuantity.isEnabled = false

                    val receiveId = binding.receiveMaterialId.editText?.text.toString()
                    val receiveSKU = binding.receiveSku.editText?.text.toString()
                    val receiveName = binding.receiveName.editText?.text.toString()
                    val receiveQty = binding.receiveQuantity.editText?.text.toString().toInt()

                    val newReceiveMat = Material(receiveId, receiveSKU, receiveName, receiveQty, null)
                    receiveItemViewModel.storeMaterial(newReceiveMat)

                    val img: Bitmap = getQrCodeBitmap(receiveId, receiveSKU, receiveName, receiveQty.toString())
                    binding.qrView.setImageBitmap(img)
                    saveImage(img, this.activity!!,receiveId)
                    binding.receiveConfirmBtn.text = getString(R.string.print)
                    binding.receiveConfirmBtn.tag = "PRINT"
                    binding.receiveCancelBtn.tag = "CREATE NEW"
                    binding.receiveCancelBtn.text = getString(R.string.create_new)
                } else{ //Print
                    // print logic
                }

            }

            binding.receiveCancelBtn.setTag("CANCEL") //Cancel
            binding.receiveCancelBtn.setOnClickListener{
                if (binding.receiveCancelBtn.tag == "CANCEL"){
                    findNavController().navigate(R.id.action_receiveMatFragment_to_warehouseFragment)
                } else{
                    binding.receiveMaterialId.editText?.setText(receiveItemViewModel.getNewMaterialID())
                    binding.receiveSku.isEnabled = true
                    binding.receiveSku.editText?.setText("")
                    binding.receiveName.isEnabled = true
                    binding.receiveName.editText?.setText("")
                    binding.receiveQuantity.isEnabled = true
                    binding.receiveQuantity.editText?.setText("")
                    binding.receiveConfirmBtn.tag = "CONFIRM" //Confirm
                    binding.receiveConfirmBtn.text = getString(R.string.confirm)
                    binding.receiveCancelBtn.tag = "CANCEL"
                    binding.receiveCancelBtn.text = getString(R.string.cancel)
                }
            }
            return binding.root
        }


        private fun getQrCodeBitmap(
            receiveId: String,
            receiveSKU: String,
            receiveName: String,
            receiveQty: String
        ): Bitmap {
            val qrCodeContent = "$receiveId,$receiveSKU,$receiveName,$receiveQty"
            val hints = hashMapOf<EncodeHintType, Int>().also {
                it[EncodeHintType.MARGIN] = 1
            } // Make the QR code buffer border narrower
            val size = 512 //pixels
            val bits = QRCodeWriter().encode(qrCodeContent, BarcodeFormat.QR_CODE, size, size, hints)
            return Bitmap.createBitmap(size, size, Bitmap.Config.RGB_565).also {
                for (x in 0 until size) {
                    for (y in 0 until size) {
                        it.setPixel(x, y, if (bits[x, y]) Color.BLACK else Color.WHITE)
                    }
                }
            }
        }

        fun saveImage(bmpImage:Bitmap, activity: Activity,fileName:String) {

            ByteArrayOutputStream().apply {
                bmpImage.compress(Bitmap.CompressFormat.JPEG, 100, this)
            }

            val imageFile =  File("${activity.getExternalFilesDir(Environment.DIRECTORY_PICTURES)},$fileName+QR.jpg/")

            if (!imageFile.exists()) {

                val contentResolver = ContentValues().apply {
                    put(MediaStore.Images.Media.DATE_ADDED, System.currentTimeMillis())
                    put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
                    put(MediaStore.Images.Media.DATA, imageFile.absolutePath)
                }

                activity.contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentResolver).apply {
                    bmpImage.compress(Bitmap.CompressFormat.JPEG, 100, activity.contentResolver.openOutputStream(this!!))
                }


                Toast.makeText(activity, "saved", Toast.LENGTH_SHORT).show()
            } else
                Toast.makeText(activity, "Already saved", Toast.LENGTH_SHORT).show()
        }
    }

    class ReceiveItemList : Fragment() {
        override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View {
            val binding : FragmentReceiveItemListBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_receive_item_list, container, false)
            val application = requireNotNull(this.activity).application
            val dataSource = WarehouseDatabase.getInstance(application).warehouseDatabaseDao
            val viewModelFactory = ReceiveItemViewModelFactory(dataSource, application)
            val receiveItemViewModel = ViewModelProvider(
                this, viewModelFactory
            ).get(ReceiveItemViewModel::class.java)

            binding.lifecycleOwner = this
            binding.receiveItemViewModel = receiveItemViewModel

            val adapter = ReceivedItemAdapter()
            binding.receiveItemDetails.adapter = adapter

            receiveItemViewModel.receivedItems.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
                it?.let {
                    adapter.dataset = it
                }
            })

            return binding.root
        }
    }


