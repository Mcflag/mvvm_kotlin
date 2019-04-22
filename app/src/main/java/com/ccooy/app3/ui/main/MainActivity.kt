package com.ccooy.app3.ui.main

import android.animation.ObjectAnimator
import android.content.Intent
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.ccooy.app3.R
import com.ccooy.app3.base.view.activity.BaseActivity
import com.ccooy.app3.binding.FabAnimateViewModel
import com.ccooy.app3.data.remote.GithubService
import com.ccooy.app3.databinding.ActivityMainBinding
import com.ccooy.app3.ext.livedata.toReactiveStream
import com.uber.autodispose.autoDisposable
import io.reactivex.Completable
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject
import com.ccooy.app3.binding.adapter.BasePagingDataBindingAdapter
import com.ccooy.app3.data.model.response.Repo
import com.ccooy.app3.databinding.ItemReposRepoBinding
import com.ccooy.app3.ext.functional.Consumer2


class MainActivity : BaseActivity<ActivityMainBinding>() {

    @Inject
    lateinit var repo: GithubService

    @Inject
    lateinit var fabViewModel: FabAnimateViewModel

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    lateinit var viewModel: MainViewModel

    override val layoutId: Int = R.layout.activity_main

    val adapter = BasePagingDataBindingAdapter<Repo, ItemReposRepoBinding>(
        layoutId = R.layout.item_repos_repo,
        bindBinding = { ItemReposRepoBinding.bind(it) },
        callback = { repo, binding, _ ->
            binding.apply {
                data = repo
                avatarEvent = object : Consumer2<String> {
                    override fun accept(t1: String, t2: String) {

                    }
                }
                repoEvent = object : Consumer2<String> {
                    override fun accept(t1: String, t2: String) {

                    }
                }
            }

        }
    )

    override fun initView() {
        toolbar.inflateMenu(R.menu.menu_repos_filter_type)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(MainViewModel::class.java)

        Completable
            .mergeArray(
                fabViewModel.visibleState.toReactiveStream()
                    .doOnNext { switchFabState(it) }
                    .ignoreElements(),
                viewModel.pagedList.toReactiveStream()
                    .doOnNext { adapter.submitList(it) }
                    .ignoreElements()
            )
            .autoDisposable(viewModel)
            .subscribe()
    }

    fun onMenuSelected(menuItem: MenuItem) {
        viewModel.sort.value = when (menuItem.itemId) {
            R.id.menu_repos_letter -> MainViewModel.sortByLetter
            R.id.menu_repos_created -> MainViewModel.sortByCreated
            R.id.menu_repos_update -> MainViewModel.sortByUpdate
            else -> throw IllegalArgumentException("error menuItem id.")
        }
    }

    private fun switchFabState(show: Boolean) =
        when (show) {
            false -> ObjectAnimator.ofFloat(fabTop, "translationX", 250f, 0f)
            true -> ObjectAnimator.ofFloat(fabTop, "translationX", 0f, 250f)
        }.apply {
            duration = 100
            start()
        }

    companion object {
        fun launch(activity: AppCompatActivity) =
            activity.apply {
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }
    }
}
