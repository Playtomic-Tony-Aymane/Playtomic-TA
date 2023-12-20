package com.example.playtomictonyaymane.ui.editProfile

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.NavHostFragment
import com.example.playtomictonyaymane.R
import com.example.playtomictonyaymane.databinding.FragmentEditprofileBinding
import com.example.playtomictonyaymane.ui.userprofile.UserProfileFragment
import com.example.playtomictonyaymane.ui.userprofile.UserProfileViewModel
import java.io.File
import java.io.FileOutputStream

class EditProfileFragment: Fragment() {

    private var  _binding: FragmentEditprofileBinding? = null
    private val binding get() = _binding!!

    private val getContent = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val imageUri = result.data?.data
            if (imageUri != null) {
                saveImageLocally(imageUri)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val dashboardViewModel =
            ViewModelProvider(this).get(EditViewModel::class.java)

        _binding = FragmentEditprofileBinding.inflate(inflater, container, false)

        return binding.root
    }

    @Suppress("DEPRECATION")
    @Deprecated("Deprecated in Java")
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                // The use of NavUtils may be deprecated in some scenarios in favor of using NavController
                // for Fragment-based navigation. However, to mark the overriding method as deprecated,
                // you would use the @Deprecated annotation like so:
                //@Deprecated("Use NavController for navigation instead")
                requireActivity().onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Back button behavior
        (activity as AppCompatActivity).supportActionBar?.apply {
            title = "Profile"
            setDisplayHomeAsUpEnabled(true)

            val userProfileViewModel: UserProfileViewModel by activityViewModels()

            binding.buttonSaveProfile.setOnClickListener{
                userProfileViewModel.apply {
                     firstName = binding.editTextFirstName.text.toString()
                     lastName = binding.editTextLastName.text.toString()
                     location = binding.editTextLocation.text.toString()
                     prefrence = binding.editTextPrefrence.text.toString()

                }
                findNavController().navigateUp()
                val userProdile = UserProfileFragment()
                val transaction = requireActivity().supportFragmentManager.beginTransaction()
                transaction.replace(R.id.container, userProdile)
                transaction.addToBackStack("UserProfile")
                transaction.commit()

            }
        }

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            val navHostFragment =
                requireActivity().supportFragmentManager.findFragmentById(com.example.playtomictonyaymane.R.id.nav_host_fragment_activity_main) as NavHostFragment
            val navController = navHostFragment.navController
            navController.popBackStack()
        }
        binding.buttonChangePhoto.setOnClickListener {
            checkGalleryPermissionAndOpen()
        }

        val userProfileViewModel: UserProfileViewModel by activityViewModels()
        binding.buttonSaveProfile.setOnClickListener{
            userProfileViewModel.apply {
                firstName = binding.editTextFirstName.text.toString()
                lastName = binding.editTextLastName.text.toString()
                location = binding.editTextLocation.text.toString()
                prefrence = binding.editTextPrefrence.text.toString()

                saveProfile()
            }
            //findNavController().navigateUp()
            //parentFragmentManager.popBackStack()
            //findNavController().navigate(R.id.action_navigation_editProfile_to_navigation_user)
            val navOptions = NavOptions.Builder()
                .setPopUpTo(R.id.editProfileFragment, true)
                .build()

            findNavController().navigate(R.id.navigation_user, null, navOptions)
        }

        // bindings
        binding.editTextFirstName.setText(userProfileViewModel.firstName)
        binding.editTextLastName.setText(userProfileViewModel.lastName)
        binding.editTextLocation.setText(userProfileViewModel.location)
        binding.editTextPrefrence.setText(userProfileViewModel.prefrence)
    }
    
    private fun checkGalleryPermissionAndOpen() {
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissions(arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), REQUEST_GALLERY_PERMISSION)
        } else {
            openGallery()
        }
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"
        getContent.launch(Intent.createChooser(intent, "Select Picture"))
    }

    private fun saveImageLocally(imageUri: Uri) {
        val inputStream = requireContext().contentResolver.openInputStream(imageUri)
        val file = File(requireContext().filesDir, "selected_image.jpg")
        val outputStream = FileOutputStream(file)
        inputStream?.copyTo(outputStream)
        inputStream?.close()
        outputStream.close()

        // Update de ImageView met de geselecteerde afbeelding
        binding.imageViewProfile.setImageURI(Uri.fromFile(file))
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_GALLERY_PERMISSION) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openGallery()
            }
        }
    }

    companion object {
        private const val REQUEST_GALLERY_PERMISSION = 101
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}