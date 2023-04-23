package com.example.etapa3

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ListView
import android.widget.Toast
import android.widget.Toast.LENGTH_SHORT
import android.widget.Toast.makeText
import androidx.activity.ComponentActivity
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase

class MainActivity : ComponentActivity() {

    private lateinit var nombre: EditText
    private lateinit var paterno: EditText
    private lateinit var materno: EditText
    private lateinit var mail: EditText
    private lateinit var agregar : ImageButton
    private lateinit var modificar : ImageButton
    private lateinit var eliminar : ImageButton
    private lateinit var buscar : ImageButton
    private lateinit var listaUsuarios: ListView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.main_layout)

        nombre = findViewById(R.id.Nombre)
        paterno = findViewById(R.id.Paterno)
        materno = findViewById(R.id.Materno)
        mail = findViewById(R.id.Mail)
        agregar = findViewById(R.id.Agregar)
        modificar = findViewById(R.id.Modificar)
        eliminar = findViewById(R.id.Eliminar)
        buscar = findViewById(R.id.Buscar)
        listaUsuarios = findViewById(R.id.LVDatos)

        botonAgregar()
        botonModicicar()
        botonEliminar()
        botonBuscar()
        listaPersonas()
    }

    private fun botonBuscar() {
        buscar.setOnClickListener {
            if (mail.text.toString().trim().isEmpty()){
                makeText(this@MainActivity.applicationContext, "Ingrese el mail a buscar.", LENGTH_SHORT).show()
            }else{
                val correo : String = mail.text.toString()
                val db : FirebaseDatabase = FirebaseDatabase.getInstance()
                val dbref : DatabaseReference = db.reference.child("Persona")
                val dbListener = object : ValueEventListener{
                    override fun onDataChange(snapshot: DataSnapshot) {
                        var respuesta = false
                        for(x : DataSnapshot in snapshot.children){
                            if(correo == x.child("mail").value.toString()){
                                respuesta = true
                                nombre.setText(x.child("nombre").value.toString())
                                paterno.setText(x.child("paterno").value.toString())
                                materno.setText(x.child("materno").value.toString())
                                mail.setText(x.child("mail").value.toString())
                                break
                            }
                        }
                        if(respuesta == false){
                            makeText(this@MainActivity.applicationContext, "El elemento no existe.", Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {

                    }

                }
                dbref.addListenerForSingleValueEvent(dbListener)
            }
        }
    }

    private fun botonAgregar() {
        agregar.setOnClickListener {
            if (nombre.text.toString().trim().isEmpty() ||
                paterno.text.toString().trim().isEmpty() ||
                materno.text.toString().trim().isEmpty() ||
                mail.text.toString().trim().isEmpty()
            ) {
                makeText(
                    this@MainActivity.applicationContext, "¡Complete los campos faltantes!",
                    LENGTH_SHORT
                ).show()
            } else {
                val nom: String = nombre.text.toString()
                val pat: String = paterno.text.toString()
                val mat: String = materno.text.toString()
                val correo: String = mail.text.toString()
                val persona = Persona(nom, pat, mat, correo)

                val database: DatabaseReference = Firebase.database.reference
                database.push().setValue(persona)
                nombre.setText("")
                paterno.setText("")
                materno.setText("")
                mail.setText("")
            }//Cierre de if/else
        }
    }

    private fun botonEliminar() {
        //TODO("Not yet implemented")
    }

    private fun botonModicicar() {
        //TODO("Not yet implemented")
    }



    private fun listaPersonas() {
        val database : FirebaseDatabase = FirebaseDatabase.getInstance()
        val dbReference : DatabaseReference = database.reference.child("Persona")
        val personas = ArrayList<Persona>()
        val adapter : ArrayAdapter<Persona> = ArrayAdapter(this@MainActivity.applicationContext, android.R.layout.simple_list_item_1, personas)

        val dbListener = object : ChildEventListener{
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                val persona : Persona? = snapshot.getValue<Persona>()
                if (persona != null) {
                    personas.add(persona)
                }
                adapter.notifyDataSetChanged()
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                adapter.notifyDataSetChanged()
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {
                //TODO("Not yet implemented")
            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
                //TODO("Not yet implemented")
            }

            override fun onCancelled(error: DatabaseError) {
                //TODO("Not yet implemented")
            }
        }
        dbReference.addChildEventListener(dbListener)
    }
}
