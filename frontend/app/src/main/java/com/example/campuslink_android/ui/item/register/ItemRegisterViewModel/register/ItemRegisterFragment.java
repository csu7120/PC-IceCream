package com.example.campuslink_android.ui.item.register

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.ViewModelProvider
import com.example.campuslink_android.core.network.TokenStore
import com.example.campuslink_android.databinding.FragmentItemRegisterBinding
import com.example.campuslink_android.domain.repository.ItemRepository
import java.io.File

class ItemRegisterFragment : Fragment() {

    private lateinit var binding: FragmentItemRegisterBinding
    private lateinit var viewModel: ItemRegisterViewModel

    private var selectedImageFile: File? = null

    // 갤러리 이미지 선택 런처
    private val imagePickerLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->

        if (result.resultCode == Activity.RESULT_OK) {
            val data: Intent? = result.data
            val uri: Uri? = data?.data

            if (uri != null) {
                binding.imagePreview.setImageURI(uri)
                selectedImageFile = uriToFile(uri)
            }
        }
    }

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        binding = FragmentItemRegisterBinding.inflate(inflater, container, false)

        val repository = provideItemRepository()
        viewModel = ItemRegisterViewModel(repository)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        // 이미지 선택 버튼
        binding.btnSelectImage.setOnClickListener {
            openGallery()
        }

        // 등록 버튼
        binding.btnRegister.setOnClickListener {
            registerItem()
        }

        observeViewModel()
    }

    private fun observeViewModel() {
        viewModel.loading.observe(viewLifecycleOwner) { loading ->
            // 필요하면 로딩 UI 추가
        }

        viewModel.success.observe(viewLifecycleOwner) { success ->
            if (success) {
                Toast.makeText(requireContext(), "등록 성공!", Toast.LENGTH_SHORT).show()
            }
        }

        viewModel.errorMessage.observe(viewLifecycleOwner) {
            Toast.makeText(requireContext(), it ?: "오류 발생", Toast.LENGTH_SHORT).show()
        }
    }

    private fun registerItem() {
        val title = binding.editTitle.text.toString()
        val description = binding.editDescription.text.toString()
        val price = binding.editPrice.text.toString().toDouble()
        val category = binding.editCategory.text.toString()
        val userId = TokenStore.userId

        val imageList = selectedImageFile?.let { listOf(it) } ?: emptyList()

        viewModel.registerItem(
                title = title,
                description = description,
                price = price,
                category = category,
                userId = userId,
                images = imageList
        )
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        imagePickerLauncher.launch(intent)
    }

    // Uri → File 변환
    private fun uriToFile(uri: Uri): File {
        val path = requireContext().contentResolver
                .query(uri, null, null, null, null)
                ?.use { cursor ->
                val index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
            cursor.moveToFirst()
            cursor.getString(index)
        }

        return File(path!!)
    }

    // Repository 주입 (임시)
    private fun provideItemRepository(): ItemRepository {
        // 이미 ItemRepositoryImpl는 DI 사용 안 하므로 직접 생성
        val api = com.example.campuslink_android.core.network.ApiClient.itemApi
        return com.example.campuslink_android.data.repository.ItemRepositoryImpl(api, TokenStore)
    }
}
