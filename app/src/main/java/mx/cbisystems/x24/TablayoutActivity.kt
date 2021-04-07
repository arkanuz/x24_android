package mx.cbisystems.x24

import android.content.Intent
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.widget.TextView
import androidx.annotation.ColorRes
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.navigation.NavigationView
import com.google.android.material.tabs.TabLayout
import mx.cbisystems.x24.pagerController.ViewPagerAdapter

class TablayoutActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    var tabLayout: TabLayout? = null // Controlador de los botones de la barra inferior
    var viewPager: ViewPager2? = null // Visor del contenido que muestra cada botón
    val adapter by lazy { ViewPagerAdapter(this) }
    var drawerLayout: DrawerLayout? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tablayout)
        // Ocultar la barra de navegación principal para usar la custom
        actionBar?.hide()

        // Activar la barra de navegación custom
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        drawerLayout = findViewById(R.id.drawer_layout)
        val toggle = ActionBarDrawerToggle(this, drawerLayout, findViewById(R.id.toolbar), 0, 0)
        drawerLayout!!.addDrawerListener(toggle)
        toggle.syncState()

        // Activar el menu lateral
        val navView: NavigationView = findViewById(R.id.navigation_view)
        navView.setNavigationItemSelectedListener(this)

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
                tabView.setTextColor(
                    ContextCompat.getColor(
                        this@TablayoutActivity,
                        R.color.hardRed
                    )
                )
                tabView.setDrawableColor(R.color.hardRed)

                if (tab.position == 0) {
                    val favoriteFragment: FavoriteFragment =
                        supportFragmentManager.findFragmentByTag(
                            "f0"
                        ) as FavoriteFragment
                    favoriteFragment.downloadFavorites()
                } else if (tab.position == 3) {
                    supportFragmentManager.executePendingTransactions()
                    //val fragment = supportFragmentManager

                    //val promosFragment: Fragment? = fragment.findFragmentByTag("f3")
                    //promosFragment.downloadPromos()
                }
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

        // Preseleccionar tab (Si no se selecciona el default es cero)
        val tabToSelect = tabLayout!!.getTabAt(2)
        tabToSelect!!.select()
    }

    // Se interviene el botón de back para impedir la acción back
    override fun onBackPressed() {
        // Esta parte hace que el botón back oculte el menú
        if (drawerLayout!!.isDrawerOpen(GravityCompat.START)) {
            drawerLayout!!.closeDrawer(GravityCompat.START)
        }
    }

    fun TextView.setDrawableColor(@ColorRes color: Int) {
        compoundDrawablesRelative.filterNotNull().forEach {
            it.colorFilter = PorterDuffColorFilter(
                ContextCompat.getColor(this.context, color),
                PorterDuff.Mode.SRC_IN
            )
        }
    }

    // Personalizar el contenido de los tab en el tabLayout (ícono y texto)
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

    // Acción al seleccionar algún elemento del menú lateral
    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        val id = item.itemId

        when (id) {
            R.id.nav_profile -> {
                val tabToSelect = tabLayout!!.getTabAt(2)
                tabToSelect!!.select()
            }

            R.id.nav_myQR -> {
                val intent = Intent(this, QRActivity::class.java)
                startActivity(intent)
            }

            R.id.nav_balance -> {
                val tabToSelect = tabLayout!!.getTabAt(2)
                tabToSelect!!.select()
            }

            R.id.nav_locations -> {
                val tabToSelect = tabLayout!!.getTabAt(1)
                tabToSelect!!.select()
            }
        }

        drawerLayout?.closeDrawer(GravityCompat.START)

        return true
    }
}