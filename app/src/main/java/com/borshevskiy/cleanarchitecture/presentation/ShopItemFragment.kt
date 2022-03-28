package com.borshevskiy.cleanarchitecture.presentation

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.borshevskiy.cleanarchitecture.R
import com.borshevskiy.cleanarchitecture.databinding.FragmentShopItemBinding
import com.borshevskiy.cleanarchitecture.util.Constants
import com.borshevskiy.cleanarchitecture.util.Constants.Companion.MODE_UNKNOWN
import com.borshevskiy.cleanarchitecture.util.Constants.Companion.UNDEFINED_ID

class ShopItemFragment : Fragment() {

    private var _binding: FragmentShopItemBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: ShopItemViewModel

    private var screenMode = MODE_UNKNOWN
    private var shopItemId = UNDEFINED_ID

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentShopItemBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        parseIntent()
        viewModel = ViewModelProvider(this)[ShopItemViewModel::class.java]
        addTextChangeListeners()
        launchRightMode()
        observeViewModel()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun observeViewModel() {
        viewModel.errorInputCount.observe(viewLifecycleOwner) {
            val message = if (it) {
                getString(R.string.error_input_count)
            } else {
                null
            }
            binding.tilCount.error = message
        }
        viewModel.errorInputName.observe(viewLifecycleOwner) {
            val message = if (it) {
                getString(R.string.error_input_name)
            } else {
                null
            }
            binding.tilName.error = message
        }
        viewModel.canCloseScreen.observe(viewLifecycleOwner) {
            activity?.onBackPressed()
        }
    }

    private fun addTextChangeListeners() {
        binding.etName.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, count: Int) {
                viewModel.resetErrorInputName()
            }

            override fun afterTextChanged(p0: Editable?) {}
        })
        binding.etCount.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, count: Int) {
                viewModel.resetErrorInputCount()
            }

            override fun afterTextChanged(p0: Editable?) {}
        })
    }

    private fun parseIntent() {
        if (!requireActivity().intent.hasExtra(Constants.EXTRA_SCREEN_MODE)) {
            throw RuntimeException("Param screen mode is absent")
        }
        val mode = requireActivity().intent.getStringExtra(Constants.EXTRA_SCREEN_MODE)
        if (mode != Constants.MODE_UPDATE && mode != Constants.MODE_ADD) {
            throw RuntimeException("Unknown screen mode $mode")
        }
        screenMode = mode
        if (screenMode == Constants.MODE_UPDATE) {
            if (!requireActivity().intent.hasExtra(Constants.EXTRA_SHOP_ITEM_ID)) {
                throw RuntimeException("Param shop item id is absent")
            }
            shopItemId = requireActivity().intent.getIntExtra(Constants.EXTRA_SHOP_ITEM_ID, Constants.UNDEFINED_ID)
        }
    }

    private fun launchRightMode() {
        when (screenMode) {
            Constants.MODE_UPDATE -> launchEditMode()
            Constants.MODE_ADD -> launchAddMode()
        }
    }

    private fun launchEditMode() {
        viewModel.getShopItem(shopItemId)
        viewModel.shopItem.observe(viewLifecycleOwner) {
            with(binding) {
                etName.setText(it.name)
                etCount.setText(it.count.toString())
                saveButton.setOnClickListener {
                    viewModel.updateShopItem(etName.text?.toString(),etCount.text?.toString())
                }
            }
        }
    }

    private fun launchAddMode() {
        with(binding) {
            viewModel.addShopItem(etName.text?.toString(),etCount.text?.toString())
        }
    }

    companion object {

        fun newIntentAddItem(context: Context): Intent {
            val intent = Intent(context, ShopItemActivity::class.java)
            return intent.putExtra(Constants.EXTRA_SCREEN_MODE, Constants.MODE_ADD)
        }

        fun newIntentUpdateItem(context: Context, shopItemId: Int): Intent {
            val intent = Intent(context, ShopItemActivity::class.java)
            intent.putExtra(Constants.EXTRA_SCREEN_MODE, Constants.MODE_UPDATE)
            intent.putExtra(Constants.EXTRA_SHOP_ITEM_ID, shopItemId)
            return intent
        }
    }
}