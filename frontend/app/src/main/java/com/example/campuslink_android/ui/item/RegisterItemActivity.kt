package com.example.campuslink_android.ui.item

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.campuslink_android.databinding.ActivityRegisterItemBinding
import com.example.campuslink_android.core.network.ApiClient
import com.example.campuslink_android.core.network.TokenStore
import com.example.campuslink_android.data.repository.ItemRepositoryImpl
import com.example.campuslink_android.data.dao.ItemApi
import com.example.campuslink_android.ui.item.RegisterItemViewModel
import com.example.campuslink_android.ui.item.RegisterItemViewModelFactory
import java.io.File

class RegisterItemActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterItemBinding
    private lateinit var viewModel: RegisterItemViewModel

    private var selectedImageFile: File? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRegisterItemBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val itemApi = ApiClient.create(ItemApi::class.java)
        val repository = ItemRepositoryImpl(itemApi, TokenStore)
        viewModel = ViewModelProvider(
            this,
            RegisterItemViewModelFactory(repository)
        )[RegisterItemViewModel::class.java]

        binding.btnPickImage.setOnClickListener {
            pickImage()
        }

        binding.btnSubmit.setOnClickListener {
            submitItem()
        }

        observeViewModel()
    }

    private fun pickImage() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"
        startActivityForResult(intent, 2000)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 2000 && resultCode == Activity.RESULT_OK) {
            val uri = data?.data ?: return
            binding.imgPreview.setImageURI(uri)

            selectedImageFile = uriToFile(uri)
        }
    }

    private fun uriToFile(uri: Uri): File {
        val path = MediaStore.Images.Media.DATA
        val cursor = contentResolver.query(uri, arrayOf(path), null, null, null)
        cursor?.moveToFirst()
        val index = cursor?.getColumnIndex(path) ?: 0
        val filePath = cursor?.getString(index) ?: ""
        cursor?.close()
        return File(filePath)
    }

    private fun submitItem() {
        val title = binding.edtTitle.text.toString()
        val desc = binding.edtDescription.text.toString()
        val price = binding.edtPrice.text.toString().toDouble()
        val category = binding.edtCategory.text.toString()
        val userId = TokenStore.getUserId() ?: 1

        viewModel.registerItem(title, desc, price, category, userId, selectedImageFile)
    }

    private fun observeViewModel() {
        viewModel.registerResult.observe(this) { result ->
            result.onSuccess {
                Toast.makeText(this, "등록 완료!", Toast.LENGTH_SHORT).show()
                finish()
            }
            result.onFailure {
                Toast.makeText(this, "등록 실패!", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
