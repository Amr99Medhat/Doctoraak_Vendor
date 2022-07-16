package com.doctoraak.doctoraakdoctor.utils

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import java.util.concurrent.atomic.AtomicBoolean

class SingleLiveData<T> : MutableLiveData<T>
{
    constructor() : super()
    constructor(value: T) : super(value)

    private val pending = AtomicBoolean(false)

    override fun observe(owner: LifecycleOwner, observer: Observer<in T>)
    {
        super.observe(owner, Observer {t->
            if (pending.compareAndSet(true, false))
                observer.onChanged(t)
        })
    }

    override fun setValue(value: T)
    {
        pending.set(true)
        super.setValue(value)
    }

}