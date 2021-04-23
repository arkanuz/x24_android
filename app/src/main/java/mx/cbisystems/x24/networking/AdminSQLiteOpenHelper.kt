package mx.cbisystems.x24.networking

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import mx.cbisystems.x24.entities.MUser

class AdminSQLiteOpenHelper(var context: Context) : SQLiteOpenHelper(context, "x24_DB", null, 1){
    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL("create table user(user_id int, name text, lastName text, email text, phone text, idProvider int, birthDate text, sex int, userName text, password text)")
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {

    }

    fun saveUser(user: MUser){
        //val admin = AdminSQLiteOpenHelper(this)
        val bd = this.writableDatabase
        val registro = ContentValues()

        registro.put("user_id", user.user_id)
        registro.put("name", user.name)
        registro.put("lastName", user.lastName)
        registro.put("email", user.email)
        registro.put("phone",user.phone)
        registro.put("idProvider", user.idProvider)
        registro.put("birthDate", user.birthDate)
        registro.put("sex", user.sex)
        if (user.userName == null){
            registro.put("userName", " ")
        } else {
            registro.put("userName", user.userName)
        }
        registro.put("password", user.password)

        val count = bd.insert("user", null, registro)
        bd.close()
        if (count == (0).toLong()){
            Log.i("UserDB", "error al guardar usuario")
        } else {
            Log.i("UserDB", "user guardado")
        }
        bd.close()
    }

    fun getUser(): MUser?{
        //val admin = AdminSQLiteOpenHelper(this)
        val bd = this.writableDatabase
        val registro = ContentValues()

        val query = "Select * from user"
        val result = bd.rawQuery(query, null)
        var user: MUser? = null
        if (result.moveToFirst()) {
            do {
                Log.i("UserDB", "usuario encontrado")
                user = MUser()

                user!!.user_id = result.getInt(result.getColumnIndex("user_id"))
                user!!.name = result.getString(result.getColumnIndex("name"))
                user!!.lastName = result.getString(result.getColumnIndex("lastName"))
                user!!.email = result.getString(result.getColumnIndex("email"))
                user!!.phone = result.getString(result.getColumnIndex("phone"))
                user!!.idProvider = result.getInt(result.getColumnIndex("idProvider"))
                user!!.birthDate = result.getString(result.getColumnIndex("birthDate"))
                user!!.sex = result.getInt(result.getColumnIndex("sex"))
                user!!.userName = result.getString(result.getColumnIndex("userName"))
                user!!.password = result.getString(result.getColumnIndex("password"))

                Log.i("UserDB", "usuario guardado para mostrar")
            }
            while (result.moveToNext())
        }
        bd.close()

        return user
    }

    fun deleteUser(){
        //val admin = AdminSQLiteOpenHelper(this)
        val bd = this.writableDatabase
        val registro = ContentValues()

        bd.delete("user", null, null)
        bd.close()
    }
}