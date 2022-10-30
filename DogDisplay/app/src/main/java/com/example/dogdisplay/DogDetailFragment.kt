package com.example.dogdisplay

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.activityViewModels
import com.example.dogdisplay.databinding.FragmentDogDetailBinding
import com.example.dogdisplay.model.Dog
import com.example.dogdisplay.viewmodel.DogViewModel

private const val ARG_POSITION = "position"
private const val TAG = "DogDetailFragment"
class DogDetailFragment : Fragment() {

    // 在 fragment 中通过 by activityViewModels()，可以实现在fragment之间
    // 共享同一个 dogList 数据，DogViewModel的init 只执行一次
    // 这里通过DogViewModel 初始化 dog ，修改 dogList 数据
    private  val dogViewModel: DogViewModel by activityViewModels()
    private lateinit var selectedDog: Dog
    private var selectedPosition = 0

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
       //fragment中 订阅的时机，一般会选择放到 onViewCreated 中进行
        Log.d(TAG,"inner onViewCreated")
        startObserver()
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG,"inner onCreate")
        // 写在onCreate中会报错
        // java.lang.IllegalStateException: Can't access the Fragment View's LifecycleOwner when getView() is null i.e., before onCreateView() or after onDestroyView()
        // startObserver()
        arguments?.let {
            selectedPosition = it.getInt(ARG_POSITION)
        }

        // 初始化数据
        dogViewModel.initSelectedDog(selectedPosition)
    }

    private fun startObserver() {
        Log.d(TAG,"inner startObserver")
        dogViewModel.dogLiveData.observe(viewLifecycleOwner){
            Log.d(TAG,"dog = $it")
            // 初始化dog
            selectedDog = it
            Log.d(TAG,"selectedDog = ${selectedDog.name} , adopted = " +
                    " ${selectedDog.adopted}")
            updateUI()
        }
    }

    private fun updateUI(){
        Log.d(TAG,"inner updateUI")
        // toolbar 使用
        _binding?.toolbar?.title = selectedDog.name
        _binding?.toolbar?.setNavigationOnClickListener {
            Log.d(TAG,"inner NavigationOnClickListener")
            // fragmentManager 已废弃
            parentFragmentManager.popBackStackImmediate()
/*            fragmentManager?.popBackStack("DogDetailFragment",
                FragmentManager.POP_BACK_STACK_INCLUSIVE)*/
        }

        val imageIdentity = resources.getIdentifier(
            selectedDog.avatarFilename, "drawable",
            GlobalApp.context.packageName
        )
        _binding?.avatarImageView?.setImageResource(imageIdentity)

        Log.d(TAG," selectedDog.adopted = ${selectedDog.adopted}")
        if (selectedDog.adopted){
            _binding?.adoptButton?.isEnabled = false
            Log.d(TAG," isEnabled = false")
        }else{
            _binding?.adoptButton?.isEnabled = true
            Log.d(TAG," isEnabled = true")
        }
        _binding?.adoptButton?.text = if (selectedDog.adopted) "Adopted" else "Adopt"
        _binding?.adoptButton?.setOnClickListener {
            showDialog()
        }

        _binding?.introductionTextView?.text = selectedDog.introduction
    }


    private var _binding: FragmentDogDetailBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        Log.d(TAG,"inner onCreateView")
        _binding = FragmentDogDetailBinding.inflate(inflater, container, false)
        return binding.root
    }


    private fun showDialog() {
        val builder : AlertDialog.Builder = createDialog()
        // 创建dialog
        val dialog : AlertDialog = builder.create()
        // 设置点击弹框以外的区域会不会消失，true消失，false不消失
        dialog.setCanceledOnTouchOutside(false)
        dialog.show()
    }

    // 创建build对象
    private fun createDialog() : AlertDialog.Builder{
        val builder : AlertDialog.Builder =
            AlertDialog.Builder(requireActivity())
        builder.setMessage("Do you want to adopt this lovely dog?")
        builder.setNegativeButton("No") { _, _ ->
            Toast.makeText(context, "No", Toast.LENGTH_SHORT).show()
        }
        builder.setPositiveButton("Yes") { _, _ ->
            Toast.makeText(context, "Yes", Toast.LENGTH_SHORT).show()
            // 修改adopted状态
            selectedDog.adopted = !selectedDog.adopted
            Log.d(TAG," selectedPosition = $selectedPosition")
            dogViewModel.updateDog(selectedDog,selectedPosition)
            if (selectedDog.adopted){
                _binding?.adoptButton?.text = "Adopted"
                _binding?.adoptButton?.isEnabled = false
            }
        }
        return builder
    }
}