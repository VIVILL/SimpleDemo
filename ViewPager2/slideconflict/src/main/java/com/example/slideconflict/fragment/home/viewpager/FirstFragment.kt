package com.example.slideconflict.fragment.home.viewpager

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.slideconflict.R
import com.example.slideconflict.adapter.StringAdapter
import com.example.slideconflict.databinding.FragmentFirstBinding
import com.example.slideconflict.viewmodel.TouchViewModel

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [FirstFragment.newInstance] factory method to
 * create an instance of this fragment.
 */

private const val TAG = "FirstFragment"
class FirstFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private var _binding: FragmentFirstBinding? = null
    private val binding get() = _binding!!

    private val viewModel: TouchViewModel by activityViewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG,"inner onCreate")
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d(TAG,"inner onCreateView")
        // Inflate the layout for this fragment
        _binding = FragmentFirstBinding.inflate(inflater, container, false)

        binding.recyclerview.addItemDecoration(
            DividerItemDecoration(
                binding.recyclerview.context,
                (binding.recyclerview.layoutManager as LinearLayoutManager).orientation
            )
        )


        val stringAdapter = StringAdapter((0..500).map { "string : $it" })
        // 为RecyclerView配置adapter
        binding.recyclerview.adapter = stringAdapter

        binding.recyclerview.setTouchEventListener{
            Log.d(TAG,"inner setTouchEventListener")
            viewModel.touchRecyclerview(it)
        }

        return binding.root
    }

    override fun onDestroyView() {
        Log.d(TAG,"inner onDestroyView")
        super.onDestroyView()
        binding.recyclerview.adapter = null
        _binding = null
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment FirstFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            FirstFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}