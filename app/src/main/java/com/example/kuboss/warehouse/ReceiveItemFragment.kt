package com.example.kuboss.warehouse

import android.graphics.Bitmap
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.kuboss.R
import com.example.kuboss.database.Material
import com.example.kuboss.database.WarehouseDatabase
import com.example.kuboss.databinding.FragmentReceiveItemBinding
import com.google.zxing.BarcodeFormat
import com.google.zxing.EncodeHintType
import com.google.zxing.qrcode.QRCodeWriter

class ReceiveItemFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val binding: FragmentReceiveItemBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_receive_item, container, false)
        val application = requireNotNull(this.activity).application
        val dataSource = WarehouseDatabase.getInstance(application).warehouseDatabaseDao
        val viewModelFactory = ReceiveItemViewModelFactory(dataSource, application)
        val receiveItemViewModel = ViewModelProvider(
            this, viewModelFactory
        ).get(ReceiveItemViewModel::class.java)

        binding.lifecycleOwner = this
        binding.receiveItemViewModel = receiveItemViewModel


        binding.receiveMaterialId.editText?.setText(receiveItemViewModel.getNewMaterialID())
        binding.receiveConfirmBtn.setOnClickListener {
            val receiveId = binding.receiveMaterialId.editText?.text.toString()
            val receiveSKU = binding.receiveSku.editText?.text.toString()
            val receiveName = binding.receiveName.editText?.text.toString()
            val receiveQty = binding.receiveQuantity.editText?.text.toString().toInt()

            val newReceiveMat = Material(receiveId, receiveSKU, receiveName, receiveQty, null)
            receiveItemViewModel.storeMaterial(newReceiveMat)

            val img: Bitmap = getQrCodeBitmap(receiveId, receiveSKU, receiveName, receiveQty.toString())
            binding.qrView.setImageBitmap(img)
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


}

