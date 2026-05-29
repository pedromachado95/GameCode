package br.com.faculdade.imepac

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class BibliotecaActivity : AppCompatActivity() {

    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_biblioteca)
        supportActionBar?.hide()

        buscarJogosComprados()
    }

    private fun buscarJogosComprados() {
        val usuarioAtual = auth.currentUser
        if (usuarioAtual == null) return

        val container = findViewById<LinearLayout>(R.id.container_lista_keys)

        // Busca apenas as compras vinculadas ao UID (Paginado com limite de 5 registros por lote)
        db.collection("Compras")
            .whereEqualTo("id_usuario", usuarioAtual.uid)
            .limit(5)
            .get()
            .addOnSuccessListener { documentos ->
                if (documentos.isEmpty) {
                    Toast.makeText(this, "Você ainda não comprou nenhum jogo.", Toast.LENGTH_SHORT).show()
                    return@addOnSuccessListener
                }

                // Para cada jogo comprado, cria um bloco na tela
                for (documento in documentos) {
                    val nomeJogo = documento.getString("nome_jogo") ?: "Jogo Desconhecido"
                    val status = documento.getString("status") ?: "Sem status"
                    val idCompra = documento.id

                    // Injeta o visual do card que criamos no Passo 1
                    val viewItem = LayoutInflater.from(this).inflate(R.layout.item_jogo_comprado, container, false)

                    val txtNome = viewItem.findViewById<TextView>(R.id.txt_nome_jogo_item)
                    val txtStatus = viewItem.findViewById<TextView>(R.id.txt_status_item)

                    txtNome.text = nomeJogo
                    txtStatus.text = status

                    // Prepara o clique para abrir a tela de Detalhes com a Key
                    viewItem.setOnClickListener {
                        val intent = Intent(this@BibliotecaActivity, DetalhesCompraActivity::class.java)
                        intent.putExtra("ID_COMPRA", idCompra) // Passa a ID secreta para a tela de detalhes
                        startActivity(intent)
                    }

                    container.addView(viewItem)
                }
            }
            .addOnFailureListener {
                Toast.makeText(this, "Erro ao carregar sua biblioteca.", Toast.LENGTH_SHORT).show()
            }
    }
}