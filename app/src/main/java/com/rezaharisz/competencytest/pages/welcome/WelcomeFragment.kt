@file:Suppress("DEPRECATION")

package com.rezaharisz.competencytest.pages.welcome

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.rezaharisz.competencytest.R
import com.rezaharisz.competencytest.data.local.model.UserLoginEntity
import com.rezaharisz.competencytest.databinding.FragmentWelcomeBinding
import com.rezaharisz.competencytest.helper.ViewModelFactory
import com.rezaharisz.competencytest.pages.on_board.OnBoardFragment
import com.rezaharisz.competencytest.utils.*
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.*


class WelcomeFragment : Fragment(), View.OnClickListener {

    private var _binding: FragmentWelcomeBinding? = null
    private val binding get() = _binding
    private var user: UserLoginEntity? = null
    private var imageUri: Uri? = null
    private var imageBitmap: Bitmap? = null
    private lateinit var viewModel: WelcomeViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentWelcomeBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (activity != null){
            viewModel = obtainViewModel(activity as AppCompatActivity)

            user = UserLoginEntity()

            binding?.btnCheckPalindrome?.setOnClickListener(this)
            binding?.ivProfile?.setOnClickListener(this)
            binding?.btnNext?.setOnClickListener(this)
        }
    }

    private fun checkPalindrome(value: String): Boolean{
        var temp = ""

        for (i in value.length - 1 downTo 0) {
            temp += value[i]
        }

        println(temp)

        return if (temp == value){
            Toast.makeText(context, "$value is Palindrome", Toast.LENGTH_SHORT).show()
            true
        } else {
            Toast.makeText(context, "$value is Not Palindrome", Toast.LENGTH_SHORT).show()
            false
        }
    }

    override fun onClick(v: View) {
        when(v.id){
            R.id.btn_checkPalindrome -> checkPalindrome(binding?.edPalindrome?.text.toString())

            R.id.iv_profile -> {
                val alertDialog = AlertDialog.Builder(context)
                val dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_image_picker, null)
                val alert = alertDialog.create()
                alert.setView(dialogView)
                alert.setCancelable(true)
                alert.show()

                val btnCamera = dialogView.findViewById<TextView>(R.id.btn_camera)
                val btnGallery = dialogView.findViewById<TextView>(R.id.btn_gallery)
                val btnCancel = dialogView.findViewById<Button>(R.id.btn_cancel)

                btnCancel.setOnClickListener { alert.cancel() }
                btnGallery.setOnClickListener {
                    openGallery()
                    alert.cancel()
                }
                btnCamera.setOnClickListener {
                    openCamera()
                    alert.cancel()
                }
            }

            R.id.btn_next -> {
                val name = binding?.edName?.text.toString().trim()

                if (imageBitmap != null || imageUri != null && name.isNotEmpty()){
                    user.let {
                        it?.name = name
                        it?.imageProfile = imageUri.toString()
                    }

                    viewModel.insertUser(user as UserLoginEntity)
                    storeImage()
                    sendUserName(name)

                    parentFragmentManager.beginTransaction().apply {
                        replace(R.id.frame_container, OnBoardFragment(), OnBoardFragment::class.java.canonicalName)
                        commit()
                    }
                }
                else if (name.isEmpty()){
                    binding?.edName?.error = StringBuilder("Field can not be blank")
                } else{
                    Toast.makeText(context, "Please choose profile picture", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when{
            resultCode == Activity.RESULT_OK && requestCode == LOCAL_IMAGE -> {
                imageUri = data?.data

                binding?.let {
                    Glide.with(requireContext())
                        .load(imageUri.toString())
                        .override(90, 90)
                        .into(it.ivProfile)
                }
            }

            resultCode == Activity.RESULT_OK && requestCode == CAMERA_IMAGE -> {
                imageBitmap = data?.extras?.get("data") as Bitmap
                binding?.ivProfile?.let {
                    Glide.with(requireContext())
                        .load(imageBitmap)
                        .override(90, 90)
                        .into(it)
                }
            }
        }
    }

    private fun obtainViewModel(activity: AppCompatActivity): WelcomeViewModel{
        val factory = ViewModelFactory.getInstance(activity.application)
        return ViewModelProvider(activity, factory)[WelcomeViewModel::class.java]
    }

    private fun openGallery(){
        if(context?.let { ActivityCompat.checkSelfPermission(it, Manifest.permission.READ_EXTERNAL_STORAGE) }
            == PackageManager.PERMISSION_DENIED){
            val permission = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
            requestPermissions(permission, PERMISSION)
        } else{
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, LOCAL_IMAGE)
        }
    }

    private fun openCamera(){
        if (context?.let { ActivityCompat.checkSelfPermission(it, Manifest.permission.CAMERA) }
            == PackageManager.PERMISSION_DENIED){
            val permission = arrayOf(Manifest.permission.CAMERA)
            requestPermissions(permission, PERMISSION)
        } else{
            val takePicture = Intent(MediaStore.ACTION_IMAGE_CAPTURE)

            try {
                startActivityForResult(takePicture, CAMERA_IMAGE)
            } catch (e: Exception){
                Log.e("CAMERA_EXCEPTION", e.message.toString())
            }
        }
    }

    private fun storeImage(){
        val currentTime = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val dir: File? = context?.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        val imgFile = File(dir, "User - $currentTime .jpg")

        imageBitmap?.compress(Bitmap.CompressFormat.JPEG, 90, FileOutputStream(imgFile))

        val intent = Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE).apply {
            data = Uri.fromFile(imgFile)
        }

        context?.sendBroadcast(intent)
    }

    private fun sendUserName(name: String){
        val sharedPreferences = context?.getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE)
        val editor = sharedPreferences?.edit()

        editor?.putString(USER_NAME, name)
        editor?.apply()
    }

}