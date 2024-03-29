package elieomatuku.cineast_android.base

import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import elieomatuku.cineast_android.connection.ConnectionService
import elieomatuku.cineast_android.extensions.*
import org.kodein.di.*
import org.kodein.di.android.x.closestDI

/**
 * Created by elieomatuku on 2021-05-05
 */

abstract class BaseFragment : Fragment, DIAware {

    constructor()
    constructor(@LayoutRes resId: Int) : super(resId)

    protected val rxSubs: io.reactivex.disposables.CompositeDisposable by lazy {
        io.reactivex.disposables.CompositeDisposable()
    }

    protected val connectionService: ConnectionService by instance()

    override val di: DI by closestDI()
    val viewModelFactory: ViewModelProvider.Factory by instance()

    protected inline fun <reified VM : ViewModel> getViewModel(): VM =
        getViewModel(viewModelFactory)

    protected inline fun <reified VM : ViewModel> getSharedViewModel(): VM =
        getSharedViewModel(viewModelFactory)

    protected inline fun <reified VM : ViewModel> viewModel(): Lazy<VM> = lifecycleAwareLazy(this) {
        getViewModel<VM>()
    }

    protected inline fun <reified VM : ViewModel> sharedViewModel(): Lazy<VM> =
        lifecycleAwareLazy(this) {
            getSharedViewModel<VM>()
        }

    override fun onResume() {
        super.onResume()

//        rxSubs.add(
//            connectionService.connectionChangedObserver
//                .subscribeOn(AndroidSchedulers.mainThread())
//                .subscribe(
//                    { hasConnection ->
//
//                        if (hasConnection) {
//                            showLoading(requireView())
//                        }
//                        Timber.d("connectionChangedObserver: hasConnection = $hasConnection, hasEmptyState = ")
//                    },
//                    { t: Throwable ->
//
//                        Timber.e(t, "Connection Change Observer failed")
//                    }
//                )
//        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        rxSubs.clear()
    }

    override fun onDestroy() {
        super.onDestroy()
        rxSubs.clear()
    }
}
