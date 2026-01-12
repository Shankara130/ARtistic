package com.example.artistic.ui.main.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.artistic.databinding.FragmentHomeBinding
import com.example.artistic.ui.adapter.ArtworkAdapter
import com.example.artistic.viewmodel.ArtworkViewModel

class HomeFragment : Fragment() {
    
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    
    private val artworkViewModel: ArtworkViewModel by viewModels()
    private lateinit var artworkAdapter: ArtworkAdapter
    
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        setupRecyclerView()
        observeData()
    }
    
    private fun setupRecyclerView() {
        artworkAdapter = ArtworkAdapter { artwork ->
            // Handle artwork click
        }
        
        binding.rvArtworks.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = artworkAdapter
        }
    }
    
    private fun observeData() {
        artworkViewModel.publicArtworks.observe(viewLifecycleOwner) { artworks ->
            if (artworks.isEmpty()) {
                binding.emptyState.visibility = View.VISIBLE
                binding.rvArtworks.visibility = View.GONE
            } else {
                binding.emptyState.visibility = View.GONE
                binding.rvArtworks.visibility = View.VISIBLE
                artworkAdapter.submitList(artworks)
            }
        }
    }
    
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}