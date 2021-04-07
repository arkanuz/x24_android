package mx.cbisystems.x24.networking

import android.app.Activity
import android.app.AlertDialog
import mx.cbisystems.x24.R

class LoadingDialog constructor(myActivity: Activity) {
    private var activity : Activity? = null
    private var dialog: AlertDialog? = null

    init {
        activity = myActivity
    }


    fun showLoading(){

        val builder = AlertDialog.Builder(activity)

        val inflater = activity?.layoutInflater
        builder.setView(inflater?.inflate(R.layout.fragment_loading, null))
        builder.setCancelable(true)

        this.dialog = builder.create()
        this.dialog?.show()
    }

    fun hideLoading(){
        this.dialog?.dismiss()
    }
}