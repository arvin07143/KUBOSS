package com.example.kuboss.warehouse

import android.app.Activity
import android.content.ContentValues
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.print.PrintHelper
import androidx.viewpager2.adapter.FragmentStateAdapter
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
import kotlinx.android.synthetic.main.fragment_receive_item_form.view.*
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
        lateinit var binding: FragmentReceiveItemFormBinding
        override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View {

            binding =FragmentReceiveItemFormBinding.inflate(layoutInflater)
            val application = requireNotNull(this.activity).application
            val dataSource = WarehouseDatabase.getInstance(application).warehouseDatabaseDao
            val viewModelFactory = ReceiveItemViewModelFactory(dataSource, application)
            val receiveItemViewModel = ViewModelProvider(
                this, viewModelFactory
            ).get(ReceiveItemViewModel::class.java)

            binding.lifecycleOwner = this
            binding.receiveItemViewModel = receiveItemViewModel

            binding.receiveMaterialId.editText?.setText(receiveItemViewModel.getNewMaterialID())

            savedInstanceState?.let {
                binding.receiveMaterialId.editText?.setText(it.getString("materialID").toString())
                binding.receiveName.editText?.setText(it.getString("materialName").toString())
                binding.receiveSku.editText?.setText(it.getString("sku").toString())
                binding.receiveQuantity.editText?.setText(it.getString("qty").toString())
                binding.qrView.setImageBitmap(it.getParcelable("bmp"))
                binding.receiveCancelBtn.tag = it.getString("btnCancelTag")
                binding.receiveConfirmBtn.tag = it.getString("btnConfirmTag")
                if (binding.receiveCancelBtn.tag == "CREATE NEW"){
                    binding.receiveSku.isEnabled = false
                    binding.receiveName.isEnabled = false
                    binding.receiveQuantity.isEnabled = false
                }
            }

            binding.receiveConfirmBtn.tag = "CONFIRM" //Confirm
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
                    doPhotoPrint((binding.qrView.drawable as BitmapDrawable).bitmap,binding.receiveMaterialId.editText?.text.toString())
                    Toast.makeText(this.context,"QR Code has been printed",Toast.LENGTH_SHORT).show()
                }

            }

            binding.receiveCancelBtn.tag = "CANCEL" //Cancel
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
                    binding.qrView.setImageBitmap(null)
                    binding.receiveConfirmBtn.tag = "CONFIRM" //Confirm
                    binding.receiveConfirmBtn.text = getString(R.string.confirm)
                    binding.receiveCancelBtn.tag = "CANCEL"
                    binding.receiveCancelBtn.text = getString(R.string.cancel)
                }
            }
            return binding.root
        }

        override fun onSaveInstanceState(outState: Bundle) {
            super.onSaveInstanceState(outState)
            outState.putString("materialID", binding.receiveMaterialId.editText?.text.toString())
            outState.putString("materialName",binding.receiveName.editText?.text.toString())
            outState.putString("sku",binding.receiveSku.editText?.text.toString())
            outState.putString("qty", binding.receiveQuantity.editText?.text.toString())
            var bmp: Bitmap? = null
            bmp = if (binding.qrView.drawable == null){
                null
            } else{
                (binding.qrView.drawable as BitmapDrawable).bitmap
            }
            outState.putParcelable("bmp",bmp)
            outState.putString("btnCancelTag",binding.receiveCancelBtn.tag.toString())
            outState.putString("btnConfirmTag",binding.receiveConfirmBtn.tag.toString())
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

        private fun doPhotoPrint(bmpImage: Bitmap,printJobName:String) {
            activity?.also { context ->
                PrintHelper(context).apply {
                    scaleMode = PrintHelper.SCALE_MODE_FIT
                }.also { printHelper ->
                    val bitmap = bmpImage
                    printHelper.printBitmap("$printJobName Print Job", bitmap)
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


