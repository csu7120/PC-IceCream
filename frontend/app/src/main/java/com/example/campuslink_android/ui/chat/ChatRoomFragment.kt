package com.example.campuslink_android.ui.chat

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.campuslink_android.R
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File

class ChatRoomFragment : Fragment(R.layout.fragment_chat_room) {

    private val vm: ChatViewModel by viewModels { ChatViewModelFactory() }
    private lateinit var adapter: ChatMessageAdapter
    private var roomId: Int = 0

    // 이미지 선택기
    private val pickImage =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            uri?.let {
                Log.d("ChatDebug", "이미지 선택됨: $uri")
                uploadImage(it)
            }
        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        roomId = arguments?.getInt("roomId") ?: 0
        Log.d("ChatDebug", "채팅방 입장 roomId = $roomId")

        val rv = view.findViewById<RecyclerView>(R.id.rvMessages)
        val et = view.findViewById<EditText>(R.id.etMessage)
        val btnSend = view.findViewById<Button>(R.id.btnSend)
        val btnImage = view.findViewById<Button>(R.id.btnImage)
        val btnLocation = view.findViewById<Button>(R.id.btnLocation)

        rv.layoutManager = LinearLayoutManager(requireContext())
        adapter = ChatMessageAdapter()
        rv.adapter = adapter

        vm.loadMessages(roomId)

        vm.messages.observe(viewLifecycleOwner) { list ->
            adapter.submitList(list)
            if (list.isNotEmpty()) rv.scrollToPosition(list.lastIndex)
        }

        // -------------------------------
        //  위치 버튼
        // -------------------------------
        btnLocation.setOnClickListener {
            android.util.Log.d("ChatDebug", "위치 버튼 클릭됨")

            val uri = Uri.parse("geo:37.5665,126.9780?q=Seoul Station")  // 임시로 서울역
            val intent = Intent(Intent.ACTION_VIEW, uri)
            intent.setPackage("com.google.android.apps.maps")

            val canOpen = intent.resolveActivity(requireContext().packageManager) != null
            android.util.Log.d("ChatDebug", "Google Maps 실행 가능 여부: $canOpen")

            if (canOpen) startActivity(intent)
            else android.util.Log.e("ChatDebug", "지도 앱을 실행할 수 없습니다.")
        }


        // -------------------------------
        //  텍스트 메시지 전송
        // -------------------------------
        btnSend.setOnClickListener {
            val text = et.text.toString().trim()
            if (text.isNotEmpty()) {
                Log.d("ChatDebug", "텍스트 전송: $text")
                vm.sendText(roomId, text)
                et.text.clear()
            }
        }

        // -------------------------------
        //  이미지 선택 버튼
        // -------------------------------
        btnImage.setOnClickListener {
            Log.d("ChatDebug", "이미지 선택 버튼 클릭됨")
            pickImage.launch("image/*")
        }
    }

    // ----------------------------------------------------
    // 이미지 업로드 처리
    // ----------------------------------------------------
    private fun uploadImage(uri: Uri) {
        val file = getFileFromUri(uri, requireContext())
        Log.d("ChatDebug", "서버로 전송할 파일: ${file.absolutePath}")

        val requestFile = file.asRequestBody("image/*".toMediaType())
        val body = MultipartBody.Part.createFormData("image", file.name, requestFile)

        vm.sendImage(roomId, body)
    }

    private fun getFileFromUri(uri: Uri, context: Context): File {
        val input = context.contentResolver.openInputStream(uri)
        val file = File(context.cacheDir, "img_${System.currentTimeMillis()}.jpg")

        file.outputStream().use { output ->
            input?.copyTo(output)
        }

        Log.d("ChatDebug", "URI → File 변환 완료: ${file.absolutePath}")
        return file
    }

    companion object {
        fun newInstance(roomId: Int) = ChatRoomFragment().apply {
            arguments = Bundle().apply { putInt("roomId", roomId) }
        }
    }
}
