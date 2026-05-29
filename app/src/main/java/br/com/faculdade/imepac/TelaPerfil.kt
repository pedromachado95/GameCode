package br.com.faculdade.imepac

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class TelaPerfil : AppCompatActivity() {

    private lateinit var emailUser: EditText
    private lateinit var usuarioUser: EditText
    private lateinit var btSair: Button
    private lateinit var btDeletar: Button
    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tela_perfil)
        supportActionBar?.hide()

        db = FirebaseFirestore.getInstance()
        iniciarComponentes()

        btSair.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            irParaLogin()
        }

        btDeletar.setOnClickListener {
            deletarContaInteira()
        }
    }

    override fun onStart() {
        super.onStart()
        val userEmail = FirebaseAuth.getInstance().currentUser?.email
        if (userEmail != null) {
            emailUser.setText(userEmail)
            buscarNomeDoEmail(userEmail)
        }
    }

    private fun buscarNomeDoEmail(email: String) {
        db.collection("Usuarios").whereEqualTo("email", email).get()
            .addOnSuccessListener { querySnapshot ->
                if (!querySnapshot.isEmpty) {
                    val nome = querySnapshot.documents[0].getString("nome")
                    usuarioUser.setText(nome ?: "Sem nome")
                }
            }
    }

    private fun deletarContaInteira() {
        val user = FirebaseAuth.getInstance().currentUser
        if (user != null) {
            // 1. Apaga dados do Firestore
            db.collection("Usuarios").whereEqualTo("email", user.email).get()
                .addOnSuccessListener { querySnapshot ->
                    for (document in querySnapshot) {
                        document.reference.delete()
                    }

                    // 2. Apaga usuário do Auth
                    user.delete().addOnSuccessListener {
                        Toast.makeText(this, "Conta deletada com sucesso!", Toast.LENGTH_LONG).show()
                        irParaLogin()
                    }.addOnFailureListener {
                        Toast.makeText(this, "Erro ao excluir login.", Toast.LENGTH_SHORT).show()
                    }
                }
        }
    }

    private fun irParaLogin() {
        val intent = Intent(this, FormLogin::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }

    private fun iniciarComponentes() {
        emailUser = findViewById(R.id.textEmailUser)
        usuarioUser = findViewById(R.id.textNomeUser)
        btSair = findViewById(R.id.bt_sair)
        btDeletar = findViewById(R.id.bt_deletar_conta)
    }
}