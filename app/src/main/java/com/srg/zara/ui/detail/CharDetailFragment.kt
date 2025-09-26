package com.srg.zara.ui.detail

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.style.TextAlign
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import coil3.compose.AsyncImage
import com.srg.domain.zara.entity.Character
import com.srg.zara.R
import com.srg.zara.theme.AppTheme


class CharDetailFragment : Fragment() {

    private val viewModel: CharDetailViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return ComposeView(requireContext()).apply {
            setContent {
                AppTheme {
                    Surface {
                        CharacterLayout(viewModel.character)
                    }
                }
            }
        }
    }

    @Preview
    @Composable
    fun CharacterLayout(character: Character?) {
        val interactionSource = remember { MutableInteractionSource() }
        Column(
            modifier = Modifier
                .fillMaxSize(),
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_back),
                contentDescription = stringResource(id = R.string.back_desc),
                colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.inverseSurface),
                modifier = Modifier
                    .padding(dimensionResource(id = R.dimen.exp_padding))
                    .size(dimensionResource(id = R.dimen.icon_large))
                    .clickable(
                        interactionSource = interactionSource,
                        indication = null,
                        role = Role.Button,
                        onClick = { findNavController().popBackStack() }
                    ),
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState()),
            ) {
                character?.let {
                    AsyncImage(
                        model = character.image,
                        placeholder = painterResource(R.drawable.avatar_placeholder),
                        contentDescription = stringResource(id = R.string.image_character),
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .clip(CircleShape)
                            .size(dimensionResource(id = R.dimen.avatar_large))
                            .align(Alignment.CenterHorizontally),
                        error = painterResource(R.drawable.avatar_placeholder)
                    )

                    Text(
                        text = it.name,
                        style = MaterialTheme.typography.headlineLarge,
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                            .padding(dimensionResource(id = R.dimen.exp_padding))
                    )

                    DataField(
                        R.drawable.ic_species,
                        it.species,
                        stringResource(id = R.string.species_desc)
                    )
                    DataField(
                        R.drawable.ic_gender,
                        it.gender,
                        stringResource(id = R.string.gender_desc)
                    )
                    DataField(
                        R.drawable.ic_status,
                        it.status,
                        stringResource(id = R.string.status_desc)
                    )
                    DataField(
                        R.drawable.ic_origin,
                        it.origin.name,
                        stringResource(id = R.string.origin_desc)
                    )
                    DataField(
                        R.drawable.ic_location,
                        it.location.name,
                        stringResource(id = R.string.location_desc)
                    )
                }
            }
        }
    }

    @Composable
    fun DataField(imageId: Int, text: String, contentDesc: String) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth(1f)
                .padding(start = dimensionResource(id = R.dimen.exp_padding_medium))
        ) {
            Image(
                painter = painterResource(id = imageId),
                contentDescription = contentDesc,
                colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.inverseSurface),
            )
            Text(
                text = text,
                textAlign = TextAlign.Start,
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(dimensionResource(id = R.dimen.exp_padding))
            )
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        val callback: OnBackPressedCallback =
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    findNavController().popBackStack()
                }
            }
        requireActivity().onBackPressedDispatcher.addCallback(
            this,
            callback
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewModel.resetData()
    }
}