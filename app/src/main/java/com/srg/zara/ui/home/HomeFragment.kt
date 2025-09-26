package com.srg.zara.ui.home

import android.app.Activity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import android.widget.TextView.OnEditorActionListener
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.srg.zara.util.on
import com.srg.zara.util.viewBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.snackbar.Snackbar
import com.srg.domain.base.DataResult
import com.srg.domain.zara.entity.Character
import com.srg.zara.R
import com.srg.zara.databinding.FragmentHomeBinding
import com.srg.zara.ui.detail.CharDetailViewModel
import com.srg.zara.ui.home.adapter.CharacterAdapter
import com.srg.zara.ui.home.adapter.EndlessScrollListener
import com.srg.zara.ui.home.adapter.FilteredCharacterAdapter
import com.srg.zara.ui.settings.SettingsViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class HomeFragment : Fragment(R.layout.fragment_home) {

    private val viewBinding by viewBinding { FragmentHomeBinding.bind(it) }
    private val viewModel: HomeViewModel by viewModels()
    private val charDetailViewModel: CharDetailViewModel by activityViewModels()
    private val settingsViewModel: SettingsViewModel by activityViewModels()

    private lateinit var characterAdapter: CharacterAdapter
    private lateinit var filteredCharacterAdapter: FilteredCharacterAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(viewModel) {
            if (characters.isNullOrEmpty()) {
                setStartingPage()
                showLoading()
                callNetworkCharList(currentPage)
            } else {
                configureUI()
                bindUI(requireNotNull(characters))
                if (!settingsViewModel.isNeedReset) {
                    hideLoading()
                } else {
                    showLoading()
                    setStartingPage()
                    callNetworkCharList(currentPage)
                }
            }
        }
    }

    private fun callNetworkCharList(page: Int?) {
        with(viewModel) {
            viewLifecycleOwner.lifecycleScope.launch {
                viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                    on(charList) { result ->
                        when (result) {
                            is DataResult.Success -> {
                                result.data?.characters?.let {
                                    if (characters.isNullOrEmpty() || settingsViewModel.isNeedReset) {
                                        settingsViewModel.isNeedReset = false
                                        characters = it as ArrayList<Character>?
                                        configureUI()
                                        bindUI(requireNotNull(it))
                                    } else {
                                        characters?.addAll(it.map { it })
                                        characterAdapter.addData(it)
                                        filteredCharacterAdapter.addData(it)
                                    }
                                    maxPage = result.data?.info?.pages?.toInt()
                                    hideLoading()
                                }
                            }

                            is DataResult.Error -> {
                                handleException(result.exception)
                                hideLoading()
                            }

                            is DataResult.Status -> {
                                when (result.status) {
                                    DataResult.DataStatus.LOADING -> {
                                        showLoading()
                                    }

                                    else -> {
                                        // No-op
                                    }
                                }
                            }

                            else -> {
                                // No-op
                            }
                        }
                    }
                }
            }
            if (page == 1) {
                getCharList(page, 2000)
            } else {
                getCharList(page, 0)
            }
        }
    }

    private fun callNetworkSearchChar(page: Int?, query: String) {
        with(viewModel) {
            viewLifecycleOwner.lifecycleScope.launch {
                viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                    on(charList) { result ->
                        when (result) {
                            is DataResult.Success -> {
                                result.data?.characters?.let {
                                    if (characters.isNullOrEmpty()) {
                                        characters = it as ArrayList<Character>?
                                        // configureUI()
                                        bindUI(requireNotNull(it))
                                    } else {
                                        characters?.addAll(it.map { it })
                                        characterAdapter.addData(it)
                                        filteredCharacterAdapter.addData(it)
                                    }
                                    maxPage = result.data?.info?.pages?.toInt()
                                    hideLoading()
                                }
                            }

                            is DataResult.Error -> {
                                handleException(result.exception)
                                hideLoading()
                            }

                            is DataResult.Status -> {
                                when (result.status) {
                                    DataResult.DataStatus.LOADING -> {
                                        showLoading()
                                    }

                                    else -> {
                                        // No-op
                                    }
                                }
                            }

                            else -> {
                                // No-op
                            }
                        }
                    }
                }
            }
            searchCharacters(page, query)
        }
    }

    fun searchCharAction() {
        with(viewBinding) {
            with(viewModel) {
                clearCharacters()
                setStartingPage()
                showLoading()
                if (etCharacters.text.toString().isEmpty()) {
                    callNetworkCharList(currentPage)
                } else {
                    callNetworkSearchChar(currentPage, etCharacters.text.toString())
                }
            }
        }
    }

    private fun configureUI() {
        with(viewBinding) {
            with(viewModel) {
                tilCharacters.setStartIconOnClickListener {
                    searchCharAction()
                    etCharacters.clearFocus()
                }

                etCharacters.setOnEditorActionListener(object : OnEditorActionListener {
                    override fun onEditorAction(
                        v: TextView?,
                        actionId: Int,
                        event: KeyEvent?
                    ): Boolean {
                        if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                            val imm =
                                activity!!.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
                            imm.hideSoftInputFromWindow(view!!.windowToken, 0)
                            searchCharAction()
                            etCharacters.clearFocus()
                            return true
                        }
                        return false
                    }
                })

                characterAdapter =
                    CharacterAdapter(object : CharacterAdapter.CharacterListListener {
                        override fun onCharacterClicked(character: Character) {
                            charDetailViewModel.character = character
                            findNavController().navigate(R.id.action_navigation_home_to_charDetailFragment)
                        }
                    })

                rvCharacters.addOnScrollListener(object : EndlessScrollListener() {
                    override fun onLoadMore() {
                        if (requireNotNull(currentPage) < requireNotNull(maxPage)) {
                            increasePage()
                            if (settingsViewModel.isCheckedFilterList) {
                                callNetworkCharList(currentPage)
                            } else {
                                callNetworkSearchChar(currentPage, etCharacters.text.toString())
                            }
                        }

                    }
                })

                filteredCharacterAdapter =
                    FilteredCharacterAdapter(object :
                        FilteredCharacterAdapter.CharacterListListener {
                        override fun onCharacterClicked(character: Character) {
                            charDetailViewModel.character = character
                            findNavController().navigate(R.id.action_navigation_home_to_charDetailFragment)
                        }
                    })

                svFilteredCharacters.editText.addTextChangedListener(object : TextWatcher {
                    override fun afterTextChanged(s: Editable?) {
                        filteredCharacterAdapter.filter(s.toString())
                    }

                    override fun beforeTextChanged(
                        s: CharSequence?,
                        start: Int,
                        count: Int,
                        after: Int
                    ) {
                    }

                    override fun onTextChanged(
                        s: CharSequence?,
                        start: Int,
                        before: Int,
                        count: Int
                    ) {
                    }
                })

                tilCharacters.isVisible = !settingsViewModel.isCheckedFilterList
                sbFilteredCharacters.isVisible = settingsViewModel.isCheckedFilterList
            }
        }
    }

    private fun bindUI(data: List<Character>) {
        with(viewBinding) {
            characterAdapter.setData(data)
            rvCharacters.layoutManager = LinearLayoutManager(context)
            rvCharacters.adapter = characterAdapter

            filteredCharacterAdapter.setData(data)
            rvFilteredCharacters.layoutManager = LinearLayoutManager(context)
            rvFilteredCharacters.adapter = filteredCharacterAdapter

            tvNoChars.isVisible = data.isEmpty()
        }
    }

    private fun hideLoading() {
        with(viewBinding) {
            shimmerCharacters.stopShimmer()
            shimmerCharacters.isVisible = false
            rvCharacters.isVisible = true
        }
    }

    private fun showLoading() {
        with(viewBinding) {
            tvNoChars.isVisible = false
            shimmerCharacters.startShimmer()
            shimmerCharacters.isVisible = true
            rvCharacters.isVisible = false
        }
    }

    private fun showSnackBarError(text: String) {
        val snackBar = getSnackBar(text, Snackbar.LENGTH_INDEFINITE)
        snackBar?.setAction(R.string.retry) {
            showLoading()
            if (settingsViewModel.isCheckedFilterList) {
                callNetworkCharList(viewModel.currentPage)
            } else {
                callNetworkSearchChar(
                    viewModel.currentPage,
                    viewBinding.etCharacters.text.toString()
                )
            }
        }?.show()
    }

    fun getSnackBar(text: String, length: Int): Snackbar? {
        view?.let {
            return createSnackBar(it, text, length)
        } ?: return null
    }

    private fun createSnackBar(
        it: View,
        text: String,
        length: Int
    ): Snackbar {
        val snackBar = Snackbar.make(it, text, length)
        val bottomNavigationView =
            it.rootView.findViewById<BottomNavigationView>(R.id.nav_view)
        snackBar.setAnchorView(bottomNavigationView)
        return snackBar
    }

    fun handleException(exception: Exception) {

        val message: String? = if (exception is java.net.ConnectException ||
            exception is java.net.UnknownHostException
        ) {
            getString(R.string.error_no_network)
        } else {
            exception.message
        }

        showSnackBarError(message.toString())
    }
}