package br.com.faculdade.imepac

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.QuerySnapshot

class TelaPerfil : AppCompatActivity() {

    private lateinit var emailUser: EditText
    private lateinit var usuarioUser: EditText
    private lateinit var btSair: Button
    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tela_perfil)

        supportActionBar?.hide()

        iniciarComponentes()

        db = FirebaseFirestore.getInstance()
        fetchAllNames()

        btSair.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            val intent = Intent(this, FormLogin::class.java)
            startActivity(intent)
            finish()
        }
    }

    override fun onStart() {
        super.onStart()

        val userEmail = FirebaseAuth.getInstance().currentUser?.email
        emailUser.setText(userEmail)

        if (userEmail != null) {
            buscarNomeDoEmail(userEmail)
        }
    }

    private fun buscarNomeDoEmail(email: String) {
        val usuariosRef = db.collection("Usuarios")
        val query = usuariosRef.whereEqualTo("email", email)

        query.get()
            .addOnSuccessListener { querySnapshot: QuerySnapshot ->
                if (!querySnapshot.isEmpty) {
                    // Usando .get(0) em vez de para garantir 100% que é um item único
                    val documento: DocumentSnapshot = querySnapshot.documents.get(0)
                    val nome: String? = documento.getString("nome")

                    if (nome != null) {
                        usuarioUser.setText(nome)
                    } else {
                        println("Nome não encontrado para o e-mail $email")
                    }
                } else {
                    println("Nenhum documento encontrado para o e-mail $email")
                }
            }
            .addOnFailureListener { e ->
                println("Erro ao buscar documento: $e")
            }
    }

    private fun fetchAllNames() {
        val usuariosRef = db.collection("Usuarios")
        usuariosRef.get().addOnSuccessListener { querySnapshot: QuerySnapshot ->
            for (doc in querySnapshot.documents) {
                val documento: DocumentSnapshot = doc
                val nome: String? = documento.getString("nome")
                println("Nome: $nome")
            }
        }.addOnFailureListener { exception ->
            println("Erro ao buscar os nomes: ${exception.message}")
        }
    }

    private fun iniciarComponentes() {
        emailUser = findViewById(R.id.textEmailUser)
        usuarioUser = findViewById(R.id.textNomeUser)
        btSair = findViewById(R.id.bt_sair)
    }
}