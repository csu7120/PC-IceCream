<?xml version="1.0" encoding="utf-8"?>
<ScrollView xmlns:android="http://schemas.android.com/apk/res/android"
android:layout_width="match_parent"
android:layout_height="match_parent">

    <LinearLayout
android:layout_width="match_parent"
android:padding="16dp"
android:layout_height="wrap_content"
android:orientation="vertical">

        <!-- 이미지 미리보기 -->
        <ImageView
android:id="@+id/imagePreview"
android:layout_width="match_parent"
android:layout_height="200dp"
android:background="#DDDDDD"
android:scaleType="centerCrop" />

        <!-- 이미지 선택 버튼 -->
        <Button
android:id="@+id/btnSelectImage"
android:layout_width="match_parent"
android:layout_height="wrap_content"
android:text="Select Image"
android:layout_marginTop="12dp" />

        <EditText
android:id="@+id/editTitle"
android:layout_width="match_parent"
android:layout_height="wrap_content"
android:hint="Title"
android:layout_marginTop="16dp" />

        <EditText
android:id="@++editDescription"
android:layout_width="match_parent"
android:layout_height="wrap_content"
android:hint="Description"
android:layout_marginTop="12dp" />

        <EditText
android:id="@+id/editPrice"
android:layout_width="match_parent"
android:layout_height="wrap_content"
android:hint="Price"
android:inputType="number"
android:layout_marginTop="12dp" />

        <EditText
android:id="@+id/editCategory"
android:layout_width="match_parent"
android:layout_height="wrap_content"
android:hint="Category"
android:layout_marginTop="12dp" />

        <!-- 등록 버튼 -->
        <Button
android:id="@+id/btnRegister"
android:layout_width="match_parent"
android:layout_height="wrap_content"
android:text="Register Item"
android:layout_marginTop="20dp" />
    </LinearLayout>
</ScrollView>
