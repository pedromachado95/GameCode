package br.com.faculdade.imepac

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class MainActivity : AppCompatActivity() {

    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar?.hide()

        // Recebe os dados do jogo que o usuário clicou na Vitrine
        val nomeJogo = intent.getStringExtra("NOME_JOGO") ?: "Erro"
        val precoJogo = intent.getStringExtra("PRECO_JOGO") ?: "R$ 0,00"

        // Atualiza a tela com as informações
        findViewById<TextView>(R.id.txt_resumo_jogo).text = nomeJogo
        findViewById<TextView>(R.id.txt_resumo_preco).text = precoJogo

        val btConfirmar = findViewById<Button>(R.id.bt_confirmar_pagamento)

        btConfirmar.setOnClickListener {
            Toast.makeText(this, "Verificando PIX...", Toast.LENGTH_SHORT).show()
            processarCompraNoFirebase(nomeJogo, precoJogo)
        }
    }

    private fun processarCompraNoFirebase(nomeJogo: String, preco: String) {
        val usuarioAtual = auth.currentUser
        if (usuarioAtual == null) return

        val keyGerada = gerarKeyAleatoria()

        val dadosCompra = hashMapOf(
            "id_usuario" to usuarioAtual.uid,
            "nome_jogo" to nomeJogo,
            "preco" to preco,
            "chave_ativacao" to keyGerada,
            "status" to "Disponível para Resgate"
        )

        db.collection("Compras").add(dadosCompra)
            .addOnSuccessListener {
                Toast.makeText(this, "Pagamento Aprovado! Key enviada para sua Biblioteca.", Toast.LENGTH_LONG).show()
                finish() // Fecha a tela de pagamento e volta para a loja
            }
            .addOnFailureListener {
                Toast.makeText(this, "Erro no pagamento.", Toast.LENGTH_SHORT).show()
            }
    }

    private fun gerarKeyAleatoria(): String {
        val caracteres = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789"
        fun randomString(tamanho: Int) = (1..tamanho).map { caracteres.random() }.joinToString("")
        return "${randomString(4)}-${randomString(4)}-${randomString(4)}"
    }
}