package com.srg.zara.ui.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.srg.zara.R
import com.srg.zara.theme.AppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SettingsFragment : Fragment() {

    private val viewModel: SettingsViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        return ComposeView(requireContext()).apply {
            setContent {
                SettingsFragmentComposable()
            }
        }
    }

    @Composable
    fun SettingsFragmentComposable() {
        AppTheme {
            Surface {
                SettingsLayout()
            }
        }
    }


    @Composable
    fun SettingsLayout() {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    horizontal = dimensionResource(id = R.dimen.exp_padding),
                    vertical = dimensionResource(id = R.dimen.exp_padding_medium)
                )
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.exp_padding_large))
        ) {
            SwitchWithLabel(
                getString(R.string.option_filter),
                getString(R.string.option_filter_desc),
                viewModel.isCheckedFilterList
            ) {
                viewModel.isCheckedFilterList = it
                viewModel.isNeedReset = viewModel.isCheckedFilterList
            }
            LanguageSelector(getString(R.string.language), viewModel.getLanguage(requireContext()))
            CacheCleaner()
        }
    }

    @Composable
    private fun SwitchWithLabel(
        label: String, labelDesc: String, state: Boolean, onStateChange: (Boolean) -> Unit
    ) {
        var checked by remember { mutableStateOf(state) }
        val interactionSource = remember { MutableInteractionSource() }
        Row(
            modifier = Modifier
                .clickable(
                    interactionSource = interactionSource,
                    indication = null,
                    role = Role.Switch,
                    onClick = {
                        onStateChange(!checked)
                        checked = !checked
                    })
                .padding(horizontal = dimensionResource(id = R.dimen.exp_padding_medium)),
            verticalAlignment = Alignment.CenterVertically

        ) {
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.exp_padding))
            ) {
                Text(
                    text = label, style = MaterialTheme.typography.titleLarge
                )

                Text(
                    text = labelDesc, style = MaterialTheme.typography.bodyMedium
                )
            }
            Spacer(modifier = Modifier.padding(start = dimensionResource(id = R.dimen.exp_padding)))
            Switch(
                modifier = Modifier.requiredWidth(IntrinsicSize.Min),
                checked = checked,
                onCheckedChange = {
                    checked = it
                    onStateChange(it)
                })
        }
    }

    @Composable
    fun LanguageSelector(label: String, language: String) {
        var selectedIndex by remember { mutableIntStateOf(0) }
        val options = listOf(
            Pair(
                getString(R.string.english) + viewModel.getCountryFlagIcon(ENGLISH), ENGLISH
            ), Pair(
                getString(R.string.spanish) + viewModel.getCountryFlagIcon(SPANISH), SPANISH
            )
        )
        Column(
            modifier = Modifier.padding(horizontal = dimensionResource(id = R.dimen.exp_padding_medium)),
            verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.exp_padding))
        ) {
            Text(
                text = label, style = MaterialTheme.typography.titleLarge
            )
            SingleChoiceSegmentedButtonRow {
                options.forEachIndexed { index, item ->
                    SegmentedButton(
                        shape = SegmentedButtonDefaults.itemShape(
                        index = index, count = options.size
                    ), onClick = {
                        if (item.second != language) {
                            selectedIndex = index
                            if (item.second == ENGLISH) {
                                viewModel.changeLocale(requireContext(), ENGLISH)
                            } else {
                                viewModel.changeLocale(requireContext(), SPANISH)
                            }
                        }
                    }, selected = item.second == language, label = { Text(item.first) })
                }
            }
        }
    }

    @Composable
    fun CacheCleaner() {
        Column(
            modifier = Modifier.padding(horizontal = dimensionResource(id = R.dimen.exp_padding_medium)),
            verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.exp_padding))
        ) {
            Text(
                text = stringResource(id = R.string.clear_cache),
                style = MaterialTheme.typography.titleLarge
            )
            Button(
                onClick = { viewModel.clearCache() },
                contentPadding = ButtonDefaults.ButtonWithIconContentPadding,
            ) {
                Icon(
                    Icons.Filled.Delete,
                    contentDescription = stringResource(id = R.string.delete_desc),
                    modifier = Modifier.size(ButtonDefaults.IconSize),
                )
                Spacer(Modifier.size(ButtonDefaults.IconSpacing))
                Text(stringResource(id = R.string.cache_button_text))
            }
        }
    }

    companion object {
        private const val ENGLISH = "gb"
        private const val SPANISH = "es"
    }
}