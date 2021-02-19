package mx.cbisystems.x24

import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.TextView
import androidx.annotation.ColorRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import mx.cbisystems.x24.pagerController.ViewPagerAdapter


class TablayoutActivity : AppCompatActivity() {
    var tabLayout: TabLayout? = null // Ciontrolador de los botones de la barra inferior
    var viewPager: ViewPager2? = null // Visor del contenido que muestra cada botón
    val adapter by lazy { ViewPagerAdapter(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tablayout)

        //supportActionBar!!.hide() // Ocultar la barra superior

        tabLayout = findViewById(R.id.tabLayout)
        viewPager = findViewById(R.id.viewPager)

        // Configurar el viewPager
        viewPager!!.isUserInputEnabled = false // No permitir mover el viewpager deslizando
        viewPager!!.adapter = adapter

        tabLayout!!.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                // Sincronizar el tabLayout con el viewPager
                viewPager!!.currentItem = tab.position

                // Colorear de rojo el texto y el icono del tab seleccionado
                val tabView = tab.view.findViewById<TextView>(R.id.tab)
                tabView.setTextColor(ContextCompat.getColor(this@TablayoutActivity, R.color.hardRed))
                tabView.setDrawableColor(R.color.hardRed)
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {
                // Colorear de gris el texto y el icono del tab deseleccionado
                val tabView = tab.view.findViewById<TextView>(R.id.tab)
                tabView.setTextColor(ContextCompat.getColor(this@TablayoutActivity, R.color.gray))
                tabView.setDrawableColor(R.color.gray)
            }

            override fun onTabReselected(tab: TabLayout.Tab) {

            }
        })

        setupTabIcons()
    }

    override fun onStart() {
        super.onStart()

        // Preseleccionar tab (Si no se selecciona el dafault es cero)
        val tabToSelect = tabLayout!!.getTabAt(0)
        tabToSelect!!.select()
    }

    fun TextView.setDrawableColor(@ColorRes color: Int) {
        compoundDrawablesRelative.filterNotNull().forEach {
            it.colorFilter = PorterDuffColorFilter(ContextCompat.getColor(this.context, color), PorterDuff.Mode.SRC_IN)
        }
    }

    // Personalizar el contenido de los tab en el tabLayout
    private fun setupTabIcons() {
        val tabOne = LayoutInflater.from(this).inflate(R.layout.custom_tab, null) as TextView
        tabOne.text = "NOVEDADES"
        tabOne.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.favorite_menu_icon, 0, 0)
        tabLayout!!.getTabAt(0)!!.customView = tabOne

        val tabTwo = LayoutInflater.from(this).inflate(R.layout.custom_tab, null) as TextView
        tabTwo.text = "SUCURSALES"
        tabTwo.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.stores_menu_icon, 0, 0)
        tabLayout!!.getTabAt(1)!!.customView = tabTwo

        val tabThree = LayoutInflater.from(this).inflate(R.layout.custom_tab, null) as TextView
        tabThree.text = "MONEDERO"
        tabThree.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.profile_menu_icon, 0, 0)
        tabLayout!!.getTabAt(2)!!.customView = tabThree

        val tabFour = LayoutInflater.from(this).inflate(R.layout.custom_tab, null) as TextView
        tabFour.text = "PROMOCIONES"
        tabFour.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.promo_menu_icon, 0, 0)
        tabLayout!!.getTabAt(3)!!.customView = tabFour
    }
}