package com.live.fixedassets.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.live.fixedassets.R
import com.live.fixedassets.core.BaseFragment
import com.live.fixedassets.databinding.FragmentHomeBinding
import com.live.fixedassets.databinding.FragmentNotificationBinding


class NotificationFragment : BaseFragment<FragmentNotificationBinding>() {
    override fun inflateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentNotificationBinding {
        return FragmentNotificationBinding.inflate(layoutInflater)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

}