package com.chocomiruku.homework9

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.chocomiruku.homework9.databinding.FragmentSignInBinding
import com.jakewharton.rxbinding4.widget.textChanges
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.CompositeDisposable


class SignInFragment : Fragment() {
    private var _binding: FragmentSignInBinding? = null
    private val binding get() = _binding!!
    private val disposeBag = CompositeDisposable()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSignInBinding.inflate(inflater, container, false)

        savedInstanceState?.let { bundle ->
            bundle.getString(KEY_USERNAME)?.let {
                binding.usernameEditText.setText(it)
            }
            bundle.getString(KEY_PASSWORD)?.let {
                binding.usernameEditText.setText(it)
            }
        }

        binding.signInBtn.setOnClickListener {
            findNavController().navigate(SignInFragmentDirections.actionSignInFragmentToSearchFragment())
        }

        val usernameObservable = binding.usernameEditText.textChanges()
        val passwordObservable = binding.passwordEditText.textChanges()

        val isSignInEnabled = Observable.combineLatest(
            usernameObservable,
            passwordObservable
        ) { u, p -> u.length > 5 && p.length > 5 }

        disposeBag.add(isSignInEnabled
            .subscribe {
                binding.signInBtn.isEnabled = it == true
            })

        return binding.root
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        binding.usernameEditText.text?.let {
            outState.putString(KEY_USERNAME, it.toString())
        }
        binding.passwordEditText.text?.let {
            outState.putString(KEY_PASSWORD, it.toString())
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        disposeBag.dispose()
    }

    companion object {
        const val KEY_USERNAME = "key_username"
        const val KEY_PASSWORD = "key_password"
    }
}