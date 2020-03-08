package com.dicoding.picodiploma.mysubmission.view.fragment


import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.picodiploma.mysubmission.R
import com.dicoding.picodiploma.mysubmission.model.Movie
import com.dicoding.picodiploma.mysubmission.util.EspressoIdlingResource
import com.dicoding.picodiploma.mysubmission.view.DetailMovieActivity
import com.dicoding.picodiploma.mysubmission.view.adapter.ListItemAdapter
import com.dicoding.picodiploma.mysubmission.viewmodel.ListMovieViewModel
import com.dicoding.picodiploma.mysubmission.viewmodel.ListTvShowViewModel
import kotlinx.android.synthetic.main.fragment_list_movies.*

class ItemMoviesFragment : Fragment() {


//    private lateinit var movieViewModel: ListMovieViewModel
//    private lateinit var tvShowViewModel: ListTvShowViewModel

    companion object {
        private val ARG_SECTION_NUMBER = "section_number"

        fun newInstance(index: Int): ItemMoviesFragment {
            val fragment =
                ItemMoviesFragment()
            val bundle = Bundle()
            bundle.putInt(ARG_SECTION_NUMBER, index)
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_list_movies, container, false) as View
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        rv_list.layoutManager = LinearLayoutManager(view.context)
//        rv_list.setHasFixedSize(true)


        var index = 0
        if (arguments != null) {
            index = arguments?.getInt(ARG_SECTION_NUMBER, 0) as Int
        }
//
//        if (index == 0) {
//            getMovieList(view.context, index)
//        } else {
//            getTvShowList(view.context, index)
//        }

        section_label.text = "${getString(R.string.content_tab_text)} $index"

    }
//
//    private fun getMovieList(context: Context, index: Int) {
//        movieViewModel = ViewModelProvider(
//            requireActivity(),
//            ViewModelProvider.NewInstanceFactory()
//        ).get(ListMovieViewModel::class.java)
//
//        val listMoviesAdapter =
//            ListItemAdapter()
//        listMoviesAdapter.notifyDataSetChanged()
//
////        EspressoIdlingResource.increment()
//        movieViewModel.setMovies(context)
//        showLoading(true)
//
//        movieViewModel.getMovies().observe(viewLifecycleOwner, Observer { moviesItems ->
//            if (moviesItems != null) {
//                listMoviesAdapter.setData(moviesItems)
//                showLoading(false)
////                EspressoIdlingResource.decrement()
//            }
//        })
//        rv_list.adapter = listMoviesAdapter
//        showSelectedMovie(listMoviesAdapter, index)
//    }
//
//    private fun getTvShowList(context: Context, index: Int) {
//        tvShowViewModel = ViewModelProvider(
//            requireActivity(),
//            ViewModelProvider.NewInstanceFactory()
//        ).get(ListTvShowViewModel::class.java)
//        val listTvShowAdapter =
//            ListItemAdapter()
//        listTvShowAdapter.notifyDataSetChanged()
//
////        EspressoIdlingResource.increment()
//        tvShowViewModel.setTvShows(context)
//        showLoading(true)
//
//        tvShowViewModel.getTvShows().observe(viewLifecycleOwner, Observer { tvShowItems ->
//            if (tvShowItems != null) {
//                listTvShowAdapter.setData(tvShowItems)
//                showLoading(false)
////                EspressoIdlingResource.decrement()
//
//            }
//        })
//
//        rv_list.adapter = listTvShowAdapter
//        showSelectedMovie(listTvShowAdapter, index)
//    }
//
//    private fun showSelectedMovie(listMovieAdapter: ListItemAdapter, index: Int) {
//        listMovieAdapter.setOnItemClickCallback(object : ListItemAdapter.OnItemClickCallback {
//            override fun onItemClicked(data: Movie) {
//                goToDetailMovie(data, index)
//            }
//        })
//    }
//
//    private fun goToDetailMovie(movie: Movie, index: Int) {
//        val intent = Intent(context, DetailMovieActivity::class.java)
//        intent.putExtra(DetailMovieActivity.EXTRA_TYPE, index)
//        intent.putExtra(DetailMovieActivity.EXTRA_SELECTED_VALUE, movie)
//        startActivity(intent)
//    }
//
//    private fun showLoading(state: Boolean) {
//        if (state) {
//            progressBar.visibility = View.VISIBLE
//        } else {
//            progressBar.visibility = View.GONE
//        }
//    }


}
