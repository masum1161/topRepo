package net.top.repo.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint
import net.top.repo.api.response.home_page.GitResponse
import net.top.repo.databinding.FragmentRepoDetailsBinding
import net.top.repo.utilities.SessionHelper
import net.top.repo.utilities.convertStringToDateObject
import net.top.repo.utilities.epochToDateString

@AndroidEntryPoint
class RepoDetailsFragment : Fragment() {
    private var _binding: FragmentRepoDetailsBinding? = null
    private val binding get() = _binding!!
    private lateinit var sessionHelper: SessionHelper
    private lateinit var getRepo: GitResponse.Item

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRepoDetailsBinding.inflate(layoutInflater)
        initialSetup()
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initialSetup() {
        binding.ivBack.setOnClickListener {
            requireActivity().onBackPressed()
        }
        sessionHelper = SessionHelper(requireContext())
        binding.username.text = getRepo.full_name
        binding.repoName.text = getRepo.name
        binding.itemDesc.text = getRepo.description
        Picasso.get().load(getRepo.owner?.avatar_url).into(binding.avatarIv)
        binding.lastUpdateDateLevelValue.text = getRepo.updated_at.convertStringToDateObject().epochToDateString("MM-dd-yy HH:ss")
    }


    companion object {
        @JvmStatic
        fun newInstance(
            getRepo:  GitResponse.Item,
        ) = RepoDetailsFragment().apply {
            this.getRepo = getRepo
        }
    }

}